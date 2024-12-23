package com.kaushalpanjee.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.kaushalpanjee.common.model.WrappedList
import com.kaushalpanjee.core.util.gone
import com.kaushalpanjee.core.util.visible
import com.kaushalpanjee.databinding.ItemStateBinding

class StateAdaptor(private val itemClickListener:ItemClickListener) : RecyclerView.Adapter<StateAdaptor.StateAdaptorVH>() {

    private var stateList = mutableListOf<WrappedList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateAdaptorVH {
       return  StateAdaptorVH(ItemStateBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return stateList.size
    }

    override fun onBindViewHolder(holder: StateAdaptorVH, position: Int) {
        holder.bindData(wrappedList = stateList[position])
    }


    fun setData(list : MutableList<WrappedList>){
        stateList.apply {
            clear()
            addAll(list)
        }

        notifyItemRangeChanged(0, stateList.size)
    }

    inner class StateAdaptorVH(private val stateItem : ItemStateBinding) : RecyclerView.ViewHolder(stateItem.root){

        fun bindData(wrappedList: WrappedList){

            stateItem.tvStateName.text = wrappedList.stateName

            if (wrappedList.isSelected){
              stateItem.ivSelected.visible()
            }else stateItem.ivSelected.gone()


            stateItem.root.setOnClickListener {
                itemClickListener.onItemClick(adapterPosition)
                notifyItemSelected(stateList , adapterPosition)
            }
        }
    }

    fun notifyItemSelected(list: MutableList<WrappedList> , selectedPosition : Int){
        list.forEachIndexed { index, wrappedList ->
            wrappedList.isSelected = selectedPosition == index
        }
        notifyItemRangeChanged(0, stateList.size)
    }

    interface ItemClickListener{
        fun onItemClick(position : Int)
    }
}