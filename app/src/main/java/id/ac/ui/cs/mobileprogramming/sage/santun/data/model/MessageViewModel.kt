package id.ac.ui.cs.mobileprogramming.sage.santun.data.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import id.ac.ui.cs.mobileprogramming.sage.santun.util.data.Compression.Companion.compress
import id.ac.ui.cs.mobileprogramming.sage.santun.util.data.toJson
import kotlinx.coroutines.launch

class MessageViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MessageRepository
    val allMessages: LiveData<List<Message>>

    init {
        val messageDao = MessageRoomDatabase.getDatabase(application).messageDao()
        repository = MessageRepository(messageDao)
        allMessages = repository.allMessages
    }

    fun insert(message: Message) = viewModelScope.launch {
        repository.insert(message)
    }

    fun getCompressedJsonMessageList(): String? {
        allMessages.value?.let {
            return compress(toJson(MessageList(it)))
        }
        return null
    }
}
