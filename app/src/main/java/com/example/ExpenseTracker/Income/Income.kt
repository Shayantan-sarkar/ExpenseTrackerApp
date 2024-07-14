package com.example.ExpenseTracker.Income

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.ExpenseTracker.database.Repository

data class Income
    (
    val description: String,
    val amount: Double,
    val date: String,
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeDouble(amount)
        parcel.writeString(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Income> {
        override fun createFromParcel(parcel: Parcel): Income {
            return Income(parcel)
        }

        override fun newArray(size: Int): Array<Income?> {
            return arrayOfNulls(size)
        }
    }
}
class IncomeManager(val repository: Repository)
{
    fun registerNewIncome(description: String, date: String, amount: Double): Int
    {
        Log.e("Shayantan", "Income2")
        var newIncome = Income(description,amount,date)
        return repository.insertIncome(newIncome)
    }

    //todo: retrieve from database directly
    fun getIncome(incomeID: Int): Income?
    {
        return repository.getIncome(incomeID)
    }
    fun deleteIncome(incomeID: Int): Boolean
    {
        return repository.deleteIncome(incomeID)
    }
    fun getIncomes(): List<Income>?
    {
        return repository.getAllIncomes().value
    }


}