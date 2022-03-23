package org.techtown.accountbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import kotlinx.android.synthetic.main.activity_add.*
import org.techtown.accountbook.MainActivity.Companion.TAG

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val monthListManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val monthListAdapter = AdapterMonth()

        calendar_custom.apply {
            layoutManager = monthListManager
            adapter = monthListAdapter
            scrollToPosition(Int.MAX_VALUE/2)
        }
        val snap = PagerSnapHelper()
        snap.attachToRecyclerView(calendar_custom)

        eat.setOnClickListener {
            val intent = Intent(this,TotalMoney::class.java)
            intent.putExtra("type","식비")
            startActivity(intent)
        }
        beer.setOnClickListener {
            val intent = Intent(this,TotalMoney::class.java)
            intent.putExtra("type","주류")
            startActivity(intent)
        }
        culture.setOnClickListener {
            val intent = Intent(this,TotalMoney::class.java)
            intent.putExtra("type","문화 및 여가")
            startActivity(intent)
        }
        snack.setOnClickListener {
            val intent = Intent(this,TotalMoney::class.java)
            intent.putExtra("type","간식")
            startActivity(intent)
        }
        tax.setOnClickListener {
            val intent = Intent(this,TotalMoney::class.java)
            intent.putExtra("type","공과금")
            startActivity(intent)
        }
        etc.setOnClickListener {
            val intent = Intent(this,TotalMoney::class.java)
            intent.putExtra("type","기다")
            startActivity(intent)
        }

    }
}