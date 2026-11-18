package com.kaushalpanjee.common.compose.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color as AndroidColor
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.R
import com.kaushalpanjee.common.CertificateActivity
import com.kaushalpanjee.common.CommonViewModel
import com.kaushalpanjee.common.compose.helpdesk.RaiseTicketBottomSheet
import com.kaushalpanjee.common.model.request.CreateTicketRequest
import com.kaushalpanjee.common.model.request.FaceCheckReq
import com.kaushalpanjee.common.model.request.SectionAndPerReq
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.databinding.FragmentMainHomeBinding
import com.nic.faceauth.sdk.FaceAuthSDK
import com.nic.faceauth.sdk.contract.FaceAuthContract
import com.nic.faceauth.sdk.models.FaceAuthResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainHomePage : BaseFragment<FragmentMainHomeBinding>(FragmentMainHomeBinding::inflate) {

    private val commonViewModel: CommonViewModel by activityViewModels()

    private var registeredEmbedding: FloatArray? = null
    private var faceSdkSecure = false
    private var faceRegistrationStarted = false
    private var faceUpdateRequestInProgress = false
    private var ticketRequestInProgress = false
    private var refreshSectionAfterPasswordChange = false
    private var ticketDialog: BottomSheetDialog? = null

    private val faceAuthLauncher =
        registerForActivityResult(FaceAuthContract()) { result: FaceAuthResult ->
            handleFaceAuthResult(result)
        }

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startFaceRegistration()
            } else {
                faceRegistrationStarted = false
                showSnackBar("Camera permission is required for face registration")
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        faceSdkSecure = FaceAuthSDK.init(requireContext())
        if (!faceSdkSecure) {
            Log.e("FaceAuth", "Environment integrity check failed")
            showSnackBar("Security check failed. Please try again.")
        }

        collectFaceUpdateResponse()
        collectCreateTicketResponse()

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
                        onCandidateDetails = {
                            findNavController().navigate(
                                MainHomePageDirections.actionMainHomePageToViewDetailsFragment()
                            )
                        },
                        onTrainingDetails = { stateCode, schemeType ->
                            findNavController().navigate(
                                MainHomePageDirections.actionMainHomePageToTrainingCenterAssign(
                                    stateCode,
                                    schemeType
                                )
                            )
                        },
                        onOpenCertificate = { certFlag ->
                            startActivity(
                                Intent(requireContext(), CertificateActivity::class.java)
                                    .putExtra("CERT_URL", certFlag)
                            )
                        },
                        onAttendance = {
                            findNavController().navigate(
                                MainHomePageDirections.actionMainHomePageToAttendanceFragment()
                            )
                        },
                        onCbt = {
                            findNavController().navigate(
                                MainHomePageDirections.actionMainHomePageToCbtDetailFragment()
                            )
                        },
                        onReKyc = {
                            findNavController().navigate(
                                MainHomePageDirections.actionMainHomePageToReKycFragment()
                            )
                        },
                        onChangePassword = {
                            refreshSectionAfterPasswordChange = true
                            findNavController().navigate(
                                MainHomePageDirections.actionMainHomePageToChangePasswordFragment()
                            )
                        },
                        onLanguage = {
                            findNavController().navigate(
                                MainHomePageDirections.actionMainHomePageToLanguageChangeFragment()
                            )
                        },
                        onNotification = {
                            findNavController().navigate(
                                MainHomePageDirections.actionMainHomePageToNotificationListFragment()
                            )
                        },
                        onSearchResult = { centerCode ->
                            findNavController().navigate(
                                MainHomePageDirections.actionMainHomePageToSearchTrainingFragment(
                                    centerCode
                                )
                            )
                        },
                        onSessionExpired = {
                            AppUtil.showSessionExpiredDialog(
                                findNavController(),
                                requireContext()
                            )
                        },
                        onLogoutSuccess = { msg ->
                            findNavController().navigate(
                                R.id.loginFragment,
                                null,
                                NavOptions.Builder()
                                    .setPopUpTo(R.id.mainHomePage, true)
                                    .build()
                            )
                            Toast.makeText(
                                requireContext(),
                                msg,
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        onStateLgdCode = { code ->
                            viewLifecycleOwner.lifecycleScope.launch {
                                userPreferences.updateUserStateLgdCode(null)
                                userPreferences.updateUserStateLgdCode(code)
                            }
                        },
                        onFaceRegistrationRequired = {
                            if (faceSdkSecure) {
                                checkCameraPermissionAndRegister()
                            } else {
                                showSnackBar("Face registration security check failed.")
                            }
                        },
                        onHelpDesk = {
                            showHelpDeskOptions()
                        },
                        onExit = {
                            requireActivity().finish()
                        }
                    )
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (refreshSectionAfterPasswordChange && isAdded) {
            refreshSectionAfterPasswordChange = false
            commonViewModel.getSecctionAndPerAPI(
                SectionAndPerReq(
                    BuildConfig.VERSION_NAME,
                    userPreferences.getUseID(),
                    AppUtil.getAndroidId(requireContext())
                ),
                AppUtil.getSavedTokenPreference(requireContext())
            )
        }
    }

    // ---------------- Face registration: reused from the old Home Fragment ----------------

    private fun checkCameraPermissionAndRegister() {
        if (faceRegistrationStarted) return

        if (
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            faceRegistrationStarted = true
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            startFaceRegistration()
        }
    }

    private fun startFaceRegistration() {
        if (!isAdded || !faceSdkSecure) {
            faceRegistrationStarted = false
            return
        }

        faceRegistrationStarted = true
        Log.d(
            "FaceAuth",
            "Starting face registration for userId=${userPreferences.getUseID()}"
        )
        FaceAuthSDK.register(faceAuthLauncher)
    }

    private fun handleFaceAuthResult(result: FaceAuthResult) {
        if (!result.isSuccess) {
            faceRegistrationStarted = false
            showSnackBar(result.errorMessage ?: "Face registration failed")
            return
        }

        when (result.eventType) {
            "register" -> {
                val embedding = result.generatedEmbedding
                registeredEmbedding = embedding

                if (embedding == null || embedding.isEmpty()) {
                    faceRegistrationStarted = false
                    showSnackBar("Face embedding not generated")
                    return
                }

                Log.d("FaceAuth", "Face registration successful")
                Log.d("FaceAuth", "Embedding size = ${embedding.size}")

                val faceEmbedding = embedding.joinToString(",")
                faceUpdateRequestInProgress = true
                commonViewModel.updateFaceApi(
                    FaceCheckReq(
                        appVersion = BuildConfig.VERSION_NAME,
                        isFaceRegistered = "Y",
                        loginId = userPreferences.getUseID(),
                        faceEmbedding = faceEmbedding
                    ),
                    AppUtil.getSavedTokenPreference(requireContext())
                )
            }

            "auth" -> {
                Log.d("FaceAuth", "Authentication result received")
                faceRegistrationStarted = false
            }

            else -> {
                faceRegistrationStarted = false
                Log.w("FaceAuth", "Unhandled FaceAuth event: ${result.eventType}")
            }
        }
    }

    private fun collectFaceUpdateResponse() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                commonViewModel.updateFaceApi.collectLatest { resource ->
                    // StateFlow replays its last value when this screen opens.
                    // Ignore that initial/stale value; only handle a request started here.
                    if (!faceUpdateRequestInProgress) return@collectLatest

                    when (resource) {
                        is Resource.Loading -> showProgressBar()
                        is Resource.Error -> {
                            faceUpdateRequestInProgress = false
                            hideProgressBar()
                            faceRegistrationStarted = false
                            showSnackBar("Face registration update failed. Please try again.")
                        }
                        is Resource.Success -> {
                            faceUpdateRequestInProgress = false
                            hideProgressBar()
                            val response = resource.data
                            when (response?.responseCode) {
                                200 -> {
                                    faceRegistrationStarted = false

                                }
                                301 -> {
                                    faceRegistrationStarted = false
                                    showSnackBar("Please update the app from Play Store")
                                }
                                401 -> {
                                    faceRegistrationStarted = false
                                    AppUtil.showSessionExpiredDialog(
                                        findNavController(),
                                        requireContext()
                                    )
                                }
                                else -> {
                                    faceRegistrationStarted = false
                                    showSnackBar(
                                        response?.responseMsg
                                            ?: "Face registration update failed"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ---------------- Helpdesk: existing Raise Ticket + Requester flow ----------------

    private fun showHelpDeskOptions() {
        val optionsDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(
            R.layout.bottom_sheet_helpdesk_options,
            null
        )

        optionsDialog.setContentView(view)

        view.findViewById<LinearLayout>(R.id.llRaiseTicket)
            .setOnClickListener {
                optionsDialog.dismiss()
                showRaiseTicketBottomSheet()
            }

        view.findViewById<LinearLayout>(R.id.llRequester)
            .setOnClickListener {
                optionsDialog.dismiss()
                findNavController().navigate(
                    MainHomePageDirections.actionMainHomePageToHelpDeskFragment()
                )
            }

        view.findViewById<TextView>(R.id.tvCancel)
            .setOnClickListener {
                optionsDialog.dismiss()
            }

        optionsDialog.show()
    }

    private fun showRaiseTicketBottomSheet() {
        ticketDialog = BottomSheetDialog(
            requireContext(),
            R.style.HelpDeskBottomSheetTheme
        ).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }

        val composeView = ComposeView(requireContext())
        composeView.setContent {
            MaterialTheme {
                RaiseTicketBottomSheet(
                    onDismiss = {
                        ticketDialog?.dismiss()
                    },
                    onSubmit = { ticketType, scheme, title, description, attachment ->
                        val ticketTypeId = when (ticketType) {
                            "Technical" -> 1
                            "Functional" -> 2
                            "Grievance" -> 3
                            "Feedback" -> 4
                            else -> 1
                        }

                        val schemeCd = when (scheme) {
                            "DDUGKY" -> "1"
                            "RSETI" -> "2"
                            else -> ""
                        }

                        val roleName = when (scheme) {
                            "DDUGKY" -> "DDUGKY MOBILE HELPDESK"
                            "RSETI" -> "RSETI MOBILE HELPDESK"
                            else -> "DDUGKY MOBILE HELPDESK"
                        }

                        val request = CreateTicketRequest(
                            loginId = userPreferences.getUseID(),
                            roleName = roleName,
                            ticketTypeId = ticketTypeId,
                            schemeCd = schemeCd,
                            issueTitle = title,
                            description = description,
                            ticketPriorityId = 3
                        )

                        val file = attachment?.let { uri ->
                            AppUtil.uriToFile(
                                context = requireContext(),
                                uri = uri
                            )
                        }

                        ticketRequestInProgress = true
                        commonViewModel.createTicket(
                            header = AppUtil.getSavedTokenPreference(requireContext()),
                            createTicketRequest = request,
                            file = file
                        )
                    }
                )
            }
        }

        ticketDialog?.setContentView(composeView)
        ticketDialog?.show()

        ticketDialog?.findViewById<FrameLayout>(
            com.google.android.material.R.id.design_bottom_sheet
        )?.apply {
            setBackgroundColor(AndroidColor.TRANSPARENT)
            background = null
        }

        ticketDialog?.behavior?.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
            isDraggable = false
            isHideable = false
            peekHeight = 0
        }

        ticketDialog?.window?.apply {
            setDimAmount(0.60f)
            setBackgroundDrawable(ColorDrawable(AndroidColor.TRANSPARENT))
        }
    }

    private fun collectCreateTicketResponse() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                commonViewModel.createTicket.collectLatest { resource ->
                    // Do not react to the StateFlow's replayed value on page entry.
                    if (!ticketRequestInProgress) return@collectLatest

                    when (resource) {
                        is Resource.Loading -> showProgressBar()
                        is Resource.Error -> {
                            ticketRequestInProgress = false
                            hideProgressBar()
                            Toast.makeText(
                                requireContext(),
                                "Something went wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is Resource.Success -> {
                            ticketRequestInProgress = false
                            hideProgressBar()
                            val response = resource.data
                            if (response?.responseCode == 200) {
                                Toast.makeText(
                                    requireContext(),
                                    response.responseDesc
                                        ?: "Ticket created successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                ticketDialog?.dismiss()
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    response?.responseDesc ?: "Failed to create ticket",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        ticketDialog?.dismiss()
        ticketDialog = null
        super.onDestroyView()
    }
}

