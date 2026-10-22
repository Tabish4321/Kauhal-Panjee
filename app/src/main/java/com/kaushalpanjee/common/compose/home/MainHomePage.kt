package com.kaushalpanjee.common.compose.home


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.kaushalpanjee.R
import com.kaushalpanjee.common.CertificateActivity
import com.kaushalpanjee.common.CommonViewModel
import com.kaushalpanjee.common.compose.home.HomeActions
import com.kaushalpanjee.common.compose.home.MainHomeRoute
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.databinding.FragmentMainHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainHomePage : BaseFragment<FragmentMainHomeBinding>(FragmentMainHomeBinding::inflate) {

    private val commonViewModel: CommonViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = userPreferences.getUseID()

        binding.homeComposeView.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )
        binding.homeComposeView.setContent {
            MaterialTheme {
                MainHomeRoute(
                    viewModel = commonViewModel,
                    userId = userId,
                    actions = HomeActions(
                        // Jab baaki fragments Compose me aa jaye, bas yahi lambdas badalne hain
                        onCandidateDetails = {
                            findNavController().navigate(MainHomePageDirections.actionMainHomePageToHomeFragment())
                        },
                        onTrainingDetails = { stateCode, schemeType ->
                            findNavController().navigate(
                                MainHomePageDirections.actionMainHomePageToTrainingCenterAssign(stateCode, schemeType)
                            )
                        },
                        onOpenCertificate = { certFlag ->
                            startActivity(
                                Intent(requireContext(), CertificateActivity::class.java)
                                    .putExtra("CERT_URL", certFlag)
                            )
                        },
                        onAttendance = {
                            findNavController().navigate(MainHomePageDirections.actionMainHomePageToAttendanceFragment())
                        },
                        onCbt = {
                            findNavController().navigate(MainHomePageDirections.actionMainHomePageToCbtDetailFragment())
                        },
                        onReKyc = {
                            findNavController().navigate(MainHomePageDirections.actionMainHomePageToReKycFragment())
                        },
                        onChangePassword = {
                            findNavController().navigate(MainHomePageDirections.actionMainHomePageToChangePasswordFragment())
                        },
                        onLanguage = {
                            findNavController().navigate(MainHomePageDirections.actionMainHomePageToTrainingFragment())
                        },
                        onNotification = {
                            findNavController().navigate(MainHomePageDirections.actionMainHomePageToNotificationListFragment())
                        },
                        onSearchResult = { centerCode ->
                            findNavController().navigate(
                                MainHomePageDirections.actionMainHomePageToSearchTrainingFragment(centerCode)
                            )
                        },
                        onSessionExpired = {
                            AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                        },
                        onLogoutSuccess = { msg ->
                            findNavController().navigate(
                                R.id.loginFragment,
                                null,
                                NavOptions.Builder().setPopUpTo(R.id.mainHomePage, true).build()
                            )
                            Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                        },
                        onStateLgdCode = { code ->
                            viewLifecycleOwner.lifecycleScope.launch {
                                userPreferences.updateUserStateLgdCode(null)
                                userPreferences.updateUserStateLgdCode(code)
                            }
                        },
                        onExit = { requireActivity().finish() }
                    )
                )
            }
        }
    }
}
