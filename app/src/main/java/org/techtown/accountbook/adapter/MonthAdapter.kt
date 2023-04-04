package org.techtown.accountbook.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.techtown.accountbook.Model.SpendingUiData
import org.techtown.accountbook.databinding.ItemCalendarMonthBinding
import java.util.*

class MonthAdapter(var uiData: List<SpendingUiData>): RecyclerView.Adapter<MonthAdapter.MonthView>(){

    private val center = Int.MAX_VALUE / 2
    private var calendar = Calendar.getInstance()
    //lateinit var dayListAdapter: DayAdapter

    interface OnItemClickListener{
        fun onItemClick(date: Date, positon : Int)
    }
    private var listener : OnItemClickListener? = null
    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    inner class MonthView(val binding: ItemCalendarMonthBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthView {
        val binding = ItemCalendarMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MonthView(binding)
    }

    override fun onBindViewHolder(holder: MonthView, position: Int) {
        calendar.time = Date()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.add(Calendar.MONTH, position - center)

        holder.binding.monthText.text = "${calendar.get(Calendar.YEAR)}년 ${calendar.get(Calendar.MONTH) + 1}월"
        val tempMonth = calendar.get(Calendar.MONTH)

        val dayList: MutableList<Date> = MutableList(6 * 7) { Date() }
        for(i in 0..5) {
            for(k in 0..6) {
                calendar.add(Calendar.DAY_OF_MONTH, (1-calendar.get(Calendar.DAY_OF_WEEK)) + k)
                dayList[i * 7 + k] = calendar.time
            }
            calendar.add(Calendar.WEEK_OF_MONTH, 1)
        }

        val dayListAdapter = DayAdapter(tempMonth, dayList, uiData)

        holder.binding.monthDay.apply {
            layoutManager = GridLayoutManager(holder.binding.root.context, 7)
            dayListAdapter.setOnItemClickListener(object : DayAdapter.OnItemClickListener{
                override fun onItemClick(date: Date, position: Int) {
                    listener?.onItemClick(date,position)
                }

            })
            adapter = dayListAdapter

        }
    }
    fun submitSendingData(data: List<SpendingUiData>){
        try {
            uiData = data
            notifyDataSetChanged()

        }catch (e:Exception){
            Log.d("태그", "submitSendingData:${e.message} ")
        }
    }
    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }
}