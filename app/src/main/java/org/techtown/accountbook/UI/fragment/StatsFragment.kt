package org.techtown.accountbook.UI.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.techtown.accountbook.Model.SpendingChartingData
import org.techtown.accountbook.Repository.ResultState
import org.techtown.accountbook.UI.custom.OnSwipeTouchListener
import org.techtown.accountbook.ViewModel.ViewModel
import org.techtown.accountbook.adapter.SpendingAdapter
import org.techtown.accountbook.databinding.FragmentStatsBinding
import java.util.Calendar
import java.util.Date

class StatsFragment : Fragment() {

    private var _binding : FragmentStatsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ViewModel by activityViewModel()
    private val calender = Calendar.getInstance()
    lateinit var adapter: SpendingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        calender.time = Date()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStatsBinding.inflate(inflater,container,false)
        initView()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestPagingData(calender.get(Calendar.YEAR),calender.get(Calendar.MONTH)+1)
        viewModel.getChartingData(calender.get(Calendar.YEAR),calender.get(Calendar.MONTH)+1)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    viewModel.spendingChartingData.collect{
                        when(it){
                            is ResultState.Error -> {
                                Log.d("태그", "onViewCreated:${it.message} ")
                            }
                            is ResultState.Loading -> {}
                            is ResultState.Success -> {
                                it.data?.let {list ->
                                    setData(binding.chart,list)
                                    setTitle(list)
                                }
                            }
                        }
                    }
                }
                launch {
                    viewModel.pagingDataFlow.collect {
                        adapter.submitData(it)
                    }
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView(){
        initBarChart(binding.chart)

        binding.chart.setOnTouchListener(object : OnSwipeTouchListener(requireContext()){
            override fun onSwipeLeft() {
                calender.add(Calendar.MONTH,1)
                viewModel.getChartingData(calender.get(Calendar.YEAR),calender.get(Calendar.MONTH)+1)
                viewModel.requestPagingData(calender.get(Calendar.YEAR),calender.get(Calendar.MONTH)+1)
            }
            override fun onSwipeRight() {
                calender.add(Calendar.MONTH,-1)
                viewModel.getChartingData(calender.get(Calendar.YEAR),calender.get(Calendar.MONTH)+1)
                viewModel.requestPagingData(calender.get(Calendar.YEAR),calender.get(Calendar.MONTH)+1)
            }
        })

        adapter = SpendingAdapter()
        binding.spendingList.adapter = adapter
        binding.spendingList.addItemDecoration(DividerItemDecoration(requireContext(),LinearLayout.VERTICAL))
    }

    private fun initBarChart(barChart: BarChart){
        barChart.setDrawGridBackground(false)
        // 막대 그림자 설정 (default = false)
        barChart.setDrawBarShadow(false)
        // 차트 테두리 설정 (default = false)
        barChart.setDrawBorders(false)

        val description = Description()
        // 오른쪽 하단 모서리 설명 레이블 텍스트 표시 (default = false)
        description.isEnabled = false
        barChart.description = description

        // X, Y 바의 애니메이션 효과
        barChart.animateY(1000)
        barChart.animateX(1000)

        // 바텀 좌표 값
        val xAxis: XAxis = barChart.xAxis
        // x축 위치 설정
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        // 그리드 선 수평 거리 설정
        xAxis.granularity = 1f
        // x축 텍스트 컬러 설정
        xAxis.textColor = Color.RED
        // x축 선 설정 (default = true)
        xAxis.setDrawAxisLine(false)
        // 격자선 설정 (default = true)
        xAxis.setDrawGridLines(false)

        val leftAxis: YAxis = barChart.axisLeft
        // 좌측 선 설정 (default = true)
        leftAxis.setDrawAxisLine(false)
        // 좌측 텍스트 컬러 설정
        leftAxis.textColor = Color.BLACK

        val rightAxis: YAxis = barChart.axisRight
        // 우측 선 설정 (default = true)
        rightAxis.setDrawAxisLine(false)
        // 우측 텍스트 컬러 설정
        rightAxis.textColor = Color.BLACK

        // 바차트의 타이틀
        val legend: Legend = barChart.legend
        // 범례 모양 설정 (default = 정사각형)
        legend.form = Legend.LegendForm.LINE
        // 타이틀 텍스트 사이즈 설정
        legend.textSize = 20f
        // 타이틀 텍스트 컬러 설정
        legend.textColor = Color.BLACK
        // 범례 위치 설정
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        // 범례 방향 설정
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        // 차트 내부 범례 위치하게 함 (default = false)
        legend.setDrawInside(false)
    }
    private fun setData(barChart: BarChart,data: List<SpendingChartingData>){
        barChart.setScaleEnabled(false)

        val valueList = ArrayList<BarEntry>()
        val title = "일별 사용 금액"

        data.forEach {
            valueList.add(BarEntry(it.day.toFloat(),it.money.toFloat()))
        }

        val barDataSet = BarDataSet(valueList,title)
        barDataSet.color = Color.BLACK

        val data = BarData(barDataSet)
        barChart.data = data
        barChart.invalidate()
    }

    @SuppressLint("SetTextI18n")
    private fun setTitle(list: List<SpendingChartingData>){

        val total = list.sumOf { it.money }
        val max = list.maxBy { it.money }.money

        binding.totalCount.text = "${calender.get(Calendar.MONTH)+1}월 총 금액 : $total"
        binding.highestSpending.text = "${calender.get(Calendar.MONTH)+1}월 최고 금액 : $max"
    }
}