package com.kaushalpanjee.common.compose.home

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.R
import com.kaushalpanjee.common.CommonViewModel
import com.kaushalpanjee.common.model.request.BannerReq
import com.kaushalpanjee.common.model.request.LogoutRequest
import com.kaushalpanjee.common.model.request.SectionAndPerReq
import com.kaushalpanjee.common.model.request.TrainingSearch
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.Resource
import kotlin.math.cos
import kotlin.math.sin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/* ───────────────────────── Theme ───────────────────────── */

private val GreenDark = Color(0xFF0B5D4B)
private val GreenMid = Color(0xFF137A63)
private val GreenAccent = Color(0xFF1FA34A)
private val TextDark = Color(0xFF10302A)
private val TextGrey = Color(0xFF5F6F6A)
private val ScreenBg = Color(0xFFF4F8F6)
private val BlueStatus = Color(0xFF1E88E5)
private val Teal = Color(0xFF12A594)
private val LogoutRed = Color(0xFFD32F2F)

/** Tablet / foldable pe content stretch na ho. */
private val MaxContentWidth = 640.dp
private fun Modifier.content(): Modifier = this.widthIn(max = MaxContentWidth).fillMaxWidth()

/* ───────────────────────── Models ───────────────────────── */

data class HomeUiState(
    val isLoading: Boolean = false,
    val candidateName: String = "",
    val profileImage: ImageBitmap? = null,
    val totalPercentage: Float = 0f,          // 0..100
    val certFlag: String = "N",
    val attendanceFlag: String = "N",
    val cbtExam: String = "N",
    val schemeType: String = "",
    val stateCode: String = "",
    // TODO: journey ke liye API key aane par yaha se replace karna. Abhi manually Y/N.
    val registerKey: String = "Y",
    val banners: List<ImageBitmap> = emptyList()
)

data class SearchItem(val name: String, val code: String)

/** Saare click / navigation yaha se bahar jaate hain, screen khud navigate nahi karti. */
data class HomeActions(
    val onCandidateDetails: () -> Unit = {},
    val onTrainingDetails: (stateCode: String, schemeType: String) -> Unit = { _, _ -> },
    val onOpenCertificate: (certFlag: String) -> Unit = {},
    val onAttendance: () -> Unit = {},
    val onCbt: () -> Unit = {},
    val onReKyc: () -> Unit = {},
    val onChangePassword: () -> Unit = {},
    val onLanguage: () -> Unit = {},
    val onNotification: () -> Unit = {},
    val onSearchResult: (centerCode: String) -> Unit = {},
    val onSessionExpired: () -> Unit = {},
    val onLogoutSuccess: (message: String) -> Unit = {},
    val onStateLgdCode: (String?) -> Unit = {},
    val onExit: () -> Unit = {}
)

private sealed interface HomeDialog {
    data class NotAvailable(val title: String, val message: String) : HomeDialog
    object Logout : HomeDialog
    object Update : HomeDialog
    object ForcePassword : HomeDialog
}

private enum class StepStatus(val label: String) {
    Completed("Completed"), InProgress("In Progress"), Upcoming("Upcoming")
}

private data class JourneyStep(val title: String, val status: StepStatus)

private data class NavItem(
    val label: String,
    val icon: ImageVector,
    val tint: Color = TextGrey,
    val onClick: () -> Unit
)

/* ───────────────────────── Route (API + logic) ───────────────────────── */

