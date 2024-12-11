package com.kaushalpanjee

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AnticipateInterpolator
import android.view.animation.Interpolator
import android.widget.ImageView
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

      // installSplashScreen()

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true

       /* splashScreen.setOnExitAnimationListener {splashScreenView->


            splashScreenView.rootView.post {
                val slideUp = ObjectAnimator.ofFloat(
                    splashScreenView,
                    View.TRANSLATION_Y,
                    0f,
                    -splashScreenView.height.toFloat()
                ).apply {
                    interpolator = AnticipateInterpolator()
                    duration = 100L
                    doOnEnd {
                        splashScreenView.remove()
                        startActivity(Intent(this@WelcomeActivity, CommonActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        })
                    }
                }
                slideUp.start()
            }

        }*/


        animateImageFromBottomToCenter(
            imageView = binding.ivImage,
            duration = 5000L, // Animation duration in milliseconds
            interpolator = android.view.animation.AccelerateDecelerateInterpolator(), // Optional interpolator
            onAnimationComplete = {
              navigate()
            }
        )

    }

    private fun navigate(){
        startActivity(Intent(this@WelcomeActivity, CommonActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }


    private fun animateImageFromBottomToCenter(
        imageView: ImageView,
        duration: Long = 1700L,
        interpolator: Interpolator? = null,
        onAnimationComplete: (() -> Unit)? = null
    ) {
        // Get screen height
        val screenHeight = imageView.context.resources.displayMetrics.heightPixels

        // Calculate start and end positions
        val startY = screenHeight.toFloat() // Image starts at the bottom of the screen
        val centerY = (screenHeight/32  - imageView.height/16).toFloat() // End position at the center

        // Move the ImageView to the starting position (offscreen bottom)
        imageView.translationY = startY

        // Create the ObjectAnimator
        val animator = ObjectAnimator.ofFloat(imageView, "translationY", startY, centerY)
        animator.duration = duration

        // Set interpolator if provided
        interpolator?.let {
            animator.interpolator = it
        }

        // Add a listener for animation completion
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                onAnimationComplete?.invoke()
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })

        // Start the animation
        animator.start()
    }


}