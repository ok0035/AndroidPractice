package com.zerodeg.chatrecyclerview.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zerodeg.chatrecyclerview.R

class ChatAdapter(iChat: ChatInterface) : RecyclerView.Adapter<ChatViewHolder>() {

    var modelList : ArrayList<ChatModel>? = null
    private val chatInterface:ChatInterface = iChat

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_item, parent, false),
            chatInterface)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(this.modelList!![position])
    }

    override fun getItemCount(): Int {
        return modelList!!.size
    }

    fun submitList(list : ArrayList<ChatModel>) {
        modelList = list
    }
}