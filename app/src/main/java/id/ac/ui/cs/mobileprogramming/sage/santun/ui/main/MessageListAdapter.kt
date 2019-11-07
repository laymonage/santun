package id.ac.ui.cs.mobileprogramming.sage.santun.ui.main

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.ac.ui.cs.mobileprogramming.sage.santun.R
import id.ac.ui.cs.mobileprogramming.sage.santun.model.Message
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class MessageListAdapter internal constructor(private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<MessageListAdapter.MessageViewHolder>() {

    private var messages = emptyList<Message>()

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val senderView: TextView = itemView.senderText
        private val receiverView: TextView = itemView.receiverText
        private val messageView: TextView = itemView.messageText
        private val imageCard: ImageView = itemView.imageCard

        fun bind(message: Message, clickListener: OnItemClickListener) {
            senderView.text = message.sender
            receiverView.text = message.receiver
            messageView.text = message.message
            if (message.imageUri != null) {
                imageCard.setImageURI(Uri.parse(message.imageUri))
            }
            itemView.setOnClickListener {
                clickListener.onItemClicked(message)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return MessageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val current = messages[position]
        holder.bind(current, itemClickListener)
    }

    internal fun setMessages(messages: List<Message>) {
        this.messages = messages
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}

interface OnItemClickListener {
    fun onItemClicked(message: Message)
}
