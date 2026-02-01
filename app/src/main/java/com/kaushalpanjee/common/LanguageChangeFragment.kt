package com.kaushalpanjee.common

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.kaushalpanjee.common.model.MultiLanguage
import com.kaushalpanjee.compose.ui.language_change.LanguageChangeRoute
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.UserPreferences
import com.kaushalpanjee.core.util.gone
import com.kaushalpanjee.core.util.visible
import com.kaushalpanjee.databinding.FragmentLanguageChangeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class LanguageChangeFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                LanguageChangeRoute(
                    onBack = { findNavController().navigateUp() },
                    onApplyLanguage = { code ->
                        lifecycleScope.launch {
                            AppUtil.changeAppLanguage(requireContext(), code)
                            AppUtil.saveLanguagePreference(requireContext(), code)
                            findNavController().navigateUp()
                        }
                    }
                )
            }
        }
    }
}





