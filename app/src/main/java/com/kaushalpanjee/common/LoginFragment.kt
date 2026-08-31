package com.kaushalpanjee.common

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.R
import com.kaushalpanjee.common.model.request.LoginReq
import com.kaushalpanjee.common.model.request.UnnatiRequest
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.AESCryptography
import com.kaushalpanjee.core.util.AppConstant
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.core.util.log
import com.kaushalpanjee.core.util.onRightDrawableClicked
import com.kaushalpanjee.core.util.setRightDrawablePassword
import com.kaushalpanjee.core.util.toastShort
import com.kaushalpanjee.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private var showPassword = true
    private var isApiCalled = false

    private val commonViewModel: CommonViewModel by activityViewModels()


    private var userName = ""
    private var password = ""
    private var token = ""
    private var saltPassword = ""

    var ddugky : String? = ""
    var rseti : String? = ""
    var nrlm : String? = ""
    var pmvishwakarma : String? = ""
    var pmkvy : String? = ""



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)







        init()
        handleBackPress()


    }



    private fun init() {
        listeners()
        collectTokenResponse()
    }


    private fun listeners() {


     /*   binding.ivDDGKY.setOnClickListener {

          findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSubmitBankConcent())
        }
*/









        binding.tvVersion.text= "V-"+BuildConfig.VERSION_NAME

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.isNotEmpty() && !isApiCalled) {
                        isApiCalled = true
                        commonViewModel.getToken(AppUtil.getAndroidId(requireContext()), BuildConfig.VERSION_NAME,binding.etEmail.text.toString())
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())

        }

        binding.tvAboutUnnati.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToAboutUnnatiFragment(
                ddugky!!,
                rseti!!,
                nrlm!!,
                pmvishwakarma!!,
                pmkvy!!
            )
            findNavController().navigate(action)
        }


        binding.changeLanguage.setOnClickListener {
            findNavController().navigate(MainHomePageDirections.actionMainHomePageToLanguageChangeFragment())

        }

// Disable long-press (prevents copy-paste menu)
        binding.etPassword.setOnLongClickListener { true }

// Prevents context menu actions (copy, cut, paste)
        binding.etPassword.customSelectionActionModeCallback = object : android.view.ActionMode.Callback {
            override fun onCreateActionMode(mode: android.view.ActionMode?, menu: android.view.Menu?): Boolean = false
            override fun onPrepareActionMode(mode: android.view.ActionMode?, menu: android.view.Menu?): Boolean = false
            override fun onActionItemClicked(mode: android.view.ActionMode?, item: android.view.MenuItem?): Boolean = false
            override fun onDestroyActionMode(mode: android.view.ActionMode?) {}
        }

// Disable clipboard pasting, but allow normal keyboard inputs
        binding.etPassword.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val clipboard = v.context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                clipboard.setPrimaryClip(android.content.ClipData.newPlainText("", "")) // Clear clipboard
            }
        }

// Disable drag-and-drop text pasting
        binding.etPassword.setOnDragListener { _, _ -> true }

