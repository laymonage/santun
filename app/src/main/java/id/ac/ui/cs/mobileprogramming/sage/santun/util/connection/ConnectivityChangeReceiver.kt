package id.ac.ui.cs.mobileprogramming.sage.santun.util.connection

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.widget.Toast
import id.ac.ui.cs.mobileprogramming.sage.santun.R

class ConnectivityChangeReceiver : BroadcastReceiver() {

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
        if (ConnectionIdentifier.getConnectionType(context) == ConnectionIdentifier.CONNECTION_NONE) {
            makeToast(context)
        }
    }
}
