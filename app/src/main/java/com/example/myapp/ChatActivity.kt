package com.example.myapp

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp.databinding.ActivityChatBinding
import com.example.myapp.databinding.FragmentExamfragBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.shashank.sony.fancytoastlib.FancyToast

class ChatActivity : AppCompatActivity() {
    private val inputBoxMargin = 55
    private lateinit var binding: ActivityChatBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance(Constants.dburl)

        adjustLayoutWhenKeyboardVisible()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val usrid = auth.currentUser?.uid
        if (usrid!=null){
            Log.d("chat", "1")
            db.reference.child("users").child(usrid).get().addOnSuccessListener { snp->
                if (snp.exists()){
                    Log.d("chat", "2")
                    val mentor = snp.child("mentor").value
                    if (mentor.toString() == "not"){
                        Log.d("chat", "3")
                        FancyToast.makeText(this,"Your mentor will be assigned within the next 24 hours. Please stay tuned!" , FancyToast.LENGTH_LONG,FancyToast.INFO, false ).show()
                    }
                    else {

                    }
                }
            }
        }
        binding.backbtn.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun adjustLayoutWhenKeyboardVisible() {
        val rootLayout: View = findViewById(R.id.main)
        rootLayout.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            rootLayout.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootLayout.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            val constraintLayout: View = findViewById(R.id.constraintLayout)
            val recyclerView: View = findViewById(R.id.recycler_view)
            if (keypadHeight > screenHeight * 0.15) {
                val translationY = -keypadHeight.toFloat() + inputBoxMargin
                constraintLayout.translationY = translationY
                recyclerView.translationY = translationY
            } else {
                constraintLayout.translationY = 0f
                recyclerView.translationY = 0f
            }
        }
    }
}