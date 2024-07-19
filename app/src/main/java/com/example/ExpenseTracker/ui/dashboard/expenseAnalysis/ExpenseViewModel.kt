package com.example.ExpenseTracker.ui.dashboard.expenseAnalysis

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.ExpenseTracker.database.Repository
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.example.ExpenseTracker.Expense.Expense
import com.example.ExpenseTracker.MainActivity

class ExpenseViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
class ExpenseViewModel(private val repository: Repository): ViewModel() {

val allExpenses: LiveData<List<Expense>> by lazy {
        repository.getAllExpenses()
    }
    val currentMonthExpenses: LiveData<List<Expense>> by lazy {
        val (startDate, endDate) = repository.getCurrentMonthRange()
        repository.getExpensesInRange(startDate, endDate)
    }

    val lastMonthExpenses: LiveData<List<Expense>> by lazy {
        val (startDate, endDate) = repository.getLastMonthRange()
        repository.getExpensesInRange(startDate, endDate)
    }

    val thisYearExpenses: LiveData<List<Expense>> by lazy {
        Log.e("DataRetrieval","ExpenseViewModel: thisYearExpenses")
        val (startDate, endDate) = repository.getCurrentYearRange()
        repository.getExpensesInRange(startDate, endDate)
    }

    val categoryWiseExpenses: LiveData<Map<String, Double>> by lazy {
        repository.getCategoryWiseExpenses()
    }

}