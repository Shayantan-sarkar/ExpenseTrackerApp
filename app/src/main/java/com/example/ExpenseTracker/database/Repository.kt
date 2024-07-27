package com.example.ExpenseTracker.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ExpenseTracker.Expense.Expense
import com.example.ExpenseTracker.Income.Income

class Repository(private val expenseDAO: ExpenseDAO, private val incomeDAO: IncomeDAO) {

    companion object
    {
        private var instance: Repository? = null
        fun getInstance(expenseDAO: ExpenseDAO?, incomeDAO: IncomeDAO?) = instance?:
                synchronized(this) {
                    instance ?: Repository(expenseDAO!!, incomeDAO!!).also { instance = it }
                }
    }
    private val expensesLiveData: MutableLiveData<List<Expense>> = MutableLiveData()
    private val incomesLiveData: MutableLiveData<List<Income>> = MutableLiveData()
    init {
        open()
        fetchAllExpenses()
        fetchAllIncomes()
    }

    fun open() {
        Log.d("Shayantan", "open:1 ")
        expenseDAO.open()
        Log.d("Shayantan", "open:2 ")
        incomeDAO.open()
        Log.d("Shayantan", "open:3 ")
    }
    fun close() {
        expenseDAO.close()
        incomeDAO.close()
    }
    private fun fetchAllExpenses() {
            Log.e("Shayantan", "fetchAllExpenses:1 ")
            val expenses = expenseDAO.allExpenses
            Log.e("Shayantan", "fetchAllExpenses:2 ")
            expensesLiveData.postValue(expenses)
            Log.e("Shayantan", "fetchAllExpenses:3 ")
    }

    private fun fetchAllIncomes() {
        val data = MutableLiveData<List<Income>>()
            val incomes = incomeDAO.allIncomes
            incomesLiveData.postValue(incomes)
    }
    fun getAllExpenses(): LiveData<List<Expense>> {
        return expensesLiveData
    }
    fun getAllIncomes(): LiveData<List<Income>> {
        return incomesLiveData
    }
    fun insertExpense(expense: Expense): Int {
        var id: Int = 0
            id = expenseDAO.addExpense(expense)
            fetchAllExpenses()
        return id
    }
    fun insertIncome(income: Income): Int {
        var id: Int = 0
            id = incomeDAO.addIncome(income)
            fetchAllIncomes()
        return id
    }
    fun deleteExpense(expenseID: Int): Boolean {
        var success = false
            success = expenseDAO.delExpense(expenseID)
            fetchAllExpenses()
        return success
    }
    fun deleteIncome(incomeID: Int): Boolean {
        var success = false
            success = incomeDAO.delIncome(incomeID)
            fetchAllIncomes()
        return success
    }

    fun getExpense(expenseID: Int): Expense? {
        var expense: Expense? = null
            expense = expenseDAO.getExpense(expenseID)
        return expense
    }

    fun getCategoryWiseExpenses(): LiveData<Map<String, Double>> {
        val liveData = MutableLiveData<Map<String, Double>>()
        val categoryWiseExpenses = expenseDAO.getCategoryWiseExpenses()
        liveData.postValue(categoryWiseExpenses)
        return liveData
    }

    fun getCategoryWiseExpensesInRange(startDate: String, endDate: String): LiveData<Map<String, Double>> {
        val liveData = MutableLiveData<Map<String, Double>>()
        val categoryWiseExpenses = expenseDAO.getCategoryWiseExpensesInRange(startDate, endDate)
        liveData.postValue(categoryWiseExpenses)
        return liveData
    }

    fun getIncome(incomeID: Int): Income? {
        var income: Income? = null
            income = incomeDAO.getIncome(incomeID)
        return income
    }

    fun getExpensesInRange(startDate: String, endDate: String): LiveData<List<Expense>>
    {
        Log.e("DataRetrieval", "getExpensesInRange Date Range: $startDate $endDate")
        var expensesInRange = MutableLiveData<List<Expense>>()
        val expenses = expenseDAO.getExpensesInRange(startDate, endDate)
        expensesInRange.postValue(expenses)
        val size = expenses.size
        Log.e("DataRetrieval", "getExpensesInRange Size: $size")
        return expensesInRange
    }
    fun getIncomesInRange(startDate: String, endDate: String): LiveData<List<Income>>
    {
        var incomesInRange = MutableLiveData<List<Income>>()
        incomesInRange.postValue(incomeDAO.getIncomesInRange(startDate, endDate))
        return incomesInRange
    }
}