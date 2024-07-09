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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.com.example.myapp.msg
import com.example.myapp.com.example.myapp.msg_adapter
import com.example.myapp.databinding.ActivityChatBinding
import com.example.myapp.databinding.FragmentExamfragBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shashank.sony.fancytoastlib.FancyToast

class ChatActivity : AppCompatActivity() {
    private val inputBoxMargin = 55
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: ActivityChatBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var msgAdapter: msg_adapter
    private lateinit var msgList: ArrayList<msg>
    var reciverRoom : String? = null
    var senderRoom : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = findViewById(R.id.recycler_view)
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance(Constants.dburl)
        msgList = ArrayList()
        msgAdapter = msg_adapter(this, msgList)
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
                        binding.sendbtn.setImageResource(R.drawable.send123bw)
                        FancyToast.makeText(this,"Your mentor will be assigned within the next 24 hours. Please stay tuned!" , FancyToast.LENGTH_LONG,FancyToast.INFO, false ).show()
                    }
                    else {

                        binding.sendbtn.setImageResource(R.drawable.send123)
                        senderRoom = mentor.toString()+usrid
                        reciverRoom = usrid+mentor.toString()

                        recyclerView.layoutManager = LinearLayoutManager(this)
                        recyclerView.adapter = msgAdapter

                        db.reference.child("chats").child(senderRoom!!).child("messages").addValueEventListener(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                msgList.clear()
                                for (snp in snapshot.children){
                                    val message = snp.getValue(msg::class.java)
                                    msgList.add(message!!)
                                }
                                msgAdapter.notifyDataSetChanged()
                                recyclerView.scrollToPosition(msgList.size - 1)
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }

                        })
                        binding.sendbtn.setOnClickListener {
                            val txt = binding.messageField.text.toString().trim()
                            if (txt.isNotEmpty()){

                                val msg = msg(txt, usrid)
                                db.reference.child("chats").child(senderRoom!!).child("messages").push().setValue(msg).addOnSuccessListener {
                                    db.reference.child("chats").child(reciverRoom!!).child("messages").push().setValue(msg)
                                }
                                binding.messageField.setText("")
                                recyclerView.scrollToPosition(msgList.size - 1)
                            }
                        }
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

            // Adjust only if the keyboard is visible
            if (keypadHeight > screenHeight * 0.15) {
                // Check if there are enough messages to translate the RecyclerView
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                if (lastVisibleItemPosition == msgList.size - 1) {
                    val translationY = -keypadHeight.toFloat() + inputBoxMargin
                    constraintLayout.translationY = translationY
                    recyclerView.translationY = translationY
                } else {
                    constraintLayout.translationY = 0f
                    recyclerView.translationY = 0f
                }
            } else {
                constraintLayout.translationY = 0f
                recyclerView.translationY = 0f
            }
        }
    }
}