package com.kaushalpanjee.CBT

// Fragment
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController

//import com.example.myapplication.CBT.CBTExamScreen
import com.example.myapplication.CBT.api.CBTViewModel
import com.example.myapplication.CBT.api.Question
import com.example.myapplication.CBT.api.QuestionSet

import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.CBT.api.CbtQuestionResult
import com.kaushalpanjee.common.CBTExamScreen
import com.kaushalpanjee.common.CommonViewModel
import com.kaushalpanjee.common.model.request.CandidateReq
import com.kaushalpanjee.common.model.request.CbtQuestionsReq
import com.kaushalpanjee.common.model.request.TrainingCenterReq
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.core.util.UserPreferences
import com.kaushalpanjee.core.util.gone
import com.kaushalpanjee.core.util.visible
import com.kaushalpanjee.databinding.FragmentCBTDetailBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

class CbtDetailFragment : BaseFragment<FragmentCBTDetailBinding>(
    bindingInflater = FragmentCBTDetailBinding::inflate
) {


    private val commonViewModel: CommonViewModel by activityViewModels()


//    lateinit var userPreferences: UserPreferences

    private var questionListData: List<Question> = emptyList()
    private var questionList: List<Question> = emptyList()
    private var examId: String = ""
    private var questionSetId: String = ""
    private var batchId: String = ""
    private var candidateId: String = ""
    private var examDateTime: String = ""

//    override fun initializeViews() {
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    userPreferences = UserPreferences(requireContext())

//    BuildConfig.VERSION_NAME,



    commonViewModel.getCBTGETQUESTION(
        AppUtil.getSavedTokenPreference(requireContext()),
        CbtQuestionsReq(BuildConfig.VERSION_NAME,"2603404318","en")
    )


    collectTrainingCenterResponse()

//    init()

}

    private fun collectTrainingCenterResponse() {
        lifecycleScope.launchWhenStarted {
            commonViewModel.getCBTQuestions.collectLatest { state ->

                when (state) {
                    is Resource.Loading -> {
                        Log.d("CBT_QS", "Loading...")
                    }

                    is Resource.Success -> {
                        val response = state.data
                        Log.d("CBT_QS", "Full Response = $response")

                        val questionSet = response?.questionset
                        val questionList = questionSet?.question ?: emptyList()

                        Log.d("CBT_QS", "QuestionSet = $questionSet")
                        Log.d("CBT_QS", "QuestionList = $questionList")




                        if (questionList.isNotEmpty()) {

                            Log.d("CBT_QS", "Exam Id = ${questionSet?.exam_id}")
                            Log.d("CBT_QS", "Candidate Id = ${questionSet?.candidate_id}")
                            Log.d("CBT_QS", "Question Set Id = ${questionSet?.question_set_id}")
                            Log.d("CBT_QS", "Batch Id = ${questionSet?.batch_id}")
                            Log.d("CBT_QS", "Exam Date Time = ${questionSet?.exam_date_time}")

                            questionList.forEachIndexed { index, question ->
                                Log.d("CBT_QS", "Question ${index + 1} Id = ${question.question_id}")
                                Log.d("CBT_QS", "Question ${index + 1} Value = ${question.question_value}")
                                Log.d("CBT_QS", "Question ${index + 1} Marks = ${question.marks_per_qs}")

                                question.option?.forEach { option ->
                                    Log.d(
                                        "CBT_QS",
                                        "Option ${option.option_key} = ${option.option_value}"
                                    )
                                }



                                binding.composeView.setContent {
                                    CBTExamScreen(
                                        questionList = questionList,
                                        userPreferences.getUseID(),
                                        userPreferences.getUserName(),
                                        examId = questionSet?.exam_id.toString(),
                                        questionSetId = questionSet?.question_set_id.toString(),
                                        batchId = questionSet?.batch_id.toString(),
                                        commonViewModel = commonViewModel,
                                        onOrientationChange = {
                                            activity?.supportFragmentManager?.popBackStack()
                                        }
                                    )
                                }

                            }

                        } else {
                            Log.e("CBT_QS", "Question list empty after parsing")
                            Toast.makeText(requireContext(), "Question list empty", Toast.LENGTH_SHORT).show()
                        }
                    }

                    is Resource.Error -> {
//                        Log.e("CBT_QS", "Error = ${state.message}")
//                        Toast.makeText(
//                            requireContext(),
//                            state.message ?: "Something went wrong",
//                            Toast.LENGTH_SHORT
//                        ).show()
                    }
                }
            }
        }
    }




//    }


}



















//class CbtDetailFragment : BaseFragment<FragmentCBTDetailBinding>(
//    bindingInflater = FragmentCBTDetailBinding::inflate
//) {
//
//    private val viewModel: CBTViewModel by viewModels()
//
////    lateinit var userPreferences: UserPreferences
//
//    private var questionListData: List<Question> = emptyList()
//
//    override fun initializeViews() {
//
//        userPreferences = UserPreferences(requireContext())
//
//        // ✅ 1. FIRST LOAD OFFLINE DATA
//        viewModel.loadOfflineFirst(requireContext())
//
//        // ✅ 2. THEN HIT API (refresh data)
//        viewModel.fetchExam(requireContext(), userPreferences.getUseID())
//
//        binding.composeView.setContent {
//
//            val questionList by viewModel.questionList.observeAsState(emptyList())
//            val examId by viewModel.examId.observeAsState("")
//            val questionSetId by viewModel.questionSetId.observeAsState("")
//            val batchId by viewModel.batchId.observeAsState("")
//
//            LaunchedEffect(questionList) {
//                questionListData = questionList
//
//                activity?.requestedOrientation =
//                    android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//            }
//
//            CBTExamScreen(
//                questionList = questionList,
//                userPreferences.getUseID(),
//                userPreferences.getUserName(),
//                examId = examId,
//                questionSetId = questionSetId,
//                batchId = batchId,
//                onOrientationChange = {
//                    activity?.supportFragmentManager?.popBackStack()
//                }
//            )
//        }
//    }
//
//
//}





//class CbtDetailFragment : BaseFragment<FragmentCBTDetailBinding>(
//    bindingInflater = FragmentCBTDetailBinding::inflate
//) {
//
//    private val viewModel: CBTViewModel by viewModels()
//
//    lateinit var userPreferences: UserPreferences
//
//    private var questionListData: List<Question> = emptyList()
//
//    override fun initializeViews() {
//
//        userPreferences = UserPreferences(requireContext())
//
//        viewModel.fetchExam(requireContext(), userPreferences.getUseID())
//        binding.composeView.setContent {
//
//            val questionList by viewModel.questionList.observeAsState(emptyList())
//            val examId by viewModel.examId.observeAsState("")
//            val questionSetId by viewModel.questionSetId.observeAsState("")
//            val batchId by viewModel.batchId.observeAsState("")
//
//            LaunchedEffect(questionList) {
//                questionListData = questionList
//                // Lock activity to portrait orientation during exam
//                activity?.requestedOrientation =
//                    android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//            }
//
//            // ✅ ONLY USE CBTTheme (REMOVE MaterialTheme)
////            CBTTheme {
//
//            CBTExamScreen(
//                questionList = questionList,
//                userPreferences.getUseID(),
//                userPreferences.getUserName(),
//                examId = examId,
//                questionSetId = questionSetId,
//                batchId = batchId,
//                onOrientationChange = {
//                    // Navigate back to home page when orientation changes
//                    activity?.supportFragmentManager?.popBackStack()
//                }
//            )
//        }
//    }
//
//    override fun setupObservers() {}
//    override fun setupClickListeners() {}
//    override fun loadInitialData() {}
//}