@Composable
fun MainHomeRoute(
    viewModel: CommonViewModel,
    userId: String,
    actions: HomeActions
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    val token = remember { AppUtil.getSavedTokenPreference(context) }
    val androidId = remember { AppUtil.getAndroidId(context) }

    var state by remember { mutableStateOf(HomeUiState()) }
    var query by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<SearchItem>>(emptyList()) }
    var dialog by remember { mutableStateOf<HomeDialog?>(null) }
    var lastBackPress by remember { mutableLongStateOf(0L) }

    // Keep the latest text visible to the long-lived API response collector.
    val latestQuery = rememberUpdatedState(query)
    var lastSearchRequestedQuery by remember { mutableStateOf("") }

    // Clear both the text and suggestions before leaving/searching elsewhere.
    val clearTrainingSearch: () -> Unit = {
        query = ""
        searchResults = emptyList()
        lastSearchRequestedQuery = ""
    }

    val showMsg: (String) -> Unit = { msg ->
        scope.launch {
            snackbar.currentSnackbarData?.dismiss()
            snackbar.showSnackbar(msg)
        }
    }

    /* ---- API hit: Section + Banner ---- */
    LaunchedEffect(Unit) {
        viewModel.getBannerAPI(
            token,
            BannerReq(BuildConfig.VERSION_NAME, userId, androidId)
        )
        viewModel.getSecctionAndPerAPI(
            SectionAndPerReq(BuildConfig.VERSION_NAME, userId, androidId),
            token
        )
    }

    /* ---- Section & Percentage response ---- */
    LaunchedEffect(Unit) {
        viewModel.getSecctionAndPerAPI.collectLatest { res ->
            when (res) {
                is Resource.Loading -> state = state.copy(isLoading = true)
                is Resource.Error -> {
                    state = state.copy(isLoading = false)
                    showMsg("Something went wrong")
                }
                is Resource.Success -> {
                    state = state.copy(isLoading = false)
                    val data = res.data
                    if (data == null) {
                        showMsg("Internal Server Error")
                    } else when (data.responseCode) {
                        200 -> {
                            val x = data.wrappedList.lastOrNull()
                            if (x != null) {
                                val avatar = decodeBase64Image(x.imagePath)
                                AppUtil.saveAadhaarPreference(context, x.aadhaarEnc)
                                actions.onStateLgdCode(x.stateLgdCode?.toString())
                                state = state.copy(
                                    candidateName = x.candidateName ?: "",
                                    profileImage = avatar,
                                    totalPercentage = x.totalPercentage,
                                    certFlag = x.certFlag ?: "N",
                                    attendanceFlag = x.ojtFlag ?: "N",
                                    cbtExam = x.cbtExam ?: "N",
                                    schemeType = x.schemeType ?: "",
                                    stateCode = x.stateCode ?: ""
                                )
                                if (x.firstLogin == "N") dialog = HomeDialog.ForcePassword
                            }
                        }
                        301 -> dialog = HomeDialog.Update
                        401 -> actions.onSessionExpired()
                        else -> showMsg("Something went wrong")
                    }
                }
            }
        }
    }

    /* ---- Banner response ---- */
    LaunchedEffect(Unit) {
        viewModel.getBannerAPI.collectLatest { res ->
            when (res) {
                is Resource.Loading -> Unit
                is Resource.Error -> showMsg("Not Found")
                is Resource.Success -> {
                    val data = res.data
                    if (data == null) {
                        showMsg("Internal Server Error")
                    } else when (data.responseCode) {
                        200 -> {
                            val list = data.bannerList.mapNotNull { decodeBase64Image(it.bannerImage) }
                            state = state.copy(banners = list)
                        }
                        301 -> dialog = HomeDialog.Update
                        401 -> actions.onSessionExpired()
                        else -> showMsg("Something went wrong")
                    }
                }
            }
        }
    }

    /* ---- Training search response ---- */
    LaunchedEffect(Unit) {
        viewModel.getTrainingSearchAPI.collectLatest { res ->
            when (res) {
                is Resource.Loading -> Unit
                is Resource.Error -> showMsg("Not Found")
                is Resource.Success -> {
                    val responseQuery = lastSearchRequestedQuery
                    // A response that arrives after clearing/changing the search must not
                    // repopulate the old suggestions list.
                    if (latestQuery.value.length < 4 || responseQuery.isBlank() || latestQuery.value != responseQuery) {
                        return@collectLatest
                    }
                    val data = res.data
                    if (data == null) {
                        searchResults = emptyList()
                        showMsg("Internal Server Error")
                    } else when (data.responseCode) {
                        200 -> searchResults = data.centerList.map {
                            SearchItem(it.centerName.toString(), it.centerCode.toString())
                        }
                        301 -> dialog = HomeDialog.Update
                        401 -> actions.onSessionExpired()
                        else -> {
                            searchResults = emptyList()
                            showMsg("Something went wrong")
                        }
                    }
                }
            }
        }
    }

    /* ---- Logout response ---- */
    LaunchedEffect(Unit) {
        viewModel.getLogout.collectLatest { res ->
            when (res) {
                is Resource.Loading -> state = state.copy(isLoading = true)
                is Resource.Error -> {
                    state = state.copy(isLoading = false)
                    showMsg(res.data?.responseDesc ?: "Something went wrong")
                }
                is Resource.Success -> {
                    state = state.copy(isLoading = false)
                    val data = res.data
                    when (data?.responseCode) {
                        200 -> actions.onLogoutSuccess(data.responseDesc ?: "Logged out")
                        301 -> dialog = HomeDialog.Update
                        401 -> actions.onSessionExpired()
                        404 -> showMsg(data.responseDesc ?: "Not found")
                        else -> showMsg("Something went wrong")
                    }
                }
            }
        }
    }

    /* ---- Search debounce (min 4 chars) ---- */
    LaunchedEffect(query) {
        val searchText = query.trim()
        // Suggestions disappear immediately as soon as the user edits/clears the field.
        searchResults = emptyList()
        if (searchText.length >= 4) {
            delay(400)
            // Query may have changed during debounce; never submit a stale value.
            if (query.trim() == searchText) {
                lastSearchRequestedQuery = searchText
                viewModel.getTrainingSearchAPI(
                    TrainingSearch(BuildConfig.VERSION_NAME, searchText, userId, androidId),
                    token
                )
            }
        } else {
            lastSearchRequestedQuery = ""
        }
    }

    /* ---- Double back to exit ---- */
    BackHandler {
        val now = System.currentTimeMillis()
        if (now - lastBackPress < 2000) actions.onExit()
        else {
            lastBackPress = now
            showMsg("Press back again to exit")
        }
    }

    /* ---- Click rules (flag based) ---- */
    val notAvailable: (String, String) -> Unit = { t, m -> dialog = HomeDialog.NotAvailable(t, m) }

    MainHomeScreen(
        state = state,
        query = query,
        onQueryChange = {
            query = it
            // Do not keep showing the previous query's list while a new query is typed.
            searchResults = emptyList()
        },
        searchResults = searchResults,
        snackbar = snackbar,
        onSearchItemClick = { item ->
            clearTrainingSearch()
            actions.onSearchResult(item.code)
        },
        onCandidateDetails = {
            clearTrainingSearch()
            actions.onCandidateDetails()
        },
        onTrainingDetails = {
            clearTrainingSearch()
            actions.onTrainingDetails(state.stateCode, state.schemeType)
        },
        onLanguage = {
            clearTrainingSearch()
            actions.onLanguage()
        },
        onNotification = {
            clearTrainingSearch()
            actions.onNotification()
        },
        onAttendance = {
            clearTrainingSearch()
            if (state.attendanceFlag == "Y") actions.onAttendance()
            else notAvailable(
                "Attendance not open yet",
                "OJT attendance isn't available for you right now. It will open once your on-the-job training begins."
            )
        },
        onCertificate = {
            clearTrainingSearch()
            if (state.certFlag == "Y") actions.onOpenCertificate(state.certFlag)
            else notAvailable(
                "Certificate not ready",
                "Your certificate hasn't been generated yet. Complete your training and assessment, and it will appear here."
            )
        },
        onCbt = {
            clearTrainingSearch()
            if (state.cbtExam == "Y") actions.onCbt()
            else notAvailable(
                "CBT not available",
                "Your CBT exam isn't open for you right now. We'll enable it as soon as you're scheduled."
            )
        },
        onReKyc = {
            clearTrainingSearch()
            actions.onReKyc()
        },
        onChangePassword = {
            clearTrainingSearch()
            actions.onChangePassword()
        },
        onLogout = {
            clearTrainingSearch()
            dialog = HomeDialog.Logout
        }
    )

    /* ---- Dialogs ---- */
    when (val d = dialog) {
        null -> Unit
        is HomeDialog.NotAvailable -> AlertDialog(
            onDismissRequest = { dialog = null },
            icon = { Icon(Icons.Filled.Info, null, tint = GreenMid) },
            title = { Text(d.title, fontWeight = FontWeight.Bold) },
            text = { Text(d.message) },
            confirmButton = { TextButton({ dialog = null }) { Text("Got it") } },
            containerColor = Color.White
        )
        HomeDialog.Logout -> AlertDialog(
            onDismissRequest = { dialog = null },
            title = { Text("Logout", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton({
                    dialog = null
                    AppUtil.saveLoginStatus(context, false)
                    viewModel.getLogout(
                        LogoutRequest(BuildConfig.VERSION_NAME, androidId, userId),
                        token
                    )
                }) { Text("Logout", color = LogoutRed) }
            },
            dismissButton = { TextButton({ dialog = null }) { Text("Cancel") } },
            containerColor = Color.White
        )
        HomeDialog.Update -> AlertDialog(
            onDismissRequest = {},
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
            title = { Text("Update Available", fontWeight = FontWeight.Bold) },
            text = { Text("A new version of the app is available. Please update to continue.") },
            confirmButton = {
                TextButton({
                    openPlayStore(context)
                    dialog = null
                }) { Text("Update") }
            },
            dismissButton = { TextButton({ dialog = null }) { Text("Cancel") } },
            containerColor = Color.White
        )
        HomeDialog.ForcePassword -> AlertDialog(
            onDismissRequest = {},
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
            title = { Text("Security Warning", fontWeight = FontWeight.Bold) },
            text = { Text("For security reasons, you must change your password before continuing.") },
            confirmButton = {
                TextButton({
                    dialog = null
                    actions.onChangePassword()
                }) { Text("OK") }
            },
            containerColor = Color.White
        )
    }
}

