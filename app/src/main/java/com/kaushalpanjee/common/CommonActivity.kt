package com.kaushalpanjee.common

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.kaushalpanjee.R
import com.kaushalpanjee.core.basecomponent.BaseActivity
import com.kaushalpanjee.databinding.ActivityCommonBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommonActivity : BaseActivity<ActivityCommonBinding>(ActivityCommonBinding::inflate) {


    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide the status bar based on API level
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // API 30 and above
         window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            // For older versions
            @Suppress("DEPRECATION")
          window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navGraphHost) as NavHostFragment
        navController = navHostFragment.navController
        val graphInflater = navHostFragment.navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.nav_graph)

        navGraph.setStartDestination(R.id.loginFragment)

        navController?.let {
            it.graph = navGraph
        }

    }




}