package com.kaushalpanjee.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kaushalpanjee.R
import com.kaushalpanjee.common.model.response.WrappedListItem

class SeccAdapter : ListAdapter<WrappedListItem, SeccAdapter.SeccDetailsViewHolder>(SeccDetailsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeccDetailsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.secc_item_layout, parent, false) // Replace with your layout file
        return SeccDetailsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeccDetailsViewHolder, position: Int) {
        val wrappedListItem = getItem(position)
        holder.bind(wrappedListItem)
    }

    class SeccDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fatherNameTextView: TextView = itemView.findViewById(R.id.fatherNameTextView) // Replace with your TextView ID
        private val ahltinTextView: TextView = itemView.findViewById(R.id.ahltinTextView) // Replace with your TextView ID
        private val seccNameTextView: TextView = itemView.findViewById(R.id.nameTextView) // Replace with your TextView ID

        fun bind(wrappedListItem: WrappedListItem) {
            fatherNameTextView.text = wrappedListItem.fatherName
            ahltinTextView.text = wrappedListItem.ahltin
            seccNameTextView.text = wrappedListItem.seccName
        }
    }

    class SeccDetailsDiffCallback : DiffUtil.ItemCallback<WrappedListItem>() {
        override fun areItemsTheSame(oldItem: WrappedListItem, newItem: WrappedListItem): Boolean {
            return oldItem.ahltin == newItem.ahltin
        }

        override fun areContentsTheSame(oldItem: WrappedListItem, newItem: WrappedListItem): Boolean {
            return oldItem == newItem
        }
    }
}
