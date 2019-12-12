package id.ac.ui.cs.mobileprogramming.sage.santun.ui.compose

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import id.ac.ui.cs.mobileprogramming.sage.santun.model.Message

class ComposeViewModel : ViewModel() {
    companion object {
        val MESSAGE_REF = FirebaseDatabase.getInstance().getReference("messages")
    }

    val sender = MutableLiveData<String>()
    val receiver = MutableLiveData<String>()
    val message = MutableLiveData<String>()
    val imageUri = MutableLiveData<Uri>()

    fun messageIsValid(): Boolean {
        return !sender.value.isNullOrBlank() && !receiver.value.isNullOrBlank()
                && !message.value.isNullOrBlank()
    }

    fun save(message: Message): Task<Void> {
        return save(message, MESSAGE_REF.push().key.toString())
    }

    fun save(message: Message, id: String): Task<Void> {
        return MESSAGE_REF.child(id).setValue(message)
    }
}
