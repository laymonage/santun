package id.ac.ui.cs.mobileprogramming.sage.santun.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import id.ac.ui.cs.mobileprogramming.sage.santun.model.Message
import org.joda.time.format.DateTimeFormat

class MainViewModel : ViewModel() {
    val query = FirebaseDatabase.getInstance().getReference("messages")
    val message = MutableLiveData<Message>()

    fun getFormattedTimestamp(): String? {
        return message.value?.let {
            DateTimeFormat.mediumDateTime().print(it.timestamp)
        }
    }
}
