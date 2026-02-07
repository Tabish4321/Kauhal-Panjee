package com.kaushalpanjee.compose.presentation.contract

import com.kaushalpanjee.compose.domain.model.Scheme

/**
 * Created by Rishi Porwal
 */

sealed interface AboutUnnatiIntent {
    data class Expandecheme(val index: Int) : AboutUnnatiIntent
}

data class AboutUnnatiState(
    val schemes: List<Scheme> = emptyList(),
    val expandedIndex: Int? = null
)


