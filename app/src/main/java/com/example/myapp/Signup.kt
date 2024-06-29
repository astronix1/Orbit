package com.example.myapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.shashank.sony.fancytoastlib.FancyToast

class Signup : AppCompatActivity() {
    data class User(val name: String = "", val username: String = "")

    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance(Constants.dburl)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.logintxt.setOnClickListener {
            startActivity(Intent(this, login2::class.java))
            finish()
        }

        binding.createacc.setOnClickListener {
            val name = binding.namefield.text.toString().trim()
            val username = binding.usernamefield.text.toString().trim()
            val email = binding.emailfield.text.toString().trim()
            val password = binding.pwdfield.text.toString().trim()

            if (email.isEmpty() || name.isEmpty() || username.isEmpty() || password.isEmpty()) {
                FancyToast.makeText(this, "Please fill in all the required details", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
                return@setOnClickListener
            }
            else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { signInTask ->
                                    if (signInTask.isSuccessful) {
                                        val usrId = auth.currentUser?.uid
                                        if (usrId != null) {
                                            val user = User(name, username)
                                            db.reference.child("users").child(usrId).setValue(user)
                                                .addOnSuccessListener {
                                                    showToast("Registration successful!", FancyToast.SUCCESS)
                                                    startActivity(Intent(this, MainActivity::class.java))
                                                    finish()
                                                }.addOnFailureListener {
                                                    showToast("Failed to save user data: ${it.message}", FancyToast.ERROR)
                                                }
                                        } else {
                                            showToast("Something went wrong!", FancyToast.ERROR)
                                        }
                                    } else {
                                        showToast("Sign-in after registration failed: ${signInTask.exception?.message}", FancyToast.ERROR)
                                    }
                                }
                        } else {
                            showToast("Registration Failed: ${task.exception?.message}", FancyToast.ERROR)
                        }
                    }
            }

        }
    }
    private fun showToast(message: String, type: Int) {
        FancyToast.makeText(this, message, FancyToast.LENGTH_SHORT, type, false).show()
    }
}
