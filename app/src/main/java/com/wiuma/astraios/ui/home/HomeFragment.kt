package com.wiuma.astraios.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.wiuma.astraios.R
import com.wiuma.astraios.utilities.createPlaceholder
import com.wiuma.astraios.utilities.fetchImage

class HomeFragment : Fragment() {

    private var usersHoroscope: Int = 0
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var usernameTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var dailyHoroscope: TextView
    private lateinit var errorText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var horoscopeSignImageView: ImageView
    private lateinit var horoscopeSignShadowImageView: ImageView
    private lateinit var horoscopeSignInkImageView: ImageView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        homeViewModel.fetchData()
        usernameTextView = root.findViewById(R.id.username)
        dateTextView = root.findViewById(R.id.todaysDate)
        nameTextView = root.findViewById(R.id.name)
        dailyHoroscope = root.findViewById(R.id.dailyHoroscope)
        errorText = root.findViewById(R.id.errorText)
        progressBar = root.findViewById(R.id.progressBar)
        nestedScrollView = root.findViewById(R.id.nestedScrollView)
        horoscopeSignImageView = root.findViewById(R.id.horoscopeSign)
        horoscopeSignShadowImageView = root.findViewById(R.id.horoscopeSignShadow)
        horoscopeSignInkImageView = root.findViewById(R.id.horoscopeSignInk)
        observeData()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val seeHoroscopeDetails: Button = view.findViewById(R.id.seeHoroscopeDetails)
        seeHoroscopeDetails.setOnClickListener {
            val action = HomeFragmentDirections.actionNavigationHomeToNavigationHoroscopeDetails(
                usersHoroscope
            )
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun observeData() {
        homeViewModel.user.observe(viewLifecycleOwner, Observer {
            usernameTextView.text = it.name
            usersHoroscope = it.horoscope!!.toInt()
        })

        homeViewModel.horoscope.observe(viewLifecycleOwner, Observer { horoscope ->
            horoscope?.let {
                nameTextView.text = horoscope.nameHoroscope
                dailyHoroscope.text = horoscope.dailyComment
                context?.let {
                    horoscopeSignImageView.fetchImage(
                        horoscope.signHoroscope,
                        createPlaceholder(it)
                    )
                    horoscopeSignShadowImageView.fetchImage(
                        horoscope.shadowSignHoroscope,
                        createPlaceholder(it)
                    )
                    horoscopeSignInkImageView.fetchImage(
                        horoscope.InkSign,
                        createPlaceholder(it)
                    )
                }
            }
        })

        homeViewModel.date.observe(viewLifecycleOwner, Observer {
            dateTextView.text = it
        })

        homeViewModel.loadingStatus.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) {
                    progressBar.visibility = View.VISIBLE
                    errorText.visibility = View.GONE
                    nestedScrollView.visibility = View.GONE
                } else {
                    progressBar.visibility = View.GONE
                    errorText.visibility = View.GONE
                    nestedScrollView.visibility = View.VISIBLE
                }
            }
        })

        homeViewModel.successBoolean.observe(viewLifecycleOwner, Observer { status ->
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