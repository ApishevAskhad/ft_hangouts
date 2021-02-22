package ru.shcool21.gloras.ft_hangouts.ui.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.shcool21.gloras.ft_hangouts.R
import ru.shcool21.gloras.ft_hangouts.data.entity.Message

internal class ChatAdapter : RecyclerView.Adapter<ChatVH>() {

    companion object {
        private const val INCOMING_MESSAGE_TYPE = 0
        private const val OUTCOMING_MESSAGE_TYPE = 1
    }

    private val messages = mutableListOf<Message>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatVH {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            INCOMING_MESSAGE_TYPE -> {
                IncomingMessageVH(inflater.inflate(R.layout.item_chat_message_incoming, parent, false))
            }
            OUTCOMING_MESSAGE_TYPE -> {
                OutcomingMessageVH(inflater.inflate(R.layout.item_chat_message_outcoming, parent, false))
            }
            else -> throw IllegalArgumentException("illegal viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (!messages[position].isIncoming) {
            INCOMING_MESSAGE_TYPE
        } else {
            OUTCOMING_MESSAGE_TYPE
        }
    }

    override fun onBindViewHolder(holder: ChatVH, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount() = messages.size

    fun updateMessages(list: List<Message>) {
        messages.clear()
        messages.addAll(list)
        notifyDataSetChanged()
    }
}

internal abstract class ChatVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(message: Message)
}

internal class IncomingMessageVH(itemView: View) : ChatVH(itemView) {
    override fun bind(message: Message) {
        with(itemView) {
            findViewById<TextView>(R.id.messageBodyTextView).text = message.body
        }
    }

}

internal class OutcomingMessageVH(itemView: View) : ChatVH(itemView) {
    override fun bind(message: Message) {
        with(itemView) {
            findViewById<TextView>(R.id.messageBodyTextView).text = message.body
        }
    }

}