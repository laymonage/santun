package id.ac.ui.cs.mobileprogramming.sage.santun.ui.compose

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import id.ac.ui.cs.mobileprogramming.sage.santun.model.Message
import id.ac.ui.cs.mobileprogramming.sage.santun.model.MessageViewModel
import id.ac.ui.cs.mobileprogramming.sage.santun.util.storage.copyFileToAppDir
import kotlinx.coroutines.launch

class ComposeViewModel : ViewModel() {

    val sender = MutableLiveData<String>()
    val receiver = MutableLiveData<String>()
    val message = MutableLiveData<String>()
    val imageUri = MutableLiveData<Uri>()

    fun saveMessage(fragment: Fragment, viewModel: MessageViewModel) {
        viewModelScope.launch {
            val item: Message
            item = if (imageUri.value != null) {
                val newUri = copyFileToAppDir(fragment, imageUri.value!!).toUri()
                Message(
                    null, sender.value!!, receiver.value!!, message.value!!, newUri.toString()
                )
            } else {
                Message(null, sender.value!!, receiver.value!!, message.value!!)
            }
            viewModel.insert(item)
        }
    }

    fun messageIsValid(): Boolean {
        return !sender.value.isNullOrBlank() && !receiver.value.isNullOrBlank()
                && !message.value.isNullOrBlank()
    }
}
