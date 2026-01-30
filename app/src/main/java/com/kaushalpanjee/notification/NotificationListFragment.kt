package com.kaushalpanjee.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.kaushalpanjee.common.CommonViewModel
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.databinding.FragmentTrainingBinding
import com.kaushalpanjee.notification.with_api.NotificationListScreenN
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Rishi Porwal
 */
@AndroidEntryPoint
class NotificationListFragment :
    BaseFragment<FragmentTrainingBinding>(FragmentTrainingBinding::inflate) {

    private val commonViewModel: CommonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    NotificationListScreenN(
                        navController = findNavController(),
                        commonViewModel = commonViewModel,
                    )
                }
            }
        }
    }
}
