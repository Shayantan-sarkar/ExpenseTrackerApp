package com.example.ExpenseTracker.ui.dashboard.expenseAnalysis

import android.graphics.Color
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
import com.example.ExpenseTracker.Date
import com.example.ExpenseTracker.Expense.Expense
import com.example.ExpenseTracker.ExpenseCalendar
import com.example.ExpenseTracker.Income.Income
import com.example.ExpenseTracker.MainActivity
import com.example.ExpenseTracker.database.Repository
import com.example.ExpenseTracker.databinding.FragmentExpenseanalysisBinding
import com.example.ExpenseTracker.ui.dashboard.DashboardViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ExpenseAnalysisFragment: Fragment() {
    private var _binding: FragmentExpenseanalysisBinding? = null
    private val binding get() = _binding!!
    var mainActivity: MainActivity? = null
    var repository: Repository? = null
    var expenseViewModel: ExpenseViewModel? = null
    private lateinit var startDateButton: Button
    private lateinit var endDataButton: Button
    private lateinit var startDateTextView: TextView
    private lateinit var endDataTextView: TextView
    private lateinit var totalExpenseTextView: TextView
    private lateinit var totalIncomeTextView: TextView
    private lateinit var totalSavingsTextView: TextView
    private lateinit var spinner: Spinner
    private var calendar = ExpenseCalendar.getInstance()
    private var startDate: Date = calendar.getCurrentDate()
    private var endDate: Date = calendar.getCurrentDate()
    private var prevStartDateObserve: String? = null
    private var prevEndDateObserve: String? = null
    private var overAllExpenseSum = 0.0
    private var overAllIncomeSum = 0.0
    private var overAllSavingsSum = 0.0
    private lateinit var categoryWiseExpense: Map<String, Double>
    private lateinit var categoryWiseDateRangeLiveData: DateRangeLiveData<Map<String, Double>>
    private lateinit var expensesDateRangeLiveData: DateRangeLiveData<List<Expense>>
    private lateinit var incomesDateRangeLiveData: DateRangeLiveData<List<Income>>
    private lateinit var pieChart: PieChart
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainActivity = activity as MainActivity?
        repository = mainActivity?.repository
        val viewModelFactory = ExpenseViewModelFactory(repository!!)
        expenseViewModel = ViewModelProvider(this, viewModelFactory).get(ExpenseViewModel::class.java)
        _binding = FragmentExpenseanalysisBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startDateButton = binding.startDateButton
        endDataButton = binding.endDateButton
        startDateTextView = binding.startDateTextView
        endDataTextView = binding.endDateTextView
        totalExpenseTextView = binding.totalexpenseAmount
        totalIncomeTextView = binding.totalincomeAmount
        totalSavingsTextView = binding.totalsavingsAmount
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
        val startDateString = calendar.ConvertDateToString(startDate)
        val endDateString = calendar.ConvertDateToString(endDate)
        startDateTextView.text = startDateString
        endDataTextView.text = endDateString
        categoryWiseDateRangeLiveData = DateRangeLiveData(expenseViewModel!!, expenseViewModel!!.getCategoryWiseExpensesForDateRange(startDateString, endDateString))
        incomesDateRangeLiveData = DateRangeLiveData(expenseViewModel!!, expenseViewModel!!.getIncomesForDateRange(startDateString, endDateString))
        expensesDateRangeLiveData = DateRangeLiveData(expenseViewModel!!, expenseViewModel!!.getExpensesForDateRange(startDateString, endDateString))
        categoryWiseDateRangeLiveData.observeDateRange(startDateString, endDateString, viewLifecycleOwner) { expenses ->
            for ((expenseCat, expenseAmount) in expenses) {
                Log.d("DataRetrievals", "$expenseCat $expenseAmount")

            }
            categoryWiseExpense = expenses
            calculateAnalysis()
        }

        expensesDateRangeLiveData.observeDateRange(startDateString, endDateString, viewLifecycleOwner) { expenses ->
            overAllExpenseSum = 0.0
            for (expense in expenses) {
                overAllExpenseSum += expense.amount
            }
            totalExpenseTextView.text = overAllExpenseSum.toString()
            calculateAnalysis()
            Log.d("DataRetrievals", overAllExpenseSum.toString())
        }

        incomesDateRangeLiveData.observeDateRange(startDateString, endDateString, viewLifecycleOwner) { incomes ->
            overAllIncomeSum = 0.0
            for (income in incomes) {
                overAllIncomeSum += income.amount
            }
            totalIncomeTextView.text = overAllIncomeSum.toString()
            calculateAnalysis()
            Log.d("DataRetrievals", overAllIncomeSum.toString())
        }
        startDateButton.setOnClickListener(this::setStartDate)
        endDataButton.setOnClickListener(this::setEndDate)
        pieChart = binding.expensePieChart
    }

    private fun calculateAnalysis()
    {
        totalSavingsTextView.text = (overAllIncomeSum - overAllExpenseSum).toString()
        updatePieChart()
    }
    private fun updatePieChart() {
        val entries = if (categoryWiseExpense == null) {
            listOf(
                PieEntry(0f, "Others"),
                PieEntry(0f, "Transport"),
                PieEntry(0f, "Entertainment"),
                PieEntry(0f, "Utilities"),
                PieEntry(0f, "Housing"),
                PieEntry(0f, "Education"),
                PieEntry(0f, "Health"),
                PieEntry(0f, "Food")
            )

         } else {
            val pieEntries: List<PieEntry> = categoryWiseExpense.map { (category, amount) ->
                PieEntry(amount.toFloat(), category)
            }
            pieEntries.toMutableList()
         }
        val filteredEntries = entries.filter { it.value != 0f }
        val dataSet = PieDataSet(filteredEntries, "Expense Categories")
        dataSet.colors = listOf(
        Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
        Color.MAGENTA, Color.CYAN, Color.LTGRAY, Color.DKGRAY
        )
        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.description.isEnabled = true // Disable the description text
        pieChart.centerText = "Expenses" // Center text
        pieChart.setEntryLabelTextSize(12f) // Entry label text size
        pieChart.setEntryLabelColor(Color.BLACK) // Entry label color
        pieChart.animateY(1000) // Animation for the chart
        pieChart.invalidate() // Refresh the chart
    }
    private fun setStartDate(view: View)
    {
        calendar.chooseDate(this.requireContext(), startDate,startDateTextView!!)
        {
            handleRangeType(spinner.selectedItemPosition)
        }

    }

    private fun setEndDate(view: View)
    {
        calendar.chooseDate(this.requireContext(), endDate, endDataTextView!!){
        handleRangeType(spinner.selectedItemPosition)}
    }

    fun getCurrentMonthExpensesAnalysis()
    {
        val (startDate, endDate) = calendar.getCurrentMonthRangeInStringPair()
        updateRange(startDate, endDate)
        Log.e("DataRetrieval", "Current Month Expenses Analysis")
    }

    fun getCurrentYearExpensesAnalysis()
    {
        val (startDate, endDate) = calendar.getCurrentYearRangeInStringPair()
        updateRange(startDate, endDate)
        Log.e("DataRetrieval", "Current Year Expenses Analysis")
    }

    fun getLastMonthExpensesAnalysis()
    {
        val (startDate, endDate) = calendar.getLastMonthRangeInStringPair()
        updateRange(startDate, endDate)
        Log.e("DataRetrieval", "Last Month Expenses Analysis")
    }

    fun getCustomRangeAnalysis()
    {
        if(startDate==null || endDate== null)
            return
        val startDateString = calendar.ConvertDateToString(startDate)
        val endDateString = calendar.ConvertDateToString(endDate)
        updateRange(startDateString, endDateString)
        Log.e("DataRetrieval", "Custom Range Expenses Analysis")
    }
    private fun updateRange(startDate: String, endDate: String) {
        categoryWiseDateRangeLiveData.updateSource(expenseViewModel!!.getCategoryWiseExpensesForDateRange(startDate, endDate))
        expensesDateRangeLiveData.updateSource(expenseViewModel!!.getExpensesForDateRange(startDate, endDate))
        incomesDateRangeLiveData.updateSource(expenseViewModel!!.getIncomesForDateRange(startDate, endDate))
    }

    private fun calculateSavingsForDateRange(startDate: String, endDate: String): Double
    {
        return overAllIncomeSum - overAllExpenseSum
    }

