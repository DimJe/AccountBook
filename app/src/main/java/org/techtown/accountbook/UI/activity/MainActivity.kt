package org.techtown.accountbook.UI.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.techtown.accountbook.R
import org.techtown.accountbook.UI.fragment.CalendarFragment
import org.techtown.accountbook.UI.fragment.StatsFragment
import org.techtown.accountbook.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val calendarFragment = CalendarFragment()
    private val statsFragment = StatsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()

    }
    private fun initView(){

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment,calendarFragment)
            .commit()
        supportFragmentManager
            .beginTransaction().add(R.id.fragment,statsFragment).hide(statsFragment).commit()
        binding.chart.hideText()
        binding.chart.setOnClickListener {

            //replace로 fragment전환을 하면 계속 새롭게 생성된다.
            //show와 hide를 사용해서 이를 방지한다.
            supportFragmentManager
                .beginTransaction().hide(calendarFragment).commit()
            supportFragmentManager
                .beginTransaction().show(statsFragment).commit()

            binding.chart.showText()
            binding.calendar.hideText()
        }
        binding.calendar.setOnClickListener {

            supportFragmentManager
                .beginTransaction().hide(statsFragment).commit()
            supportFragmentManager
                .beginTransaction().show(calendarFragment).commit()

            binding.chart.hideText()
            binding.calendar.showText()
        }
    }
}