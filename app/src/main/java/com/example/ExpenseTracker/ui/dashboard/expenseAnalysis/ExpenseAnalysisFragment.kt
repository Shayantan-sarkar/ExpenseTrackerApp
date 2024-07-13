package com.example.ExpenseTracker.ui.dashboard.expenseAnalysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ExpenseTracker.MainActivity
import com.example.ExpenseTracker.databinding.FragmentExpenseanalysisBinding

class ExpenseAnalysisFragment: Fragment() {
    private var _binding: FragmentExpenseanalysisBinding? = null
    private val binding get() = _binding!!
    var mainActivity: MainActivity? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpenseanalysisBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mainActivity = activity as MainActivity?
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