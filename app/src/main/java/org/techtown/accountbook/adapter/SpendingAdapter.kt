package org.techtown.accountbook.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.techtown.accountbook.Model.SpendingData
import org.techtown.accountbook.databinding.RecyclerSpendingItemBinding

class SpendingAdapter : PagingDataAdapter<SpendingData,SpendingAdapter.SpendingViewHolder>(
    diffCallback) {

    override fun onBindViewHolder(holder: SpendingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpendingViewHolder {
        return SpendingViewHolder(
            RecyclerSpendingItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    companion object{
        val diffCallback = object : DiffUtil.ItemCallback<SpendingData>(){
            override fun areItemsTheSame(oldItem: SpendingData, newItem: SpendingData) =
                oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: SpendingData, newItem: SpendingData) =
                oldItem == newItem
        }
    }

    inner class SpendingViewHolder(private val binding: RecyclerSpendingItemBinding): RecyclerView.ViewHolder(binding.root){

        @SuppressLint("SetTextI18n")
        fun bind(item: SpendingData?){
            item?.let {
                binding.time.text = "${it.year}/${it.month.toString().padStart(2,'0')}/${it.day.toString().padStart(2,'0')}"
                binding.type.text = it.type
                binding.amount.text = it.money.toString()
            }
        }
    }
}