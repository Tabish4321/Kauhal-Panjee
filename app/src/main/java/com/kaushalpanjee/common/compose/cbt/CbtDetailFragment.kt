package com.kaushalpanjee.common.compose.cbt

// Fragment
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController

//import com.example.myapplication.CBT.CBTExamScreen
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator

import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.R
import com.kaushalpanjee.common.CommonViewModel
import com.kaushalpanjee.common.model.request.CbtQuestionsReq
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.core.util.UserPreferences
import com.kaushalpanjee.databinding.FragmentCBTDetailBinding
import kotlinx.coroutines.flow.collectLatest
import kotlin.getValue






class CbtDetailFragment : BaseFragment<FragmentCBTDetailBinding>(
    bindingInflater = FragmentCBTDetailBinding::inflate
) {

    private val commonViewModel: CommonViewModel by activityViewModels()

    // ✅ Compose-observable loading state (class property, no remember)
    private var isLoading by mutableStateOf(false)

//    private lateinit var userPreferences: UserPreferences

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            showExitExamDialog()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            backPressedCallback
        )
        userPreferences = UserPreferences(requireContext())

        commonViewModel.getCbtExam(
            AppUtil.getSavedTokenPreference(requireContext()),
            CbtQuestionsReq(
                BuildConfig.VERSION_NAME,
                userPreferences.getUseID(),
                AppUtil.getSavedLanguagePreference(requireContext())
            )
//            "2603404318"
//            CbtQuestionsReq(BuildConfig.VERSION_NAME, userPreferences.getUseID(), AppUtil.getSavedLanguagePreference(requireContext()))
        )
        isLoading=true
        collectTrainingCenterResponse()
    }

    private fun collectTrainingCenterResponse() {
        lifecycleScope.launchWhenStarted {
            commonViewModel.getCbtQuetios.collectLatest { state ->

                when (state) {
                    is Resource.Loading -> {
                        Log.d("CBT_QS", "Loading...")
                        isLoading = true          // ✅ Loading start
                    }

                    is Resource.Success -> {
                        isLoading = false          // ✅ Dismiss on success
                        val response = state.data
                        val questionSet = response?.questionset
                        val questionList = questionSet?.question ?: emptyList()

                        if (questionList.isNotEmpty()) {

                            binding.composeView.setContent {
                                Box(modifier = Modifier.fillMaxSize()) {

                                    CBTExamScreen(
                                        questionList = questionList,
                                        candidateId = userPreferences.getUseID(),
                                        candidatName = userPreferences.getUserName(),
                                        examId = questionSet?.exam_id.toString(),
                                        questionSetId = questionSet?.question_set_id.toString(),
                                        batchId = questionSet?.batch_id.toString(),
                                        commonViewModel = commonViewModel,
                                        onOrientationChange = {
                                            activity?.supportFragmentManager?.popBackStack()
                                        },
                                        onSubmitSuccess = {
                                            findNavController().navigate(
                                                CbtDetailFragmentDirections.actionCbtDetailFragmentToMainHomePage()
                                            )
                                        }
                                    )

                                    // ✅ Loading overlay - success ya fail dono par dismiss ho jayega
                                    if (isLoading) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color.Black.copy(alpha = 0.3f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    }
                                }
                            }

                        } else {
                            Log.e("CBT_QS", "Question list empty after parsing")

                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle(getString(R.string.no_questions_available))
                                .setMessage(getString(R.string.no_questions_are_available_for_this_user))
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                                    dialog.dismiss()

                                    backPressedCallback.isEnabled = false
                                    requireActivity().onBackPressedDispatcher.onBackPressed()
                                }
                                .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .show()
//                            Toast.makeText(requireContext(), "Question list empty", Toast.LENGTH_SHORT).show()
                        }
                    }

                    is Resource.Error -> {
                        isLoading = false          // ✅ Dismiss on error
                        // handle error
                    }
                }
            }
        }
    }

    private fun showExitExamDialog() {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.exit_exam))
            .setMessage(getString(R.string.do_you_want_to_exit_exam))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                dialog.dismiss()

                backPressedCallback.isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onResume() {
        super.onResume()

        requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onDestroyView() {
        super.onDestroyView()

        requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}


















//class CbtDetailFragment : BaseFragment<FragmentCBTDetailBinding>(
//    bindingInflater = FragmentCBTDetailBinding::inflate
//) {
//
//    private val commonViewModel: CommonViewModel by activityViewModels()
//
//
////    var isLoading by remember { mutableStateOf(false) }
//      private var isLoading = false
//    private val backPressedCallback = object : OnBackPressedCallback(true) {
//        override fun handleOnBackPressed() {
//            showExitExamDialog()
//        }
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        requireActivity().onBackPressedDispatcher.addCallback(
//            this,
//            backPressedCallback
//        )
//        userPreferences = UserPreferences(requireContext())
//
//        commonViewModel.getCbtExam(
//            AppUtil.getSavedTokenPreference(requireContext()),
//            CbtQuestionsReq(BuildConfig.VERSION_NAME, "2603404318", AppUtil.getSavedLanguagePreference(requireContext()))
////            CbtQuestionsReq(BuildConfig.VERSION_NAME, userPreferences.getUseID(), AppUtil.getSavedLanguagePreference(requireContext()))
//        )
//
//        collectTrainingCenterResponse()
//
//
//    }
//
//    private fun collectTrainingCenterResponse() {
//        lifecycleScope.launchWhenStarted {
//            commonViewModel.getCbtQuetios.collectLatest { state ->
//
//                when (state) {
//                    is Resource.Loading -> {
//                        Log.d("CBT_QS", "Loading...")
//                    }
//
//                    is Resource.Success -> {
//                        val response = state.data
//                        val questionSet = response?.questionset
//                        val questionList = questionSet?.question ?: emptyList()
//
//                        if (questionList.isNotEmpty()) {
//
//                            binding.composeView.setContent {
//                                CBTExamScreen(
//                                    questionList = questionList,
//                                    candidateId = userPreferences.getUseID(),
//                                    candidatName = userPreferences.getUserName(),
//                                    examId = questionSet?.exam_id.toString(),
//                                    questionSetId = questionSet?.question_set_id.toString(),
//                                    batchId = questionSet?.batch_id.toString(),
//                                    commonViewModel = commonViewModel,
//                                    onOrientationChange = {
//                                        activity?.supportFragmentManager?.popBackStack()
//                                    },
//                                    onSubmitSuccess = {
//                                        findNavController().navigate(
//                                            CbtDetailFragmentDirections.actionCbtDetailFragmentToMainHomePage()
//                                        )
//                                    }
//                                )
//                            }
//
//                        } else {
//                            Log.e("CBT_QS", "Question list empty after parsing")
//                            Toast.makeText(requireContext(), "Question list empty", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//
//                    is Resource.Error -> {
//                        // handle error
//                    }
//                }
//            }
//        }
//    }
//
//    private fun showExitExamDialog() {
//
//        MaterialAlertDialogBuilder(requireContext())
//            .setTitle("Exit Exam")
//            .setMessage("Do you want to exit exam?")
//            .setCancelable(false)
//            .setPositiveButton("Yes") { dialog, _ ->
//                dialog.dismiss()
//
//                backPressedCallback.isEnabled = false
//                requireActivity().onBackPressedDispatcher.onBackPressed()
//            }
//            .setNegativeButton("No") { dialog, _ ->
//                dialog.dismiss()
//            }
//            .show()
//    }
//    override fun onResume() {
//        super.onResume()
//
//        requireActivity().requestedOrientation =
//            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//    }
//    override fun onDestroyView() {
//        super.onDestroyView()
//
//        requireActivity().requestedOrientation =
//            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
//    }
//}