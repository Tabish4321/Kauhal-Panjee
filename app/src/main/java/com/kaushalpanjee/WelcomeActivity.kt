package com.kaushalpanjee

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.kaushalpanjee.common.CommonActivity
import com.kaushalpanjee.core.util.AppUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import kotlin.system.exitProcess

class WelcomeActivity : ComponentActivity() {

    private var navigationStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)

        // Match the previous fullscreen splash behavior from BaseActivity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes = window.attributes.apply {
                layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
        }

        AppUtil.changeAppLanguage(this, AppUtil.getSavedLanguagePreference(this))

        setContent {
            MaterialTheme {
                WelcomeSplashScreen()
            }
        }

        lifecycleScope.launch {
            delay(2200)

            val warning = getSecurityWarningMessage()
            if (warning != null) {
                showSecurityWarning(warning)
            } else {
                navigateToCommonActivity()
            }
        }
    }

    private fun navigateToCommonActivity() {
        if (navigationStarted || isFinishing || isDestroyed) return
        navigationStarted = true

        startActivity(
            Intent(this@WelcomeActivity, CommonActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun getSecurityWarningMessage(): String? {
        val rooted = isDeviceRooted()
        val emulator = isRunningOnEmulator()
        val developerMode = isDeveloperModeEnabled(this)

        return when {
            rooted && emulator ->
                "Rooted device and emulator detected. For security reasons, this app cannot run."
            rooted ->
                "Rooted device detected. For security reasons, this app cannot run."
            emulator ->
                "Emulator detected. This app cannot run on emulators."
            /*developerMode ->
                "Developer Options or USB Debugging is enabled. Please disable it to continue."*/
            else -> null
        }
    }

    private fun isDeviceRooted(): Boolean {
        val paths = arrayOf(
            "/system/bin/su",
            "/system/xbin/su",
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/.ext/.su",
            "/system/usr/we-need-root/su-backup",
            "/system/xbin/mu"
        )

        return paths.any { File(it).exists() } ||
                Build.TAGS?.contains("test-keys") == true
    }

    private fun isRunningOnEmulator(): Boolean {
        return Build.FINGERPRINT.startsWith("generic") ||
                Build.FINGERPRINT.startsWith("unknown") ||
                Build.MODEL.contains("google_sdk", true) ||
                Build.MODEL.contains("emulator", true) ||
                Build.MODEL.contains("android sdk built for x86", true) ||
                Build.MODEL.contains("nox", true) ||
                Build.MANUFACTURER.contains("Genymotion", true) ||
                Build.MANUFACTURER.contains("nox", true) ||
                Build.HARDWARE.contains("goldfish", true) ||
                Build.HARDWARE.contains("ranchu", true) ||
                Build.HARDWARE.contains("vbox", true) ||
                Build.HARDWARE.contains("nox", true) ||
                Build.PRODUCT.contains("sdk", true) ||
                Build.PRODUCT.contains("google_sdk", true) ||
                Build.PRODUCT.contains("emulator", true) ||
                Build.PRODUCT.contains("simulator", true) ||
                Build.BRAND.startsWith("generic") ||
                Build.DEVICE.startsWith("generic") ||
                Build.BOARD.contains("nox", true)
    }

    private fun isDeveloperModeEnabled(activity: Activity): Boolean {
        return try {
            val developerOptions = Settings.Global.getInt(
                activity.contentResolver,
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
                0
            ) == 1

            val adbEnabled = Settings.Global.getInt(
                activity.contentResolver,
                Settings.Global.ADB_ENABLED,
                0
            ) == 1

            developerOptions || adbEnabled
        } catch (_: Exception) {
            false
        }
    }

    private fun showSecurityWarning(message: String) {
        if (isFinishing || isDestroyed) return

        AlertDialog.Builder(this)
            .setTitle("Security Warning")
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Exit") { _, _ ->
                finishAffinity()
                android.os.Process.killProcess(android.os.Process.myPid())
                exitProcess(0)
            }
            .show()
    }
}

@Composable
private fun WelcomeSplashScreen() {
    val transition = rememberInfiniteTransition(label = "splash")
    val logoScale = transition.animateFloat(
        initialValue = 0.97f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logoScale"
    )
    val loaderAlpha = transition.animateFloat(
        initialValue = 0.55f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(850),
            repeatMode = RepeatMode.Reverse
        ),
        label = "loaderAlpha"
    )

    val navy = Color(0xFF102A56)
    val blue = Color(0xFF1E5AA8)
    val orange = Color(0xFFF28C28)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF3F8FF),
                            Color.White,
                            Color(0xFFFFF8F0)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 26.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(22.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_ddgky),
                            contentDescription = "DDU-GKY logo",
                            modifier = Modifier
                                .width(112.dp)
                                .height(72.dp),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.width(22.dp))

                        Image(
                            painter = painterResource(id = R.drawable.ic_rseti),
                            contentDescription = "RSETI logo",
                            modifier = Modifier
                                .width(112.dp)
                                .height(72.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(142.dp)
                            .scale(logoScale.value)
                            .background(Color.White, CircleShape)
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.app_icon),
                            contentDescription = "Kaushal Panjee app icon",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "KAUSHAL PANJEE",
                            color = navy,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.1.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(7.dp))

                        Text(
                            text = "A step towards a skilled future",
                            color = Color(0xFF68778D),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_people),
                        contentDescription = "People illustration",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(145.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(30.dp)
                            .alpha(loaderAlpha.value),
                        color = blue,
                        strokeWidth = 3.dp,
                        trackColor = Color(0xFFDCE7F5)
                    )

                    Spacer(modifier = Modifier.height(11.dp))

                    Text(
                        text = "Getting things ready...",
                        color = navy,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Powered by DDU-GKY  •  RSETI",
                        color = orange,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.4.sp
                    )
                }
            }
        }
    }
}
