package com.kaushalpanjee.common
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.kaushalpanjee.common.model.WrappedList
import com.kaushalpanjee.common.model.response.BlockList
import com.kaushalpanjee.common.model.response.DistrictList
import com.kaushalpanjee.common.model.response.GrampanchayatList
import com.kaushalpanjee.common.model.response.VillageList
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.core.util.gone
import com.kaushalpanjee.core.util.visible
import com.kaushalpanjee.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
import java.util.Calendar
import com.kaushalpanjee.R
import com.kaushalpanjee.core.util.toastLong


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    val commonViewModel: CommonViewModel by activityViewModels()


    //Boolean Values
    private var isPersonalVisible = true
    private var isAddressVisible = true
    private var isEducationalInfoVisible = true
    private var isEmploymentInfoVisible = true
    private var isTrainingInfoVisible = true
    private var isBankingInfoVisible = true
    private var isSeccInfoVisible = true
    private var isClickedPermanentYes = false
    private var isClickedPermanentNo = false

      //Other Values
      var addressLine1 =""
      var addressLine2 =""
      var pinCode =""

    //  Present
    var addressPresentLine1 =""
    var addressPresentLine2 =""
    var pinCodePresent =""


    // State var
    private var stateList: MutableList<WrappedList> = mutableListOf()
    private lateinit var stateAdapter: ArrayAdapter<String>
    private var state = ArrayList<String>()
    private var stateCode = ArrayList<String>()
    private var stateLgdCode = ArrayList<String>()
    private var selectedStateCodeItem=""
    private var  selectedStateLgdCodeItem=""
    private var selectedStateItem=""

           // district var
    private var districtList: MutableList<DistrictList> = mutableListOf()
    private lateinit var districtAdapter: ArrayAdapter<String>
    private var district = ArrayList<String>()
    private var districtCode = ArrayList<String>()
    private var districtLgdCode = ArrayList<String>()
    private var selectedDistrictCodeItem=""
    private var  selectedDistrictLgdCodeItem=""
    private var selectedDistrictItem=""


    //block var
    private var blockList: MutableList<BlockList> = mutableListOf()
    private lateinit var blockAdapter: ArrayAdapter<String>
    private var block = ArrayList<String>()
    private var blockCode = ArrayList<String>()
    private var blockLgdCode = ArrayList<String>()
    private var selectedBlockCodeItem=""
    private var  selectedbBlockLgdCodeItem=""
    private var selectedBlockItem=""



    //GP var
    private var gpList: MutableList<GrampanchayatList> = mutableListOf()
    private lateinit var gpAdapter: ArrayAdapter<String>
    private var gp = ArrayList<String>()
    private var gpCode = ArrayList<String>()
    private var gpLgdCode = ArrayList<String>()
    private var selectedGpCodeItem=""
    private var  selectedbGpLgdCodeItem=""
    private var selectedGpItem=""


    //Village var
    private var villageList: MutableList<VillageList> = mutableListOf()
    private lateinit var villageAdapter: ArrayAdapter<String>
    private var village = ArrayList<String>()
    private var villageCode = ArrayList<String>()
    private var villageLgdCode = ArrayList<String>()
    private var selectedVillageCodeItem=""
    private var  selectedbVillageLgdCodeItem=""
    private var selectedVillageItem=""

    //  Present Address Variables


    // State var
    private var statePresentList: MutableList<WrappedList> = mutableListOf()
    private lateinit var statePresentAdapter: ArrayAdapter<String>
    private var statePresent = ArrayList<String>()
    private var statePresentCode = ArrayList<String>()
    private var statePresentLgdCode = ArrayList<String>()
    private var selectedStatePresentCodeItem=""
    private var  selectedStatePresentLgdCodeItem=""
    private var selectedStatePresentItem=""

    // district var
    private var districtPresentList: MutableList<DistrictList> = mutableListOf()
    private lateinit var districtPresentAdapter: ArrayAdapter<String>
    private var districtPresent = ArrayList<String>()
    private var districtPresentCode = ArrayList<String>()
    private var districtPresentLgdCode = ArrayList<String>()
    private var selectedDistrictPresentCodeItem=""
    private var  selectedDistrictPresentLgdCodeItem=""
    private var selectedDistrictPresentItem=""


    //block var
    private var blockListPresent: MutableList<BlockList> = mutableListOf()
    private lateinit var blockPresentAdapter: ArrayAdapter<String>
    private var blockPresent = ArrayList<String>()
    private var blockPresentCode = ArrayList<String>()
    private var blockPresentLgdCode = ArrayList<String>()
    private var selectedBlockPresentCodeItem=""
    private var  selectedbBlockPresentLgdCodeItem=""
    private var selectedBlockPresentItem=""



    //GP var
    private var gpPresentList: MutableList<GrampanchayatList> = mutableListOf()
    private lateinit var gpPresentAdapter: ArrayAdapter<String>
    private var gpPresent = ArrayList<String>()
    private var gpPresentCode = ArrayList<String>()
    private var gpPresentLgdCode = ArrayList<String>()
    private var selectedGpPresentCodeItem=""
    private var  selectedbGpPresentLgdCodeItem=""
    private var selectedGpPresentItem=""


    //Village var
    private var villagePresentList: MutableList<VillageList> = mutableListOf()
    private lateinit var villagePresentAdapter: ArrayAdapter<String>
    private var villagePresent = ArrayList<String>()
    private var villagePresentCode = ArrayList<String>()
    private var villagePresentLgdCode = ArrayList<String>()
    private var selectedVillagePresentCodeItem=""
    private var  selectedbVillagePresentLgdCodeItem=""
    private var selectedVillagePresentItem=""



    //yaha tak

    // Calendar instance to get current date
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        init()
    }



    private fun init() {
        listener()
        collectStateResponse()
        collectDistrictResponse()
        collectBlockResponse()
        collectGpResponse()
        collectVillageResponse()
        commonViewModel.getStateListApi()


        }


    @SuppressLint("SetTextI18n")
    private fun listener() {


        //Adapter state setting
        stateAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            state
        )

        binding.SpinnerStateName.setAdapter(stateAdapter)


        //Adapter District setting

        districtAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            district
        )

        binding.spinnerDistrict.setAdapter(districtAdapter)


        //Adapter Block setting

        blockAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            block
        )

        binding.spinnerBlock.setAdapter(blockAdapter)

        //Adapter GP setting

       gpAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            gp
        )

        binding.spinnerGp.setAdapter(gpAdapter)


        //Adapter Village setting

        villageAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            village
        )

        binding.spinnerVillage.setAdapter(villageAdapter)

        binding.llTopPersonal.setOnClickListener {

            if (isPersonalVisible){
                isPersonalVisible = false
                binding.personalExpand.visible()
                binding.viewSecc.visible()
            }else {
                isPersonalVisible = true
                binding.personalExpand.gone()
                binding.viewSecc.gone()
            }
        }

