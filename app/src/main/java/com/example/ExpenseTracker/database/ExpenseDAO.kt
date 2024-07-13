package com.example.ExpenseTracker.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.ExpenseTracker.Expense.Expense
import com.example.ExpenseTracker.Expense.ExpenseCategory


class ExpenseDAO(val context: Context?) {
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
        DatabaseHelper.COLUMN_CATEGORY,
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

    fun addExpense(expense: Expense): Int {
        val values = ContentValues()
        values.put(DatabaseHelper.COLUMN_AMOUNT, expense.amount)
        values.put(DatabaseHelper.COLUMN_CATEGORY, expense.expenseType.toString())
        values.put(DatabaseHelper.COLUMN_DATE, expense.date)
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, expense.description)
        if(database==null)
        {
            Log.e("Shayantan", "Null database")
            return -1
        }
        val insertId = database!!.insert(DatabaseHelper.TABLE_EXPENSES, null, values)
        return insertId.toInt()
    }
    fun getExpense(id: Int): Expense? {
        val cursor = database!!.query(
            DatabaseHelper.TABLE_EXPENSES, allColumns,
            "${DatabaseHelper.COLUMN_ID} = ?", arrayOf(id.toString()),
            null, null, null
        )
        cursor?.moveToFirst()
        val expense = if (cursor != null && cursor.count > 0) {
            cursorToExpense(cursor)
        } else {
            null
        }
        cursor?.close()
        return expense
    }

    fun delExpense(id: Int): Boolean {
        val rowsDeleted = database!!.delete(
            DatabaseHelper.TABLE_EXPENSES,
            "${DatabaseHelper.COLUMN_ID} = ?", arrayOf(id.toString())
        )
        return rowsDeleted > 0
    }
    val allExpenses: List<Expense>
        get() {
            val expenses: MutableList<Expense> = ArrayList()

            val cursor = database!!.query(
                DatabaseHelper.TABLE_EXPENSES,
                allColumns, null, null, null, null, null
            )

            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val expense = cursorToExpense(cursor)
                expenses.add(expense)
                cursor.moveToNext()
            }
            cursor.close()
            return expenses
        }

    private fun cursorToExpense(cursor: Cursor): Expense {
        val expense: Expense = Expense((cursor.getString(4)), cursor.getDouble(1), cursor.getString(3), ExpenseCategory.string2ExpenseType(cursor.getString(2)))
        return expense
    }
}