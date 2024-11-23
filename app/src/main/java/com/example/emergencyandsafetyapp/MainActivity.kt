package com.example.emergencyandsafetyapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.telephony.SmsManager
import android.view.MotionEvent
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var buttonPressedTime: Long = 0
    private val handler = Handler()
    private lateinit var sosButton: Button
    private var emergencyNumbers: List<String> = mutableListOf()

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViewById<LinearLayout>(R.id.contact).setOnClickListener{
            val intent = Intent(this, ContactsActivity::class.java)
            startActivity(intent)
        }
        sosButton = findViewById(R.id.sosCard)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermission()
        }
        fetchEmergencyNumbers()
        sosButton.setOnTouchListener { v, event ->
            when (event.action) {
                // Action when button is pressed down
                MotionEvent.ACTION_DOWN -> {
                    buttonPressedTime = System.currentTimeMillis()
                }
                // Action when button is released
                MotionEvent.ACTION_UP -> {
                    val duration = System.currentTimeMillis() - buttonPressedTime
                    if (duration >= 3000) { // If held for 3 seconds
                        sendEmergencySMS()
                    }
                }
            }
            true
        }
        fun requestPermission() {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.SEND_SMS),
                1
            )
        }
        fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
        fun fetchEmergencyNumbers() {
            db.collection("EmergencyNumbers")
                .get()
                .addOnSuccessListener { documents ->
                    val numbers = mutableListOf<String>()
                    for (document in documents) {
                        numbers.add(document.getString("number") ?: "")
                    }
                    emergencyNumbers = numbers
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to fetch emergency numbers", Toast.LENGTH_SHORT).show()
                }
        }
        fun sendEmergencySMS() {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                if (emergencyNumbers.isNotEmpty()) {
                    for (number in emergencyNumbers) {
                        val smsManager = SmsManager.getDefault()
                        smsManager.sendTextMessage(number, null, "Emergency! Location: [Your Location]", null, null)
                    }
                    Toast.makeText(this, "Emergency SMS sent", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "No emergency numbers available", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}