private fun openPlayStore(context: Context) {
    val pkg = "com.kaushalpanjee"
    try {
        context.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$pkg"))
                .setPackage("com.android.vending")
        )
    } catch (e: Exception) {
        context.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$pkg&hl=en_IN"))
        )
    }
}

private suspend fun decodeBase64Image(data: String?): ImageBitmap? =
    withContext(Dispatchers.Default) {
        if (data.isNullOrBlank()) return@withContext null
        runCatching {
            val bytes = Base64.decode(data, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
        }.getOrNull()
    }

/* ───────────────────────── Screen (stateless UI) ───────────────────────── */

@Composable
fun MainHomeScreen(
    state: HomeUiState,
    query: String,
    onQueryChange: (String) -> Unit,
    searchResults: List<SearchItem>,
    snackbar: SnackbarHostState,
    onSearchItemClick: (SearchItem) -> Unit,
    onCandidateDetails: () -> Unit,
    onTrainingDetails: () -> Unit,
    onLanguage: () -> Unit,
    onNotification: () -> Unit,
    onAttendance: () -> Unit,
    onCertificate: () -> Unit,
    onCbt: () -> Unit,
    onReKyc: () -> Unit,
    onChangePassword: () -> Unit,
    onLogout: () -> Unit
) {
    // Floating bottom bar: Attendance OJT, ReKYC, [Home], Change Password, Logout
    val left = listOf(
        NavItem("Attendance OJT", Icons.Outlined.CalendarMonth, onClick = onAttendance),
        NavItem("ReKYC", Icons.Outlined.Fingerprint, onClick = onReKyc)
    )
    val right = listOf(
        NavItem("Change Password", Icons.Outlined.Lock, onClick = onChangePassword),
        NavItem("Logout", Icons.AutoMirrored.Outlined.Logout, tint = LogoutRed, onClick = onLogout)
    )

    Scaffold(
        containerColor = ScreenBg,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbar) },
        bottomBar = { FloatingBottomBar(left, right) }
    ) { padding ->
        BoxWithConstraints(Modifier.fillMaxSize().padding(padding)) {
            val compact = maxWidth < 360.dp

            Column(
                Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Header + overlapping search bar
                Box(Modifier.fillMaxWidth()) {
                    HomeHeader(
                        name = state.candidateName,
                        profile = state.profileImage,
                        compact = compact,
                        onLanguage = onLanguage,
                        onNotification = onNotification
                    )
                    HomeSearchBar(
                        query = query,
                        onQueryChange = onQueryChange,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = 29.dp)
                            .content()
                            .padding(horizontal = 16.dp)
                    )
                }
                Spacer(Modifier.height(41.dp))

                if (searchResults.isNotEmpty()) {
                    SearchResults(searchResults, onSearchItemClick)
                    Spacer(Modifier.height(12.dp))
                }

                BannerCarousel(
                    banners = state.banners,
                    compact = compact,
                    onExplore = onTrainingDetails,
                    modifier = Modifier.content().padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(16.dp))

                ProfileCard(
                    percent = state.totalPercentage,
                    compact = compact,
                    onCompleteNow = onCandidateDetails,
                    modifier = Modifier.content().padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(16.dp))

                val steps = listOf(
                    JourneyStep(
                        "Register",
                        if (state.registerKey == "Y") StepStatus.Completed else StepStatus.InProgress
                    ),
                    // baki static, API key aane par replace
                    JourneyStep("Batch Enrolled", StepStatus.Completed),
                    JourneyStep("Batch Completed", StepStatus.Completed),
                    JourneyStep("Assessment Completed", StepStatus.InProgress),
                    JourneyStep("Certificate Generated", StepStatus.Upcoming)
                )
                JourneyCard(steps, compact, Modifier.content().padding(horizontal = 16.dp))
                Spacer(Modifier.height(22.dp))

                Text(
                    "Services for you",
                    modifier = Modifier.content().padding(horizontal = 16.dp),
                    fontSize = if (compact) 16.sp else 18.sp,
                    fontWeight = FontWeight.Bold, color = TextDark
                )
                Spacer(Modifier.height(10.dp))

                // 2 x 2 services grid
                Column(
                    Modifier.content().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ServiceCard(
                            title = "Candidate Details", subtitle = "View & update your profile",
                            icon = Icons.Outlined.Badge, compact = compact,
                            bg = Color(0xFFE6F6EF), border = Color(0xFFBFE6D4),
                            iconBg = Color(0xFFC9EEDB), iconTint = GreenMid,
                            onClick = onCandidateDetails,
                            modifier = Modifier.weight(1f).fillMaxHeight()
                        )
                        ServiceCard(
                            title = "Training Details", subtitle = "Check your training information",
                            icon = Icons.Outlined.School, compact = compact,
                            bg = Color(0xFFFFF3E0), border = Color(0xFFFFD9A8),
                            iconBg = Color(0xFFFFE2B8), iconTint = Color(0xFFE08A00),
                            onClick = onTrainingDetails,
                            modifier = Modifier.weight(1f).fillMaxHeight()
                        )
                    }
                    Row(
                        Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ServiceCard(
                            title = "View Certificate", subtitle = "Download your training certificate",
                            icon = Icons.Outlined.WorkspacePremium, compact = compact,
                            bg = Color(0xFFE8F0FE), border = Color(0xFFC5D8F7),
                            iconBg = Color(0xFFD5E3FB), iconTint = Color(0xFF2F6FDB),
                            onClick = onCertificate,
                            modifier = Modifier.weight(1f).fillMaxHeight()
                        )
                        ServiceCard(
                            title = "CBT Exam", subtitle = "Take your computer based test",
                            icon = Icons.Outlined.Description, compact = compact,
                            bg = Color(0xFFF3EAFB), border = Color(0xFFDCC8F0),
                            iconBg = Color(0xFFE6D5F6), iconTint = Color(0xFF7B3FC4),
                            onClick = onCbt,
                            modifier = Modifier.weight(1f).fillMaxHeight()
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))
            }

            if (state.isLoading) {
                Box(
                    Modifier.fillMaxSize().background(Color(0x66000000))
                        .clickable(enabled = true, onClick = {}),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = Color.White) }
            }
        }
    }
}

