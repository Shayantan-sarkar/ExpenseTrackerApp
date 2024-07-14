package com.example.ExpenseTracker.ui.dashboard.expenseAnalysis

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
    var allExpenses: LiveData<List<Expense>>? = null
    init {
        allExpenses = repository.getAllExpenses()
    }
}