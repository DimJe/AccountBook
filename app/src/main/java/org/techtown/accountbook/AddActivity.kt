package org.techtown.accountbook

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
            
        }
        beer.setOnClickListener {

        }
        culture.setOnClickListener {

        }
        idontknow.setOnClickListener {

        }
        tax.setOnClickListener {

        }
        etc.setOnClickListener {

        }

    }
}