package com.example.ExpenseTracker.ui.dashboard.expenseAnalysis

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ExpenseTracker.MainActivity
import com.example.ExpenseTracker.database.Repository
import com.example.ExpenseTracker.databinding.FragmentExpenseanalysisBinding
import com.example.ExpenseTracker.ui.dashboard.DashboardViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ExpenseAnalysisFragment: Fragment() {
    private var _binding: FragmentExpenseanalysisBinding? = null
    private val binding get() = _binding!!
    var mainActivity: MainActivity? = null
    var repository: Repository? = null
    var expenseViewModel: ExpenseViewModel? = null
    private lateinit var startDataButton: Button
    private lateinit var endDataButton: Button
    private lateinit var startDataTextView: TextView
    private lateinit var endDataTextView: TextView
    private lateinit var spinner: Spinner
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainActivity = activity as MainActivity?
        repository = mainActivity?.repository
        val viewModelFactory = ExpenseViewModelFactory(repository!!)
        expenseViewModel = ViewModelProvider(this, viewModelFactory).get(ExpenseViewModel::class.java)
        getCurrentYearExpensesAnalysis()
        _binding = FragmentExpenseanalysisBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startDataButton = binding.startDateButton
        endDataButton = binding.endDateButton
        startDataTextView = binding.startDateTextView
        endDataTextView = binding.endDateTextView
        spinner = binding.editRangeType
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position)
                Log.e("Shayantan", "Selected item:at position: $position")
                handleRangeType(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    fun getCurrentMonthExpensesAnalysis()
    {
        val (startDate, endDate) = getCurrentMonthRange()
        updateCategoryWiseExpensesForDateRange(startDate, endDate)
        updateIncomesForDateRange(startDate, endDate)
    }

    fun getCurrentYearExpensesAnalysis()
    {
        val (startDate, endDate) = getCurrentYearRange()
        updateCategoryWiseExpensesForDateRange(startDate, endDate)
        updateIncomesForDateRange(startDate, endDate)
    }

    fun getLastMonthExpensesAnalysis()
    {
        val (startDate, endDate) = getLastMonthRange()
        updateCategoryWiseExpensesForDateRange(startDate, endDate)
        updateIncomesForDateRange(startDate, endDate)
    }

    private fun updateCategoryWiseExpensesForDateRange(startDate: String, endDate: String) {
        expenseViewModel!!.getCategoryWiseExpensesForDateRange(startDate, endDate).observe(viewLifecycleOwner) { expenses ->
            for ((expenseCat, expenseAmount) in expenses) {
                Log.d("DataRetrievals", "$expenseCat $expenseAmount")
            }
        }
    }

    private fun updateExpensesForDateRange(startDate: String, endDate: String) {
    var overAllSum: Double = 0.0
    expenseViewModel!!.getExpensesForDateRange(startDate, endDate).observe(viewLifecycleOwner) { expenses ->
        for(expense in expenses)    {
            overAllSum+= expense.amount
        }
        Log.d("DataRetrieval", overAllSum.toString())
        }
    }

    private fun updateIncomesForDateRange(startDate: String, endDate: String) {
        var overAllSum: Double = 0.0
        expenseViewModel!!.getIncomesForDateRange(startDate, endDate).observe(viewLifecycleOwner) { incomes ->
            for(income in incomes)    {
                overAllSum+= income.amount
            }
            Log.d("DataRetrievals", overAllSum.toString())
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCurrentMonthRange(): Pair<String, String> {
        val calendar = Calendar.getInstance()
        val start = calendar.apply { set(Calendar.DAY_OF_MONTH, 1) }.time
        val end = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return Pair(dateFormat.format(start), dateFormat.format(end))
    }

    private fun getLastMonthRange(): Pair<String, String> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        val start = calendar.apply { set(Calendar.DAY_OF_MONTH, 1) }.time
        val end = calendar.apply { set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) }.time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return Pair(dateFormat.format(start), dateFormat.format(end))
    }

    private fun getCurrentYearRange(): Pair<String, String> {
        Log.e("Shayantan", "getCurrentYearRange:1 ")
        val calendar = Calendar.getInstance()
        val start = calendar.apply { set(Calendar.DAY_OF_YEAR, 1) }.time
        val end = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return Pair(dateFormat.format(start), dateFormat.format(end))
    }

    private fun handleRangeType(position: Int) {
        if (position < 3) {
            binding.startDateButton.visibility = View.GONE
            binding.endDateButton.visibility = View.GONE
            binding.startDateTextView.visibility = View.GONE
            binding.endDateTextView.visibility = View.GONE
        } else if (position == 3){
            binding.startDateButton.visibility = View.VISIBLE
            binding.endDateButton.visibility = View.VISIBLE
            binding.startDateTextView.visibility = View.VISIBLE
            binding.endDateTextView.visibility = View.VISIBLE
        }
        else
        {
            Log.e("Shayantan","Unimplemented")
        }
    }



}