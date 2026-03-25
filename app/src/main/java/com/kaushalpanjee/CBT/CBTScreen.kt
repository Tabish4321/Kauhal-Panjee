package com.example.myapplication.CBT
import android.content.Context
import androidx.compose.foundation.Image
//import com.example.myapplication.R
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.CBT.api.Option
import com.example.myapplication.CBT.api.Question



// 🔹 Android
import android.util.Log
import android.widget.Toast
import android.content.pm.ActivityInfo
import android.content.res.Configuration

// 🔹 Compose Core
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.CBT.api.ApiResponse
import com.example.myapplication.CBT.api.QuestionSet
import com.google.gson.Gson

// 🔹 Coroutines
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// 🔹 Retrofit
import retrofit2.Retrofit
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// 🔹 Gson
import com.google.gson.JsonObject
import com.kaushalpanjee.CBT.ActionButton
import com.kaushalpanjee.CBT.OutlineBtn
import com.kaushalpanjee.CBT.AppDimens

import com.kaushalpanjee.CBT.submit.RetrofitClient
import com.kaushalpanjee.CBT.submit.SubmitExamItem
import com.kaushalpanjee.CBT.submit.SubmitExamRequest
import com.kaushalpanjee.core.util.AppUtil
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.compareTo
import kotlin.dec
import kotlin.inc
import kotlin.text.compareTo
import com.kaushalpanjee.R

