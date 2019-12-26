package id.ac.ui.cs.mobileprogramming.sage.santun.util.permissions

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class Camera : Permission {
    companion object {
        const val REQUEST_CAMERA = 1
        const val PERMISSION = Manifest.permission.CAMERA
    }

    override fun verifyPermission(activity: Activity): Boolean {
        val grant = ContextCompat.checkSelfPermission(activity, PERMISSION)
        return grant == PackageManager.PERMISSION_GRANTED
    }

    override fun requestPermission(fragment: Fragment) {
        fragment.requestPermissions(
            arrayOf(PERMISSION), REQUEST_CAMERA
        )
    }
}
