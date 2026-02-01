package com.kaushalpanjee.compose.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaushalpanjee.compose.presentation.contract.LanguageChangeState
import com.kaushalpanjee.compose.ui.commonComponent.BaseTopBar
import com.kaushalpanjee.compose.ui.language_change.LanguageCard
import com.kaushalpanjee.compose.ui.language_change.LanguageData.languages

/**
 * Created by Rishi Porwal
 */
@Composable
fun LanguageChangeScreen(
    state: LanguageChangeState,
    onBackClick: () -> Unit,
    onLanguageClick: (String) -> Unit,
    onDialogConfirm: () -> Unit,
    onDialogDismiss: () -> Unit
) {
    val languages = remember { languages }

    Scaffold(
        topBar = {
            BaseTopBar(
                title = "Choose Language",
                onBackClick = onBackClick
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = languages,
                    key = { it.code }
                ) { lang ->
                    LanguageCard(
                        language = lang,
                        selected = state.selectedLanguage == lang.code,
                        onClick = { onLanguageClick(lang.code) }
                    )
                }
            }


            AnimatedVisibility(
                visible = state.showConfirmDialog,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                AlertDialog(
                    onDismissRequest = onDialogDismiss,
                    containerColor = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 6.dp,
                    title = {
                        Text(
                            text = "Confirmation",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    },
                    text = {
                        Text(
                            text = "Do you want to change language?",
                            fontSize = 15.sp,
                            color = Color.DarkGray
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = onDialogConfirm) {
                            Text(
                                text = "Yes",
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = onDialogDismiss) {
                            Text(
                                text = "No",
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray
                            )
                        }
                    }
                )

            }
        }
    }
}

