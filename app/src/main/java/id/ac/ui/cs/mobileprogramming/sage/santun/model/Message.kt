package id.ac.ui.cs.mobileprogramming.sage.santun.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message_table")
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "sender") val sender: String,
    @ColumnInfo(name = "receiver") val receiver: String,
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "image_uri") val imageUri: String? = null
)
