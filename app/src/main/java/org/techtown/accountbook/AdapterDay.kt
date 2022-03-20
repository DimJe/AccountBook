package org.techtown.accountbook

import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_day.view.*
import org.techtown.accountbook.MainActivity.Companion.TAG
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AdapterDay(val tempMonth:Int, val dayList: MutableList<Date>): RecyclerView.Adapter<AdapterDay.DayView>() {
    val ROW = 6
    val current = LocalDateTime.now()
    val forMatter = DateTimeFormatter.ofPattern("dd")
    val forMonth = DateTimeFormatter.ofPattern("MM")
    val forMonthed = current.format(forMonth)
    val forMatted = current.format(forMatter)
    inner class DayView(val layout: View): RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayView {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_day, parent, false)
        return DayView(view)
    }

    override fun onBindViewHolder(holder: DayView, position: Int) {
        Log.d(TAG, "${forMonthed.toInt()} , $tempMonth")
        holder.layout.item_day_layout.setOnClickListener {
            Toast.makeText(holder.layout.context, "${dayList[position]}", Toast.LENGTH_SHORT).show()
        }
        holder.layout.item_day_text.text = dayList[position].date.toString()

        holder.layout.item_day_text.setTextColor(when(position % 7) {
            0 -> Color.RED
            6 -> Color.BLUE
            else -> Color.BLACK
        })
        if(tempMonth+2 == forMonthed.toInt() && dayList[position].date.toString() == forMatted)
            holder.layout.item_day_layout.setBackgroundResource(R.drawable.stroke)

        if(tempMonth != dayList[position].month - 1) {
            holder.layout.item_day_text.alpha = 0.4f
            holder.layout.item_day_text.setTypeface(null, Typeface.NORMAL)
        }
    }

    override fun getItemCount(): Int {
        return ROW * 7
    }
}