/* ───────────────────────── Header ───────────────────────── */

@Composable
private fun HomeHeader(
    name: String,
    profile: ImageBitmap?,
    compact: Boolean,
    onLanguage: () -> Unit,
    onNotification: () -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
            .background(Brush.verticalGradient(listOf(GreenMid, GreenDark)))
            .drawBehind {
                // soft decorative shapes (leaf / circles) like the reference
                drawCircle(Color(0x12FFFFFF), size.width * 0.55f, Offset(size.width * 1.0f, size.height * 1.05f))
                drawCircle(Color(0x0DFFFFFF), size.width * 0.38f, Offset(size.width * 0.05f, -size.height * 0.1f))
                val cx = size.width * 0.84f
                val cy = size.height * 0.62f
                val lw = 26.dp.toPx()
                val lh = 78.dp.toPx()
                rotate(-28f, Offset(cx, cy)) {
                    drawOval(Color(0x1CFFFFFF), Offset(cx - lw / 2, cy - lh / 2), Size(lw, lh))
                }
                rotate(28f, Offset(cx, cy)) {
                    drawOval(Color(0x16FFFFFF), Offset(cx - lw / 2 + 12.dp.toPx(), cy - lh / 2 + 10.dp.toPx()), Size(lw, lh))
                }
                drawCircle(Color(0x24FFFFFF), 11.dp.toPx(), Offset(cx, cy - lh / 2 - 6.dp.toPx()))
            }
            .statusBarsPadding()
    ) {
        Column(
            Modifier
                .align(Alignment.TopCenter)
                .content()
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 52.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.app_name),
                    modifier = Modifier.weight(1f),
                    color = Color.White,
                    fontSize = if (compact) 18.sp else 20.sp,
                    fontWeight = FontWeight.Medium
                )
                // Language
                Box(
                    Modifier.size(30.dp).clip(RoundedCornerShape(10.dp))
                        .border(2.dp, Color.White, RoundedCornerShape(10.dp))
                        .clickable(onClick = onLanguage),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Aअ", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(if (compact) 10.dp else 16.dp))
                // Avatar
                Box(
                    Modifier.size(if (compact) 44.dp else 52.dp).clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                        .background(Color(0xFFCFE8DF)),
                    contentAlignment = Alignment.Center
                ) {
                    if (profile != null) {
                        Image(profile, null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    } else {
                        Icon(Icons.Filled.Person, null, tint = GreenDark)
                    }
                }
                Spacer(Modifier.width(if (compact) 8.dp else 14.dp))
                // Bell
                Box(Modifier.size(42.dp).clip(CircleShape).clickable(onClick = onNotification), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.Notifications, null, tint = Color.White, modifier = Modifier.size(30.dp))
                    Box(
                        Modifier.align(Alignment.TopEnd).padding(top = 5.dp, end = 7.dp)
                            .size(11.dp).clip(CircleShape).background(Color(0xFFFF4D4D))
                            .border(1.5.dp, GreenMid, CircleShape)
                    )
                }
            }
            Spacer(Modifier.height(if (compact) 14.dp else 20.dp))
            Text(
                "Hi, ${name.ifBlank { "Candidate" }} ",
                color = Color.White,
                fontSize = if (compact) 24.sp else 29.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1, overflow = TextOverflow.Ellipsis
            )
            Text(
                "Continue your skilling journey",
                color = Color.White, fontSize = if (compact) 14.sp else 16.sp
            )
        }
    }
}

