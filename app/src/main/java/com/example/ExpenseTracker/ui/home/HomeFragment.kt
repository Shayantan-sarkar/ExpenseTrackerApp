package com.example.ExpenseTracker.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ExpenseTracker.MainActivity
import com.example.ExpenseTracker.R
import com.example.ExpenseTracker.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

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
        return root
    }

    fun onAddButtonClicked(view: View) {
        Log.e("Shayantan", "onAddButtonClicked")
        var descEditText: EditText = binding.editTextDescription
        var dateEditText: EditText = binding.editTextDate
        var amountEditText: EditText = binding.editTextAmount
        var expenseTypeTextView: Spinner = binding.editTextExpenseCategory
        if(mainActivity==null||amountEditText.text.isEmpty())
        {
            return
        }
        val desc=descEditText.text.toString()
        val date=dateEditText.text.toString()
        val amount=amountEditText.text.toString().toDouble()
        val expenseType=expenseTypeTextView.selectedItem.toString()
        mainActivity!!.registerExpense(desc,date,amount, expenseType)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}