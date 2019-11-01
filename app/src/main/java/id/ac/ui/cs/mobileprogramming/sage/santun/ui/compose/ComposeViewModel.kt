package id.ac.ui.cs.mobileprogramming.sage.santun.ui.compose

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ComposeViewModel : ViewModel() {
    val from = MutableLiveData<String>()
    val to = MutableLiveData<String>()
    val message = MutableLiveData<String>()
}