/* ───────────────────────── Search ───────────────────────── */

private val searchHints = listOf(
    "Search with training center name",
    "Search with location",
    "Search with courses"
)

@Composable
private fun HomeSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var hintIndex by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(2500)
            hintIndex = (hintIndex + 1) % searchHints.size
        }
    }

    Surface(
        modifier = modifier.fillMaxWidth().height(58.dp),
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        shadowElevation = 6.dp
    ) {
        Row(
            Modifier.padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Search, null, tint = TextGrey, modifier = Modifier.size(26.dp))
            Spacer(Modifier.width(12.dp))
            Box(Modifier.weight(1f).clipToBounds(), contentAlignment = Alignment.CenterStart) {
                if (query.isEmpty()) {
                    AnimatedContent(
                        targetState = hintIndex,
                        transitionSpec = {
                            (slideInVertically { it } + fadeIn()) togetherWith
                                    (slideOutVertically { -it } + fadeOut())
                        },
                        label = "searchHint"
                    ) { i ->
                        Text(
                            searchHints[i], color = TextGrey, fontSize = 16.sp,
                            maxLines = 1, overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = TextStyle(color = TextDark, fontSize = 16.sp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(Modifier.width(8.dp))
            if (query.isNotEmpty()) {
                Icon(
                    Icons.Filled.Close, null, tint = TextGrey,
                    modifier = Modifier.size(24.dp).clip(CircleShape).clickable { onQueryChange("") }
                )
            } else {
                Icon(Icons.Outlined.Tune, null, tint = TextDark, modifier = Modifier.size(26.dp))
            }
        }
    }
}

@Composable
private fun SearchResults(items: List<SearchItem>, onClick: (SearchItem) -> Unit) {
    Surface(
        modifier = Modifier.content().padding(horizontal = 16.dp).heightIn(max = 240.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            items.forEachIndexed { i, item ->
                Row(
                    Modifier.fillMaxWidth().clickable { onClick(item) }.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.LocationOn, null, tint = GreenMid)
                    Spacer(Modifier.width(10.dp))
                    Text(item.name, color = TextDark, fontSize = 15.sp)
                }
                if (i != items.lastIndex) {
                    Box(Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFE8EEEC)))
                }
            }
        }
    }
}

