package com.kaushalpanjee.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.R
import com.kaushalpanjee.common.model.request.ChangePassReq
import com.kaushalpanjee.compose.presentation.viewmodel.ChangePasswordViewModel
import com.kaushalpanjee.compose.ui.changePassword.ChangePasswordRoute
import com.kaushalpanjee.compose.ui.screen.ChangePasswordScreen
import com.kaushalpanjee.compose.ui.screen.NotificationScreen
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.AESCryptography
import com.kaushalpanjee.core.util.AppConstant
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.core.util.UserPreferences
import com.kaushalpanjee.core.util.log
import com.kaushalpanjee.core.util.onRightDrawableClicked
import com.kaushalpanjee.core.util.setRightDrawablePassword
import com.kaushalpanjee.core.util.toastShort
import com.kaushalpanjee.databinding.ChangePassFragmentBinding
import kotlinx.coroutines.launch

class ChangePasswordFragment : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
           val nav : NavController =findNavController()

            return ComposeView(requireContext()).apply {
                setContent {
                        ChangePasswordRoute(
                            onNavigateHome={ nav.popBackStack()}
                        )
                }
            }
        }
    }
