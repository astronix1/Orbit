package com.example.myapp.com.example.myapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.R
import com.google.firebase.auth.FirebaseAuth

class msg_adapter(val context: Context, val msgList: ArrayList<msg>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    class sentvh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentmsg= itemView.findViewById<TextView>(R.id.sent)
    }
    class receivevh(itemView: View): RecyclerView.ViewHolder(itemView) {
        val receivemsg = itemView.findViewById<TextView>(R.id.receive)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1){
            val view = LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false)
            return receivevh(view)
        }
        else {
            val view = LayoutInflater.from(context).inflate(R.layout.sent_chat_item, parent, false)
            return sentvh(view)
        }
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentmsg = msgList[position]
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentmsg.senderId)){
            return ITEM_SENT
        }
        else {
            return ITEM_RECEIVE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMsg = msgList[position]
        if (holder.javaClass == sentvh::class.java){
            val vh = holder as sentvh
            holder.sentmsg.text = currentMsg.message
        }
        else {
            val vh = holder as receivevh
            holder.receivemsg.text = currentMsg.message
        }
    }
}