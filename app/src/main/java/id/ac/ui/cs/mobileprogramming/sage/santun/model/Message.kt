package id.ac.ui.cs.mobileprogramming.sage.santun.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime
import java.util.UUID

@Entity(tableName = "message_table")
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "sender") val sender: String,
    @ColumnInfo(name = "receiver") val receiver: String,
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "image_uri") val imageUri: String? = null,
    @ColumnInfo(name = "timestamp") val timestamp: Long = DateTime.now().millis,
    @ColumnInfo(name = "uuid") val uuid: String = UUID.randomUUID().toString()
)
