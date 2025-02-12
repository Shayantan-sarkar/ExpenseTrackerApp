package com.example.ExpenseTracker.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.ExpenseTracker.Expense.ExpenseAdapter
import com.example.ExpenseTracker.MainActivity
import com.example.ExpenseTracker.R
import com.example.ExpenseTracker.databinding.FragmentDashboardBinding
import com.example.ExpenseTracker.ui.dashboard.expenseAnalysis.ExpenseAnalysisFragment
import com.example.ExpenseTracker.ui.dashboard.expenseList.ExpenseListFragment
import com.example.ExpenseTracker.ui.dashboard.expenseList.IncomeListFragment

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    var adapter: ExpenseAdapter? = null
    var mainActivity: MainActivity? = null

    private lateinit var expenseListFragment: ExpenseListFragment
    private lateinit var analysisFragment: ExpenseAnalysisFragment
    private lateinit var incomeListFragment: IncomeListFragment
    private var currentFragment: Fragment? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.e("Sarkar","Dashboard1")
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)
        Log.e("Sarkar","Dashboard2")
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        Log.e("Sarkar","Dashboard3")
        val root: View = binding.root
        Log.e("Sarkar","Dashboard4")
        mainActivity = activity as MainActivity?
        Log.e("Sarkar","Dashboard5")
        expenseListFragment = ExpenseListFragment()
        analysisFragment = ExpenseAnalysisFragment()
        incomeListFragment = IncomeListFragment()
        val iconShowAnalysis = binding.buttonShowAnalysis
        val iconShowExpenses = binding.buttonShowExpenses
        val iconShowIncomes = binding.buttonShowIncomes
        replaceFragment(expenseListFragment)
        iconShowExpenses.isSelected = true
        iconShowExpenses.setOnClickListener {
            iconShowExpenses.isSelected = true
            iconShowAnalysis.isSelected = false
            iconShowIncomes.isSelected = false
            replaceFragment(expenseListFragment)
        }
        iconShowAnalysis.setOnClickListener {
            iconShowExpenses.isSelected = false
            iconShowAnalysis.isSelected = true
            iconShowIncomes.isSelected = false
            replaceFragment(analysisFragment)
        }
        iconShowIncomes.setOnClickListener {
            iconShowExpenses.isSelected = false
            iconShowAnalysis.isSelected = false
            iconShowIncomes.isSelected = true
            replaceFragment(incomeListFragment)
        }
        Log.e("Sarkar","Dashboard6")
        return root
    }

    fun replaceFragment(fragment: Fragment)
    {
        currentFragment?.let {
            if (it == fragment)
                return
        }

        currentFragment = fragment
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
    override fun onResume() {
        super.onResume()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}