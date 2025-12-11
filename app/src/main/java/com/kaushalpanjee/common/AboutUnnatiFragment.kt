package com.kaushalpanjee.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.R
import com.kaushalpanjee.common.model.request.UnnatiRequest
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.AESCryptography
import com.kaushalpanjee.core.util.AppConstant
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.core.util.toastShort
import com.kaushalpanjee.databinding.FragmentAboutUnnatiBinding
import com.kaushalpanjee.model.Scheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AboutUnnatiFragment :
    BaseFragment<FragmentAboutUnnatiBinding>(FragmentAboutUnnatiBinding::inflate) {

    private val commonViewModel: CommonViewModel by activityViewModels()

    var ddugky : String? = ""
    var rseti : String? = ""
    var nrlm : String? = ""
    var pmvishwakarma : String? = ""
    var pmkvy : String? = ""

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
        // Retrieve arguments
         ddugky = arguments?.getString("ddugky")
         rseti = arguments?.getString("rseti")
         nrlm = arguments?.getString("nrlm")
        pmvishwakarma = arguments?.getString("pmvishwakarma")
        pmkvy = arguments?.getString("pmkvy")
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
            )
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
//            Scheme(
//                "PMKVY",
//                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
//                arrayListOf(
//                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon.",
//                )
//            )

        )
    }
}