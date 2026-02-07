package com.kaushalpanjee.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.R
import com.kaushalpanjee.common.model.request.UnnatiRequest
import com.kaushalpanjee.compose.data.mapper.AboutUnnatiUiMapper
import com.kaushalpanjee.compose.presentation.viewmodel.AboutUnnatiViewModel
import com.kaushalpanjee.compose.ui.screen.AboutUnnatiScreen
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
class AboutUnnatiFragment : Fragment() {

    private val commonViewModel: AboutUnnatiViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val ddugky = requireArguments().getString("ddugky")!!
        val rseti = requireArguments().getString("rseti")!!
        val pmkvy = requireArguments().getString("pmkvy")!!

        commonViewModel.setScheme(ddugky, rseti, pmkvy)
        return ComposeView(requireContext()).apply {
            setContent {
                AboutUnnatiScreen(commonViewModel,
                    {findNavController().navigateUp()},
                )
            }
        }
    }


//    var ddugky : String? = ""
//    var rseti : String? = ""
//    var nrlm : String? = ""
//    var pmvishwakarma : String? = ""
//    var pmkvy : String? = ""
//
//    private val schemeAdapter: SchemeAdapter by lazy {
//        SchemeAdapter(getUnnatiSchemes())
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        init()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        schemeAdapter.releaseAllPlayers()
//    }
//
//    private fun init() {
//        // Retrieve arguments
//         ddugky = arguments?.getString("ddugky")
//         rseti = arguments?.getString("rseti")
//         nrlm = arguments?.getString("nrlm")
//        pmvishwakarma = arguments?.getString("pmvishwakarma")
//        pmkvy = arguments?.getString("pmkvy")
//        binding.rvScheme.adapter = schemeAdapter
//        listeners()
//    }
//
//    private fun listeners() {
//        binding.progressBackButton.setOnClickListener {
//            findNavController().navigateUp()
//        }
//    }
//
//    private fun getUnnatiSchemes() : List<Scheme> {
//        return  listOf<Scheme>(
//            Scheme(
//                getString(R.string.ddugky_title),
//                ddugky!!,
//                arrayListOf(
//                    getString(R.string.ddugky_1),
//                    getString(R.string.ddugky_2),
//                    getString(R.string.ddugky_3),
//                )
//            ),
//            Scheme(
//                getString(R.string.rseti_title),
//                rseti!!,
//                arrayListOf(
//                    getString(R.string.rseti_1),
//                    getString(R.string.rseti_2),
//                    getString(R.string.rseti_3)
//                )
//            ),
////            ,
////            Scheme(
////                "NRLM",
////                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
////                arrayListOf(
////                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon.",
////                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon.",
////                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon."
////                )
////            ),
////            Scheme(
////                "PM Vishwakarma",
////                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
////                arrayListOf(
////                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon.",
////                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon.",
////                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. It may wrap into multiple lines but stays below the top-left icon."
////                )
////            ),
//            Scheme(
//                getString(R.string.pmkvy_),
//                pmkvy!!,
//                arrayListOf(
//                    getString(R.string.pmkvy_1),
//                    getString(R.string.pmkvy_2),
//                    getString(R.string.pmkvy_3),
//                    getString(R.string.pmkvy_4),
//                    getString(R.string.pmkvy_5),
//                    getString(R.string.pmkvy_6),
//                    getString(R.string.pmkvy_7),
//                    getString(R.string.pmkvy_8),
//                    getString(R.string.pmkvy_9),
//                    getString(R.string.pmkvy_10)
//                )
//            )
//
//        )
//    }
}