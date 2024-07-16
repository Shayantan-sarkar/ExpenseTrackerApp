package com.example.ExpenseTracker.Expense

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LiveData
import com.example.ExpenseTracker.database.Repository

data class Expense
    (
    val description: String,
    val amount: Double,
    val date: String,
    val expenseType: ExpenseCategory
            ): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readString() ?: "",
        parcel.readTypedObject(ExpenseCategory)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
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

//Todo: remove this as not required any more
class ExpenseFactory
{
        fun createExpense(description: String, amount: Double, date: String, expenseType: ExpenseCategory): Expense
        {
            return Expense(description, amount, date, expenseType)
        }

}

class ExpenseManager(val repository: Repository)
{
        private val expenseFactory: ExpenseFactory = ExpenseFactory()
        fun registerNewExpense(description: String, date: String, amount: Double, expenseType: String): Int
        {
                var newExpense = Expense(description,amount,date, ExpenseCategory.string2ExpenseType(expenseType))
                return repository.insertExpense(newExpense)
        }
        //todo: retrieve from database directly
        fun getExpense(expenseID: Int): Expense?
        {
            return repository.getExpense(expenseID)
        }
    fun deleteExpense(expenseID: Int): Boolean
    {
        return repository.deleteExpense(expenseID)
    }
    //Todo: Currently converted the live<list> to list data.
    fun getExpenses(): List<Expense>?
    {
        return repository.getAllExpenses().value
    }


}