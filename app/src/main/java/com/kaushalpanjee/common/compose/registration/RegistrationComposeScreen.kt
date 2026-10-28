package com.kaushalpanjee.common.compose.registration

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.common.CommonViewModel
import com.kaushalpanjee.common.model.request.AadhaarCheckReq
import com.kaushalpanjee.common.model.request.ValidateOtpReq
import com.kaushalpanjee.core.util.AESCryptography
import com.kaushalpanjee.core.util.AppConstant
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.common.compose.login.LoginHeader
import com.kaushalpanjee.common.compose.login.LoginFooter
import com.kaushalpanjee.common.compose.login.LoginRuralBackground
import com.kaushalpanjee.common.compose.login.LoginLockBadge

private val RegGreen = Color(0xFF075E46)
private val RegGreenLight = Color(0xFFE8F5EF)
private val RegBg = Color(0xFFF5F8F6)
private val RegBorder = Color(0xFFDCE7E0)
private val RegText = Color(0xFF18352B)
private val RegMuted = Color(0xFF687A72)

/**
 * Compose version of the iOS registration journey.
 * Keep the existing CommonViewModel/API models. FaceRD capture and the final user-creation
 * transaction remain app-specific; onAadhaarCheckSuccess is the bridge to that existing flow.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationComposeScreen(
    commonViewModel: CommonViewModel,
    onBack: () -> Unit,
    onAadhaarCheckSuccess: (aadhaar: String, stateCode: String, stateLgdCode: String, candidateId: String) -> Unit,
    onLoginClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var step by remember { mutableStateOf(RegistrationStep.EMAIL) }
    var email by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var aadhaar by remember { mutableStateOf("") }
    var aadhaarVisible by remember { mutableStateOf(false) }
    var consent by remember { mutableStateOf(false) }
    var selectedStateName by remember { mutableStateOf("") }
    var selectedStateCode by remember { mutableStateOf("") }
    var selectedStateLgd by remember { mutableStateOf("") }
    var candidateId by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    var info by remember { mutableStateOf("") }
    var stateRows by remember { mutableStateOf<List<RegState>>(emptyList()) }

    // Existing API flows from CommonViewModel.
    LaunchedEffect(commonViewModel) {
        commonViewModel.sendEmailOTP.collect { result ->
            when (result) {
                is Resource.Loading -> loading = true
                is Resource.Error -> { loading = false; error = result.error?.message ?: "Unable to send email OTP." }
                is Resource.Success -> {
                    loading = false
                    val data = result.data
                    if (data?.responseCode == 200) { otp = ""; info = data.responseDesc; error = ""; step = RegistrationStep.EMAIL_OTP }
                    else { error = data?.responseDesc?.takeIf { it.isNotBlank() } ?: "Unable to send email OTP." }
                }
            }
        }
    }
    LaunchedEffect(commonViewModel) {
        commonViewModel.sendMobileOTP.collect { result ->
            when (result) {
                is Resource.Loading -> loading = true
                is Resource.Error -> { loading = false; error = result.error?.message ?: "Unable to send mobile OTP." }
                is Resource.Success -> {
                    loading = false
                    val data = result.data
                    if (data?.responseCode == 200) { otp = ""; info = data.responseDesc; error = ""; step = RegistrationStep.MOBILE_OTP }
                    else { error = data?.responseDesc?.takeIf { it.isNotBlank() } ?: "Unable to send mobile OTP." }
                }
            }
        }
    }
    LaunchedEffect(commonViewModel) {
        commonViewModel.getOtpValidateApi.collect { result ->
            when (result) {
                is Resource.Loading -> loading = true
                is Resource.Error -> { loading = false; error = result.error?.message ?: "OTP verification failed." }
                is Resource.Success -> {
                    loading = false
                    val data = result.data
                    if (data?.responseCode == 200) {
                        error = ""; otp = ""
                        if (step == RegistrationStep.EMAIL_OTP) step = RegistrationStep.MOBILE
                        else if (step == RegistrationStep.MOBILE_OTP) {
                            candidateId = data.candidateId.orEmpty()
                            if (candidateId.isBlank()) error = "Candidate ID was not returned by the server."
                            else {
                                commonViewModel.getOtpStateListApi(candidateId)
                                step = RegistrationStep.STATE
                            }
                        }
                    } else error = data?.responseDesc?.takeIf { it.isNotBlank() } ?: "Invalid OTP. Please try again."
                }
            }
        }
    }
    LaunchedEffect(commonViewModel) {
        commonViewModel.stateOtpList.collect { result ->
            when (result) {
                is Resource.Loading -> loading = true
                is Resource.Error -> { loading = false; error = result.error?.message ?: "Could not load states." }
                is Resource.Success -> {
                    loading = false
                    val data = result.data
                    if (data?.responseCode == 200) {
                        stateRows = data.stateList.orEmpty().map { row -> RegState(row.stateName.orEmpty(), row.stateCode.orEmpty(), row.lgdStateCode.orEmpty()) }
                        if (stateRows.isEmpty()) error = "No states were returned by the server."
                    } else error = data?.responseDesc?.takeIf { it.isNotBlank() } ?: "Could not load states."
                }
            }
        }
    }
    LaunchedEffect(commonViewModel) {
        commonViewModel.getAadhaarCheck.collect { result ->
            when (result) {
                is Resource.Loading -> loading = true
                is Resource.Error -> { loading = false; error = result.error?.message ?: "Aadhaar verification request failed." }
                is Resource.Success -> {
                    loading = false
                    val data = result.data
                    if (data?.responseCode == 200) {
                        error = ""
                        onAadhaarCheckSuccess(aadhaar, selectedStateCode, selectedStateLgd, candidateId)
                    } else error = data?.responseDesc?.takeIf { it.isNotBlank() } ?: "Aadhaar verification failed."
                }
            }
        }
    }

    fun goBackStep() {
        error = ""; info = ""
        when (step) {
            RegistrationStep.EMAIL -> onBack()
            RegistrationStep.EMAIL_OTP -> step = RegistrationStep.EMAIL
            RegistrationStep.MOBILE -> step = if (email.isBlank()) RegistrationStep.EMAIL else RegistrationStep.EMAIL_OTP
            RegistrationStep.MOBILE_OTP -> step = RegistrationStep.MOBILE
            RegistrationStep.STATE -> step = RegistrationStep.MOBILE_OTP
            RegistrationStep.AADHAAR -> step = RegistrationStep.STATE
            RegistrationStep.COMPLETE -> step = RegistrationStep.AADHAAR
        }
    }
    BackHandler { goBackStep() }

    val stepNumber = when (step) {
        RegistrationStep.EMAIL -> 1; RegistrationStep.EMAIL_OTP -> 2; RegistrationStep.MOBILE -> 3
        RegistrationStep.MOBILE_OTP -> 4; RegistrationStep.STATE -> 5; RegistrationStep.AADHAAR, RegistrationStep.COMPLETE -> 6
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = RegBg,
        topBar = {
            Column(Modifier.fillMaxWidth().background(RegBg)) {
                Spacer(Modifier.height(10.dp))
                LoginLockBadge(modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(Modifier.height(6.dp))
                LoginHeader()
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier.size(40.dp).background(Color.White, CircleShape).clickable { goBackStep() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = RegGreen)
                    }
                    Spacer(Modifier.weight(1f))
                    Text("Registration", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = RegGreen)
                    Spacer(Modifier.weight(1f))
                    Spacer(Modifier.size(40.dp))
                }
                StepProgress(stepNumber)
                Spacer(Modifier.height(6.dp))
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier.fillMaxWidth().heightIn(min = 155.dp).background(RegBg)
            ) {
                LoginRuralBackground(modifier = Modifier.align(Alignment.BottomCenter))
                Column(
                    modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(bottom = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoginFooter(version = BuildConfig.VERSION_NAME)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).imePadding().padding(horizontal = 20.dp).padding(top = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(targetState = step, transitionSpec = { fadeIn() togetherWith fadeOut() }, label = "registration-step") { current ->
                Column(
                    modifier = Modifier.fillMaxWidth().weight(1f, fill = false).padding(top = 12.dp, bottom = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (current) {
                        RegistrationStep.EMAIL -> {
                            HeroIcon { Icon(Icons.Default.Email, null, tint = RegGreen, modifier = Modifier.size(32.dp)) }
                            StepTitle("Create your account", "Enter your email address to get started.")
                            LabeledField("Email address", email, { email = it.take(120); error = "" }, "name@example.com", KeyboardType.Email, leading = { Icon(Icons.Default.Email, null, tint = RegGreen) })
                            ErrorText(error)
                            PrimaryButton("Send OTP", loading, enabled = email.trim().matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))) {
                                error = ""; info = ""
                                commonViewModel.sendEmailOTP(email.trim(), BuildConfig.VERSION_NAME, AppUtil.getAndroidId(context))
                            }
                            TextButton(onClick = { email = ""; error = ""; step = RegistrationStep.MOBILE }) { Text("Skip email", color = RegGreen, fontWeight = FontWeight.SemiBold) }
                        }
                        RegistrationStep.EMAIL_OTP -> {
                            HeroIcon { Icon(Icons.Default.Email, null, tint = RegGreen, modifier = Modifier.size(32.dp)) }
                            StepTitle("Verify your email", "Enter the 4-digit OTP sent to")
                            Text(email, color = RegGreen, fontWeight = FontWeight.SemiBold)
                            Spacer(Modifier.height(26.dp))
                            OtpBoxes(otp) { otp = it; error = "" }
                            Text("Enter the 4-digit code", color = RegMuted, fontSize = 12.sp, modifier = Modifier.padding(top = 12.dp))
                            ErrorText(error)
                            PrimaryButton("Verify email", loading, otp.length == 4) {
                                commonViewModel.getOtpValidateApi(ValidateOtpReq(BuildConfig.VERSION_NAME, email, "", AppUtil.getAndroidId(context), otp))
                            }
                            TextButton(onClick = { commonViewModel.sendEmailOTP(email, BuildConfig.VERSION_NAME, AppUtil.getAndroidId(context)) }) { Text("Resend OTP", color = RegGreen) }
                        }
                        RegistrationStep.MOBILE -> {
                            HeroIcon { Icon(Icons.Default.PhoneAndroid, null, tint = RegGreen, modifier = Modifier.size(32.dp)) }
                            StepTitle("Verify mobile number", "We’ll send a one-time password to your mobile.")
                            LabeledField("Mobile number", mobile, { mobile = it.filter(Char::isDigit).take(10); error = "" }, "10-digit mobile number", KeyboardType.Phone, leading = { Text("+91", color = RegGreen, fontWeight = FontWeight.Bold) })
                            ErrorText(error)
                            PrimaryButton("Send OTP", loading, mobile.matches(Regex("^[6-9][0-9]{9}$"))) {
                                commonViewModel.sendMobileOTP(mobile, BuildConfig.VERSION_NAME, AppUtil.getAndroidId(context))
                            }
                        }
                        RegistrationStep.MOBILE_OTP -> {
                            HeroIcon { Icon(Icons.Default.PhoneAndroid, null, tint = RegGreen, modifier = Modifier.size(32.dp)) }
                            StepTitle("Verify your mobile", "Enter the 4-digit OTP sent to")
                            Text(mobile, color = RegGreen, fontWeight = FontWeight.SemiBold)
                            Spacer(Modifier.height(26.dp))
                            OtpBoxes(otp) { otp = it; error = "" }
                            Text("Enter the 4-digit code", color = RegMuted, fontSize = 12.sp, modifier = Modifier.padding(top = 12.dp))
                            ErrorText(error)
                            PrimaryButton("Verify mobile", loading, otp.length == 4) {
                                commonViewModel.getOtpValidateApi(ValidateOtpReq(BuildConfig.VERSION_NAME, "", mobile, AppUtil.getAndroidId(context), otp))
                            }
                            TextButton(onClick = { commonViewModel.sendMobileOTP(mobile, BuildConfig.VERSION_NAME, AppUtil.getAndroidId(context)) }) { Text("Resend OTP", color = RegGreen) }
                        }
                        RegistrationStep.STATE -> {
                            HeroIcon { Icon(Icons.Default.Map, null, tint = RegGreen, modifier = Modifier.size(32.dp)) }
                            StepTitle("Select your state", "Choose the state you belong to.")
                            if (selectedStateName.isNotBlank()) {
                                Row(
                                    Modifier.fillMaxWidth().padding(top = 20.dp)
                                        .background(RegGreenLight, RoundedCornerShape(14.dp))
                                        .padding(horizontal = 16.dp, vertical = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Map, null, tint = RegGreen)
                                    Spacer(Modifier.width(10.dp))
                                    Text(selectedStateName, modifier = Modifier.weight(1f), color = RegGreen, fontWeight = FontWeight.SemiBold)
                                    Icon(Icons.Default.CheckCircle, null, tint = RegGreen)
                                }
                            }
                            Spacer(Modifier.height(18.dp))
                            if (stateRows.isEmpty()) {
                                if (loading) CircularProgressIndicator(color = RegGreen, modifier = Modifier.size(24.dp))
                                else Text("State list is not available.", color = RegMuted, fontSize = 13.sp)
                            } else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxWidth().heightIn(max = 310.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(stateRows) { row ->
                                        val isSelected = row.code == selectedStateCode
                                        Row(
                                            Modifier.fillMaxWidth()
                                                .background(Color.White, RoundedCornerShape(14.dp))
                                                .clickable {
                                                    selectedStateName = row.name
                                                    selectedStateCode = row.code
                                                    selectedStateLgd = row.lgd
                                                    error = ""
                                                }
                                                .padding(horizontal = 16.dp, vertical = 15.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                Icons.Default.Map,
                                                null,
                                                tint = if (isSelected) RegGreen else RegMuted,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(Modifier.width(14.dp))
                                            Text(row.name, modifier = Modifier.weight(1f), color = RegText, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                                            if (isSelected) Icon(Icons.Default.CheckCircle, null, tint = RegGreen)
                                        }
                                        androidx.compose.foundation.Canvas(Modifier.fillMaxWidth().height(1.dp)) {
                                            drawLine(RegBorder, androidx.compose.ui.geometry.Offset(0f, 0f), androidx.compose.ui.geometry.Offset(size.width, 0f), 1.dp.toPx())
                                        }
                                    }
                                }
                            }
                            ErrorText(error)
                            PrimaryButton("Continue", loading, selectedStateCode.isNotBlank() && selectedStateLgd.isNotBlank()) {
                                step = RegistrationStep.AADHAAR
                                aadhaar = ""
                                consent = false
                                error = ""
                            }
                        }
                        RegistrationStep.AADHAAR -> {
                            HeroIcon { Icon(Icons.Default.VerifiedUser, null, tint = RegGreen, modifier = Modifier.size(34.dp)) }
                            StepTitle("Aadhaar verification", "Verify your identity using Aadhaar-based face authentication.")
                            Spacer(Modifier.height(22.dp))
                            LabeledField("Aadhaar number", aadhaar, { aadhaar = it.filter(Char::isDigit).take(12); error = "" }, "Enter 12-digit Aadhaar", KeyboardType.Number, leading = { Icon(Icons.Default.Person, null, tint = RegGreen) }, visual = if (aadhaarVisible) VisualTransformation.None else PasswordVisualTransformation(), trailing = { TextButton(onClick = { aadhaarVisible = !aadhaarVisible }) { Text(if (aadhaarVisible) "Hide" else "Show", color = RegGreen) } })
                            if (aadhaar.length == 12) Text("Aadhaar number entered", color = RegGreen, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start).padding(top = 6.dp))
                            Spacer(Modifier.height(16.dp))
                            Card(colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, RegBorder), shape = RoundedCornerShape(14.dp)) {
                                Column(Modifier.padding(14.dp)) {
                                    Text("Consent for Aadhaar verification", fontWeight = FontWeight.SemiBold, color = RegGreen)
                                    Spacer(Modifier.height(6.dp))
                                    Text("I voluntarily consent to use my Aadhaar details for identity verification and registration. My details will be processed for this purpose.", fontSize = 12.sp, color = RegMuted, lineHeight = 17.sp)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Checkbox(checked = consent, onCheckedChange = { consent = it }, colors = CheckboxDefaults.colors(checkedColor = RegGreen))
                                        Text("I have read and agree to the consent above", fontSize = 12.sp, color = RegText)
                                    }
                                }
                            }
                            ErrorText(error)
                            PrimaryButton("Verify Aadhaar", loading, aadhaar.length == 12 && consent && selectedStateCode.isNotBlank() && candidateId.isNotBlank()) {
                                try {
                                    val encrypted = AESCryptography.encryptIntoBase64String(aadhaar, AppConstant.Constants.ENCRYPT_KEY, AppConstant.Constants.ENCRYPT_IV_KEY)
                                    commonViewModel.getAadhaarCheck(AadhaarCheckReq(BuildConfig.VERSION_NAME, encrypted))
                                } catch (e: Exception) { error = e.message ?: "Unable to prepare Aadhaar verification." }
                            }
                            Text("Your Aadhaar number is masked on screen", color = RegMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 10.dp))
                        }
                        RegistrationStep.COMPLETE -> {
                            HeroIcon { Icon(Icons.Default.CheckCircle, null, tint = RegGreen, modifier = Modifier.size(36.dp)) }
                            StepTitle("Registration complete", "Your registration details have been submitted successfully.")
                        }
                    }
                }
            }
            if (step == RegistrationStep.EMAIL) {
                Spacer(Modifier.height(8.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).clickable { onLoginClick() },
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, RegBorder),
                    shadowElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 13.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Already have an account? ", color = RegMuted, fontSize = 14.sp)
                        Text("Login", color = RegGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(5.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Login", tint = RegGreen, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }


}

private enum class RegistrationStep { EMAIL, EMAIL_OTP, MOBILE, MOBILE_OTP, STATE, AADHAAR, COMPLETE }

private data class RegState(val name: String, val code: String, val lgd: String)

@Composable
private fun StepProgress(current: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 42.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (step in 1..6) {
            Box(
                modifier = Modifier.size(38.dp).background(if (step == current) RegGreen else Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.Canvas(Modifier.fillMaxSize()) {
                    drawCircle(
                        color = if (step == current) RegGreen else RegBorder,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx())
                    )
                }
                Text(
                    step.toString(),
                    fontSize = 16.sp,
                    fontWeight = if (step == current) FontWeight.Bold else FontWeight.Normal,
                    color = if (step == current) Color.White else RegMuted
                )
            }
            if (step < 6) {
                Box(Modifier.weight(1f).padding(horizontal = 6.dp).height(1.dp).background(RegBorder))
            }
        }
    }
}

@Composable
private fun HeroIcon(content: @Composable () -> Unit) {
    Box(Modifier.padding(top = 30.dp).size(48.dp), contentAlignment = Alignment.Center) { content() }
}

@Composable
private fun StepTitle(title: String, subtitle: String) {
    Text(title, modifier = Modifier.fillMaxWidth().padding(top = 18.dp), fontSize = 26.sp, fontWeight = FontWeight.Bold, color = RegGreen, textAlign = TextAlign.Center, lineHeight = 30.sp)
    Text(subtitle, modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 22.dp), fontSize = 14.sp, color = RegMuted, textAlign = TextAlign.Center, lineHeight = 20.sp)
}

@Composable
private fun LabeledField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType,
    leading: @Composable (() -> Unit)? = null,
    visual: VisualTransformation = VisualTransformation.None,
    trailing: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(60.dp).background(Color.White, RoundedCornerShape(14.dp))
            .padding(horizontal = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(44.dp).background(RegGreenLight, RoundedCornerShape(9.dp)), contentAlignment = Alignment.Center) {
            if (leading != null) leading()
            else Icon(Icons.Default.Person, null, tint = RegGreen, modifier = Modifier.size(19.dp))
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Medium, color = RegMuted)
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = TextStyle(fontSize = 13.sp, color = RegText),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = ImeAction.Done),
                visualTransformation = visual,
                decorationBox = { innerTextField ->
                    Box {
                        if (value.isEmpty()) Text(placeholder, fontSize = 13.sp, color = RegMuted)
                        innerTextField()
                    }
                }
            )
        }
        if (trailing != null) {
            Spacer(Modifier.width(4.dp))
            trailing()
        }
    }
    Spacer(Modifier.height(2.dp))
    Box(Modifier.fillMaxWidth().height(1.dp).background(RegBorder))
}

@Composable
private fun OtpBoxes(otp: String, onOtpChange: (String) -> Unit) {
    BasicTextField(
        value = otp,
        onValueChange = { onOtpChange(it.filter(Char::isDigit).take(4)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Done),
        singleLine = true,
        textStyle = TextStyle(color = Color.Transparent, fontSize = 1.sp),
        modifier = Modifier.fillMaxWidth().height(62.dp),
        decorationBox = {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                repeat(4) { index ->
                    val char = otp.getOrNull(index)?.toString().orEmpty()
                    Box(
                        Modifier.weight(1f).height(62.dp).background(Color.White, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.foundation.Canvas(Modifier.fillMaxSize()) {
                            drawRoundRect(
                                color = if (index == minOf(otp.length, 3)) RegGreen else RegBorder,
                                style = androidx.compose.ui.graphics.drawscope.Stroke(
                                    width = if (index == minOf(otp.length, 3)) 1.5.dp.toPx() else 1.dp.toPx()
                                ),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(14.dp.toPx())
                            )
                        }
                        Text(char, fontSize = 26.sp, fontWeight = FontWeight.Medium, color = RegGreen)
                    }
                }
            }
        }
    )
}

@Composable
private fun ErrorText(message: String) {
    if (message.isNotBlank()) Text(message, modifier = Modifier.fillMaxWidth().padding(top = 12.dp), color = Color(0xFFB3261E), fontSize = 13.sp, lineHeight = 18.sp)
}

@Composable
private fun PrimaryButton(title: String, loading: Boolean, enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled && !loading,
        modifier = Modifier.fillMaxWidth().padding(top = 24.dp).height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = RegGreen,
            disabledContainerColor = RegGreen.copy(alpha = 0.45f)
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.size(22.dp), strokeWidth = 2.dp, color = Color.White)
        } else {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(title, fontWeight = FontWeight.SemiBold, fontSize = 17.sp, color = Color.White)
                Spacer(Modifier.weight(1f))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        }
    }
}
