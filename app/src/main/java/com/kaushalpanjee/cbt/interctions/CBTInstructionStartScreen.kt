package com.kaushalpanjee.cbt.interctions

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaushalpanjee.R


@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CBTInstructionStartScreen(
    title: String,
    remainingSeconds: Int,
    isDeclarationChecked: Boolean,
    onDeclarationChecked: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    onStartClick: () -> Unit
) {
//    val instructions = remember {
        val instructions = listOf(
            stringResource(R.string.instruction_1),
            stringResource(R.string.instruction_2),
            stringResource(R.string.instruction_3),
            stringResource(R.string.instruction_4),
            stringResource(R.string.instruction_5),
            stringResource(R.string.instruction_6),
            stringResource(R.string.instruction_7),
            stringResource(R.string.instruction_8),
            stringResource(R.string.instruction_9),
            stringResource(R.string.instruction_10),
            stringResource(R.string.instruction_11),
            stringResource(R.string.instruction_12),
            stringResource(R.string.instruction_13),
            stringResource(R.string.instruction_14)
        )
//        listOf(
//            "The Computer Based Test consists of 50 Multiple Choice Questions (MCQs). Each question carries 2 marks.",
//            "Total duration of the test is 60 minutes.",
//            "The test will be automatically submitted after 60 minutes or earlier if the candidate submits the test.",
//            "There is no negative marking.",
//            "Each question has four options. Only one option is correct.",
//            "Candidates can navigate to any question by clicking on the question number. Questions need not be attempted sequentially.",
//            "Use the Next and Previous buttons to move between questions.",
//            "Unanswered questions will be displayed in white or red color on the right-hand panel.",
//            "Answered questions will be displayed in green color.",
//            "Answered and marked for review questions will be displayed in orange color.",
//            "Questions marked for review will be displayed in blue color.",
//            "If a question is marked for review and an option is selected, it will be treated as attempted at final submission or on expiry of time.",
//            "The remaining time is displayed at the top-left corner of the screen besides candidate's details.",
//            "Don’t press ESC button during exam, after 2 attempts exam will be submitted forcefully and user will be logged out."
//        )
//    }

    Scaffold(
        containerColor = Color(0xFFF5F7FA),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1F8A70)
                )
            )
        }
    ) { innerPadding ->

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(12.dp)
        )
        {
            val isLargeScreen = maxWidth >= 900.dp
            val cardMaxWidth = if (isLargeScreen) 1100.dp else Dp.Unspecified

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .then(
                            if (cardMaxWidth != Dp.Unspecified) {
                                Modifier.widthIn(max = cardMaxWidth)
                            } else Modifier
                        )
                        .fillMaxWidth()
                        .fillMaxHeight(0.96f),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.5.dp, Color(0xFF63C56E)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                horizontal = if (isLargeScreen) 42.dp else 16.dp,
                                vertical = if (isLargeScreen) 28.dp else 16.dp
                            )
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = stringResource(R.string.exam_instructions),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = if (isLargeScreen) 30.sp else 22.sp,
                            color = Color(0xFF222222)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = stringResource(R.string.please_read_the_instructions_carefully_before_starting_the_exam),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = if (isLargeScreen) 21.sp else 15.sp,
                            color = Color(0xFF222222)
                        )

                        Spacer(modifier = Modifier.height(26.dp))

                        CBTInstructionList(
                            instructions = instructions,
                            isLargeScreen = isLargeScreen
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        Text(
                            text = stringResource(R.string.declaration),
                            fontWeight = FontWeight.Bold,
                            fontSize = if (isLargeScreen) 21.sp else 16.sp,
                            color = Color(0xFF222222)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        CBTDeclarationSection(
                            checked = isDeclarationChecked,
                            onCheckedChange = onDeclarationChecked,
                            text = stringResource(R.string.i_have_read_and_understood_the_instructions_and_wish_to_proceed_with_the_test),
                            isLargeScreen = isLargeScreen
                        )

                        Spacer(modifier = Modifier.height(26.dp))

                        Text(
                            text = stringResource(
                                R.string.exam_starts_in,
                                formatExamStartTime(remainingSeconds)
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontSize = if (isLargeScreen) 20.sp else 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF222222)
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = onStartClick,
                            enabled = isDeclarationChecked,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .defaultMinSize(minWidth = 140.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF173430),
//                                containerColor = Color(0xFFB8B8B8),
                                disabledContainerColor = Color(0xFFD3D3D3),
                                contentColor = Color.White,
                                disabledContentColor = Color.White
                            ),
                            contentPadding = PaddingValues(
                                horizontal = 28.dp,
                                vertical = 12.dp
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.start_now),
                                fontSize = if (isLargeScreen) 18.sp else 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }


        }


    }
}
fun formatExamStartTime(totalSeconds: Int): String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return "${hours} hrs ${minutes} mins ${seconds} sec"
}