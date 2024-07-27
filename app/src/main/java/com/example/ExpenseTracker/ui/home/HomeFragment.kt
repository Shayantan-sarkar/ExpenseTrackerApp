package com.example.ExpenseTracker.ui.home

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ExpenseTracker.Date
import com.example.ExpenseTracker.ExpenseCalendar
import com.example.ExpenseTracker.MainActivity
import com.example.ExpenseTracker.R
import com.example.ExpenseTracker.databinding.FragmentHomeBinding
import java.util.Calendar

class HomeFragment : Fragment() {

    var recordTypeTextView: Spinner? = null
    var descEditText: EditText? = null
    var dateTextView: TextView? = null
    var amountEditText: EditText? = null
    var expenseTypeTextView: Spinner? = null
    private var _binding: FragmentHomeBinding? = null
    var mainActivity:MainActivity? = null
    val calendar = ExpenseCalendar.getInstance()
    val date: Date = calendar.getCurrentDate()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mainActivity = getActivity() as MainActivity?
        binding.buttonAddExpense.setOnClickListener(this::onAddButtonClicked)
        binding.dateButton.setOnClickListener(this::onSelectDateButtonClicked)
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recordTypeTextView = binding.editRecordType
        descEditText = binding.editTextDescription
        dateTextView = binding.dateTextView
        amountEditText= binding.editTextAmount
        expenseTypeTextView= binding.editTextExpenseCategory

        recordTypeTextView!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position)
                Log.e("Shayantan", "Selected item:at position: $position")
                handleRecordType(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        descEditText!!.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                descriptionAdded(descEditText!!.text.toString())
            }
        }

    }
    private fun descriptionAdded(desc: String)
    {
        if(expenseTypeTextView==null||desc.isEmpty())
            return
        var predicted = mainActivity!!.predictor.predict(desc)
        expenseTypeTextView!!.setSelection(categoryStringToInt(predicted))
        Log.e("shayantan", "Machine Learning Model Description: $desc, Predicted: $predicted")
    }
    private fun categoryStringToInt(category: String): Int {
        when (category) {
            "Others" -> return 0
            "Transportation" -> return 1
            "Entertainment" -> return 2
            "Utilities" -> return 3
            "Housing" -> return 4
            "Education" -> return 5
            "Health" -> return 6
            "Food" -> return 7
            else -> {Log.e("shayantan", "Unimplemented category $category")
                return 0}
        }
    }

    fun onSelectDateButtonClicked(view: View) {
        calendar.chooseDate(this.requireContext(), date, dateTextView!!)
    }
    fun onAddButtonClicked(view: View) {
        if(mainActivity==null||amountEditText!!.text.isEmpty())
        {
            return
        }
        val desc=descEditText!!.text.toString()
        val date=dateTextView!!.text.toString()
        val amount=amountEditText!!.text.toString().toDouble()
        val recordType=recordTypeTextView!!.selectedItem.toString()
        when(recordType)
        {
            "Income"->mainActivity!!.registerIncome(desc,date,amount)
            "Expense"->mainActivity!!.registerExpense(desc,date,amount, expenseTypeTextView!!.selectedItem.toString())
        }
    }
    private fun handleRecordType(position: Int) {
        if (position == 1) {
            binding.editTextExpenseCategory.visibility = View.GONE
        } else if (position == 0){
            binding.editTextExpenseCategory.visibility = View.VISIBLE
        }
        else
        {
            Log.e("Shayantan","Unimplemented")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}