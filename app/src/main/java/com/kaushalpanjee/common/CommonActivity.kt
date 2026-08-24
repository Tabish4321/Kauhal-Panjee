package com.kaushalpanjee.common

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.provider.Settings
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.d2k.samiksha.SamikshaSdk
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.R
import com.kaushalpanjee.common.model.request.LogoutRequest
import com.kaushalpanjee.core.basecomponent.BaseActivity
import com.kaushalpanjee.core.util.AppConstant
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.databinding.ActivityCommonBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import kotlin.system.exitProcess
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.kaushalpanjee.core.util.AppConstant.Constants.SESSION_TIMEOUT
import com.kaushalpanjee.core.util.AppUtil.showUpdateDialog
import com.kaushalpanjee.core.util.Resource
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommonActivity : BaseActivity<ActivityCommonBinding>(ActivityCommonBinding::inflate) {

    private var navController: NavController? = null
    private val commonViewModel: CommonViewModel by viewModels()
    private var isFirstLaunch = true



    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //printSslPin()

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION


        collectLogoutResponse()



        if (AppUtil.getLoginStatus(this)) {

            AppConstant.SessionTimeoutManager.start {
                runOnUiThread {
                    autoLogout()
                }
            }

        } else {

            AppConstant.SessionTimeoutManager.stop()

        }



        // ===========================
        // Security Checks
        // ===========================

           when {
             /*  !AppSignatureVerifier.isSignatureValid(this) -> {
                   showSecurityWarning(
                       "Application integrity verification failed. This application appears to have been modified or re-signed and cannot be trusted."
                   )
                   return
               }*/
               isDeviceRooted() -> {
                   showSecurityWarning(
                       "Rooted device detected! For security reasons, this application cannot run on rooted devices."
                   )
                   return
               }

               isRunningOnEmulator() -> {
                   showSecurityWarning(
                       "Emulator detected! This application cannot run on emulators."
                   )
                   return
               }

            /*   isDeveloperModeEnabled(this) -> {
                   showSecurityWarning(
                       "Developer Options or USB Debugging is enabled. Please disable it to continue."
                   )
                   return
               }*/
        }






        SamikshaSdk.init( this,
            baseUrl = "https://samikshaapi.nabard.org/",
            apiKey = "624f2281-b0f1-44e3-9d3e-24826a53e7a6",
            calledFrom = "NABSKILL",
            apiVersion = "2",
            onFailure = { msg ->
              //  Toast.makeText( this@CommonActivity, msg, Toast.LENGTH_SHORT ).show()
                        },
            onSuccess = {
              //  Toast.makeText( this@CommonActivity, "Samiksha SDK initialized successfully", Toast.LENGTH_SHORT ).show()
        }
        )


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navGraphHost) as NavHostFragment
        navController = navHostFragment.navController
        val graphInflater = navHostFragment.navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.nav_graph)
        val isLoggedIn = AppUtil.getLoginStatus(this)
        if (isLoggedIn) {
            navGraph.setStartDestination(R.id.mainHomePage)
        } else {
            navGraph.setStartDestination(R.id.loginFragment)
//            navGraph.setStartDestination(R.id.cbtFragment)
        }

        navController?.graph = navGraph

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                101
            )
        }

        handleNotificationIntent(intent)


    }




    private fun handleNotificationIntent() {
        // Always check the current intent
        val intent = intent
        Log.d("NOTI_DEBUG", "Checking intent in onResume: ${intent.extras}")

        // Check if we came from notification (you need to set this flag in the notification)
        val openNotification = intent.getBooleanExtra("OPEN_NOTIFICATION_LIST", false)

        Log.d("NOTI_DEBUG", "OPEN_NOTIFICATION_LIST = $openNotification")

        // Also check SharedPreferences as a backup method
        val prefs = getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
        val shouldOpenNotification = prefs.getBoolean("SHOULD_OPEN_NOTIFICATION", false)

        if (intent.getBooleanExtra("OPEN_NOTIFICATION_LIST", false) || shouldOpenNotification) {
            Log.d("NOTI_DEBUG", "Opening notification list")

            // if (openNotification && navController != null) {
            // Clear the flag immediately to prevent re-triggering
            intent.removeExtra("OPEN_NOTIFICATION_LIST")
            prefs.edit().putBoolean("SHOULD_OPEN_NOTIFICATION", false).apply()

            // Check if user is logged in
            val isLoggedIn = AppUtil.getLoginStatus(this)

            // Use handler to ensure navigation happens after UI is ready
            Handler(Looper.getMainLooper()).postDelayed({
                if (isLoggedIn) {
                    navController?.navigate(
                        R.id.notificationListFragment,
                        null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.mainHomePage, false)
                            .build()
                    )
                } else {
                    val bundle = Bundle().apply {
                        putBoolean("redirect_to_notifications", true)
                    }
                    navController?.navigate(
                        R.id.loginFragment,
                        bundle,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.loginFragment, true)
                            .build()
                    )
                }
            }, 300)
        }
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        //   setIntent(intent)
        handleNotificationIntent(intent)
    }



    private fun handleNotificationIntent(intent: Intent?) {
        if (intent?.getBooleanExtra("OPEN_NOTIFICATION_LIST", false) == true) {
            intent.removeExtra("OPEN_NOTIFICATION_LIST")

            window.decorView.post { navController?.navigate(R.id.notificationListFragment)
            }
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

        if (paths.any { File(it).exists() })
            return true

        return Build.TAGS?.contains("test-keys") == true
    }


    private fun isRunningOnEmulator(): Boolean {

        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.BRAND.startsWith("generic")
                || Build.DEVICE.startsWith("generic")
                || Build.PRODUCT.contains("sdk")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
    }


    private fun isDeveloperModeEnabled(activity: Activity): Boolean {
        return try {
            val devOptions = Settings.Global.getInt(
                activity.contentResolver,
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0
            ) == 1
            val adbEnabled = Settings.Global.getInt(
                activity.contentResolver,
                Settings.Global.ADB_ENABLED, 0
            ) == 1
            devOptions || adbEnabled
        } catch (e: Exception) {
            false
        }
    }


    private fun showSecurityWarning(message: String) {
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

    override fun onDestroy() {
        AppConstant.SessionTimeoutManager.stop()
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()

        if (isFirstLaunch) {
            isFirstLaunch = false
            return
        }

        val lastTime = AppUtil.getBackgroundTime(this)

        if (
            AppUtil.getLoginStatus(this) &&
            lastTime != 0L &&
            System.currentTimeMillis() - lastTime >= SESSION_TIMEOUT
        ) {
            autoLogout()
        }
    }

    override fun onStop() {
        super.onStop()
        AppUtil.saveBackgroundTime(this, System.currentTimeMillis())
    }


    private fun autoLogout() {
        commonViewModel.getLogout(
            LogoutRequest(
                BuildConfig.VERSION_NAME,
                AppUtil.getAndroidId(this),
                userPreferences.getUseID()
            ),
            AppUtil.getSavedTokenPreference(this)
        )


    }

    private fun collectLogoutResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getLogout) {

                when (it) {

                    is Resource.Loading -> {
                        showProgress()
                    }

                    is Resource.Error -> {
                        hideProgress()

                        navController?.let {
                            AppUtil.showSessionExpiredDialog(
                                it,
                                this@CommonActivity
                            )
                        }
                    }

                    is Resource.Success -> {

                        hideProgress()

                        when (it.data?.responseCode) {

                            200 -> {

                                AppUtil.saveLoginStatus(this@CommonActivity, false)

                                navController?.let {
                                    AppUtil.showSessionExpiredDialog(
                                        it,
                                        this@CommonActivity
                                    )
                                }

                            }

                            301 -> {
                                showUpdateDialog(this@CommonActivity)
                            }

                            401 -> {

                                navController?.let {
                                    AppUtil.showSessionExpiredDialog(
                                        it,
                                        this@CommonActivity
                                    )
                                }
                            }

                            404 -> {

                                navController?.let {
                                    AppUtil.showSessionExpiredDialog(
                                        it,
                                        this@CommonActivity
                                    )
                                }
                            }

                            else -> {

                                navController?.let {
                                    AppUtil.showSessionExpiredDialog(
                                        it,
                                        this@CommonActivity
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
