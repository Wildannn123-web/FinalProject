package com.example.fp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var db: DatabaseLogin
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var logoutButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        nameTextView = findViewById(R.id.nameprofilr)
        emailTextView = findViewById(R.id.emailprofile)
        phoneTextView = findViewById(R.id.nohp)
        logoutButton = findViewById(R.id.logout_button)

        db = DatabaseLogin(this)

        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val email = intent.getStringExtra("USER_EMAIL") ?: sharedPreferences.getString("USER_EMAIL", null)

        if (email != null) {
            val user = getUserDataByEmail(email)
            if (user != null) {
                nameTextView.text = user.first
                emailTextView.text = user.second
                phoneTextView.text = user.third
            } else {
                Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "No email found in session", Toast.LENGTH_SHORT).show()
        }

        logoutButton.setOnClickListener {
            logout()
        }

        val tohome = findViewById<ImageButton>(R.id.btn_thome)
        tohome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val tocart = findViewById<ImageButton>(R.id.btn_carttp)
        tocart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getUserDataByEmail(email: String): Triple<String, String, String>? {
        val dbRead = db.readableDatabase
        val query = """
            SELECT ${DatabaseLogin.COLUMN_NAME}, ${DatabaseLogin.COLUMN_EMAIL}, ${DatabaseLogin.COLUMN_PHONE}
            FROM ${DatabaseLogin.TABLE_USERS}
            WHERE ${DatabaseLogin.COLUMN_EMAIL} = ?
        """.trimIndent()

        dbRead.rawQuery(query, arrayOf(email)).use { cursor ->
            return if (cursor.moveToFirst()) {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseLogin.COLUMN_NAME))
                val userEmail = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseLogin.COLUMN_EMAIL))
                val phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseLogin.COLUMN_PHONE))
                Triple(name, userEmail, phone)
            } else {
                Log.w("ProfileActivity", "No user found in database for email: $email")
                null
            }
        }
    }

    private fun logout() {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply() // Clear session data

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish() // Close ProfileActivity
        Log.d("ProfileActivity", "User logged out successfully.")
    }
}
