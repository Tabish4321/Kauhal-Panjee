package com.kaushalpanjee.cbt

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.clip


// 🔹 Android
import android.util.Log
import android.widget.Toast
import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kaushalpanjee.R

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontStyle
import androidx.lifecycle.compose.LocalLifecycleOwner

import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.myapplication.CBT.CircularTimer
import com.example.myapplication.CBT.api.CBTViewModel
import com.example.myapplication.CBT.api.Question
import com.kaushalpanjee.cbt.WorkManager.startSubmitWorker
import com.kaushalpanjee.cbt.api.answers.CbtAnsersSubmit
import com.kaushalpanjee.cbt.interctions.CBTInstructionStartScreen
import com.kaushalpanjee.cbt.submit.SubmitExamItem
import com.kaushalpanjee.common.CommonViewModel
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.Resource
import kotlinx.coroutines.flow.collectLatest
import kotlin.collections.get


//code commit and use 13 july 2026 time 10:04 PM
@SuppressLint("ViewModelConstructorInComposable")
@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun CBTExamScreen(
    questionList: List<Question>,
    candidateId: String,
    candidatName: String,
    examId: String,
    questionSetId: String,
    batchId: String,
    viewModel: CBTExamViewModel = viewModel(),
    commonViewModel: CommonViewModel = viewModel(),
    onOrientationChange: (() -> Unit)? = null
)
{
    var isDeclarationChecked by rememberSaveable { mutableStateOf(false) }
    if (questionList.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading Questions...")
        }
        return
    }

    // Collect ViewModel StateFlows
    val examStarted by viewModel.examStarted.collectAsState()
//    val commonViewModel: CommonViewModel = viewModel()

    val currentIndex by viewModel.currentIndex.collectAsState()
    val timeLeft by viewModel.timeLeft.collectAsState()
    val examFinished by viewModel.examFinished.collectAsState()
    val editMode by viewModel.editMode.collectAsState()
    var showReviewDialog by remember { mutableStateOf(false) }
    var showQuestionPalette by remember { mutableStateOf(false) }
    var showDialogTime by remember { mutableStateOf(false) }
//    val showReviewDialog by viewModel.showReviewDialog.collectAsState()
    val showSuccessDialog by viewModel.showSuccessDialog.collectAsState()
    val submissionLoading by viewModel.submissionLoading.collectAsState()
    val submissionError by viewModel.submissionError.collectAsState()
    val answers by viewModel.answers.collectAsState()
    val markedQuestions by viewModel.markedQuestions.collectAsState()
//    val markedQuestions = remember {
//        mutableStateListOf<Int>()
//    }

//    val markedQuestions = remember {
//        mutableStateListOf<Int>()
//    }

    val reviewQuestions = remember {
        mutableStateListOf<Int>()
    }
    var currentQuestionIndex by remember {
        mutableIntStateOf(0)
    }

    val questionStatus by viewModel.questionStatus.collectAsState()
    val DarkGreen = Color(0xFF173430)
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val context = LocalContext.current
    val configuration = LocalConfiguration.current



    val lifecycleOwner = LocalLifecycleOwner.current

//    var showQuestionPalette by remember { mutableStateOf(false) }
//    var showDialogTime by remember { mutableStateOf(false) }
    var showReviewScreen by remember { mutableStateOf(false) }

    // Answer States
    val answeredQuestions = remember {
        mutableStateMapOf<Int, String>()
    }