//    private fun updateExpensesForDateRange(startDate: String, endDate: String) {
//    var overAllSum: Double = 0.0
//    expenseViewModel!!.getExpensesForDateRange(startDate, endDate).observe(viewLifecycleOwner) { expenses ->
//        for(expense in expenses)    {
//            overAllSum+= expense.amount
//        }
//        Log.d("DataRetrieval", overAllSum.toString())
//        }
//    }
//
//    private fun updateIncomesForDateRange(startDate: String, endDate: String) {
//        var overAllSum: Double = 0.0
//        expenseViewModel!!.getIncomesForDateRange(startDate, endDate).observe(viewLifecycleOwner) { incomes ->
//            for(income in incomes)    {
//                overAllSum+= income.amount
//            }
//            Log.d("DataRetrievals", overAllSum.toString())
//        }
//    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun handleRangeType(position: Int) {
        if (position < 3) {
            binding.startDateButton.visibility = View.GONE
            binding.endDateButton.visibility = View.GONE
            binding.startDateTextView.visibility = View.GONE
            binding.endDateTextView.visibility = View.GONE
        } else if (position == 3) {
            binding.startDateButton.visibility = View.VISIBLE
            binding.endDateButton.visibility = View.VISIBLE
            binding.startDateTextView.visibility = View.VISIBLE
            binding.endDateTextView.visibility = View.VISIBLE
        } else {
            Log.e("Shayantan", "Unimplemented")
            return
        }
        when (position) {
            0 -> getCurrentMonthExpensesAnalysis()
            1 -> getLastMonthExpensesAnalysis()
            2 -> getCurrentYearExpensesAnalysis()
            3 -> getCustomRangeAnalysis()
        }
    }



}