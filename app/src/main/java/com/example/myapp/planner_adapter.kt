package com.example.myapp.com.example.myapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        holder.binding.timetxt.text="${datalist.get(position).hour1}:${datalist.get(position).minute1} - ${datalist.get(position).hour2}:${datalist.get(position).minute2}"
        
    }
}