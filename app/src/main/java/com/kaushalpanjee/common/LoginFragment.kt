package com.kaushalpanjee.common

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.R
import com.kaushalpanjee.common.model.request.LoginReq
import com.kaushalpanjee.common.model.request.SectionAndPerReq
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.core.util.gone
import com.kaushalpanjee.core.util.log
import com.kaushalpanjee.core.util.onRightDrawableClicked
import com.kaushalpanjee.core.util.setRightDrawablePassword
import com.kaushalpanjee.core.util.toastShort
import com.kaushalpanjee.core.util.visible
import com.kaushalpanjee.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate){

  private var showPassword = true

    private val commonViewModel: CommonViewModel by activityViewModels()



   private  var userName=""
   private  var password=""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        init()


    }


    private fun init(){
        listeners()



        }

    private fun listeners(){
        binding.tvRegister.setOnClickListener {
          findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())

        }
        binding.tvLogin.setOnClickListener {
            lifecycleScope.launch{
                if (AppUtil.getSavedLanguagePreference(requireContext()).contains("eng")){

                    AppUtil.saveLanguagePreference(requireContext(),"eng")


                }
                else
                    AppUtil.changeAppLanguage(requireContext(),AppUtil.getSavedLanguagePreference(requireContext()))


                if (binding.etEmail.text.isNotEmpty() && binding.etPassword.text.isNotEmpty()){
                     userName= binding.etEmail.text.toString()
                     password= binding.etPassword.text.toString()


                  //commonViewModel.getLoginAPI(LoginReq("2505000001","Ya$@x7Q#mv",AppUtil.getAndroidId(requireContext()),BuildConfig.VERSION_NAME,""))
                    commonViewModel.getLoginAPI(LoginReq("2501000005","aSMe#D1oU*",AppUtil.getAndroidId(requireContext()),BuildConfig.VERSION_NAME,""))
                 //   commonViewModel.getLoginAPI(LoginReq(userName,password,AppUtil.getAndroidId(requireContext()),BuildConfig.VERSION_NAME,""))

                    collectLoginResponse()

                }
                else
                    showSnackBar("Please enter id and password")






            }

        }

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
        }

        binding.etPassword.onRightDrawableClicked {

            log("onRightDrawableClicked", "onRightDrawableClicked")
            if (showPassword){
                showPassword = false
                binding.etPassword.setRightDrawablePassword(true,null,null,
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_open_eye),null)
            }
            else {
                showPassword = true

                binding.etPassword.setRightDrawablePassword(false,null,null,
                    ContextCompat.getDrawable(requireContext(), R.drawable.close_eye),null)

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
                            toastShort(baseErrorResponse.message)
                        }
                    }

                    is Resource.Success -> {
                        hideProgressBar()
                        it.data?.let { getLoginResponse ->
                            when (getLoginResponse.responseCode) {
                                200 -> {

                                    showSnackBar(getLoginResponse.responseMsg)

                                    userPreferences.updateUserId(null)
                                    userPreferences.updateUserId(userName)
                                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainHomePage())


                                }
                                203->{
                                    showSnackBar(getLoginResponse.responseMsg)

                                }

                                301 -> {
                                    showSnackBar(getLoginResponse.responseMsg)
                                }
                                else -> {
                                    showSnackBar("Something went wrong")
                                }
                            }
                        } ?: showSnackBar("Internal Server Error")
                    }
                }
            }
        }
    }


}