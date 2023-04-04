package org.techtown.accountbook.UI.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.techtown.accountbook.Model.SpendingData
import org.techtown.accountbook.Model.SpendingDataDao
import org.techtown.accountbook.R
import org.techtown.accountbook.Repository.ResultState
import org.techtown.accountbook.UI.dialog.AddSpendingDataDialog
import org.techtown.accountbook.UI.dialog.DialogListener
import org.techtown.accountbook.ViewModel.ViewModel
import org.techtown.accountbook.adapter.MonthAdapter
import org.techtown.accountbook.databinding.FragmentCalendarBinding
import java.util.*


class CalendarFragment : Fragment(),DialogListener {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private val calender = Calendar.getInstance()
    private val viewModel: ViewModel by activityViewModel()
    lateinit var calAdapter: MonthAdapter
    val dao: SpendingDataDao by inject()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.spendingUiData.collect{
                    when(it){
                        is ResultState.Error -> {
                            Log.d("태그", "onViewCreated:${it.message} ")
                        }
                        is ResultState.Loading -> {}
                        is ResultState.Success -> {
                            it.data?.let {list ->
                                list.forEach {value ->
                                    Log.d("태그", "onViewCreated: $value")
                                }
                                calAdapter.submitSendingData(list)
                            }
                        }
                    }
                }
            }
        }
        viewModel.getUiData(calender.get(Calendar.YEAR),calender.get(Calendar.MONTH)+1)

    }
    private fun initView(){

        calAdapter = MonthAdapter(listOf())

        binding.calendarRecyclerView.apply {
            val psHelper = PagerSnapHelper()
            psHelper.attachToRecyclerView(this)


            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = calAdapter.apply {
                setOnItemClickListener(object : MonthAdapter.OnItemClickListener{
                    override fun onItemClick(date: Date, positon: Int) {
                        Toast.makeText(requireActivity(), "${Calendar.getInstance().apply { 
                            time = date
                        }.get(Calendar.DAY_OF_MONTH)}", Toast.LENGTH_SHORT).show()
                        AddSpendingDataDialog(this@CalendarFragment,date).apply {
                            show(this@CalendarFragment.requireActivity().supportFragmentManager,"spending")
                        }
                    }
                })
            }
            scrollToPosition(Int.MAX_VALUE/2)
            addOnScrollListener(object : RecyclerView.OnScrollListener(){
                var temp = 0
                //var dir = false
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
                            viewModel.getUiData(calender.get(Calendar.YEAR),calender.get(Calendar.MONTH)+1)
                        }
                        else if(currentPos<position){
                            calender.add(Calendar.MONTH,1)
                            Log.d("태그", "onScrollStateChanged:${calender.get(Calendar.YEAR)} ${calender.get(Calendar.MONTH)+1}월 ")
                            viewModel.getUiData(calender.get(Calendar.YEAR),calender.get(Calendar.MONTH)+1)
                        }
                        else Log.d("태그", "onScrollStateChanged: 같은 월 ")
                        temp = 0
                    }
                }
            })
        }
    }
    override fun submitData(money: Int, type: String, date: Date) {
        //db insert
        val cal = Calendar.getInstance().apply { time = date }
        viewModel.insertData(SpendingData(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH),type,money))
    }
}