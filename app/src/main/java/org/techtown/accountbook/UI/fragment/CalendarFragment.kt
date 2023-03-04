package org.techtown.accountbook.UI.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import org.techtown.accountbook.R
import org.techtown.accountbook.adapter.MonthAdapter
import org.techtown.accountbook.databinding.FragmentCalendarBinding
import java.util.*


class CalendarFragment : Fragment() {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private val calender = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        calender.time = Date()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCalendarBinding.inflate(inflater,container,false)

        initView()

        return binding.root
    }

    private fun initView(){
        binding.calendarRecyclerView.apply {
            val psHelper = PagerSnapHelper()
            psHelper.attachToRecyclerView(this)

            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = MonthAdapter().apply {
                setOnItemClickListener(object : MonthAdapter.OnItemClickListener{
                    override fun onItemClick(date: Date, positon: Int) {
                        Toast.makeText(requireActivity(), "${Calendar.getInstance().apply { 
                            time = date
                        }.get(Calendar.DAY_OF_MONTH)}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            scrollToPosition(Int.MAX_VALUE/2)
            addOnScrollListener(object : RecyclerView.OnScrollListener(){
                var temp = 0
                var dir = false
                var currentPos = RecyclerView.NO_POSITION
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if(temp == 0){
                        super.onScrolled(recyclerView, dx, dy)
                        if(dx==0) return
                        val view = psHelper.findSnapView(recyclerView.layoutManager)!!
                        currentPos = recyclerView.layoutManager!!.getPosition(view)
                        temp = 1
                    }

                }
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if(newState==RecyclerView.SCROLL_STATE_IDLE && temp == 1){
                        super.onScrollStateChanged(recyclerView, newState)
                        val view = psHelper.findSnapView(recyclerView.layoutManager)!!
                        val position = recyclerView.layoutManager!!.getPosition(view)
                        if(currentPos>position) {
                            calender.add(Calendar.MONTH,-1)
                            Log.d("태그", "onScrollStateChanged: ${calender.get(Calendar.YEAR)} ${calender.get(Calendar.MONTH)+1}월 ")
                        }
                        else if(currentPos<position){
                            calender.add(Calendar.MONTH,1)
                            Log.d("태그", "onScrollStateChanged:${calender.get(Calendar.YEAR)} ${calender.get(Calendar.MONTH)+1}월 ")
                        }
                        else Log.d("태그", "onScrollStateChanged: 같은 월 ")
                        temp = 0
                    }
                }
            })
        }
    }
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalendarFragment().apply {
            }
    }
}