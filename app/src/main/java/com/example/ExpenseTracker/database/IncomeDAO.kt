package com.example.ExpenseTracker.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.ExpenseTracker.Expense.Expense
import com.example.ExpenseTracker.Income.Income

class IncomeDAO(val context: Context?) {
    private var database: SQLiteDatabase? = null
    private var dbHelper: DatabaseHelper? = null
        get()
        {
            if(field == null)
            {
                field = DatabaseHelper(context!!)
            }
            return field
        }
    private val allColumns = arrayOf(
        DatabaseHelper.COLUMN_ID,
        DatabaseHelper.COLUMN_AMOUNT,
        DatabaseHelper.COLUMN_DATE,
        DatabaseHelper.COLUMN_DESCRIPTION
    )

    @Throws(SQLException::class)
    fun open() {
        database = dbHelper!!.writableDatabase
    }

    fun close() {
        dbHelper!!.close()
    }

    fun addIncome(income: Income): Int {
        Log.e("Shayantan", "Income3")
        val values = ContentValues()
        values.put(DatabaseHelper.COLUMN_AMOUNT, income.amount)
        values.put(DatabaseHelper.COLUMN_DATE, income.date)
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, income.description)
        if(database == null) {
            Log.e("Shayantan", "Null database")
            return -1
        }
        Log.e("Shayantan", "Income4")
        val insertId = database!!.insert(DatabaseHelper.TABLE_INCOME, null, values)
        Log.e("Shayantan", "Income5")
        return insertId.toInt()
    }

    fun getIncome(id: Int): Income? {
        Log.e("Shayantan", "Income7")
        val cursor = database!!.query(
            DatabaseHelper.TABLE_INCOME, allColumns,
            "${DatabaseHelper.COLUMN_ID} = ?", arrayOf(id.toString()),
            null, null, null
        )
        Log.e("Shayantan", "Income8")
        cursor?.moveToFirst()
        Log.e("Shayantan", "Income9")
        val income = if (cursor != null && cursor.count > 0) {
            cursorToIncome(cursor)
        } else {
            null
        }
        Log.e("Shayantan", "Income10")
        cursor?.close()
        Log.e("Shayantan", "Income11")
        return income
    }

    fun delIncome(id: Int): Boolean {
        val rowsDeleted = database!!.delete(
            DatabaseHelper.TABLE_INCOME,
            "${DatabaseHelper.COLUMN_ID} = ?", arrayOf(id.toString())
        )
        return rowsDeleted > 0
    }

    val allIncomes: List<Income>
        get() {
            val incomes: MutableList<Income> = ArrayList()

            val cursor = database!!.query(
                DatabaseHelper.TABLE_INCOME,
                allColumns, null, null, null, null, null
            )

            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val income = cursorToIncome(cursor)
                incomes.add(income)
                cursor.moveToNext()
            }
            cursor.close()
            return incomes
        }

    private fun cursorToIncome(cursor: Cursor): Income {
        Log.e("Shayantan", "Income12")
        val income: Income = Income(cursor.getString(3), cursor.getDouble(1), cursor.getString(2))
        return income
    }
    fun getIncomesInRange(startDate: String, endDate: String): List<Income> {
        val incomes: MutableList<Income> = ArrayList()
        val cursor = database!!.query(
            DatabaseHelper.TABLE_INCOME,
            allColumns,
            "${DatabaseHelper.COLUMN_DATE} BETWEEN ? AND ?",
            arrayOf(startDate, endDate),
            null,
            null,
            null
        )
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val expense = cursorToIncome(cursor)
            incomes.add(expense)
            cursor.moveToNext()
        }
        cursor.close()
        return incomes
    }
}