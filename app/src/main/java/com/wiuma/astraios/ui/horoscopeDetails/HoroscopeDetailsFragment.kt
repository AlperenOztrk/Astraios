package com.wiuma.astraios.ui.horoscopeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.wiuma.astraios.R

class HoroscopeDetailsFragment : Fragment() {

    private lateinit var horoscopeDetailsViewModel: HoroscopeDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        horoscopeDetailsViewModel =
            ViewModelProvider(this).get(HoroscopeDetailsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_horoscope_details, container, false)


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val userHoroscopeId = HoroscopeDetailsFragmentArgs.fromBundle(it).horoscopeId
        }
    }
}