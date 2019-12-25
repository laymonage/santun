package id.ac.ui.cs.mobileprogramming.sage.santun.data.model

import androidx.lifecycle.LiveData

class MessageRepository(private val messageDao: MessageDao) {
    val allMessages: LiveData<List<Message>> = messageDao.getAllMessages()

    suspend fun insert(message: Message) {
        messageDao.insert(message)
    }
}
