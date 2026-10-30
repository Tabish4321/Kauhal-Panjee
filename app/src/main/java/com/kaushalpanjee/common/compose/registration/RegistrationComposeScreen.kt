package com.kaushalpanjee.common.compose.registration

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.R
import com.kaushalpanjee.common.CommonViewModel
import com.kaushalpanjee.common.RegisterFragmentDirections
import com.kaushalpanjee.common.compose.login.LoginColors
import com.kaushalpanjee.common.compose.login.LoginFooter
import com.kaushalpanjee.common.compose.login.LoginLockBadge
import com.kaushalpanjee.common.compose.login.LoginRuralBackground
import com.kaushalpanjee.common.model.request.AadhaarCheckReq
import com.kaushalpanjee.common.model.request.ValidateOtpReq
import com.kaushalpanjee.core.util.AESCryptography
import com.kaushalpanjee.core.util.AppConstant
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.Resource
import kotlinx.coroutines.delay

private val RegButtonGradient = listOf(LoginColors.Primary, LoginColors.Primary, LoginColors.DarkGreen)

private val ScreenPadding = 20.dp

private enum class RegistrationStep { WELCOME, EMAIL, EMAIL_OTP, MOBILE, MOBILE_OTP, AADHAAR, COMPLETE }

private data class RegState(val name: String, val code: String, val lgd: String)

private const val RESEND_SECONDS = 30

/** All sizes live here so the screen scales down on small phones and fits on one screen. */
private class RegDims(private val compact: Boolean, private val veryCompact: Boolean) {
    private fun pick(vc: Int, c: Int, n: Int) = if (veryCompact) vc else if (compact) c else n

    // Compact sizing keeps the welcome screen comfortably visible on common phone heights.
    val headerTop = pick(6, 10, 14).dp
    val brandSp = pick(19, 21, 23).sp
    val tagSp = pick(10, 11, 12).sp
    val panelGap = pick(30, 34, 38).dp
    val panelTopPad = pick(18, 22, 26).dp
    val panelSidePad = pick(14, 16, 18).dp

    val titleSp = pick(18, 19, 21).sp
    val titleLine = pick(22, 24, 27).sp
    val subSp = pick(11, 12, 13).sp
    val subLine = pick(14, 16, 18).sp
    val titleBottom = pick(5, 7, 9).dp
    val accentGap = pick(4, 5, 7).dp
    val showAccent = true

    val inputH = pick(46, 50, 54).dp
    val inputIconBox = pick(32, 34, 36).dp
    val inputSp = pick(14, 15, 16).sp

    val btnH = pick(43, 46, 49).dp
    val btnTop = pick(6, 8, 10).dp
    val btnSp = pick(15, 16, 17).sp

    val featCircle = pick(28, 31, 34).dp
    val featIcon = pick(15, 17, 18).dp
    val featSp = pick(12, 13, 14).sp
    val featPadV = pick(1, 2, 3).dp

    val stepCircle = pick(24, 27, 30).dp
    val stepGap = pick(8, 10, 14).dp
    val stepLabelSp = pick(9, 10, 10).sp

    val otpH = pick(46, 50, 54).dp
    val otpSp = pick(20, 22, 23).sp

    val loginIcon = pick(27, 29, 31).dp
    val loginPadV = pick(5, 6, 7).dp
    val sectionGap = pick(4, 6, 8).dp

    /**
     * Height of the rural artwork area that overlaps the bottom of the white panel.
     * The panel content gets this much bottom padding, so the "Already have an account?"
     * row always stays ABOVE the image. Tune if your artwork is taller/shorter.
     */
    val bottomOverlap = pick(58, 66, 74).dp
}

