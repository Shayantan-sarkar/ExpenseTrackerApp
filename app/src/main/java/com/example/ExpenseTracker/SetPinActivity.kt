package com.example.ExpenseTracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ExpenseTracker.SecureStorage
import androidx.core.content.ContextCompat.startActivity

class SetPinActivity : AppCompatActivity() {

    private lateinit var secureStorage: SecureStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_pin)

        secureStorage = SecureStorage(this)

        val pinEditText: EditText = findViewById(R.id.pinEditText)
        val confirmPinEditText: EditText = findViewById(R.id.confirmPinEditText)
        val setPinButton: Button = findViewById(R.id.setPinButton)

        setPinButton.setOnClickListener {
            val pin = pinEditText.text.toString()
            val confirmPin = confirmPinEditText.text.toString()

            if (pin.isNotEmpty() && pin == confirmPin) {
                secureStorage.savePin(pin)
                Toast.makeText(this, "PIN set successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "PINs do not match", Toast.LENGTH_SHORT).show()
            }
        }
    }
}