//    val reviewQuestions = remember { mutableStateSetOf<Int>() }

    // Summary Counts
    val answeredCount = answeredQuestions.size
    val markedCount = markedQuestions.size
    val notAnsweredCount = questionList.size - answeredCount


    LaunchedEffect(Unit) {
        commonViewModel.submitAnswers.collectLatest { state ->
            when (state) {

                is Resource.Loading -> {
                    Log.d("CBT_SUBMIT", "Loading...")
                }

                is Resource.Success -> {
                    val response = state.data
                    Log.d("CBT_SUBMIT", "Success Response = $response")

                    Toast.makeText(
                        context,
                        response?.message ?: "Submitted successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Agar submit ke baad back jana hai ya next screen open karni hai
                    // onOrientationChange?.invoke()
                }

                is Resource.Error -> {
                    Log.e("CBT_SUBMIT", "Error = ${state.error?.message}")

                    Toast.makeText(
                        context,
                        state.error?.message ?: "Something went wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    // 🔥 ORIENTATION DETECTION - Go back to home if device rotates to landscape
    val currentOrientation = configuration.orientation
    LaunchedEffect(currentOrientation) {
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.w("CBTExamScreen", "Device rotated to landscape - navigating back to home")
            onOrientationChange?.invoke()
        }
    }

    // Start timer once when exam begins
    LaunchedEffect(examStarted) {
        if (examStarted && !examFinished) {
            viewModel.startTimer()
        }
    }


    if (!examStarted) {
        CBTInstructionStartScreen(
            title = "CBT Exam",
            remainingSeconds = timeLeft,
            isDeclarationChecked = isDeclarationChecked,
            onDeclarationChecked = { isDeclarationChecked = it },
            onBackClick = {
                onOrientationChange?.invoke()
            },
            onStartClick = {
                viewModel.startExam()
            }
        )
        return

    }

    val currentQuestion = questionList[currentIndex]


    Scaffold(
        containerColor = Color(0x33F2F2F2),

        topBar = {
//            Surface(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .statusBarsPadding()
//                    .height((screenHeight * 0.08f).dp)   // 8% of screen height
//                    .background(
//                        Brush.horizontalGradient(
//                            listOf(
//                                MaterialTheme.colorScheme.primary,
//                                MaterialTheme.colorScheme.primaryContainer,
//                                MaterialTheme.colorScheme.secondary
//                            )
//                        )
//                    ),
//                color = Color.Transparent
//            )
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height((screenHeight * 0.08f).dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                DarkGreen,
                                DarkGreen,
                                DarkGreen
                            )
                        )
                    ),
                color = Color.Transparent
            )
//            Surface(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .statusBarsPadding()
//                    .height((screenHeight * 0.08f).dp),
//                color = Color.Transparent
//            )

            {

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {



                    Column(
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Candidate ID : ${candidateId?.takeIf { it.isNotBlank() } ?: "NA"}",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

//                        Text(
//                            text = "Candidate Name : ${candidatName?.takeIf { it.isNotBlank() } ?: "NA"}",
//                            fontWeight = FontWeight.Bold,
//                            color = Color.Black
//                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        CircularTimer(
                            timeLeft = timeLeft,
                            totalTime = 30 * 60
//                            totalTime = 1 * 60
                        )

                        Spacer(Modifier.width(10.dp))
                        IconButton(onClick = { viewModel.toggleReviewDialog() }) {
                            Icon(
                                imageVector = Icons.Default.RateReview,
                                contentDescription = "Review",
                                tint = Color.White
                            )
                        }
//                        IconButton(onClick = { viewModel.toggleReviewDialog() }) {
//                            Icon(Icons.Default.List, null, tint = Color.Black)
//                        }
                    }
                }
            }
        }

    )
    { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .navigationBarsPadding()
        ) {

            // 🔥 SCROLLABLE QUESTION AREA
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {

                // Question Counter & Progress
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.question),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        " ${currentIndex + 1}",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    Text(" / ${questionList.size}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                LinearProgressIndicator(
                    progress = (currentIndex + 1) / questionList.size.toFloat(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .padding(vertical = 6.dp)
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    "${currentIndex + 1}. ${currentQuestion.question_value}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(Modifier.height(10.dp))

                currentQuestion.option?.forEach { option ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.selectAnswer(
                                    currentQuestion.question_id.toString(),
                                    option.option_key.toString()
                                )
                            }
                            .padding(vertical = 6.dp)
                            .background(
                                if (answers[currentQuestion.question_id] == option.option_key)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    Color.Transparent,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        RadioButton(
                            selected = answers[currentQuestion.question_id] == option.option_key,
                            onClick = {
                                viewModel.selectAnswer(currentQuestion.question_id.toString(), option.option_key.toString())
                            }
                        )

                        Text(option.option_value.toString(), modifier = Modifier.padding(start = 10.dp), fontSize = 13.sp)
                    }
                }

                Spacer(Modifier.height(12.dp))
            }

                if (showReviewDialog)
//                {
//
//                    Dialog(
//                        onDismissRequest = { },
//                        properties = DialogProperties(
//                            dismissOnBackPress = false,
//                            dismissOnClickOutside = false,
//                            usePlatformDefaultWidth = false
//                        )
//                    ) {
//
//                        Box(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .background(Color.White)
//                                .zIndex(10f)
//                        ) {
//
//                            Column(
//                                modifier = Modifier
//                                    .fillMaxSize()
//                                    .padding(16.dp)
//                            ) {
//
//                                // ---------------- TOP BAR ----------------
//                                Row(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.SpaceBetween,
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//
//                                    Text(
//                                        text = stringResource(id = R.string.question_index),
//                                        fontWeight = FontWeight.Bold,
//                                        fontSize = 24.sp,
//                                        color = Color.Black
//                                    )
//
//                                    IconButton(
//                                        onClick = {
//                                            viewModel.closeReviewDialog()
//                                        }
//                                    ) {
//                                        Icon(
//                                            imageVector = Icons.Default.Close,
//                                            contentDescription = "Close",
//                                            tint = Color.Black
//                                        )
//                                    }
//                                }
//
//                                Spacer(modifier = Modifier.height(20.dp))
//
//                                // ---------------- LEGEND ----------------
//                                Row(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.SpaceEvenly
//                                ) {
//
//                                    LegendItem(
//                                        color = Color(0xFF9E9E9E), // Not Answered
//                                        text = stringResource(id = R.string.not_answered)
//                                    )
//
//                                    LegendItem(
//                                        color = Color(0xFF4CAF50), // Answered
//                                        text = stringResource(id = R.string.answered)
//                                    )
//
//                                    LegendItem(
//                                        color = Color(0xFFFFC107), // Review
//                                        text = "Review"
//                                    )
//
//                                    LegendItem(
//                                        color = Color(0xFF03A9F4), // Marked
//                                        text = "Marked"
//                                    )
//                                }
//
//                                Spacer(modifier = Modifier.height(20.dp))
//
//                                // ---------------- QUESTION GRID ----------------
//                                LazyVerticalGrid(
//                                    columns = GridCells.Fixed(5),
//                                    modifier = Modifier.weight(1f),
//                                    verticalArrangement = Arrangement.spacedBy(8.dp),
//                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                                ) {
//
//                                    items(questionList.size) { index ->
//
//                                        val question = questionList[index]
//                                        val id = question.question_id
//
//                                        val bgColor = when {
//                                            // Answered + Review
//                                            answers.containsKey(id) && markedQuestions.contains(id) ->
//                                                Color(0xFFFFC107)
//
//                                            // Only Marked / Review later
//                                            !answers.containsKey(id) && markedQuestions.contains(id) ->
//                                                Color(0xFF03A9F4)
//
//                                            // Answered
//                                            answers.containsKey(id) ->
//                                                Color(0xFF4CAF50)
//
//                                            // Not Answered
//                                            else ->
//                                                Color(0xFF9E9E9E)
//                                        }
//
//                                        Box(
//                                            modifier = Modifier
//                                                .padding(4.dp)
//                                                .size(58.dp)
//                                                .clip(RoundedCornerShape(10.dp))
//                                                .background(bgColor)
//                                                .clickable {
//                                                    viewModel.goToQuestion(index)
//                                                    viewModel.closeReviewDialog()
//                                                },
//                                            contentAlignment = Alignment.Center
//                                        ) {
//                                            Text(
//                                                text = "${index + 1}",
//                                                color = Color.White,
//                                                fontWeight = FontWeight.Bold,
//                                                fontSize = 16.sp
//                                            )
//                                        }
//                                    }
//                                }
//
//                                Spacer(modifier = Modifier.height(16.dp))
//
//                                // ---------------- BOTTOM BUTTONS ----------------
//                                Row(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.SpaceBetween
//                                ) {
//
//                                    // Previous Button
//                                    Button(
//                                        onClick = {
//                                            viewModel.previousQuestion()
//                                            viewModel.closeReviewDialog()
//                                        },
//                                        modifier = Modifier
//                                            .weight(1f)
//                                            .height(48.dp),
//                                        shape = RoundedCornerShape(10.dp),
//                                        colors = ButtonDefaults.buttonColors(
//                                            containerColor = Color(0xFFE53935) // RED
//                                        )
//                                    ) {
//                                        Text(
//                                            text = stringResource(id = R.string.previous),
//                                            color = Color.White,
//                                            fontWeight = FontWeight.Bold
//                                        )
//                                    }
//
//                                    Spacer(modifier = Modifier.width(12.dp))
//
//                                    // Next & Review Button
//                                    Button(
//                                        onClick =
//
////                                            {
////                                            val currentQuestion = questionList.getOrNull(currentQuestionIndex)
////                                            currentQuestion?.question_id?.let { id ->
////                                                viewModel.markQuestion(id.toString())
////                                                viewModel.saveAndNext(
////                                                    id.toString(),
////                                                    "Next & Review",
////                                                    questionList.size
////                                                )
////                                            }
////                                            viewModel.closeReviewDialog()
////                                        }
//                                            {},
//                                        modifier = Modifier
//                                            .weight(1f)
//                                            .height(48.dp),
//                                        shape = RoundedCornerShape(10.dp),
//                                        colors = ButtonDefaults.buttonColors(
//                                            containerColor = Color(0xFF43A047) // GREEN
//                                        )
//                                    ) {
//                                        Text(
//                                            text = "Next & Review",
//                                            color = Color.White,
//                                            fontWeight = FontWeight.Bold
//                                        )
//                                    }
//                                }
//
//                                Spacer(modifier = Modifier.height(12.dp))
//
//                                // ---------------- CLOSE BUTTON ----------------
//                                Box(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .padding(4.dp)
//                                        .padding(10.dp),
//                                    contentAlignment = Alignment.Center
//                                ) {
//                                    Text(
//                                        text = "Close",
//                                        color = Color.White,
//                                        fontWeight = FontWeight.Bold,
//                                        fontSize = 13.sp
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
            {

                Dialog(
                    onDismissRequest = { },
                    properties = DialogProperties(
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false,
                        usePlatformDefaultWidth = false
                    )
                ) {

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.White
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp)
                        ) {

                            // Top Bar (Back + Title)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                IconButton(
                                    onClick = { viewModel.closeReviewDialog() }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }

                                Text(
                                    text = stringResource(id = R.string.question_index),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Color Legend Layout
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {

                                // Red Box
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .background(Color(0xFFB8B8B8), RoundedCornerShape(3.dp))
//                                        .background(Color(0xFFF44336), RoundedCornerShape(3.dp))
                                )

                                Text(
                                    text = stringResource(id = R.string.not_answered),
                                    fontSize = 11.sp
                                )

                                // Green Box
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .background(Color(0xFF4CAF50), RoundedCornerShape(3.dp))
                                )

                                Text(
                                    text = stringResource(id = R.string.answered),
                                    fontSize = 11.sp
                                )

                                // Yellow Box
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .background(Color(0xFFFFC107), RoundedCornerShape(3.dp))
                                )

                                Text(
                                    text = stringResource(R.string.review),
                                    fontSize = 11.sp
                                )
                             // Marked Box
//                                Box(
//                                    modifier = Modifier
//                                        .size(14.dp)
//                                        .background(Color(0xFF4FC3F7), RoundedCornerShape(3.dp))
//                                )
//
//                                Text(
//                                    text = stringResource(R.string.mark),
//                                    fontSize = 11.sp
//                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

//                            LazyVerticalGrid(
//                                columns = GridCells.Fixed(5),
//                                modifier = Modifier
//                                    .weight(1f)
//                                    .fillMaxWidth(),
//                                verticalArrangement = Arrangement.spacedBy(6.dp),
//                                horizontalArrangement = Arrangement.spacedBy(6.dp)
//                            ) {
//
//                                items(questionList.size) { index ->
//
//                                    val question = questionList[index]
//                                    val id = question.question_id
//
//                                    val bgColor = when {
//
//                                        answers.containsKey(id) && markedQuestions.contains(index) ->
//                                            Color(0xFFFFC107)   // Answered & Review
//
//                                        !answers.containsKey(id) && markedQuestions.contains(index) ->
//                                            Color(0xFF4FC3F7)   // Review Later
//
//                                        answers.containsKey(id) ->
//                                            Color(0xFF4CAF50)   // Answered
//
//                                        else ->
//                                            Color(0xFFB8B8B8)   // Not Answered
////                                            Color(0xFFF44336)   // Not Answered
//                                    }
//
//                                    Box(
//                                        modifier = Modifier
//                                            .size(45.dp)
//                                            .clip(RoundedCornerShape(6.dp))
//                                            .background(bgColor)
//                                            .clickable {
//                                                viewModel.goToQuestion(index)
//                                                viewModel.closeReviewDialog()
//                                            },
//                                        contentAlignment = Alignment.Center
//                                    ) {
//
//                                        Text(
//                                            text = "${index + 1}",
//                                            color = Color.White,
//                                            fontWeight = FontWeight.Bold,
//                                            fontSize = 11.sp
//                                        )
//                                    }
//                                }
//                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // ---------------------------
                            // Bottom Buttons
                            // ---------------------------
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                // Previous Button - RED
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFB8B8B8))
//                                        .background(Color(0xFFE53935))
                                        .clickable {
                                            viewModel.previousQuestion()
                                            viewModel.closeReviewDialog()
                                        }
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.previous),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }

                                // Next & Review Button - GREEN
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF43A047))
                                        .clickable {


//                                                showQuestionPalette = false
                                            showReviewDialog = false

                                            showQuestionPalette = true
                                                if (currentQuestionIndex < questionList.lastIndex) {

                                                    currentQuestionIndex++
                                                    showReviewDialog = false
                                                }

//                                            val currentQuestion = questionList.getOrNull(currentQuestionIndex)
//                                            currentQuestion?.question_id?.let { id ->
//                                                viewModel.markQuestion(id.toString())
//                                                viewModel.saveAndNext(
//                                                    id.toString(),
//                                                    "Next & Review",
//                                                    questionList.size
//                                                )
//                                            }
//                                            viewModel.closeReviewDialog()
                                        }
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Next & Review",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Close Button
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                                    .padding(10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Close",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }

//            if (showQuestionPalette) {
//                showDialogTime = false
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(Color.White)
//                        .zIndex(20f)
//                ) {
//
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(16.dp)
//                    ) {
//
//                        Text(
//                            text = "Review Your Test",
//                            fontSize = 22.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.SpaceEvenly
//                        ) {
//                            SummaryCard("Answered", answeredCount.toString(), Color(0xFF4CAF50))
//                            SummaryCard("Not Answered", notAnsweredCount.toString(), Color(0xFFF44336))
//                            SummaryCard("Marked", markedCount.toString(), Color(0xFFFFC107))
//                            SummaryCard("Total", questionList.size.toString(), Color(0xFF2196F3))
//                        }
//
//                        Spacer(modifier = Modifier.height(20.dp))
//
//                        LazyColumn(
//                            modifier = Modifier.weight(1f)
//                        ) {
//                            items(questionList.size) { index ->
//
//                                val status = when {
//                                    reviewQuestions.contains(index) -> "Marked for Review"
//                                    markedQuestions.contains(index) -> "Marked"
//                                    answeredQuestions.containsKey(index) -> "Answered"
//                                    else -> "Not Answered"
//                                }
//
//                                Row(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .padding(8.dp),
//                                    horizontalArrangement = Arrangement.SpaceBetween
//                                ) {
//                                    Text(text = "Q. ${index + 1}")
//                                    Text(text = status)
//                                }
//
//                                Divider()
//                            }
//                        }
//
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.spacedBy(12.dp)
//                        ) {
//
//                            OutlinedButton(
//                                modifier = Modifier.weight(1f),
//                                onClick = {
//                                    // Previous = dismiss
//                                    showReviewScreen = false
//                                }
//                            ) {
//                                Text("Back to Test")
//                            }
//
//                            Button(
//                                modifier = Modifier.weight(1f),
//                                onClick = {
//
//                                    val submitList = questionList.map { question ->
//                                        val answerGiven = answers[question.question_id] ?: ""
//                                        val category = when {
//                                            markedQuestions.contains(question.question_id)-> "Mark & Review"
//                                            answerGiven.isNotEmpty() -> "Save & Next"
//                                            else -> "Save & Next"
//                                        }
//                                        SubmitExamItem(
//                                            question_id = question.question_id ?: "",
//                                            answer_given = if (answerGiven.isEmpty()) "NA" else answerGiven,
//                                            category = category,
//                                            marks_per_qs = question.marks_per_qs ?: 0.0
//                                        )
//                                    }
//
//                                    commonViewModel.submitExam(
//                                        AppUtil.getSavedTokenPreference(context),
//                                        CbtAnsersSubmit(
//                                            candidateId,
//                                            batchId,
//                                            examId,
//                                            questionSetId,
//                                            submitList
//                                        )
//                                    )
//                                }
//                            ) {
//                                Text("Submit Test")
//                            }
//                        }
//                    }
//                }
//            }






// commonViewModel.submitExam(
//                            AppUtil.getSavedTokenPreference(context),
////                            CbtAnsersSubmit(BuildConfig.VERSION_NAME,"2603404318","en")
////                            CbtAnsersSubmit(candidateId,batchId,examId,questionSetId,questionList.map { question ->
//                            CbtAnsersSubmit(
//                                "2603404318",
//                                batchId,
//                                examId,
//                                questionSetId,
//                                questionList.map { question ->
//                                    val answerGiven = answers[question.question_id] ?: ""
//                                    val category = when {
//                                        markedQuestions.contains(question.question_id) -> "Mark & Review"
//                                        answerGiven.isNotEmpty() -> "Save & Next"
////                                    answerGiven.isNotEmpty() -> "NA"
//                                        else -> "Save & Next"
////                                    else -> "Not Answered"
//                                    }
//                                    SubmitExamItem(
//                                        question_id = question.question_id ?: "",
//                                        answer_given = if (answerGiven.isEmpty()) "NA" else answerGiven,
//                                        category = category,
//                                        marks_per_qs = question.marks_per_qs ?: 0.0
//                                    )
//                                }
//                            ))



            // 🔥 ACTION BUTTONS (Save & Next, Save Review, Mark, Clear)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            )
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    val buttonList = listOf(
                        R.string.save_next to Color(0xFF4CAF50),
                        R.string.save_review to Color(0xFFFFC107),
                        R.string.mark to Color(0xFF03A9F4),
                        R.string.clear to Color.Gray
                    )

                    buttonList.forEach { (textRes, color) ->
                        val text = stringResource(id = textRes)

                        Text(
                            text = text,
                            modifier = Modifier
                                .weight(1f)
                                .background(color, RoundedCornerShape(5.dp))
                                .clickable {
                                    val id = currentQuestion.question_id

                                    when (textRes) {
                                        R.string.clear -> {
                                            viewModel.clearAnswer(id.toString())
                                        }

                                        R.string.save_review -> {
                                            viewModel.markQuestion(id.toString())
                                            viewModel.saveAndNext(
                                                id.toString(),
                                                text,
                                                questionList.size
                                            )
                                        }

                                        R.string.mark -> {
                                            viewModel.markQuestion(id.toString())
                                        }

                                        else -> {
                                            viewModel.saveAndNext(
                                                id.toString(),
                                                text,
                                                questionList.size
                                            )
                                        }
                                    }
                                }
                                .padding(vertical = 7.dp),
                            textAlign = TextAlign.Center,
                            color = if (textRes == R.string.save_review) Color.Black else Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.height(4.dp))

                // 🔥 NAVIGATION BUTTONS (Previous & Next)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Previous Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Transparent)
                            .border(
                                width = 1.5.dp,
                                brush = Brush.horizontalGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.primaryContainer
                                    )
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                viewModel.previousQuestion()
                            }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.previous),
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }

                    // Next Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Transparent)
                            .border(
                                width = 1.5.dp,
                                brush = Brush.horizontalGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.primaryContainer
                                    )
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                viewModel.nextQuestion(questionList.size)
                            }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.next),
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // 🔥 SUBMIT BUTTON

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (submissionLoading) Color(0xFF173430).copy(alpha = 0.6f)
                        else Color(0xFF173430)
                    )
                    .clickable(enabled = !submissionLoading) {

                        viewModel.toggleReviewDialog()
                        commonViewModel.submitExam(
                            AppUtil.getSavedTokenPreference(context),
//                            CbtAnsersSubmit(BuildConfig.VERSION_NAME,"2603404318","en")
//                            CbtAnsersSubmit(candidateId,batchId,examId,questionSetId,questionList.map { question ->
                            CbtAnsersSubmit(
                                "2603404318",
                                batchId,
                                examId,
                                questionSetId,
                                questionList.map { question ->
                                    val answerGiven = answers[question.question_id] ?: ""
                                    val category = when {
                                        markedQuestions.contains(question.question_id) -> "Mark & Review"
                                        answerGiven.isNotEmpty() -> "Save & Next"
//                                    answerGiven.isNotEmpty() -> "NA"
                                        else -> "Save & Next"
//                                    else -> "Not Answered"
                                    }
                                    SubmitExamItem(
                                        question_id = question.question_id ?: "",
                                        answer_given = if (answerGiven.isEmpty()) "NA" else answerGiven,
                                        category = category,
                                        marks_per_qs = question.marks_per_qs ?: 0.0
                                    )
                                }
                            ))
                        showQuestionPalette = true
                    }
