package org.techtown.accountbook.UI.activity

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.migration.CustomInjection.inject
import org.techtown.accountbook.Model.SpendingData
import org.techtown.accountbook.R
import org.techtown.accountbook.Service.MyNotificationListenerService
import org.techtown.accountbook.UI.dialog.AddSpendingDataDialog
import org.techtown.accountbook.UI.dialog.DialogListener
import org.techtown.accountbook.UI.fragment.CalendarFragment
import org.techtown.accountbook.UI.fragment.CalendarFragmentDirections
import org.techtown.accountbook.UI.fragment.StatsFragment
import org.techtown.accountbook.UI.fragment.StatsFragmentDirections
import org.techtown.accountbook.ViewModel.ViewModel
import org.techtown.accountbook.databinding.ActivityMainBinding
import timber.log.Timber
import java.util.Calendar
import java.util.Date

/*
hilt변경
navigation
ui design
그냥 알림 권한
*/
@AndroidEntryPoint
class MainActivity : AppCompatActivity(),DialogListener {

    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel: ViewModel by viewModels()
    private val navController: NavController by lazy {
        findNavController(R.id.fragment)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    override fun onResume() {
        super.onResume()
        val flag = intent.getBooleanExtra("addSpending",false)
        if(flag){
            Timber.e("flag == true")
            Timber.e("value = ${intent.getStringExtra("money")}")
            showDialog(money = intent.getStringExtra("money")?:"")
        }

    }
    private fun initView(){

        binding.chart.hideText()


        binding.chart.setOnClickListener {

            navController.navigate(CalendarFragmentDirections.actionCalendarFragmentToStatsFragment())

            binding.chart.showText()
            binding.calendar.hideText()
        }
        binding.calendar.setOnClickListener {

            navController.navigate(StatsFragmentDirections.actionStatsFragmentToCalendarFragment())

            binding.chart.hideText()
            binding.calendar.showText()
        }
        if(!isNotificationPermissionGranted()) {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
        //startForegroundService(Intent(this,MyNotificationListenerService::class.java))

    }
    fun showDialog(date: Date = Date(), money: String = ""){
        AddSpendingDataDialog(this,date,money).apply {
            show(supportFragmentManager,"spending")
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
    override fun submitData(money: Int, type: String, date: Date) {
        //db insert
        val cal = Calendar.getInstance().apply { time = date }
        viewModel.insertData(
            SpendingData(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(
                Calendar.DAY_OF_MONTH),type,money)
        )
    }
}