/**
 * @param onLanguageClick pass the SAME handler used on the Login screen's language icon.
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationComposeScreen(
    commonViewModel: CommonViewModel,
    onBack: () -> Unit,
    onAadhaarCheckSuccess: (aadhaar: String, stateCode: String, stateLgdCode: String, candidateId: String) -> Unit,
    onLoginClick: () -> Unit = {},
    onLanguageClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    onWelcomeStateChanged: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current
    val view = LocalView.current
    val version = BuildConfig.VERSION_NAME

    // Keep the status bar hidden by default. If the user swipes it down,
    // let it remain visible briefly and hide it again after 5 seconds.
    DisposableEffect(view) {
        val activity = context.findActivity()
        val window = activity?.window
        val controller = window?.let { WindowInsetsControllerCompat(it, view) }
        val handler = Handler(Looper.getMainLooper())
        var hideAt = 0L
        val statusBars = WindowInsetsCompat.Type.statusBars()

        controller?.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        controller?.hide(statusBars)

        val hideStatusBarRunnable = object : Runnable {
            override fun run() {
                val rootInsets = view.rootWindowInsets
                val visible = rootInsets?.let {
                    WindowInsetsCompat.toWindowInsetsCompat(it, view).isVisible(statusBars)
                } ?: false

                if (visible) {
                    if (hideAt == 0L) hideAt = android.os.SystemClock.uptimeMillis() + 5_000L
                    if (android.os.SystemClock.uptimeMillis() >= hideAt) {
                        controller?.hide(statusBars)
                        hideAt = 0L
                    }
                } else {
                    hideAt = 0L
                }
                handler.postDelayed(this, 250L)
            }
        }
        handler.post(hideStatusBarRunnable)

        onDispose {
            handler.removeCallbacks(hideStatusBarRunnable)
            // Restore normal system UI when leaving this screen.
            controller?.show(statusBars)
        }
    }

    var step by remember { mutableStateOf(RegistrationStep.WELCOME) }
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
    // Prevents stale/replayed Loading emissions from showing a spinner when the screen opens.
    var activeRequest by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    var info by remember { mutableStateOf("") }
    var stateRows by remember { mutableStateOf<List<RegState>>(emptyList()) }
    var stateDropdownExpanded by remember { mutableStateOf(false) }

    var resendSeconds by remember { mutableIntStateOf(0) }
    var resendTick by remember { mutableIntStateOf(0) }

    val aadhaarDrawableId = remember(context) {
        listOf("aadhaar", "aadhar", "aadhaar_logo", "aadhar_logo", "ic_aadhaar", "ic_aadhar", "aadhaar_icon", "aadhar_icon")
            .map { name -> context.resources.getIdentifier(name, "drawable", context.packageName) }
            .firstOrNull { it != 0 } ?: 0
    }

    LaunchedEffect(step, resendTick) {
        if (step == RegistrationStep.EMAIL_OTP || step == RegistrationStep.MOBILE_OTP) {
            resendSeconds = RESEND_SECONDS
            while (resendSeconds > 0) {
                delay(1000)
                resendSeconds--
            }
        }
    }

    // ---------- Existing API flows from CommonViewModel ----------
    LaunchedEffect(commonViewModel) {
        commonViewModel.sendEmailOTP.collect { result ->
            when (result) {
                is Resource.Loading -> if (activeRequest) loading = true
                is Resource.Error -> { loading = false; activeRequest = false; error = result.error?.message ?: "Unable to send email OTP." }
                is Resource.Success -> {
                    loading = false
                    activeRequest = false
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
                is Resource.Loading -> if (activeRequest) loading = true
                is Resource.Error -> { loading = false; activeRequest = false; error = result.error?.message ?: "Unable to send mobile OTP." }
                is Resource.Success -> {
                    loading = false
                    activeRequest = false
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
                is Resource.Loading -> if (activeRequest) loading = true
                is Resource.Error -> { loading = false; activeRequest = false; error = result.error?.message ?: "OTP verification failed." }
                is Resource.Success -> {
                    loading = false
                    activeRequest = false
                    val data = result.data
                    if (data?.responseCode == 200) {
                        error = ""; otp = ""
                        if (step == RegistrationStep.EMAIL_OTP) step = RegistrationStep.MOBILE
                        else if (step == RegistrationStep.MOBILE_OTP) {
                            candidateId = data.candidateId.orEmpty()
                            if (candidateId.isBlank()) error = "Candidate ID was not returned by the server."
                            else {
                                activeRequest = true
                                loading = true
                                commonViewModel.getOtpStateListApi(candidateId)
                                step = RegistrationStep.AADHAAR
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
                is Resource.Loading -> if (activeRequest) loading = true
                is Resource.Error -> { loading = false; activeRequest = false; error = result.error?.message ?: "Could not load states." }
                is Resource.Success -> {
                    loading = false
                    activeRequest = false
                    val data = result.data
                    if (data?.responseCode == 200) {
                        stateRows = data.stateList.orEmpty().map { row ->
                            RegState(row.stateName.orEmpty(), row.stateCode.orEmpty(), row.lgdStateCode.orEmpty())
                        }
                        if (stateRows.isEmpty()) error = "No states were returned by the server."
                    } else error = data?.responseDesc?.takeIf { it.isNotBlank() } ?: "Could not load states."
                }
            }
        }
    }
    LaunchedEffect(commonViewModel) {
        commonViewModel.getAadhaarCheck.collect { result ->

            Log.d("AADHAAR_VERIFY", "API response received")

            when (result) {
                is Resource.Loading -> if (activeRequest) loading = true
                is Resource.Error -> { Log.e("AADHAAR_VERIFY", "API failed: ${error}") }
                is Resource.Success -> {
                    loading = false
                    activeRequest = false
                    val data = result.data
                    if (data?.responseCode == 200) {
                        error = ""
                        onAadhaarCheckSuccess(aadhaar, selectedStateCode, selectedStateLgd, candidateId)
                    } else error = data?.responseDesc?.takeIf { it.isNotBlank() } ?: "Aadhaar verification failed."
                }
            }
        }
    }

    // Fragment handles double-back on Welcome; Compose handles step navigation elsewhere.
    LaunchedEffect(step) {
        onWelcomeStateChanged(step == RegistrationStep.WELCOME)
    }

    // ---------- Navigation ----------
    fun goBackStep() {
        error = ""; info = ""
        when (step) {
            RegistrationStep.WELCOME -> onBack()
            RegistrationStep.EMAIL -> step = RegistrationStep.WELCOME
            RegistrationStep.EMAIL_OTP -> step = RegistrationStep.EMAIL
            RegistrationStep.MOBILE -> step = if (email.isBlank()) RegistrationStep.EMAIL else RegistrationStep.EMAIL_OTP
            RegistrationStep.MOBILE_OTP -> step = RegistrationStep.MOBILE
            RegistrationStep.AADHAAR -> step = RegistrationStep.MOBILE
            RegistrationStep.COMPLETE -> step = RegistrationStep.AADHAAR
        }
    }
    BackHandler(enabled = step != RegistrationStep.WELCOME) { goBackStep() }

    // COMPLETE = 5 so every circle shows a check.
    val stepNumber = when (step) {
        RegistrationStep.WELCOME -> 0
        RegistrationStep.EMAIL, RegistrationStep.EMAIL_OTP -> 1
        RegistrationStep.MOBILE, RegistrationStep.MOBILE_OTP -> 2
        RegistrationStep.AADHAAR -> 3
        RegistrationStep.COMPLETE -> 5
    }

    fun sendEmailOtp() {
        activeRequest = true; loading = true; error = ""; info = ""
        commonViewModel.sendEmailOTP(email.trim(), BuildConfig.VERSION_NAME, AppUtil.getAndroidId(context))
    }

    fun sendMobileOtp() {
        activeRequest = true; loading = true; error = ""; info = ""
        commonViewModel.sendMobileOTP(mobile, BuildConfig.VERSION_NAME, AppUtil.getAndroidId(context))
    }

    val imeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

    // ---------- UI ----------
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(LoginColors.Background)
    ) {
        val veryCompact = maxHeight < 640.dp
        val compact = maxHeight < 740.dp
        val d = remember(compact, veryCompact) { RegDims(compact, veryCompact) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                // Fixed top spacing prevents layout jumping when the status bar appears.
                .imePadding()
                .padding(horizontal = 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(d.headerTop + 8.dp))

            // Language icon at top-right (same as Login)
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp) .padding(top = 0.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onLanguageClick, modifier = Modifier.size(42.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.languagen),
                        contentDescription = "Select Language",
                        modifier = Modifier.size(29.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // DDU-GKY and RSETI logos — same drawable resources and proportions as LoginHeader.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_ddgky),
                    contentDescription = stringResource(R.string.login_ddugky_logo),
                    modifier = Modifier.width(if (veryCompact) 94.dp else 112.dp)
                        .height(if (veryCompact) 50.dp else 61.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(Modifier.width(10.dp))

                Box(
                    Modifier
                        .width(1.dp)
                        .height(38.dp)
                        .background(LoginColors.Border)
                )

                Spacer(Modifier.width(10.dp))

                Image(
                    painter = painterResource(id = R.drawable.ic_rseti),
                    contentDescription = stringResource(R.string.login_rseti_logo),
                    modifier = Modifier.width(if (veryCompact) 94.dp else 112.dp)
                        .height(if (veryCompact) 50.dp else 61.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_icon),
                    contentDescription = "Kaushal Panjee logo",
                    modifier = Modifier.size(32.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Kaushal Panjee",
                    fontSize = d.brandSp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LoginColors.DarkGreen,
                    letterSpacing = 0.3.sp,
                    textAlign = TextAlign.Center
                )
            }
            Row(
                modifier = Modifier.padding(top = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.width(22.dp).height(1.dp).background(Brush.horizontalGradient(listOf(Color.Transparent, LoginColors.Primary))))
                Text(
                    "Empowering Rural India",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    fontSize = d.tagSp,
                    color = LoginColors.TextPrimary.copy(alpha = 0.8f),
                    letterSpacing = 0.5.sp
                )
                Box(Modifier.width(22.dp).height(1.dp).background(Brush.horizontalGradient(listOf(LoginColors.Primary, Color.Transparent))))
            }

            // ===== Full-height panel (runs to the very bottom): only TOP corners rounded =====
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = d.panelGap)
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Brush.verticalGradient(listOf(Color.White, Color.White, Color(0xFFF1F9F5))))
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                                .padding(
                                    start = d.panelSidePad,
                                    end = d.panelSidePad,
                                    top = d.panelTopPad,
                                    // Reserve room for the fixed footer artwork. Login stays above it
                                    // and remains part of the scrollable content.
                                    bottom = if (imeVisible || step == RegistrationStep.AADHAAR) 8.dp else d.bottomOverlap
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (step != RegistrationStep.WELCOME) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        modifier = Modifier.size(36.dp).clickable(enabled = !loading) { goBackStep() },
                                        shape = CircleShape,
                                        color = LoginColors.LightGreen,
                                        border = BorderStroke(1.dp, LoginColors.Border)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                                contentDescription = "Go back",
                                                tint = LoginColors.DarkGreen,
                                                modifier = Modifier.size(19.dp)
                                            )
                                        }
                                    }
                                    Spacer(Modifier.width(10.dp))
                                    Text(
                                        text = when (step) {
                                            RegistrationStep.EMAIL -> "Email verification"
                                            RegistrationStep.EMAIL_OTP -> "Verify email"
                                            RegistrationStep.MOBILE -> "Mobile verification"
                                            RegistrationStep.MOBILE_OTP -> "Verify mobile"
                                            RegistrationStep.AADHAAR -> "Identity verification"
                                            RegistrationStep.COMPLETE -> "Registration status"
                                            RegistrationStep.WELCOME -> ""
                                        },
                                        color = LoginColors.TextSecondary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                StepProgress(stepNumber, d)
                                Spacer(Modifier.height(d.stepGap))
                            }

                            AnimatedContent(
                                targetState = step,
                                transitionSpec = {
                                    if (targetState.ordinal >= initialState.ordinal) {
                                        (slideInHorizontally { w -> w / 6 } + fadeIn()) togetherWith
                                                (slideOutHorizontally { w -> -w / 6 } + fadeOut())
                                    } else {
                                        (slideInHorizontally { w -> -w / 6 } + fadeIn()) togetherWith
                                                (slideOutHorizontally { w -> w / 6 } + fadeOut())
                                    }
                                },
                                label = "registration-step"
                            ) { current ->
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    when (current) {
                                        RegistrationStep.WELCOME -> {
                                            StepTitle("Create Your Account", "Join Kaushal Panjee and take the\nnext step towards your future", d)
                                            WelcomeFeature(Icons.Default.Person, "Secure & Easy Registration", d)
                                            WelcomeFeature(Icons.Default.Description, "Access to Government Schemes", d)
                                            WelcomeFeature(Icons.Default.VerifiedUser, "Build Your Digital Identity", d)
                                            WelcomeFeature(Icons.Default.BarChart, "Track Your Progress", d)
                                            PrimaryButton("Get Started", loading, true, d) {
                                                error = ""; info = ""
                                                step = RegistrationStep.EMAIL
                                            }
                                        }

                                        RegistrationStep.EMAIL -> {
                                            StepTitle("Enter Your Email Address", "We’ll send a verification code\nto your email", d)
                                            InputField(
                                                label = "Email Address",
                                                value = email,
                                                onValueChange = { email = it.take(120); error = "" },
                                                placeholder = "example@domain.com",
                                                keyboardType = KeyboardType.Email,
                                                d = d,
                                                leading = { Icon(Icons.Default.Email, null, tint = LoginColors.DarkGreen, modifier = Modifier.size(22.dp)) }
                                            )
                                            ErrorText(error)
                                            PrimaryButton(
                                                "Send OTP", loading,
                                                enabled = email.trim().matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")),
                                                d = d
                                            ) { sendEmailOtp() }
                                            TextButton(
                                                onClick = { email = ""; error = ""; step = RegistrationStep.MOBILE },
                                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                                            ) {
                                                Text("Skip email", color = LoginColors.Primary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                            }
                                        }

                                        RegistrationStep.EMAIL_OTP -> {
                                            StepTitle("Enter OTP", "We’ve sent a 4-digit code to", d, bottomPad = 4.dp)
                                            Text(email, color = LoginColors.DarkGreen, fontWeight = FontWeight.Bold, fontSize = 15.sp, textAlign = TextAlign.Center)
                                            Spacer(Modifier.height(d.titleBottom))
                                            OtpBoxes(otp, d) { otp = it; error = "" }
                                            ResendRow(resendSeconds) { resendTick++; sendEmailOtp() }
                                            ErrorText(error)
                                            PrimaryButton("Verify & Continue", loading, otp.length == 4, d) {
                                                activeRequest = true; loading = true
                                                commonViewModel.getOtpValidateApi(
                                                    ValidateOtpReq(BuildConfig.VERSION_NAME, email, "", AppUtil.getAndroidId(context), otp)
                                                )
                                            }
                                        }

                                        RegistrationStep.MOBILE -> {
                                            StepTitle("Enter Your Mobile Number", "We’ll send a verification code\nto your mobile", d)
                                            InputField(
                                                label = "Mobile Number",
                                                value = mobile,
                                                onValueChange = { mobile = it.filter(Char::isDigit).take(10); error = "" },
                                                placeholder = "10-digit mobile number",
                                                keyboardType = KeyboardType.Phone,
                                                d = d,
                                                prefix = "+91",
                                                leading = { Icon(Icons.Default.PhoneAndroid, null, tint = LoginColors.DarkGreen, modifier = Modifier.size(22.dp)) }
                                            )
                                            ErrorText(error)
                                            PrimaryButton("Send OTP", loading, mobile.matches(Regex("^[6-9][0-9]{9}$")), d) { sendMobileOtp() }
                                        }

                                        RegistrationStep.MOBILE_OTP -> {
                                            StepTitle("Enter OTP", "We’ve sent a 4-digit code to", d, bottomPad = 4.dp)
                                            Text("+91 $mobile", color = LoginColors.DarkGreen, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                            Spacer(Modifier.height(d.titleBottom))
                                            OtpBoxes(otp, d) { otp = it; error = "" }
                                            ResendRow(resendSeconds) { resendTick++; sendMobileOtp() }
                                            ErrorText(error)
                                            PrimaryButton("Verify & Continue", loading, otp.length == 4, d) {
                                                activeRequest = true; loading = true
                                                commonViewModel.getOtpValidateApi(
                                                    ValidateOtpReq(BuildConfig.VERSION_NAME, "", mobile, AppUtil.getAndroidId(context), otp)
                                                )
                                            }
                                        }

                                        RegistrationStep.AADHAAR -> {
                                            StepTitle("Aadhaar Verification", "Verify your identity using Aadhaar-based\nface authentication", d)
                                            InputField(
                                                label = "Aadhaar Number",
                                                value = aadhaar,
                                                onValueChange = { aadhaar = it.filter(Char::isDigit).take(12); error = "" },
                                                placeholder = "Enter 12-digit Aadhaar",
                                                keyboardType = KeyboardType.Number,
                                                d = d,
                                                leading = {
                                                    if (aadhaarDrawableId != 0) {
                                                        Image(
                                                            painter = painterResource(aadhaarDrawableId),
                                                            contentDescription = "Aadhaar",
                                                            modifier = Modifier.size(24.dp),
                                                            contentScale = ContentScale.Fit
                                                        )
                                                    } else {
                                                        Icon(Icons.Default.VerifiedUser, contentDescription = "Aadhaar", tint = LoginColors.DarkGreen, modifier = Modifier.size(22.dp))
                                                    }
                                                },
                                                visual = if (aadhaarVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                                trailing = {
                                                    TextButton(onClick = { aadhaarVisible = !aadhaarVisible }, contentPadding = PaddingValues(horizontal = 6.dp)) {
                                                        Text(if (aadhaarVisible) "Hide" else "Show", color = LoginColors.Primary, fontSize = 12.sp)
                                                    }
                                                }
                                            )
                                            if (aadhaar.length == 12) {
                                                val valid = AadhaarValidator.isValidAadhaar(aadhaar)
                                                Text(
                                                    text = if (valid) "Aadhaar format is valid" else "Invalid Aadhaar Number",
                                                    color = if (valid) LoginColors.Primary else LoginColors.Error,
                                                    fontSize = 11.sp,
                                                    modifier = Modifier.align(Alignment.Start).padding(top = 4.dp, start = 4.dp)
                                                )
                                            }

                                            Spacer(Modifier.height(10.dp))
                                            Text(
                                                "Select State",
                                                modifier = Modifier.fillMaxWidth().padding(start = 2.dp),
                                                color = LoginColors.TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold
                                            )
                                            Spacer(Modifier.height(5.dp))
                                            Box(modifier = Modifier.fillMaxWidth()) {
                                                OutlinedButton(
                                                    onClick = { stateDropdownExpanded = true },
                                                    modifier = Modifier.fillMaxWidth().height(d.inputH - 8.dp),
                                                    shape = RoundedCornerShape(14.dp),
                                                    border = BorderStroke(1.dp, LoginColors.Border),
                                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = LoginColors.TextPrimary),
                                                    contentPadding = PaddingValues(horizontal = 12.dp)
                                                ) {
                                                    Icon(Icons.Default.Map, contentDescription = null, tint = LoginColors.Primary, modifier = Modifier.size(20.dp))
                                                    Spacer(Modifier.width(7.dp))
                                                    Text(
                                                        selectedStateName.ifBlank { "Choose your state" },
                                                        modifier = Modifier.weight(1f),
                                                        textAlign = TextAlign.Start,
                                                        fontSize = 14.sp,
                                                        color = if (selectedStateName.isBlank()) LoginColors.TextSecondary else LoginColors.TextPrimary
                                                    )
                                                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Open state list", tint = LoginColors.Primary)
                                                }
                                                DropdownMenu(
                                                    expanded = stateDropdownExpanded,
                                                    onDismissRequest = { stateDropdownExpanded = false },
                                                    modifier = Modifier.fillMaxWidth(0.82f).heightIn(max = 280.dp).background(Color.White)
                                                ) {
                                                    if (stateRows.isEmpty()) {
                                                        DropdownMenuItem(
                                                            text = { Text(if (loading) "Loading states…" else "State list unavailable") },
                                                            onClick = { stateDropdownExpanded = false },
                                                            enabled = false
                                                        )
                                                    } else {
                                                        stateRows.forEach { row ->
                                                            DropdownMenuItem(
                                                                text = { Text(row.name, color = if (row.code == selectedStateCode) LoginColors.Primary else LoginColors.TextPrimary) },
                                                                trailingIcon = {
                                                                    if (row.code == selectedStateCode) Icon(Icons.Default.CheckCircle, null, tint = LoginColors.Primary)
                                                                },
                                                                onClick = {
                                                                    selectedStateName = row.name
                                                                    selectedStateCode = row.code
                                                                    selectedStateLgd = row.lgd
                                                                    stateDropdownExpanded = false
                                                                    error = ""
                                                                }
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                            if (stateRows.isEmpty() && !loading) {
                                                TextButton(
                                                    onClick = {
                                                        if (candidateId.isNotBlank()) {
                                                            activeRequest = true
                                                            loading = true
                                                            commonViewModel.getOtpStateListApi(candidateId)
                                                        }
                                                    },
                                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                                                ) { Text("Retry loading states", color = LoginColors.Primary, fontSize = 13.sp) }
                                            }

                                            Spacer(Modifier.height(10.dp))
                                            Card(
                                                modifier = Modifier.fillMaxWidth(),
                                                colors = CardDefaults.cardColors(containerColor = LoginColors.Background),
                                                border = BorderStroke(1.dp, LoginColors.Border),
                                                shape = RoundedCornerShape(14.dp)
                                            ) {
                                                Column(Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                                                    Text("Consent for Aadhaar verification", fontWeight = FontWeight.SemiBold, color = LoginColors.Primary, fontSize = 12.sp)
                                                    Spacer(Modifier.height(3.dp))
                                                    Text(
                                                        "I voluntarily consent to use my Aadhaar details for identity verification and registration. My details will be processed for this purpose.",
                                                        fontSize = 10.sp, color = LoginColors.TextSecondary, lineHeight = 14.sp
                                                    )
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Checkbox(
                                                            checked = consent,
                                                            onCheckedChange = { consent = it },
                                                            colors = CheckboxDefaults.colors(checkedColor = LoginColors.TextPrimary),
                                                            modifier = Modifier.size(36.dp)
                                                        )
                                                        Text("I have read and agree to the consent above", fontSize = 11.sp, color = LoginColors.Primary)
                                                    }
                                                }
                                            }
                                            ErrorText(error)
                                            PrimaryButton(
                                                "Verify Aadhaar", loading,
                                                AadhaarValidator.isValidAadhaar(aadhaar) && consent && selectedStateCode.isNotBlank() && candidateId.isNotBlank(),
                                                d
                                            ) {
                                                if (!AadhaarValidator.isValidAadhaar(aadhaar)) { error = "Invalid Aadhaar Number"; return@PrimaryButton }
                                                activeRequest = true
                                                loading = true
                                                try {
                                                    val encrypted = AESCryptography.encryptIntoBase64String(
                                                        aadhaar, AppConstant.Constants.ENCRYPT_KEY, AppConstant.Constants.ENCRYPT_IV_KEY
                                                    )
                                                    commonViewModel.getAadhaarCheck(AadhaarCheckReq(BuildConfig.VERSION_NAME, encrypted))

                                                    Log.d("AADHAAR_VERIFY", "Verify Aadhaar API started")

                                                } catch (e: Exception) {
                                                    activeRequest = false
                                                    loading = false
                                                    error = e.message ?: "Unable to prepare Aadhaar verification."
                                                }
                                            }
                                            Text(
                                                "Your Aadhaar number is masked on screen",
                                                color = LoginColors.TextSecondary, fontSize = 10.sp, modifier = Modifier.padding(top = 8.dp)
                                            )
                                        }

                                        RegistrationStep.COMPLETE -> {
                                            Icon(Icons.Default.CheckCircle, null, tint = LoginColors.Primary, modifier = Modifier.size(52.dp))
                                            Spacer(Modifier.height(8.dp))
                                            StepTitle("Registration Complete", "Your registration details have been\nsubmitted successfully.", d)
                                        }
                                    }
                                }
                            }

                            // Login card remains in the scrollable panel, above the fixed footer artwork.
                            if (step != RegistrationStep.AADHAAR && step != RegistrationStep.COMPLETE) {
                                Spacer(Modifier.height(d.sectionGap))
                                OrDivider()
                                Spacer(Modifier.height(d.sectionGap - 2.dp))
                                //  LoginRow(enabled = !loading, d = d, onClick = onLoginClick)

                                LoginRow(
                                    enabled = !loading,
                                    d = d,
                                    onClick = onLoginClick
                                )
                            }

                        }
                    }
                }

                LoginLockBadge(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = (-35).dp)
                )
            }
        }

        // Footer artwork is fixed to the screen bottom; it never enters the ScrollView.
        // Hide it while the keyboard is open so it cannot cover form fields.
        if (!imeVisible && step != RegistrationStep.AADHAAR) {
            LoginRuralBackground(Modifier.align(Alignment.BottomCenter).fillMaxWidth())
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = if (veryCompact) 2.dp else 5.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.common_version, version),
                    modifier = Modifier.padding(top = 8.dp),
                    color = LoginColors.DarkGreen,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// ======================= Components =======================

@Composable
private fun LoginRow(enabled: Boolean, d: RegDims, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(enabled = enabled) { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = LoginColors.LightGreen.copy(alpha = 0.55f),
        border = BorderStroke(1.dp, Color(0xFFD3EADD))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = d.loginPadV + 1.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(d.loginIcon + 2.dp).background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = LoginColors.Primary, modifier = Modifier.size(d.loginIcon * 0.52f))
            }
            Spacer(Modifier.width(7.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Already have an account?", color = LoginColors.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                Text("Login to continue", color = LoginColors.TextSecondary, fontSize = 11.sp, modifier = Modifier.padding(top = 1.dp), maxLines = 1)
            }
            Text("Login", color = LoginColors.Primary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(4.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Login", tint = LoginColors.Primary, modifier = Modifier.size(15.dp))
        }
    }
}

@Composable
private fun WelcomeFeature(icon: ImageVector, label: String, d: RegDims) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = d.featPadV + 3.dp)
            .background(LoginColors.Background.copy(alpha = 0.7f), RoundedCornerShape(14.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(d.featCircle)
                .background(Brush.linearGradient(listOf(LoginColors.LightGreen, Color.White)), CircleShape)
                .border(1.dp, Color(0xFFD3EADD), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = LoginColors.Primary, modifier = Modifier.size(d.featIcon))
        }
        Spacer(Modifier.width(8.dp))
        Text(label, color = LoginColors.TextPrimary, fontSize = d.featSp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun StepProgress(current: Int, d: RegDims) {
    val labels = listOf("Email", "Mobile", "Aadhaar", "Complete")
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        labels.forEachIndexed { index, label ->
            val n = index + 1
            val done = n < current
            val active = n == current
            val circleColor by animateColorAsState(if (done || active) LoginColors.Primary else LoginColors.Background, label = "circle")
            val lineColor by animateColorAsState(if (current > n) LoginColors.Primary else LoginColors.Border, label = "line")
            Column(
                modifier = Modifier.width(52.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(d.stepCircle)
                        .then(if (active) Modifier.shadow(5.dp, CircleShape, ambientColor = LoginColors.Primary, spotColor = LoginColors.Primary) else Modifier)
                        .background(circleColor, CircleShape)
                        .border(1.dp, if (done || active) LoginColors.Primary else LoginColors.Border, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (done) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(d.stepCircle * 0.53f))
                    } else {
                        Text(
                            n.toString(),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (active) Color.White else LoginColors.TextPrimary
                        )
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    label,
                    fontSize = d.stepLabelSp,
                    maxLines = 1,
                    softWrap = false,
                    color = if (done || active) LoginColors.TextPrimary else LoginColors.TextSecondary,
                    fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal
                )
            }
            if (n < labels.size) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = d.stepCircle / 2 - 1.dp)
                        .height(2.dp)
                        .background(lineColor, RoundedCornerShape(2.dp))
                )
            }
        }
    }
}

/** Title + small green accent bar + subtitle. */
@Composable
private fun StepTitle(title: String, subtitle: String, d: RegDims, bottomPad: Dp = d.titleBottom) {
    Text(
        title,
        modifier = Modifier.fillMaxWidth(),
        fontSize = d.titleSp,
        fontWeight = FontWeight.ExtraBold,
        color = LoginColors.TextPrimary,
        textAlign = TextAlign.Center,
        lineHeight = d.titleLine,
        letterSpacing = (-0.2).sp
    )
    if (d.showAccent) {
        Box(
            modifier = Modifier
                .padding(top = d.accentGap)
                .width(38.dp)
                .height(3.dp)
                .background(Brush.horizontalGradient(listOf(LoginColors.LightGreen, LoginColors.DarkGreen)), RoundedCornerShape(2.dp))
        )
    }
    Text(
        subtitle,
        modifier = Modifier.fillMaxWidth().padding(top = d.accentGap, bottom = bottomPad),
        fontSize = d.subSp,
        color = LoginColors.TextSecondary,
        textAlign = TextAlign.Center,
        lineHeight = d.subLine
    )
}

