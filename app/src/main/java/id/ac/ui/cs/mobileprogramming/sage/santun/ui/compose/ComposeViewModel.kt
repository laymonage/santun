package id.ac.ui.cs.mobileprogramming.sage.santun.ui.compose

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ComposeViewModel : ViewModel() {

    val sender = MutableLiveData<String>()
    val receiver = MutableLiveData<String>()
    val message = MutableLiveData<String>()
    val imageUri = MutableLiveData<Uri>()

    fun messageIsValid(): Boolean {
        return !sender.value.isNullOrBlank() && !receiver.value.isNullOrBlank()
                && !message.value.isNullOrBlank()
    }
}