/*
        binding.llTopSecc.setOnClickListener {
            if (isSeccInfoVisible){

                isSeccInfoVisible = false
                binding.expandSecc.visible()
                binding.viewSeccc.visible()
            }else {
                isSeccInfoVisible = true
                binding.expandSecc.gone()
                binding.viewSeccc.gone()
            }

        }
*/

        binding.llTopAddress.setOnClickListener {

            if (isAddressVisible){
                isAddressVisible = false
                binding.expandAddress.visible()
                binding.viewAddress.visible()
            }else {
                isAddressVisible = true
                binding.expandAddress.gone()
                binding.viewAddress.gone()
            }
        }

        binding.llTopEducational.setOnClickListener {

            if (isEducationalInfoVisible){
                isEducationalInfoVisible = false
                binding.expandEducational.visible()
                binding.viewEducational.visible()
            }else {
                isEducationalInfoVisible = true
                binding.expandEducational.gone()
                binding.viewEducational.gone()
            }
        }

        binding.llTopEmployment.setOnClickListener {

            if (isEmploymentInfoVisible){
                isEmploymentInfoVisible = false
                binding.expandEmployment.visible()
                binding.viewEmployment.visible()
            }else {
                isEmploymentInfoVisible = true
                binding.expandEmployment.gone()
                binding.viewEmployment.gone()
            }
        }

        binding.llTopBanking.setOnClickListener {

            if (isBankingInfoVisible){
                isBankingInfoVisible = false
                binding.expandBanking.visible()
                binding.viewBanking.visible()
            }else {
                isBankingInfoVisible = true
                binding.expandBanking.gone()
                binding.viewBanking.gone()
            }
        }
        binding.llTopTraining.setOnClickListener {

            if (isTrainingInfoVisible){
                isTrainingInfoVisible = false
                binding.expandTraining.visible()
                binding.viewTraining.visible()
            }else {
                isTrainingInfoVisible = true
                binding.expandTraining.gone()
                binding.viewTraining.gone()
            }
        }

        binding.tvClickYearOfPassing.setOnClickListener {

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Save and display the selected date
                    val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    binding.tvClickYearOfPassing.text = formattedDate.toString()
                },
                year, month, day
            )
            datePickerDialog.datePicker.maxDate = calendar.timeInMillis
            datePickerDialog.show()
            datePickerDialog.show()
        }

        binding.tvClickYearOfPassingTech.setOnClickListener {

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Save and display the selected date
                    val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    binding.tvClickYearOfPassingTech.text = formattedDate.toString()
                },
                year, month, day
            )
            datePickerDialog.datePicker.maxDate = calendar.timeInMillis
            datePickerDialog.show()
            datePickerDialog.show()
        }

        binding.tvClickPreviouslycompletedduring.setOnClickListener {

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Save and display the selected date
                    val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    binding.tvClickPreviouslycompletedduring.text = formattedDate.toString()
                },
                year, month, day
            )
            datePickerDialog.datePicker.maxDate = calendar.timeInMillis
            datePickerDialog.show()
            datePickerDialog.show()
        }



        //State selection
        binding.SpinnerStateName.setOnItemClickListener { parent, view, position, id ->
             selectedStateItem = parent.getItemAtPosition(position).toString()
            if (position in state.indices) {
                 selectedStateCodeItem = stateCode[position]
               selectedStateLgdCodeItem = stateLgdCode[position]
                commonViewModel.getDistrictListApi(selectedStateCodeItem)
            } else {
                Toast.makeText(requireContext(), "Invalid selection", Toast.LENGTH_SHORT).show()
            }
        }



        //District selection
        binding.spinnerDistrict.setOnItemClickListener { parent, view, position, id ->
            selectedDistrictItem = parent.getItemAtPosition(position).toString()
            if (position in district.indices) {
                selectedDistrictCodeItem = districtCode[position]
                selectedDistrictLgdCodeItem = districtLgdCode[position]
                commonViewModel.getBlockListApi(selectedDistrictCodeItem)
            } else {
                Toast.makeText(requireContext(), "Invalid selection", Toast.LENGTH_SHORT).show()
            }
        }

        //Block Spinner
        binding.spinnerBlock.setOnItemClickListener { parent, view, position, id ->
            selectedBlockItem= parent.getItemAtPosition(position).toString()
            if (position in block.indices) {
                selectedBlockCodeItem = blockCode[position]
                selectedbBlockLgdCodeItem = blockLgdCode[position]
                commonViewModel.getGpListApi(selectedBlockCodeItem)
            } else {
                Toast.makeText(requireContext(), "Invalid selection", Toast.LENGTH_SHORT).show()
            }
        }


        //GP Spinner
        binding.spinnerGp.setOnItemClickListener { parent, view, position, id ->
            selectedGpItem= parent.getItemAtPosition(position).toString()
            if (position in gp.indices) {
                selectedGpCodeItem = gpCode[position]
                selectedbGpLgdCodeItem = gpLgdCode[position]
                commonViewModel.getVillageListApi(selectedGpCodeItem)

            } else {
                Toast.makeText(requireContext(), "Invalid selection", Toast.LENGTH_SHORT).show()
            }
        }


        //Village Spinner
        binding.spinnerVillage.setOnItemClickListener { parent, view, position, id ->
            selectedVillageItem= parent.getItemAtPosition(position).toString()
            if (position in village.indices) {
                selectedVillageCodeItem = villageCode[position]
                selectedbVillageLgdCodeItem = villageLgdCode[position]

            } else {
                Toast.makeText(requireContext(), "Invalid selection", Toast.LENGTH_SHORT).show()
            }
        }


        // If Present Address is same as permanent

        binding.optionllSamePermanentYesSelect.setOnClickListener {


            if (selectedStateCodeItem.isNotEmpty() &&
                selectedDistrictCodeItem.isNotEmpty() &&
                selectedBlockCodeItem.isNotEmpty() &&
                selectedGpCodeItem.isNotEmpty() &&
                selectedVillageCodeItem.isNotEmpty()) {

                binding.optionllSamePermanentYesSelect.setBackgroundResource(R.drawable.card_background_selected) // Reset to default
                binding.optionSamePermanentNoSelect.setBackgroundResource(R.drawable.card_background) // Change to clicked color

                isClickedPermanentYes=true
                isClickedPermanentNo= false

                binding.llPresentAddressState.gone()
                binding.llPresentAddressDistrict.gone()
                binding.llPresentAddressBlock.gone()
                binding.llPresentAddressGp.gone()
                binding.llPresentAddressVillage.gone()
                binding.llPresentAddressAdressLine.gone()

                //Set State Value
                selectedStatePresentCodeItem=  selectedStateCodeItem
                selectedStatePresentLgdCodeItem=  selectedStateLgdCodeItem
                selectedStatePresentItem=  selectedStateItem


                //Set District Value

                selectedDistrictPresentCodeItem= selectedDistrictCodeItem
                selectedDistrictPresentLgdCodeItem=selectedDistrictLgdCodeItem
                selectedDistrictPresentItem=selectedDistrictItem

                //Set Block Value

                selectedBlockPresentCodeItem=  selectedBlockCodeItem
                selectedbBlockPresentLgdCodeItem= selectedbBlockLgdCodeItem
                selectedBlockPresentItem=  selectedBlockItem

                //Set GP Value
                selectedGpPresentCodeItem=  selectedGpCodeItem
                selectedbGpPresentLgdCodeItem=  selectedbGpLgdCodeItem
                selectedGpPresentItem=  selectedGpItem


                //Set Village Value
                selectedVillagePresentCodeItem=  selectedVillageCodeItem
                selectedbVillagePresentLgdCodeItem= selectedbVillageLgdCodeItem
                selectedVillagePresentItem= selectedVillageItem

                //others

                addressPresentLine1 =  addressLine1
                addressPresentLine2 = addressLine2
                pinCodePresent = pinCode


            }
            else

                toastLong("Please Complete Your Permanent Address First")


        }

        // If Present Address is not same as permanent


        binding.optionSamePermanentNoSelect.setOnClickListener {
            isClickedPermanentNo= true
            isClickedPermanentYes=false

            binding.optionSamePermanentNoSelect.setBackgroundResource(R.drawable.card_background_selected) // Reset to default
                binding.optionllSamePermanentYesSelect.setBackgroundResource(R.drawable.card_background) // Change to clicked color
            toastLong("isClickedPermanentYe: $isClickedPermanentNo+ isClickedPermanentYes: $isClickedPermanentYes")


            binding.llPresentAddressState.visible()
            binding.llPresentAddressDistrict.visible()
            binding.llPresentAddressBlock.visible()
            binding.llPresentAddressGp.visible()
            binding.llPresentAddressVillage.visible()
            binding.llPresentAddressAdressLine.visible()

            //Set State Value
            selectedStatePresentCodeItem=  ""
            selectedStatePresentLgdCodeItem=  ""
            selectedStatePresentItem=  ""


            //Set District Value

            selectedDistrictPresentCodeItem= ""
            selectedDistrictPresentLgdCodeItem=""
            selectedDistrictPresentItem=""

            //Set Block Value

            selectedBlockPresentCodeItem=  ""
            selectedbBlockPresentLgdCodeItem= ""
            selectedBlockPresentItem=  ""

            //Set GP Value
            selectedGpPresentCodeItem=  ""
            selectedbGpPresentLgdCodeItem=  ""
            selectedGpPresentItem=  ""


            //Set Village Value
            selectedVillagePresentCodeItem=  ""
            selectedbVillagePresentLgdCodeItem= ""
            selectedVillagePresentItem= ""


            //others

            addressPresentLine1 =  ""
            addressPresentLine2 = ""
            pinCodePresent = ""

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
                            showSnackBar(baseErrorResponse.message)
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

    private fun collectBlockResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getBlockList) {
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
                        it.data?.let { getBlockResponse ->
                            if (getBlockResponse.responseCode == 200) {
                                blockList = getBlockResponse.blockList
                                block.clear()
                                blockCode.clear()
                                blockLgdCode.clear()

                                for (x in blockList) {
                                    block.add(x.blockName)
                                    blockCode.add(x.blockCode) // Replace with actual field
                                    blockLgdCode.add(x.lgdBlockCode) // Replace with actual field
                                }
                                blockAdapter.notifyDataSetChanged()
                            } else if (getBlockResponse.responseCode == 301) {
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

    private fun collectGpResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getGpList) {
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
                        it.data?.let { getGpResponse ->
                            if (getGpResponse.responseCode == 200) {
                                gpList = getGpResponse.grampanchayatList
                                gp.clear()
                                gpCode.clear()
                                gpLgdCode.clear()

                                for (x in gpList) {
                                    gp.add(x.gpName)
                                    gpCode.add(x.gpCode) // Replace with actual field
                                    gpLgdCode.add(x.lgdGpCode) // Replace with actual field
                                }
                                blockAdapter.notifyDataSetChanged()
                            } else if (getGpResponse.responseCode == 301) {
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

    private fun collectVillageResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getVillageList) {
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
                        it.data?.let { getVillageResponse ->
                            if (getVillageResponse.responseCode == 200) {
                                villageList = getVillageResponse.villageList
                                village.clear()
                                villageCode.clear()
                                villageLgdCode.clear()

                                for (x in villageList) {
                                    village.add(x.villageName)
                                    villageCode.add(x.villageCode) // Replace with actual field
                                    villageLgdCode.add(x.lgdVillageCode) // Replace with actual field
                                }
                                villageAdapter.notifyDataSetChanged()
                            } else if (getVillageResponse.responseCode == 301) {
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






}
