package com.kaushalpanjee.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.R
import com.kaushalpanjee.common.model.request.ChangePassReq
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.AESCryptography
import com.kaushalpanjee.core.util.AppConstant
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.core.util.UserPreferences
import com.kaushalpanjee.core.util.toastShort
import com.kaushalpanjee.databinding.ChangePassFragmentBinding
import kotlinx.coroutines.launch

class ChangePasswordFragment : BaseFragment<ChangePassFragmentBinding>(ChangePassFragmentBinding::inflate) {


    private val commonViewModel: CommonViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreferences = UserPreferences(requireContext())


        init()
    }

    private fun init(){

        listener()

    }

    private  fun listener(){

        binding.tvSubmit.setOnClickListener {

            val oldPassword= binding.etPassword.text.toString()
            val newPassword= binding.etnewPassword.text.toString()
            val confirmNewPassword= binding.etconfirmPassword.text.toString()

            if (oldPassword.isNotEmpty()&& newPassword.isNotEmpty()&& confirmNewPassword.isNotEmpty()){

                if (newPassword == confirmNewPassword){

                    val shaOldPassword= AppUtil.sha512Hash(oldPassword)
                    // val shaNewPassword= AppUtil.sha512Hash(newPassword)

                    val encryptedNewPass=   AESCryptography.encryptIntoBase64String(newPassword, AppConstant.Constants.ENCRYPT_KEY, AppConstant.Constants.ENCRYPT_IV_KEY)


                    commonViewModel.getChangePass(ChangePassReq(BuildConfig.VERSION_NAME,userPreferences.getUseID(),
                        shaOldPassword,encryptedNewPass),AppUtil.getSavedTokenPreference(requireContext()))

                    collectChangePassResponse()




                }
                else  toastShort("confirm password is not matched")
            }

            else toastShort("please fill all fields")

        }

    }

    private fun collectChangePassResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getChangePass) {
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
                        it.data?.let { getChangePass ->
                            if (getChangePass.responseCode == 200) {

                                toastShort(getChangePass.responseDesc)
                                findNavController().navigate(
                                    R.id.mainHomePage,
                                    null,
                                    NavOptions.Builder()
                                        .setPopUpTo(R.id.changePasswordFragment, true)
                                        .build()
                                )

                            } else if (getChangePass.responseCode == 301) {
                                showSnackBar("Please Update from PlayStore")
                            }

                            else if (getChangePass.responseCode == 206) {
                                toastShort(getChangePass.responseDesc)
                            }

                            else if (getChangePass.responseCode == 207) {
                                toastShort(getChangePass.responseDesc)
                            }
                            else if (getChangePass.responseCode == 208) {
                                toastShort(getChangePass.responseDesc)
                            }
                            else if (getChangePass.responseCode==401){
                                AppUtil.showSessionExpiredDialog(findNavController(),requireContext())
                            }


                            else {
                                showSnackBar("Something went wrong")
                            }
                        } ?: showSnackBar("Internal Server Error")
                    }
                }
            }
        }
    }

}