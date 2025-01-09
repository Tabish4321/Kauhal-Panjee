package com.kaushalpanjee.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.common.model.request.TrainingCenterReq
import com.kaushalpanjee.common.model.response.Center
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.core.util.toastShort
import com.kaushalpanjee.databinding.TrainingCenterViewBinding
import kotlinx.coroutines.launch

class TrainingCenterView : BaseFragment<TrainingCenterViewBinding>(TrainingCenterViewBinding::inflate) {

    private val commonViewModel: CommonViewModel by activityViewModels()
    private val centerList = mutableListOf<Center>() // Use mutableList for easy updates
    private lateinit var centerAdapter: CenterAdapter // Initialize the adapter later

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        // Retrieve arguments
        val districtCode = arguments?.getString("districtCode")
        val sectorCode = arguments?.getString("sectorCode")

        // Fetch data from ViewModel
        commonViewModel.getTrainingListAPI(
            TrainingCenterReq(BuildConfig.VERSION_NAME, sectorCode, districtCode)
        )
        setupRecyclerView()
        collectTrainingCenterResponse()
    }

    private fun setupRecyclerView() {
        // Initialize adapter and RecyclerView
        centerAdapter = CenterAdapter(centerList)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = centerAdapter
        }
    }

    private fun collectTrainingCenterResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getTrainingListAPI) { resource ->
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
                                updateCenterList(response.centerList)
                            } else if (response.responseCode == 301) {
                                showSnackBar("Please update from PlayStore")
                            } else {
                                showSnackBar("Something went wrong")
                            }
                        } ?: showSnackBar("Internal Server Error")
                    }
                }
            }
        }
    }

    private fun updateCenterList(newList: List<Center>) {
        centerList.clear()
        centerList.addAll(newList)
        centerAdapter.notifyDataSetChanged() // Notify adapter about data changes
    }
}
