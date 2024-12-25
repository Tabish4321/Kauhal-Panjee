package com.kaushalpanjee

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.AnticipateInterpolator
import android.view.animation.Interpolator
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.kaushalpanjee.common.CommonActivity
import com.kaushalpanjee.core.basecomponent.BaseActivity
import com.kaushalpanjee.databinding.ActivityWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class WelcomeActivity : BaseActivity<ActivityWelcomeBinding>(
    ActivityWelcomeBinding::inflate){
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

      // installSplashScreen()

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
       /* window.statusBarColor = resources.getColor(android.R.color.transparent)*/


        lifecycleScope.launch {
            delay(3000)
            navigate()
        }

    }

    private fun navigate(){
        startActivity(Intent(this@WelcomeActivity, CommonActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }



}