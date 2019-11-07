package id.ac.ui.cs.mobileprogramming.sage.santun

import android.app.Application
import net.danlew.android.joda.JodaTimeAndroid

class Santun : Application() {
    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
    }
}
