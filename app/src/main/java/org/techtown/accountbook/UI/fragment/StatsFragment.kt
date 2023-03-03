package org.techtown.accountbook.UI.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.techtown.accountbook.R

class StatsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("태그", "stats onCreate: called")
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d("태그", "stats onCreateView: called")
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StatsFragment().apply {

            }
    }
}