@Composable
private fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType,
    d: RegDims,
    leading: @Composable () -> Unit,
    prefix: String? = null,
    visual: VisualTransformation = VisualTransformation.None,
    trailing: @Composable (() -> Unit)? = null
) {
    val shape = RoundedCornerShape(14.dp)
    var focused by remember { mutableStateOf(false) }
    val borderColor by animateColorAsState(if (focused) LoginColors.Primary else LoginColors.Border, label = "field-border")
    val iconBg by animateColorAsState(if (focused) LoginColors.LightGreen else LoginColors.LightGreen.copy(alpha = 0.55f), label = "field-icon-bg")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(d.inputH)
            .then(if (focused) Modifier.shadow(4.dp, shape, ambientColor = LoginColors.Primary, spotColor = LoginColors.Primary) else Modifier)
            .background(Color.White, shape)
            .border(if (focused) 1.5.dp else 1.dp, borderColor, shape)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(d.inputIconBox).background(iconBg, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) { leading() }
        Spacer(Modifier.width(7.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
            Text(label, fontSize = 11.sp, color = if (focused) LoginColors.Primary else LoginColors.TextSecondary, fontWeight = FontWeight.Medium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (prefix != null) {
                    Text(prefix, fontSize = d.inputSp, color = LoginColors.TextPrimary, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.width(6.dp))
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.weight(1f).onFocusChanged { focused = it.isFocused },
                    singleLine = true,
                    textStyle = TextStyle(fontSize = d.inputSp, color = LoginColors.TextPrimary, fontWeight = FontWeight.Medium),
                    cursorBrush = SolidColor(LoginColors.Primary),
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = ImeAction.Done),
                    visualTransformation = visual,
                    decorationBox = { inner ->
                        Box {
                            if (value.isEmpty()) Text(placeholder, fontSize = d.inputSp, color = LoginColors.TextSecondary.copy(alpha = 0.7f))
                            inner()
                        }
                    }
                )
            }
        }
        if (trailing != null) {
            Spacer(Modifier.width(2.dp))
            trailing()
        }
    }
}

