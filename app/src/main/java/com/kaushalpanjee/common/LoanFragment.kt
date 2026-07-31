package com.kaushalpanjee.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.kaushalpanjee.common.compose.LoanStepperScreen
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.databinding.LoanFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class LoanFragment : BaseFragment<LoanFragmentBinding>(LoanFragmentBinding::inflate) {

    private val commonViewModel: CommonViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    LoanStepperScreen(
                        onBack = {
                          //  requireContext().finish()
                        },
                        viewModel = commonViewModel,
                        requireContext()
                    )
                }
            }
        }
    }


}