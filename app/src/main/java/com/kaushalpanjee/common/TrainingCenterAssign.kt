package com.kaushalpanjee.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.kaushalpanjee.R
import com.kaushalpanjee.common.model.StateDataResponse
import com.kaushalpanjee.common.model.WrappedList
import com.kaushalpanjee.common.model.response.DistrictList
import com.kaushalpanjee.common.model.response.DistrictResponse
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.databinding.FragmentTrainingBinding
import com.pehchaan.backend.models.ApiResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrainingCenterAssign : BaseFragment<FragmentTrainingBinding>(FragmentTrainingBinding::inflate) {

    private var selectedScheme = "gdf"
    private val commonViewModel: CommonViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        commonViewModel.getStateListApi()

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    TrainingCenterScreen(commonViewModel)
                }
            }
        }
    }
}




@Composable
fun TrainingCenterScreen(commonViewModel: CommonViewModel) {

    val context = LocalContext.current

    val stateResponse by commonViewModel.getStateList.collectAsState()
    val districtResponse by commonViewModel.getDistrictList.collectAsState()

    val isLoading =
        stateResponse is Resource.Loading
             //   || districtResponse is Resource.Loading

    var topText by remember { mutableStateOf("") }

    var selectedState by remember { mutableStateOf<WrappedList?>(null) }
    var selectedDistrict by remember { mutableStateOf<DistrictList?>(null) }

    var pia by remember { mutableStateOf("") }
    var trainingCenter by remember { mutableStateOf("") }
    var trade by remember { mutableStateOf("") }
    var bank by remember { mutableStateOf("") }
    var institute by remember { mutableStateOf("") }

    val stateList = rememberApiList(
        response = stateResponse,
        getCode = { it.responseCode },
        extractList = { it.stateList },
        emptyMessage = "No States Found"
    )

    val districtList = rememberApiList(
        response = districtResponse,
        getCode = { it.responseCode },
        extractList = { it.districtList },
        emptyMessage = "No Districts Found"
    )

    val trainingCenters = listOf(
        "Training Center 1",
        "Training Center 2",
        "Training Center 3"
    )

    // 🔥 ROOT CONTAINER (for overlay loader)
    Box(modifier = Modifier.fillMaxSize()) {

        // ========== UI ==========
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {

            // ========== Progress Bar ==========
            if (isLoading) {
                Box( modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center ) { CircularProgressIndicator() } }

            // Back button
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
            }

            Text(
                text = "Assign Training Center",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Scheme
            OutlinedTextField(
                value = topText,
                onValueChange = { topText = it },
                label = { Text("Enter Scheme (DDUGKY / RSETI)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ---------- STATE ----------
            SimpleDropdown(
                label = "Select State",
                selectedValue = selectedState?.stateName ?: "",
                options = stateList.map { it.stateName },
                onValueSelected = { name ->
                    val state = stateList.firstOrNull { it.stateName == name }
                    selectedState = state
                    selectedDistrict = null      // reset district

                    state?.let {
                       /* commonViewModel.getDistrictListApi(
                            it.stateCode,
                            "",
                            ""
                        )*/
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ---------- DISTRICT ----------
            SimpleDropdown(
                label = "Select District",
                selectedValue = selectedDistrict?.districtName ?: "",
                options = districtList.map { it.districtName },
                onValueSelected = { name ->
                    selectedDistrict = districtList.firstOrNull { it.districtName == name }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ---------- DDUGKY ----------
            if (topText.equals("DDUGKY", true)) {
                SimpleDropdown("PIA", pia, trainingCenters) { pia = it }
                Spacer(modifier = Modifier.height(8.dp))
                SimpleDropdown("Training Center", trainingCenter, trainingCenters) { trainingCenter = it }
                Spacer(modifier = Modifier.height(8.dp))
                SimpleDropdown("Trade", trade, trainingCenters) { trade = it }
            }

            // ---------- RSETI ----------
            if (topText.equals("RSETI", true)) {
                SimpleDropdown("Bank", bank, trainingCenters) { bank = it }
                Spacer(modifier = Modifier.height(8.dp))
                SimpleDropdown("Institute", institute, trainingCenters) { institute = it }
                Spacer(modifier = Modifier.height(8.dp))
                SimpleDropdown("Trade", trade, trainingCenters) { trade = it }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Assign Center")
            }
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleDropdown(
    label: String,
    selectedValue: String,
    options: List<String>,
    onValueSelected: (String) -> Unit
) {
    //val options = listOf("Option 1", "Option 2", "Option 3")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onValueSelected(it)
                        expanded = false
                    }
                )
            }
        }
    }
}
@Composable
fun <T, R> rememberApiList(
    response: Resource<T>,
    getCode: (T) -> Int,
    extractList: (T) -> List<R>,
    emptyMessage: String
): List<R> {

    val context = LocalContext.current

    LaunchedEffect(response) {
        if (response is Resource.Success) {
            if (response.data?.let { getCode(it) } == 201) {
                Toast.makeText(context, emptyMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    return remember(response) {
        if (response is Resource.Success && response.data?.let { getCode(it) } == 200) {
            extractList(response.data)
        } else emptyList()
    }
}



