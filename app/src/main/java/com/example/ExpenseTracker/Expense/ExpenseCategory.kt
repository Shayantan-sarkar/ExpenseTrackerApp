package com.example.ExpenseTracker.Expense

import android.os.Parcel
import android.os.Parcelable

enum class ExpenseCategory(val value: Int) : Parcelable {
    Food(0),
    Transport(1),
    Entertainment(2),
    Health(3),
    Education(4),
    Housing(5),
    Utilities(6),
    Others(7);

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(value)
    }

    companion object CREATOR : Parcelable.Creator<ExpenseCategory> {
        override fun createFromParcel(parcel: Parcel): ExpenseCategory {
            return values()[parcel.readInt()]
        }

        override fun newArray(size: Int): Array<ExpenseCategory?> {
            return arrayOfNulls(size)
        }

        fun string2ExpenseType(string: String): ExpenseCategory
        {
            when(string)
            {
                "Food" -> return Food
                "Entertainment" -> return Entertainment
                "Health" -> return Health
                "Transport" -> return Transport
                "Education" -> return Education
                "Housing" -> return Housing
                "Utilities" -> return Utilities
                else -> return Others
            }
        }
    }

}