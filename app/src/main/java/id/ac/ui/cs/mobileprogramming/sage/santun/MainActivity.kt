package id.ac.ui.cs.mobileprogramming.sage.santun

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.ac.ui.cs.mobileprogramming.sage.santun.ui.main.DetailFragment
import id.ac.ui.cs.mobileprogramming.sage.santun.ui.main.MainFragment
import id.ac.ui.cs.mobileprogramming.sage.santun.util.broadcast.ConnectivityChangeReceiver

class MainActivity : AppCompatActivity() {
    private val connectivityChangeReceiver = ConnectivityChangeReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
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

}
