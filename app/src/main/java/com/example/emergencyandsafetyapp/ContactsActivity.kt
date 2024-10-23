package com.example.emergencyandsafetyapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ContactsActivity : AppCompatActivity() {
    private lateinit var listview : ListView
    private lateinit var phoneNumbers: List<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contacts)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        listview = findViewById(R.id.listView)
        phoneNumbers = listOf(
            "123-456-7890",
            "987-654-3210",
            "555-123-4567",
            "111-222-3333",
            "444-555-6666"
        )
        val adapter = ArrayAdapter(this, R.layout.list_item_phone_number, R.id.phoneNumberTextView, phoneNumbers)
        listview.adapter = adapter
    }
}