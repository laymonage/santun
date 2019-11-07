package id.ac.ui.cs.mobileprogramming.sage.santun.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.ac.ui.cs.mobileprogramming.sage.santun.model.Message
import org.joda.time.format.DateTimeFormat

class MainViewModel : ViewModel() {
    val message = MutableLiveData<Message>()

    fun getFormattedTimestamp(): String {
        return DateTimeFormat.mediumDateTime().print(message.value!!.timestamp)
    }
}
