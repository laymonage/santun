package id.ac.ui.cs.mobileprogramming.sage.santun.ui.compose

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ComposeViewModel : ViewModel() {
    val sender = MutableLiveData<String>()
    val receiver = MutableLiveData<String>()
    val message = MutableLiveData<String>()
}
