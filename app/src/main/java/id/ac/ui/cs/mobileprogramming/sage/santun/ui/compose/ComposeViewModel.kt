package id.ac.ui.cs.mobileprogramming.sage.santun.ui.compose

import androidx.lifecycle.*
import id.ac.ui.cs.mobileprogramming.sage.santun.model.Message

class ComposeViewModel : ViewModel() {

    val sender = MutableLiveData<String>()
    val receiver = MutableLiveData<String>()
    val message = MutableLiveData<String>()

    fun getMessage(): Message {
        return Message(null, sender.value!!, receiver.value!!, message.value!!)
    }

    fun messageIsValid(): Boolean {
        return sender.value != null && receiver.value != null && message.value != null
    }
}
