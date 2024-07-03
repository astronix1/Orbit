package com.example.myapp.com.example.myapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.R


class planner_adapter(var datalist: ArrayList<planner_data>): RecyclerView.Adapter<planner_adapter.planner_item_viewholder>() {
    inner class planner_item_viewholder(var view:View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): planner_item_viewholder {
        var view= LayoutInflater.from(parent.context).inflate(R.layout.item_list,parent,false)
        return planner_item_viewholder(view)
    }

    override fun getItemCount(): Int {
       return datalist.size
    }

    override fun onBindViewHolder(holder: planner_item_viewholder, position: Int) {
//        TODO("Not yet implemented")
    }
}