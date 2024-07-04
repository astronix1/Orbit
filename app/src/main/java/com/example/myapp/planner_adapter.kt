package com.example.myapp.com.example.myapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.R
import com.example.myapp.databinding.ItemListBinding


class planner_adapter(var datalist: ArrayList<planner_data>): RecyclerView.Adapter<planner_adapter.planner_item_viewholder>() {
    inner class planner_item_viewholder(var binding:ItemListBinding): RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): planner_item_viewholder {
        var binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return planner_item_viewholder(binding)
    }

    override fun getItemCount(): Int {
       return datalist.size
    }

    override fun onBindViewHolder(holder: planner_item_viewholder, position: Int) {
        holder.binding.subtxt.text=datalist.get(position).sub
        holder.binding.descptxt.text=datalist.get(position).des
        holder.binding.topictxt.text=datalist.get(position).topic
        holder.binding.time1txt.text="${datalist.get(position).hour1}:${datalist.get(position).minute1}"
        holder.binding.time2txt.text="${datalist.get(position).hour2}:${datalist.get(position).minute2}"

        holder.binding.editSave.setOnClickListener {
            enable_edit(holder, datalist.get(position))
        }

    }

    private fun enable_edit(holder: planner_item_viewholder, item: planner_data) {
        val editSubject = EditText(holder.itemView.context).apply{
            setText(holder.binding.subtxt.text)
        }
        val editDescription = EditText(holder.itemView.context).apply{
            setText(holder.binding.descptxt.text)
        }
        val editTopic = EditText(holder.itemView.context).apply{
            setText(holder.binding.topictxt.text)
        }
        holder.binding.time1txt.setOnClickListener{
            openT(holder, item)
        }
        holder.binding.time2txt.setOnClickListener {
            openT(holder, item)
        }
        holder.binding.editSave.text="Save"
        holder.binding.editSave.setOnClickListener {
            holder.binding.editSave.setOnClickListener {
                saveChanges(holder, item, editSubject, editDescription, editTopic)
            }
        }
    }

    private fun saveChanges(
        holder: planner_item_viewholder,
        item: planner_data,
        editSubject: EditText,
        editDescription: EditText,
        editTopic: EditText
    ) {
        item.sub = editSubject.text.toString()
        item.des = editDescription.text.toString()
        item.topic = editTopic.text.toString()

        holder.binding.subtxt.text = item.sub
        holder.binding.descptxt.text = item.des
        holder.binding.topictxt.text = item.topic

        val parent = editSubject.parent as ViewGroup
        val index1 = parent.indexOfChild(editSubject)
        parent.removeView(editSubject)
        parent.addView(holder.binding.subtxt, index1)
        val index2 = parent.indexOfChild(editDescription)
        parent.removeView(editDescription)
        parent.addView(holder.binding.descptxt, index2)
        val index3 = parent.indexOfChild(editTopic)
        parent.removeView(editTopic)
        parent.addView(holder.binding.topictxt, index3)
        holder.binding.time1txt.setOnClickListener (null)
        holder.binding.time2txt.setOnClickListener(null)
        holder.binding.editSave.text = "Edit"
        holder.binding.editSave.setOnClickListener {
            enable_edit(holder, item)
        }
    }

    private fun openT(holder: planner_item_viewholder, item: planner_data) {
        TODO("Not yet implemented")
    }
}