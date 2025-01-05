package com.example.fp

import DrinkItem
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fp.database.DatabaseHelper

class DrinkMenuActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink_menu)

        dbHelper = DatabaseHelper(this)

        setupAddDrinkButtons()
        setupNavigationButtons()
    }

    private fun setupAddDrinkButtons() {
        val drinks = listOf(
            DrinkItem("Ice Tea", 20000, 1),
            DrinkItem("Red Velvet", 25000, 1),
            DrinkItem("Chocolate Ice", 25000, 1),
            DrinkItem("Matcha Ice", 25000, 1),
            DrinkItem("Milkshake Chocolate", 30000, 1),
            DrinkItem("Milkshake Strawberry", 30000, 1),
            DrinkItem("Coffee Ice", 20000, 1),
            DrinkItem("Milkshake Vanilla", 30000, 1)
        )
        val buttonIds = listOf(
            R.id.btnad1drink,
            R.id.btnadd2drink,
            R.id.btnadd3drink,
            R.id.btnadd4drink,
            R.id.btnadd5drink,
            R.id.btnadd6drink,
            R.id.btnadd7drink,
            R.id.btnadd8drink
        )

        buttonIds.forEachIndexed { index, buttonId ->
            val button = findViewById<Button?>(buttonId)
            button?.setOnClickListener {
                addToCart(drinks[index])
            } ?: run {
                // Jika button tidak ditemukan, log error untuk debug
                Toast.makeText(this, "Button with ID $buttonId not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupNavigationButtons() {
        findViewById<ImageButton>(R.id.btn_cartmnudrink).setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
        findViewById<ImageButton>(R.id.btn_hmnudrink).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        findViewById<ImageButton>(R.id.btn_pmnudrink).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun addToCart(item: DrinkItem) {
        val existingItems = dbHelper.getItems("drink")
        val existingItem = existingItems.find { (it as DrinkItem).name == item.name }

        if (existingItem != null) {
            dbHelper.updateItem(item.name, (existingItem as DrinkItem).quantity + item.quantity)
        } else {
            dbHelper.insertItem(item.name, item.price, item.quantity, "drink")
        }
        showCustomToast("${item.name} added to cart!")
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
