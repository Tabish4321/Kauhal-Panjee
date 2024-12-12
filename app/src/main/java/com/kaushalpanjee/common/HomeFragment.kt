package com.kaushalpanjee.common

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import androidx.core.animation.addListener
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.gone
import com.kaushalpanjee.core.util.visible
import com.kaushalpanjee.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private var isSeccVisible = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        listener()
    }

    private fun listener() {

        binding.ivSeccArrow.setOnClickListener {

            if (isSeccVisible){
                isSeccVisible = false
                binding.clSecc.gone()
                binding.viewSecc.gone()
            }else {
                isSeccVisible = true
                binding.clSecc.visible()
                binding.viewSecc.visible()
            }


         //   animateExpandCollapse(binding.clSecc, isSeccVisible)


        }

    }

 private   fun animateExpandCollapse(
        view: View,
        isExpanding: Boolean,
        duration: Long = 300
    ) {
        // Get the current and target heights
        val initialHeight = if (isExpanding) 0 else view.measuredHeight
        val targetHeight = if (isExpanding) view.measuredHeight else 0

        // Set the initial height for collapsing
        if (!isExpanding) {
            view.measure(
                View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.UNSPECIFIED
            )
        }

        // Create ValueAnimator
        val animator = ValueAnimator.ofInt(initialHeight, targetHeight)
        animator.interpolator = android.view.animation.AccelerateDecelerateInterpolator()
        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            val layoutParams = view.layoutParams
            layoutParams.height = animatedValue
            view.layoutParams = layoutParams
        }

        animator.duration = duration
        animator.start()

        // Toggle visibility after animation
        animator.addListener(onEnd = {
            view.visibility = if (isExpanding) View.VISIBLE else View.GONE
        })
    }

}