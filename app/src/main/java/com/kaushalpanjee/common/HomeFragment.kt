package com.kaushalpanjee.common
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
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
import android.Manifest // For permission constants
import android.app.AlertDialog
import android.content.pm.PackageManager // For checking permissions
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat // For permission checks
import com.kaushalpanjee.common.model.request.ShgValidateReq
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.createHalfCircleProgressBitmap
import com.kaushalpanjee.core.util.setDrawable
import com.utilize.core.util.FileUtils.Companion.getFileName
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val commonViewModel: CommonViewModel by activityViewModels()

    private var currentRequestPurpose: String? = null
    private val REQUEST_PICK_VOTER_ID = 101
    private val REQUEST_PICK_DRIVING_LICENSE = 102
    private val REQUEST_PICK_MINORITY = 103
    private val REQUEST_PICK_CATEGORY = 104
    private val REQUEST_PICK_PWD = 105
    private val REQUEST_PICK_ANTOYADA = 106
    private val REQUEST_PICK_RSBY = 107
    private val REQUEST_PICK_RESIDENCE = 107
    private val PERMISSION_READ_MEDIA_IMAGES = 201
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
    private var addressLine1 =""
      private var addressLine2 =""
      private var pinCode =""
     private var voterIdImage=""
     private var voterIdNo=""
     private var guardianName=""
     private var motherName=""
     private var guardianMobileNumber=""
     private var yearlyIncomeFamily=""
     private var drivingLicenceNumber=""
     private var categoryCertiImage=""
     private var drivingLicenceImage=""
    private var minorityStatus=""
    private var minorityImage=""
    private var pwdStatus=""
    private var pwdImage=""
    private var technicalEducationStatus=""
    private var antoyadaStatus=""
    private var antoyadaImage=""
    private var selectedCategoryItem=""
    private var selectedMaritalItem=""
    private var selectedHighestEducationItem=""
    private var shgValidateStatus=""
    private var shgName=""
    private var shgCode=""
    private var shgStatus=""
    private var rsbyStatus=""
    private var rsbyImage=""
    private var pipStatus=""
    private var residenceImage=""
    private var highestEducationDate=""





    //Secc Address


    // State var
    private lateinit var stateSeccAdapter: ArrayAdapter<String>
    private var selectedSeccStateCodeItem=""
    private var  selectedSeccStateLgdCodeItem=""
    private var selectedSeccStateItem=""


    // district var
    private lateinit var districtSeccAdapter: ArrayAdapter<String>
    private var selectedSeccDistrictCodeItem=""
    private var  selectedSeccDistrictLgdCodeItem=""
    private var selectedSeccDistrictItem=""


    //block var
    private lateinit var blockSeccAdapter: ArrayAdapter<String>
    private var selectedSeccBlockCodeItem=""
    private var  selectedSeccBlockLgdCodeItem=""
    private var selectedSeccBlockItem=""



    //GP var
    private lateinit var gpSeccAdapter: ArrayAdapter<String>
    private var selectedSeccGpCodeItem=""
    private var  selectedSeccGpLgdCodeItem=""
    private var selectedSeccGpItem=""


    //Village var
    private lateinit var villageSeccAdapter: ArrayAdapter<String>
    private var selectedSeccVillageCodeItem=""
    private var  selectedbSeccVillageLgdCodeItem=""
    private var selectedSeccVillageItem=""


    //  Present
    var addressPresentLine1 =""
    var addressPresentLine2 =""
    var pinCodePresent =""


    private lateinit var categoryAdapter: ArrayAdapter<String>
    private lateinit var maritalAdapter: ArrayAdapter<String>
    private lateinit var highestEducationAdapter: ArrayAdapter<String>


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
    private var courseesName = ArrayList<String>()
    private var courseesCode = ArrayList<String>()
    private var villageCode = ArrayList<String>()
    private var villageLgdCode = ArrayList<String>()
    private var selectedVillageCodeItem=""
    private var  selectedbVillageLgdCodeItem=""
    private var selectedVillageItem=""

    //  Present Address Variables


    // State var
    private lateinit var statePresentAdapter: ArrayAdapter<String>
    private var statePresent = ArrayList<String>()
    private var selectedStatePresentCodeItem=""
    private var  selectedStatePresentLgdCodeItem=""
    private var selectedStatePresentItem=""

    // district var
    private lateinit var districtPresentAdapter: ArrayAdapter<String>
    private var districtPresent = ArrayList<String>()
    private var selectedDistrictPresentCodeItem=""
    private var  selectedDistrictPresentLgdCodeItem=""
    private var selectedDistrictPresentItem=""


    //block var
    private lateinit var blockPresentAdapter: ArrayAdapter<String>
    private var blockPresent = ArrayList<String>()
    private var selectedBlockPresentCodeItem=""
    private var  selectedbBlockPresentLgdCodeItem=""
    private var selectedBlockPresentItem=""



    //GP var
    private lateinit var gpPresentAdapter: ArrayAdapter<String>
    private var gpPresent = ArrayList<String>()
    private var selectedGpPresentCodeItem=""
    private var  selectedbGpPresentLgdCodeItem=""
    private var selectedGpPresentItem=""


    //Village var
    private lateinit var villagePresentAdapter: ArrayAdapter<String>
    private var villagePresent = ArrayList<String>()
    private var selectedVillagePresentCodeItem=""
    private var  selectedbVillagePresentLgdCodeItem=""
    private var selectedVillagePresentItem=""

    private lateinit var TechEduAdapter: ArrayAdapter<String>
    private lateinit var TechEduDomaiAdapter: ArrayAdapter<String>


    //Value for dropdown
    private val categoryList = listOf("SC", "ST", "OBC", "OTHER")
    private val maritalList = listOf("Married", "Unmarried", "Divorce")
   private val highestEducationList = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")





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
        collectShgValidateResponse()
        commonViewModel.getStateListApi()

        binding.ivProgress.setImageBitmap(
            createHalfCircleProgressBitmap(300,300,40f,
                ContextCompat.getColor(requireContext(),R.color.color_dark_green),
                ContextCompat.getColor(requireContext(),R.color.color_green),35f,20f,
                ContextCompat.getColor(requireContext(),R.color.black),
                ContextCompat.getColor(requireContext(),R.color.color_background))
        )

        }


    @SuppressLint("SetTextI18n")
    private fun listener() {


        binding.llPresentAddressState.gone()
        binding.llPresentAddressDistrict.gone()
        binding.llPresentAddressBlock.gone()
        binding.llPresentAddressGp.gone()
        binding.llPresentAddressVillage.gone()
        binding.llPresentAddressAdressLine.gone()


        //Adapter Category

         categoryAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            categoryList
        )
        binding.SpinnerCategory.setAdapter(categoryAdapter)


        //Adapter Marital

        maritalAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            maritalList
        )

        binding.SpinnerMarital.setAdapter(maritalAdapter)

        //Adapter Highest Education

        highestEducationAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            highestEducationList
        )

        binding.spinnerHighestEducation.setAdapter(highestEducationAdapter)

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


        //Present Address Adapter

        //Adapter state setting
        statePresentAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            state
        )

        binding.SpinnerPresentAddressStateName.setAdapter(statePresentAdapter)


        //Adapter District setting

        districtPresentAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            district
        )

        binding.spinnerPresentAddressDistrict.setAdapter(districtPresentAdapter)


        //Adapter Block setting

        blockPresentAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            block
        )

        binding.spinnerPresentAddressBlock.setAdapter(blockPresentAdapter)

        //Adapter GP setting

        gpPresentAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            gp
        )

        binding.spinnerPresentAddressGp.setAdapter(gpPresentAdapter)


        //Adapter Village setting

        villagePresentAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            village
        )

        binding.spinnerPresentAddressVillage.setAdapter(villagePresentAdapter)


        //Secc Address Adapter

        stateSeccAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            state
        )

        binding.spinnerStateSecc.setAdapter(stateSeccAdapter)



        //Adapter District setting

        districtSeccAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            district
        )

        binding.spinnerDistrictSecc.setAdapter(districtSeccAdapter)



        //Adapter Block setting

        blockSeccAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            block
        )

        binding.spinnerBlockSecc.setAdapter(blockSeccAdapter)


        //Adapter GP setting

        gpSeccAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            gp
        )

        binding.spinnerGpSecc.setAdapter(gpSeccAdapter)



        //Adapter Village setting

        villageSeccAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            village
        )

        binding.spinnerVillageSecc.setAdapter(villageSeccAdapter)


        binding.llTopPersonal.setOnClickListener {

            if (isPersonalVisible){
                isPersonalVisible = false
                binding.personalExpand.visible()
                binding.viewSecc.visible()
                binding.tvPersonal.setDrawable(null,null,
                    AppCompatResources.getDrawable(requireContext(),R.drawable.ic_verified),null)
            }else {
                binding.tvPersonal.setDrawable(null,null,null,null)
                isPersonalVisible = true
                binding.personalExpand.gone()
                binding.viewSecc.gone()
            }
        }

        binding.llTopSecc.setOnClickListener {
            if (isSeccInfoVisible){

                isSeccInfoVisible = false
                binding.expandSecc.visible()
                binding.viewSeccc.visible()

                setDropdownValue(binding.spinnerStateSecc, selectedStateItem, state)
                setDropdownValue(binding.spinnerDistrictSecc, selectedDistrictItem, district)
                setDropdownValue(binding.spinnerBlockSecc, selectedBlockItem, block)
                setDropdownValue(binding.spinnerGpSecc, selectedGpItem, gp)
                setDropdownValue(binding.spinnerVillageSecc, selectedVillageItem, village)

                selectedSeccStateCodeItem=selectedStateCodeItem
                selectedSeccDistrictCodeItem=selectedDistrictCodeItem
                selectedSeccBlockCodeItem=selectedBlockCodeItem
                selectedSeccGpCodeItem=selectedGpCodeItem
                selectedSeccVillageCodeItem=selectedVillageCodeItem

            }else {
                isSeccInfoVisible = true
                binding.expandSecc.gone()
                binding.viewSeccc.gone()
            }

        }

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
            showMonthYearPicker { selectedYear, selectedMonth ->
                // Handle the selected month and year

                highestEducationDate= "$selectedMonth/$selectedYear"
                binding.tvClickYearOfPassing.text = "$selectedMonth/$selectedYear"

            }
        }

        binding.tvClickYearOfPassingTech.setOnClickListener {

            showMonthYearPicker { selectedYear, selectedMonth ->
                // Handle the selected month and year
                binding.tvClickYearOfPassingTech.text = "$selectedMonth/$selectedYear"

            }
        }

        binding.tvClickPreviouslycompletedduring.setOnClickListener {

            showMonthYearPicker { selectedYear, selectedMonth ->
                // Handle the selected month and year
                binding.tvClickPreviouslycompletedduring.text = "$selectedMonth/$selectedYear"

            }
        }

        //Category Selection


            binding.SpinnerCategory.setOnItemClickListener { parent, view, position, id ->
                selectedCategoryItem = parent.getItemAtPosition(position).toString()
        }


        // Marital selection

        binding.SpinnerMarital.setOnItemClickListener { parent, view, position, id ->
            selectedMaritalItem = parent.getItemAtPosition(position).toString()
        }


        // Highest Education selection

        binding.spinnerHighestEducation.setOnItemClickListener { parent, view, position, id ->
            selectedHighestEducationItem = parent.getItemAtPosition(position).toString()
        }



        // Secc State selection

        binding.spinnerStateSecc.setOnItemClickListener { parent, view, position, id ->
            selectedSeccStateItem = parent.getItemAtPosition(position).toString()
            if (position in state.indices) {
                selectedSeccStateCodeItem = stateCode[position]
                selectedSeccStateLgdCodeItem = stateLgdCode[position]
                commonViewModel.getDistrictListApi(selectedSeccStateCodeItem)

                //Clearing Data
                selectedSeccDistrictCodeItem=""
                selectedSeccDistrictLgdCodeItem=""
                selectedSeccDistrictItem=""
                binding.spinnerDistrictSecc.clearFocus()
                binding.spinnerDistrictSecc.setText("", false)

                selectedSeccBlockCodeItem=""
                selectedSeccBlockLgdCodeItem=""
                selectedBlockItem=""
                binding.spinnerBlockSecc.clearFocus()
                binding.spinnerBlockSecc.setText("", false)

                district.clear()
                block.clear()
                gp.clear()
                village.clear()



                selectedSeccGpCodeItem=""
                selectedSeccGpLgdCodeItem=""
                selectedSeccGpItem=""
                binding.spinnerGpSecc.clearFocus()
                binding.spinnerGpSecc.setText("", false)


                selectedSeccVillageCodeItem=""
                selectedbSeccVillageLgdCodeItem=""
                selectedSeccVillageItem=""
                binding.spinnerVillageSecc.clearFocus()
                binding.spinnerVillageSecc.setText("", false)


            } else {
                Toast.makeText(requireContext(), "Invalid selection", Toast.LENGTH_SHORT).show()
            }
        }


        // Secc District selection

        binding.spinnerDistrictSecc.setOnItemClickListener { parent, view, position, id ->
            selectedSeccDistrictItem = parent.getItemAtPosition(position).toString()
            if (position in district.indices) {
                selectedSeccDistrictCodeItem = districtCode[position]
                selectedSeccDistrictLgdCodeItem = districtLgdCode[position]
                commonViewModel.getBlockListApi(selectedSeccDistrictCodeItem)

                selectedSeccBlockCodeItem=""
                selectedSeccBlockLgdCodeItem=""
                selectedBlockItem=""
                binding.spinnerBlockSecc.clearFocus()
                binding.spinnerBlockSecc.setText("", false)



                selectedSeccGpCodeItem=""
                selectedSeccGpLgdCodeItem=""
                selectedSeccGpItem=""
                binding.spinnerGpSecc.clearFocus()
                binding.spinnerGpSecc.setText("", false)


                selectedSeccVillageCodeItem=""
                selectedbSeccVillageLgdCodeItem=""
                selectedSeccVillageItem=""
                binding.spinnerVillageSecc.clearFocus()
                binding.spinnerVillageSecc.setText("", false)


            } else {
                Toast.makeText(requireContext(), "Invalid selection", Toast.LENGTH_SHORT).show()
            }
        }

        // Secc Block selection

        binding.spinnerBlockSecc.setOnItemClickListener { parent, view, position, id ->
            selectedSeccBlockItem = parent.getItemAtPosition(position).toString()
            if (position in block.indices) {
                selectedSeccBlockCodeItem = blockCode[position]
                selectedSeccBlockLgdCodeItem = blockLgdCode[position]
                commonViewModel.getGpListApi(selectedSeccBlockCodeItem)


                selectedSeccGpCodeItem=""
                selectedSeccGpLgdCodeItem=""
                selectedSeccGpItem=""
                binding.spinnerGpSecc.clearFocus()
                binding.spinnerGpSecc.setText("", false)


                selectedSeccVillageCodeItem=""
                selectedbSeccVillageLgdCodeItem=""
                selectedSeccVillageItem=""
                binding.spinnerVillageSecc.clearFocus()
                binding.spinnerVillageSecc.setText("", false)


            } else {
                Toast.makeText(requireContext(), "Invalid selection", Toast.LENGTH_SHORT).show()
            }
        }

        // Secc GP selection

        binding.spinnerGpSecc.setOnItemClickListener { parent, view, position, id ->
            selectedSeccGpItem = parent.getItemAtPosition(position).toString()
            if (position in gp.indices) {
                selectedSeccGpCodeItem = gpCode[position]
                selectedSeccGpLgdCodeItem = gpLgdCode[position]
                commonViewModel.getVillageListApi(selectedSeccGpCodeItem)


                selectedSeccVillageCodeItem=""
                selectedbSeccVillageLgdCodeItem=""
                selectedSeccVillageItem=""
                binding.spinnerVillageSecc.clearFocus()
                binding.spinnerVillageSecc.setText("", false)


            } else {
                Toast.makeText(requireContext(), "Invalid selection", Toast.LENGTH_SHORT).show()
            }
        }

        // Secc Village selection

        binding.spinnerVillageSecc.setOnItemClickListener { parent, view, position, id ->
            selectedSeccVillageItem = parent.getItemAtPosition(position).toString()
            if (position in village.indices) {
                selectedSeccVillageCodeItem = villageCode[position]
                selectedbSeccVillageLgdCodeItem = villageLgdCode[position]


            } else {
                Toast.makeText(requireContext(), "Invalid selection", Toast.LENGTH_SHORT).show()
            }
        }


        //State selection
        binding.SpinnerStateName.setOnItemClickListener { parent, view, position, id ->
             selectedStateItem = parent.getItemAtPosition(position).toString()
            if (position in state.indices) {
                 selectedStateCodeItem = stateCode[position]
               selectedStateLgdCodeItem = stateLgdCode[position]
                commonViewModel.getDistrictListApi(selectedStateCodeItem)

                //Clearing Data
                selectedDistrictCodeItem=""
                selectedDistrictLgdCodeItem=""
                selectedDistrictItem=""
                binding.spinnerDistrict.clearFocus()
                binding.spinnerDistrict.setText("", false)

                selectedDistrictPresentCodeItem=""
                selectedDistrictPresentLgdCodeItem=""
                selectedDistrictPresentItem=""
                binding.spinnerPresentAddressDistrict.clearFocus()
                binding.spinnerPresentAddressDistrict.setText("", false)
                binding.SpinnerPresentAddressStateName.clearFocus()
                binding.SpinnerPresentAddressStateName.setText("", false)


                selectedBlockCodeItem=""
                selectedbBlockLgdCodeItem=""
                selectedBlockItem=""
                binding.spinnerBlock.clearFocus()
                binding.spinnerBlock.setText("", false)

                selectedBlockPresentCodeItem=""
                selectedbBlockPresentLgdCodeItem=""
                selectedBlockPresentItem=""
                binding.spinnerPresentAddressBlock.clearFocus()
                binding.spinnerPresentAddressBlock.setText("", false)



                selectedGpCodeItem=""
                selectedbGpLgdCodeItem=""
                selectedGpItem=""
                binding.spinnerGp.clearFocus()
                binding.spinnerGp.setText("", false)


                selectedGpPresentCodeItem=""
                selectedbGpPresentLgdCodeItem=""
                selectedGpPresentItem=""
                binding.spinnerPresentAddressGp.clearFocus()
                binding.spinnerPresentAddressGp.setText("", false)


                selectedVillageCodeItem=""
                selectedbVillageLgdCodeItem=""
                selectedVillageItem=""
                binding.spinnerVillage.clearFocus()
                binding.spinnerVillage.setText("", false)


                selectedVillagePresentCodeItem=""
                selectedbVillagePresentLgdCodeItem=""
                selectedVillagePresentItem=""
                binding.spinnerPresentAddressVillage.clearFocus()
                binding.spinnerPresentAddressVillage.setText("", false)




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




                selectedBlockCodeItem=""
                selectedbBlockLgdCodeItem=""
                selectedBlockItem=""
                binding.spinnerBlock.clearFocus()
                binding.spinnerBlock.setText("", false)





                selectedGpCodeItem=""
                selectedbGpLgdCodeItem=""
                selectedGpItem=""
                binding.spinnerGp.clearFocus()
                binding.spinnerGp.setText("", false)



                binding.spinnerPresentAddressGp.clearFocus()
                binding.spinnerPresentAddressGp.setText("", false)


                selectedVillageCodeItem=""
                selectedbVillageLgdCodeItem=""
                selectedVillageItem=""
                binding.spinnerVillage.clearFocus()
                binding.spinnerVillage.setText("", false)


                selectedVillagePresentCodeItem=""
                selectedbVillagePresentLgdCodeItem=""
                selectedVillagePresentItem=""
                selectedDistrictPresentCodeItem=""
                selectedDistrictPresentLgdCodeItem=""
                selectedDistrictPresentItem=""
                selectedGpPresentCodeItem=""
                selectedbGpPresentLgdCodeItem=""
                selectedGpPresentItem=""
                selectedBlockPresentCodeItem=""
                selectedbBlockPresentLgdCodeItem=""
                selectedBlockPresentItem=""
                selectedDistrictPresentCodeItem=""
                selectedDistrictPresentLgdCodeItem=""
                selectedDistrictPresentItem=""


                binding.spinnerPresentAddressVillage.clearFocus()
                binding.spinnerPresentAddressVillage.setText("", false)
                binding.spinnerPresentAddressDistrict.clearFocus()
                binding.spinnerPresentAddressDistrict.setText("", false)
                binding.SpinnerPresentAddressStateName.clearFocus()
                binding.SpinnerPresentAddressStateName.setText("", false)
                binding.spinnerPresentAddressBlock.clearFocus()
                binding.spinnerPresentAddressBlock.setText("", false)

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


                selectedGpCodeItem=""
                selectedbGpLgdCodeItem=""
                selectedGpItem=""
                binding.spinnerGp.clearFocus()
                binding.spinnerGp.setText("", false)





                selectedVillageCodeItem=""
                selectedbVillageLgdCodeItem=""
                selectedVillageItem=""
                binding.spinnerVillage.clearFocus()
                binding.spinnerVillage.setText("", false)


                selectedVillagePresentCodeItem=""
                selectedbVillagePresentLgdCodeItem=""
                selectedVillagePresentItem=""
                selectedDistrictPresentCodeItem=""
                selectedDistrictPresentLgdCodeItem=""
                selectedDistrictPresentItem=""
                selectedGpPresentCodeItem=""
                selectedbGpPresentLgdCodeItem=""
                selectedGpPresentItem=""
                selectedBlockPresentCodeItem=""
                selectedbBlockPresentLgdCodeItem=""
                selectedBlockPresentItem=""
                selectedDistrictPresentCodeItem=""
                selectedDistrictPresentLgdCodeItem=""
                selectedDistrictPresentItem=""


                binding.spinnerPresentAddressVillage.clearFocus()
                binding.spinnerPresentAddressVillage.setText("", false)
                binding.spinnerPresentAddressDistrict.clearFocus()
                binding.spinnerPresentAddressDistrict.setText("", false)
                binding.SpinnerPresentAddressStateName.clearFocus()
                binding.SpinnerPresentAddressStateName.setText("", false)
                binding.spinnerPresentAddressBlock.clearFocus()
                binding.spinnerPresentAddressBlock.setText("", false)
                binding.spinnerPresentAddressGp.clearFocus()
                binding.spinnerPresentAddressGp.setText("", false)
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


                selectedVillageCodeItem=""
                selectedbVillageLgdCodeItem=""
                selectedVillageItem=""
                binding.spinnerVillage.clearFocus()
                binding.spinnerVillage.setText("", false)


                selectedVillagePresentCodeItem=""
                selectedbVillagePresentLgdCodeItem=""
                selectedVillagePresentItem=""
                selectedDistrictPresentCodeItem=""
                selectedDistrictPresentLgdCodeItem=""
                selectedDistrictPresentItem=""
                selectedGpPresentCodeItem=""
                selectedbGpPresentLgdCodeItem=""
                selectedGpPresentItem=""
                selectedBlockPresentCodeItem=""
                selectedbBlockPresentLgdCodeItem=""
                selectedBlockPresentItem=""
                selectedDistrictPresentCodeItem=""
                selectedDistrictPresentLgdCodeItem=""
                selectedDistrictPresentItem=""


                binding.spinnerPresentAddressVillage.clearFocus()
                binding.spinnerPresentAddressVillage.setText("", false)
                binding.spinnerPresentAddressDistrict.clearFocus()
                binding.spinnerPresentAddressDistrict.setText("", false)
                binding.SpinnerPresentAddressStateName.clearFocus()
                binding.SpinnerPresentAddressStateName.setText("", false)
                binding.spinnerPresentAddressBlock.clearFocus()
                binding.spinnerPresentAddressBlock.setText("", false)
                binding.spinnerPresentAddressGp.clearFocus()
                binding.spinnerPresentAddressGp.setText("", false)

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


                selectedVillagePresentCodeItem=""
                selectedbVillagePresentLgdCodeItem=""
                selectedVillagePresentItem=""
                selectedDistrictPresentCodeItem=""
                selectedDistrictPresentLgdCodeItem=""
                selectedDistrictPresentItem=""
                selectedGpPresentCodeItem=""
                selectedbGpPresentLgdCodeItem=""
                selectedGpPresentItem=""
                selectedBlockPresentCodeItem=""
                selectedbBlockPresentLgdCodeItem=""
                selectedBlockPresentItem=""
                selectedDistrictPresentCodeItem=""
                selectedDistrictPresentLgdCodeItem=""
                selectedDistrictPresentItem=""


                binding.spinnerPresentAddressVillage.clearFocus()
                binding.spinnerPresentAddressVillage.setText("", false)
                binding.spinnerPresentAddressDistrict.clearFocus()
                binding.spinnerPresentAddressDistrict.setText("", false)
                binding.SpinnerPresentAddressStateName.clearFocus()
                binding.SpinnerPresentAddressStateName.setText("", false)
                binding.spinnerPresentAddressBlock.clearFocus()
                binding.spinnerPresentAddressBlock.setText("", false)
                binding.spinnerPresentAddressGp.clearFocus()
                binding.spinnerPresentAddressGp.setText("", false)


            } else {
                Toast.makeText(requireContext(), "Invalid selection", Toast.LENGTH_SHORT).show()
            }
        }



 //Present Address Spinner Setting



        //State selection
        binding.SpinnerPresentAddressStateName.setOnItemClickListener { parent, view, position, id ->
            selectedStatePresentItem = parent.getItemAtPosition(position).toString()
            if (position in state.indices) {
                selectedStatePresentCodeItem = stateCode[position]
                selectedStatePresentLgdCodeItem= stateLgdCode[position]
                commonViewModel.getDistrictListApi(selectedStatePresentCodeItem)

                selectedVillagePresentCodeItem=""
                selectedbVillagePresentLgdCodeItem=""
                selectedVillagePresentItem=""
                selectedDistrictPresentCodeItem=""
                selectedDistrictPresentLgdCodeItem=""
                selectedDistrictPresentItem=""
                selectedGpPresentCodeItem=""
                selectedbGpPresentLgdCodeItem=""
                selectedGpPresentItem=""
                selectedBlockPresentCodeItem=""
                selectedbBlockPresentLgdCodeItem=""
                selectedBlockPresentItem=""
                selectedDistrictPresentCodeItem=""
                selectedDistrictPresentLgdCodeItem=""
                selectedDistrictPresentItem=""


                binding.spinnerPresentAddressVillage.clearFocus()
                binding.spinnerPresentAddressVillage.setText("", false)
                binding.spinnerPresentAddressDistrict.clearFocus()
                binding.spinnerPresentAddressDistrict.setText("", false)
                binding.SpinnerPresentAddressStateName.clearFocus()
                binding.spinnerPresentAddressBlock.clearFocus()
                binding.spinnerPresentAddressBlock.setText("", false)
                binding.spinnerPresentAddressGp.clearFocus()
                binding.spinnerPresentAddressGp.setText("", false)



            } else {
                Toast.makeText(requireContext(), "Invalid selection", Toast.LENGTH_SHORT).show()
            }
        }

        //District selection
        binding.spinnerPresentAddressDistrict.setOnItemClickListener { parent, view, position, id ->
            selectedDistrictPresentItem = parent.getItemAtPosition(position).toString()
            if (position in district.indices) {
                selectedDistrictPresentCodeItem= districtCode[position]
                selectedDistrictPresentLgdCodeItem = districtLgdCode[position]
                commonViewModel.getBlockListApi(selectedDistrictPresentCodeItem)


                selectedVillagePresentCodeItem=""
                selectedbVillagePresentLgdCodeItem=""
                selectedVillagePresentItem=""
                selectedGpPresentCodeItem=""
                selectedbGpPresentLgdCodeItem=""
                selectedGpPresentItem=""
                selectedBlockPresentCodeItem=""
                selectedbBlockPresentLgdCodeItem=""
                selectedBlockPresentItem=""


                binding.spinnerPresentAddressVillage.clearFocus()
                binding.spinnerPresentAddressVillage.setText("", false)
                binding.spinnerPresentAddressBlock.clearFocus()
                binding.spinnerPresentAddressBlock.setText("", false)
                binding.spinnerPresentAddressGp.clearFocus()
                binding.spinnerPresentAddressGp.setText("", false)



            } else {
                Toast.makeText(requireContext(), "Invalid selection", Toast.LENGTH_SHORT).show()
            }
        }

        //Block Spinner
        binding.spinnerPresentAddressBlock.setOnItemClickListener { parent, view, position, id ->
            selectedBlockPresentItem= parent.getItemAtPosition(position).toString()
            if (position in block.indices) {
                selectedBlockPresentCodeItem= blockCode[position]
                selectedbBlockPresentLgdCodeItem = blockLgdCode[position]
                commonViewModel.getGpListApi(selectedBlockPresentCodeItem)

                selectedVillagePresentCodeItem=""
                selectedbVillagePresentLgdCodeItem=""
                selectedVillagePresentItem=""
                selectedGpPresentCodeItem=""
                selectedbGpPresentLgdCodeItem=""
                selectedGpPresentItem=""


                binding.spinnerPresentAddressVillage.clearFocus()
                binding.spinnerPresentAddressVillage.setText("", false)
                binding.spinnerPresentAddressGp.clearFocus()
                binding.spinnerPresentAddressGp.setText("", false)
            } else {
                Toast.makeText(requireContext(), "Invalid selection", Toast.LENGTH_SHORT).show()
            }
        }


        //GP Spinner
        binding.spinnerPresentAddressGp.setOnItemClickListener { parent, view, position, id ->
            selectedGpPresentItem= parent.getItemAtPosition(position).toString()
            if (position in gp.indices) {
                selectedGpPresentCodeItem = gpCode[position]
                selectedbGpPresentLgdCodeItem = gpLgdCode[position]
                commonViewModel.getVillageListApi(selectedGpCodeItem)

                selectedVillagePresentCodeItem=""
                selectedbVillagePresentLgdCodeItem=""
                selectedVillagePresentItem=""

                binding.spinnerPresentAddressVillage.clearFocus()
                binding.spinnerPresentAddressVillage.setText("", false)

            } else {
                Toast.makeText(requireContext(), "Invalid selection", Toast.LENGTH_SHORT).show()
            }
        }


        //Village Spinner
        binding.spinnerPresentAddressVillage.setOnItemClickListener { parent, view, position, id ->
            selectedVillagePresentItem= parent.getItemAtPosition(position).toString()
            if (position in village.indices) {
                selectedVillagePresentCodeItem = villageCode[position]
                selectedbVillagePresentLgdCodeItem = villageLgdCode[position]

            } else {
                Toast.makeText(requireContext(), "Invalid selection", Toast.LENGTH_SHORT).show()
            }
        }




        // If Present Address is same as permanent

        binding.optionllSamePermanentYesSelect.setOnClickListener {
            addressLine1=binding.etAdressLine.text.toString()
            addressLine2= binding.etAdressLine2.text.toString()
            pinCode= binding.etPinCode.text.toString()



            if (selectedStateCodeItem.isNotEmpty() &&
                selectedDistrictCodeItem.isNotEmpty() &&
                selectedBlockCodeItem.isNotEmpty() &&
                selectedGpCodeItem.isNotEmpty() &&
                selectedVillageCodeItem.isNotEmpty()&& addressLine1.isNotEmpty()
                && addressLine2.isNotEmpty()&& pinCode.isNotEmpty()) {

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
                binding.btnAddressSubmit.visible()

                addressPresentLine1 =  addressLine1
                addressPresentLine2 = addressLine2
                pinCodePresent = pinCode


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




            }
            else

                toastLong("Please Complete Your Permanent Address First")


        }

        // If Present Address is not same as permanent


        binding.optionSamePermanentNoSelect.setOnClickListener {

            addressLine1=binding.etAdressLine.text.toString()
            addressLine2= binding.etAdressLine2.text.toString()
            pinCode= binding.etPinCode.text.toString()

            if (selectedStateCodeItem.isNotEmpty() &&
                selectedDistrictCodeItem.isNotEmpty() &&
                selectedBlockCodeItem.isNotEmpty() &&
                selectedGpCodeItem.isNotEmpty() &&
                selectedVillageCodeItem.isNotEmpty()&& addressLine1.isNotEmpty()
                && addressLine2.isNotEmpty()&& pinCode.isNotEmpty()) {


                isClickedPermanentNo = true
                isClickedPermanentYes = false
                binding.btnAddressSubmit.visible()

                district.clear()
                block.clear()
                gp.clear()
                village.clear()

                binding.optionSamePermanentNoSelect.setBackgroundResource(R.drawable.card_background_selected) // Reset to default
                binding.optionllSamePermanentYesSelect.setBackgroundResource(R.drawable.card_background) // Change to clicked color


                binding.llPresentAddressState.visible()
                binding.llPresentAddressDistrict.visible()
                binding.llPresentAddressBlock.visible()
                binding.llPresentAddressGp.visible()
                binding.llPresentAddressVillage.visible()
                binding.llPresentAddressAdressLine.visible()

                //Set State Value
                selectedStatePresentCodeItem = ""
                selectedStatePresentLgdCodeItem = ""
                selectedStatePresentItem = ""


                //Set District Value

                selectedDistrictPresentCodeItem = ""
                selectedDistrictPresentLgdCodeItem = ""
                selectedDistrictPresentItem = ""

                //Set Block Value

                selectedBlockPresentCodeItem = ""
                selectedbBlockPresentLgdCodeItem = ""
                selectedBlockPresentItem = ""

                //Set GP Value
                selectedGpPresentCodeItem = ""
                selectedbGpPresentLgdCodeItem = ""
                selectedGpPresentItem = ""


                //Set Village Value
                selectedVillagePresentCodeItem = ""
                selectedbVillagePresentLgdCodeItem = ""
                selectedVillagePresentItem = ""


                //others

                addressPresentLine1 = ""
                addressPresentLine2 = ""
                pinCodePresent = ""
            }
            else
            toastLong("Please Complete Your Permanent Address First")


        }

        //Marital Selection If yes
        binding.optionMinorityYesSelect.setOnClickListener {
            binding.optionMinorityYesSelect.setBackgroundResource(R.drawable.card_background_selected)
            binding.optionMinorityNoSelect.setBackgroundResource(R.drawable.card_background)

            minorityStatus= "Yes"
            binding.minorityImageUpload.visible()


        }
        //Marital Selection If No
        binding.optionMinorityNoSelect.setOnClickListener {
            binding.optionMinorityYesSelect.setBackgroundResource(R.drawable.card_background)
            binding.optionMinorityNoSelect.setBackgroundResource(R.drawable.card_background_selected)

            minorityStatus= "No"

            binding.minorityImageUpload.gone()



        }


        //PWD Selection If yes
        binding.optionPwdYesSelect.setOnClickListener {
            binding.optionPwdYesSelect.setBackgroundResource(R.drawable.card_background_selected)
            binding.optionPwdNoSelect.setBackgroundResource(R.drawable.card_background)

            pwdStatus= "Yes"
            binding.pwdImageUpload.visible()


        }
        //PWD Selection If No
        binding.optionPwdNoSelect.setOnClickListener {
            binding.optionPwdYesSelect.setBackgroundResource(R.drawable.card_background)
            binding.optionPwdNoSelect.setBackgroundResource(R.drawable.card_background_selected)

            pwdStatus= "No"

            binding.pwdImageUpload.gone()


        }

        //Technical Education Selection If yes
        binding.optionTechnicalEducationYesSelect.setOnClickListener {
            binding.optionTechnicalEducationYesSelect.setBackgroundResource(R.drawable.card_background_selected)
            binding.optionTechnicalEducationNoSelect.setBackgroundResource(R.drawable.card_background)

            technicalEducationStatus= "Yes"
            binding.llTechEducation.visible()
            binding.llYearOfPassingTech.visible()
            binding.llDomainOfTech.visible()


        }
        //Technical Education Selection If No
        binding.optionTechnicalEducationNoSelect.setOnClickListener {
            binding.optionTechnicalEducationYesSelect.setBackgroundResource(R.drawable.card_background)
            binding.optionTechnicalEducationNoSelect.setBackgroundResource(R.drawable.card_background_selected)

            technicalEducationStatus= "No"

            binding.llTechEducation.gone()
            binding.llYearOfPassingTech.gone()
            binding.llDomainOfTech.gone()


        }


        //Shg Selection If yes
        binding.optionShgYesSelect.setOnClickListener {
            binding.optionShgYesSelect.setBackgroundResource(R.drawable.card_background_selected)
            binding.optionShgNoSelect.setBackgroundResource(R.drawable.card_background)

            shgStatus= "Yes"
            binding.etShgValidate.visible()
            binding.btnShgValidate.visible()


        }
        //SHG Selection If No
        binding.optionShgNoSelect.setOnClickListener {
            binding.optionShgYesSelect.setBackgroundResource(R.drawable.card_background)
            binding.optionShgNoSelect.setBackgroundResource(R.drawable.card_background_selected)

            shgStatus= "No"
            binding.etShgValidate.gone()
            binding.btnShgValidate.gone()
            binding.tvShgValidate.gone()


        }



        //Antoyada Selection If yes
        binding.optionAntyodayaYesSelect.setOnClickListener {
            binding.optionAntyodayaYesSelect.setBackgroundResource(R.drawable.card_background_selected)
            binding.optionAntyodayaNoSelect.setBackgroundResource(R.drawable.card_background)

            antoyadaStatus= "Yes"
            binding.antyodayaCardUpload.visible()


        }
        //Antoyada Selection If No
        binding.optionAntyodayaNoSelect.setOnClickListener {
            binding.optionAntyodayaYesSelect.setBackgroundResource(R.drawable.card_background)
            binding.optionAntyodayaNoSelect.setBackgroundResource(R.drawable.card_background_selected)

            antoyadaStatus= "No"

            binding.antyodayaCardUpload.gone()


        }

        //Rsby Selection If yes
        binding.optionllRsbyYesSelect.setOnClickListener {
            binding.optionllRsbyYesSelect.setBackgroundResource(R.drawable.card_background_selected)
            binding.optionllRsbyNoSelect.setBackgroundResource(R.drawable.card_background)

            rsbyStatus= "Yes"
            binding.rsbyUpload.visible()


        }
        //Rsby Selection If No
        binding.optionllRsbyNoSelect.setOnClickListener {
            binding.optionllRsbyYesSelect.setBackgroundResource(R.drawable.card_background)
            binding.optionllRsbyNoSelect.setBackgroundResource(R.drawable.card_background_selected)

            rsbyStatus= "No"

            binding.rsbyUpload.gone()


        }


        //PIP Selection If yes
        binding.optionPipYesSelect.setOnClickListener {
            binding.optionPipYesSelect.setBackgroundResource(R.drawable.card_background_selected)
            binding.optionPipNoSelect.setBackgroundResource(R.drawable.card_background)

            pipStatus= "Yes"


        }
        //PIP Selection If No
        binding.optionPipNoSelect.setOnClickListener {
            binding.optionPipYesSelect.setBackgroundResource(R.drawable.card_background)
            binding.optionPipNoSelect.setBackgroundResource(R.drawable.card_background_selected)

            pipStatus= "No"

        }


        // If Secc Yess

        binding.optionOrigSecccYesSelect.setOnClickListener {

            binding.optionOrigSecccYesSelect.setBackgroundResource(R.drawable.card_background_selected) // Reset to default
            binding.optionOrigSecccNoSelect.setBackgroundResource(R.drawable.card_background) // Change to clicked color

            binding.etATINName.gone()
            binding.etATINNo.visible()


        }

        binding.optionOrigSecccNoSelect.setOnClickListener {

            binding.optionOrigSecccNoSelect.setBackgroundResource(R.drawable.card_background_selected) // Reset to default
            binding.optionOrigSecccYesSelect.setBackgroundResource(R.drawable.card_background) // Change to clicked color

            binding.etATINNo.gone()
            binding.etATINName.visible()

        }

        //All Submit Button Here

        binding.btnShgValidate.setOnClickListener {

            commonViewModel.shgValidateAPI(ShgValidateReq(binding.etShgValidate.text.toString(),"9"))  //41358

            lifecycleScope.launch {
                delay(1000)
                if (shgValidateStatus == "Y"){

                    binding.tvShgValidate.visible()
                    binding.btnShgValidate.gone()
                    shgCode= binding.etShgValidate.text.toString()
                    binding.tvShgValidate.text = "Validate Successfully: $shgName"
                }
                else toastLong("Validation Failed please check your SHG Code")
            }
            }

        binding.btnAddressSubmit.setOnClickListener {

            addressLine1=binding.etAdressLine.text.toString()
            addressLine2= binding.etAdressLine2.text.toString()
            pinCode= binding.etPinCode.text.toString()

            if (isClickedPermanentNo){
                addressPresentLine1=binding.etPresentAddressAdressLine.text.toString()
                addressPresentLine2= binding.etPresentLine2.text.toString()
                pinCodePresent= binding.etPresentPinCode.text.toString()


            }


            if (selectedStateCodeItem.isNotEmpty() &&
                selectedDistrictCodeItem.isNotEmpty() &&
                selectedBlockCodeItem.isNotEmpty() &&
                selectedGpCodeItem.isNotEmpty() &&
                selectedVillageCodeItem.isNotEmpty() &&
                selectedStatePresentCodeItem.isNotEmpty() &&
                selectedDistrictPresentCodeItem.isNotEmpty() &&
                selectedBlockPresentCodeItem.isNotEmpty() &&
                selectedGpPresentCodeItem.isNotEmpty() &&
                selectedVillagePresentCodeItem.isNotEmpty()&&addressLine1.isNotEmpty()&& addressLine2.isNotEmpty()&&
                pinCode.isNotEmpty()&& addressPresentLine1.isNotEmpty()&&addressPresentLine2.isNotEmpty()&&
                pinCodePresent.isNotEmpty()){

                // Hit The Insert API
                selectedStateCodeItem
                selectedDistrictCodeItem
                selectedBlockCodeItem
                selectedGpCodeItem
                selectedVillageCodeItem
                selectedStatePresentCodeItem
                selectedDistrictPresentCodeItem
                selectedBlockPresentCodeItem
                selectedGpPresentCodeItem
                selectedVillagePresentCodeItem
                addressLine1
                addressLine2
                pinCode
                addressPresentLine1
                addressPresentLine2
                pinCodePresent
                residenceImage

                 toastLong("Success")



            }
            else toastLong("Please complete your address first")
        }

        binding.btnSeccSubmit.setOnClickListener {
            if (selectedSeccStateCodeItem.isNotEmpty() && selectedDistrictCodeItem.isNotEmpty()
                && selectedSeccBlockCodeItem.isNotEmpty()&& selectedSeccGpCodeItem.isNotEmpty()
                && selectedSeccVillageCodeItem.isNotEmpty()){

                toastLong("Success")

            }
            else
                toastLong("Please Complete SECC info First")

        }

        binding.btnPersonalSubmit.setOnClickListener {

            guardianName= binding.etGName.text.toString()
            motherName= binding.etMotherName.text.toString()
            guardianMobileNumber= binding.etGNumber.text.toString()
            yearlyIncomeFamily= binding.etFIncome.text.toString()
            voterIdNo= binding.etllVoterId.text.toString()
            voterIdImage
            drivingLicenceNumber= binding.etdrivingId.text.toString()
            drivingLicenceImage
            selectedCategoryItem
            categoryCertiImage
            selectedMaritalItem
            minorityStatus
            minorityImage
            pwdStatus
            pwdImage
            shgStatus
            shgName
            shgCode
            shgValidateStatus
            antoyadaStatus
            antoyadaImage
            rsbyStatus
            rsbyImage
            pipStatus


        }


        // External Validation



        //image Upload

        binding.voterIdUpload.setOnClickListener {
            checkAndRequestPermissionsForPurpose("VOTER_ID")
        }

        binding.drivingLicenceUpload.setOnClickListener {
            checkAndRequestPermissionsForPurpose("DRIVING_LICENSE")
        }

        binding.minorityImageUpload.setOnClickListener {
            checkAndRequestPermissionsForPurpose("MINORITY_CERTIFICATE")
        }
        binding.categoryCertificateUpload.setOnClickListener {
            checkAndRequestPermissionsForPurpose("CATEGORY_CERTIFICATE")
        }

        binding.pwdImageUpload.setOnClickListener {
            checkAndRequestPermissionsForPurpose("PWD_CERTIFICATE")
        }

        binding.antyodayaCardUpload.setOnClickListener {
            checkAndRequestPermissionsForPurpose("ANTOYADA_CERTIFICATE")
        }
                binding.rsbyUpload.setOnClickListener {
                    checkAndRequestPermissionsForPurpose("RSBY_CERTIFICATE")
                }

        binding.uploadResidenceImage.setOnClickListener {
            checkAndRequestPermissionsForPurpose("RSBY_CERTIFICATE")
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

    private fun collectTechEducationResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.techEducation) {
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
                        it.data?.let { getTechEduRes ->
                            if (getTechEduRes.responseCode == 200) {
                              var  courseList = getTechEduRes.courseList

                                for (x in courseList) {
                                    courseesName.add(x.qualName)
                                    courseesCode.add(x.qualCode)
                                }
                                TechEduAdapter.notifyDataSetChanged()
                            } else if (getTechEduRes.responseCode == 301) {
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
/*
    private fun collectTechEducationDomainResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.techEducationDomain) {
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
                        it.data?.let { getTechEduRes ->
                            if (getTechEduRes.responseCode == 200) {
                                var  courseList = getTechEduRes.courseList

                                for (x in courseList) {
                                    courseesName.add(x.qualName)
                                    courseesCode.add(x.qualCode)
                                }
                                TechEduAdapter.notifyDataSetChanged()
                            } else if (getTechEduRes.responseCode == 301) {
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
*/


    private fun collectShgValidateResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.shgValidate) {
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
                        it.data?.let { getValidateStatus ->
                            if (getValidateStatus.isSuccessful) {
                                shgValidateStatus = getValidateStatus.body()?.Valid.toString()
                                 shgName = getValidateStatus.body()?.shg_name.toString()

                            }
                        } ?: showSnackBar("Internal Server Error")
                    }
                }
            }
        }
    }



    private fun setDropdownValue(autoCompleteTextView: AutoCompleteTextView, value: String, dataList: List<String>) {
        if (dataList.contains(value)) { // Check if the value exists in the list
            autoCompleteTextView.setText(value, false) // Set text without filtering or triggering dropdown
        }
    }



    private fun checkAndRequestPermissionsForPurpose(purpose: String) {
        currentRequestPurpose = purpose

        when {
            // Android 13+ (API level 33 and above)
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                        PERMISSION_READ_MEDIA_IMAGES
                    )
                } else {
                    handlePermissionGranted(purpose)
                }
            }

            // Android 6.0 to 12 (API levels 23 to 32)
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        PERMISSION_READ_MEDIA_IMAGES
                    )
                } else {
                    handlePermissionGranted(purpose)
                }
            }

            // Below Android 6.0 (No runtime permissions)
            else -> {
                handlePermissionGranted(purpose)
            }
        }
    }

    private fun handlePermissionGranted(purpose: String) {
        when (purpose) {
            "VOTER_ID" -> openGalleryForVoterId()
            "DRIVING_LICENSE" -> openGalleryForDrivingLicense()
            "MINORITY_CERTIFICATE" -> openGalleryForMinority()
            "CATEGORY_CERTIFICATE" -> openGalleryForCategory()
            "PWD_CERTIFICATE" -> openGalleryForPwd()
            "ANTOYADA_CERTIFICATE" -> openGalleryForAntoyada()
            "RSBY_CERTIFICATE" -> openGalleryForRsby()
            "RESIDENCE_CERTIFICATE" -> openGalleryForResidence()
        }
    }

    private fun openGalleryForVoterId() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_PICK_VOTER_ID)
    }

    private fun openGalleryForDrivingLicense() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_PICK_DRIVING_LICENSE)
    }

    private fun openGalleryForMinority() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_PICK_MINORITY)
    }

    private fun openGalleryForCategory() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_PICK_CATEGORY)
    }

    private fun openGalleryForPwd() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_PICK_PWD)
    }

    private fun openGalleryForAntoyada() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_PICK_ANTOYADA)
    }

    private fun openGalleryForRsby() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_PICK_RSBY)
    }

    private fun openGalleryForResidence() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_PICK_RESIDENCE)
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                PERMISSION_READ_MEDIA_IMAGES -> handlePermissionGranted(currentRequestPurpose ?: "")
            }
        } else {
            Toast.makeText(requireContext(), "Permission denied for $currentRequestPurpose", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            when (requestCode) {
                REQUEST_PICK_VOTER_ID -> {
                    // Handle voter ID image

                    voterIdImage=
                        selectedImageUri?.let { AppUtil.convertUriToBase64(it,requireContext()) }.toString()
                    var fileName = selectedImageUri?.let { getFileName(requireContext(), it) }
                    binding.voterimageText.text = fileName


                }

                REQUEST_PICK_DRIVING_LICENSE -> {
                    // Handle driving license image
                    drivingLicenceImage=
                        selectedImageUri?.let { AppUtil.convertUriToBase64(it,requireContext()) }.toString()
                    var fileName = selectedImageUri?.let { getFileName(requireContext(), it) }
                    binding.drivingLicenceimageText.text = fileName



                }

                REQUEST_PICK_CATEGORY -> {
                    // Handle voter ID image

                    categoryCertiImage=
                        selectedImageUri?.let { AppUtil.convertUriToBase64(it,requireContext()) }.toString()
                    var fileName = selectedImageUri?.let { getFileName(requireContext(), it) }
                    binding.categoryCertimageText.text = fileName


                }

                REQUEST_PICK_MINORITY -> {
                    // Handle driving license image
                    minorityImage=
                        selectedImageUri?.let { AppUtil.convertUriToBase64(it,requireContext()) }.toString()

                    var fileName = selectedImageUri?.let { getFileName(requireContext(), it) }
                    binding.minorityimageText.text = fileName

                }


                        REQUEST_PICK_PWD -> {
                    // Handle driving license image
                    pwdImage=
                        selectedImageUri?.let { AppUtil.convertUriToBase64(it,requireContext()) }.toString()

                    val fileName = selectedImageUri?.let { getFileName(requireContext(), it) }
                    binding.pwdImageText.text = fileName

                }


                REQUEST_PICK_ANTOYADA -> {
                    // Handle driving license image
                    antoyadaImage=
                        selectedImageUri?.let { AppUtil.convertUriToBase64(it,requireContext()) }.toString()

                    val fileName = selectedImageUri?.let { getFileName(requireContext(), it) }
                    binding.antyodayamageText.text = fileName

                }

                REQUEST_PICK_RSBY -> {
                    // Handle driving license image
                    rsbyImage=
                        selectedImageUri?.let { AppUtil.convertUriToBase64(it,requireContext()) }.toString()

                    val fileName = selectedImageUri?.let { getFileName(requireContext(), it) }
                    binding.rsbyimageText.text = fileName

                }


                REQUEST_PICK_RESIDENCE -> {
                    // Handle driving license image
                    residenceImage=
                        selectedImageUri?.let { AppUtil.convertUriToBase64(it,requireContext()) }.toString()

                    val fileName = selectedImageUri?.let { getFileName(requireContext(), it) }
                    binding.residentalimageText.text = fileName

                }

            }
        }


    }


    @SuppressLint("MissingInflatedId")
    private fun showMonthYearPicker(onDateSelected: (year: Int, month: Int) -> Unit) {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1 // Months are 0-indexed
        val currentYear = calendar.get(Calendar.YEAR)

        val dialogView = layoutInflater.inflate(R.layout.month_year_picker, null)
        val monthPicker = dialogView.findViewById<NumberPicker>(R.id.monthPicker)
        val yearPicker = dialogView.findViewById<NumberPicker>(R.id.yearPicker)

        // Configure the MonthPicker
        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.value = currentMonth
        monthPicker.displayedValues = arrayOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )

        // Configure the YearPicker
        val startYear = 1900
        yearPicker.minValue = startYear
        yearPicker.maxValue = currentYear
        yearPicker.value = currentYear
        yearPicker.wrapSelectorWheel = false

        // Show the AlertDialog
        AlertDialog.Builder(requireContext())
            .setTitle("Select Month and Year")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                val selectedMonth = monthPicker.value
                val selectedYear = yearPicker.value
                onDateSelected(selectedMonth, selectedYear)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
