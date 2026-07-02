package com.kaushalpanjee.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kaushalpanjee.common.model.BankItem
import com.kaushalpanjee.databinding.ItemBankBinding

/**
 * Created by Rishi Porwal
 */
class BankAdapter : RecyclerView.Adapter<BankAdapter.ViewHolder>() {

    private val list = mutableListOf<BankItem>()

    fun setData(data: List<BankItem>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemBankBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.tvIfsc.text = item.ifscCode
        holder.binding.tvBankName.text = item.bankName
        holder.binding.tvAccount.text = item.accountNumber
        holder.binding.tvPan.text = item.panNo
    }
}