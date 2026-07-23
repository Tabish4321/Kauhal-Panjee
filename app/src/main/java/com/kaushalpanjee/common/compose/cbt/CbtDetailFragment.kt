package com.kaushalpanjee.common.compose.cbt

// Fragment
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController

//import com.example.myapplication.CBT.CBTExamScreen
import com.google.android.material.dialog.MaterialAlertDialogBuilder

import com.kaushalpanjee.BuildConfig
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
            CbtQuestionsReq(BuildConfig.VERSION_NAME, "2603404318", AppUtil.getSavedLanguagePreference(requireContext()))
//            CbtQuestionsReq(BuildConfig.VERSION_NAME, userPreferences.getUseID(), AppUtil.getSavedLanguagePreference(requireContext()))
        )

        collectTrainingCenterResponse()


    }

    private fun collectTrainingCenterResponse() {
        lifecycleScope.launchWhenStarted {
            commonViewModel.getCbtQuetios.collectLatest { state ->

                when (state) {
                    is Resource.Loading -> {
                        Log.d("CBT_QS", "Loading...")
                    }

                    is Resource.Success -> {
                        val response = state.data
                        val questionSet = response?.questionset
                        val questionList = questionSet?.question ?: emptyList()

                        if (questionList.isNotEmpty()) {

                            // 👇 setContent ko forEachIndexed loop ke BAAHAR rakho
                            // (loop ke andar hone se ye baar baar Compose set kar raha tha)
                            binding.composeView.setContent {
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
                                        // 👇 yaha navigation call karo
                                        findNavController().navigate(
                                            CbtDetailFragmentDirections.actionCbtDetailFragmentToMainHomePage()
                                        )
                                    }
                                )
                            }

                        } else {
                            Log.e("CBT_QS", "Question list empty after parsing")
                            Toast.makeText(requireContext(), "Question list empty", Toast.LENGTH_SHORT).show()
                        }
                    }

                    is Resource.Error -> {
                        // handle error
                    }
                }
            }
        }
    }
    private fun showExitExamDialog() {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Exit Exam")
            .setMessage("Do you want to exit exam?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()

                backPressedCallback.isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            .setNegativeButton("No") { dialog, _ ->
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