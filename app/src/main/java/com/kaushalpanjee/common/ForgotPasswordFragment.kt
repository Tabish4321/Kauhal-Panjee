package com.kaushalpanjee.common

import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.R
import com.kaushalpanjee.common.model.request.ChangePassReq
import com.kaushalpanjee.common.model.request.GetLoginIdNdPassReq
import com.kaushalpanjee.common.model.request.ValidateOtpReq
import com.kaushalpanjee.common.model.response.UserIdName
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.core.util.UserPreferences
import com.kaushalpanjee.core.util.gone
import com.kaushalpanjee.core.util.onDone
import com.kaushalpanjee.core.util.setLeftDrawable
import com.kaushalpanjee.core.util.showKeyboard
import com.kaushalpanjee.core.util.toastLong
import com.kaushalpanjee.core.util.toastShort
import com.kaushalpanjee.core.util.visible
import com.kaushalpanjee.databinding.FragmentForgotPasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>(FragmentForgotPasswordBinding::inflate){

    private val commonViewModel: CommonViewModel by activityViewModels()
    private var countDownTimer: CountDownTimer? = null
    private var email = ""
    private var mobileNo = ""
    private var selectedUserIdItem = ""
    private var userIdList = ArrayList<String>()
    private var userIdNameList = ArrayList<UserIdName>()
    private lateinit var userIdAdapter: ArrayAdapter<String>






    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreferences = UserPreferences(requireContext())

        userIdAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            userIdList
        )

        binding.SpinnerUserSelect.setAdapter(userIdAdapter)



        init()

    }
    private fun init(){

        listener()
        resendOTPTimer()
        addTextWatchers()
        otpUI()


    }

    private fun listener(){

        // Secc GP selection

        binding.SpinnerUserSelect.setOnItemClickListener { parent, view, position, id ->
            selectedUserIdItem = parent.getItemAtPosition(position).toString()

            val input = selectedUserIdItem
            val index = input.indexOf('(')

            val id = if (index != -1) input.substring(0, index) else input




            commonViewModel.getLoginIdPass(GetLoginIdNdPassReq(BuildConfig.VERSION_NAME,mobileNo,email,AppUtil.getAndroidId(requireContext()),id))
            collectGetIdPassResponse()

        }


        binding.tvVerify.setOnClickListener {

            validateAndNavigate()

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


            email= binding.etEmail.text.toString()
            mobileNo= binding.etPhone.text.toString()

            if (email.isNotEmpty()&& mobileNo.isNotEmpty()){

                commonViewModel.getChangePassOtp(GetLoginIdNdPassReq(BuildConfig.VERSION_NAME,mobileNo,email,AppUtil.getAndroidId(requireContext()),""))
                collectForgotOtpResponse()

            }

            else toastShort("please fill all fields")

        resendOTPTimer()

        }


        binding.progressButton.centerButton.setOnClickListener {

             email= binding.etEmail.text.toString()
             mobileNo= binding.etPhone.text.toString()

            if (email.isNotEmpty()&& mobileNo.isNotEmpty()){


                commonViewModel.getChangePassOtp(GetLoginIdNdPassReq(BuildConfig.VERSION_NAME,mobileNo,email,AppUtil.getAndroidId(requireContext()),""))
                collectForgotOtpResponse()

            }

            else toastShort("please fill all fields")


        }

        }



    private fun collectForgotOtpResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getChangePassOtp) {
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
                        it.data?.let { getChangePassOtp ->
                            if (getChangePassOtp.responseCode == 200) {
                                toastShort(getChangePassOtp.responseDesc)

                                binding.clForgotOTP.visible()
                                binding.tvVerify.visible()
                                binding.etEmail.gone()
                                binding.etPhone.gone()
                                binding.progressButton.root.gone()





                            } else if (getChangePassOtp.responseCode == 301) {
                                showSnackBar("Please Update from PlayStore")
                            }  else if (getChangePassOtp.responseCode==401){
                                AppUtil.showSessionExpiredDialog(findNavController(),requireContext())
                            }

                            else if (getChangePassOtp.responseCode == 207) {
                                toastShort(getChangePassOtp.responseDesc)
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

    private fun collectGetIdPassResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getLoginIdPass) {
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
                        it.data?.let { getLoginIdPass ->
                            if (getLoginIdPass.responseCode == 200) {
                                toastShort(getLoginIdPass.responseDesc)
                                findNavController().navigateUp()


                            } else if (getLoginIdPass.responseCode == 301) {
                                showSnackBar("Please Update from PlayStore")
                            }

                            else if (getLoginIdPass.responseCode == 207) {
                                toastShort(getLoginIdPass.responseDesc)
                            }

                            else if (getLoginIdPass.responseCode==401){
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

    private fun collectValidateOtpResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getOtpValidateApi) {
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
                        it.data?.let { getOtpValidateApi ->
                            if (getOtpValidateApi.responseCode == 200) {
                                toastShort(getOtpValidateApi.responseDesc)
                                binding.clForgotOTP.gone()
                                binding.tvVerify.gone()
                                binding.llUserSelect.visible()
                                userIdNameList= getOtpValidateApi.wrappedList as ArrayList<UserIdName>
                                userIdList.clear()
                                for (x in userIdNameList){

                                    userIdList.add(x.loginId)
                                }


                            } else if (getOtpValidateApi.responseCode == 301) {
                                showSnackBar("Please Update from PlayStore")
                                binding.llUserSelect.gone()

                            }

                            else if (getOtpValidateApi.responseCode == 207) {
                                toastShort(getOtpValidateApi.responseDesc)
                                binding.llUserSelect.gone()

                            }
                            else if (getOtpValidateApi.responseCode == 210) {
                                toastShort(getOtpValidateApi.responseDesc)
                                binding.llUserSelect.gone()

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

        private  fun validateAndNavigate(){

            if ("${binding.et1.text}${binding.et2.text}${binding.et3.text}${binding.et4.text}".isNotEmpty()
            ) {

                // hit api
                val otp= "${binding.et1.text}${binding.et2.text}${binding.et3.text}${binding.et4.text}"



                commonViewModel.getOtpValidateApi(ValidateOtpReq(BuildConfig.VERSION_NAME, email,mobileNo,AppUtil.getAndroidId(requireContext()),otp))
                collectValidateOtpResponse()





            }
            else {
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

}
