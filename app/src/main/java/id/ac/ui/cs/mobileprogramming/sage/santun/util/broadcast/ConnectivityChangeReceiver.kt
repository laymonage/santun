package id.ac.ui.cs.mobileprogramming.sage.santun.util.broadcast

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import id.ac.ui.cs.mobileprogramming.sage.santun.R

class ConnectivityChangeReceiver : BroadcastReceiver() {
    companion object {
        private const val CONNECTION_NONE = 0
        private const val CONNECTION_WIFI = 1
        private const val CONNECTION_CELLULAR = 2
    }

    private fun makeToast(context: Context?) {
        Toast.makeText(context, R.string.no_connection_message, Toast.LENGTH_LONG).show()
    }

    fun register(activity: Activity) {
        val connectivityManager = activity.
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.
                registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                    override fun onLost(network: Network?) {
                        makeToast(activity)
                    }
                })
        } else {
            activity.registerReceiver(
                this, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (getConnectionType(context) == CONNECTION_NONE) {
            makeToast(context)
        }
    }

    private fun getConnectionType(context: Context?): Int {
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
}
