package com.example.ExpenseTracker.Expense
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.ExpenseTracker.Income.Income
import com.example.ExpenseTracker.R


class IncomeAdapter(private val context: Context, private val incomes: List<Income>) : BaseAdapter() {

    override fun getCount(): Int {
        return incomes.size
    }

    override fun getItem(position: Int): Any {
        return incomes[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: IncomeViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.incomesitem_card, parent, false)
            viewHolder = IncomeViewHolder(
                view.findViewById(R.id.incomeTextNo),
                view.findViewById(R.id.incomeTextAmount),
                view.findViewById(R.id.incomeTextDescription),
                view.findViewById(R.id.incomeTextDate)
            )
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as IncomeViewHolder
        }
        val income = getItem(position) as Income
        viewHolder.textNo.text = (position + 1).toString()
        viewHolder.textAmount.text = income.amount.toString()
        viewHolder.textDescription.text = income.description
        viewHolder.textDate.text = income.date
        return view
    }

    private data class IncomeViewHolder(
        val textNo: TextView,
        val textAmount: TextView,
        val textDescription: TextView,
        val textDate: TextView
    )
}