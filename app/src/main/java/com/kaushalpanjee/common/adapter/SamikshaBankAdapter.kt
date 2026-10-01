package com.kaushalpanjee.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kaushalpanjee.R

class SamikshaBankAdapter(
    private var banks: List<com.d2k.samiksha.domain.Bank>,
    private val onBankSelected: (com.d2k.samiksha.domain.Bank) -> Unit
) : RecyclerView.Adapter<SamikshaBankAdapter.BankViewHolder>() {

    private var filteredBanks = banks

    inner class BankViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvBankName: TextView =
            itemView.findViewById(R.id.tvBankName)

        fun bind(bank: com.d2k.samiksha.domain.Bank) {

            tvBankName.text = bank.displayName

            itemView.setOnClickListener {
                onBankSelected(bank)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BankViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_samiksha_bank,
                parent,
                false
            )

        return BankViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: BankViewHolder,
        position: Int
    ) {
        holder.bind(filteredBanks[position])
    }

    override fun getItemCount(): Int = filteredBanks.size

    fun filter(query: String) {

        filteredBanks = if (query.isBlank()) {
            banks
        } else {
            banks.filter {
                it.displayName
                    .contains(query, ignoreCase = true)
            }
        }

        notifyDataSetChanged()
    }
}