package org.techtown.accountbook.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.techtown.accountbook.Model.SpendingUiData
import org.techtown.accountbook.databinding.ItemCalendarDayBinding
import java.util.*

class DayAdapter(val tempMonth:Int, val dayList: MutableList<Date>,var uiData: List<SpendingUiData>): RecyclerView.Adapter<DayAdapter.DayView>()  {

    val ROW = 6

    interface OnItemClickListener{
        fun onItemClick(date: Date, position : Int)
    }
    private var listener : OnItemClickListener? = null
    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    inner class DayView(val binding: ItemCalendarDayBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayView {
        var binding = ItemCalendarDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayView(binding)
    }

    override fun onBindViewHolder(holder: DayView, position: Int) {

        holder.binding.root.setOnClickListener {
            listener?.onItemClick(dayList[position],position)
        }

        holder.binding.dayText.text = dayList[position].date.toString()
        uiData.find {
            it.month==dayList[position].month+1 && it.day==dayList[position].date
        }.apply {
            if(this==null) {
                holder.binding.outputMoney.text = "0"
            } else {
                this.money.toString().also { holder.binding.outputMoney.text = it }
            }
        }


        holder.binding.dayText.setTextColor(when(position % 7) {
            0 -> Color.RED
            6 -> Color.BLUE
            else -> Color.BLACK
        })

        if(tempMonth != dayList[position].month) {
            holder.binding.root.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return ROW * 7
    }
}