// Prevent programmatic clipboard pasting
        binding.etPassword.setTextIsSelectable(false) // Prevents text selection
        binding.etPassword.isLongClickable = false



        binding.tvLogin.setOnClickListener {
            lifecycleScope.launch {
                if (AppUtil.getSavedLanguagePreference(requireContext()).contains("en")) {

                    AppUtil.saveLanguagePreference(requireContext(), "en")


                } else
                    AppUtil.changeAppLanguage(
                        requireContext(),
                        AppUtil.getSavedLanguagePreference(requireContext())
                    )


                if (binding.etEmail.text.isEmpty() || binding.etPassword.text.isEmpty()) {
                    showSnackBar("Please enter id and password")
                    return@launch
                }
                userName = binding.etEmail.text.toString()
                password = binding.etPassword.text.toString()
                val shaPass = AppUtil.sha512Hash(password)
                //   toastLong("saltPass $saltPassword")
                Log.d("saltPass", "saltPass:  "+ saltPassword)

                val saltPass = saltPassword + shaPass
                val finalPass = AppUtil.sha512Hash(saltPass)

                FirebaseMessaging.getInstance().token
                    .addOnCompleteListener { task ->
                        val fcmToken = if (task.isSuccessful) {
                            task.result ?: ""
                        } else {
                            ""
                        }

                        Log.d("--FCM_TOKEN-", fcmToken)
                        commonViewModel.getLoginAPI(
                            LoginReq(
                                userName,
                                finalPass,
                                AppUtil.getAndroidId(requireContext()),
                                BuildConfig.VERSION_NAME,
                                "",
                                fcmToken = fcmToken
                            ))
                        collectLoginResponse()
                    }
            }
        }



        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgotPassViaAadhaarFragment())


        }

        binding.etPassword.onRightDrawableClicked {

            log("onRightDrawableClicked", "onRightDrawableClicked")
            if (showPassword) {
                showPassword = false
                binding.etPassword.setRightDrawablePassword(
                    true, null, null,
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_open_eye), null
                )
            } else {
                showPassword = true
                binding.etPassword.setRightDrawablePassword(
                    false, null, null,
                    ContextCompat.getDrawable(requireContext(), R.drawable.close_eye), null
                )
            }

        }


    }

    private fun collectLoginResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getLoginAPI) {
                when (it) {
                    is Resource.Loading -> showProgressBar()
                    is Resource.Error -> {
                        hideProgressBar()
                        it.error?.let { baseErrorResponse ->
                            toastShort("Server error")


                            isApiCalled = false
                        }
                    }

                    is Resource.Success -> {
                        hideProgressBar()

                        it.data?.let { getLoginResponse ->
                            when (getLoginResponse.responseCode) {

                                200 -> {

                                    // findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainHomePage())

                                        AppUtil.saveTokenPreference(requireContext(),"Bearer "+getLoginResponse.accessToken)
                                        userPreferences.updateUserId(null)
                                        userPreferences.updateUserId(userName)
                                        AppUtil.saveLoginStatus(requireContext(), true)  // true means user is logged in

                                        findNavController().navigate(
                                            R.id.mainHomePage,
                                            null,
                                            NavOptions.Builder()
                                                .setPopUpTo(R.id.loginFragment, true)
                                                .build()
                                        )
                                }

                                203 -> {

                                    toastShort(getLoginResponse.responseDesc)
                                    toastShort(getLoginResponse.responseMsg)
                                    commonViewModel.getToken(AppUtil.getAndroidId(requireContext()), BuildConfig.VERSION_NAME,binding.etEmail.text.toString())

                                    isApiCalled = false

                                }

                                301 -> {
                                    showSnackBar(getLoginResponse.responseDesc)
                                    //Update app
                                    showUpdateDialog()

                                }

                                else -> {
                                    showSnackBar(getLoginResponse.responseDesc)
                                    commonViewModel.getToken(AppUtil.getAndroidId(requireContext()), BuildConfig.VERSION_NAME,binding.etEmail.text.toString())

                                    isApiCalled = false

                                }
                            }
                        } ?: showSnackBar("Internal Server Error")
                    }
                }
            }
        }
    }


    private fun handleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                private var backPressedTime: Long = 0
                private val exitInterval = 2000 // 2 seconds

                override fun handleOnBackPressed() {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - backPressedTime < exitInterval) {
                        isEnabled =
                            false // Disable callback to let the system handle the back press
                        requireActivity().finish()
                    } else {
                        backPressedTime = currentTime
                        showSnackBar("Press back again to exit")
                    }
                }
            })
    }


    private fun collectTokenResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getToken) {
                when (it) {
                    is Resource.Loading -> showProgressBar()
                    is Resource.Error -> {
                        hideProgressBar()
                        it.error?.let { baseErrorResponse ->
                            toastShort(baseErrorResponse.message)
                        }
                    }

                    is Resource.Success -> {
                        hideProgressBar()
                        it.data?.let { getToken ->
                            when (getToken.responseCode) {
                                200 -> {

                                   //token= AESCryptography.decryptIntoString(getToken.authToken,AppConstant.Constants.ENCRYPT_KEY,AppConstant.Constants.ENCRYPT_IV_KEY)
                                   saltPassword= getToken.passString
                                   Log.d("saltPass", "saltPass:  "+ saltPassword)


                                }
                                301 -> {
                                    //Update app
                                    showUpdateDialog()

                                }

                                else -> {
                                    showSnackBar(getToken.responseDesc)
                                    isApiCalled = false


                                }
                            }
                        } ?: showSnackBar("Internal Server Error")
                    }
                }
            }
        }
    }


    private fun showUpdateDialog() {
        val builder = AlertDialog.Builder(requireContext()) //  use requireContext() inside Fragment
        builder.setTitle("Update Available")
        builder.setMessage("A new version of the app is available. Please update to continue.")

        builder.setPositiveButton("Update") { dialog, _ ->
            val appPackageName = "com.kaushalpanjee"
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
                intent.setPackage("com.android.vending")
                startActivity(intent)
            } catch (e: Exception) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName&hl=en_IN"))
                startActivity(intent)
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.setCancelable(false)
        builder.create().show()
    }

}