/* ───────────────────────── Banner ───────────────────────── */

@Composable
private fun BannerCarousel(
    banners: List<ImageBitmap>,
    compact: Boolean,
    onExplore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(22.dp)
    val bannerHeight = if (compact) 160.dp else 176.dp

    if (banners.isEmpty()) {
        // Fallback: reference design wala card (API banner aane par replace ho jata hai)
        Box(
            modifier.fillMaxWidth().height(bannerHeight).clip(shape)
                .background(Brush.horizontalGradient(listOf(Color(0xFFD6F0E3), Color(0xFFF0FAF5))))
                .drawBehind {
                    drawCircle(Color(0x33FFFFFF), size.height * 0.9f, Offset(size.width * 0.05f, -size.height * 0.1f))
                    drawCircle(Color(0x40C9EEDB), size.height * 1.1f, Offset(size.width * 1.0f, size.height * 1.25f))
                }
        ) {
            // Illustration placeholder (yaha apna drawable laga sakte ho: painterResource)
            Box(
                Modifier.align(Alignment.CenterStart).padding(start = 14.dp)
                    .size(if (compact) 84.dp else 104.dp)
                    .clip(CircleShape).background(Color(0xFFBFE6D4)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.School, null, tint = GreenDark, modifier = Modifier.size(if (compact) 44.dp else 54.dp))
            }
            Column(
                Modifier.align(Alignment.CenterEnd).fillMaxWidth(if (compact) 0.58f else 0.6f)
                    .padding(end = 14.dp)
            ) {
                Text(
                    "Your dream job awaits", color = GreenDark,
                    fontSize = if (compact) 19.sp else 22.sp, fontWeight = FontWeight.ExtraBold,
                    lineHeight = if (compact) 23.sp else 26.sp, maxLines = 2
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Explore government-backed career training", color = TextDark,
                    fontSize = if (compact) 12.sp else 14.sp, lineHeight = 17.sp, maxLines = 2
                )
                Spacer(Modifier.height(10.dp))
                Button(
                    onClick = onExplore,
                    colors = ButtonDefaults.buttonColors(containerColor = GreenDark),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text("Explore courses", fontWeight = FontWeight.SemiBold, fontSize = if (compact) 12.sp else 14.sp, maxLines = 1)
                    Spacer(Modifier.width(4.dp))
                    Icon(Icons.Filled.KeyboardArrowRight, null, modifier = Modifier.size(18.dp))
                }
            }
            Row(
                Modifier.align(Alignment.BottomEnd).padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(Modifier.size(10.dp).clip(CircleShape).background(GreenDark))
                Box(Modifier.size(10.dp).clip(CircleShape).background(Color(0xFF9ADBB8)))
                Box(Modifier.size(10.dp).clip(CircleShape).background(Color(0xFF9ADBB8)))
            }
        }
        return
    }

    val pagerState = rememberPagerState { banners.size }
    LaunchedEffect(banners.size) {
        while (banners.size > 1) {
            delay(3000)
            pagerState.animateScrollToPage((pagerState.currentPage + 1) % banners.size)
        }
    }
    Box(modifier.fillMaxWidth().height(bannerHeight).clip(shape)) {
        HorizontalPager(pagerState, Modifier.fillMaxSize()) { page ->
            Image(
                bitmap = banners[page], contentDescription = null,
                modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop
            )
        }
        if (banners.size > 1) {
            Row(
                Modifier.align(Alignment.BottomEnd).padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(banners.size) { i ->
                    Box(
                        Modifier.size(9.dp).clip(CircleShape)
                            .background(if (i == pagerState.currentPage) GreenDark else Color(0x99FFFFFF))
                    )
                }
            }
        }
    }
}

/* ───────────────────────── Profile completion ───────────────────────── */

