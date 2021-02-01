package com.wiuma.astraios.ui.horoscopeDetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.wiuma.astraios.R
import com.wiuma.astraios.utilities.createPlaceholder
import com.wiuma.astraios.utilities.fetchImage

class HoroscopeDetailsFragment : Fragment() {

    private lateinit var horoscopeDetailsViewModel: HoroscopeDetailsViewModel
    private var receivedHoroscopeId = 0;
    private lateinit var nameHoroscopeTextView: TextView
    private lateinit var signHoroscopeImageView: ImageView
    private lateinit var shadowSignHoroscopeImageView: ImageView
    private lateinit var dailyCommentTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var planetTextView: TextView
    private lateinit var planetImageView: ImageView
    private lateinit var elementTextView: TextView
    private lateinit var elementImageView: ImageView
    private lateinit var highlightsTextView: TextView
    private lateinit var characterTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_horoscope_details, container, false)
        nameHoroscopeTextView = root.findViewById(R.id.nameHoroscope)
        signHoroscopeImageView = root.findViewById(R.id.horiscopeSign)
        shadowSignHoroscopeImageView = root.findViewById(R.id.horoscopeSignShadow)
        dailyCommentTextView = root.findViewById(R.id.dailyComment)
        dateTextView = root.findViewById(R.id.horoscopeDates)
        planetTextView = root.findViewById(R.id.namePlanet)
        planetImageView = root.findViewById(R.id.signPlanet)
        elementTextView = root.findViewById(R.id.nameElement)
        elementImageView = root.findViewById(R.id.signElement)
        highlightsTextView = root.findViewById(R.id.highlights)
        characterTextView = root.findViewById(R.id.characterDescription)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        horoscopeDetailsViewModel =
            ViewModelProvider(this).get(HoroscopeDetailsViewModel::class.java)
        arguments?.let {
            receivedHoroscopeId = HoroscopeDetailsFragmentArgs.fromBundle(it).horoscopeId
        }
        observeData()
        horoscopeDetailsViewModel.getData(receivedHoroscopeId)
    }

    @SuppressLint("SetTextI18n")
    fun observeData() {
        horoscopeDetailsViewModel.horoscopeDetails.observe(
            viewLifecycleOwner,
            Observer { horoscope ->
                horoscope?.let {
                    nameHoroscopeTextView.text = it.nameHoroscope
                    dailyCommentTextView.text = it.dailyComment
                    dateTextView.text = it.startDate + " - " + it.endDate
                    planetTextView.text = it.namePlanet
                    elementTextView.text = it.nameElement
                    highlightsTextView.text = it.highlightOfHoroscope
                    characterTextView.text = it.characterOfHoroscope
                    context?.let {
                        signHoroscopeImageView.fetchImage(
                            horoscope.signHoroscope,
                            createPlaceholder(it)
                        )
                        shadowSignHoroscopeImageView.fetchImage(
                            horoscope.shadowSignHoroscope,
                            createPlaceholder(it)
                        )
                        planetImageView.fetchImage(horoscope.signPlanet, createPlaceholder(it))
                        elementImageView.fetchImage(horoscope.signElement, createPlaceholder(it))
                    }
                }
            })
    }

}