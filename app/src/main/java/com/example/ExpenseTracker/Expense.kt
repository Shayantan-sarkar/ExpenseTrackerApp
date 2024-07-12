package com.example.ExpenseTracker

import android.os.Parcel
import android.os.Parcelable

data class Expense
    (
            val id: Int,
            val description: String,
            val amount: Double,
            val date: String
            ): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(description)
        parcel.writeDouble(amount)
        parcel.writeString(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Expense> {
        override fun createFromParcel(parcel: Parcel): Expense {
            return Expense(parcel)
        }

        override fun newArray(size: Int): Array<Expense?> {
            return arrayOfNulls(size)
        }
    }
}
class ExpenseFactory
{
        fun createExpense(id: Int, description: String, amount: Double, date: String): Expense
    {
        return Expense(id, description, amount, date)
    }

}
class ExpenseList
{
    private val expenses = mutableListOf<Expense>()
        fun addExpense(expense: Expense)
    {
        expenses.add(expense)
    }
    fun getExpenses(): List<Expense>
    {
        return expenses
    }
        fun getSize(): Int
        {
             return expenses.size
        }
}
class ExpenseManager
{
        private val expenseFactory: ExpenseFactory = ExpenseFactory()
        private val expenseList: ExpenseList = ExpenseList()
        private var expenseCounter: Int =0;
        fun registerNewExpense(description: String, date: String, amount: Double): Int
        {
                var newExpenseId=expenseCounter++
                var newExpense = expenseFactory.createExpense(newExpenseId,description,amount,date)
                expenseList.addExpense(newExpense)
                return newExpenseId
        }
        fun getExpense(expenseID: Int): Expense?
        {
                for(expense in expenseList.getExpenses())
                {
                        if(expense.id==expenseID)
                                return expense
                }
                return null
        }
    fun getExpenses(): List<Expense>
    {
        return expenseList.getExpenses()
    }

}