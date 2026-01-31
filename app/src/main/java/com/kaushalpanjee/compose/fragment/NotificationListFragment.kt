package com.kaushalpanjee.compose.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.findNavController
import com.kaushalpanjee.compose.ui.screen.NotificationScreen
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.databinding.FragmentTrainingBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Rishi Porwal
 */
@AndroidEntryPoint
class NotificationListFragment :
    BaseFragment<FragmentTrainingBinding>(FragmentTrainingBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    NotificationScreen(
                        navController = findNavController(),
                    )
                }
            }
        }
    }
}