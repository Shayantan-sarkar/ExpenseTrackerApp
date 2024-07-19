package com.example.ExpenseTracker.ui.dashboard.expenseAnalysis

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ExpenseTracker.MainActivity
import com.example.ExpenseTracker.database.Repository
import com.example.ExpenseTracker.databinding.FragmentExpenseanalysisBinding
import com.example.ExpenseTracker.ui.dashboard.DashboardViewModel

class ExpenseAnalysisFragment: Fragment() {
    private var _binding: FragmentExpenseanalysisBinding? = null
    private val binding get() = _binding!!
    var mainActivity: MainActivity? = null
    var repository: Repository? = null
    var expenseViewModel: ExpenseViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainActivity = activity as MainActivity?
        repository = mainActivity?.repository
        val viewModelFactory = ExpenseViewModelFactory(repository!!)
        expenseViewModel = ViewModelProvider(this, viewModelFactory).get(ExpenseViewModel::class.java)
        UpdateFromViewModel()
        _binding = FragmentExpenseanalysisBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }


    fun UpdateFromViewModel() {
        var overAllSum: Double = 0.0
        expenseViewModel!!.allExpenses!!.observe(viewLifecycleOwner) { expenses ->
            for(expense in expenses)    {
                overAllSum+= expense.amount
            }
            Log.d("DataRetrieval", overAllSum.toString())
        }
        var thisYearSum: Double = 0.0
        expenseViewModel!!.thisYearExpenses!!.observe(viewLifecycleOwner) { expenses ->
            for(expense in expenses)    {
                thisYearSum+= expense.amount
            }
            Log.d("DataRetrieval", thisYearSum.toString())
        }
        var thisMonthSum: Double = 0.0
        expenseViewModel!!.currentMonthExpenses!!.observe(viewLifecycleOwner) { expenses ->
            for (expense in expenses) {
                thisMonthSum += expense.amount
            }
            Log.d("DataRetrieval", thisMonthSum.toString())
        }
        var lastMonthSum: Double = 0.0
        expenseViewModel!!.lastMonthExpenses!!.observe(viewLifecycleOwner) { expenses ->
            for (expense in expenses) {
                lastMonthSum += expense.amount
            }
            Log.d("DataRetrieval", lastMonthSum.toString())
        }
        expenseViewModel!!.categoryWiseExpenses!!.observe(viewLifecycleOwner) { expenses ->
            for ((expenseCat,expenseAmount) in expenses) {
                Log.d("DataRetrieval", expenseCat.toString()+" "+ expenseAmount.toString())
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}