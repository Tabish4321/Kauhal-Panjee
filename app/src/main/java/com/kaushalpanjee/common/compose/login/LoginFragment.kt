package com.kaushalpanjee.common.compose.login

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.R
import com.kaushalpanjee.common.CommonViewModel
import com.kaushalpanjee.common.model.request.LoginReq
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.core.util.toastShort
import com.kaushalpanjee.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@AndroidEntryPoint
class LoginFragment :
    BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val commonViewModel: CommonViewModel by activityViewModels()

    private var isApiCalled = false
    private var userName = ""
    private var password = ""
    private var saltPassword = ""

    private var loginIdState by mutableStateOf("")
    private var passwordState by mutableStateOf("")
    private var loadingState by mutableStateOf(false)
    private var errorState by mutableStateOf("")
    private var languageState by mutableStateOf("en")

    var ddugky: String? = ""
    var rseti: String? = ""
    var nrlm: String? = ""
    var pmvishwakarma: String? = ""
    var pmkvy: String? = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Screen must open without showing a loader.
        loadingState = false
        errorState = ""
        isApiCalled = false

        val savedLanguage =
            AppUtil.getSavedLanguagePreference(requireContext())

        if (savedLanguage.contains("en")) {
            AppUtil.saveLanguagePreference(requireContext(), "en")
        } else {
            AppUtil.changeAppLanguage(
                requireContext(),
                savedLanguage
            )
        }

        binding.loginComposeView.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )

        binding.loginComposeView.setContent {
            MaterialTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 24.dp)
                ) {
                    LoginScreen(
                        loginId = loginIdState,
                        password = passwordState,
                        isLoading = loadingState,
                        errorMessage = errorState,
                        currentLanguage = languageState,
                        version = BuildConfig.VERSION_NAME,

                        onLoginIdChanged = {
                            loginIdState = it
                        },

                        onPasswordChanged = {
                            passwordState = it
                        },

                        onLogin = {
                            performLogin(loginIdState, passwordState)
                        },

                        onRegister = {
                            findNavController().navigate(
                                LoginFragmentDirections
                                    .actionLoginFragmentToRegisterFragment()
                            )
                        },

                        onForgotPassword = {
                            findNavController().navigate(
                                LoginFragmentDirections
                                    .actionLoginFragmentToForgotPassViaAadhaarFragment()
                            )
                        },

                        onAboutUnnati = {
                            findNavController().navigate(
                                LoginFragmentDirections
                                    .actionLoginFragmentToAboutUnnatiFragment(
                                        ddugky.orEmpty(),
                                        rseti.orEmpty(),
                                        nrlm.orEmpty(),
                                        pmvishwakarma.orEmpty(),
                                        pmkvy.orEmpty()
                                    )
                            )
                        },

                        onLanguageSelected = {
                            findNavController().navigate(
                                R.id.action_loginFragment_to_languageChangeFragment
                            )
                        }
                    )
                }
            }
        }
        // Collect both flows once; do not start collectors on button clicks.
        collectTokenResponse()
        collectLoginResponse()
        handleBackPress()
    }

    override fun onResume() {
        super.onResume()

        WindowCompat.setDecorFitsSystemWindows(
            requireActivity().window,
            false
        )

        WindowInsetsControllerCompat(
            requireActivity().window,
            requireActivity().window.decorView
        ).hide(WindowInsetsCompat.Type.statusBars())
    }

    override fun onPause() {
        if (isAdded) {
            WindowInsetsControllerCompat(
                requireActivity().window,
                requireActivity().window.decorView
            ).show(WindowInsetsCompat.Type.statusBars())

            WindowCompat.setDecorFitsSystemWindows(
                requireActivity().window,
                true
            )
        }

        super.onPause()
    }

    private fun performLogin(id: String, enteredPassword: String) {
        if (isApiCalled) {
            Log.d("LOGIN_FLOW", "Login already in progress; duplicate click ignored")
            return
        }

        if (id.isBlank() || enteredPassword.isBlank()) {
            errorState = "Please enter ID and password"
            return
        }

        errorState = ""
        userName = id.trim()
        password = enteredPassword
        saltPassword = ""
        isApiCalled = true
        loadingState = true


        // Token API is called only after Login is clicked.
        commonViewModel.getToken(
            AppUtil.getAndroidId(requireContext()),
            BuildConfig.VERSION_NAME,
            userName
        )
    }

    private fun submitLoginAfterToken() {
        if (!isAdded || !isApiCalled) return

        // Keep the existing SHA-512 password logic unchanged.
        val shaPass = AppUtil.sha512Hash(password)
        val finalPass = AppUtil.sha512Hash(saltPassword + shaPass)


        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!isAdded || !isApiCalled) return@addOnCompleteListener

                val fcmToken =
                    if (task.isSuccessful) task.result.orEmpty() else ""

                val request = LoginReq(
                    userName,
                    finalPass,
                    AppUtil.getAndroidId(requireContext()),
                    BuildConfig.VERSION_NAME,
                    "",
                    fcmToken = fcmToken
                )

                commonViewModel.getLoginAPI(request)
            }
    }

    private fun collectTokenResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getToken) { result ->
                when (result) {
                    is Resource.Loading -> {
                        // Ignore any old/replayed Loading event when idle.
                        if (isApiCalled) {
                            loadingState = true
                            Log.d("TOKEN_API", "generateToken loading")
                        }
                    }

                    is Resource.Error -> {
                        if (!isApiCalled) return@collectLatestLifecycleFlow

                        Log.e(
                            "TOKEN_API",
                            "generateToken failed: ${result.error?.message}"
                        )

                        errorState =
                            result.error?.message ?: "Unable to generate token"

                        loadingState = false
                        isApiCalled = false
                    }

                    is Resource.Success -> {
                        if (!isApiCalled) return@collectLatestLifecycleFlow

                        val response = result.data

                        if (response == null) {
                            errorState = "Internal Server Error"
                            loadingState = false
                            isApiCalled = false
                            return@collectLatestLifecycleFlow
                        }

                        Log.d("TOKEN_API", "Response code: ${response.responseCode}")
                        Log.d(
                            "TOKEN_API",
                            "Response description: ${response.responseDesc}"
                        )

                        when (response.responseCode) {
                            200 -> {
                                saltPassword = response.passString.orEmpty()

                                if (saltPassword.isBlank()) {
                                    errorState = "Unable to generate password token"
                                    loadingState = false
                                    isApiCalled = false
                                } else {
                                    submitLoginAfterToken()
                                }
                            }

                            301 -> {
                                loadingState = false
                                isApiCalled = false
                                showUpdateDialog()
                            }

                            else -> {
                                errorState = response.responseDesc
                                    .orEmpty()
                                    .ifBlank { "Unable to generate token" }

                                loadingState = false
                                isApiCalled = false
                            }
                        }
                    }
                }
            }
        }
    }

    private fun collectLoginResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getLoginAPI) { result ->
                when (result) {
                    is Resource.Loading -> {
                        // Only the Compose button shows the loading indicator.
                        if (isApiCalled) {
                            loadingState = true
                            Log.d("LOGIN_API", "Login request loading")
                        }
                    }

                    is Resource.Error -> {
                        if (!isApiCalled) return@collectLatestLifecycleFlow

                        Log.e(
                            "LOGIN_API",
                            "Login request failed: ${result.error?.message}"
                        )

                        errorState = result.error?.message ?: "Server error"
                        loadingState = false
                        isApiCalled = false
                    }

                    is Resource.Success -> {
                        if (!isApiCalled) return@collectLatestLifecycleFlow

                        val response = result.data

                        if (response == null) {
                            errorState = "Internal Server Error"
                            loadingState = false
                            isApiCalled = false
                            return@collectLatestLifecycleFlow
                        }

                        Log.d("LOGIN_API", "Response code: ${response.responseCode}")
                        Log.d(
                            "LOGIN_API",
                            "Response description: ${response.responseDesc}"
                        )

                        when (response.responseCode) {
                            200 -> {
                                loadingState = false
                                isApiCalled = false

                                AppUtil.saveTokenPreference(
                                    requireContext(),
                                    "Bearer " + response.accessToken
                                )

                                userPreferences.updateUserId(null)
                                userPreferences.updateUserId(userName)
                                AppUtil.saveLoginStatus(requireContext(), true)

                                findNavController().navigate(
                                    R.id.mainHomePage,
                                    null,
                                    NavOptions.Builder()
                                        .setPopUpTo(R.id.loginFragment, true)
                                        .build()
                                )
                            }

                            203 -> {
                                errorState = response.responseDesc.orEmpty()
                                    .ifBlank { "Please try logging in again." }

                                response.responseMsg?.let { toastShort(it) }

                                loadingState = false
                                isApiCalled = false
                            }

                            301 -> {
                                loadingState = false
                                isApiCalled = false
                                showUpdateDialog()
                            }

                            else -> {
                                errorState = response.responseDesc.orEmpty()
                                    .ifBlank {
                                        "Unable to login. Please try again."
                                    }

                                loadingState = false
                                isApiCalled = false
                            }
                        }
                    }
                }
            }
        }
    }

    private fun handleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                private var lastBackPressed = 0L

                override fun handleOnBackPressed() {
                    val now = System.currentTimeMillis()

                    if (now - lastBackPressed < 2000) {
                        isEnabled = false
                        requireActivity().finish()
                    } else {
                        lastBackPressed = now
                        showSnackBar("Press back again to exit")
                    }
                }
            }
        )
    }

    private fun showUpdateDialog() {
        val packageName = "com.kaushalpanjee"

        AlertDialog.Builder(requireContext())
            .setTitle("Update Available")
            .setMessage(
                "A new version of the app is available. Please update to continue."
            )
            .setPositiveButton("Update") { dialog, _ ->
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$packageName")
                        ).setPackage("com.android.vending")
                    )
                } catch (_: Exception) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(
                                "https://play.google.com/store/apps/details?id=$packageName&hl=en_IN"
                            )
                        )
                    )
                }

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }
}
