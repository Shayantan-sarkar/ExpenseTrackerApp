package com.example.ExpenseTracker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ExpenseTracker.Expense.ExpenseManager
import com.example.ExpenseTracker.Income.IncomeManager
import com.example.ExpenseTracker.database.ExpenseDAO
import com.example.ExpenseTracker.database.IncomeDAO
import com.example.ExpenseTracker.database.Repository
import com.example.ExpenseTracker.databinding.ActivityMainBinding
import com.example.ExpenseTracker.SecureStorage
import java.security.KeyStore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val expenseDAO = ExpenseDAO(this)
    val incomeDAO = IncomeDAO(this)
    var repository: Repository? = null
    var expenseManager: ExpenseManager? = null
    var incomeManager: IncomeManager? = null
    lateinit var predictor: ExpensePredictor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        predictor = ExpensePredictor(this)
        repository = Repository(expenseDAO, incomeDAO)
        expenseManager = ExpenseManager(repository!!)
        incomeManager = IncomeManager(repository!!)
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
        var newExpenseId = expenseManager!!.registerNewExpense(desc, date, amount, expenseCategory)
        var newExpense = expenseManager!!.getExpense(newExpenseId)
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
        var newIncomeId = incomeManager!!.registerNewIncome(desc,date,amount)
        Log.e("Shayantan", "Income6")
        var newIncome = incomeManager!!.getIncome(newIncomeId)
        if(newIncome==null)
        {
            Log.e("Shayantan", "New income was not created successfully.")
            return
            }
        Log.e("Shayantan","Income Received desc= ${newIncome!!.description} date= ${newIncome.date} amount= ${newIncome.amount}")
    }

    override fun onDestroy() {
        super.onDestroy()
        repository!!.close()
    }
}