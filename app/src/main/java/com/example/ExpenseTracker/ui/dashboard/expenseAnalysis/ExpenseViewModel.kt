package com.example.ExpenseTracker.ui.dashboard.expenseAnalysis

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.ExpenseTracker.database.Repository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ExpenseTracker.Expense.Expense
import com.example.ExpenseTracker.Income.Income
import com.example.ExpenseTracker.MainActivity
import java.util.Date

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
    fun getCategoryWiseExpensesForDateRange(startDate: String, endDate: String): LiveData<Map<String, Double>> {
        return repository.getCategoryWiseExpensesInRange(startDate, endDate)
    }

    fun getExpensesForDateRange(startDate: String, endDate: String): LiveData<List<Expense>> {
        return repository.getExpensesInRange(startDate, endDate)
    }

    fun getIncomesForDateRange(startDate: String, endDate: String): LiveData<List<Income>> {
        return repository.getIncomesInRange(startDate, endDate)
    }
}
class DateRangeLiveData<T>(
    private val viewModel: ExpenseViewModel,
    private var currentLiveData: LiveData<T>,
) : MediatorLiveData<T>() {

    private var startDate: String? = null
    private var endDate: String? = null

    init {
        addSource(currentLiveData) {
            if (it != null) {
                value = it
            }
        }
    }

    fun observeDateRange(startDate: String, endDate: String, owner: LifecycleOwner, observer: Observer<T>) {
        this.startDate = startDate
        this.endDate = endDate
        observe(owner, observer)
    }

    fun updateSource(newLiveData: LiveData<T>) {
        currentLiveData?.let { removeSource(it) }
        currentLiveData = newLiveData
        addSource(newLiveData) {
            if (it != null) {
                value = it
            }
        }
    }
}
