package com.example.ExpenseTracker
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView



class ExpenseAdapter(private val context: Context, private val expenses: List<Expense>) : BaseAdapter() {

    override fun getCount(): Int {
        return expenses.size
    }

    override fun getItem(position: Int): Any {
        return expenses[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false)
            viewHolder = ViewHolder(
                view.findViewById(R.id.textNo),
                view.findViewById(R.id.textAmount),
                view.findViewById(R.id.textDescription),
                view.findViewById(R.id.textDate)
            )
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val expense = getItem(position) as Expense
        viewHolder.textNo.text = (position + 1).toString()
        viewHolder.textAmount.text = expense.amount.toString()
        viewHolder.textDescription.text = expense.description
        viewHolder.textDate.text = expense.date

        return view
    }

    private data class ViewHolder(
        val textNo: TextView,
        val textAmount: TextView,
        val textDescription: TextView,
        val textDate: TextView
    )
}