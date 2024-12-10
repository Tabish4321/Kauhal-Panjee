package com.kaushalpanjee.common

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.R
import com.kaushalpanjee.common.model.StateDataResponse
import com.kaushalpanjee.common.model.WrappedList
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.core.util.gone
import com.kaushalpanjee.core.util.log
import com.kaushalpanjee.core.util.onRightDrawableClicked
import com.kaushalpanjee.core.util.setRightDrawablePassword
import com.kaushalpanjee.core.util.setUnderline
import com.kaushalpanjee.core.util.toastLong
import com.kaushalpanjee.core.util.toastShort
import com.kaushalpanjee.core.util.visible
import com.kaushalpanjee.databinding.FragmentEkyBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EkycFragment : BaseFragment<FragmentEkyBinding>(FragmentEkyBinding::inflate) {

    private val commonViewModel: CommonViewModel by viewModels()
    private var stateList: MutableList<WrappedList> = mutableListOf()

    private var selectedState = ""
    private var showPassword = true

    private val stateAdaptor by lazy {
        StateAdaptor(object : StateAdaptor.ItemClickListener {
            override fun onItemClick(position: Int) {

                selectedState = stateList[position].state_name
                binding.tvWelcome.text = getString(R.string.slected_state)
                binding.tvWelcomeMsg.text = selectedState
                binding.tvWelcomeMsg.visible()
                binding.tvWelcomeMsg.setUnderline(selectedState)
                toastShort("${stateList[position].state_name} ,  ${stateList[position].state_code}")
                binding.progressButton.root.visible()

                lifecycleScope.launch {
                    delay(1000)
                  //  binding.recyclerView.scrollToPosition(1)

                }

            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        listener()
        setUI()
        collectStateResponse()
        addTextWatchers()
        commonViewModel.getStateListApi()

    }

    private fun setUI() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = stateAdaptor
    }

    private fun listener() {
        if (binding.etAadhaar.text.length ==12){
            binding.aadhaarVerifyButton.root.visible()
        }
        binding.aadhaarVerifyButton.centerButton.setOnClickListener {

          if(binding.etAadhaar.text.length !=12 ){

              showSnackBar("Please enter valid aadhaar number")
          }
            else{
              showSnackBar("success")


          }

        }

        binding.progressButton.centerButton.setOnClickListener {
            binding.recyclerView.gone()
            binding.progressButton.root.gone()
            binding.etAadhaar.visible()
            if (binding.etAadhaar.text.isNotEmpty()){
                binding.etAadhaar.setText("")
            }

        }

        binding.tvWelcomeMsg.setOnClickListener {
            binding.recyclerView.visible()
            //binding.tvWelcomeMsg.gone()
           // binding.tvWelcome.text = getString(R.string.slected_state)
            binding.tvWelcomeMsg.text = selectedState
            binding.etAadhaar.gone()
            binding.progressButton.root.visible()
            binding.etAadhaar.gone()
            binding.aadhaarVerifyButton.root.gone()
        }
        binding.etAadhaar.onRightDrawableClicked {

            log("onRightDrawableClicked", "onRightDrawableClicked")

            if (showPassword){
                showPassword = false
                binding.etAadhaar.setRightDrawablePassword(true)
            }
            else {
                showPassword = true
                binding.etAadhaar.setRightDrawablePassword(false)
            }

        }


    }

    private fun collectStateResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getStateList) {
                when (it) {
                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Error -> {
                        hideProgressBar()
                        it.error?.let { baseErrorResponse ->
                            showSnackBar(baseErrorResponse.message)
                        }
                    }

                    is Resource.Success -> {
                        hideProgressBar()
                        it.data?.let { getStateResponse ->
                            if (getStateResponse.responseCode == 200) {
                                stateList = getStateResponse.wrappedList



                                stateAdaptor.setData(stateList)
                            } else showSnackBar("Something went wrong")

                        } ?: showSnackBar("Internal Sever Error")
                    }
                }
            }
        }
    }

    private fun addTextWatchers(){

        binding.etAadhaar.doOnTextChanged { text, start, before, count ->

            if (text?.length == 12) {
                binding.aadhaarVerifyButton.root.visible()
            } else binding.aadhaarVerifyButton.root.gone()

        }

    }

}