package com.example.fp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val drinkmenu= findViewById<Button>(R.id.btn_drinkhome)

        drinkmenu.setOnClickListener {
            val intent = Intent(this, DrinkMenuActivity::class.java)
            startActivity(intent)
        }

        val menu= findViewById<Button>(R.id.btn_pizahome)

        menu.setOnClickListener {
            val intent = Intent(this, PizzaMenuActivity::class.java)
            startActivity(intent)
        }

        val tocart= findViewById<ImageButton>(R.id.btn_carthome)

        tocart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        val toprofilh = findViewById<ImageButton>(R.id.btn_profilehome)
        toprofilh.setOnClickListener {
            val email = intent.getStringExtra("USER_EMAIL") // Dapatkan email dari MainActivity
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("USER_EMAIL", email)
            startActivity(intent)
        }





    }
}
