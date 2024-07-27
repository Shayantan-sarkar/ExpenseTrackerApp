package com.example.ExpenseTracker.ui.dashboard.expenseList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.ExpenseTracker.Expense.Expense
import com.example.ExpenseTracker.Expense.ExpenseAdapter
import com.example.ExpenseTracker.Expense.IncomeAdapter
import com.example.ExpenseTracker.Income.Income
import com.example.ExpenseTracker.MainActivity
import com.example.ExpenseTracker.databinding.FragmentExpenselistBinding
import com.example.ExpenseTracker.databinding.FragmentIncomelistBinding

class IncomeListFragment : Fragment() {
    private var _binding: FragmentIncomelistBinding? = null
    private val binding get() = _binding!!
    var adapter: IncomeAdapter? = null
    var mainActivity: MainActivity? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIncomelistBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mainActivity = activity as MainActivity?
        val incomeItems: List<Income>? = mainActivity!!.incomeManager!!.getIncomes()
        if(incomeItems!=null)
            adapter = IncomeAdapter(requireContext(), incomeItems!!)
        val listView: ListView = binding.incomeListView
        if (listView != null&&adapter!=null) {
            listView.setAdapter(adapter)
        }
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