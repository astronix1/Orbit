package com.example.myapp

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseDatabase.getInstance(Constants.dburl).setPersistenceEnabled(true)
    }
}
