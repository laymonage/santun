package id.ac.ui.cs.mobileprogramming.sage.santun.util.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import id.ac.ui.cs.mobileprogramming.sage.santun.R

class ConnectionIdentifier {
    companion object {
        const val CONNECTION_NONE = 0
        const val CONNECTION_WIFI = 1
        const val CONNECTION_CELLULAR = 2

        fun getConnectionType(context: Context?): Int {
            val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cm?.run {
                    cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                        if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                            return CONNECTION_WIFI
                        } else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                            return CONNECTION_CELLULAR
                        }
                    }
                }
            } else {
                cm?.run {
                    cm.activeNetworkInfo?.run {
                        if (type == ConnectivityManager.TYPE_WIFI) {
                            return CONNECTION_WIFI
                        } else if (type == ConnectivityManager.TYPE_MOBILE) {
                            return CONNECTION_CELLULAR
                        }
                    }
                }
            }
            return CONNECTION_NONE
        }

        fun ensureInternetConnection(context: Context?): Boolean {
            if (getConnectionType(context) == CONNECTION_NONE) {
                Toast.makeText(context, R.string.connect_internet_hint, Toast.LENGTH_LONG).show()
                return false
            }
            return true
        }
    }
}