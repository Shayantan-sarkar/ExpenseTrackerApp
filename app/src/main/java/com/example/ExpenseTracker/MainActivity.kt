package com.example.ExpenseTracker

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ExpenseTracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val expenseManager = ExpenseManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    public fun registerExpense(desc: String, date: String, amount: Double)
    {
        var newExpenseId = expenseManager.registerNewExpense(desc, date, amount)
        var newExpense = expenseManager.getExpense(newExpenseId)
        if(newExpense==null)
        {
            Log.e("Shayantan", "New expense was not created successfully.")
            return
        }
        Log.e("Shayantan","Expense Received desc= ${newExpense!!.description} date= ${newExpense.date} amount= ${newExpense.amount} id= ${newExpense.id}")
    }
}