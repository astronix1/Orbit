package com.example.myapp

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ChatActivity : AppCompatActivity() {
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
            val rect = android.graphics.Rect()
            rootLayout.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootLayout.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            // Check if keyboard is shown
            if (keypadHeight > screenHeight * 0.15) {
                // Keyboard is shown
                findViewById<View>(R.id.constraintLayout).translationY = -keypadHeight.toFloat()
            } else {
                // Keyboard is hidden
                findViewById<View>(R.id.constraintLayout).translationY = 0f
            }
        }
    }
}