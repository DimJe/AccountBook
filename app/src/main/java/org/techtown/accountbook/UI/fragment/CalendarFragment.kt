package org.techtown.accountbook.UI.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import org.techtown.accountbook.R
import org.techtown.accountbook.adapter.MonthAdapter
import org.techtown.accountbook.databinding.FragmentCalendarBinding


class CalendarFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = MonthAdapter()
            scrollToPosition(Int.MAX_VALUE/2)
        }
        PagerSnapHelper().attachToRecyclerView(binding.calendarRecyclerView)
    }
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalendarFragment().apply {
            }
    }
}