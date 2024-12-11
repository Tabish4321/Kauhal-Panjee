package com.kaushalpanjee.common

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.kaushalpanjee.R
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.log
import com.kaushalpanjee.core.util.onRightDrawableClicked
import com.kaushalpanjee.core.util.setRightDrawablePassword
import com.kaushalpanjee.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate){

  private var showPassword = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init(){
        listeners()
    }

    private fun listeners(){
        binding.tvRegister.setOnClickListener {
           // findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())

            if (userPreferences.getIsRegistered()){
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToEkycFragment())
            }else findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
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

                binding.etPassword.setRightDrawablePassword(true,null,null,
                    ContextCompat.getDrawable(requireContext(), R.drawable.close_eye),null)

            }

        }
    }

}