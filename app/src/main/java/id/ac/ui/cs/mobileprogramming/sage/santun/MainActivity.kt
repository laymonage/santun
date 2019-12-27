package id.ac.ui.cs.mobileprogramming.sage.santun

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.ac.ui.cs.mobileprogramming.sage.santun.ui.main.DetailFragment
import id.ac.ui.cs.mobileprogramming.sage.santun.ui.main.MainFragment
import id.ac.ui.cs.mobileprogramming.sage.santun.util.connection.ConnectivityChangeReceiver

class MainActivity : AppCompatActivity() {
    private val connectivityChangeReceiver = ConnectivityChangeReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        createNotificationChannel()
        connectivityChangeReceiver.register(this)
        val isTablet = resources.getBoolean(R.bool.is_tablet)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                if (isTablet) {
                    add(R.id.main_fragment, MainFragment.newInstance())
                    add(R.id.detail_fragment, DetailFragment.newInstance())
                } else {
                    replace(R.id.container, MainFragment.newInstance())
                }
                commitNow()
            }
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.sync_notification_channel_name)
            val descriptionText = getString(R.string.sync_notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(MainFragment.CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}
