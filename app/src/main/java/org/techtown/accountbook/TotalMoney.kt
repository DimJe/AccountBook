package org.techtown.accountbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import kotlinx.android.synthetic.main.activity_total_money.*
import org.techtown.accountbook.MainActivity.Companion.TAG

class TotalMoney : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_total_money)
        
        val type = intent.getStringExtra("type")
        Log.d(TAG, "onCreate: $type")

        hundred.setOnClickListener {
            money.text = (money.text.toString().toInt()+100).toString()
        }
        _5_hundred.setOnClickListener {
            money.text = (money.text.toString().toInt()+500).toString()
        }
        thousand.setOnClickListener {
            money.text = (money.text.toString().toInt()+1000).toString()
        }
        _5_thousand.setOnClickListener {
            money.text = (money.text.toString().toInt()+5000).toString()
        }
        ten_thousand.setOnClickListener {
            money.text = (money.text.toString().toInt()+10000).toString()
        }
        erase.setOnClickListener {
            money.text = "0"
        }
        submit.setOnClickListener {
            Log.d(TAG, "today-money : ${money.text}")
            finish()
        }

    }
}