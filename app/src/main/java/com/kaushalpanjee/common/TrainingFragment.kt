package com.kaushalpanjee.common

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.common.model.WrappedList
import com.kaushalpanjee.common.model.request.TechQualification
import com.kaushalpanjee.common.model.request.TrainingCenterReq
import com.kaushalpanjee.common.model.response.DistrictList
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.core.util.toastLong
import com.kaushalpanjee.core.util.toastShort
import com.kaushalpanjee.databinding.FragmentTrainingBinding
import kotlinx.coroutines.launch

class TrainingFragment : BaseFragment<FragmentTrainingBinding>(FragmentTrainingBinding::inflate) {

    private val commonViewModel: CommonViewModel by activityViewModels()

    private var stateList: MutableList<WrappedList> = mutableListOf()
    private lateinit var stateAdapter: ArrayAdapter<String>

    private var state = ArrayList<String>()
    private var stateCode = ArrayList<String>()
    private var stateLgdCode = ArrayList<String>()
    private var selectedStateCodeItem = ""
    private var selectedStateItem = ""
    private var selectedSectorItem = ""
    private var selectedSectorCodeItem = ""
    private var sectorList = ArrayList<String>()
    private var sectorCode = ArrayList<String>()

    private var districtList: MutableList<DistrictList> = mutableListOf()
    private lateinit var districtAdapter: ArrayAdapter<String>
    private var district = ArrayList<String>()
    private var districtCode = ArrayList<String>()
    private var districtLgdCode = ArrayList<String>()
    private var selectedDistrictCodeItem = ""
    private var selectedDistrictItem = ""
    private lateinit var sectorAdapter: ArrayAdapter<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setupObservers()
        fetchInitialData()
    }

    override fun onResume() {
        super.onResume()
        // Refresh fragment data when navigating back
        refreshData()
    }

    private fun init() {
        setupAdapters()
        setupListeners()
    }

    private fun setupAdapters() {
        sectorAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, sectorList)
        binding.SpinnerSectorName.setAdapter(sectorAdapter)

        stateAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, state)
        binding.spinnerStateName.setAdapter(stateAdapter)

        districtAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, district)
        binding.SpinnerDistrictName.setAdapter(districtAdapter)
    }

    private fun setupListeners() {
        binding.tvSubmit.setOnClickListener {
            if (selectedSectorItem.isNotEmpty() && selectedDistrictItem.isNotEmpty()) {
                findNavController().navigate(
                    TrainingFragmentDirections.actionTrainingFragmentToTrainingCenterView(
                        selectedDistrictCodeItem,
                        selectedSectorCodeItem
                    )
                )
            } else {
                toastLong("Kindly fill details first")
            }
        }

        binding.SpinnerDistrictName.setOnItemClickListener { parent, _, position, _ ->
            selectedDistrictItem = parent.getItemAtPosition(position).toString()
            if (position in district.indices) {
                commonViewModel.getSectorListAPI(TechQualification(BuildConfig.VERSION_NAME))
                selectedDistrictCodeItem = districtCode[position]
            } else {
                Toast.makeText(requireContext(), "Invalid selection", Toast.LENGTH_SHORT).show()
            }
        }

        binding.spinnerStateName.setOnItemClickListener { parent, _, position, _ ->
            selectedStateItem = parent.getItemAtPosition(position).toString()
            if (position in state.indices) {
                selectedStateCodeItem = stateCode[position]
                commonViewModel.getDistrictListApi(selectedStateCodeItem)
            } else {
                Toast.makeText(requireContext(), "Invalid selection", Toast.LENGTH_SHORT).show()
            }
        }

        binding.SpinnerSectorName.setOnItemClickListener { parent, _, position, _ ->
            selectedSectorItem = parent.getItemAtPosition(position).toString()
            if (position in sectorList.indices) {
                selectedSectorCodeItem = sectorCode[position]
            } else {
                Toast.makeText(requireContext(), "Invalid selection", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupObservers() {
        collectStateResponse()
        collectDistrictResponse()
        collectSectorResponse()
    }

    private fun fetchInitialData() {
        commonViewModel.getStateListApi()
    }

    private fun refreshData() {
        // Fetch state list again to refresh data
        fetchInitialData()
    }

    private fun collectStateResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getStateList) { resource ->
                when (resource) {
                    is Resource.Loading -> showProgressBar()
                    is Resource.Error -> {
                        hideProgressBar()
                        resource.error?.message?.let { toastShort(it) }
                    }
                    is Resource.Success -> {
                        hideProgressBar()
                        resource.data?.let { response ->
                            if (response.responseCode == 200) {
                                stateList = response.stateList
                                state.clear()
                                stateCode.clear()
                                stateLgdCode.clear()

                                for (x in stateList) {
                                    state.add(x.stateName)
                                    stateCode.add(x.stateCode)
                                    stateLgdCode.add(x.lgdStateCode)
                                }
                                stateAdapter.notifyDataSetChanged()
                            } else {
                                showSnackBar("Something went wrong")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun collectDistrictResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getDistrictList) { resource ->
                when (resource) {
                    is Resource.Loading -> showProgressBar()
                    is Resource.Error -> {
                        hideProgressBar()
                        resource.error?.message?.let { showSnackBar(it) }
                    }
                    is Resource.Success -> {
                        hideProgressBar()
                        resource.data?.let { response ->
                            if (response.responseCode == 200) {
                                districtList = response.districtList
                                district.clear()
                                districtCode.clear()
                                districtLgdCode.clear()

                                for (x in districtList) {
                                    district.add(x.districtName)
                                    districtCode.add(x.districtCode)
                                    districtLgdCode.add(x.lgdDistrictCode)
                                }
                                districtAdapter.notifyDataSetChanged()
                            } else {
                                showSnackBar("Something went wrong")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun collectSectorResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getSectorListAPI) { resource ->
                when (resource) {
                    is Resource.Loading -> showProgressBar()
                    is Resource.Error -> {
                        hideProgressBar()
                        resource.error?.message?.let { showSnackBar(it) }
                    }
                    is Resource.Success -> {
                        hideProgressBar()
                        resource.data?.let { response ->
                            if (response.responseCode == 200) {
                                val sectorListData = response.wrappedList
                                sectorCode.clear()
                                sectorList.clear()
                                for (x in sectorListData) {
                                    sectorList.add(x.sectorName)
                                    sectorCode.add(x.sectorId)
                                }
                                sectorAdapter.notifyDataSetChanged()
                            } else {
                                showSnackBar("Something went wrong")
                            }
                        }
                    }
                }
            }
        }
    }
}
