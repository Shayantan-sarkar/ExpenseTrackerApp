package com.example.ExpenseTracker

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.TextView
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.time.Month
import java.util.Calendar
import java.util.Locale

data class Date(var year: Int, var month: Int, var day: Int)

class ExpenseCalendar {

    private var calendar: Calendar = Calendar.getInstance()
    companion object {
        private var instance: ExpenseCalendar = getInstance()
        fun getInstance(): ExpenseCalendar
        {
            if(instance== null)
            {
                instance = ExpenseCalendar()
            }
            return instance!!
        }
    }


    fun setCurrentTime(date: Date)
    {
        date.year = calendar.get(Calendar.YEAR)
        date.month = calendar.get(Calendar.MONTH)
        date.day = calendar.get(Calendar.DAY_OF_MONTH)
    }

    fun getCurrentDate(): Date
    {
        var date = Date(0,0,0)
        setCurrentTime(date)
        return date
    }

    fun chooseDate(context: Context, currentSetDate: Date, dateTextView: TextView)
    {
        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                setDate(selectedYear, selectedMonth, selectedDay, dateTextView, currentSetDate)
            },
            currentSetDate.year, currentSetDate.month, currentSetDate.day
        )
        datePickerDialog.show()
    }

    private fun setDate(year: Int, month: Int, day: Int, dateTextView: TextView, currentSetDate: Date)
    {
        currentSetDate.year = year
        currentSetDate.month = month
        currentSetDate.day = day
        val formattedDate = String.format("%04d-%02d-%02d", year, month + 1, day)
        dateTextView.text = formattedDate
    }

    fun getCurrentMonthRangeInStringPair(): Pair<String, String> {
        val start = calendar.apply { set(Calendar.DAY_OF_MONTH, 1) }.time
        val end = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return Pair(dateFormat.format(start), dateFormat.format(end))
    }

    fun getLastMonthRangeInStringPair(): Pair<String, String> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        val start = calendar.apply { set(Calendar.DAY_OF_MONTH, 1) }.time
        val end = calendar.apply { set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) }.time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return Pair(dateFormat.format(start), dateFormat.format(end))
    }

    fun getCurrentYearRangeInStringPair(): Pair<String, String> {
        Log.e("Shayantan", "getCurrentYearRange:1 ")
        val calendar = Calendar.getInstance()
        val start = calendar.apply { set(Calendar.DAY_OF_YEAR, 1) }.time
        val end = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return Pair(dateFormat.format(start), dateFormat.format(end))
    }

    fun ConvertDateToString(date: Date): String
    {
        val formattedDate = String.format("%04d-%02d-%02d", date.year, date.month + 1, date.day)
        return formattedDate
    }
}