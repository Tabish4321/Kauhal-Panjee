package com.kaushalpanjee.compose.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kaushalpanjee.R
import com.kaushalpanjee.compose.presentation.contract.AboutUnnatiIntent
import com.kaushalpanjee.compose.presentation.viewmodel.AboutUnnatiViewModel
import com.kaushalpanjee.compose.ui.SchemeCard
import com.kaushalpanjee.compose.ui.UnnatiTopHeader
import com.kaushalpanjee.compose.ui.theme.AvenirNextBold

/**
 * Created by Rishi Porwal
 */
@Composable
fun AboutUnnatiScreen(
    viewModel: AboutUnnatiViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column {
            UnnatiTopHeader(
                onBack = onBack
            )

            Spacer(Modifier.height(30.dp))


            Text(
                text = stringResource(R.string.unnati_schemes),
                color = Color.Black,
                fontFamily = AvenirNextBold,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            Spacer(Modifier.height(20.dp))


            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(state.schemes) { index, scheme ->
                    SchemeCard(
                        scheme = scheme,
                        expanded = state.expandedIndex == index,
                        onClick = { viewModel.onIntent(AboutUnnatiIntent.Expandecheme(index)) },
                        index
                    )
                }
            }
        }
    }
}

