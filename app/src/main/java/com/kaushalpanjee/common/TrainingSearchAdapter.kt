package com.kaushalpanjee.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kaushalpanjee.R
import com.kaushalpanjee.common.model.response.Center

class TrainingSearchAdapter(private val onItemClick: (Center) -> Unit) :
    ListAdapter<Center, TrainingSearchAdapter.TrainingSearchViewHolder>(SeccDetailsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingSearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.training__center_search, parent, false)
        return TrainingSearchViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: TrainingSearchViewHolder, position: Int) {
        val wrappedListItem = getItem(position)
        holder.bind(wrappedListItem)
    }

    class TrainingSearchViewHolder(itemView: View, private val onItemClick: (Center) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val trainingNameTextView: TextView = itemView.findViewById(R.id.trainingnameTextView)

        fun bind(wrappedListItem: Center) {
            trainingNameTextView.text = wrappedListItem.centerName

            itemView.setOnClickListener {
                onItemClick(wrappedListItem)
            }
        }
    }

    class SeccDetailsDiffCallback : DiffUtil.ItemCallback<Center>() {
        override fun areItemsTheSame(oldItem: Center, newItem: Center): Boolean {
            return oldItem.centerName == newItem.centerName
        }

        override fun areContentsTheSame(oldItem: Center, newItem: Center): Boolean {
            return oldItem == newItem
        }
    }
}

