package com.example.myapp.com.example.myapp

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.R
import com.example.myapp.databinding.ItemListBinding
import com.google.firebase.database.collection.LLRBNode
import com.google.gson.Gson
import java.util.Calendar


class planner_adapter(var datalist: ArrayList<planner_data>,private val context: Context): RecyclerView.Adapter<planner_adapter.planner_item_viewholder>() {
    inner class planner_item_viewholder(var binding:ItemListBinding): RecyclerView.ViewHolder(binding.root)




  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): planner_item_viewholder {
    val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return planner_item_viewholder(binding)
}

    override fun getItemCount(): Int {
       return datalist.size
    }

    override fun onBindViewHolder(holder: planner_item_viewholder, position: Int) {
//        holder.binding.subtxt.text=datalist.get(position).sub
//        holder.binding.descptxt.text=datalist.get(position).des
//        holder.binding.topictxt.text=datalist.get(position).topic
//        holder.binding.time1txt.text="${datalist.get(position).hour1}:${datalist.get(position).minute1}"
//        holder.binding.time2txt.text="${datalist.get(position).hour2}:${datalist.get(position).minute2}"
        val srf = context.getSharedPreferences("planner", Context.MODE_PRIVATE)
        val json = srf.getString("card${position}", null)
        val item = Gson().fromJson(json, planner_data::class.java)
        holder.binding.subtxt.text = item.sub
        holder.binding.descptxt.text = item.des
        holder.binding.topictxt.text = item.topic
        holder.binding.time1txt.text = "${item.hour1}:${item.minute1}"
        holder.binding.time2txt.text = "${item.hour2}:${item.minute2}"

        holder.binding.editSave.setOnClickListener {
            enable_edit(holder, datalist.get(position))
        }

    }

    private fun enable_edit(holder: planner_item_viewholder, item: planner_data) {
        val redd = ContextCompat.getColor(context, R.color.redd)
        holder.binding.subtxt.setTextColor(redd)
        holder.binding.descptxt.setTextColor(redd)
        holder.binding.topictxt.setTextColor(redd)
        holder.binding.time1txt.setTextColor(redd)
        holder.binding.time2txt.setTextColor(redd)
        holder.binding.textView19.setTextColor(redd)
        holder.binding.subtxt.setHintTextColor(redd)
        holder.binding.descptxt.setHintTextColor(redd)
        holder.binding.topictxt.setHintTextColor(redd)
        holder.binding.time1txt.setHintTextColor(redd)
        holder.binding.time2txt.setHintTextColor(redd)
        holder.binding.textView19.setHintTextColor(redd)
        holder.binding.deleteItem.visibility = View.VISIBLE
        holder.binding.deleteItem.setOnClickListener {
            val position = holder.adapterPosition
            datalist.removeAt(position)
            saveChanges(holder, item)
            notifyItemRemoved(position)
        }
        holder.binding.subtxt.setOnClickListener {

            showEditDialog(holder, item, "Subject", holder.binding.subtxt)
        }

        holder.binding.descptxt.setOnClickListener {
            showEditDialog(holder, item, "Description", holder.binding.descptxt)
        }

        holder.binding.topictxt.setOnClickListener {
            showEditDialog(holder, item, "Topic", holder.binding.topictxt)
        }

        holder.binding.time1txt.setOnClickListener {
            openT(holder, item, 0)
        }

        holder.binding.time2txt.setOnClickListener {
            openT(holder, item, 1)
        }
        holder.binding.editSave.text = "Save"
        holder.binding.editSave.setOnClickListener {
            saveChanges(holder, item)
        }
    }

    private fun showEditDialog(
        holder: planner_item_viewholder,
        item: planner_data,
        s: String,
        txtview: TextView
    ) {
        val editText = EditText(context)
        if (txtview is TextView) {
            editText.setText(txtview.text)
        }
        AlertDialog.Builder(context)
            .setTitle("$s")
            .setView(editText)
            .setPositiveButton("Ok") {dialog, _ ->
                val new = editText.text.toString().trim()
                when (s){
                    "Subject" -> {
                        item.sub = new
                        holder.binding.subtxt.text = new
                    }
                    "Description" -> {
                        item.des = new
                        holder.binding.descptxt.text = new
                    }
                    "Topic" -> {
                        item.topic = new
                        holder.binding.topictxt.text = new
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") {dialog, _ ->
                dialog.dismiss()
            }
            .show()

    }

    private fun saveChanges(holder: planner_item_viewholder, item: planner_data) {
        val blackk = Color.BLACK
        val grey = ContextCompat.getColor(context, R.color.descolor)
        holder.binding.subtxt.setTextColor(blackk)
        holder.binding.descptxt.setTextColor(grey)
        holder.binding.topictxt.setTextColor(blackk)
        holder.binding.time1txt.setTextColor(blackk)
        holder.binding.time2txt.setTextColor(blackk)
        holder.binding.textView19.setTextColor(blackk)
        holder.binding.subtxt.setHintTextColor(blackk)
        holder.binding.descptxt.setHintTextColor(grey)
        holder.binding.topictxt.setHintTextColor(blackk)
        holder.binding.time1txt.setHintTextColor(blackk)
        holder.binding.time2txt.setHintTextColor(blackk)
        holder.binding.textView19.setHintTextColor(blackk)
        holder.binding.deleteItem.visibility = View.GONE
        holder.binding.deleteItem.setOnClickListener(null)
        holder.binding.time1txt.setOnClickListener(null)
        holder.binding.time2txt.setOnClickListener(null)
        holder.binding.subtxt.setOnClickListener(null)
        holder.binding.descptxt.setOnClickListener(null)
        holder.binding.topictxt.setOnClickListener(null)

        val editor = context.getSharedPreferences("planner", Context.MODE_PRIVATE).edit()
        val json = Gson().toJson(datalist.get(holder.adapterPosition))
        editor.putString("card${holder.adapterPosition}", json)
        editor.apply()

        holder.binding.editSave.text = "Edit"
        holder.binding.editSave.setOnClickListener {
            enable_edit(holder, item)
        }
    }

    private fun openT(holder: planner_item_viewholder, item: planner_data, i: Int) {
        val currentTime= Calendar.getInstance()

        TimePickerDialog(context, TimePickerDialog.OnTimeSetListener{ view, hourOfDay, minute ->
        if (i==0){
            item.hour1=hourOfDay
            item.minute1=minute
            holder.binding.time1txt.text="${item.hour1}:${item.minute1}"
        }
            else{
                item.hour2=hourOfDay
                item.minute2=minute
                holder.binding.time2txt.text="${item.hour2}:${item.minute2}"
            }

        },currentTime.get(Calendar.HOUR_OF_DAY),currentTime.get(Calendar.MINUTE),false).show()
    }
}