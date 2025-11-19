package com.kaushalpanjee.common

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.databinding.FragmentAboutUnnatiBinding
import com.kaushalpanjee.model.Scheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutUnnatiFragment :
    BaseFragment<FragmentAboutUnnatiBinding>(FragmentAboutUnnatiBinding::inflate) {

    private val schemeAdapter: SchemeAdapter by lazy {
        SchemeAdapter(getUnnatiSchemes())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onStop() {
        super.onStop()
        schemeAdapter.releaseAllPlayers()
    }

    private fun init() {
        binding.rvScheme.adapter = schemeAdapter

        listeners()
    }

    private fun listeners() {
        binding.progressBackButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun getUnnatiSchemes() : List<Scheme> {
        return  listOf<Scheme>(
            Scheme(
                "DDUGKY",
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                arrayListOf(
                    "Skill development opportunities to rural youth in the age group of 15–35 years.",
                    "Industry-aligned training aimed at improving employability and access to quality job opportunities.",
                    "Structured placement support upon successful completion of training.",
                )
            ),
            Scheme(
                "RSETI",
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                arrayListOf(
                    "Short-duration training programmes for rural youth aged 18–50 years, focusing on self-employment and micro-enterprise development.",
                    "Facilitates linkage with banks and credit institutions to enable establishment of individual enterprises.",
                    "Post-training mentoring and handholding support to ensure sustainability and growth of new businesses."
                )
            ),
            Scheme(
                "NRLM",
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                arrayListOf(
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon.",
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon.",
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon."
                )
            ),
            Scheme(
                "PM Vishwakarma",
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                arrayListOf(
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon.",
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon.",
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon."
                )
            ),
            Scheme(
                "PMKVY",
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                arrayListOf(
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon.",
                )
            )

        )
    }
}