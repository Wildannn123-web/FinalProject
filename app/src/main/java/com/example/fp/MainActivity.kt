package com.example.fp

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var databaseLogin: DatabaseLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseLogin = DatabaseLogin(this)

        val loginButton = findViewById<Button>(R.id.btn_log)
        val signupButton = findViewById<Button>(R.id.signup_button)
        val emailInput = findViewById<EditText>(R.id.email_input)
        val passwordInput = findViewById<EditText>(R.id.password_input)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                showCustomToast("Please enter email and password")
                return@setOnClickListener
            }

            if (databaseLogin.loginUser(email, password)) {
                val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                sharedPreferences.edit().putString("USER_EMAIL", email).apply()

                showCustomToast("Login Successful")
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("USER_EMAIL", email) // Send email to HomeActivity
                startActivity(intent)
                finish()
            } else {
                showCustomToast("Invalid email or password")
            }
        }

        signupButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showCustomToast(message: String) {
        val layoutInflater = LayoutInflater.from(applicationContext)
        val view: View = layoutInflater.inflate(R.layout.toast, null)

        val textView: TextView = view.findViewById(R.id.toastText)
        textView.text = message

        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = view
        toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 100)
        toast.show()
    }
}