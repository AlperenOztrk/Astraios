package com.wiuma.astraios.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.wiuma.astraios.R
import com.wiuma.astraios.ui.horoscope.HoroscopeFragment

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var usernameTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var dailyHoroscope: TextView
    private lateinit var errorText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var nestedScrollView: NestedScrollView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        homeViewModel.refreshData()
        usernameTextView = root.findViewById(R.id.username)
        dateTextView = root.findViewById(R.id.todaysDate)
        nameTextView = root.findViewById(R.id.name)
        dailyHoroscope = root.findViewById(R.id.dailyHoroscope)
        errorText = root.findViewById(R.id.errorText)
        progressBar = root.findViewById(R.id.progressBar)
        nestedScrollView = root.findViewById(R.id.nestedScrollView)
        observeData()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val seeHoroscopeDetails: Button = view.findViewById(R.id.seeHoroscopeDetails)
        seeHoroscopeDetails.setOnClickListener {
            //TODO
            val action = HomeFragmentDirections.actionNavigationHomeToNavigationHoroscopeDetails(0)
            Navigation.findNavController(it).navigate(action)
        }
    }

    fun observeData() {
        homeViewModel.username.observe(viewLifecycleOwner, Observer { username ->
            username?.let {
                usernameTextView.text = it
            }
        })

        homeViewModel.date.observe(viewLifecycleOwner, Observer {
            dateTextView.text = it
        })

        homeViewModel.name.observe(viewLifecycleOwner, Observer {
            nameTextView.text = it
        })

        homeViewModel.horoscopeList.observe(viewLifecycleOwner, Observer { horoscope ->
            horoscope?.let {
                errorText.visibility = View.GONE
                progressBar.visibility = View.GONE
                dailyHoroscope.text = horoscope.get(0).dailyComment
            }
        })

        homeViewModel.loadingStatus.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) {
                    progressBar.visibility = View.VISIBLE
                    errorText.visibility = View.GONE
                    nestedScrollView.visibility = View.GONE
                }
            }
        })

        homeViewModel.returnBoolean.observe(viewLifecycleOwner, Observer { status ->
            status?.let {
                if (!it) {
                    progressBar.visibility = View.GONE
                    errorText.visibility = View.VISIBLE
                    nestedScrollView.visibility = View.GONE
                }
            }
        })
    }

}