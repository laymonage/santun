package id.ac.ui.cs.mobileprogramming.sage.santun.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message_table")
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    val sender: String,
    val receiver: String,
    val message: String
)