@Composable
private fun ProfileCard(
    percent: Float,
    compact: Boolean,
    onCompleteNow: () -> Unit,
    modifier: Modifier = Modifier
) {
    val complete = percent >= 100f
    val shape = RoundedCornerShape(20.dp)
    Row(
        modifier.fillMaxWidth()
            .clip(shape)
            .background(Brush.horizontalGradient(listOf(Color(0xFFE4F6EC), Color(0xFFF1FAF6))))
            .border(1.dp, Color(0xFFCDEBDD), shape)
            .padding(start = 14.dp, top = 10.dp, bottom = 10.dp, end = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!compact) {
            Box(Modifier.size(52.dp)) {          // bahar wala box: clip nahi, badge ko jagah milti hai
                Box(
                    Modifier
                        .align(Alignment.Center)
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFC9EEDB)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Badge, null,
                        tint = GreenDark,
                        modifier = Modifier.size(26.dp)
                    )
                }
                // Check badge: ab pura dikhega
                Box(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .size(19.dp)
                        .clip(CircleShape)
                        .background(GreenDark)
                        .border(1.5.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Check, null, tint = Color.White, modifier = Modifier.size(11.dp))
                }
            }
            Spacer(Modifier.width(10.dp))
        }
        Column(Modifier.weight(1f)) {
            Text(
                if (complete) "Your profile is complete!" else "Complete your profile",
                color = TextDark, fontSize = if (compact) 13.sp else 14.sp,
                fontWeight = FontWeight.Bold, lineHeight = if (compact) 16.sp else 17.sp,
                maxLines = 1, softWrap = false, overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(2.dp))
            Text(
                if (complete) "All set to access training, assessments and certification services."
                else "Finish your profile to access training, assessments and certification.",
                color = TextGrey, fontSize = if (compact) 10.sp else 11.sp, lineHeight = if (compact) 13.sp else 14.sp,
                maxLines = 2, overflow = TextOverflow.Ellipsis
            )
            if (!complete) {
                Spacer(Modifier.height(6.dp))
                Text(
                    "Complete now →",
                    color = GreenDark, fontSize = 12.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.clip(RoundedCornerShape(6.dp)).clickable(onClick = onCompleteNow)
                )
            }
        }
        Spacer(Modifier.width(4.dp))
        ProgressRing(percent, if (compact) 66.dp else 72.dp, if (compact) 9.dp else 10.dp)
    }
}

private val confettiSpec = listOf(
    -75f to Color(0xFFFF5A5F), -35f to Color(0xFFB04CE0), 5f to Color(0xFF1FA34A),
    50f to Color(0xFFFFB300), 95f to Color(0xFF2F9BFF), 140f to Color(0xFFFF8A3D),
    185f to Color(0xFF1FA34A), 225f to Color(0xFFFFC21A)
)

@Composable
private fun ProgressRing(percent: Float, boxSize: Dp, inset: Dp) {
    val p = percent.coerceIn(0f, 100f)
    var target by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(p) { target = p }
    val sweep by animateFloatAsState(360f * target / 100f, tween(1200), label = "ring")

    Box(Modifier.size(boxSize), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val c = center
            val stroke = 7.dp.toPx()
            val ringR = size.minDimension / 2 - inset.toPx()
            val tl = Offset(c.x - ringR, c.y - ringR)
            val sz = Size(ringR * 2, ringR * 2)

            drawCircle(Color.White, ringR)
            drawArc(Color(0xFFD3EDE0), -90f, 360f, false, tl, sz, style = Stroke(stroke))
            drawArc(GreenAccent, -90f, sweep, false, tl, sz, style = Stroke(stroke, cap = StrokeCap.Round))

            if (p >= 100f) {
                confettiSpec.forEach { (deg, col) ->
                    val rad = Math.toRadians(deg.toDouble())
                    val d = Offset(cos(rad).toFloat(), sin(rad).toFloat())
                    val r1 = ringR + stroke / 2 + 4.dp.toPx()
                    val r2 = r1 + 6.dp.toPx()
                    drawLine(col, c + d * r1, c + d * r2, 3.dp.toPx(), StrokeCap.Round)
                }
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("${p.toInt()}%", color = TextDark, fontSize = 15.sp, lineHeight = 17.sp, fontWeight = FontWeight.ExtraBold, maxLines = 1)
            Text(
                "Profile\nCompletion", color = TextDark, fontSize = 7.sp,
                textAlign = TextAlign.Center, lineHeight = 8.sp, maxLines = 2
            )
        }
    }
}

/* ───────────────────────── Skilling journey ───────────────────────── */

