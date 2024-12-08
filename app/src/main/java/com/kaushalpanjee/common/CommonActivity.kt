package com.kaushalpanjee.common

import android.os.Bundle
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
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true

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