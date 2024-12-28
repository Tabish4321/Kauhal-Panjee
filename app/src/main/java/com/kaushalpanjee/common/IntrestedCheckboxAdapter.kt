package com.kaushalpanjee.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kaushalpanjee.R


class IntrestedCheckboxAdapter(private val items: List<String>) : RecyclerView.Adapter<IntrestedCheckboxAdapter.ViewHolder>() {

    // List to store selected items
    private val selectedItems = mutableSetOf<Int>()

    // ViewHolder class
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        val textView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_checkbox, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textView.text = item

        // Update checkbox state
        holder.checkBox.isChecked = selectedItems.contains(position)

        // Handle checkbox change
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedItems.add(position)
            } else {
                selectedItems.remove(position)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    // Get selected items
    fun getSelectedItems(): List<String> {
        return selectedItems.map { items[it] }
    }
}
