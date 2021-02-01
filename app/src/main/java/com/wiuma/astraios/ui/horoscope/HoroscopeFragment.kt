package com.wiuma.astraios.ui.horoscope

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wiuma.astraios.R
import com.wiuma.astraios.adapters.HoroscopeRecyclerAdapter

class HoroscopeFragment : Fragment() {

    private lateinit var horoscopeViewModel: HoroscopeViewModel
    private val recyclerViewAdapter = HoroscopeRecyclerAdapter(arrayListOf())
    private lateinit var horoscopeRecyclerView: RecyclerView
    private lateinit var errorText: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        horoscopeViewModel =
                ViewModelProvider(this).get(HoroscopeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_horoscope, container, false)
        horoscopeViewModel.fetchData()
        errorText = root.findViewById(R.id.errorText)
        progressBar = root.findViewById(R.id.progressBar)
        horoscopeRecyclerView = root.findViewById(R.id.horoscopeRecyclerView)
        horoscopeRecyclerView.layoutManager = LinearLayoutManager(context)
        horoscopeRecyclerView.adapter = recyclerViewAdapter
        observeData()
        return root
    }

    fun observeData() {
        horoscopeViewModel.horoscopeList.observe(viewLifecycleOwner, Observer { horoscope ->
            horoscope?.let {
                errorText.visibility = View.GONE
                progressBar.visibility = View.GONE
                horoscopeRecyclerView.visibility = View.VISIBLE
                recyclerViewAdapter.updateList(it)
            }
        })

        horoscopeViewModel.loadingStatus.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) {
                    progressBar.visibility = View.VISIBLE
                    errorText.visibility = View.GONE
                    horoscopeRecyclerView.visibility = View.GONE
                }
            }
        })

        horoscopeViewModel.successBoolean.observe(viewLifecycleOwner, Observer { status ->
            status?.let {
                if (!it) {
                    progressBar.visibility = View.GONE
                    errorText.visibility = View.VISIBLE
                    horoscopeRecyclerView.visibility = View.GONE
                }
            }
        })
    }
}