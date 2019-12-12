package id.ac.ui.cs.mobileprogramming.sage.santun.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.ac.ui.cs.mobileprogramming.sage.santun.model.Message
import id.ac.ui.cs.mobileprogramming.sage.santun.util.data.compress
import id.ac.ui.cs.mobileprogramming.sage.santun.util.data.toJson
import org.joda.time.format.DateTimeFormat

class MainViewModel : ViewModel() {
    val message = MutableLiveData<Message>()

    fun getFormattedTimestamp(): String? {
        return message.value?.let {
            DateTimeFormat.mediumDateTime().print(it.timestamp)
        }
    }

    fun getJsonMessage(): String {
        return toJson(message.value!!)
    }

    fun getCompressedJsonMessage(): ByteArray {
        return compress(getJsonMessage())
    }
}
