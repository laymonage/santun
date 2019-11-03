package id.ac.ui.cs.mobileprogramming.sage.santun.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.ac.ui.cs.mobileprogramming.sage.santun.model.Message

class MainViewModel : ViewModel() {
    val message = MutableLiveData<Message>()
}
