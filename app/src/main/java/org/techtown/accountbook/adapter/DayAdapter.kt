package org.techtown.accountbook.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import org.techtown.accountbook.databinding.ItemCalendarDayBinding
import java.util.*

class DayAdapter(val tempMonth:Int, val dayList: MutableList<Date>): RecyclerView.Adapter<DayAdapter.DayView>()  {

    val ROW = 6

    inner class DayView(val binding: ItemCalendarDayBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayView {
        var binding = ItemCalendarDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayView(binding)
    }

    override fun onBindViewHolder(holder: DayView, position: Int) {
        holder.binding.root.setOnClickListener {
            Toast.makeText(holder.binding.root.context, "${Calendar.getInstance().apply { 
                time = dayList[position]
            }.get(Calendar.YEAR)}", Toast.LENGTH_SHORT).show()
        }
        holder.binding.dayText.text = dayList[position].date.toString()

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