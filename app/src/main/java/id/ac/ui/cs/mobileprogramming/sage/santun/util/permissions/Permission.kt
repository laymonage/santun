package id.ac.ui.cs.mobileprogramming.sage.santun.util.permissions

import android.app.Activity
import androidx.fragment.app.Fragment

interface Permission {
    fun verifyPermission(activity: Activity): Boolean
    fun requestPermission(fragment: Fragment)
}
