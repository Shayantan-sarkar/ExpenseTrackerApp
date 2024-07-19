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
    // This property is only valid between onCreateView and
    // onDestroyView.
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


    }
    fun onSelectDateButtonClicked(view: View) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this.requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                //val formattedDate = "${selectedYear}/${selectedMonth + 1}/${selectedDay}"
                val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                dateTextView!!.text = formattedDate
            },
            year, month, day
        )

        datePickerDialog.show()
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