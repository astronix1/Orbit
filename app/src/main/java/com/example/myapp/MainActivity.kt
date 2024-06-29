package com.example.myapp

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.myapp.databinding.ActivityMainBinding
import com.example.myapp.databinding.FragmentHomefragBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
//    var pfl = findViewById<ImageView>(R.id.profile_icon)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        pfl.setOnClickListener {
//            binding.menubar.setItemSelected(R.id.item2)
//            repl(profile())
//        }
        binding.menubar.setItemSelected(R.id.item0)
        repl(homefrag())
        binding.menubar.setOnItemSelectedListener {
            id->
            when(id){
                R.id.item0 -> repl(homefrag())
                R.id.item1 -> repl(examfrag())
                R.id.item2 -> repl(profile())
                else -> false

            }
            true
        }
    }
     fun repl(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container,fragment)
        fragmentTransaction.commit()
    }
}