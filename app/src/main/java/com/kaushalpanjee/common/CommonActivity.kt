package com.kaushalpanjee.common

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.d2k.samiksha.SamikshaSdk
import com.kaushalpanjee.R
import com.kaushalpanjee.core.basecomponent.BaseActivity
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.AppUtil.printSslPin
import com.kaushalpanjee.databinding.ActivityCommonBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommonActivity : BaseActivity<ActivityCommonBinding>(ActivityCommonBinding::inflate) {

    private var navController: NavController? = null

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //printSslPin()

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION




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

//    override fun onStart() {
//        super.onStart()
//        handleNotificationIntent()
//    }

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
//    override fun onNewIntent(intent: Intent) {
//        super.onNewIntent(intent)
//        setIntent(intent)
//        //handleNotificationIntent(intent)
//        Log.d("NOTI_DEBUG", "onNewIntent called with: ${intent.extras}")
//    }

//    override fun onResume() {
//        super.onResume()
//        handleNotificationIntent()
//
//    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        //   setIntent(intent)
        handleNotificationIntent(intent)
    }

//    private fun handleNotificationIntent(intent: Intent?) {
//
//        val openNotification =
//            intent?.getBooleanExtra("OPEN_NOTIFICATION_LIST", false) ?: false
//
//        Log.d("NOTI_DEBUG", "OPEN_NOTIFICATION_LIST = $openNotification")
//
//        if (openNotification) {
//            intent!!.removeExtra("OPEN_NOTIFICATION_LIST")
//
//            window.decorView.post {
//                navController?.navigate(R.id.notificationListFragment)
//            }
//        }
//
//    }

    private fun handleNotificationIntent(intent: Intent?) {
        if (intent?.getBooleanExtra("OPEN_NOTIFICATION_LIST", false) == true) {
            intent.removeExtra("OPEN_NOTIFICATION_LIST")

            window.decorView.post { navController?.navigate(R.id.notificationListFragment)
            }
        }

    }



}
