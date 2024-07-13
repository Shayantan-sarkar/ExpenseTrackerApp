package com.example.ExpenseTracker

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ExpenseTracker.Expense.ExpenseManager
import com.example.ExpenseTracker.Income.IncomeManager
import com.example.ExpenseTracker.database.ExpenseDAO
import com.example.ExpenseTracker.database.IncomeDAO
import com.example.ExpenseTracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val expenseDAO = ExpenseDAO(this)
    val incomeDAO = IncomeDAO(this)
    val expenseManager = ExpenseManager(expenseDAO)
    val incomeManager = IncomeManager(incomeDAO)
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("Shayantan", "onCreate1")
        super.onCreate(savedInstanceState)
        expenseDAO.open()
        incomeDAO.open()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    public fun registerExpense(desc: String, date: String, amount: Double, expenseCategory: String)
    {
        var newExpenseId = expenseManager.registerNewExpense(desc, date, amount, expenseCategory)
        var newExpense = expenseManager.getExpense(newExpenseId)
        if(newExpense==null)
        {
            Log.e("Shayantan", "New expense was not created successfully.")
            return
        }
        Log.e("Shayantan","Expense Received desc= ${newExpense!!.description} date= ${newExpense.date} amount= ${newExpense.amount}")
    }

    public fun registerIncome(desc: String, date: String, amount: Double)
    {
        Log.e("Shayantan", "Income1")
        var newIncomeId = incomeManager.registerNewIncome(desc,date,amount)
        Log.e("Shayantan", "Income6")
        var newIncome = incomeManager.getIncome(newIncomeId)
        if(newIncome==null)
        {
            Log.e("Shayantan", "New income was not created successfully.")
            return
            }
        Log.e("Shayantan","Income Received desc= ${newIncome!!.description} date= ${newIncome.date} amount= ${newIncome.amount}")
    }

    override fun onDestroy() {
        super.onDestroy()
        expenseDAO.close()
        incomeDAO.close()
    }
}