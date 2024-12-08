package com.kaushalpanjee.common

import android.os.Bundle
import android.view.View
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
                    binding.recyclerView.scrollToPosition(1)
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
        commonViewModel.getStateListApi()

    }

    private fun setUI() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = stateAdaptor
    }

    private fun listener() {

        binding.progressButton.centerButton.setOnClickListener {
        }

        binding.tvWelcomeMsg.setOnClickListener {
            binding.recyclerView.visible()
            binding.tvWelcomeMsg.gone()
            binding.tvWelcome.text = getString(R.string.select_state)
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

}