@Composable
private fun OtpBoxes(otp: String, d: RegDims, onOtpChange: (String) -> Unit) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { runCatching { focusRequester.requestFocus() } }

    BasicTextField(
        value = otp,
        onValueChange = { onOtpChange(it.filter(Char::isDigit).take(4)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
        singleLine = true,
        cursorBrush = SolidColor(Color.Transparent),
        textStyle = TextStyle(color = Color.Transparent, fontSize = 1.sp),
        modifier = Modifier.fillMaxWidth().height(d.otpH).focusRequester(focusRequester),
        decorationBox = {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                repeat(4) { index ->
                    val char = otp.getOrNull(index)?.toString().orEmpty()
                    val focused = index == minOf(otp.length, 3)
                    val filled = char.isNotEmpty()
                    val shape = RoundedCornerShape(12.dp)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(d.otpH)
                            .background(if (filled) LoginColors.LightGreen.copy(alpha = 0.6f) else Color.White, shape)
                            .border(
                                if (focused) 1.5.dp else 1.dp,
                                if (focused || filled) LoginColors.Primary else LoginColors.Border,
                                shape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(char, fontSize = d.otpSp, fontWeight = FontWeight.SemiBold, color = LoginColors.TextPrimary)
                    }
                }
            }
        }
    )
}

