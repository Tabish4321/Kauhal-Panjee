package com.kaushalpanjee.common

import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kaushalpanjee.databinding.ItemSchemeBinding
import com.kaushalpanjee.databinding.ItemSchemePointBinding
import com.kaushalpanjee.model.Scheme

class SchemeDetailAdapter(private val schemeDetails: ArrayList<String>) :
    RecyclerView.Adapter<SchemeDetailAdapter.SchemeDetailAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchemeDetailAdapterViewHolder {
        return SchemeDetailAdapterViewHolder(ItemSchemePointBinding.inflate(from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SchemeDetailAdapterViewHolder, position: Int) {
        holder.bind(schemeDetails[position])
    }

    override fun getItemCount(): Int {
        return schemeDetails.size
    }

    inner class SchemeDetailAdapterViewHolder(private val binding: ItemSchemePointBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(schemePoint: String) {
            binding.data = schemePoint
        }
    }
}