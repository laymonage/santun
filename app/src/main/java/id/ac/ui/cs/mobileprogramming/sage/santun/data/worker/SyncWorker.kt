package id.ac.ui.cs.mobileprogramming.sage.santun.data.worker

import android.content.Context
import androidx.lifecycle.Observer
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import id.ac.ui.cs.mobileprogramming.sage.santun.data.model.Message
import id.ac.ui.cs.mobileprogramming.sage.santun.data.model.MessageRoomDatabase
import id.ac.ui.cs.mobileprogramming.sage.santun.data.remote.APIWise
import id.ac.ui.cs.mobileprogramming.sage.santun.data.remote.SyncRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class SyncWorker(applicationContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(applicationContext, workerParams) {

    private val messageDao = MessageRoomDatabase
        .getDatabase(applicationContext, CoroutineScope(Dispatchers.IO)).messageDao()
    private val service = APIWise.getAPIService()

    override suspend fun doWork(): Result {
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
}
