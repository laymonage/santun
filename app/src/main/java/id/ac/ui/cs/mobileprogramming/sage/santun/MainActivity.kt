package id.ac.ui.cs.mobileprogramming.sage.santun

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.ac.ui.cs.mobileprogramming.sage.santun.ui.main.MainFragment
import id.ac.ui.cs.mobileprogramming.sage.santun.util.broadcast.ConnectivityChangeReceiver

class MainActivity : AppCompatActivity() {
    private val connectivityChangeReceiver = ConnectivityChangeReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        connectivityChangeReceiver.register(this)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

}
