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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.e("Shayantan", "ExpenseAnalysisFragment0")
        mainActivity = activity as MainActivity?
        repository = mainActivity?.repository
        val viewModelFactory = ExpenseViewModelFactory(repository!!)
        val expenseViewModel = ViewModelProvider(this, viewModelFactory).get(ExpenseViewModel::class.java)
        Log.d("Shayantan", "ExpenseAnalysisFragment2")
        var sum: Double = 0.0
        expenseViewModel.allExpenses!!.observe(viewLifecycleOwner) { expenses ->
            for(expense in expenses)    {
                sum+= expense.amount
            }
            Log.d("Shayantan", sum.toString())
        }
        _binding = FragmentExpenseanalysisBinding.inflate(inflater, container, false)
        Log.d("Shayantan", "ExpenseAnalysisFragment3")
        val root: View = binding.root
        Log.d("Shayantan", "ExpenseAnalysisFragment4")

        return root
    }

    override fun onResume() {
        super.onResume()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}