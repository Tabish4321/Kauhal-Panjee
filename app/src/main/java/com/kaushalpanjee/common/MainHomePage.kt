package com.kaushalpanjee.common

import AdvertiseCardAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.kaushalpanjee.R
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.databinding.FragmentMainHomeBinding

class MainHomePage : BaseFragment<FragmentMainHomeBinding>(FragmentMainHomeBinding::inflate) {


    private lateinit var adapter: AdvertiseCardAdapter
    private val handler = Handler()
    private var scrollPosition = 0

    val imageList = listOf(
        R.drawable.banner,
        R.drawable.banner,
        R.drawable.banner,
        R.drawable.banner,
        R.drawable.banner,
        R.drawable.banner
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }
     private fun init(){
         listeners()
         autoScroll()


     }

 private fun  listeners(){


     binding.changeLanguage.setOnClickListener {
         findNavController().navigate(MainHomePageDirections.actionMainHomePageToLanguageChangeFragment())
     }
     binding.personalImageLogo.setOnClickListener {

         findNavController().navigate(MainHomePageDirections.actionMainHomePageToHomeFragment())
     }
     val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
     binding.recyclerView.layoutManager = layoutManager

     // Set up the adapter with dummy data
     adapter = AdvertiseCardAdapter(imageList)
     binding.recyclerView.adapter = adapter
     // Start auto-scrolling
 }


    private fun autoScroll(){
        val scrollRunnable = Runnable {
            // Scroll to the next position
            if (scrollPosition < adapter.itemCount) {
                binding.recyclerView.smoothScrollToPosition(scrollPosition)
                scrollPosition++
            }

            if (scrollPosition >= adapter.itemCount) {
                // Reset scroll position if we reach the end of the list
                scrollPosition = 0
            }
        }

        // Post the scrolling task initially and periodically every 2 seconds
        handler.postDelayed(object : Runnable {
            override fun run() {
                scrollRunnable.run()
                handler.postDelayed(this, 3000) // Scroll every 2 seconds
            }
        }, 0) // Delay the first scroll action by 2 seconds
    }

    // Cleanup handler to stop auto-scrolling when activity is destroyed
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null) // Remove all pending posts
    }





}