@Composable
private fun ResendRow(seconds: Int, onResend: () -> Unit) {
    Row(
        modifier = Modifier.padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text("Didn’t receive the code? ", color = LoginColors.TextSecondary, fontSize = 12.sp)
        if (seconds > 0) {
            Text("Resend in 00:%02d".format(seconds), color = LoginColors.Primary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        } else {
            Text(
                "Resend OTP",
                color = LoginColors.Primary, fontSize = 12.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onResend() }
            )
        }
    }
}

@Composable
private fun ErrorText(message: String) {
    if (message.isNotBlank()) {
        Text(
            message,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .background(LoginColors.Error.copy(alpha = 0.07f), RoundedCornerShape(10.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            color = LoginColors.Error,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

/** Gradient primary button. */
@Composable
private fun PrimaryButton(title: String, loading: Boolean, enabled: Boolean, d: RegDims, onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    val clickable = enabled && !loading
    val alpha = if (enabled) 1f else 0.45f
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = d.btnTop)
            .height(d.btnH)
            .shadow(if (enabled) 6.dp else 0.dp, shape, ambientColor = LoginColors.Primary, spotColor = LoginColors.Primary)
            .clip(shape)
            .background(Brush.horizontalGradient(RegButtonGradient.map { it.copy(alpha = alpha) }))
            .clickable(enabled = clickable, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.size(22.dp), strokeWidth = 2.dp, color = Color.White)
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(title, fontWeight = FontWeight.SemiBold, fontSize = d.btnSp, color = Color.White, letterSpacing = 0.3.sp)
                Spacer(Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(19.dp))
            }
        }
    }
}

@Composable
private fun OrDivider() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.weight(1f).height(1.dp).background(Brush.horizontalGradient(listOf(Color.Transparent, LoginColors.Border))))
        Text("OR", modifier = Modifier.padding(horizontal = 12.dp), color = LoginColors.TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        Box(Modifier.weight(1f).height(1.dp).background(Brush.horizontalGradient(listOf(LoginColors.Border, Color.Transparent))))
    }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

