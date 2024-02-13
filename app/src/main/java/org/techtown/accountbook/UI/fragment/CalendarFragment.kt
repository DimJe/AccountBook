package org.techtown.accountbook.UI.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.techtown.accountbook.Model.SpendingData
import org.techtown.accountbook.Repository.ResultState
import org.techtown.accountbook.UI.activity.MainActivity
import org.techtown.accountbook.UI.dialog.AddSpendingDataDialog
import org.techtown.accountbook.UI.dialog.DialogListener
import org.techtown.accountbook.ViewModel.ViewModel
import org.techtown.accountbook.adapter.MonthAdapter
import org.techtown.accountbook.databinding.FragmentCalendarBinding
import timber.log.Timber
import java.util.*


class CalendarFragment : Fragment() {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private val calender = Calendar.getInstance()
    private val viewModel: ViewModel by activityViewModel()
    lateinit var calAdapter: MonthAdapter

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
                                calAdapter.submitSendingData(list)
                            }
                            it.data?.forEach {
                                Timber.e("$it")
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

            setHasFixedSize(true)
            setItemViewCacheSize(3)
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = calAdapter.apply {
                setOnItemClickListener(object : MonthAdapter.OnItemClickListener{
                    override fun onItemClick(date: Date, position: Int) {
                        (requireActivity() as MainActivity).showDialog(date = date)
                    }
                })
            }
            scrollToPosition(Int.MAX_VALUE/2)
            addOnScrollListener(object : RecyclerView.OnScrollListener(){
                var temp = 0
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
                            viewModel.getUiData(calender.get(Calendar.YEAR),calender.get(Calendar.MONTH)+1)
                        }
                        else if(currentPos<position){
                            calender.add(Calendar.MONTH,1)
                            viewModel.getUiData(calender.get(Calendar.YEAR),calender.get(Calendar.MONTH)+1)
                        }
                        temp = 0
                    }
                }
            })
        }
    }
}