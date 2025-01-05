package com.example.fp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseLogin(context: Context) : SQLiteOpenHelper(context, "DatabaseLogin", null, 1) {

    companion object {
        const val TABLE_USERS = "Users"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_PHONE = "phone"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_EMAIL TEXT UNIQUE NOT NULL,
                $COLUMN_PASSWORD TEXT NOT NULL,
                $COLUMN_PHONE TEXT NOT NULL
            )
        """
        db.execSQL(createUserTable)
        Log.d("DatabaseLogin", "Database created with table $TABLE_USERS")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
        Log.d("DatabaseLogin", "Database upgraded from version $oldVersion to $newVersion")
    }

    fun registerUser(name: String, email: String, password: String, phone: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_PHONE, phone)
        }
        return try {
            val result = db.insert(TABLE_USERS, null, values)
            if (result == -1L) {
                Log.e("DatabaseLogin", "Failed to register user with email: $email")
                false
            } else {
                Log.d("DatabaseLogin", "User registered successfully with email: $email")
                true
            }
        } catch (e: Exception) {
            Log.e("DatabaseLogin", "Error registering user: ${e.message}")
            false
        }
    }

    fun loginUser(email: String, password: String): Boolean {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_ID),
            "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(email, password),
            null,
            null,
            null
        )
        val isLoggedIn = cursor.count > 0
        cursor.close()
        if (isLoggedIn) {
            Log.d("DatabaseLogin", "Login successful for email: $email")
        } else {
            Log.e("DatabaseLogin", "Login failed for email: $email")
        }
        return isLoggedIn
    }

    fun getUserByEmail(email: String): Triple<String, String, String>? {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_NAME, COLUMN_EMAIL, COLUMN_PHONE),
            "$COLUMN_EMAIL = ?",
            arrayOf(email),
            null,
            null,
            null
        )
        return if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val emailResult = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            val phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
            cursor.close()
            Log.d("DatabaseLogin", "User fetched: Name=$name, Email=$emailResult, Phone=$phone")
            Triple(name, emailResult, phone)
        } else {
            cursor.close()
            Log.e("DatabaseLogin", "No user found for email: $email")
            null
        }
    }
}
