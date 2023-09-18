package org.techtown.accountbook.UI.activity

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import org.koin.android.ext.android.inject
import org.techtown.accountbook.R
import org.techtown.accountbook.Service.MyNotificationListenerService
import org.techtown.accountbook.UI.fragment.CalendarFragment
import org.techtown.accountbook.UI.fragment.StatsFragment
import org.techtown.accountbook.ViewModel.ViewModel
import org.techtown.accountbook.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val calendarFragment = CalendarFragment()
    private val statsFragment = StatsFragment()
    private val viewModel: ViewModel by inject()

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
                .beginTransaction().setCustomAnimations(R.anim.from_right,R.anim.to_left).hide(calendarFragment).commit()
            supportFragmentManager
                .beginTransaction().setCustomAnimations(R.anim.from_right,R.anim.to_left).show(statsFragment).commit()

            binding.chart.showText()
            binding.calendar.hideText()
        }
        binding.calendar.setOnClickListener {

            supportFragmentManager
                .beginTransaction().setCustomAnimations(R.anim.from_left,R.anim.to_right).hide(statsFragment).commit()
            supportFragmentManager
                .beginTransaction().setCustomAnimations(R.anim.from_left,R.anim.to_right).show(calendarFragment).commit()

            binding.chart.hideText()
            binding.calendar.showText()
        }
        if(!isNotificationPermissionGranted()) {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
    }

    private fun isNotificationPermissionGranted(): Boolean {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.isNotificationListenerAccessGranted(ComponentName(
                application,
                MyNotificationListenerService::class.java
            ))
        } else {
            NotificationManagerCompat.getEnabledListenerPackages(applicationContext).contains(applicationContext.packageName)
        }
    }
}