//                    }



//                    }

//                    .clickable(enabled = !submissionLoading) {
//                        viewModel.submitExam(
//                            questionList,
//                            candidateId,
//                            batchId,
//                            examId,
//                            questionSetId,
//                            context
//                        )
//                    }
                    .padding(vertical = 11.dp),
                contentAlignment = Alignment.Center
            )
            {
                if (submissionLoading) {
                    Text(
                        text = "Submitting...",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                } else {
                    Text(
                        text = if (editMode) "Update & Submit" else "Submit Exam",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }





            LaunchedEffect(Unit) {
                WorkManager.getInstance(context)
                    .getWorkInfosByTagLiveData("submit_exam")
                    .observe(lifecycleOwner) { list ->

                        list.forEach { workInfo ->

                            Log.d("WORK_STATUS", workInfo.state.name)

                            when (workInfo.state) {

                                WorkInfo.State.RUNNING -> {
                                    Log.d("WORK_STATUS", "API RUNNING")
                                }

                                WorkInfo.State.SUCCEEDED -> {
                                    Log.d("WORK_STATUS", "API SUCCESS DONE")
                                }

                                WorkInfo.State.FAILED -> {
                                    Log.d("WORK_STATUS", "API FAILED")
                                }

                                else -> {}
                            }
                        }
                    }
            }
            // 🔥 SUCCESS DIALOG - Show when exam submitted successfully
            if (showSuccessDialog) {
                val repo = CBTViewModel()
                repo.deleteOfflineJson(context)




                // 🔥 ADD THIS LINE (IMPORTANT)
                LaunchedEffect(showSuccessDialog) {
                    if (showSuccessDialog) {
                        startSubmitWorker(context, candidateId)
                    }
                }

                AlertDialog(
                    onDismissRequest = { viewModel.closeSuccessDialog() },
                    title = {
                        Text(
                            text = "✅ Success!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                    },
                    text = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Your exam has been submitted successfully!",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Divider()

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = "Candidate ID:",
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = candidateId,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            Text(
                                text = "We're syncing your results in the background. You can close this app.",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.closeSuccessDialog()
                                repo.deleteOfflineJson(context)
                                      },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(45.dp)
                        ) {
                            Text(
                                text = "OK",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            // 🔥 FAILURE DIALOG - Show when exam submission fails
            if (submissionError != null) {
                AlertDialog(
                    onDismissRequest = { viewModel.closeSuccessDialog() },
                    title = {
                        Text(
                            text = "❌ Submission Failed",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE53935)
                        )
                    },
                    text = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Error:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE53935)
                            )

                            Text(
                                text = submissionError ?: "Unknown error occurred",
                                fontSize = 13.sp,
                                color = Color.Black,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFFFEBEE), RoundedCornerShape(6.dp))
                                    .padding(10.dp)
                            )

                            Divider()

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = "Candidate ID:",
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = candidateId,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            Text(
                                text = "Please check your internet connection and try again.",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {  viewModel.clearSubmissionError()
                                // Clear error and allow user to retry or go back
                                // User can try submitting again
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE53935)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(45.dp)
                        )




                        {
                            Text(

                                text = "Close",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            // Show error toast if submission fails
            submissionError?.let { error ->
                LaunchedEffect(error) {
                    Toast.makeText(
                        context,
                        error,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}

