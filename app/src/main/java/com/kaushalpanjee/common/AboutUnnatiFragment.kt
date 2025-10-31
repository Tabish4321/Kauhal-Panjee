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
            ),
            Scheme(
                "DDUGKY",
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                arrayListOf(
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon.",
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon.",
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon.",
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon.",
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon."
                )
            ),
            Scheme(
                "RSETI",
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                arrayListOf(
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon.",
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon."
                )
            )
        )
    }
}