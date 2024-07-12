package com.example.ExpenseTracker.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ExpenseTracker.Expense
import com.example.ExpenseTracker.ExpenseAdapter
import com.example.ExpenseTracker.MainActivity
import com.example.ExpenseTracker.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    var adapter: ExpenseAdapter? = null
    var mainActivity: MainActivity? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mainActivity = activity as MainActivity?
        val expenseItems: List<Expense> = mainActivity!!.expenseManager.getExpenses()
        if(expenseItems!=null)
            adapter = ExpenseAdapter(requireContext(), expenseItems)
        val listView: ListView = binding.expenseListView
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