// ViewModel and SavedState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.collectAsState
import com.kaushalpanjee.CBT.CBTExamViewModel

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
    onOrientationChange: (() -> Unit)? = null
) {

    if (questionList.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading Questions...")
        }
        return
    }

    // Collect ViewModel StateFlows
    val examStarted by viewModel.examStarted.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val timeLeft by viewModel.timeLeft.collectAsState()
    val examFinished by viewModel.examFinished.collectAsState()
    val editMode by viewModel.editMode.collectAsState()
    val showReviewDialog by viewModel.showReviewDialog.collectAsState()
    val showSuccessDialog by viewModel.showSuccessDialog.collectAsState()
    val submissionLoading by viewModel.submissionLoading.collectAsState()
    val submissionError by viewModel.submissionError.collectAsState()

    val answers by viewModel.answers.collectAsState()
    val markedQuestions by viewModel.markedQuestions.collectAsState()
    val questionStatus by viewModel.questionStatus.collectAsState()

    val screenHeight = LocalConfiguration.current.screenHeightDp
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

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

    // ---------------- START SCREEN ----------------
    if (!examStarted) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF36D1A6),
                            Color(0xFF4F9488),
                            Color(0xFF2A4D44)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    "Welcome To CBT Exam",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(20.dp))

                Button(onClick = { viewModel.startExam() }) {
                    Text("Start Exam")
                }
            }
        }
        return
    }

    val currentQuestion = questionList[currentIndex]


    Scaffold(
        containerColor = Color(0x33F2F2F2),

        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height((screenHeight * 0.08f).dp)   // 8% of screen height
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.secondary
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
                            text = stringResource(
                                id = R.string.candidate_id,
                                candidateId
                            ),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(
                                id = R.string.candidate_name_cbt,
                                candidatName
                            ),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        CircularTimer(
                            timeLeft = timeLeft,
                            totalTime = 30 * 60
                        )

                        Spacer(Modifier.width(10.dp))

                        IconButton(onClick = { viewModel.toggleReviewDialog() }) {
                            Icon(Icons.Default.List, null, tint = Color.Black)
                        }
                    }
                }
            }
        }

    ) { padding ->

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

                currentQuestion.option.forEach { option ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.selectAnswer(currentQuestion.question_id, option.option_key)
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
                                viewModel.selectAnswer(currentQuestion.question_id, option.option_key)
                            }
                        )

                        Text(option.option_value, modifier = Modifier.padding(start = 10.dp), fontSize = 13.sp)
                    }
                }

                Spacer(Modifier.height(12.dp))
            }
            if (showReviewDialog) {

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
                                        .background(Color(0xFFF44336), RoundedCornerShape(3.dp))
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
                                    text = "Review",
                                    fontSize = 11.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(5),
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {

                                items(questionList.size) { index ->

                                    val question = questionList[index]
                                    val id = question.question_id

                                    val bgColor = when {

                                        answers.containsKey(id) && markedQuestions.contains(id) ->
                                            Color(0xFFFFC107)   // Answered & Review

                                        !answers.containsKey(id) && markedQuestions.contains(id) ->
                                            Color(0xFF4FC3F7)   // Review Later

                                        answers.containsKey(id) ->
                                            Color(0xFF4CAF50)   // Answered

                                        else ->
                                            Color(0xFFF44336)   // Not Answered
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(45.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(bgColor)
                                            .clickable {
                                                viewModel.goToQuestion(index)
                                                viewModel.closeReviewDialog()
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {

                                        Text(
                                            text = "${index + 1}",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Close Button
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(6.dp))
                                    .clickable { viewModel.closeReviewDialog() }
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
            
            // 🔥 ACTION BUTTONS (Save & Next, Save Review, Mark, Clear)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
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
                                            viewModel.clearAnswer(id)
                                        }
                                        R.string.save_review -> {
                                            viewModel.markQuestion(id)
                                            viewModel.saveAndNext(id, text, questionList.size)
                                        }
                                        R.string.mark -> {
                                            viewModel.markQuestion(id)
                                        }
                                        else -> {
                                            viewModel.saveAndNext(id, text, questionList.size)
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
                        if (submissionLoading) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                        else MaterialTheme.colorScheme.primary
                    )
                    .clickable(enabled = !submissionLoading) {
                        viewModel.submitExam(
                            questionList,
                            candidateId,
                            batchId,
                            examId,
                            questionSetId,
                            context
                        )
                    }
                    .padding(vertical = 11.dp),
                contentAlignment = Alignment.Center
            ) {
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

            // 🔥 SUCCESS DIALOG - Show when exam submitted successfully
            if (showSuccessDialog) {
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
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = { viewModel.closeSuccessDialog() },
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
                    onDismissRequest = { },
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
                            onClick = { 
                                // Clear error and allow user to retry or go back
                                // User can try submitting again
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE53935)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(45.dp)
                        ) {
                            Text(
                                text = "Retry",
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










































//code use today 17/03/2026
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CBTExamScreen(
//    questionList: List<Question>,
//    candidateId: String,
//    candidatName: String,
//    examId: String,
//    questionSetId: String,
//    batchId: String
//) {
//
//    val dimens = MaterialTheme.dimens   // ✅ IMPORTANT
//
//    if (questionList.isEmpty()) {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            Text("Loading Questions...", fontSize = dimens.textMedium)
//        }
//        return
//    }
//
//    var examStarted by remember { mutableStateOf(false) }
//    var currentIndex by remember { mutableStateOf(0) }
//    var timeLeft by remember { mutableStateOf(1800) }
//    var examFinished by remember { mutableStateOf(false) }
//    var editMode by remember { mutableStateOf(false) }
//    val questionStatus = remember { mutableStateMapOf<String, String>() }
//    var showReviewDialog by remember { mutableStateOf(false) }
//    var showSuccessDialog by remember { mutableStateOf(false) }
//
//    val answers = remember { mutableStateMapOf<String, String>() }
//    val markedQuestions = remember { mutableStateMapOf<String, Boolean>() }
//
//    // ---------------- START SCREEN ----------------
//    if (!examStarted) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(
//                    Brush.verticalGradient(
//                        listOf(Color(0xFF36D1A6), Color(0xFF4F9488), Color(0xFF2A4D44))
//                    )
//                ),
//            contentAlignment = Alignment.Center
//        ) {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//
//                Text(
//                    text = "Welcome To CBT Exam",
//                    color = Color.White,
//                    fontSize = dimens.textLarge,
//                    fontWeight = FontWeight.Bold
//                )
//
//                Spacer(modifier = Modifier.height(dimens.paddingLarge))
//
//                Button(onClick = {
//                    examStarted = true
//                    currentIndex = 0
//                    timeLeft = 1800
//                }) {
//                    Text("Start Exam")
//                }
//            }
//        }
//        return
//    }
//
//    val currentQuestion = questionList[currentIndex]
//
//    // ---------------- TIMER ----------------
//    LaunchedEffect(examFinished) {
//        while (!examFinished && timeLeft > 0) {
//            delay(1000)
//            timeLeft--
//        }
//        if (timeLeft == 0) {
//            examFinished = true
//            showSuccessDialog = true
//        }
//    }
//
//    Scaffold(
//        containerColor = Color(0x33F2F2F2),
//
//        topBar = {
//            Surface(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .statusBarsPadding()
//                    .height(dimens.topBarHeight), // ✅ responsive
//                color = Color.Transparent
//            ) {
//
//                Row(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(horizontal = dimens.paddingMedium),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//
//                    Column(
//                        modifier = Modifier
//                            .background(Color.White, RoundedCornerShape(dimens.radiusMedium))
//                            .padding(
//                                horizontal = dimens.paddingMedium,
//                                vertical = dimens.paddingSmall
//                            )
//                    ) {
//                        Text(
//                            "Candidate ID : $candidateId",
//                            fontSize = dimens.textSmall,
//                            fontWeight = FontWeight.Bold
//                        )
//                        Text(
//                            "Candidate Name : $candidatName",
//                            fontSize = dimens.textSmall
//                        )
//                    }
//
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//
//                        CircularTimer(timeLeft, 1800)
//
//                        Spacer(modifier = Modifier.width(dimens.paddingMedium))
//
//                        IconButton(onClick = { showReviewDialog = true }) {
//                            Icon(Icons.Default.List, null)
//                        }
//                    }
//                }
//            }
//        }
//    ) { padding ->
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .padding(dimens.paddingMedium)
//        ) {
//
//            Row {
//                Text("Question ", fontSize = dimens.textLarge, fontWeight = FontWeight.Bold)
//                Text("${currentIndex + 1}",
//                    fontSize = dimens.textLarge,
//                    color = MaterialTheme.colorScheme.primary)
//                Text(" / ${questionList.size}", fontSize = dimens.textLarge)
//            }
//
//            Spacer(modifier = Modifier.height(dimens.paddingSmall))
//
//            LinearProgressIndicator(
//                progress = (currentIndex + 1) / questionList.size.toFloat(),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(dimens.progressHeight)
//            )
//
//            Spacer(modifier = Modifier.height(dimens.paddingMedium))
//
//            Text(
//                text = "${currentIndex + 1}. ${currentQuestion.question_value}",
//                fontSize = dimens.textMedium,
//                fontWeight = FontWeight.Bold
//            )
//
//            Spacer(modifier = Modifier.height(dimens.paddingMedium))
//
//            currentQuestion.option.forEach { option ->
//
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable {
//                            answers[currentQuestion.question_id] = option.option_key
//                        }
//                        .padding(vertical = dimens.paddingSmall),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//
//                    RadioButton(
//                        selected = answers[currentQuestion.question_id] == option.option_key,
//                        onClick = {
//                            answers[currentQuestion.question_id] = option.option_key
//                        }
//                    )
//
//                    Text(
//                        option.option_value,
//                        fontSize = dimens.textMedium,
//                        modifier = Modifier.padding(start = dimens.paddingSmall)
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(dimens.paddingMedium))
//
//            // BUTTON ROW
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(dimens.paddingSmall)
//            ) {
//
//                ActionButton("Save & Next", Color(0xFF4CAF50), dimens) {
//                    val id = currentQuestion.question_id
//                    questionStatus[id] = "Save & Next"
//                    if (currentIndex < questionList.lastIndex) currentIndex++
//                }
//
//                ActionButton("Save & Review", Color(0xFFFFC107), dimens) {
//                    val id = currentQuestion.question_id
//                    questionStatus[id] = "Save & Review"
//                    if (currentIndex < questionList.lastIndex) currentIndex++
//                }
//
//                ActionButton("Mark", Color(0xFF03A9F4), dimens) {
//                    val id = currentQuestion.question_id
//                    questionStatus[id] = "Mark"
//                    if (currentIndex < questionList.lastIndex) currentIndex++
//                }
//
//                ActionButton("Clear", Color.Gray, dimens) {
//                    val id = currentQuestion.question_id
//                    answers.remove(id)
//                    questionStatus.remove(id)
//                }
//            }
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            // NAVIGATION
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//
//                OutlineBtn("Previous", dimens) {
//                    if (currentIndex > 0) currentIndex--
//                }
//
//                OutlineBtn("Next", dimens) {
//                    if (currentIndex < questionList.lastIndex) currentIndex++
//                }
//            }
//        }
//    }
//}














































//this code use old 17/03/2026
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CBTExamScreen(questionList: List<Question>,candidateId: String,candidatName: String, examId: String,questionSetId: String, batchId: String) {
//    if (questionList.isEmpty()) {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            Text("Loading Questions...")
//        }
//        return
//    }
//
//    // ---------------- STATES ----------------
//    var examStarted by remember { mutableStateOf(false) }
//    var currentIndex by remember { mutableStateOf(0) }
//    var timeLeft by remember { mutableStateOf(1800) }
//    var examFinished by remember { mutableStateOf(false) }
//    var editMode by remember { mutableStateOf(false) }
//    val questionStatus = remember { mutableStateMapOf<String, String>() }
//    var showReviewDialog by remember { mutableStateOf(false) }
//
//    val screenHeight = LocalConfiguration.current.screenHeightDp
//    var showSuccessDialog by remember { mutableStateOf(false) }
//
//
//
//    val answers = remember { mutableStateMapOf<String, String>() }
//    val markedQuestions = remember { mutableStateMapOf<String, Boolean>() }
//
//    // ---------------- START SCREEN ----------------
//    if (!examStarted) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(
//                    Brush.verticalGradient(
//                        listOf(
//                            Color(0xFF36D1A6),
//                            Color(0xFF4F9488),
//                            Color(0xFF2A4D44)
//                        )
//                    )
//                ),
//            contentAlignment = Alignment.Center
//        )
//
//        {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//
//                Text(
//                    text = "Welcome To CBT Exam",
//                    color = Color.White,
//                    fontSize = 22.sp,
//                    fontWeight = FontWeight.Bold
//                )
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                Button(onClick = {
//                    examStarted = true
//                    currentIndex = 0
//                    timeLeft = 1800
//                }) {
//                    Text("Start Exam")
//                }
//            }
//        }
//        return
//    }
//
//    val currentQuestion = questionList[currentIndex]
//
//    // ---------------- TIMER ----------------
//    LaunchedEffect(examFinished) {
//        while (!examFinished && timeLeft > 0) {
//            delay(1000L)
//            timeLeft--
//        }
//        if (timeLeft == 0) {
//            examFinished = true
//            showSuccessDialog = true
//        }
//    }
//    Scaffold(
//        containerColor = Color(0x33F2F2F2),
////        containerColor = Color(0xFFF2F2F2),
//
//        topBar = {
//
//
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
////            Surface(
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .statusBarsPadding()
////                    .height(70.dp)
////                    .background(
////                        Brush.horizontalGradient(
////                            listOf(
////                                MaterialTheme.colorScheme.primary,
////                                MaterialTheme.colorScheme.primaryContainer,
////                                MaterialTheme.colorScheme.secondary
////                            )
////                        )
////                    ),
////                color = Color.Transparent
////            )
//
//            {
//
//                Row(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(horizontal = 12.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//
//                    // Candidate Info Box
//                    Column(
//                        modifier = Modifier
//                            .background(
//                                Color.White,
//                                RoundedCornerShape(8.dp)
//                            )
//                            .padding(horizontal = 12.dp, vertical = 6.dp)
//                    ) {
//
//                        Text(
//                            text = "Candidate ID : $candidateId",
//                            fontSize = 14.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Color.Black
//                        )
//
//                        Text(
//                            text ="Candidate Name : $candidatName",
//                            fontSize = 13.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Color.DarkGray
//                        )
//                    }
//
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//
//                        CircularTimer(
//                            timeLeft = timeLeft,
//                            totalTime = 30 * 60
//                        )
//
//                        Spacer(modifier = Modifier.width(12.dp))
//
//                        IconButton(
//                            onClick = { showReviewDialog = true }
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.List,
//                                contentDescription = null,
//                                tint = Color.White
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    )
//    { padding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .padding(16.dp)
//        ) {
//
//            // ---------------- QUESTION HEADER ----------------
//
//            Row(
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//
//                Text(
//                    text = "Question ",
//                    fontSize = 22.sp,
//                    fontWeight = FontWeight.Bold
//                )
//
//                Text(
//                    text = "${currentIndex + 1}",
//                    fontSize = 22.sp,
//                    color = MaterialTheme.colorScheme.primary,
////                    color = Color(0xFF1DB9A6),
//                    fontWeight = FontWeight.Bold
//                )
//
//                Text(
//                    text = " / ${questionList.size}",
//                    fontSize = 22.sp
//                )
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//            LinearProgressIndicator(
//                progress = (currentIndex + 1) / questionList.size.toFloat(),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(6.dp)
//                    .clip(RoundedCornerShape(10.dp)),
//                color = MaterialTheme.colorScheme.primary,
//                trackColor = MaterialTheme.colorScheme.primaryContainer
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//
//
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//
//            )
//            {
//
//                // Question with number
//                Text(
//                    text = "${currentIndex + 1}. ${currentQuestion.question_value}",
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//                currentQuestion.option.forEach { option ->
//
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable {
//                                answers[currentQuestion.question_id] = option.option_key
//                            }
//                            .padding(vertical = 8.dp),
//
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//
//                        RadioButton(
//                            selected = answers[currentQuestion.question_id] == option.option_key,
//                            onClick = {
//                                answers[currentQuestion.question_id] = option.option_key
//                            }
//                        )
//
//                        Text(
//                            text = option.option_value,
//                            color = Color.Black,
//                            fontSize = 16.sp,
//                            modifier = Modifier.padding(start = 8.dp)
//                        )
//                    }
//                }
//                Spacer(modifier = Modifier.height(20.dp))
//
//                // ---------------- HORIZONTAL BUTTONS ----------------
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(6.dp)
//                ) {
//
//                    // Save & Next
//                    Text(
//                        text = "Save & Next",
//                        color = Color.White,
//                        fontSize = 10.sp,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier
//                            .weight(1f)
//                            .background(Color(0xFF4CAF50), shape = RoundedCornerShape(6.dp))
//                            .clickable {
//                                val id = currentQuestion.question_id
//                                questionStatus[id] = "Save & Next"
//
//                                if (currentIndex < questionList.lastIndex)
//                                    currentIndex++
//                            }
//                            .padding(vertical = 10.dp)
//                    )
//
//                    // Save & Review Later
//                    Text(
//                        text = "Save & Review",
//                        color = Color.Black,
//                        fontSize = 10.sp,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier
//                            .weight(1f)
//                            .background(Color(0xFFFFC107), shape = RoundedCornerShape(6.dp))
//                            .clickable {
//                                val id = currentQuestion.question_id
//                                questionStatus[id] = "Save & Review Later"
//
//                                if (currentIndex < questionList.lastIndex)
//                                    currentIndex++
//                            }
//                            .padding(vertical = 10.dp)
//                    )
//
//                    // Marked for Review Later
//                    Text(
//                        text = "Mark",
//                        color = Color.White,
//                        fontSize = 10.sp,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier
//                            .weight(1f)
//                            .background(Color(0xFF03A9F4), shape = RoundedCornerShape(6.dp))
//                            .clickable {
//                                val id = currentQuestion.question_id
//                                questionStatus[id] = "Marked for Review Later"
//
//                                if (currentIndex < questionList.lastIndex)
//                                    currentIndex++
//                            }
//                            .padding(vertical = 10.dp)
//                    )
//
//                    // Clear
//                    Text(
//                        text = "Clear",
//                        color = Color.White,
//                        fontSize = 10.sp,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier
//                            .weight(1f)
//                            .background(Color.Gray, shape = RoundedCornerShape(6.dp))
//                            .clickable {
//                                val id = currentQuestion.question_id
//                                answers.remove(id)
//                                questionStatus.remove(id)
//                            }
//                            .padding(vertical = 10.dp)
//                    )
//                }
//                Spacer(modifier = Modifier.weight(1f))
//
//                // ---------------- NAVIGATION BUTTONS ----------------
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 16.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .clip(RoundedCornerShape(12.dp))
//                            .background(Color.Transparent)
//                            .border(
//                                width = 2.dp,
//                                brush = Brush.horizontalGradient(
//                                    listOf(
//                                        MaterialTheme.colorScheme.primary,
//                                        MaterialTheme.colorScheme.primaryContainer,
//                                        MaterialTheme.colorScheme.secondary
//                                    )
//                                ),
//                                shape = RoundedCornerShape(12.dp)
//                            )
//                            .clickable {
//                                if (currentIndex > 0) currentIndex--
//                            }
//                            .padding(horizontal = 16.dp, vertical = 10.dp)
//                    ) {
//                        Text(
//                            text = "Previous",
//                            color = MaterialTheme.colorScheme.primary,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//
//                    // Next
//
//                    Box(
//                        modifier = Modifier
//                            .clip(RoundedCornerShape(12.dp))
//                            .background(Color.Transparent)
//                            .border(
//                                width = 2.dp,
//                                brush = Brush.horizontalGradient(
//                                    listOf(
//                                        MaterialTheme.colorScheme.primary,
//                                        MaterialTheme.colorScheme.primaryContainer,
//                                        MaterialTheme.colorScheme.secondary
//                                    )
//                                ),
//                                shape = RoundedCornerShape(12.dp)
//                            )
//                            .clickable {
//                                if (currentIndex < questionList.lastIndex)
//                                    currentIndex++
//                            }
//                            .padding(horizontal = 16.dp, vertical = 10.dp)
//                    ) {
//                        Text(
//                            text = "Next",
//                            color = MaterialTheme.colorScheme.primary,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//                }
//                if (showReviewDialog) {
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
//                        Surface(
//                            modifier = Modifier.fillMaxSize(),
//                            color = Color.White
//                        ) {
//
//                            Column(
//                                modifier = Modifier
//                                    .fillMaxSize()
//                                    .padding(16.dp)
//                            ) {
//
//                                // Top Bar (Back + Title)
//                                Row(
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//
//                                    IconButton(
//                                        onClick = { showReviewDialog = false }
//                                    ) {
//                                        Icon(
//                                            imageVector = Icons.Default.ArrowBack,
//                                            contentDescription = "Back"
//                                        )
//                                    }
//
//                                    Text(
//                                        text = "Question Index",
//                                        fontSize = 20.sp,
//                                        fontWeight = FontWeight.Bold
//                                    )
//                                }
//
//                                Spacer(modifier = Modifier.height(10.dp))
//
//                                // Color Legend Layout
//                                Row(
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//
//                                    // Red Box
//                                    Box(
//                                        modifier = Modifier
//                                            .size(18.dp)
//                                            .background(Color(0xFFF44336), RoundedCornerShape(4.dp))
//                                    )
//
//                                    Spacer(modifier = Modifier.width(6.dp))
//
//                                    Text("Not Answered")
//
//                                    Spacer(modifier = Modifier.width(20.dp))
//
//                                    // Green Box
//                                    Box(
//                                        modifier = Modifier
//                                            .size(18.dp)
//                                            .background(Color(0xFF4CAF50), RoundedCornerShape(4.dp))
//                                    )
//
//                                    Spacer(modifier = Modifier.width(6.dp))
//
//                                    Text("Answered")
//                                }
//
//                                Spacer(modifier = Modifier.height(16.dp))
//
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
//
//                                            answers.containsKey(id) && markedQuestions.contains(id) ->
//                                                Color(0xFFFFC107)   // Answered & Review
//
//                                            !answers.containsKey(id) && markedQuestions.contains(id) ->
//                                                Color(0xFF4FC3F7)   // Review Later
//
//                                            answers.containsKey(id) ->
//                                                Color(0xFF4CAF50)   // Answered
//
//                                            else ->
//                                                Color(0xFFF44336)   // Not Answered
//                                        }
//
//                                        Box(
//                                            modifier = Modifier
//                                                .size(50.dp)
//                                                .clip(RoundedCornerShape(8.dp))
//                                                .background(bgColor)
//                                                .clickable {
//                                                    currentIndex = index
//                                                    showReviewDialog = false
//                                                },
//                                            contentAlignment = Alignment.Center
//                                        ) {
//
//                                            Text(
//                                                text = "${index + 1}",
//                                                color = Color.White,
//                                                fontWeight = FontWeight.Bold
//                                            )
//                                        }
//                                    }
//                                }
//
//
//
//
//                            }
//                        }
//                    }
//                }
//                Spacer(modifier = Modifier.weight(1f))
//
//                // Submit Button
//
//                val context = LocalContext.current
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp)
//                        .clip(RoundedCornerShape(14.dp))
//                        .background(MaterialTheme.colorScheme.primary)
//                        .clickable {
//
//                            // 🔥 Prepare List
//                            val submitList = questionList.map { question ->
//
//                                val answerGiven = answers[question.question_id] ?: ""
//
//                                val category = when {
//                                    markedQuestions[question.question_id] == true ->
//                                        "Mark & Review"
//
//                                    answerGiven.isNotEmpty() ->
//                                        "Save & Next"
//
//                                    else ->
//                                        "Not Answered"
//                                }
//
//                                SubmitExamItem(
//                                    question_id = question.question_id,
//                                    answer_given = answerGiven,
//                                    category = category,
//                                    marks_per_qs = question.marks_per_qs
//                                )
//                            }
//
//                            val request = SubmitExamRequest(
//                                cand_id = candidateId,
//                                batch_id = batchId,
//                                exam_id = examId,
//                                question_set_id = questionSetId,
//                                Ques_and_ans = submitList
//                            )
//
//
//                            // 🔥 API CALL
//                            CoroutineScope(Dispatchers.IO).launch {
//
//                                try {
//                                    val gson = Gson()
//                                    val jsonString = gson.toJson(request)
//
//                                    Log.d("FINAL_JSON", jsonString)
//                                    val response = RetrofitClient.api.submitExam(request)
//
//                                    withContext(Dispatchers.Main) {
//
//                                        if (response.isSuccessful) {
//
//                                            Log.d("SUBMIT_API", "Success: ${response.body()}")
//
//                                            Toast.makeText(
//                                                context,
//                                                "Exam Submitted Successfully ✅",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//
//                                            examFinished = true
//                                            showSuccessDialog = true
//
//                                        } else {
//
//                                            Toast.makeText(
//                                                context,
//                                                "Submission Failed ❌",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//
//                                            Log.e(
//                                                "SUBMIT_API",
//                                                "Error: ${response.errorBody()?.string()}"
//                                            )
//                                        }
//                                    }
//
//                                } catch (e: Exception) {
//
//                                    withContext(Dispatchers.Main) {
//
//                                        Toast.makeText(
//                                            context,
//                                            "Network Error ⚠️",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//
//                                    }
//
//                                    e.printStackTrace()
//                                }
//
//                            }
//
//                        }
//                        .padding(vertical = 14.dp),
//
//                    contentAlignment = Alignment.Center
//                )
//                {
//
//                    Text(
//                        text = if (editMode)
//                            "Update & Submit"
//                        else
//                            "Submit Exam",
//                        color = Color.White,
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 16.sp
//                    )
//
//                }
//
//            }
//        }
//
//
//    }
//}


















//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CBTExamScreen(questionList: List<Question>,candidateId: String,candidatName: String, examId: String,questionSetId: String, batchId: String) {
////fun CBTExamScreen(questionList: List<Question>,candidateId: String) {
//
//    if (questionList.isEmpty()) {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            Text("Loading Questions...")
//        }
//        return
//    }
//
//    // ---------------- STATES ----------------
//    var examStarted by remember { mutableStateOf(false) }
//    var currentIndex by remember { mutableStateOf(0) }
//    var timeLeft by remember { mutableStateOf(1800) }
//    var examFinished by remember { mutableStateOf(false) }
//    var editMode by remember { mutableStateOf(false) }
//    val questionStatus = remember { mutableStateMapOf<String, String>() }
//    var showReviewDialog by remember { mutableStateOf(false) }
//    var showSuccessDialog by remember { mutableStateOf(false) }
//
//
//
//    val answers = remember { mutableStateMapOf<String, String>() }
//    val markedQuestions = remember { mutableStateMapOf<String, Boolean>() }
//
//    val allAnswered = answers.keys.containsAll(questionList.map { it.question_id })
////    val batch_id = answers.keys.containsAll(questionList.map { it.})
//
////    batch_id = "195",
////                            exam_id = "47",
////                            question_set_id = "QS2",
//
//
//    // ---------------- START SCREEN ----------------
//    if (!examStarted) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(
//                    Brush.verticalGradient(
//                        listOf(
//                            Color(0xFF36D1A6),
//                            Color(0xFF4F9488),
//                            Color(0xFF2A4D44)
//                        )
//                    )
//                ),
//            contentAlignment = Alignment.Center
//        )
//
//        {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//
//                Text(
//                    text = "Welcome To CBT Exam",
//                    color = Color.White,
//                    fontSize = 22.sp,
//                    fontWeight = FontWeight.Bold
//                )
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                Button(onClick = {
//                    examStarted = true
//                    currentIndex = 0
//                    timeLeft = 1800
//                }) {
//                    Text("Start Exam")
//                }
//            }
//        }
//        return
//    }
//
//    val currentQuestion = questionList[currentIndex]
//
//    // ---------------- TIMER ----------------
//    LaunchedEffect(examFinished) {
//        while (!examFinished && timeLeft > 0) {
//            delay(1000L)
//            timeLeft--
//        }
//        if (timeLeft == 0) {
//            examFinished = true
//            showSuccessDialog = true
//        }
//    }
//    Scaffold(
//        containerColor = Color(0x33F2F2F2),
////        containerColor = Color(0xFFF2F2F2),
//
//        topBar = {
//            Surface(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .statusBarsPadding()
//                    .height(70.dp)
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
////            Surface(
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .statusBarsPadding()
////                    .height(60.dp)
////                    .background(
////                        Brush.horizontalGradient(
////                            listOf(
////                                Color(0xFF36D1A6),
////                                Color(0xFF4F9488),
////                                Color(0xFF2A4D44)
////                            )
////                        )
////                    ),
////                color = Color.Transparent
////            )
//
//
//
//            {
//
//                Row(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(horizontal = 12.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//
//                    // Candidate Info Box
//                    Column(
//                        modifier = Modifier
//                            .background(
//                                Color.White,
//                                RoundedCornerShape(8.dp)
//                            )
//                            .padding(horizontal = 12.dp, vertical = 6.dp)
//                    ) {
//
//                        Text(
//                            text = "Candidate ID : $candidateId",
//                            fontSize = 14.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Color.Black
//                        )
//
//                        Text(
//                            text ="Candidate Name : $candidatName",
//                            fontSize = 13.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Color.DarkGray
//                        )
//                    }
//
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//
//                        CircularTimer(
//                            timeLeft = timeLeft,
//                            totalTime = 30 * 60
//                        )
//
//                        Spacer(modifier = Modifier.width(12.dp))
//
//                        IconButton(
//                            onClick = { showReviewDialog = true }
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.List,
//                                contentDescription = null,
//                                tint = Color.White
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    )
//
//
//
//
//
//
//
//    { padding ->
//
//
//
//
//
//
//
//
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .padding(16.dp)
//        ) {
//
//            // ---------------- QUESTION HEADER ----------------
//
//            Row(
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//
//                Text(
//                    text = "Question ",
//                    fontSize = 22.sp,
//                    fontWeight = FontWeight.Bold
//                )
//
//                Text(
//                    text = "${currentIndex + 1}",
//                    fontSize = 22.sp,
//                    color = MaterialTheme.colorScheme.primary,
////                    color = Color(0xFF1DB9A6),
//                    fontWeight = FontWeight.Bold
//                )
//
//                Text(
//                    text = " / ${questionList.size}",
//                    fontSize = 22.sp
//                )
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//
////            LinearProgressIndicator(
////                progress = (currentIndex + 1) / questionList.size.toFloat(),
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .height(6.dp)
////                    .clip(RoundedCornerShape(10.dp)),
////                color = Color(0xFF1DB9A6)
////            )
//            LinearProgressIndicator(
//                progress = (currentIndex + 1) / questionList.size.toFloat(),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(6.dp)
//                    .clip(RoundedCornerShape(10.dp)),
//                color = MaterialTheme.colorScheme.primary,
//                trackColor = MaterialTheme.colorScheme.primaryContainer
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//
//        )
//        {
//
//                // Question with number
//                Text(
//                    text = "${currentIndex + 1}. ${currentQuestion.question_value}",
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//// Options simple text
////                currentQuestion.option.forEach { option ->
////
////                    Row(
////                        modifier = Modifier
////                            .fillMaxWidth()
////                            .clickable {
////                                answers[currentQuestion.question_id] =
////                                    option.option_key
////                            }
////                            .padding(vertical = 6.dp),
////                        verticalAlignment = Alignment.CenterVertically   // ⭐ important
////                    )
//
//            currentQuestion.option.forEach { option ->
//
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable {
//                            answers[currentQuestion.question_id] = option.option_key
//                        }
//                        .padding(vertical = 8.dp),
//
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//
//                    RadioButton(
//                        selected = answers[currentQuestion.question_id] == option.option_key,
//                        onClick = {
//                            answers[currentQuestion.question_id] = option.option_key
//                        }
//                    )
//
//                    Text(
//                        text = option.option_value,
//                        color = Color.Black,
//                        fontSize = 16.sp,
//                        modifier = Modifier.padding(start = 8.dp)
//                    )
//                }
//            }
//                Spacer(modifier = Modifier.height(20.dp))
//
//                // ---------------- HORIZONTAL BUTTONS ----------------
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(6.dp)
//                ) {
//
//                    // Save & Next
//                    Text(
//                        text = "Save & Next",
//                        color = Color.White,
//                        fontSize = 10.sp,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier
//                            .weight(1f)
//                            .background(Color(0xFF4CAF50), shape = RoundedCornerShape(6.dp))
//                            .clickable {
//                                val id = currentQuestion.question_id
//                                questionStatus[id] = "Save & Next"
//
//                                if (currentIndex < questionList.lastIndex)
//                                    currentIndex++
//                            }
//                            .padding(vertical = 10.dp)
//                    )
//
//                    // Save & Review Later
//                    Text(
//                        text = "Save & Review",
//                        color = Color.Black,
//                        fontSize = 10.sp,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier
//                            .weight(1f)
//                            .background(Color(0xFFFFC107), shape = RoundedCornerShape(6.dp))
//                            .clickable {
//                                val id = currentQuestion.question_id
//                                questionStatus[id] = "Save & Review Later"
//
//                                if (currentIndex < questionList.lastIndex)
//                                    currentIndex++
//                            }
//                            .padding(vertical = 10.dp)
//                    )
//
//                    // Marked for Review Later
//                    Text(
//                        text = "Mark",
//                        color = Color.White,
//                        fontSize = 10.sp,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier
//                            .weight(1f)
//                            .background(Color(0xFF03A9F4), shape = RoundedCornerShape(6.dp))
//                            .clickable {
//                                val id = currentQuestion.question_id
//                                questionStatus[id] = "Marked for Review Later"
//
//                                if (currentIndex < questionList.lastIndex)
//                                    currentIndex++
//                            }
//                            .padding(vertical = 10.dp)
//                    )
//
//                    // Clear
//                    Text(
//                        text = "Clear",
//                        color = Color.White,
//                        fontSize = 10.sp,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier
//                            .weight(1f)
//                            .background(Color.Gray, shape = RoundedCornerShape(6.dp))
//                            .clickable {
//                                val id = currentQuestion.question_id
//                                answers.remove(id)
//                                questionStatus.remove(id)
//                            }
//                            .padding(vertical = 10.dp)
//                    )
//                }
//                Spacer(modifier = Modifier.weight(1f))
//
//                // ---------------- NAVIGATION BUTTONS ----------------
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 16.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .clip(RoundedCornerShape(12.dp))
//                            .background(Color.Transparent)
//                            .border(
//                                width = 2.dp,
//                                brush = Brush.horizontalGradient(
//                                    listOf(
//                                        MaterialTheme.colorScheme.primary,
//                                        MaterialTheme.colorScheme.primaryContainer,
//                                        MaterialTheme.colorScheme.secondary
//                                    )
//                                ),
//                                shape = RoundedCornerShape(12.dp)
//                            )
//                            .clickable {
//                                if (currentIndex > 0) currentIndex--
//                            }
//                            .padding(horizontal = 16.dp, vertical = 10.dp)
//                    ) {
//                        Text(
//                            text = "Previous",
//                            color = MaterialTheme.colorScheme.primary,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//                    // Previous
////                    Box(
////                        modifier = Modifier
////                            .clip(RoundedCornerShape(12.dp))
////                            .background(
////                                Brush.horizontalGradient(
////                                    listOf(
////                                        Color(0xFF36D1A6),
////                                        Color(0xFF4F9488),
////                                        Color(0xFF2A4D44)
////                                    )
////                                )
////                            )
////                            .clickable {
////                                if (currentIndex > 0) currentIndex--
////                            }
////                            .padding(horizontal = 16.dp, vertical = 10.dp)
////                    ) {
////                        Text(
////                            text = "Previous",
////                            color = Color.White,
////                            fontWeight = FontWeight.Bold
////                        )
////                    }
//
//                    // Next
//
//                    Box(
//                        modifier = Modifier
//                            .clip(RoundedCornerShape(12.dp))
//                            .background(Color.Transparent)
//                            .border(
//                                width = 2.dp,
//                                brush = Brush.horizontalGradient(
//                                    listOf(
//                                        MaterialTheme.colorScheme.primary,
//                                        MaterialTheme.colorScheme.primaryContainer,
//                                        MaterialTheme.colorScheme.secondary
//                                    )
//                                ),
//                                shape = RoundedCornerShape(12.dp)
//                            )
//                            .clickable {
//                                if (currentIndex < questionList.lastIndex)
//                                    currentIndex++
//                            }
//                            .padding(horizontal = 16.dp, vertical = 10.dp)
//                    ) {
//                        Text(
//                            text = "Next",
//                            color = MaterialTheme.colorScheme.primary,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
////                    Box(
////                        modifier = Modifier
////                            .clip(RoundedCornerShape(12.dp))
////                            .background(
////                                Brush.horizontalGradient(
////                                    listOf(
////                                        Color(0xFF36D1A6),
////                                        Color(0xFF4F9488),
////                                        Color(0xFF2A4D44)
////                                    )
////                                )
////                            )
////                            .clickable {
////                                if (currentIndex < questionList.lastIndex)
////                                    currentIndex++
////                            }
////                            .padding(horizontal = 16.dp, vertical = 10.dp)
////                    ) {
////                        Text(
////                            text = "Next",
////                            color = Color.White,
////                            fontWeight = FontWeight.Bold
////                        )
////                    }
//                }
//
////                if (showReviewDialog) {
////
////                    Dialog(
////                        onDismissRequest = { },
////                        properties = DialogProperties(
////                            dismissOnBackPress = false,
////                            dismissOnClickOutside = false,
////                            usePlatformDefaultWidth = false
////                        )
////                    ) {
////
////                        Surface(
////                            modifier = Modifier.fillMaxSize(),
////                            color = Color.White
////                        ) {
////
////                            Column(
////                                modifier = Modifier
////                                    .fillMaxSize()
////                                    .padding(16.dp)
////                            ) {
////
////                                Text(
////                                    text = "Question Index",
////                                    fontSize = 20.sp,
////                                    fontWeight = FontWeight.Bold,
////                                    modifier = Modifier.padding(bottom = 12.dp)
////                                )
////
////                                LazyVerticalGrid(
////                                    columns = GridCells.Fixed(5),   // 5 boxes per row
////                                    modifier = Modifier.weight(1f),
////                                    verticalArrangement = Arrangement.spacedBy(8.dp),
////                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
////                                ) {
////
////                                    items(questionList.size) { index ->
////
////                                        val question = questionList[index]
////                                        val id = question.question_id
////
////                                        // STATUS COLOR LOGIC
////                                        val bgColor = when {
////
////                                            answers.containsKey(id) && markedQuestions.contains(id) ->
////                                                Color(0xFFFFC107)   // Answered & Review
////
////                                            !answers.containsKey(id) && markedQuestions.contains(id) ->
////                                                Color(0xFF4FC3F7)   // Review Later
////
////                                            answers.containsKey(id) ->
////                                                Color(0xFF4CAF50)   // Answered
////
////                                            else ->
////                                                Color(0xFFF44336)   // Not Answered
////                                        }
////
////                                        Box(
////                                            modifier = Modifier
////                                                .size(50.dp)
////                                                .clip(RoundedCornerShape(8.dp))
////                                                .background(bgColor)
////                                                .clickable {
////                                                    currentIndex = index
////                                                    showReviewDialog = false
////                                                },
////                                            contentAlignment = Alignment.Center
////                                        ) {
////
////                                            Text(
////                                                text = "${index + 1}",
////                                                color = Color.White,
////                                                fontWeight = FontWeight.Bold
////                                            )
////                                        }
////                                    }
////                                }
////
////                                Spacer(modifier = Modifier.height(10.dp))
////
////                                Button(
////                                    onClick = { showReviewDialog = false },
////                                    modifier = Modifier.fillMaxWidth(),
////                                    shape = RoundedCornerShape(10.dp)
////                                ) {
////                                    Text("Close")
////                                }
////                            }
////                        }
////                    }
////                }
//            if (showReviewDialog) {
//
//                Dialog(
//                    onDismissRequest = { },
//                    properties = DialogProperties(
//                        dismissOnBackPress = false,
//                        dismissOnClickOutside = false,
//                        usePlatformDefaultWidth = false
//                    )
//                ) {
//
//                    Surface(
//                        modifier = Modifier.fillMaxSize(),
//                        color = Color.White
//                    ) {
//
//                        Column(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .padding(16.dp)
//                        ) {
//
//                            // Top Bar (Back + Title)
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//
//                                IconButton(
//                                    onClick = { showReviewDialog = false }
//                                ) {
//                                    Icon(
//                                        imageVector = Icons.Default.ArrowBack,
//                                        contentDescription = "Back"
//                                    )
//                                }
//
//                                Text(
//                                    text = "Question Index",
//                                    fontSize = 20.sp,
//                                    fontWeight = FontWeight.Bold
//                                )
//                            }
//
//                            Spacer(modifier = Modifier.height(10.dp))
//
//                            // Color Legend Layout
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//
//                                // Red Box
//                                Box(
//                                    modifier = Modifier
//                                        .size(18.dp)
//                                        .background(Color(0xFFF44336), RoundedCornerShape(4.dp))
//                                )
//
//                                Spacer(modifier = Modifier.width(6.dp))
//
//                                Text("Not Answered")
//
//                                Spacer(modifier = Modifier.width(20.dp))
//
//                                // Green Box
//                                Box(
//                                    modifier = Modifier
//                                        .size(18.dp)
//                                        .background(Color(0xFF4CAF50), RoundedCornerShape(4.dp))
//                                )
//
//                                Spacer(modifier = Modifier.width(6.dp))
//
//                                Text("Answered")
//                            }
//
//                            Spacer(modifier = Modifier.height(16.dp))
//
//                            LazyVerticalGrid(
//                                columns = GridCells.Fixed(5),
//                                modifier = Modifier.weight(1f),
//                                verticalArrangement = Arrangement.spacedBy(8.dp),
//                                horizontalArrangement = Arrangement.spacedBy(8.dp)
//                            ) {
//
//                                items(questionList.size) { index ->
//
//                                    val question = questionList[index]
//                                    val id = question.question_id
//
//                                    val bgColor = when {
//
//                                        answers.containsKey(id) && markedQuestions.contains(id) ->
//                                            Color(0xFFFFC107)   // Answered & Review
//
//                                        !answers.containsKey(id) && markedQuestions.contains(id) ->
//                                            Color(0xFF4FC3F7)   // Review Later
//
//                                        answers.containsKey(id) ->
//                                            Color(0xFF4CAF50)   // Answered
//
//                                        else ->
//                                            Color(0xFFF44336)   // Not Answered
//                                    }
//
//                                    Box(
//                                        modifier = Modifier
//                                            .size(50.dp)
//                                            .clip(RoundedCornerShape(8.dp))
//                                            .background(bgColor)
//                                            .clickable {
//                                                currentIndex = index
//                                                showReviewDialog = false
//                                            },
//                                        contentAlignment = Alignment.Center
//                                    ) {
//
//                                        Text(
//                                            text = "${index + 1}",
//                                            color = Color.White,
//                                            fontWeight = FontWeight.Bold
//                                        )
//                                    }
//                                }
//                            }
//
//
//
//
//                        }
//                    }
//                }
//            }
//                Spacer(modifier = Modifier.weight(1f))
//
//                // Submit Button
//
//                val context = LocalContext.current
//
////                Box(
////                    modifier = Modifier
////                        .fillMaxWidth()
////                        .padding(16.dp)
////                        .clip(RoundedCornerShape(14.dp))
////                        .background(
////                            Brush.horizontalGradient(
////                                listOf(
////                                    MaterialTheme.colorScheme.primary,
////                                    MaterialTheme.colorScheme.primaryContainer,
////                                    MaterialTheme.colorScheme.secondary
////                                )
////                            )
////                        )
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//                    .clip(RoundedCornerShape(14.dp))
//                    .background(MaterialTheme.colorScheme.primary)
//                        .clickable {
//
//                            // 🔥 Prepare List
//                            val submitList = questionList.map { question ->
//
//                                val answerGiven = answers[question.question_id] ?: ""
//
//                                val category = when {
//                                    markedQuestions[question.question_id] == true ->
//                                        "Mark & Review"
//
//                                    answerGiven.isNotEmpty() ->
//                                        "Save & Next"
//
//                                    else ->
//                                        "Not Answered"
//                                }
//
//                                SubmitExamItem(
//                                    question_id = question.question_id,
//                                    answer_given = answerGiven,
//                                    category = category,
//                                    marks_per_qs = question.marks_per_qs
//                                )
//                            }
//
//                            val request = SubmitExamRequest(
//                                cand_id = candidateId,
//                                batch_id = batchId,
//                                exam_id = examId,
//                                question_set_id = questionSetId,
//                                Ques_and_ans = submitList
//                            )
//
//
//                            // 🔥 API CALL
//                            CoroutineScope(Dispatchers.IO).launch {
//
//                                try {
//                                    val gson = Gson()
//                                    val jsonString = gson.toJson(request)
//
//                                    Log.d("FINAL_JSON", jsonString)
//                                    val response = RetrofitClient.api.submitExam(request)
//
//                                    withContext(Dispatchers.Main) {
//
//                                        if (response.isSuccessful) {
//
//                                            Log.d("SUBMIT_API", "Success: ${response.body()}")
//
//                                            Toast.makeText(
//                                                context,
//                                                "Exam Submitted Successfully ✅",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//
//                                            examFinished = true
//                                            showSuccessDialog = true
//
//                                        } else {
//
//                                            Toast.makeText(
//                                                context,
//                                                "Submission Failed ❌",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//
//                                            Log.e(
//                                                "SUBMIT_API",
//                                                "Error: ${response.errorBody()?.string()}"
//                                            )
//                                        }
//                                    }
//
//                                } catch (e: Exception) {
//
//                                    withContext(Dispatchers.Main) {
//
//                                        Toast.makeText(
//                                            context,
//                                            "Network Error ⚠️",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//
//                                    }
//
//                                    e.printStackTrace()
//                                }
//
//                            }
//
//                        }
//                        .padding(vertical = 14.dp),
//
//                    contentAlignment = Alignment.Center
//                )
//                {
//
//                    Text(
//                        text = if (editMode)
//                            "Update & Submit"
//                        else
//                            "Submit Exam",
//                        color = Color.White,
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 16.sp
//                    )
//
//                }
//
//            }
//        }
//
//
//    }
//}
