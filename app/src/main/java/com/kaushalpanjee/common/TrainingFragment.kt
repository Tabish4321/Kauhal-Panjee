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

class TrainingFragment : BaseFragment<FragmentTrainingBinding>(FragmentTrainingBinding::inflate){


    private val commonViewModel: CommonViewModel by activityViewModels()


    // State var
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



    // district var
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

        commonViewModel.getStateListApi()
        collectStateResponse()
        collectDistrictResponse()
        collectSectorResponse()


    }

    private fun init(){
        listener()


    }
    private fun listener(){



        binding.tvSubmit.setOnClickListener {

            if (selectedSectorItem.isNotEmpty()&& selectedDistrictItem.isNotEmpty()){
                findNavController().navigate(TrainingFragmentDirections.actionTrainingFragmentToTrainingCenterView(selectedDistrictCodeItem,selectedSectorCodeItem))


            }

            else  toastLong("Kindly fill details first")




        }




        //Adapter Sector setting

        sectorAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            sectorList
        )

        binding.SpinnerSectorName.setAdapter(sectorAdapter)


        //Adapter state setting
        stateAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            state
        )

        binding.spinnerStateName.setAdapter(stateAdapter)



        //Adapter District setting

        districtAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            district
        )

        binding.SpinnerDistrictName.setAdapter(districtAdapter)





        //District selection
        binding.SpinnerDistrictName.setOnItemClickListener { parent, view, position, id ->
            selectedDistrictItem = parent.getItemAtPosition(position).toString()
            if (position in district.indices) {
                commonViewModel.getSectorListAPI(TechQualification(BuildConfig.VERSION_NAME))
                selectedDistrictCodeItem = districtCode[position]
            //    selectedDistrictCodeItem = districtLgdCode[position]

            } else {
                Toast.makeText(requireContext(), "Invalid selection", Toast.LENGTH_SHORT).show()
            }
        }



        //State selection
        binding.spinnerStateName.setOnItemClickListener { parent, view, position, id ->
            selectedStateItem = parent.getItemAtPosition(position).toString()
            if (position in state.indices) {
                selectedStateCodeItem = stateCode[position]
                commonViewModel.getDistrictListApi(selectedStateCodeItem)

            }
            else {
                Toast.makeText(requireContext(), "Invalid selection", Toast.LENGTH_SHORT).show()
            }
        }



        //Sector selection
        binding.SpinnerSectorName.setOnItemClickListener { parent, view, position, id ->
            selectedSectorItem = parent.getItemAtPosition(position).toString()
            if (position in sectorList.indices) {
                selectedSectorCodeItem = sectorCode[position]

            }
            else {
                Toast.makeText(requireContext(), "Invalid selection", Toast.LENGTH_SHORT).show()
            }
        }
    }








            private fun collectStateResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getStateList) {
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
                        it.data?.let { getStateResponse ->
                            if (getStateResponse.responseCode == 200) {
                                stateList = getStateResponse.stateList
                                state.clear()
                                stateCode.clear()
                                stateLgdCode.clear()

                                for (x in stateList) {
                                    state.add(x.stateName)
                                    stateCode.add(x.stateCode) // Replace with actual field
                                    stateLgdCode.add(x.lgdStateCode) // Replace with actual field
                                }

                                stateAdapter.notifyDataSetChanged()
                            } else if (getStateResponse.responseCode == 301) {
                                showSnackBar("Please Update from PlayStore")
                            } else {
                                showSnackBar("Something went wrong")
                            }
                        } ?: showSnackBar("Internal Server Error")
                    }
                }
            }
        }
    }


            private fun collectDistrictResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getDistrictList) {
                when (it) {
                    is Resource.Loading -> showProgressBar()
                    is Resource.Error -> {
                        hideProgressBar()
                        it.error?.let { baseErrorResponse ->
                            showSnackBar(baseErrorResponse.message)
                        }
                    }

                    is Resource.Success -> {
                        hideProgressBar()
                        it.data?.let { getDistrictResponse ->
                            if (getDistrictResponse.responseCode == 200) {
                                districtList = getDistrictResponse.districtList
                                district.clear()
                                districtCode.clear()
                                districtLgdCode.clear()

                                for (x in districtList) {
                                    district.add(x.districtName)
                                    districtCode.add(x.districtCode) // Replace with actual field
                                    districtLgdCode.add(x.lgdDistrictCode) // Replace with actual field
                                }
                                districtAdapter.notifyDataSetChanged()
                            } else if (getDistrictResponse.responseCode == 301) {
                                showSnackBar("Please Update from PlayStore")
                            } else {
                                showSnackBar("Something went wrong")
                            }
                        } ?: showSnackBar("Internal Server Error")
                    }
                }
            }
        }
    }

            private fun collectSectorResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getSectorListAPI) {
                when (it) {
                    is Resource.Loading -> showProgressBar()
                    is Resource.Error -> {
                        hideProgressBar()
                        it.error?.let { baseErrorResponse ->
                            showSnackBar(baseErrorResponse.message)
                        }
                    }

                    is Resource.Success -> {
                        hideProgressBar()
                        it.data?.let { getSectorList ->
                            if (getSectorList.responseCode == 200) {
                                val   sectorList1 = getSectorList.wrappedList

                                sectorCode.clear()
                                sectorList.clear()
                                for (x in sectorList1) {
                                    sectorList.add(x.sectorName)
                                    sectorCode.add(x.sectorId)

                                }



                            } else if (getSectorList.responseCode == 301) {
                                getSectorList.responseMsg?.let { it1 -> showSnackBar(it1) }
                            }
                            else if (getSectorList.responseCode == 302) {
                                getSectorList.responseMsg?.let { it1 -> showSnackBar(it1) }
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
