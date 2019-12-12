package id.ac.ui.cs.mobileprogramming.sage.santun.model

import org.joda.time.DateTime

data class Message(
    val sender: String = "",
    val receiver: String = "",
    val message: String = "",
    val imageUri: String? = null,
    val timestamp: Long = DateTime.now().millis
)
