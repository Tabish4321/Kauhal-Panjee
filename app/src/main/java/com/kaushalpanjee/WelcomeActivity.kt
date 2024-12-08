package com.kaushalpanjee

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AnticipateInterpolator
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.kaushalpanjee.common.CommonActivity
import com.kaushalpanjee.core.basecomponent.BaseActivity
import com.kaushalpanjee.databinding.ActivityWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WelcomeActivity : BaseActivity<ActivityWelcomeBinding>(
    ActivityWelcomeBinding::inflate){
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true

        splashScreen.setOnExitAnimationListener {splashScreenView->


            splashScreenView.rootView.post {
                val slideUp = ObjectAnimator.ofFloat(
                    splashScreenView,
                    View.TRANSLATION_Y,
                    0f,
                    -splashScreenView.height.toFloat()
                ).apply {
                    interpolator = AnticipateInterpolator()
                    duration = 1000L
                    doOnEnd {
                        splashScreenView.remove()
                        startActivity(Intent(this@WelcomeActivity, CommonActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        })
                    }
                }
                slideUp.start()
            }

        }

    }
}