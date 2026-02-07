package com.kaushalpanjee.compose.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.kaushalpanjee.compose.data.mapper.AboutUnnatiUiMapper
import com.kaushalpanjee.compose.presentation.contract.AboutUnnatiIntent
import com.kaushalpanjee.compose.presentation.contract.AboutUnnatiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Created by Rishi Porwal
 */
@HiltViewModel
class AboutUnnatiViewModel @Inject constructor(
    private val uiMapper: AboutUnnatiUiMapper
) : ViewModel() {

    private val _state = MutableStateFlow(AboutUnnatiState())
    val state = _state.asStateFlow()


    fun setScheme(
        ddugky: String,
        rseti: String,
        pmkvy: String
    ) {
        val schemes = uiMapper.buildSchemes(
            ddugky = ddugky,
            rseti = rseti,
            pmkvy = pmkvy
        )

        _state.update {
            it.copy(schemes = schemes)
        }
    }



    fun onIntent(intent: AboutUnnatiIntent) {
        when (intent) {
            is AboutUnnatiIntent.Expandecheme -> toggle(intent.index)
        }
    }

    private fun toggle(index: Int) {
        _state.update {
            it.copy(
                expandedIndex = if (it.expandedIndex == index) null else index
            )
        }
    }
}
