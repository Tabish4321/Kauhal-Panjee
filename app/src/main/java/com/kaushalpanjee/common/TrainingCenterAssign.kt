package com.kaushalpanjee.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kaushalpanjee.R
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.databinding.FragmentTrainingBinding


class TrainingCenterAssign : BaseFragment<FragmentTrainingBinding>(FragmentTrainingBinding::inflate) {

    var selectedScheme = "gdf"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    TrainingCenterScreen(selectedScheme)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingCenterScreen( selectedScheme : String) {
    val context = LocalContext.current


    var selectedCenter by remember { mutableStateOf("") }
    val trainingCenters = listOf(
        "Training Center 1 - Delhi",
        "Training Center 2 - Mumbai",
        "Training Center 3 - Bangalore",
        "Training Center 4 - Chennai",
        "Training Center 5 - Kolkata"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {




        // ✅ 1. BACK BUTTON (Top Left)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = {
                // Fragment pop karo ya finish()
              //  context.onBackPressedDispatcher.onBackPressed()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // ✅ 2. TWO IMAGES SIDE BY SIDE (Horizontal Row)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image 1 - DDGKY
            Image(
                painter = painterResource(id = R.drawable.ic_ddgky),
                contentDescription = "DDGKY Logo",
                modifier = Modifier
                    .size(100.dp, 70.dp)  // @dimen/dp_100, @dimen/dp_70
                    .padding(end = 20.dp),  // android:layout_marginStart="@dimen/dp_20"
                contentScale = ContentScale.Fit
            )

            // Image 2 - RSETI
            Image(
                painter = painterResource(id = R.drawable.ic_rseti),
                contentDescription = "RSETI Logo",
                modifier = Modifier
                    .size(100.dp, 70.dp),
                contentScale = ContentScale.Fit
            )
        }



        Text(
            text = "Assign Training Center",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )


        Text(
            text = selectedScheme,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )


        // ✅ Compose Dropdown (ExposedDropdownMenuBox)
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedCenter,
                onValueChange = { },
                readOnly = true,
                label = { Text("Select Training Center") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                leadingIcon = {
                    Icon(painter = painterResource(R.drawable.ddy_logo),null)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                trainingCenters.forEach { center ->
                    DropdownMenuItem(
                        text = { Text(text = center) },
                        onClick = {
                            selectedCenter = center
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // Submit logic
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Assign Center")
        }
    }
}
