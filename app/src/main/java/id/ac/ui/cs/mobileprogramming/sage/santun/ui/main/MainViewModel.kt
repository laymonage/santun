package id.ac.ui.cs.mobileprogramming.sage.santun.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.ui.cs.mobileprogramming.sage.santun.model.Message
import id.ac.ui.cs.mobileprogramming.sage.santun.util.data.toJson
import id.ac.ui.cs.mobileprogramming.sage.santun.util.storage.requestFilePath
import id.ac.ui.cs.mobileprogramming.sage.santun.util.storage.writeStringToFile
import kotlinx.coroutines.launch
import org.joda.time.format.DateTimeFormat

class MainViewModel : ViewModel() {
    val message = MutableLiveData<Message>()

    fun onMessageSave(fragment: Fragment) {
        val message = message.value!!
        val fileName = "${message.id.toString()}.json"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            requestFilePath(
                fragment, "application/json", fileName
            )
        } else {
            viewModelScope.launch {
                writeStringToFile(fragment.activity!!, toJson(message), fileName)
            }
        }
    }

    fun onMessageFileUriReceived(activity: Activity, data: Intent) {
        viewModelScope.launch {
            writeStringToFile(activity, toJson(message.value!!), data.data!!)
        }
    }

    fun getFormattedTimestamp(): String {
        return DateTimeFormat.mediumDateTime().print(message.value!!.timestamp)
    }
}
