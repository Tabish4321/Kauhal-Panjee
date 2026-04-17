package com.kaushalpanjee.common

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import com.kaushalpanjee.common.compose.SubmitBankConsentScreen
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.databinding.SubmitconscentFragmentBinding


class SubmitBankConcent : BaseFragment<SubmitconscentFragmentBinding>(SubmitconscentFragmentBinding::inflate) {


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        requireActivity().window.insetsController?.hide(
            android.view.WindowInsets.Type.statusBars()
        )


        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    SubmitBankConsentScreen()
                }
            }
        }
    }

}