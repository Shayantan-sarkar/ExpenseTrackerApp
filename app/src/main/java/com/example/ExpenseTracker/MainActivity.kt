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
import com.example.ExpenseTracker.database.Repository
import com.example.ExpenseTracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val expenseDAO = ExpenseDAO(this)
    val incomeDAO = IncomeDAO(this)
    var repository: Repository? = null
    var expenseManager: ExpenseManager? = null
    var incomeManager: IncomeManager? = null
    private lateinit var predictor: ExpensePredictor
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("Shayantan", "onCreate1")
        predictor = ExpensePredictor(this)
        super.onCreate(savedInstanceState)
        Log.e("Shayantan", "onCreate2")
        repository = Repository(expenseDAO, incomeDAO)
        Log.e("Shayantan", "onCreate3")

        expenseManager = ExpenseManager(repository!!)
        incomeManager = IncomeManager(repository!!)
        Log.e("Shayantan", "onCreate3")
        binding = ActivityMainBinding.inflate(layoutInflater)
        Log.e("Shayantan", "onCreate31")
        setContentView(binding.root)
        Log.e("Shayantan", "onCreate4")
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        Log.e("Shayantan", "onCreate5")
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        Log.e("Shayantan", "onCreate6")

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        Log.e("Shayantan", "onCreate7")
        val category = predictor.predict("Yoga class Fee")

        // Display or use the category
        Log.d("MachineLearningModel", "Predicted category: $category")
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