@Composable
private fun JourneyCard(steps: List<JourneyStep>, compact: Boolean, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier.fillMaxWidth()
            .shadow(2.dp, shape)
            .background(Color.White, shape)
            .border(1.dp, Color(0xFFEAF0EE), shape)
            .padding(horizontal = if (compact) 10.dp else 14.dp, vertical = 16.dp)
    ) {
        Text(
            "Your skilling journey", color = TextDark,
            fontSize = if (compact) 16.sp else 18.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Text(
            "Track your progress from registration to certification",
            color = TextGrey, fontSize = if (compact) 12.sp else 13.sp, lineHeight = 17.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Spacer(Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth()) {
            steps.forEachIndexed { i, step ->
                Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Connector(
                            Modifier.weight(1f),
                            visible = i > 0,
                            solid = i > 0 && steps[i - 1].status != StepStatus.Upcoming &&
                                    steps[i - 1].status == StepStatus.Completed,
                            color = if (step.status == StepStatus.InProgress) Teal else GreenAccent
                        )
                        StepCircle(i + 1, step.status, if (compact) 27.dp else 31.dp)
                        Connector(
                            Modifier.weight(1f),
                            visible = i < steps.lastIndex,
                            solid = step.status == StepStatus.Completed &&
                                    steps[i + 1].status != StepStatus.Upcoming,
                            color = GreenAccent
                        )
                    }
                    Spacer(Modifier.height(5.dp))
                    Text("${i + 1}", color = TextDark, fontSize = 14.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Text(
                        step.title, color = TextDark,
                        fontSize = if (compact) 8.sp else 10.sp, fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center, lineHeight = if (compact) 10.sp else 12.sp,
                        minLines = 2, maxLines = 2
                    )
                    Text(
                        step.status.label, fontSize = if (compact) 10.sp else 11.sp,
                        textAlign = TextAlign.Center, maxLines = 1,
                        color = when (step.status) {
                            StepStatus.Completed -> GreenAccent
                            StepStatus.InProgress -> BlueStatus
                            StepStatus.Upcoming -> TextGrey
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun StepCircle(number: Int, status: StepStatus, size: Dp) {
    val bg = when (status) {
        StepStatus.Completed -> GreenAccent
        StepStatus.InProgress -> Teal
        StepStatus.Upcoming -> Color(0xFFE3E7E6)
    }
    Box(
        Modifier.size(size)
            .then(
                if (status == StepStatus.InProgress)
                    Modifier.border(3.dp, Color(0xFFB7E6DF), CircleShape) else Modifier
            )
            .clip(CircleShape).background(bg),
        contentAlignment = Alignment.Center
    ) {
        if (status == StepStatus.Completed) {
            Icon(Icons.Filled.Check, null, tint = Color.White, modifier = Modifier.size(size * 0.58f))
        } else {
            Text(
                "$number", fontWeight = FontWeight.Bold, fontSize = if (size <= 27.dp) 12.sp else 13.sp,
                color = if (status == StepStatus.InProgress) Color.White else TextGrey
            )
        }
    }
}

@Composable
private fun Connector(modifier: Modifier, visible: Boolean, solid: Boolean, color: Color) {
    Canvas(modifier.height(3.dp)) {
        if (!visible) return@Canvas
        drawLine(
            color = if (solid) color else Color(0xFFCBD3D0),
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = if (solid) 3.dp.toPx() else 2.dp.toPx(),
            pathEffect = if (solid) null else PathEffect.dashPathEffect(floatArrayOf(6f, 8f))
        )
    }
}

/* ───────────────────────── Services ───────────────────────── */

@Composable
private fun ServiceCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    compact: Boolean,
    bg: Color,
    border: Color,
    iconBg: Color,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(18.dp)
    Row(
        modifier.shadow(2.dp, shape).clip(shape).background(bg).border(1.dp, border, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(if (compact) 40.dp else 46.dp).clip(CircleShape).background(iconBg),
            contentAlignment = Alignment.Center
        ) { Icon(icon, null, tint = iconTint, modifier = Modifier.size(if (compact) 22.dp else 26.dp)) }
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                title, color = TextDark, fontSize = if (compact) 12.sp else 13.sp,
                fontWeight = FontWeight.Bold, lineHeight = 15.sp, maxLines = 2,
                textAlign = TextAlign.Center, overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(2.dp))
            Text(
                subtitle, color = TextGrey, fontSize = if (compact) 9.sp else 10.sp,
                lineHeight = 12.sp, maxLines = 2, textAlign = TextAlign.Center, overflow = TextOverflow.Ellipsis
            )
        }
        Icon(Icons.Filled.KeyboardArrowRight, null, tint = TextDark, modifier = Modifier.size(20.dp))
    }
}

/* ───────────────────────── Floating bottom bar ───────────────────────── */

@Composable
private fun FloatingBottomBar(left: List<NavItem>, right: List<NavItem>) {
    val shape = RoundedCornerShape(30.dp)
    Box(
        Modifier.fillMaxWidth().navigationBarsPadding()
            .padding(start = 14.dp, end = 14.dp, bottom = 10.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(Modifier.widthIn(max = 480.dp).fillMaxWidth()) {
            Row(
                Modifier
                    .padding(top = 12.dp)                 // pehle 30.dp tha
                    .fillMaxWidth()
                    .shadow(14.dp, shape, clip = false)
                    .background(Color.White, shape)
                    .padding(vertical = 9.dp, horizontal = 4.dp),
                verticalAlignment = Alignment.Top
            ) {
                left.forEach { BarItem(it, Modifier.weight(1f)) }
                Spacer(Modifier.weight(1.25f))
                right.forEach { BarItem(it, Modifier.weight(1f)) }
            }
            // Home button (neeche kiya)
            Column(
                Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 6.dp)                     // button ko aur neeche
                    .size(62.dp)                          // pehle 68.dp
                    .shadow(10.dp, CircleShape)
                    .background(Brush.verticalGradient(listOf(GreenMid, GreenAccent)), CircleShape)
                    .border(4.dp, Color.White, CircleShape),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Home", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                Spacer(Modifier.height(4.dp))
                Box(Modifier.width(26.dp).height(3.dp).clip(RoundedCornerShape(2.dp)).background(Color.White))
            }
        }
    }
}

@Composable
private fun BarItem(item: NavItem, modifier: Modifier = Modifier) {
    Column(
        modifier.clip(RoundedCornerShape(14.dp)).clickable(onClick = item.onClick)
            .padding(horizontal = 2.dp, vertical = 3.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(item.icon, null, tint = item.tint, modifier = Modifier.size(26.dp))
        Spacer(Modifier.height(3.dp))
        Text(
            item.label, color = item.tint, fontSize = 10.sp, lineHeight = 12.sp,
            textAlign = TextAlign.Center, maxLines = 2
        )
    }
}