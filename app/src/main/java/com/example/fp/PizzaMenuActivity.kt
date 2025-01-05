package com.example.fp

import PizzaItem
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

class PizzaMenuActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pizza_menu)

        databaseHelper = DatabaseHelper(this)

        setupAddPizzaButtons()
        setupNavigationButtons()
    }

    private fun setupAddPizzaButtons() {
        val pizzas = listOf(
            PizzaItem("American Pepperoni", 120000, 1),
            PizzaItem("Extravaganza Pizza", 150000, 1),
            PizzaItem("Cheesemania", 170000, 1),
            PizzaItem("Super Supreme", 180000, 1),
            PizzaItem("Tuna Pepperoni", 120000, 1),
            PizzaItem("Heat Monster", 180000, 1),
            PizzaItem("American Favourite", 190000, 1),
            PizzaItem("Sweet Chili Chicken", 120000, 1)
        )

        val buttons = listOf(
            findViewById<Button>(R.id.btnad1),
            findViewById<Button>(R.id.btnadd2),
            findViewById<Button>(R.id.btnadd3),
            findViewById<Button>(R.id.btnadd4),
            findViewById<Button>(R.id.btnadd5),
            findViewById<Button>(R.id.btnadd6),
            findViewById<Button>(R.id.btnadd7),
            findViewById<Button>(R.id.btnadd8)
        )

        for ((index, button) in buttons.withIndex()) {
            button.setOnClickListener {
                addToCart(pizzas[index])
            }
        }
    }

    private fun setupNavigationButtons() {
        findViewById<ImageButton>(R.id.btn_cartmnu).setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
        findViewById<ImageButton>(R.id.btn_hmnu).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        findViewById<ImageButton>(R.id.btn_pmnu).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun addToCart(item: PizzaItem) {
        databaseHelper.insertItem(item.name, item.price, item.quantity, "pizza")
        showCustomToast("${item.name} added to cart")
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
