package id.ac.ui.cs.mobileprogramming.sage.santun.data.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDao {

    @Query("SELECT * FROM message_table ORDER BY timestamp DESC")
    fun getAllMessages(): LiveData<List<Message>>

    @Query("SELECT * FROM message_table ORDER BY timestamp DESC")
    fun getAllCurrentMessages(): List<Message>

    @Insert
    suspend fun insert(message: Message)

    @Query("DELETE FROM message_table")
    suspend fun deleteAll()
}