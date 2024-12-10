package com.kaushalpanjee.common

import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.R
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.core.util.gone
import com.kaushalpanjee.core.util.log
import com.kaushalpanjee.core.util.onDone
import com.kaushalpanjee.core.util.setLeftDrawable
import com.kaushalpanjee.core.util.showKeyboard
import com.kaushalpanjee.core.util.toastLong
import com.kaushalpanjee.core.util.visible
import com.kaushalpanjee.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    private var isEmailVerified = false
    private var countDownTimer: CountDownTimer? = null
    private val commonViewModel: CommonViewModel by viewModels()
    private var mobileOTP: String? = null
    private var emailOTP: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        collectSendMobileOTPResponse()
        collectSendEmailOTPResponse()
        listeners()
        otpUI()
        addTextWatchers()

    }

    private fun listeners() {
        binding.progressBackButton.setOnClickListener {

            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())



        }


        binding.progressButton.centerButton.setOnClickListener {
            log("setOnClickListener", "setOnClickListener")

            if (AppUtil.isNetworkAvailable(requireContext())){
                binding.clOTP.visible()
                binding.progressButton.root.gone()
                binding.tvSendOtpAgain.isEnabled = false
                binding.tvSendOtpAgain.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_grey
                    )
                )
                showProgressBar()
                resendOTPTimer()

                if (isEmailVerified) {
                    "${getString(R.string.enter_code_msg)} ${binding.etPhone.text}".also {
                        binding.tvEnterCodeMsg.text = it
                    }

                    commonViewModel.sendMobileOTP(binding.etPhone.text.toString())

                } else {"${getString(R.string.enter_code_email_msg)} ${binding.etEmail.text}".also {
                    binding.tvEnterCodeMsg.text = it
                }

                    commonViewModel.sendEmailOTP(binding.etEmail.text.toString())
                }


            }

            else showSnackBar("No internet connection")


            }


        binding.tvVerify.setOnClickListener {

            if (isEmailVerified) {

                if ("${binding.et1.text}${binding.et2.text}${binding.et3.text}${binding.et4.text}".contentEquals(
                        mobileOTP
                    )
                ) {
                    binding.clOTP.gone()
                    toastLong("Phone number is verified")
                    binding.etPhone.isEnabled = false
                    binding.etPhone.setLeftDrawable(requireContext(), R.drawable.ic_verified)
                    lifecycleScope.launch {
                        delay(500)
                        userPreferences.setIsRegistered(true)
                        findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToEkycFragment())

                    }
                } else {
                    toastLong("Invalid OTP")
                    binding.et1.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_otp_invalid_rectangle
                    )
                    binding.et2.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_otp_invalid_rectangle
                    )
                    binding.et3.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_otp_invalid_rectangle
                    )
                    binding.et4.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_otp_invalid_rectangle
                    )
                }

            }
            else {


                if ("${binding.et1.text}${binding.et2.text}${binding.et3.text}${binding.et4.text}".contentEquals(
                        emailOTP
                    )
                ) {
                    toastLong("Email is verified")
                    binding.etEmail.setLeftDrawable(requireContext(), R.drawable.ic_verified)
                    binding.tvVerify.gone()
                    binding.etEmail.isEnabled = false
                    binding.etPhone.visible()
                    binding.clOTP.gone()
                    isEmailVerified = true
                } else {
                    toastLong("Invalid OTP")
                    binding.et1.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_otp_invalid_rectangle
                    )
                    binding.et2.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_otp_invalid_rectangle
                    )
                    binding.et3.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_otp_invalid_rectangle
                    )
                    binding.et4.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_otp_invalid_rectangle
                    )
                }
            }


            binding.et1.text.clear()
            binding.et2.text.clear()
            binding.et3.text.clear()
            binding.et4.text.clear()
        }

        binding.tvSendOtpAgain.setOnClickListener {
            binding.tvSendOtpAgain.isEnabled = false
            binding.tvSendOtpAgain.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_grey
                )
            )
            if (isEmailVerified){
                commonViewModel.sendMobileOTP(binding.etPhone.text.toString())
            }
            else commonViewModel.sendEmailOTP(binding.etEmail.text.toString())

            resendOTPTimer()
        }

    }

    private fun otpUI() {
        binding.et1.setOnClickListener {

            showKeyboard(binding.et1)
            hasFocus(R.id.et1)

        }
        binding.et2.setOnClickListener {

            showKeyboard(binding.et2)
            hasFocus(R.id.et2)

        }
        binding.et3.setOnClickListener {
            showKeyboard(binding.et3)
            hasFocus(R.id.et3)
        }
        binding.et4.setOnClickListener {
            showKeyboard(binding.et4)
            hasFocus(R.id.et4)
        }

        binding.et4.onDone {
            validateAndNavigate()
        }
    }

    private fun validateAndNavigate() {

    }

    private fun collectSendMobileOTPResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.sendMobileOTP) {
                when (it) {
                    is Resource.Loading -> {}
                    is Resource.Error -> {
                        hideProgressBar()
                        it.error?.let { baseErrorResponse ->
                            showSnackBar(baseErrorResponse.message)
                        }
                    }

                    is Resource.Success -> {
                        hideProgressBar()
                        it.data?.let { sendMobileOTPResponse ->
                            if (sendMobileOTPResponse.responseCode == 200) {
                                mobileOTP = sendMobileOTPResponse.otp
                                if (BuildConfig.DEBUG)
                                    showSnackBar(sendMobileOTPResponse.otp)
                            } else showSnackBar("Something went wrong")

                        } ?: showSnackBar("Internal Sever Error")
                    }
                }
            }
        }
    }



    private fun collectSendEmailOTPResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.sendEmailOTP) {
                when (it) {
                    is Resource.Loading -> {}
                    is Resource.Error -> {
                        hideProgressBar()
                        it.error?.let { baseErrorResponse ->
                            showSnackBar(baseErrorResponse.message)
                        }
                    }

                    is Resource.Success -> {
                        hideProgressBar()
                        it.data?.let { sendMobileOTPResponse ->
                            if (sendMobileOTPResponse.responseCode == 200) {
                                emailOTP = sendMobileOTPResponse.otp
                                if (BuildConfig.DEBUG)
                                    showSnackBar(sendMobileOTPResponse.otp)
                            } else showSnackBar("Something went wrong")

                        } ?: showSnackBar("Internal Sever Error")
                    }
                }
            }
        }
    }
    private fun resendOTPTimer() {


        countDownTimer?.let {
            it.cancel()
        }

        countDownTimer = object : CountDownTimer(60000.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                val seconds: Long =
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - (minutes * 60)
                var minutesInString = minutes.toString()
                var secondsInString = seconds.toString()
                if (minutes.toString().length == 1) {
                    minutesInString = "0$minutes"
                } else if (minutes.toString().isEmpty()) {
                    minutesInString = "00"
                }
                if (seconds.toString().length == 1) {
                    secondsInString = "0$seconds"
                } else if (seconds.toString().isEmpty()) {
                    secondsInString = "00"
                }
                ("$minutesInString:$secondsInString").also {
                    binding.tvTimer.text = it
                }
            }

            override fun onFinish() {
                binding.tvSendOtpAgain.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_dark_green
                    )
                )
                binding.tvSendOtpAgain.isEnabled = true
            }
        }.start()
    }

    private fun addTextWatchers() {

        binding.etPhone.doOnTextChanged { text, start, before, count ->

          val input : String  = binding.etPhone.text.toString()
            if (!input.startsWith("6789") && input.isNotEmpty()) {
                binding.etPhone.error = "Number must start with 6789"
            }
        }

        binding.etEmail.doOnTextChanged { text, _, _, _ ->
            if (!TextUtils.isEmpty(text.toString()) &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()
            ) {
                binding.progressButton.root.visible()
            } else {
                binding.progressButton.root.gone()
                binding.clOTP.gone()
            }
        }

        binding.etPhone.doOnTextChanged { text, start, before, count ->

            if (text?.length == 10) {
                binding.progressButton.root.visible()
            } else binding.progressButton.root.gone()

        }

        binding.et1.doOnTextChanged { text, _, _, _ ->
            if (text?.length == 1) {
                binding.et2.requestFocus()
                hasFocus(R.id.et2)
            } else {
                binding.et1.requestFocus()
                hasFocus(R.id.et1)
            }
            showVerifyButtonUI()
        }

        binding.et2.doOnTextChanged { text, _, _, _ ->
            if (text?.length == 1) {
                binding.et3.requestFocus()
                hasFocus(R.id.et3)
            } else {
                binding.et1.requestFocus()
                hasFocus(R.id.et1)
            }

            showVerifyButtonUI()
        }

        binding.et3.doOnTextChanged { text, _, _, _ ->
            if (text?.length == 1) {
                binding.et4.requestFocus()
                hasFocus(R.id.et4)
            } else {
                binding.et2.requestFocus()
                hasFocus(R.id.et2)
            }

            showVerifyButtonUI()
        }

        binding.et4.doOnTextChanged { text, _, _, _ ->
            if (text?.length == 1) {
                binding.et4.requestFocus()
                hasFocus(R.id.et4)
            } else {
                binding.et3.requestFocus()
                hasFocus(R.id.et3)
            }

            showVerifyButtonUI()
        }

    }

    private fun showVerifyButtonUI() {

        val et1Text = binding.et1.text.toString()
        val et2Text = binding.et2.text.toString()
        val et3Text = binding.et3.text.toString()
        val et4Text = binding.et4.text.toString()

        val hasToShow = "$et1Text$et2Text$et3Text$et4Text".length == 4

        if (hasToShow)
            binding.tvVerify.visible()
        else binding.tvVerify.gone()
    }

    private fun hasFocus(et: Int) {
        when (et) {
            R.id.et1 -> {
                binding.et1.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_grey_rectangle)

                if (binding.et2.text.toString().length == 1)
                    binding.et2.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_rectangle)
                else binding.et2.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_invalid_rectangle)

                if (binding.et3.text.toString().length == 1)
                    binding.et3.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_rectangle)
                else binding.et3.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_invalid_rectangle)

                if (binding.et4.text.toString().length == 1)
                    binding.et4.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_rectangle)
                else binding.et4.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_invalid_rectangle)


            }

            R.id.et2 -> {
                binding.et2.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_grey_rectangle)

                if (binding.et1.text.toString().length == 1)
                    binding.et1.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_rectangle)
                else binding.et1.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_invalid_rectangle)

                if (binding.et3.text.toString().length == 1)
                    binding.et3.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_rectangle)
                else binding.et3.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_invalid_rectangle)

                if (binding.et4.text.toString().length == 1)
                    binding.et4.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_rectangle)
                else binding.et4.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_invalid_rectangle)


            }

            R.id.et3 -> {
                binding.et3.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_grey_rectangle)

                if (binding.et1.text.toString().length == 1)
                    binding.et1.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_rectangle)
                else binding.et1.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_invalid_rectangle)

                if (binding.et1.text.toString().length == 1)
                    binding.et2.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_rectangle)
                else binding.et2.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_invalid_rectangle)

                if (binding.et4.text.toString().length == 1)
                    binding.et4.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_rectangle)
                else binding.et4.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_invalid_rectangle)

            }

            R.id.et4 -> {
                binding.et4.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_rectangle)

                if (binding.et1.text.toString().length == 1)
                    binding.et1.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_rectangle)
                else binding.et1.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_invalid_rectangle)

                if (binding.et2.text.toString().length == 1)
                    binding.et2.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_rectangle)
                else binding.et2.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_invalid_rectangle)

                if (binding.et3.text.toString().length == 1)
                    binding.et3.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_rectangle)
                else binding.et3.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_invalid_rectangle)

            }

        }
    }
}