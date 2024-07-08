package com.example.myapp

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ChatActivity : AppCompatActivity() {
    private val inputBoxMargin = 55
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)

        adjustLayoutWhenKeyboardVisible()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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

            // Check if keyboard is shown
            if (keypadHeight > screenHeight * 0.15) {
                // Keyboard is shown
                val translationY = -keypadHeight.toFloat() + inputBoxMargin
                constraintLayout.translationY = translationY
                recyclerView.translationY = translationY
            } else {
                // Keyboard is hidden
                constraintLayout.translationY = 0f
                recyclerView.translationY = 0f
            }
        }
    }
}