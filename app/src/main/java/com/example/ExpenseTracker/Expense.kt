package com.example.ExpenseTracker

import android.os.Parcel
import android.os.Parcelable

data class Expense
    (
            val id: Int,
            val description: String,
            val amount: Double,
            val date: String,
            val expenseType: ExpenseCategory
            ): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readString() ?: "",
        parcel.readTypedObject(ExpenseCategory.CREATOR)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(description)
        parcel.writeDouble(amount)
        parcel.writeString(date)
        parcel.writeTypedObject(expenseType, flags)
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
        fun createExpense(id: Int, description: String, amount: Double, date: String, expenseType: ExpenseCategory): Expense
    {
        return Expense(id, description, amount, date, expenseType)
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
    fun string2ExpenseType(string: String): ExpenseCategory
    {
        when(string)
        {
            "Food" -> return ExpenseCategory.Food
            "Entertainment" -> return ExpenseCategory.Entertainment
            "Health" -> return ExpenseCategory.Health
            "Transport" -> return ExpenseCategory.Transport
            "Education" -> return ExpenseCategory.Education
            "Housing" -> return ExpenseCategory.Housing
            "Utilities" -> return ExpenseCategory.Utilities
            else -> return ExpenseCategory.Others
        }
    }
        fun registerNewExpense(description: String, date: String, amount: Double, expenseType: String): Int
        {
                var newExpenseId=expenseCounter++
                var newExpense = expenseFactory.createExpense(newExpenseId,description,amount,date, string2ExpenseType(expenseType))
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