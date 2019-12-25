package id.ac.ui.cs.mobileprogramming.sage.santun.data.remote

import id.ac.ui.cs.mobileprogramming.sage.santun.data.model.Message
import kotlin.math.roundToLong

data class MessageBody (
    val id: String,
    val sender: String,
    val receiver: String,
    val message: String,
    val image_uri: String? = null,
    val timestamp: Double
) {
    constructor(message: Message) : this(
        message.uuid, message.sender, message.receiver, message.message,
        message.imageUri, message.timestamp / 1000.0
    )

    fun toMessage() = Message(
        null, sender, receiver, message, image_uri, (timestamp * 1000).roundToLong(), id
    )
}
