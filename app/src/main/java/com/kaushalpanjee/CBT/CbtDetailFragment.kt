package com.kaushalpanjee.CBT

// Fragment
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.fragment.app.activityViewModels
import com.example.myapplication.CBT.CBTExamScreen

//import com.example.myapplication.CBT.CBTExamScreen
import com.example.myapplication.CBT.api.CBTViewModel
import com.example.myapplication.CBT.api.Question
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.common.CommonViewModel
import com.kaushalpanjee.common.model.request.CandidateReq
import com.kaushalpanjee.common.model.request.CbtQuestionsReq
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.UserPreferences
import com.kaushalpanjee.databinding.FragmentCBTDetailBinding
import kotlin.getValue

class CbtDetailFragment : BaseFragment<FragmentCBTDetailBinding>(
    bindingInflater = FragmentCBTDetailBinding::inflate
) {

    private val viewModel: CBTViewModel by viewModels()
    private val commonViewModel: CommonViewModel by activityViewModels()

//    lateinit var userPreferences: UserPreferences

    private var questionListData: List<Question> = emptyList()

//    override fun initializeViews() {
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    userPreferences = UserPreferences(requireContext())


    commonViewModel.getCandidateDetailsAPI(
        CandidateReq(
            BuildConfig.VERSION_NAME,
            userPreferences.getUseID()
        ), AppUtil.getSavedTokenPreference(requireContext())
    )



    init()

}
    private fun init(){

        // ✅ 1. FIRST LOAD OFFLINE DATA
        viewModel.loadOfflineFirst(requireContext())

        // ✅ 2. THEN HIT API (refresh data)
        viewModel.fetchExam(requireContext(), userPreferences.getUseID())

        binding.composeView.setContent {

            val questionList by viewModel.questionList.observeAsState(emptyList())
            val examId by viewModel.examId.observeAsState("")
            val questionSetId by viewModel.questionSetId.observeAsState("")
            val batchId by viewModel.batchId.observeAsState("")





            LaunchedEffect(questionList) {
                questionListData = questionList

                activity?.requestedOrientation =
                    android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }

            CBTExamScreen(
                questionList = questionList,
                userPreferences.getUseID(),
                userPreferences.getUserName(),
                examId = examId,
                questionSetId = questionSetId,
                batchId = batchId,
                onOrientationChange = {
                    activity?.supportFragmentManager?.popBackStack()
                }
            )





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