// ======================= Validation =======================

/** Verhoeff checksum validation for a 12-digit Aadhaar number. Checks format/checksum only. */
private object AadhaarValidator {
    private val multiplicationTable = arrayOf(
        intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
        intArrayOf(1, 2, 3, 4, 0, 6, 7, 8, 9, 5),
        intArrayOf(2, 3, 4, 0, 1, 7, 8, 9, 5, 6),
        intArrayOf(3, 4, 0, 1, 2, 8, 9, 5, 6, 7),
        intArrayOf(4, 0, 1, 2, 3, 9, 5, 6, 7, 8),
        intArrayOf(5, 9, 8, 7, 6, 0, 4, 3, 2, 1),
        intArrayOf(6, 5, 9, 8, 7, 1, 0, 4, 3, 2),
        intArrayOf(7, 6, 5, 9, 8, 2, 1, 0, 4, 3),
        intArrayOf(8, 7, 6, 5, 9, 3, 2, 1, 0, 4),
        intArrayOf(9, 8, 7, 6, 5, 4, 3, 2, 1, 0)
    )

    private val permutationTable = arrayOf(
        intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
        intArrayOf(1, 5, 7, 6, 2, 8, 3, 0, 9, 4),
        intArrayOf(5, 8, 0, 3, 7, 9, 6, 1, 4, 2),
        intArrayOf(8, 9, 1, 6, 0, 4, 3, 5, 2, 7),
        intArrayOf(9, 4, 5, 3, 1, 2, 6, 8, 7, 0),
        intArrayOf(4, 2, 8, 6, 5, 7, 3, 9, 0, 1),
        intArrayOf(2, 7, 9, 3, 8, 0, 6, 4, 1, 5),
        intArrayOf(7, 0, 4, 6, 9, 1, 3, 2, 5, 8)
    )

    fun isValidAadhaar(aadhaar: String): Boolean {
        if (aadhaar.length != 12 || aadhaar.any { !it.isDigit() }) return false
        var checksum = 0
        aadhaar.reversed().forEachIndexed { index, char ->
            checksum = multiplicationTable[checksum][permutationTable[index % 8][char - '0']]
        }
        return checksum == 0
    }
}
