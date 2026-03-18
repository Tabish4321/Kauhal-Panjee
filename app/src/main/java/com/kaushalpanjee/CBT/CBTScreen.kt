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
import com.kaushalpanjee.CBT.dimens
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CBTExamScreen(
    questionList: List<Question>,
    candidateId: String,
    candidatName: String,
    examId: String,
    questionSetId: String,
    batchId: String
) {

    if (questionList.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading Questions...")
        }
        return
    }

    var examStarted by remember { mutableStateOf(false) }
    var currentIndex by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(1800) }
    var examFinished by remember { mutableStateOf(false) }
    var editMode by remember { mutableStateOf(false) }
    var showReviewDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val answers = remember { mutableStateMapOf<String, String>() }
    val markedQuestions = remember { mutableStateMapOf<String, Boolean>() }
    val questionStatus = remember { mutableStateMapOf<String, String>() }

    val screenHeight = LocalConfiguration.current.screenHeightDp

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

                Button(onClick = { examStarted = true }) {
                    Text("Start Exam")
                }
            }
        }
        return
    }

    val currentQuestion = questionList[currentIndex]

    // ---------------- TIMER ----------------
    LaunchedEffect(Unit) {
        while (!examFinished && timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
        if (timeLeft == 0) {
            examFinished = true
            showSuccessDialog = true
        }
    }

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

                        IconButton(onClick = { showReviewDialog = true }) {
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

            // 🔥 Scroll Area
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = R.string.question),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${currentIndex + 1}",
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    Text(" / ${questionList.size}", fontSize = 22.sp)
                }

                Spacer(Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = (currentIndex + 1) / questionList.size.toFloat(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    "${currentIndex + 1}. ${currentQuestion.question_value}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(16.dp))

                currentQuestion.option.forEach { option ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                answers[currentQuestion.question_id] = option.option_key
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        RadioButton(
                            selected = answers[currentQuestion.question_id] == option.option_key,
                            onClick = {
                                answers[currentQuestion.question_id] = option.option_key
                            }
                        )

                        Text(option.option_value, modifier = Modifier.padding(start = 8.dp))
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    Text(
                        text = "",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

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
                                .background(color, RoundedCornerShape(6.dp))
                                .clickable {
                                    val id = currentQuestion.question_id

                                    when (textRes) {

                                        R.string.clear -> {
                                            answers.remove(id)
                                            questionStatus.remove(id)
                                        }

                                        else -> {
                                            questionStatus[id] = text
                                            if (currentIndex < questionList.lastIndex) currentIndex++
                                        }
                                    }
                                }
                                .padding(vertical = 10.dp),

                            textAlign = TextAlign.Center,
                            color = if (textRes == R.string.save_review) Color.Black else Color.White,
                            fontSize = 10.sp
                        )
                    }
                }




















//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(6.dp)
//                ) {
//                    Text(
//                        text = stringResource(id = R.string.question),
//                        fontSize = 22.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//
//                    listOf(
//                        "Save & Next" to Color(0xFF4CAF50),
//                        "Save & Review" to Color(0xFFFFC107),
//                        "Mark" to Color(0xFF03A9F4),
//                        "Clear" to Color.Gray
//                    ).forEach { (text, color) ->
//
//                        Text(
//                            text,
//                            modifier = Modifier
//                                .weight(1f)
//                                .background(color, RoundedCornerShape(6.dp))
//                                .clickable {
//                                    val id = currentQuestion.question_id
//
//                                    when (text) {
//                                        "Clear" -> {
//                                            answers.remove(id)
//                                            questionStatus.remove(id)
//                                        }
//                                        else -> {
//                                            questionStatus[id] = text
//                                            if (currentIndex < questionList.lastIndex) currentIndex++
//                                        }
//                                    }
//                                }
//                                .padding(vertical = 10.dp),
//                            textAlign = TextAlign.Center,
//                            color = if (text == "Save & Review") Color.Black else Color.White,
//                            fontSize = 10.sp
//                        )
//                    }
//                }
//            }
                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Previous (Left)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Transparent)
                            .border(
                                width = 2.dp,
                                brush = Brush.horizontalGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.primaryContainer,
                                        MaterialTheme.colorScheme.secondary
                                    )
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                if (currentIndex > 0) currentIndex--
                            }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(
//                            text = "Previous",
                            text = stringResource(id = R.string.previous),
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Next (Right)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Transparent)
                            .border(
                                width = 2.dp,
                                brush = Brush.horizontalGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.primaryContainer,
                                        MaterialTheme.colorScheme.secondary
                                    )
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                if (currentIndex < questionList.lastIndex)
                                    currentIndex++
                            }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text =  stringResource(id = R.string.next),
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }


//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//
//                Button(onClick = {
//                    if (currentIndex > 0) currentIndex--
//                }) {
//                    Text("Previous")
//                }

//                Button(onClick = {
//                    if (currentIndex < questionList.lastIndex) currentIndex++
//                }) {
//                    Text("Next")
//                }


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
                                .padding(16.dp)
                        ) {

                            // Top Bar (Back + Title)
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                IconButton(
                                    onClick = { showReviewDialog = false }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }

                                Text(
//                                    text = "Question Index",
                                    text = stringResource(id = R.string.question_index),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Color Legend Layout
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                // Red Box
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .background(Color(0xFFF44336), RoundedCornerShape(4.dp))
                                )

                                Spacer(modifier = Modifier.width(6.dp))

//                                Text("Not Answered")
                                Text(
//                                    text = "Question Index",
                                    text = stringResource(id = R.string.not_answered)

                                )
//                                Text stringResource(id = R.string.question_index)

                                Spacer(modifier = Modifier.width(20.dp))

                                // Green Box
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .background(Color(0xFF4CAF50), RoundedCornerShape(4.dp))
                                )

                                Spacer(modifier = Modifier.width(6.dp))

//                                Text("Answered")
                                Text(
                                text = stringResource(id = R.string.answered))
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(5),
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                                            .size(50.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(bgColor)
                                            .clickable {
                                                currentIndex = index
                                                showReviewDialog = false
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {

                                        Text(
                                            text = "${index + 1}",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }




                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            // 🔥 YOUR ORIGINAL SUBMIT BUTTON (UNCHANGED)
            val context = LocalContext.current

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {

                        val submitList = questionList.map { question ->

                            val answerGiven = answers[question.question_id] ?: ""

                            val category = when {
                                markedQuestions[question.question_id] == true -> "Mark & Review"
                                answerGiven.isNotEmpty() -> "Save & Next"
                                else -> "Not Answered"
                            }

                            SubmitExamItem(
                                question_id = question.question_id,
                                answer_given = answerGiven,
                                category = category,
                                marks_per_qs = question.marks_per_qs
                            )
                        }

                        val request = SubmitExamRequest(
                            cand_id = candidateId,
                            batch_id = batchId,
                            exam_id = examId,
                            question_set_id = questionSetId,
                            Ques_and_ans = submitList
                        )

                        CoroutineScope(Dispatchers.IO).launch {
                            try {

                                val gson = Gson()
                                Log.d("FINAL_JSON", gson.toJson(request))

                                val response = RetrofitClient.api.submitExam(request)

                                withContext(Dispatchers.Main) {

                                    if (response.isSuccessful) {
                                        Toast.makeText(
                                            context,
                                            "Exam Submitted Successfully ✅",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        examFinished = true
                                        showSuccessDialog = true
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Submission Failed ❌",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "Network Error ⚠️",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = if (editMode) "Update & Submit" else "Submit Exam",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
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
