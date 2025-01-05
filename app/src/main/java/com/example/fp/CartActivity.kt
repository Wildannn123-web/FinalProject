package com.example.fp

import DrinkItem
import PizzaItem
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fp.com.example.fp.CheckOutActivity
import com.example.fp.database.DatabaseHelper

class CartActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var cartItemsContainer: LinearLayout
    private lateinit var totalPrice: TextView
    private var totalAmount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        databaseHelper = DatabaseHelper(this)
        cartItemsContainer = findViewById(R.id.cart_items_container)
        totalPrice = findViewById(R.id.total_price)

        findViewById<Button>(R.id.btn_checkout).setOnClickListener {
            startActivity(Intent(this, CheckOutActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        displayCartItems()
    }

    private fun displayCartItems() {
        cartItemsContainer.removeAllViews()
        totalAmount = 0

        val pizzas = databaseHelper.getItems("pizza")
        val drinks = databaseHelper.getItems("drink")

        for (item in pizzas + drinks) {
            when (item) {
                is PizzaItem -> addItemToCartView(item.name, item.price, item.quantity) {
                    databaseHelper.deleteItem(item.name)
                    displayCartItems()
                }
                is DrinkItem -> addItemToCartView(item.name, item.price, item.quantity) {
                    databaseHelper.deleteItem(item.name)
                    displayCartItems()
                }
                else -> Toast.makeText(this, "Unknown item type", Toast.LENGTH_SHORT).show()
            }
        }

        totalPrice.text = "Total: Rp.$totalAmount"
    }


    private fun addItemToCartView(itemName: String, itemPrice: Int, itemQuantity: Int, onRemoveItem: () -> Unit) {
        val itemLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(12, 12, 12, 12)
        }

        val nameTextView = TextView(this).apply {
            text = itemName
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f)
        }

        val minusButton = Button(this).apply {
            text = "-"
            layoutParams = LinearLayout.LayoutParams(80, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(2, 0, 2, 0)
            }
            setOnClickListener {
                if (itemQuantity > 1) {
                    databaseHelper.updateItem(itemName, itemQuantity - 1)
                    displayCartItems()
                } else {
                    databaseHelper.deleteItem(itemName)
                    displayCartItems()
                }
            }
        }

        val quantityTextView = TextView(this).apply {
            text = itemQuantity.toString()
            textSize = 14f
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(60, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(2, 0, 2, 0)
            }
        }

        val plusButton = Button(this).apply {
            text = "+"
            layoutParams = LinearLayout.LayoutParams(80, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(2, 0, 2, 0)
            }
            setOnClickListener {
                databaseHelper.updateItem(itemName, itemQuantity + 1)
                displayCartItems()
            }
        }

        val priceTextView = TextView(this).apply {
            text = "Rp.${itemPrice * itemQuantity}"
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            gravity = Gravity.END
        }

        val deleteButton = Button(this).apply {
            text = "delete"
            textSize = 10f
            layoutParams = LinearLayout.LayoutParams(160, LinearLayout.LayoutParams.MATCH_PARENT).apply {
                setMargins(8, 0, 0, 0)
            }
            gravity = Gravity.CENTER
            setOnClickListener { onRemoveItem() }
        }

        itemLayout.addView(nameTextView)
        itemLayout.addView(minusButton)
        itemLayout.addView(quantityTextView)
        itemLayout.addView(plusButton)
        itemLayout.addView(priceTextView)
        itemLayout.addView(deleteButton)

        cartItemsContainer.addView(itemLayout)

        totalAmount += itemPrice * itemQuantity
    }



}
