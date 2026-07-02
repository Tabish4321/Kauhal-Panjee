package com.kaushalpanjee.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kaushalpanjee.R
import com.kaushalpanjee.common.model.request.UnnatiRequest
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.databinding.FragmentAboutUnnatiBinding
import com.kaushalpanjee.model.Scheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue


@AndroidEntryPoint
class AboutUnnatiFragment :
    BaseFragment<FragmentAboutUnnatiBinding>(FragmentAboutUnnatiBinding::inflate) {

    private val commonViewModel: CommonViewModel by activityViewModels()
    private lateinit var schemeAdapter: SchemeAdapter

    var ddugky : String? = ""
    var rseti : String? = ""
    var nrlm : String? = ""
    var pmvishwakarma : String? = ""
    var pmkvy : String? = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        commonViewModel.getUnnati(UnnatiRequest(AppUtil.getSavedLanguagePreference(requireContext())))
        collectUnnatiData()
        listeners()
    }

    override fun onStop() {
        super.onStop()
        schemeAdapter.releaseAllPlayers()
    }



    private fun listeners() {
        binding.progressBackButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun getUnnatiSchemes() : List<Scheme> {
        return  listOf<Scheme>(
            Scheme(
                getString(R.string.ddugky_title),
                ddugky!!,
                arrayListOf(
                    getString(R.string.ddugky_1),
                    getString(R.string.ddugky_2),
                    getString(R.string.ddugky_3),
                )
            ),
            Scheme(
                getString(R.string.rseti_title),
                rseti!!,
                arrayListOf(
                    getString(R.string.rseti_1),
                    getString(R.string.rseti_2),
                    getString(R.string.rseti_3)
                )
            ),
//            ,
//            Scheme(
//                "NRLM",
//                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
//                arrayListOf(
//                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon.",
//                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon.",
//                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon."
//                )
//            ),
//            Scheme(
//                "PM Vishwakarma",
//                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
//                arrayListOf(
//                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon.",
//                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon.",
//                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon."
//                )
//            ),
            Scheme(
                getString(R.string.pmkvy_),
                pmkvy!!,
                arrayListOf(
                    getString(R.string.pmkvy_1),
                    getString(R.string.pmkvy_2),
                    getString(R.string.pmkvy_3),
                    getString(R.string.pmkvy_4),
                    getString(R.string.pmkvy_5),
                    getString(R.string.pmkvy_6),
                    getString(R.string.pmkvy_7),
                    getString(R.string.pmkvy_8),
                    getString(R.string.pmkvy_9),
                    getString(R.string.pmkvy_10)
                )
            )

        )
    }
    private fun collectUnnatiData() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getUnnati) {
                when (it) {
                    is Resource.Loading -> showProgressBar()
                    is Resource.Error -> {
                        hideProgressBar()
                    }
                    is Resource.Success -> {
                        hideProgressBar()

                        it.data.let { response ->

                            ddugky = response?.data?.DDUGKY
                            rseti = response?.data?.RSETI
                            nrlm = response?.data?.NRLM
                            pmvishwakarma = response?.data?.PM_VISHWAKARMA
                            pmkvy = response?.data?.PMKVY
                            schemeAdapter = SchemeAdapter(getUnnatiSchemes())
                            binding.rvScheme.adapter = schemeAdapter

                        }

                    }
                }
            }
        }
    }

}