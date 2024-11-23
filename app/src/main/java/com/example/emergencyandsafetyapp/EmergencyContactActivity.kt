package com.example.emergencyandsafetyapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EmergencyContactActivity : AppCompatActivity() {
    private lateinit var contactInput1: EditText
    private lateinit var contactInput2: EditText
    private lateinit var contactInput3: EditText
    private lateinit var contactInput4: EditText
    private lateinit var nextBtn: Button
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_emergency_contact)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        contactInput1 = findViewById(R.id.contactInput1)
        contactInput2 = findViewById(R.id.contactInput2)
        contactInput3 = findViewById(R.id.contactInput3)
        contactInput4 = findViewById(R.id.contactInput4)
        nextBtn = findViewById(R.id.nextBtn)
        nextBtn.setOnClickListener {
            // Collect the input data
            val contact1 = contactInput1.text.toString().trim()
            val contact2 = contactInput2.text.toString().trim()
            val contact3 = contactInput3.text.toString().trim()
            val contact4 = contactInput4.text.toString().trim()

            if (contact1.isEmpty() || contact2.isEmpty() || contact3.isEmpty() || contact4.isEmpty()) {
                Toast.makeText(this, "Please enter all contact numbers", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val emergencyContactData = hashMapOf(
                "contact1" to contact1,
                "contact2" to contact2,
                "contact3" to contact3,
                "contact4" to contact4
            )
            db.collection("emergencyContacts")
                .add(emergencyContactData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Emergency contacts saved successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error saving contacts: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}