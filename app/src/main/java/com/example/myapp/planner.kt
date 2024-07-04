package com.example.myapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.com.example.myapp.planner_adapter
import com.example.myapp.com.example.myapp.planner_data
import com.example.myapp.databinding.ActivityPlannerBinding

class planner : AppCompatActivity() {
    private lateinit var recyclerview: RecyclerView
    private lateinit var binding: ActivityPlannerBinding
    private lateinit var datalist: ArrayList<planner_data>
    private lateinit var adapter: planner_adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityPlannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        datalist= ArrayList()
        recyclerview=binding.plannerRecycler
        adapter = planner_adapter(datalist,this)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter
        binding.addcard.setOnClickListener {
            additem()
        }





    }

    private fun additem() {
        val new = planner_data(0,0,0,0, "","", "")
        datalist.add(new)
        adapter.notifyItemInserted(datalist.size - 1)
    }
}