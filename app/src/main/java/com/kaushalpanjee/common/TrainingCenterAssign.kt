package com.kaushalpanjee.common

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.kaushalpanjee.R
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.common.model.request.*
import com.kaushalpanjee.common.model.response.*
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.databinding.FragmentTrainingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrainingCenterAssign : BaseFragment<FragmentTrainingBinding>(FragmentTrainingBinding::inflate) {

    private val commonViewModel: CommonViewModel by viewModels()
    private var selectedScheme: String? = ""
    private var stateCode: String? = ""

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        requireActivity().window.insetsController?.hide(
            android.view.WindowInsets.Type.statusBars()
        )

         stateCode = arguments?.getString("stateCode")
         selectedScheme = arguments?.getString("schemeType")


        commonViewModel.getDistrictListApi(
            stateCode.toString(),
            AppUtil.getSavedTokenPreference(requireContext()),
            userPreferences.getUseID()
        )

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    TrainingCenterScreen(commonViewModel, selectedScheme,userPreferences.getUseID(),navController = findNavController()  )
                }
            }
        }
    }



}

@Composable
fun TrainingCenterScreen(
    commonViewModel: CommonViewModel,
    selectedScheme: String?,
    loginId: String,
    navController: NavController
) {
    val context = LocalContext.current

    var showSchemePopup by remember { mutableStateOf(false) }


    /* ---------------- DISTRICT ---------------- */
    val districtResponse by commonViewModel.getDistrictList.collectAsState()
    var selectedDistrict by remember { mutableStateOf<DistrictList?>(null) }

    val districtList = rememberApiList(
        response = districtResponse,
        getCode = { it.responseCode },
        extractList = { it.districtList },
        emptyMessage = "No District Found"
    )

    /* ---------------- PIA / BANK ---------------- */


    val piaResponse by commonViewModel.getPiaOrgList.collectAsState()
    val isPiaCalled by commonViewModel.isPiaCalled.collectAsState()
    var selectedPia by remember { mutableStateOf<PiaOrg?>(null) }

    val piaList = rememberApiList(
        response = piaResponse,
        getCode = { it.responseCode },
        extractList = { it.wrappedList },
        emptyMessage = "No PIA Found"
    )

    /* ---------------- TRAINING CENTER (DDUGKY) ---------------- */
    val trainingResponse by commonViewModel.getTrainingList.collectAsState()
    val isTrainingCalled by commonViewModel.isPiaTrainingCalled.collectAsState()
    var selectedTraining by remember { mutableStateOf<TrainingCenter?>(null) }

    val trainingList = rememberApiList(
        response = trainingResponse,
        getCode = { it.responseCode },
        extractList = { it.wrappedList },
        emptyMessage = "No Training Center Found"
    )

    /* ---------------- TRADE (DDUGKY) ---------------- */
    val tradeResponse by commonViewModel.getTradeList.collectAsState()
    val isTradeCalled by commonViewModel.isPiaTradeCalled.collectAsState()
    var selectedTrade by remember { mutableStateOf<Trades?>(null) }

    val tradeList = rememberApiList(
        response = tradeResponse,
        getCode = { it.responseCode },
        extractList = { it.wrappedList },
        emptyMessage = "No Trade Found"
    )

    /* ---------------- INSTITUTE (RSETI) ---------------- */
    val instituteResponse by commonViewModel.getInstituteList.collectAsState()
    val isInstituteCalled by commonViewModel.isInstituteCalled.collectAsState()
    var selectedInstitute by remember { mutableStateOf<Institute?>(null) }

    val instituteList = rememberApiList(
        response = instituteResponse,
        getCode = { it.responseCode },
        extractList = { it.wrappedList },
        emptyMessage = "No Institute Found"
    )

    /* ---------------- COURSE (RSETI) ---------------- */
    val courseResponse by commonViewModel.getInstituteCourseList.collectAsState()
    val isCourseCalled by commonViewModel.isInstituteCourseCalled.collectAsState()
    var selectedCourse by remember { mutableStateOf<InstituteCourse?>(null) }

    val courseList = rememberApiList(
        response = courseResponse,
        getCode = { it.responseCode },
        extractList = { it.wrappedList },
        emptyMessage = "No Course Found"
    )



    //------------------- insert .........--------

    val insertResponse by commonViewModel.insertTrainingCenter.collectAsState()
    val isInsertCalled by commonViewModel.isInsertTrainingCenterCalled.collectAsState()

    LaunchedEffect(insertResponse) {

        when (insertResponse) {

            is Resource.Success -> {
                val res =
                    (insertResponse as Resource.Success<out InsertTrainingCenterRes>).data

                if (res?.responseCode == 200) {
                    Toast.makeText(
                        context,
                        "Training Center assigned successfully",
                        Toast.LENGTH_LONG
                    ).show()

                    navController.navigateUp()

                    commonViewModel.resetInsertTrainingCenter()


                } else {
                    Toast.makeText(
                        context,
                        res?.responseDesc ?: "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            is Resource.Error -> {
                Toast.makeText(
                    context,
                    ("Something went wrong"),
                    Toast.LENGTH_LONG
                ).show()
            }

            else -> Unit
        }
    }


    /* ---------------- LOADER ---------------- */
    val isLoading =
        districtResponse is Resource.Loading ||
                (isPiaCalled && piaResponse is Resource.Loading) ||
                (isTrainingCalled && trainingResponse is Resource.Loading) ||
                (isTradeCalled && tradeResponse is Resource.Loading) ||
                (isInstituteCalled && instituteResponse is Resource.Loading) ||
                (isCourseCalled && courseResponse is Resource.Loading) ||
                (isInsertCalled && insertResponse is Resource.Loading)





    Box(modifier = Modifier.fillMaxSize()) {

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {


            Image(
                painter = painterResource(id = R.drawable.ic_arrow_back_ios_new),
                contentDescription = "Back",
                modifier = Modifier
                    .clickable {
                        navController.popBackStack()
                    }
            )



            Spacer(modifier = Modifier.width(20.dp))



            TopLogoRow()

            Text("Assign Training Center", style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(0.dp,15.dp,0.dp,0.dp))
            Spacer(modifier = Modifier.height(16.dp))

            BorderedTextView(selectedScheme)

            Spacer(modifier = Modifier.height(12.dp))

            /* ---------------- DISTRICT ---------------- */
            SimpleDropdown(
                label = "Select District",
                selectedItem = selectedDistrict,
                options = districtList,
                optionLabel = { it.districtName },
                onValueSelected = { district ->
                    selectedDistrict = district

                    // reset everything
                    selectedPia = null
                    selectedTraining = null
                    selectedTrade = null
                    selectedInstitute = null
                    selectedCourse = null

                    commonViewModel.resetAllDependentApis()

                    commonViewModel.getPiaOrgList(
                        PiaListReq(
                            BuildConfig.VERSION_NAME,
                            selectedScheme!!,
                            district.districtCode
                        )
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            /* ================= DDUGKY FLOW ================= */
            if (selectedScheme == "DDUGKY") {

                SimpleDropdown(
                    label = "Select PIA",
                    selectedItem = selectedPia,
                    options = piaList,
                    optionLabel = { it.piaOrgName },
                    onValueSelected = { pia ->
                        selectedPia = pia
                        selectedTraining = null
                        selectedTrade = null

                        commonViewModel.resetPiaTraining()
                        commonViewModel.resetPiaTrade()

                        commonViewModel.getPiaTrainingList(
                            PiaTrainingCenterReq(
                                BuildConfig.VERSION_NAME,
                                pia.piaOrgCode
                            )
                        )
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SimpleDropdown(
                    label = "Select Training Center",
                    selectedItem = selectedTraining,
                    options = trainingList,
                    optionLabel = { it.trainingCenterName },
                    onValueSelected = { training ->
                        selectedTraining = training
                        selectedTrade = null

                        commonViewModel.resetPiaTrade()

                        commonViewModel.getPiaTradeList(
                            PiaTradeReq(
                                BuildConfig.VERSION_NAME,
                                training.trainingCenterId.toString()
                            )
                        )
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SimpleDropdown(
                    label = "Select Trade",
                    selectedItem = selectedTrade,
                    options = tradeList,
                    optionLabel = { it.courseName },
                    onValueSelected = { trade ->
                        selectedTrade = trade
                    }
                )
            }

            /* ================= RSETI FLOW ================= */
            if (selectedScheme == "RSETI") {

                SimpleDropdown(
                    label = "Select Bank",
                    selectedItem = selectedPia,
                    options = piaList,
                    optionLabel = { it.piaOrgName },
                    onValueSelected = { bank ->
                        selectedPia = bank
                        selectedInstitute = null
                        selectedCourse = null

                        commonViewModel.resetInstitute()
                        commonViewModel.resetInstituteCourse()

                        commonViewModel.getInstituteList(
                            OrgInstituteReq(
                                BuildConfig.VERSION_NAME,
                                bank.piaOrgCode
                            )
                        )
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SimpleDropdown(
                    label = "Select Institute",
                    selectedItem = selectedInstitute,
                    options = instituteList,
                    optionLabel = { it.instituteName },
                    onValueSelected = { institute ->
                        selectedInstitute = institute
                        selectedCourse = null

                        commonViewModel.resetInstituteCourse()

                        commonViewModel.getInstituteCourseList(
                            InstituteCourseReq(
                                BuildConfig.VERSION_NAME,
                                institute.instituteId.toString()
                            )
                        )
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SimpleDropdown(
                    label = "Select Course",
                    selectedItem = selectedCourse,
                    options = courseList,
                    optionLabel = { it.instCourseName },
                    onValueSelected = { course ->
                        selectedCourse = course
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                    when (selectedScheme) {

                        // ================= DDUGKY =================
                        "DDUGKY" -> {
                            when {
                                selectedDistrict == null -> {
                                    showError(context, "Please select District")
                                }

                                selectedPia == null -> {
                                    showError(context, "Please select PIA")
                                }

                                selectedTraining == null -> {
                                    showError(context, "Please select Training Center")
                                }

                                selectedTrade == null -> {
                                    showError(context, "Please select Trade")
                                }

                                else -> {


                                    val request = InsertTrainingCenterReq(
                                        appVersion = BuildConfig.VERSION_NAME,
                                        loginId = loginId,
                                        trainingCenterId = selectedTraining!!.trainingCenterId,
                                        schemeType = selectedScheme,
                                        districtCode = selectedDistrict!!.districtCode,
                                        piaId = selectedPia!!.piaOrgCode,
                                        courseId = selectedTrade!!.courseId,
                                        orgId = "",
                                        instituteId = "0"
                                    )

                                    commonViewModel.insertTrainingCenter(
                                        insertTrainingCenterReq = request,
                                        header = AppUtil.getSavedTokenPreference(context))
                                }
                            }
                        }

                        // ================= RSETI =================
                        "RSETI" -> {
                            when {
                                selectedDistrict == null -> {
                                    showError(context, "Please select District")
                                }

                                selectedPia == null -> {
                                    showError(context, "Please select Bank")
                                }

                                selectedInstitute == null -> {
                                    showError(context, "Please select Institute")
                                }

                                selectedCourse == null -> {
                                    showError(context, "Please select Course")
                                }

                                else -> {

                                    // 👉 Call FINAL SUBMIT API here

                                    val request = InsertTrainingCenterReq(
                                        appVersion = BuildConfig.VERSION_NAME,
                                        loginId = loginId,
                                        schemeType = selectedScheme,
                                        districtCode = selectedDistrict!!.districtCode,
                                        orgId = selectedPia!!.piaOrgCode,
                                        instituteId = selectedInstitute!!.instituteId.toString(),
                                        courseId = selectedCourse!!.instCourseId,
                                        trainingCenterId = 0,
                                        piaId = "",

                                        )

                                    commonViewModel.insertTrainingCenter(
                                        insertTrainingCenterReq = request,
                                        header = AppUtil.getSavedTokenPreference(context)
                                    )



                                }
                            }
                        }

                        else -> {
                            showError(context, "Invalid Scheme Selected")
                        }
                    }
                }
            ) {
                Text("Assign Center")
            }

        }



            LaunchedEffect(selectedScheme) {
                if (selectedScheme == "NA") {
                    showSchemePopup = true
                }
            }



        CustomPopup(
            show = showSchemePopup,
            message = "Scheme is not selected, kindly complete your profile first",
            onDismiss = {
                showSchemePopup = false
            },
            onOkClick = {
                showSchemePopup = false
                navController.popBackStack()   // 👈 BACK NAVIGATION
            }
        )
    }




}

/* ===================== COMMON COMPONENTS ===================== */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SimpleDropdown(
    label: String,
    selectedItem: T?,
    options: List<T>,
    optionLabel: (T) -> String,
    onValueSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedItem?.let { optionLabel(it) } ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { item ->
                DropdownMenuItem(
                    text = { Text(optionLabel(item)) },
                    onClick = {
                        onValueSelected(item)
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
        if (response is Resource.Success &&

            response.data?.let { getCode(it) } in listOf(201, 202)
        ) {
            Toast.makeText(context, emptyMessage, Toast.LENGTH_SHORT).show()
        }
    }

    return remember(response) {
        if (response is Resource.Success &&
            response.data?.let { getCode(it) } == 200
        ) {
            extractList(response.data)
        } else emptyList()
    }
}

@Composable
fun BorderedTextView(text: String?) {
    Surface(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text ?: "")
        }
    }
}

fun showError(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

@Composable
fun TopLogoRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 12.dp,
                end = 12.dp,
                top = 25.dp
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_ddgky),
            contentDescription = "DDUGKY",
            modifier = Modifier
                .width(100.dp)
                .height(70.dp)
        )

        Spacer(modifier = Modifier.width(20.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_rseti),
            contentDescription = "RSETI",
            modifier = Modifier
                .width(100.dp)
                .height(70.dp)
        )
    }
}

@Composable
fun CustomPopup(
    show: Boolean,
    message: String,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit
) {
    if (show) {
        Dialog(onDismissRequest = {}) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "ALERT ❌",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(message)

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = onOkClick) {
                        Text("OK")
                    }
                }
            }
        }
    }
}
