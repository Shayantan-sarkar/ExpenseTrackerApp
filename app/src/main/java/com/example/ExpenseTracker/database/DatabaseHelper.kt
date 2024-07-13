package com.example.ExpenseTracker.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {

    override fun onCreate(db: SQLiteDatabase) {
        Log.e("Shayantan", "onCreate1")
        db.execSQL(TABLE_CREATE_EXPENSES)
        Log.e("Shayantan", "onCreate2")
        db.execSQL(TABLE_CREATE_INCOME)
        Log.d("DatabaseHelper", "onCreate3")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EXPENSES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_INCOME")
        onCreate(db)
    }

    companion object {
        private var instance: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper =
            instance ?: synchronized(this) {
                instance ?: DatabaseHelper(context.applicationContext).also { instance = it }
            }
        private const val DATABASE_NAME = "expenseTracker.db"
        private const val DATABASE_VERSION = 1

        // Table and column names for expenses
        const val TABLE_EXPENSES = "expenses"
        const val COLUMN_ID = "id"
        const val COLUMN_AMOUNT = "amount"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_DATE = "date"
        const val COLUMN_DESCRIPTION = "description"

        // SQL statement to create the expenses table
        private val TABLE_CREATE_EXPENSES = """
            CREATE TABLE $TABLE_EXPENSES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
                $COLUMN_AMOUNT REAL, 
                $COLUMN_CATEGORY TEXT, 
                $COLUMN_DATE TEXT, 
                $COLUMN_DESCRIPTION TEXT
            );
        """.trimIndent()

        // Table and column names for income
        const val TABLE_INCOME = "incomes"
        const val COLUMN_INCOME_ID = "id"
        const val COLUMN_INCOME_AMOUNT = "amount"
        const val COLUMN_INCOME_DATE = "date"
        const val COLUMN_INCOME_DESCRIPTION = "description"

        // SQL statement to create the income table
        private val TABLE_CREATE_INCOME = """
            CREATE TABLE $TABLE_INCOME (
                $COLUMN_INCOME_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
                $COLUMN_INCOME_AMOUNT REAL,
                $COLUMN_INCOME_DATE TEXT, 
                $COLUMN_INCOME_DESCRIPTION TEXT
            );
        """.trimIndent()
    }
}
