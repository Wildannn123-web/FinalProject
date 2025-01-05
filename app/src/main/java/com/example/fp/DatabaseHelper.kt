package com.example.fp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import DrinkItem
import PizzaItem

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "CartDatabase", null, 1) {

    companion object {
        const val TABLE_CART = "Cart"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PRICE = "price"
        const val COLUMN_QUANTITY = "quantity"
        const val COLUMN_TYPE = "type"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_CART (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_PRICE INTEGER,
                $COLUMN_QUANTITY INTEGER,
                $COLUMN_TYPE TEXT
            )
        """
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        onCreate(db)
    }

    fun insertItem(name: String, price: Int, quantity: Int, type: String) {
        val existingItems = getItems(type).filter {
            (it as? PizzaItem)?.name == name || (it as? DrinkItem)?.name == name
        }

        if (existingItems.isNotEmpty()) {
            val existingQuantity = when (val item = existingItems[0]) {
                is PizzaItem -> item.quantity
                is DrinkItem -> item.quantity
                else -> 0
            }
            updateItem(name, existingQuantity + quantity)
        } else {
            val db = writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_NAME, name)
                put(COLUMN_PRICE, price)
                put(COLUMN_QUANTITY, quantity)
                put(COLUMN_TYPE, type)
            }
            db.insert(TABLE_CART, null, values)
        }
    }

    fun getItems(type: String): MutableList<Any> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CART,
            null,
            "$COLUMN_TYPE = ?",
            arrayOf(type),
            null,
            null,
            null
        )
        val items = mutableListOf<Any>()
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val price = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE))
            val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY))
            items.add(
                if (type == "pizza") PizzaItem(name, price, quantity)
                else DrinkItem(name, price, quantity)
            )
        }
        cursor.close()
        return items
    }

    fun updateItem(name: String, quantity: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_QUANTITY, quantity)
        }
        db.update(TABLE_CART, values, "$COLUMN_NAME = ?", arrayOf(name))
    }

    fun deleteItem(name: String) {
        val db = writableDatabase
        db.delete(TABLE_CART, "$COLUMN_NAME = ?", arrayOf(name))
    }

    fun clearCart() {
        val db = writableDatabase
        db.delete(TABLE_CART, null, null)
    }

    fun getTotalPrice(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT SUM($COLUMN_PRICE * $COLUMN_QUANTITY) AS total FROM $TABLE_CART", null)
        var total = 0
        if (cursor.moveToFirst()) {
            total = cursor.getInt(cursor.getColumnIndexOrThrow("total"))
        }
        cursor.close()
        return total
    }
}

