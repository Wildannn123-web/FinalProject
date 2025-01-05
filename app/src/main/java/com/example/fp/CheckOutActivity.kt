package com.example.fp.com.example.fp

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fp.HomeActivity
import com.example.fp.R
import com.example.fp.SuksesActivity
import com.example.fp.database.DatabaseHelper

class CheckOutActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        // Inisialisasi DatabaseHelper
        databaseHelper = DatabaseHelper(this)

        val checkoutButton = findViewById<Button>(R.id.btn_checkoutt)
        checkoutButton.setOnClickListener {
            clearCart() // Kosongkan keranjang
            showCustomToast("Thank You!")

            val intent = Intent(this, SuksesActivity::class.java)
            startActivity(intent)
            finish()
        }

        val cancelButton = findViewById<Button>(R.id.btn_cancel)
        cancelButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Fungsi untuk menghapus semua item di keranjang
    private fun clearCart() {
        databaseHelper.clearCart()

        // Tampilan notif
        showCustomToast("Your cart is cleared!")
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
