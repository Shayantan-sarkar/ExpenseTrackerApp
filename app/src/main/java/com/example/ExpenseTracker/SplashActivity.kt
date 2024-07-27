package com.example.ExpenseTracker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity: AppCompatActivity() {
    private lateinit var secureStorage: SecureStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        secureStorage = SecureStorage(this)
        if(secureStorage.isPinSaved()==false)
        {
            val intent = Intent(this, SetPinActivity::class.java)
            startActivity(intent)
            finish()
        }
        else
        {
            val intent = Intent(this, EntryPinActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}