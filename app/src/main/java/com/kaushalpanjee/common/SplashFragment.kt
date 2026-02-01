package com.kaushalpanjee.common

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.kaushalpanjee.R
import com.kaushalpanjee.core.util.AppUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.activity_welcome) {

    companion object {
        private const val TAG = "SplashFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "🚀 SplashFragment started")

        // Wait 2 seconds then check
        view.postDelayed({
            checkAndNavigate()
        }, 2000)
    }

    private fun checkAndNavigate() {
        Log.d(TAG, "🔍 Checking where to navigate...")

        // FIRST priority: Check if notification was clicked
        val notificationClicked = AppUtil.consumeNotificationClicked(requireContext())

        if (notificationClicked) {
            Log.d(TAG, "✅ Navigating to notification list (from notification)")

            // Clear any old data
            AppUtil.clearNotificationData(requireContext())

            findNavController().navigate(
                R.id.notificationListFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.navHostFragment, true)
                    .build()
            )
            return
        }

        // SECOND: Check intent from activity (in case SharedPreferences failed)
        val activity = requireActivity() as? CommonActivity
        val intent = activity?.intent
        val fromIntent = intent?.getBooleanExtra("from_notification", false) == true ||
                intent?.action == "NOTIFICATION_CLICK" ||
                intent?.hasExtra("google.message_id") == true

        if (fromIntent) {
            Log.d(TAG, "✅ Navigating to notification list (from intent)")

            findNavController().navigate(
                R.id.notificationListFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.navHostFragment, true)
                    .build()
            )
            return
        }

        // NORMAL flow: Check login status
        val isLoggedIn = AppUtil.getLoginStatus(requireContext())
        val destination = if (isLoggedIn) {
            R.id.mainHomePage
        } else {
            R.id.loginFragment
        }

        Log.d(TAG, "📱 Normal flow - isLoggedIn: $isLoggedIn, destination: $destination")

        findNavController().navigate(
            destination,
            null,
            NavOptions.Builder()
                .setPopUpTo(R.id.navHostFragment, true)
                .build()
        )
    }
}