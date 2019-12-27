package id.ac.ui.cs.mobileprogramming.sage.santun.data.worker

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import id.ac.ui.cs.mobileprogramming.sage.santun.MainActivity
import id.ac.ui.cs.mobileprogramming.sage.santun.R
import id.ac.ui.cs.mobileprogramming.sage.santun.data.model.MessageRoomDatabase
import id.ac.ui.cs.mobileprogramming.sage.santun.data.remote.APIWise
import id.ac.ui.cs.mobileprogramming.sage.santun.data.remote.SyncRequest
import id.ac.ui.cs.mobileprogramming.sage.santun.ui.main.MainFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class SyncWorker(applicationContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(applicationContext, workerParams) {

    private val messageDao = MessageRoomDatabase
        .getDatabase(applicationContext).messageDao()
    private val service = APIWise.getAPIService()

    override suspend fun doWork(): Result {
        notifySyncComplete()
        return if (sync()) Result.success() else Result.failure()
    }

    private suspend fun sync(): Boolean {
        val messages = messageDao.getAllCurrentMessages()
        val messageIds = messages.map { it.uuid }
        val result = service.sync(SyncRequest(messageIds))
        if (result.isSuccessful) {
            result.body()!!.messages.forEach {
                if (!it.image_uri.isNullOrBlank()) {
                    downloadImage(it.id)
                }
                messageDao.insert(it.toMessage())
            }
            return true
        }
        return false
    }

    private suspend fun downloadImage(uuid: String) {
        val response = service.getImage(APIWise.STATIC_URL + uuid)
        val file = File(applicationContext.getExternalFilesDir(null), uuid)
        withContext(Dispatchers.IO) {
            response.body()?.byteStream()?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
    }

    private fun notifySyncComplete() {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        val builder = NotificationCompat.Builder(applicationContext, MainFragment.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(applicationContext.getString(R.string.sync_notification_title))
            .setContentText(applicationContext.getText(R.string.sync_notification_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(MainFragment.SYNC_NOTIFICATION_ID, builder.build())
        }
    }
}
