package com.kaushalpanjee.CBT


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.setContent

// Fragment
import androidx.fragment.app.viewModels

// ViewBinding

// Compose Core
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.LocalContext

// Compose UI
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp

import androidx.compose.ui.graphics.Color
  // ❌ REMOVE

// Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.CBT.CBTExamScreen
import com.example.myapplication.CBT.api.CBTViewModel
import com.example.myapplication.CBT.api.Question
import com.google.gson.Gson
import com.kaushalpanjee.base.BaseFragment
import com.kaushalpanjee.core.util.UserPreferences
import com.kaushalpanjee.databinding.FragmentCBTDetailBinding
import javax.inject.Inject


class CbtDetailFragment : BaseFragment<FragmentCBTDetailBinding>(
    bindingInflater = FragmentCBTDetailBinding::inflate
) {

    private val viewModel: CBTViewModel by viewModels()

    lateinit var userPreferences: UserPreferences

    private var questionListData: List<Question> = emptyList()

    override fun initializeViews() {

        userPreferences = UserPreferences(requireContext())

        viewModel.fetchExam(requireContext(), userPreferences.getUseID())

//        these use font and screen size
        binding.composeView.setContent {

            val questionList by viewModel.questionList.observeAsState(emptyList())
            val examId by viewModel.examId.observeAsState("")
            val questionSetId by viewModel.questionSetId.observeAsState("")
            val batchId by viewModel.batchId.observeAsState("")

            LaunchedEffect(questionList) {
                questionListData = questionList
            }

            // ✅ ONLY USE CBTTheme (REMOVE MaterialTheme)
//            CBTTheme {

                CBTExamScreen(
                    questionList = questionList,
                    userPreferences.getUseID(),
                     userPreferences.getUserName(),
                    examId = examId,
                    questionSetId = questionSetId,
                    batchId = batchId
                )
            }
        }





//          these use without font and screen size
//        binding.composeView.setContent {
//
//            val questionList by viewModel.questionList.observeAsState(emptyList())
//            val examId by viewModel.examId.observeAsState("")
//            val questionSetId by viewModel.questionSetId.observeAsState("")
//            val batchId by viewModel.batchId.observeAsState("")
//
//            LaunchedEffect(questionList) {
//
//                questionListData = questionList
//
////                val gson = Gson()
////                val jsonString = gson.toJson(questionList)
////
////                Log.d("FINAL_JSON", jsonString)
////
////                Log.d("EXAM_ID", examId)
////                Log.d("QUESTION_SET_ID", questionSetId)
////                Log.d("BATCH_ID", batchId)
//            }
//
//            MaterialTheme {
//                CBTTheme {
//
//                    CBTExamScreen(
//                        questionList,
//                        userPreferences.getUseID(),
//                        userPreferences.getUserName(),
//                        examId,
//                        questionSetId,
//                        batchId
//                    )
//                }
//            }
//        }
    }

    override fun setupObservers() {}
    override fun setupClickListeners() {}
    override fun loadInitialData() {}
}


//class CbtDetailFragment : BaseFragment<FragmentCBTDetailBinding>(
//    bindingInflater = FragmentCBTDetailBinding::inflate
//) {
//
//    private val viewModel: CBTViewModel by viewModels()
//
//    lateinit var userPreferences: UserPreferences
//
//    override fun initializeViews() {
//
//        binding.composeView.apply {
//
//            setViewCompositionStrategy(
//                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
//            )
//            userPreferences = UserPreferences(requireContext())
//            viewModel.fetchExam(requireContext(),userPreferences.getUseID())
////            viewModel.fetchExam(requireContext(),"2509163165")
////            2509347732
//
//            setContent {
//                MaterialTheme {
//
//                    val questionList by viewModel
//                        .questionList
//                        .observeAsState(emptyList())
//
//                    val message by viewModel
//                        .message
//                        .observeAsState("")
//
//                    LaunchedEffect(message) {
//                        if (message.isNotEmpty()) {
//                            Toast.makeText(
//                                requireContext(),
//                                message,
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//
//                    com.example.myapplication.CBT.CBTExamScreen(questionList,userPreferences.getUseID())
//                }
//            }
//        }
//    }
//
//    override fun setupObservers() {}
//    override fun setupClickListeners() {}
//    override fun loadInitialData() {}
//}
