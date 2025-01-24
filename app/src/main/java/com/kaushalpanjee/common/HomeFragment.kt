package com.kaushalpanjee.common

import Language
import LanguageRead
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
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager // For checking permissions
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat // For permission checks
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.google.android.material.chip.Chip

import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.common.model.request.AddressInsertReq
import com.kaushalpanjee.common.model.request.AdharDetailsReq
import com.kaushalpanjee.common.model.request.BankingInsertReq
import com.kaushalpanjee.common.model.request.BankingReq
import com.kaushalpanjee.common.model.request.CandidateReq
import com.kaushalpanjee.common.model.request.EducationalInsertReq
import com.kaushalpanjee.common.model.request.EmploymentInsertReq
import com.kaushalpanjee.common.model.request.ImageChangeReq
import com.kaushalpanjee.common.model.request.PersonalInsertReq
import com.kaushalpanjee.common.model.request.SeccInsertReq
import com.kaushalpanjee.common.model.request.SeccReq
import com.kaushalpanjee.common.model.request.SectionAndPerReq
import com.kaushalpanjee.common.model.request.ShgValidateReq
import com.kaushalpanjee.common.model.request.TechQualification
import com.kaushalpanjee.common.model.request.TradeReq
import com.kaushalpanjee.common.model.request.TrainingInsertReq
import com.kaushalpanjee.common.model.response.Address
import com.kaushalpanjee.common.model.response.AddressDetail
import com.kaushalpanjee.common.model.response.Bank
import com.kaushalpanjee.common.model.response.Educational
import com.kaushalpanjee.common.model.response.Employment
import com.kaushalpanjee.common.model.response.Personal
import com.kaushalpanjee.common.model.response.PersonalDetail
import com.kaushalpanjee.common.model.response.Secc
import com.kaushalpanjee.common.model.response.Training
import com.kaushalpanjee.common.model.response.UserDetails
import com.kaushalpanjee.core.util.AESCryptography
import com.kaushalpanjee.core.util.AppConstant
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.createHalfCircleProgressBitmap
import com.kaushalpanjee.core.util.isNull
import com.kaushalpanjee.core.util.setDrawable
import com.kaushalpanjee.core.util.toastShort
import com.utilize.core.util.FileUtils.Companion.getFileName
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import java.io.ByteArrayOutputStream
import java.security.MessageDigest

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
    private val REQUEST_PICK_RSBY = 109
    private val REQUEST_PICK_NREGA = 110
    private val REQUEST_PICK_PROFILE_PIC = 108
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
    private var addressLine1 = ""
    private var addressLine2 = ""
    private var pinCode = ""
    private var voterIdImage = ""
    private var profilePicIdImage = ""
    private var voterIdNo = ""
    private var guardianName = ""
    private var motherName = ""
    private var guardianMobileNumber = ""
    private var drivingLicenceNumber = ""
    private var yearlyIncomeFamily = ""
    private var categoryCertiImage = ""
    private var drivingLicenceImage = ""
    private var minorityStatus = ""
    private var minorityImage = ""
    private var pwdStatus = ""
    private var pwdImage = ""
    private var technicalEducationStatus = ""
    private var antoyadaStatus = ""
    private var antoyadaImage = ""
    private var selectedCategoryItem = ""
    private var selectedMaritalItem = ""
    private var selectedHighestEducationItem = ""
    private var shgValidateStatus = ""
    private var nregaValidateStatus = ""
    private var nregaJobCard = ""
    private var nregaImageJobCard = ""
    private var shgName = ""
    private var shgCode = ""
    private var shgStatus = ""
    private var nregaStatus = ""
    private var rsbyStatus = ""
    private var rsbyImage = ""
    private var pipStatus = ""
    private var residenceImage = ""
    private var highestEducationDate = ""
    private var selectedTechEducationItem = ""
    private var selectedTechEducationItemCode = ""
    private var selectedTechEducationDomainItem = ""
    private var selectedTechEducationDomainCode = ""
    private var selectedTechEducationDate = ""
    private var selectedHeardABoutItem = ""
    private var selectedHeardABoutCode = ""
    private var selectedInterestedIn = ""
    private var selectedPrevCompleteTraining = ""
    private var selectedIEmploymentPref = ""
    private var selectedJobLocation = ""
    private var currentlyEmpStatus = ""
    private var natureEmpEmpStatus = ""
    private var traingBeforeStatus = ""
    private var selectedSector = ""
    private var selectedSectorCode = ""
    private var selectedTrade = ""
    private var haveUHeardStatus = ""
    private var totalPercentange = 0.0f
    private var previouslycompletedduring = ""
    private var personalStatus = ""
    private var isPermanentStatus = ""
    private var educationalStatus = ""
    private var trainingStatus = ""
    private var seccStatus = ""
    private var addressStatus = ""
    private var employmentStatus = ""
    private var bankingStatus = ""
    private var bankCode = ""
    private var bankName1 = ""
    private var branchCode = ""
    private var branchName = ""
    private var selectedSeccName = ""
    private var selectedAhlTin = ""
    private var jobCardNo= ""
    private var IfscCode= ""
    private var BankName= ""
    private var BranchName= ""
    private var PanNumber= ""
    private var BankAcNo= ""
    private var previousTrainingDuration= ""
   private var result = StringBuilder()
    private var userAadhaarDetailsListNew: List<UserDetails> = mutableListOf()
    private var userCandidatePersonalDetailsList: List<Personal> = mutableListOf()
    private var userCandidatePersonalDetailsList2: List<PersonalDetail> = mutableListOf()
    private var userCandidateAddressDetailsList: List<Address> = mutableListOf()
    private var userCandidateAddressDetailsList2: List<AddressDetail> = mutableListOf()
    private var userCandidateSeccDetailsList: List<Secc> = mutableListOf()
    private var userCandidateEducationalDetailsList: List<Educational> = mutableListOf()
    private var userCandidateEmploymentDetailsList: List<Employment> = mutableListOf()
    private var userCandidateTrainingDetailsList: List<Training> = mutableListOf()
    private var userCandidateBankDetailsList: List<Bank> = mutableListOf()
    private var currentSalary =""
    private var salaryExpectation=""
    private var accLenghth=""







    //Secc Address


    // State var
    private lateinit var stateSeccAdapter: ArrayAdapter<String>
    private var selectedSeccStateCodeItem = ""
    private var selectedSeccStateLgdCodeItem = ""
    private var selectedSeccStateItem = ""


    // district var
    private lateinit var districtSeccAdapter: ArrayAdapter<String>
    private var selectedSeccDistrictCodeItem = ""
    private var selectedSeccDistrictLgdCodeItem = ""
    private var selectedSeccDistrictItem = ""


    //block var
    private lateinit var blockSeccAdapter: ArrayAdapter<String>
    private var selectedSeccBlockCodeItem = ""
    private var selectedSeccBlockLgdCodeItem = ""
    private var selectedSeccBlockItem = ""


    //GP var
    private lateinit var gpSeccAdapter: ArrayAdapter<String>
    private var selectedSeccGpCodeItem = ""
    private var selectedSeccGpLgdCodeItem = ""
    private var selectedSeccGpItem = ""


    //Village var
    private lateinit var villageSeccAdapter: ArrayAdapter<String>
    private var selectedSeccVillageCodeItem = ""
    private var selectedbSeccVillageLgdCodeItem = ""
    private var selectedSeccVillageItem = ""


    //  Present
    var addressPresentLine1 = ""
    var addressPresentLine2 = ""
    var pinCodePresent = ""


    private lateinit var categoryAdapter: ArrayAdapter<String>
    private lateinit var maritalAdapter: ArrayAdapter<String>
    private lateinit var highestEducationAdapter: ArrayAdapter<String>


    // State var
    private var stateList: MutableList<WrappedList> = mutableListOf()
    private lateinit var stateAdapter: ArrayAdapter<String>
    private lateinit var seccAdapter: SeccAdapter

    private var state = ArrayList<String>()
    private var stateCode = ArrayList<String>()
    private var stateLgdCode = ArrayList<String>()
    private var selectedStateCodeItem = ""
    private var selectedStateLgdCodeItem = ""
    private var selectedStateItem = ""

    // district var
    private var districtList: MutableList<DistrictList> = mutableListOf()
    private lateinit var districtAdapter: ArrayAdapter<String>
    private var district = ArrayList<String>()
    private var districtCode = ArrayList<String>()
    private var districtCodePer = ArrayList<String>()
    private var districtLgdCode = ArrayList<String>()
    private var selectedDistrictCodeItem = ""
    private var selectedDistrictLgdCodeItem = ""
    private var selectedDistrictItem = ""


    //block var
    private var blockList: MutableList<BlockList> = mutableListOf()
    private var userAadhaarDetailsList: List<UserDetails> = mutableListOf()
    private lateinit var blockAdapter: ArrayAdapter<String>
    private var block = ArrayList<String>()
    private var blockCode = ArrayList<String>()
    private var blockLgdCode = ArrayList<String>()
    private var selectedBlockCodeItem = ""
    private var selectedbBlockLgdCodeItem = ""
    private var selectedBlockItem = ""


    //GP var
    private var gpList: MutableList<GrampanchayatList> = mutableListOf()
    private lateinit var gpAdapter: ArrayAdapter<String>
    private var gp = ArrayList<String>()
    private var gpCode = ArrayList<String>()
    private var gpLgdCode = ArrayList<String>()
    private var selectedGpCodeItem = ""
    private var selectedbGpLgdCodeItem = ""
    private var selectedGpItem = ""


    //Village var
    private var villageList: MutableList<VillageList> = mutableListOf()
    private lateinit var villageAdapter: ArrayAdapter<String>
    private var village = ArrayList<String>()
    private var courseesName = ArrayList<String>()
    private var courseesCode = ArrayList<String>()
    private var courseesDomainName = ArrayList<String>()
    private var courseesDomainCode = ArrayList<String>()

    private var statePer = ArrayList<String>()
    private var districtPer = ArrayList<String>()
    private var blockPer = ArrayList<String>()
    private var gpPer = ArrayList<String>()
    private var villagePer = ArrayList<String>()



    private var heardName = ArrayList<String>()
    private var languageName = ArrayList<String>()
    private var languageCode = ArrayList<String>()
    private var fatherName = ArrayList<String>()
    private var ahlTinNo = ArrayList<String>()
    private var seccName = ArrayList<String>()
    private var heardCode = ArrayList<String>()
    private var villageCode = ArrayList<String>()
    private var villageLgdCode = ArrayList<String>()
    private var selectedVillageCodeItem = ""
    private var selectedbVillageLgdCodeItem = ""
    private var selectedVillageItem = ""

    //  Present Address Variables


    // State var
    private lateinit var statePresentAdapter: ArrayAdapter<String>
    private var selectedStatePresentCodeItem = ""
    private var selectedStatePresentLgdCodeItem = ""
    private var selectedStatePresentItem = ""

    // district var
    private lateinit var districtPresentAdapter: ArrayAdapter<String>
    private var selectedDistrictPresentCodeItem = ""
    private var selectedDistrictPresentLgdCodeItem = ""
    private var selectedDistrictPresentItem = ""


    //block var
    private lateinit var blockPresentAdapter: ArrayAdapter<String>
    private var selectedBlockPresentCodeItem = ""
    private var selectedbBlockPresentLgdCodeItem = ""
    private var selectedBlockPresentItem = ""


    //GP var
    private lateinit var gpPresentAdapter: ArrayAdapter<String>
    private var selectedGpPresentCodeItem = ""
    private var selectedbGpPresentLgdCodeItem = ""
    private var selectedGpPresentItem = ""


    //Village var
    private lateinit var villagePresentAdapter: ArrayAdapter<String>
    private var selectedVillagePresentCodeItem = ""
    private var selectedbVillagePresentLgdCodeItem = ""
    private var selectedVillagePresentItem = ""

    private lateinit var TechEduAdapter: ArrayAdapter<String>
    private lateinit var TechEduDomaiAdapter: ArrayAdapter<String>
    private lateinit var HeardAdapter: ArrayAdapter<String>


    //Value for dropdown
    private val categoryList = listOf("SC", "ST", "OBC", "OTHER")
    private val maritalList = listOf("Married", "Unmarried", "Divorce")
    private val highestEducationList =
        listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
    private val items = arrayOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
    private var sectorList = ArrayList<String>()
    private var sectorCode = ArrayList<String>()
    private var tradeName = ArrayList<String>()

    private var selectedSectorIndices: MutableList<Int> = mutableListOf()
    private var selectedTradeIndices: MutableList<Int> = mutableListOf()
    private val searchQuery = MutableLiveData<String>()




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        init()
    }


    private fun init() {
        listener()
        collectStateResponse()
        collectBlockResponse()
        collectGpResponse()
        collectVillageResponse()
        collectShgValidateResponse()
        collectTechEducationDomainResponse()
        collectWhereHaveUHeardResponse()
        collectTechEducationResponse()
        collectAadharDetailsResponse()
        collectSeccListResponse()
        collectSetionAndPerResponse()
        collectBankResponse()
        collectNregaValidateResponse()
        collectTradeResponse()
        collectSectorResponse()
        collectCandidateDetailsResponse()

        commonViewModel.getCandidateDetailsAPI(CandidateReq(BuildConfig.VERSION_NAME,userPreferences.getUseID()))


        commonViewModel.getSecctionAndPerAPI(SectionAndPerReq(BuildConfig.VERSION_NAME,userPreferences.getUseID(),AppUtil.getAndroidId(requireContext())))
        commonViewModel.getStateListApi()
        commonViewModel.getSectorListAPI(TechQualification(BuildConfig.VERSION_NAME))
        commonViewModel.getAadhaarListAPI(
            AdharDetailsReq(
                BuildConfig.VERSION_NAME,
                AppUtil.getAndroidId(requireContext()),
               userPreferences.getUseID()
            )
        )




    }


    @SuppressLint("SetTextI18n", "CheckResult", "SuspiciousIndentation")
    private fun listener() {

        binding.profileView.editImageButton.setOnClickListener {


            checkAndRequestPermissionsForPurpose("PROFILE_PIC")







        }

        binding.tvLanguages.setOnClickListener {

            showStyledLanguageSelectionDialog()
        }


        // Secc Search Text
        searchQuery.observe(viewLifecycleOwner) { query ->
            if (query.length >= 4) {
                handleSearchQuery(query) // Trigger API call
            }
        }

        // Add TextWatcher to EditText
        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })




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
      /*  stateAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            state
        )

        binding.SpinnerStateName.setAdapter(stateAdapter)
*/
        //Secc Adapter Setting


        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        seccAdapter = SeccAdapter { selectedItem ->

            selectedAhlTin   =  selectedItem.ahltin
            selectedSeccName=  selectedItem.seccName
            toastShort("Selected Item: ${selectedItem.seccName}")
            binding.recyclerView.gone()
        }
        binding.recyclerView.adapter = seccAdapter

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

        //Adapter TechEducation setting

        TechEduAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            courseesName
        )

        binding.spinnerTechnicalEducation.setAdapter(TechEduAdapter)

        //Adapter TechDomainEducation setting

        TechEduDomaiAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            courseesDomainName
        )

        binding.spinnerDomainOfTech.setAdapter(TechEduDomaiAdapter)


        //Adapter Where Have u heard setting

        HeardAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            heardName
        )

        binding.spinnerHeardAboutddugky.setAdapter(HeardAdapter)


        // open llTop by 1 change 0




        binding.llTopPersonal.setOnClickListener {

            if (isPersonalVisible && personalStatus.contains("0")) {

                isPersonalVisible = false
                binding.personalExpand.visible()
                binding.viewSecc.visible()

            }

            else {
                isPersonalVisible = true
                binding.personalExpand.gone()
                binding.viewSecc.gone()
            }


             if (personalStatus.contains("1")){

                 showYesNoDialog(
                     context = requireContext(),  // Use your context here (e.g., `requireContext()` in fragments)
                     title = "Confirmation",
                     message = "Do you want to edit your personal info?",
                     onYesClicked = {
                         // Action for Yes button
                         isPersonalVisible = false
                         binding.personalExpand.visible()
                         binding.viewSecc.visible()

                         for (x in userCandidatePersonalDetailsList2) {
                             try {
                                 // Populate the UI
                                 binding.etGName.setText(x.guardianName)
                                 binding.etGNumber.setText(x.guardianMobilNo)
                                 binding.etMotherName.setText(x.motherName)
                                 binding.etFIncome.setText(x.annualFamilyIncome)
                                 binding.etllVoterId.setText(x.voterId)
                                 binding.etdrivingId.setText(x.dlNo)
                               //  binding.SpinnerCategory.setText(x.castCategory)
                                 setDropdownValue(binding.SpinnerCategory, x.castCategory, categoryList)
                                 setDropdownValue(binding.SpinnerMarital, x.maritalStatus, maritalList)

                               //  binding.SpinnerMarital.setText(x.maritalStatus)
                                 binding.etShgValidate.setText(x.shgNo)

                                 val minorityStatusn = x.isMinority
                                 val pwdStatusn = x.isDisablity
                                 val nregaJobCardStatusn = x.isNarega
                                 val shgStatusn = x.isSHG
                                 val antyodayaStatusn = x.antyodaya
                                 val rsbyStatusm = x.isRSBY
                                 val pipStatusn = x.isPIP

                                 // Set the UI based on conditions
                                 handleStatus(binding.optionMinorityYesSelect, binding.optionMinorityNoSelect, minorityStatusn)
                                 handleStatus(binding.optionPwdYesSelect, binding.optionPwdNoSelect, pwdStatusn)
                                 handleStatus(binding.optionNregaJobYesSelect, binding.optionNregaJobNoSelect, nregaJobCardStatusn)
                                 handleStatus(binding.optionShgYesSelect, binding.optionShgNoSelect, shgStatusn)
                                 handleStatus(binding.optionAntyodayaYesSelect, binding.optionAntyodayaNoSelect, antyodayaStatusn)
                                 handleStatus(binding.optionllRsbyYesSelect, binding.optionllRsbyNoSelect, rsbyStatusm)
                                 handleStatus(binding.optionPipYesSelect, binding.optionPipNoSelect, pipStatusn)

                                 guardianName =x.guardianName
                                 motherName= x.motherName
                                 guardianMobileNumber= x.guardianMobilNo
                                 yearlyIncomeFamily=x.annualFamilyIncome
                                 voterIdNo =x.voterId
                                 drivingLicenceNumber=x.dlNo
                                 selectedCategoryItem=x.castCategory
                                 selectedMaritalItem=x.maritalStatus
                                 minorityStatus=  x.isMinority
                                 pwdStatus =x.isDisablity
                                 nregaStatus = x.isNarega
                                 nregaJobCard=x.naregaJobCard
                                 shgStatus= x.isSHG
                                 shgCode=x.shgNo
                                 antoyadaStatus=x.antyodaya
                                 rsbyStatus=x.isRSBY
                                 pipStatus=x.isPIP

                             } catch (e: Exception) {
                                 showSnackBar("Error setting data: ${e.message}")
                             }
                         }

                     },
                     onNoClicked = {

                     }
                 )



            }




        }

        binding.llTopSecc.setOnClickListener {
            if (isSeccInfoVisible && seccStatus.contains("0")) {

                isSeccInfoVisible = false
                binding.expandSecc.visible()
                binding.viewSeccc.visible()

               /* setDropdownValue(binding.spinnerStateSecc, selectedStateItem, state)
                setDropdownValue(binding.spinnerDistrictSecc, selectedDistrictItem, district)
                setDropdownValue(binding.spinnerBlockSecc, selectedBlockItem, block)
                setDropdownValue(binding.spinnerGpSecc, selectedGpItem, gp)
                setDropdownValue(binding.spinnerVillageSecc, selectedVillageItem, village)

                selectedSeccStateCodeItem = selectedStateCodeItem
                selectedSeccDistrictCodeItem = selectedDistrictCodeItem
                selectedSeccBlockCodeItem = selectedBlockCodeItem
                selectedSeccGpCodeItem = selectedGpCodeItem
                selectedSeccVillageCodeItem = selectedVillageCodeItem*/

            } else {

                isSeccInfoVisible = true
                binding.expandSecc.gone()
                binding.viewSeccc.gone()}

            if (seccStatus.contains("1")){

                showYesNoDialog(
                    context = requireContext(),  // Use your context here (e.g., `requireContext()` in fragments)
                    title = "Confirmation",
                    message = "Do you want to edit your Secc info?",
                    onYesClicked = {
                        isSeccInfoVisible = false
                        binding.expandSecc.visible()
                        binding.viewSeccc.visible()


                        for (x in userCandidateSeccDetailsList){



                            lifecycleScope.launch {

                                commonViewModel.getDistrictListApi(x.seccStateCode)
                                collectDistrictResponse(true)
                                commonViewModel.getBlockListApi(x.seccDistrictCode)
                                commonViewModel.getGpListApi(x.seccBlcokCode)
                                commonViewModel.getVillageListApi(x.seccGPCode)
                                delay(2000)


                                setDropdownValue(binding.spinnerStateSecc, x.seccStateName, state)
                                setDropdownValue(binding.spinnerDistrictSecc, x.seccDistrictName, district)
                                setDropdownValue(binding.spinnerBlockSecc, x.seccBlockName, block)
                                setDropdownValue(binding.spinnerGpSecc, x.seccGPName, gp)
                                setDropdownValue(binding.spinnerVillageSecc, x.seccVillageName, village)

                            }



                            selectedSeccStateCodeItem = x.seccStateCode
                            selectedSeccDistrictCodeItem = x.seccDistrictCode
                            selectedSeccBlockCodeItem = x.seccBlcokCode
                            selectedSeccGpCodeItem = x.seccGPCode
                            selectedSeccVillageCodeItem = x.seccVillageCode
                            selectedSeccName= x.seccCandidateName
                            selectedAhlTin=x.seccAHLTIN



                        }



                    },
                    onNoClicked = {

                    }
                )

            }

        }

        binding.llTopAddress.setOnClickListener {

            if (isAddressVisible && addressStatus.contains("0")) {
                isAddressVisible = false
                binding.expandAddress.visible()
                binding.viewAddress.visible()
            } else {
                isAddressVisible = true
                binding.expandAddress.gone()
                binding.viewAddress.gone()
            }

            if (addressStatus.contains("1")){
                showYesNoDialog(
                    context = requireContext(),  // Use your context here (e.g., `requireContext()` in fragments)
                    title = "Confirmation",
                    message = "Do you want to edit your Address info?",
                    onYesClicked = {
                        isAddressVisible = false
                        binding.expandAddress.visible()
                        binding.viewAddress.visible()
                        binding.btnAddressSubmit.visible()


                        for (x in userCandidateAddressDetailsList2){



                            lifecycleScope.launch {


                                district.clear()
                                Log.d("StateCode", "Selected Value: ${x.permanentStateCode}")

                                commonViewModel.getDistrictListApi(x.permanentStateCode)
                                commonViewModel.getBlockListApi(x.permanentDistrictCode)
                                commonViewModel.getGpListApi(x.permanentBlcokCode)
                                commonViewModel.getVillageListApi(x.permanentGPCode)



                                delay(1000)
                                collectDistrictResponse(true)


                                binding.TvSpinnerStateName.text = x.permanentStateName
                                setDropdownValue(binding.spinnerDistrict, x.permanentDistrictName, district)
                                setDropdownValue(binding.spinnerBlock, x.permanentBlockName, block)
                                setDropdownValue(binding.spinnerGp, x.permanentGPName, gp)
                                setDropdownValue(binding.spinnerVillage, x.permanentVillageName, village)


                                commonViewModel.getDistrictListApi(x.presentStateCode)
                                collectDistrictResponse(false)
                                commonViewModel.getBlockListApi(x.presentDistrictCode)
                                commonViewModel.getGpListApi(x.presentBlcokCode)
                                commonViewModel.getVillageListApi(x.presentGPCode)

                                                       delay(1000)

                                setDropdownValue(binding.SpinnerPresentAddressStateName, x.presentStateName, statePer)
                                setDropdownValue(binding.spinnerPresentAddressDistrict, x.presentDistrictName, districtPer)
                                setDropdownValue(binding.spinnerPresentAddressBlock, x.presentBlockName, blockPer)
                                setDropdownValue(binding.spinnerPresentAddressGp, x.presentGPName, gpPer)
                                setDropdownValue(binding.spinnerPresentAddressVillage, x.presentVillageName, villagePer)





                            }





                            binding.etAdressLine.setText(x.permanentStreet1)
                            binding.etAdressLine2.setText(x.permanentStreet2)
                            binding.etPinCode.setText(x.permanentPinCode)
                            val adreessStatus = x.isPresentAddressSame

                            binding.etPresentAddressAdressLine.setText(x.presentStreet1)
                            binding.etPresentLine2.setText(x.presentStreet2)
                            binding.etPresentPinCode.setText(x.presentPinCode)

                            handleStatus(binding.optionllSamePermanentYesSelect, binding.optionSamePermanentNoSelect, adreessStatus)


                            selectedStateCodeItem  =  x.permanentStateCode
                            selectedDistrictCodeItem= x.permanentDistrictCode
                            selectedBlockCodeItem  = x.permanentBlcokCode
                            selectedGpCodeItem = x.permanentGPCode
                            selectedVillageCodeItem=  x.permanentVillageCode
                            addressLine1 = x.presentStreet1
                            addressLine2= x.presentStreet2
                            pinCode=x.presentPinCode
                            isPermanentStatus=x.isPresentAddressSame


                            selectedStatePresentCodeItem=x.presentStateCode
                            selectedDistrictPresentCodeItem=x.presentDistrictCode
                            selectedBlockPresentCodeItem= x.presentBlcokCode
                            selectedGpPresentCodeItem=x.presentGPCode
                            selectedVillagePresentCodeItem=x.presentVillageCode


                            addressPresentLine1=x.permanentStreet1
                            addressPresentLine2=x.permanentStreet2
                            pinCodePresent=x.permanentPinCode


                        }














                    },
                    onNoClicked = {

                    }
                )


            }
        }

        binding.llTopEducational.setOnClickListener {

            commonViewModel.getTechEducation(BuildConfig.VERSION_NAME)

            if (isEducationalInfoVisible && educationalStatus.contains("0")) {
                isEducationalInfoVisible = false
                binding.expandEducational.visible()
                binding.viewEducational.visible()
            } else {


                isEducationalInfoVisible = true
                binding.expandEducational.gone()
                binding.viewEducational.gone()}

            if (educationalStatus.contains("1")){
                showYesNoDialog(
                    context = requireContext(),  // Use your context here (e.g., `requireContext()` in fragments)
                    title = "Confirmation",
                    message = "Do you want to edit your Educational info?",
                    onYesClicked = {
                        isEducationalInfoVisible = false
                        binding.expandEducational.visible()
                        binding.viewEducational.visible()
                        binding.btnEIddressSubmit.visible()
                        binding.spinnerDomainOfTech.visible()
                        binding.spinnerTechnicalEducation.visible()
                        binding.tvClickYearOfPassingTech.visible()

                        for (x in userCandidateEducationalDetailsList ){
                            setDropdownValue(binding.spinnerHighestEducation, x.highesteducation, highestEducationList)
                            setDropdownValue(binding.spinnerTechnicalEducation, x.techQualification, courseesName)
                            setDropdownValue(binding.spinnerDomainOfTech, x.techDomain, courseesDomainName)
                            binding.tvClickYearOfPassing.setText(x.monthYearOfPassing)
                            binding.tvLanguages.setText(x.language)
                            handleStatus(binding.optionTechnicalEducationYesSelect, binding.optionTechnicalEducationNoSelect, x.isTechEducate)
                            binding.tvClickYearOfPassingTech.setText(x.passingTechYear)



                            selectedHighestEducationItem=x.highesteducation
                            highestEducationDate=x.monthYearOfPassing
                            result= StringBuilder(x.language)

                            technicalEducationStatus =x.isTechEducate
                            selectedTechEducationItemCode = x.techQualification
                            selectedTechEducationDate = x.passingTechYear
                            selectedTechEducationDomainCode= x.techDomain


                        }
                    },
                    onNoClicked = {

                    }
                )

            }
        }

        binding.llTopEmployment.setOnClickListener {

            if (isEmploymentInfoVisible && employmentStatus.contains("0")) {
                isEmploymentInfoVisible = false
                binding.expandEmployment.visible()
                binding.viewEmployment.visible()
            } else {

                isEmploymentInfoVisible = true
                binding.expandEmployment.gone()
                binding.viewEmployment.gone()}
            if (employmentStatus.contains("1")){
                showYesNoDialog(
                    context = requireContext(),  // Use your context here (e.g., `requireContext()` in fragments)
                    title = "Confirmation",
                    message = "Do you want to edit your Employment info?",
                    onYesClicked = {
                        isEmploymentInfoVisible = false
                        binding.expandEmployment.visible()
                        binding.viewEmployment.visible()


                        for (x in userCandidateEmploymentDetailsList ){

                            val currentluempo = x.isEmployeed
                            val natureEmp = x.empNature

                            if (natureEmp.contains("Self Employed")){
                                binding.optionnatureOfEmplYesSelect.setBackgroundResource(R.drawable.card_background_selected)
                                binding.optionnatureOfEmpldNoSelect.setBackgroundResource(R.drawable.card_background)
                            }
                            else if (natureEmp.contains("Salaried")){

                                binding.optionnatureOfEmpldNoSelect.setBackgroundResource(R.drawable.card_background_selected)
                                binding.optionnatureOfEmplYesSelect.setBackgroundResource(R.drawable.card_background)
                            }

                            handleStatus(binding.optionCurentlyEmployedYesSelect, binding.optionCurentlyEmployedNoSelect, currentluempo)


                            binding.etCurrentEarning.setText(x.monthlyEarning)
                            binding.etExpectationSalary.setText(x.expectedSalary)


                            currentlyEmpStatus= x.isEmployeed
                            natureEmpEmpStatus= x.empNature
                            selectedInterestedIn= x.intrestedIn
                            selectedIEmploymentPref = x.empPreference
                            selectedJobLocation= x.preferJobLocation
                            currentSalary =x.monthlyEarning
                            salaryExpectation= x.expectedSalary



                        }


                    },
                    onNoClicked = {

                    }
                )


            }
        }

        binding.llTopBanking.setOnClickListener {

            if (isBankingInfoVisible && bankingStatus.contains("0")) {
                isBankingInfoVisible = false
                binding.expandBanking.visible()
                binding.viewBanking.visible()
            } else {

                isBankingInfoVisible = true
                binding.expandBanking.gone()
                binding.viewBanking.gone()}
            if ( bankingStatus.contains("1")){
                showYesNoDialog(
                    context = requireContext(),  // Use your context here (e.g., `requireContext()` in fragments)
                    title = "Confirmation",
                    message = "Do you want to edit your Banking info?",
                    onYesClicked = {
                        isBankingInfoVisible = false
                        binding.expandBanking.visible()
                        binding.viewBanking.visible()
                        binding.btnBnakingSubmit.visible()

                  for (x in userCandidateBankDetailsList){


                   val DecIfscCode = AESCryptography.decryptIntoString(x.ifscCode,AppConstant.Constants.ENCRYPT_KEY,AppConstant.Constants.ENCRYPT_IV_KEY)
                   val DecBankName = AESCryptography.decryptIntoString(x.bankName,AppConstant.Constants.ENCRYPT_KEY,AppConstant.Constants.ENCRYPT_IV_KEY)
                   val DecbankBranchName = AESCryptography.decryptIntoString(x.bankBranchName,AppConstant.Constants.ENCRYPT_KEY,AppConstant.Constants.ENCRYPT_IV_KEY)
                   val DecbankbankAccNumber = AESCryptography.decryptIntoString(x.bankAccNumber,AppConstant.Constants.ENCRYPT_KEY,AppConstant.Constants.ENCRYPT_IV_KEY)
                   val DecpanNo = AESCryptography.decryptIntoString(x.panNo,AppConstant.Constants.ENCRYPT_KEY,AppConstant.Constants.ENCRYPT_IV_KEY)


                      binding.etIfscCode.setText(DecIfscCode)
                      binding.etBankName.setText(DecBankName)
                      binding.etBranchName.setText(DecbankBranchName)
                      binding.etBankAcNo.setText(DecbankbankAccNumber)
                      binding.etPanNumber.setText(DecpanNo)

                          BankName =DecBankName
                          BankAcNo= DecbankbankAccNumber
                          IfscCode = DecIfscCode
                          PanNumber= DecpanNo

                  }


                    },
                    onNoClicked = {

                    }
                )

            }
        }

        binding.llTopTraining.setOnClickListener {

            if (isTrainingInfoVisible && trainingStatus.contains("0")) {
                isTrainingInfoVisible = false
                binding.expandTraining.visible()
                binding.viewTraining.visible()
            } else {
                isTrainingInfoVisible = true
                binding.expandTraining.gone()
                binding.viewTraining.gone()}
            if (trainingStatus.contains("1")){


                showYesNoDialog(
                    context = requireContext(),  // Use your context here (e.g., `requireContext()` in fragments)
                    title = "Confirmation",
                    message = "Do you want to edit your Training info?",
                    onYesClicked = {
                        isTrainingInfoVisible = false
                        binding.expandTraining.visible()
                        binding.viewTraining.visible()



                        for (x in userCandidateTrainingDetailsList){

                            val recievedTrainingBeforeStatus = x.isPreTraining
                            val heardStatus = x.hearedAboutScheme

                            // Set the UI based on conditions
                            handleStatus(binding.optionrecievedAnyTrainingBeforeYesSelect, binding.optioRecievedAnyTrainingBeforeNoSelect, recievedTrainingBeforeStatus)
                            handleStatus(binding.optionHaveYouHeardYes, binding.optionHaveYouHeardNo, heardStatus)
                            setDropdownValue(binding.spinnerHeardAboutddugky, x.hearedFrom, highestEducationList)

                            binding.tvClickPreviouslycompletedduring.text = x.compTrainingDuration
                            binding.tvSectorItems.text = x.sectorName
                            binding.tvTradeItems.text = x.trade


                            traingBeforeStatus = x.isPreTraining
                            selectedPrevCompleteTraining= x.preCompTraining
                            previousTrainingDuration= x.compTrainingDuration
                            haveUHeardStatus= x.hearedAboutScheme
                            selectedHeardABoutItem=x.hearedFrom
                            selectedSector= x.sectorName
                            selectedTrade= x.trade

                        }





                    },
                    onNoClicked = {

                    }
                )

            }
        }

        binding.tvClickYearOfPassing.setOnClickListener {
            showMonthYearPicker { selectedYear, selectedMonth ->
                // Handle the selected month and year

                highestEducationDate = "$selectedMonth/$selectedYear"
                binding.tvClickYearOfPassing.text = "$selectedMonth/$selectedYear"

            }
        }

        binding.tvClickYearOfPassingTech.setOnClickListener {

            showMonthYearPicker { selectedYear, selectedMonth ->
                // Handle the selected month and year
                binding.tvClickYearOfPassingTech.text = "$selectedMonth/$selectedYear"
                selectedTechEducationDate = "$selectedMonth/$selectedYear"

            }
        }

        binding.tvClickPreviouslycompletedduring.setOnClickListener {

            showMonthYearPicker { selectedYear, selectedMonth ->
                // Handle the selected month and year
                previouslycompletedduring = "$selectedMonth/$selectedYear"
                binding.tvClickPreviouslycompletedduring.text = "$selectedMonth/$selectedYear"

            }
        }

        //Ifsc Search

        binding.progressButton.centerButton.setOnClickListener {
            val inputText = binding.etIfscCode.text.toString()
            val upperCaseText = inputText.uppercase()
            val encryptedUpperCaseText =   AESCryptography.encryptIntoBase64String(upperCaseText, AppConstant.Constants.ENCRYPT_KEY, AppConstant.Constants.ENCRYPT_IV_KEY)

            commonViewModel.getBankDetailsAPI(BankingReq(BuildConfig.VERSION_NAME,
                encryptedUpperCaseText
            ))

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


        // Tech Education selection

        binding.spinnerTechnicalEducation.setOnItemClickListener { parent, view, position, id ->
            selectedTechEducationItem = parent.getItemAtPosition(position).toString()
            if (position in courseesName.indices) {
                selectedTechEducationItemCode = courseesCode[position]

                commonViewModel.getTechEducationDomainAPI(
                    BuildConfig.VERSION_NAME,
                    selectedTechEducationItemCode
                )
            } else toastShort("Wrong Selection")
        }

        // Tech Education Domain selection

        binding.spinnerDomainOfTech.setOnItemClickListener { parent, view, position, id ->
            selectedTechEducationDomainItem = parent.getItemAtPosition(position).toString()
            if (position in courseesDomainName.indices) {
                selectedTechEducationDomainCode = courseesDomainCode[position]
            } else toastShort("Wrong Selection")
        }

        // Tech Heard about selection

        binding.spinnerHeardAboutddugky.setOnItemClickListener { parent, view, position, id ->
            binding.spinnerHeardAboutddugky.clearFocus()
            selectedHeardABoutItem = parent.getItemAtPosition(position).toString()

            if (position in heardName.indices) {
                selectedHeardABoutCode = heardCode[position]
            } else toastShort("Wrong Selection")
        }

        // Secc State selection

        binding.spinnerStateSecc.setOnItemClickListener { parent, view, position, id ->
            selectedSeccStateItem = parent.getItemAtPosition(position).toString()
            binding.stateesecc.setHint("")
            if (position in state.indices) {
                selectedSeccStateCodeItem = stateCode[position]
                selectedSeccStateLgdCodeItem = stateLgdCode[position]
                commonViewModel.getDistrictListApi(selectedSeccStateCodeItem)
                lifecycleScope.launch {
                    collectDistrictResponse(true)

                }

                //Clearing Data
                selectedSeccDistrictCodeItem = ""
                selectedSeccDistrictLgdCodeItem = ""
                selectedSeccDistrictItem = ""
                binding.spinnerDistrictSecc.clearFocus()
                binding.spinnerDistrictSecc.setText("", false)

                selectedSeccBlockCodeItem = ""
                selectedSeccBlockLgdCodeItem = ""
                selectedBlockItem = ""
                binding.spinnerBlockSecc.clearFocus()
                binding.spinnerBlockSecc.setText("", false)

                district.clear()
                block.clear()
                gp.clear()
                village.clear()



                selectedSeccGpCodeItem = ""
                selectedSeccGpLgdCodeItem = ""
                selectedSeccGpItem = ""
                binding.spinnerGpSecc.clearFocus()
                binding.spinnerGpSecc.setText("", false)


                selectedSeccVillageCodeItem = ""
                selectedbSeccVillageLgdCodeItem = ""
                selectedSeccVillageItem = ""
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

                selectedSeccBlockCodeItem = ""
                selectedSeccBlockLgdCodeItem = ""
                selectedBlockItem = ""
                binding.spinnerBlockSecc.clearFocus()
                binding.spinnerBlockSecc.setText("", false)



                selectedSeccGpCodeItem = ""
                selectedSeccGpLgdCodeItem = ""
                selectedSeccGpItem = ""
                binding.spinnerGpSecc.clearFocus()
                binding.spinnerGpSecc.setText("", false)


                selectedSeccVillageCodeItem = ""
                selectedbSeccVillageLgdCodeItem = ""
                selectedSeccVillageItem = ""
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


                selectedSeccGpCodeItem = ""
                selectedSeccGpLgdCodeItem = ""
                selectedSeccGpItem = ""
                binding.spinnerGpSecc.clearFocus()
                binding.spinnerGpSecc.setText("", false)


                selectedSeccVillageCodeItem = ""
                selectedbSeccVillageLgdCodeItem = ""
                selectedSeccVillageItem = ""
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


                selectedSeccVillageCodeItem = ""
                selectedbSeccVillageLgdCodeItem = ""
                selectedSeccVillageItem = ""
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


/*
        binding.SpinnerStateName.setOnItemClickListener { parent, view, position, id ->
            selectedStateItem = parent.getItemAtPosition(position).toString()
            if (position in state.indices) {
                selectedStateCodeItem = stateCode[position]
                selectedStateLgdCodeItem = stateLgdCode[position]

                //Clearing Data
                selectedDistrictCodeItem = ""
                selectedDistrictLgdCodeItem = ""
                selectedDistrictItem = ""
                binding.spinnerDistrict.clearFocus()
                binding.spinnerDistrict.setText("", false)

                selectedDistrictPresentCodeItem = ""
                selectedDistrictPresentLgdCodeItem = ""
                selectedDistrictPresentItem = ""
                binding.spinnerPresentAddressDistrict.clearFocus()
                binding.spinnerPresentAddressDistrict.setText("", false)
                binding.SpinnerPresentAddressStateName.clearFocus()
                binding.SpinnerPresentAddressStateName.setText("", false)


                selectedBlockCodeItem = ""
                selectedbBlockLgdCodeItem = ""
                selectedBlockItem = ""
                binding.spinnerBlock.clearFocus()
                binding.spinnerBlock.setText("", false)

                selectedBlockPresentCodeItem = ""
                selectedbBlockPresentLgdCodeItem = ""
                selectedBlockPresentItem = ""
                binding.spinnerPresentAddressBlock.clearFocus()
                binding.spinnerPresentAddressBlock.setText("", false)



                selectedGpCodeItem = ""
                selectedbGpLgdCodeItem = ""
                selectedGpItem = ""
                binding.spinnerGp.clearFocus()
                binding.spinnerGp.setText("", false)


                selectedGpPresentCodeItem = ""
                selectedbGpPresentLgdCodeItem = ""
                selectedGpPresentItem = ""
                binding.spinnerPresentAddressGp.clearFocus()
                binding.spinnerPresentAddressGp.setText("", false)


                selectedVillageCodeItem = ""
                selectedbVillageLgdCodeItem = ""
                selectedVillageItem = ""
                binding.spinnerVillage.clearFocus()
                binding.spinnerVillage.setText("", false)


                selectedVillagePresentCodeItem = ""
                selectedbVillagePresentLgdCodeItem = ""
                selectedVillagePresentItem = ""
                binding.spinnerPresentAddressVillage.clearFocus()
                binding.spinnerPresentAddressVillage.setText("", false)


            } else {
                Toast.makeText(requireContext(), "Invalid selection", Toast.LENGTH_SHORT).show()
            }
        }
*/


        //District selection
        binding.spinnerDistrict.setOnItemClickListener { parent, view, position, id ->
            selectedDistrictItem = parent.getItemAtPosition(position).toString()
            if (position in district.indices) {
                selectedDistrictCodeItem = districtCode[position]
                selectedDistrictLgdCodeItem = districtLgdCode[position]
                commonViewModel.getBlockListApi(selectedDistrictCodeItem)




                selectedBlockCodeItem = ""
                selectedbBlockLgdCodeItem = ""
                selectedBlockItem = ""
                binding.spinnerBlock.clearFocus()
                binding.spinnerBlock.setText("", false)





                selectedGpCodeItem = ""
                selectedbGpLgdCodeItem = ""
                selectedGpItem = ""
                binding.spinnerGp.clearFocus()
                binding.spinnerGp.setText("", false)



                binding.spinnerPresentAddressGp.clearFocus()
                binding.spinnerPresentAddressGp.setText("", false)


                selectedVillageCodeItem = ""
                selectedbVillageLgdCodeItem = ""
                selectedVillageItem = ""
                binding.spinnerVillage.clearFocus()
                binding.spinnerVillage.setText("", false)


                selectedVillagePresentCodeItem = ""
                selectedbVillagePresentLgdCodeItem = ""
                selectedVillagePresentItem = ""
                selectedDistrictPresentCodeItem = ""
                selectedDistrictPresentLgdCodeItem = ""
                selectedDistrictPresentItem = ""
                selectedGpPresentCodeItem = ""
                selectedbGpPresentLgdCodeItem = ""
                selectedGpPresentItem = ""
                selectedBlockPresentCodeItem = ""
                selectedbBlockPresentLgdCodeItem = ""
                selectedBlockPresentItem = ""
                selectedDistrictPresentCodeItem = ""
                selectedDistrictPresentLgdCodeItem = ""
                selectedDistrictPresentItem = ""


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
            selectedBlockItem = parent.getItemAtPosition(position).toString()
            if (position in block.indices) {
                selectedBlockCodeItem = blockCode[position]
                selectedbBlockLgdCodeItem = blockLgdCode[position]
                commonViewModel.getGpListApi(selectedBlockCodeItem)


                selectedGpCodeItem = ""
                selectedbGpLgdCodeItem = ""
                selectedGpItem = ""
                binding.spinnerGp.clearFocus()
                binding.spinnerGp.setText("", false)





                selectedVillageCodeItem = ""
                selectedbVillageLgdCodeItem = ""
                selectedVillageItem = ""
                binding.spinnerVillage.clearFocus()
                binding.spinnerVillage.setText("", false)


                selectedVillagePresentCodeItem = ""
                selectedbVillagePresentLgdCodeItem = ""
                selectedVillagePresentItem = ""
                selectedDistrictPresentCodeItem = ""
                selectedDistrictPresentLgdCodeItem = ""
                selectedDistrictPresentItem = ""
                selectedGpPresentCodeItem = ""
                selectedbGpPresentLgdCodeItem = ""
                selectedGpPresentItem = ""
                selectedBlockPresentCodeItem = ""
                selectedbBlockPresentLgdCodeItem = ""
                selectedBlockPresentItem = ""
                selectedDistrictPresentCodeItem = ""
                selectedDistrictPresentLgdCodeItem = ""
                selectedDistrictPresentItem = ""


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
            selectedGpItem = parent.getItemAtPosition(position).toString()

            if (position in gp.indices) {
                selectedGpCodeItem = gpCode[position]
                selectedbGpLgdCodeItem = gpLgdCode[position]
                commonViewModel.getVillageListApi(selectedGpCodeItem)


                selectedVillageCodeItem = ""
                selectedbVillageLgdCodeItem = ""
                selectedVillageItem = ""
                binding.spinnerVillage.clearFocus()
                binding.spinnerVillage.setText("", false)


                selectedVillagePresentCodeItem = ""
                selectedbVillagePresentLgdCodeItem = ""
                selectedVillagePresentItem = ""
                selectedDistrictPresentCodeItem = ""
                selectedDistrictPresentLgdCodeItem = ""
                selectedDistrictPresentItem = ""
                selectedGpPresentCodeItem = ""
                selectedbGpPresentLgdCodeItem = ""
                selectedGpPresentItem = ""
                selectedBlockPresentCodeItem = ""
                selectedbBlockPresentLgdCodeItem = ""
                selectedBlockPresentItem = ""
                selectedDistrictPresentCodeItem = ""
                selectedDistrictPresentLgdCodeItem = ""
                selectedDistrictPresentItem = ""


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
            selectedVillageItem = parent.getItemAtPosition(position).toString()
            if (position in village.indices) {
                selectedVillageCodeItem = villageCode[position]
                selectedbVillageLgdCodeItem = villageLgdCode[position]



                selectedVillagePresentCodeItem = ""
                selectedbVillagePresentLgdCodeItem = ""
                selectedVillagePresentItem = ""
                selectedDistrictPresentCodeItem = ""
                selectedDistrictPresentLgdCodeItem = ""
                selectedDistrictPresentItem = ""
                selectedGpPresentCodeItem = ""
                selectedbGpPresentLgdCodeItem = ""
                selectedGpPresentItem = ""
                selectedBlockPresentCodeItem = ""
                selectedbBlockPresentLgdCodeItem = ""
                selectedBlockPresentItem = ""
                selectedDistrictPresentCodeItem = ""
                selectedDistrictPresentLgdCodeItem = ""
                selectedDistrictPresentItem = ""


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
                selectedStatePresentLgdCodeItem = stateLgdCode[position]
                commonViewModel.getDistrictListApi(selectedStatePresentCodeItem)

                lifecycleScope.launch {
                    collectDistrictResponse(false)
                }
                selectedVillagePresentCodeItem = ""
                selectedbVillagePresentLgdCodeItem = ""
                selectedVillagePresentItem = ""
                selectedDistrictPresentCodeItem = ""
                selectedDistrictPresentLgdCodeItem = ""
                selectedDistrictPresentItem = ""
                selectedGpPresentCodeItem = ""
                selectedbGpPresentLgdCodeItem = ""
                selectedGpPresentItem = ""
                selectedBlockPresentCodeItem = ""
                selectedbBlockPresentLgdCodeItem = ""
                selectedBlockPresentItem = ""
                selectedDistrictPresentCodeItem = ""
                selectedDistrictPresentLgdCodeItem = ""
                selectedDistrictPresentItem = ""


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
                selectedDistrictPresentCodeItem = districtCode[position]

                selectedDistrictPresentLgdCodeItem = districtLgdCode[position]
                commonViewModel.getBlockListApi(selectedDistrictPresentCodeItem)


                selectedVillagePresentCodeItem = ""
                selectedbVillagePresentLgdCodeItem = ""
                selectedVillagePresentItem = ""
                selectedGpPresentCodeItem = ""
                selectedbGpPresentLgdCodeItem = ""
                selectedGpPresentItem = ""
                selectedBlockPresentCodeItem = ""
                selectedbBlockPresentLgdCodeItem = ""
                selectedBlockPresentItem = ""


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
            selectedBlockPresentItem = parent.getItemAtPosition(position).toString()
            if (position in block.indices) {
                selectedBlockPresentCodeItem = blockCode[position]
                selectedbBlockPresentLgdCodeItem = blockLgdCode[position]
                commonViewModel.getGpListApi(selectedBlockPresentCodeItem)

                selectedVillagePresentCodeItem = ""
                selectedbVillagePresentLgdCodeItem = ""
                selectedVillagePresentItem = ""
                selectedGpPresentCodeItem = ""
                selectedbGpPresentLgdCodeItem = ""
                selectedGpPresentItem = ""


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
            selectedGpPresentItem = parent.getItemAtPosition(position).toString()
            if (position in gp.indices) {
                selectedGpPresentCodeItem = gpCode[position]
                selectedbGpPresentLgdCodeItem = gpLgdCode[position]
                commonViewModel.getVillageListApi(selectedGpPresentCodeItem)

                selectedVillagePresentCodeItem = ""
                selectedbVillagePresentLgdCodeItem = ""
                selectedVillagePresentItem = ""

                binding.spinnerPresentAddressVillage.clearFocus()
                binding.spinnerPresentAddressVillage.setText("", false)

            } else {
                Toast.makeText(requireContext(), "Invalid selection", Toast.LENGTH_SHORT).show()
            }
        }


        //Village Spinner
        binding.spinnerPresentAddressVillage.setOnItemClickListener { parent, view, position, id ->
            selectedVillagePresentItem = parent.getItemAtPosition(position).toString()
            if (position in village.indices) {
                selectedVillagePresentCodeItem = villageCode[position]
                selectedbVillagePresentLgdCodeItem = villageLgdCode[position]

            } else {
                Toast.makeText(requireContext(), "Invalid selection", Toast.LENGTH_SHORT).show()
            }
        }


        // If Present Address is same as permanent

        binding.optionllSamePermanentYesSelect.setOnClickListener {
            addressLine1 = binding.etAdressLine.text.toString()
            addressLine2 = binding.etAdressLine2.text.toString()
            pinCode = binding.etPinCode.text.toString()



            if (selectedStateCodeItem.isNotEmpty() &&
                selectedDistrictCodeItem.isNotEmpty() &&
                selectedBlockCodeItem.isNotEmpty() &&
                selectedGpCodeItem.isNotEmpty() &&
                selectedVillageCodeItem.isNotEmpty() && addressLine1.isNotEmpty()
                && addressLine2.isNotEmpty() && pinCode.isNotEmpty()
            ) {


                binding.optionllSamePermanentYesSelect.setBackgroundResource(R.drawable.card_background_selected) // Reset to default
                binding.optionSamePermanentNoSelect.setBackgroundResource(R.drawable.card_background) // Change to clicked color

                isClickedPermanentYes = true
                isClickedPermanentNo = false
                isPermanentStatus = "Yes"

                binding.llPresentAddressState.gone()
                binding.llPresentAddressDistrict.gone()
                binding.llPresentAddressBlock.gone()
                binding.llPresentAddressGp.gone()
                binding.llPresentAddressVillage.gone()
                binding.llPresentAddressAdressLine.gone()
                binding.btnAddressSubmit.visible()

                addressPresentLine1 = addressLine1
                addressPresentLine2 = addressLine2
                pinCodePresent = pinCode


                //Set State Value
                selectedStatePresentCodeItem = selectedStateCodeItem
                selectedStatePresentLgdCodeItem = selectedStateLgdCodeItem
                selectedStatePresentItem = selectedStateItem


                //Set District Value

                selectedDistrictPresentCodeItem = selectedDistrictCodeItem
                selectedDistrictPresentLgdCodeItem = selectedDistrictLgdCodeItem
                selectedDistrictPresentItem = selectedDistrictItem

                //Set Block Value

                selectedBlockPresentCodeItem = selectedBlockCodeItem
                selectedbBlockPresentLgdCodeItem = selectedbBlockLgdCodeItem
                selectedBlockPresentItem = selectedBlockItem

                //Set GP Value
                selectedGpPresentCodeItem = selectedGpCodeItem
                selectedbGpPresentLgdCodeItem = selectedbGpLgdCodeItem
                selectedGpPresentItem = selectedGpItem


                //Set Village Value
                selectedVillagePresentCodeItem = selectedVillageCodeItem
                selectedbVillagePresentLgdCodeItem = selectedbVillageLgdCodeItem
                selectedVillagePresentItem = selectedVillageItem

                //others


            } else

                toastLong("Please Complete Your Permanent Address First")


        }

        // If Present Address is not same as permanent


        binding.optionSamePermanentNoSelect.setOnClickListener {

            addressLine1 = binding.etAdressLine.text.toString()
            addressLine2 = binding.etAdressLine2.text.toString()
            pinCode = binding.etPinCode.text.toString()

            if (selectedStateCodeItem.isNotEmpty() &&
                selectedDistrictCodeItem.isNotEmpty() &&
                selectedBlockCodeItem.isNotEmpty() &&
                selectedGpCodeItem.isNotEmpty() &&
                selectedVillageCodeItem.isNotEmpty() && addressLine1.isNotEmpty()
                && addressLine2.isNotEmpty() && pinCode.isNotEmpty()
            ) {


                isClickedPermanentNo = true
                isClickedPermanentYes = false
                isPermanentStatus = "No"

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
            } else
                toastLong("Please Complete Your Permanent Address First")


        }

        //Marital Selection If yes
        binding.optionMinorityYesSelect.setOnClickListener {
            binding.optionMinorityYesSelect.setBackgroundResource(R.drawable.card_background_selected)
            binding.optionMinorityNoSelect.setBackgroundResource(R.drawable.card_background)

            minorityStatus = "Yes"
            binding.minorityImageUpload.visible()


        }
        //Marital Selection If No
        binding.optionMinorityNoSelect.setOnClickListener {
            binding.optionMinorityYesSelect.setBackgroundResource(R.drawable.card_background)
            binding.optionMinorityNoSelect.setBackgroundResource(R.drawable.card_background_selected)

            minorityStatus = "No"

            binding.minorityImageUpload.gone()


        }


        //PWD Selection If yes
        binding.optionPwdYesSelect.setOnClickListener {
            binding.optionPwdYesSelect.setBackgroundResource(R.drawable.card_background_selected)
            binding.optionPwdNoSelect.setBackgroundResource(R.drawable.card_background)

            pwdStatus = "Yes"
            binding.pwdImageUpload.visible()


        }
        //PWD Selection If No
        binding.optionPwdNoSelect.setOnClickListener {
            binding.optionPwdYesSelect.setBackgroundResource(R.drawable.card_background)
            binding.optionPwdNoSelect.setBackgroundResource(R.drawable.card_background_selected)

            pwdStatus = "No"

            binding.pwdImageUpload.gone()


        }


        //Have You Heard About DDUGKY Selection If yes
        binding.optionHaveYouHeardYes.setOnClickListener {
            binding.optionHaveYouHeardYes.setBackgroundResource(R.drawable.card_background_selected)
            binding.optionHaveYouHeardNo.setBackgroundResource(R.drawable.card_background)

            haveUHeardStatus = "Yes"
            commonViewModel.getWhereHaveYouHeardAPI()
            binding.upHeardAboutddugky.visible()
            binding.tvHeardAboutddugky.visible()


        }
        //Have You Heard About DDUGKY Selection If No
        binding.optionHaveYouHeardNo.setOnClickListener {
            binding.optionHaveYouHeardYes.setBackgroundResource(R.drawable.card_background)
            binding.optionHaveYouHeardNo.setBackgroundResource(R.drawable.card_background_selected)

            haveUHeardStatus = "No"

            binding.upHeardAboutddugky.gone()
            binding.tvHeardAboutddugky.gone()


        }


        //Technical Education Selection If yes
        binding.optionTechnicalEducationYesSelect.setOnClickListener {
            binding.optionTechnicalEducationYesSelect.setBackgroundResource(R.drawable.card_background_selected)
            binding.optionTechnicalEducationNoSelect.setBackgroundResource(R.drawable.card_background)

            technicalEducationStatus = "Yes"
            binding.llTechEducation.visible()
            binding.llYearOfPassingTech.visible()
            binding.llDomainOfTech.visible()
            binding.btnEIddressSubmit.visible()


        }
        //Technical Education Selection If No
        binding.optionTechnicalEducationNoSelect.setOnClickListener {
            binding.optionTechnicalEducationYesSelect.setBackgroundResource(R.drawable.card_background)
            binding.optionTechnicalEducationNoSelect.setBackgroundResource(R.drawable.card_background_selected)

            technicalEducationStatus = "No"

            binding.llTechEducation.gone()
            binding.llYearOfPassingTech.gone()
            binding.llDomainOfTech.gone()
            binding.btnEIddressSubmit.visible()


        }


        //Shg Selection If yes
        binding.optionShgYesSelect.setOnClickListener {
            binding.optionShgYesSelect.setBackgroundResource(R.drawable.card_background_selected)
            binding.optionShgNoSelect.setBackgroundResource(R.drawable.card_background)

            shgStatus = "Yes"
            binding.etShgValidate.visible()
            binding.btnShgValidate.visible()


        }
        //SHG Selection If No
        binding.optionShgNoSelect.setOnClickListener {
            binding.optionShgYesSelect.setBackgroundResource(R.drawable.card_background)
            binding.optionShgNoSelect.setBackgroundResource(R.drawable.card_background_selected)

            shgStatus = "No"
            binding.etShgValidate.gone()
            binding.btnShgValidate.gone()
            binding.tvShgValidate.gone()


        }



        //Nrega Selection If yes
        binding.optionNregaJobYesSelect.setOnClickListener {
            binding.optionNregaJobYesSelect.setBackgroundResource(R.drawable.card_background_selected)
            binding.optionNregaJobNoSelect.setBackgroundResource(R.drawable.card_background)

            nregaStatus = "Yes"
            binding.etNregaValidate.visible()
            binding.btnjobcardnoValidate.visible()
            binding.nregaJobUpload.visible()


        }
        //Nrega Selection If No
        binding.optionNregaJobNoSelect.setOnClickListener {
            binding.optionNregaJobYesSelect.setBackgroundResource(R.drawable.card_background)
            binding.optionNregaJobNoSelect.setBackgroundResource(R.drawable.card_background_selected)

            nregaStatus = "No"
            binding.etNregaValidate.gone()
            binding.btnjobcardnoValidate.gone()
            binding.nregaJobUpload.gone()


        }


        //Antoyada Selection If yes
        binding.optionAntyodayaYesSelect.setOnClickListener {
            binding.optionAntyodayaYesSelect.setBackgroundResource(R.drawable.card_background_selected)
            binding.optionAntyodayaNoSelect.setBackgroundResource(R.drawable.card_background)

            antoyadaStatus = "Yes"
            binding.antyodayaCardUpload.visible()


        }
        //Antoyada Selection If No
        binding.optionAntyodayaNoSelect.setOnClickListener {
            binding.optionAntyodayaYesSelect.setBackgroundResource(R.drawable.card_background)
            binding.optionAntyodayaNoSelect.setBackgroundResource(R.drawable.card_background_selected)

            antoyadaStatus = "No"

            binding.antyodayaCardUpload.gone()


        }

        //Rsby Selection If yes
        binding.optionllRsbyYesSelect.setOnClickListener {
            binding.optionllRsbyYesSelect.setBackgroundResource(R.drawable.card_background_selected)
            binding.optionllRsbyNoSelect.setBackgroundResource(R.drawable.card_background)

            rsbyStatus = "Yes"
            binding.rsbyUpload.visible()


        }
        //Rsby Selection If No
        binding.optionllRsbyNoSelect.setOnClickListener {
            binding.optionllRsbyYesSelect.setBackgroundResource(R.drawable.card_background)
            binding.optionllRsbyNoSelect.setBackgroundResource(R.drawable.card_background_selected)

            rsbyStatus = "No"

            binding.rsbyUpload.gone()


        }


        //PIP Selection If yes
        binding.optionPipYesSelect.setOnClickListener {
            binding.optionPipYesSelect.setBackgroundResource(R.drawable.card_background_selected)
            binding.optionPipNoSelect.setBackgroundResource(R.drawable.card_background)

            pipStatus = "Yes"


        }
        //PIP Selection If No
        binding.optionPipNoSelect.setOnClickListener {
            binding.optionPipYesSelect.setBackgroundResource(R.drawable.card_background)
            binding.optionPipNoSelect.setBackgroundResource(R.drawable.card_background_selected)

            pipStatus = "No"

        }

        //Currently Emp Selection If yes
        binding.optionCurentlyEmployedYesSelect.setOnClickListener {
            binding.optionCurentlyEmployedYesSelect.setBackgroundResource(R.drawable.card_background_selected)
            binding.optionCurentlyEmployedNoSelect.setBackgroundResource(R.drawable.card_background)

            currentlyEmpStatus = "Yes"


        }
        //Currently Emp  Selection If No
        binding.optionCurentlyEmployedNoSelect.setOnClickListener {
            binding.optionCurentlyEmployedYesSelect.setBackgroundResource(R.drawable.card_background)
            binding.optionCurentlyEmployedNoSelect.setBackgroundResource(R.drawable.card_background_selected)

            currentlyEmpStatus = "No"

        }


        //Nature Of Emp Selection If yes
        binding.optionnatureOfEmplYesSelect.setOnClickListener {
            binding.optionnatureOfEmplYesSelect.setBackgroundResource(R.drawable.card_background_selected)
            binding.optionnatureOfEmpldNoSelect.setBackgroundResource(R.drawable.card_background)

            natureEmpEmpStatus = "Self Employed"


        }
        //Nature Of  Selection If No
        binding.optionnatureOfEmpldNoSelect.setOnClickListener {
            binding.optionnatureOfEmplYesSelect.setBackgroundResource(R.drawable.card_background)
            binding.optionnatureOfEmpldNoSelect.setBackgroundResource(R.drawable.card_background_selected)

            natureEmpEmpStatus = "Salary"

        }


        //Have u received training beforeSelection If yes
        binding.optionrecievedAnyTrainingBeforeYesSelect.setOnClickListener {
            binding.optionrecievedAnyTrainingBeforeYesSelect.setBackgroundResource(R.drawable.card_background_selected)
            binding.optioRecievedAnyTrainingBeforeNoSelect.setBackgroundResource(R.drawable.card_background)

            traingBeforeStatus = "Yes"
            binding.llPreviousComTraining.visible()
            binding.tvPrevCom.visible()
            binding.tvClickPreviouslycompletedduring.visible()


        }
        //Have u received training beforeSelection If No
        binding.optioRecievedAnyTrainingBeforeNoSelect.setOnClickListener {
            binding.optionrecievedAnyTrainingBeforeYesSelect.setBackgroundResource(R.drawable.card_background)
            binding.optioRecievedAnyTrainingBeforeNoSelect.setBackgroundResource(R.drawable.card_background_selected)

            traingBeforeStatus = "No"
            binding.llPreviousComTraining.gone()
            binding.tvPrevCom.gone()
            binding.tvClickPreviouslycompletedduring.gone()

        }


        // If Secc Yess

        binding.optionOrigSecccYesSelect.setOnClickListener {

            binding.optionOrigSecccYesSelect.setBackgroundResource(R.drawable.card_background_selected) // Reset to default
            binding.optionOrigSecccNoSelect.setBackgroundResource(R.drawable.card_background) // Change to clicked color

            binding.etATINName.visible()
            binding.searchView.gone()
            binding.recyclerView.gone()


        }

        binding.optionOrigSecccNoSelect.setOnClickListener {

            binding.optionOrigSecccNoSelect.setBackgroundResource(R.drawable.card_background_selected) // Reset to default
            binding.optionOrigSecccYesSelect.setBackgroundResource(R.drawable.card_background) // Change to clicked color

            binding.etATINName.gone()
            binding.searchView.visible()
            binding.recyclerView.visible()

        }

        binding.tvSectorItems.setOnClickListener {
            MaterialDialog(requireContext()).show {
                title(text = "Select Sectors")
                val itemList = sectorList.toList()

                listItemsMultiChoice(
                    items = itemList,
                    initialSelection = selectedSectorIndices.toIntArray() // Use sector-specific indices
                ) { _, indices, _ ->
                    // Update selected indices for sectors
                    selectedSectorIndices = indices.toMutableList()

                    // Display selected items in the TextView
                    binding.tvSectorItems.text =
                        "Selected Items: ${indices.joinToString(",") { itemList[it] }}"

                    // Store the selected sectors as a comma-separated string
                    selectedSector = indices.joinToString(",") { itemList[it] }

                    // Store the selected sector codes as a comma-separated string
                    selectedSectorCode = indices.joinToString(",") { sectorCode[it] }
                    toastLong("Value at index $selectedSectorCode")

                    // Call API with selected sector codes
                    commonViewModel.getTradeListAPI(TradeReq(BuildConfig.VERSION_NAME, selectedSectorCode))
                    selectedTradeIndices.clear()
                    binding.tvTradeItems.text ="Select Trade"
                    selectedTrade=""


                }

                positiveButton(text = "OK")
                negativeButton(text = "Cancel")
            }
        }

        binding.tvTradeItems.setOnClickListener {
            MaterialDialog(requireContext()).show {
                title(text = "Select Trades")
                val itemList = tradeName.toList()

                listItemsMultiChoice(
                    items = itemList,
                    initialSelection = selectedTradeIndices.toIntArray() // Use trade-specific indices
                ) { _, indices, _ ->
                    // Update selected indices for trades
                    selectedTradeIndices = indices.toMutableList()

                    // Display selected items in the TextView
                    binding.tvTradeItems.text =
                        "Selected Items: ${indices.joinToString(",") { itemList[it] }}"

                    // Store the selected trades as a comma-separated string
                    selectedTrade = indices.joinToString(",") { itemList[it] }
                }

                positiveButton(text = "OK")
                negativeButton(text = "Cancel")
            }
        }
        //All Submit Button Here


        binding.profileView.viewDetails.setOnClickListener {

            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToViewDetailsFragment())
        }

        binding.btnBnakingSubmit.setOnClickListener {

            IfscCode = binding.etIfscCode.text.toString()
            BankName = binding.etBankName.text.toString()
            BranchName = binding.etBranchName.text.toString()
            BankAcNo =  binding.etBankAcNo.text.toString()
            PanNumber = binding.etPanNumber.text.toString()


            if (IfscCode.isNotEmpty() && BankName.isNotEmpty() &&
                BranchName.isNotEmpty() && BankAcNo.isNotEmpty()
                && PanNumber.isNotEmpty()){



                // Hit Insert API

            //    val encryptedUserID =   AESCryptography.encryptIntoBase64String(userPreferences.getUseID(), AppConstant.Constants.ENCRYPT_KEY, AppConstant.Constants.ENCRYPT_IV_KEY)
                val encryptedBankCode =   AESCryptography.encryptIntoBase64String(bankCode, AppConstant.Constants.ENCRYPT_KEY, AppConstant.Constants.ENCRYPT_IV_KEY)
                val encryptedBranchCode =   AESCryptography.encryptIntoBase64String(branchCode, AppConstant.Constants.ENCRYPT_KEY, AppConstant.Constants.ENCRYPT_IV_KEY)
                val encryptedBankAcNo =   AESCryptography.encryptIntoBase64String(BankAcNo, AppConstant.Constants.ENCRYPT_KEY, AppConstant.Constants.ENCRYPT_IV_KEY)
                val encryptedIfscCode =   AESCryptography.encryptIntoBase64String(IfscCode, AppConstant.Constants.ENCRYPT_KEY, AppConstant.Constants.ENCRYPT_IV_KEY)
                val encryptedPanNumber =   AESCryptography.encryptIntoBase64String(PanNumber, AppConstant.Constants.ENCRYPT_KEY, AppConstant.Constants.ENCRYPT_IV_KEY)


                commonViewModel.insertBankingAPI(BankingInsertReq(BuildConfig.VERSION_NAME,userPreferences.getUseID(),AppUtil.getAndroidId(requireContext()),"7",
                    encryptedBankCode,encryptedBranchCode,encryptedBankAcNo,encryptedIfscCode,encryptedPanNumber))

                collectInsertBankingResponse()


            }

            else
                toastShort("Please Complete Bank Details First")
        }


        binding.btnTrainingSubmit.setOnClickListener {

            //Previous Training

            val selectedChipIds = binding.chipPreviousComTraGroup.checkedChipIds

            // Map selected chip IDs to chip text
            val selectedOptions = selectedChipIds.mapNotNull { id ->
                val chip = binding.chipPreviousComTraGroup.findViewById<Chip>(id)
                chip?.text?.toString()
            }

            // Join the selected options into a comma-separated string
            selectedPrevCompleteTraining = selectedOptions.joinToString(", ")
            traingBeforeStatus
            previousTrainingDuration= binding.tvClickPreviouslycompletedduring.text.toString()


            if (traingBeforeStatus.isNotEmpty() && selectedSector.isNotEmpty() && selectedTrade.isNotEmpty()) {


                if (traingBeforeStatus.contains("Yes") &&  selectedPrevCompleteTraining.isNotEmpty())
                {
                    //Hit the Insert API

                    commonViewModel.insertTrainingAPI(TrainingInsertReq(BuildConfig.VERSION_NAME,userPreferences.getUseID(),
                        AppUtil.getAndroidId(requireContext()),"6",traingBeforeStatus,selectedPrevCompleteTraining,
                        previousTrainingDuration,haveUHeardStatus, selectedHeardABoutItem,selectedSectorCode,selectedTrade))

                    collectInsertTrainingResponse()




                }
                else if (traingBeforeStatus.contains("No") && haveUHeardStatus.contains("No"))
                {


                  //Hit the Insert API

                    commonViewModel.insertTrainingAPI(TrainingInsertReq(BuildConfig.VERSION_NAME,userPreferences.getUseID(),
                        AppUtil.getAndroidId(requireContext()),"6",traingBeforeStatus,selectedPrevCompleteTraining,
                        previousTrainingDuration,haveUHeardStatus, selectedHeardABoutItem,selectedSector,selectedTrade))

                    collectInsertTrainingResponse()




                }
                else if (traingBeforeStatus.contains("No") && haveUHeardStatus.contains("Yes") && selectedHeardABoutItem.isNotEmpty()){

                    commonViewModel.insertTrainingAPI(TrainingInsertReq(BuildConfig.VERSION_NAME,userPreferences.getUseID(),
                        AppUtil.getAndroidId(requireContext()),"6",traingBeforeStatus,selectedPrevCompleteTraining,
                        previousTrainingDuration,haveUHeardStatus, selectedHeardABoutItem,selectedSector,selectedTrade))

                    collectInsertTrainingResponse()


                }
                else {

                    toastShort("Please complete training info first")


                }



            }
            else {

                toastShort("Please complete training info first")


            }


        }

        binding.btnEmployementSubmit.setOnClickListener {


            //Interested In

            val selectedChipIds = binding.chipGroup.checkedChipIds

            // Map selected chip IDs to chip text
            val selectedOptions = selectedChipIds.mapNotNull { id ->
                val chip = binding.chipGroup.findViewById<Chip>(id)
                chip?.text?.toString()
            }

            // Join the selected options into a comma-separated string
            selectedInterestedIn = selectedOptions.joinToString(", ")


            // Employment Pref

            val selectedSelfEmpChipIds = binding.chipemployementPrefGroup.checkedChipIds

            // Map selected chip IDs to chip text
            val selectedSEOptions = selectedSelfEmpChipIds.mapNotNull { id ->
                val chip = binding.chipemployementPrefGroup.findViewById<Chip>(id)
                chip?.text?.toString()
            }

            // Join the selected options into a comma-separated string
            selectedIEmploymentPref = selectedSEOptions.joinToString(", ")


            // Job Location

            val selectedJobLocChipIds = binding.chipJobLocGroup.checkedChipIds

            // Map selected chip IDs to chip text
            val selectedJobLocOptions = selectedJobLocChipIds.mapNotNull { id ->
                val chip = binding.chipJobLocGroup.findViewById<Chip>(id)
                chip?.text?.toString()
            }

            // Join the selected options into a comma-separated string
            selectedJobLocation = selectedJobLocOptions.joinToString(", ")





            if (currentlyEmpStatus.isEmpty()) {

                toastShort("Please select Currently Employed")


            } else if (selectedInterestedIn.isEmpty()) {

                toastShort("Please select Interested in ")

            }
            else if (selectedJobLocation.isNotEmpty() && selectedIEmploymentPref.isNotEmpty()){



                //HitInsertAPI
                 currentSalary = binding.etCurrentEarning.text.toString()
                 salaryExpectation = binding.etExpectationSalary.text.toString()
                if (salaryExpectation.isNull){


                    toastShort("Please fill Salary expectation first")

                }

                else{

                    commonViewModel.insertEmploymentAPI(EmploymentInsertReq(BuildConfig.VERSION_NAME,userPreferences.getUseID(),AppUtil.getAndroidId(requireContext()),"5",
                        currentlyEmpStatus,natureEmpEmpStatus,selectedInterestedIn,selectedIEmploymentPref,selectedJobLocation,currentSalary,salaryExpectation))


                    collectInsertEmployementResponse()


                }


            }
            else {

                toastShort("Please complete Employment info first")


            }

        }

        binding.btnEIddressSubmit.setOnClickListener {



            if (selectedHighestEducationItem.isNotEmpty() && highestEducationDate.isNotEmpty() &&
               result.isNotEmpty()  && technicalEducationStatus.contains("No")
            ) {


                selectedTechEducationItemCode = ""
                selectedTechEducationDomainCode = ""

                // Hit the Insert Api




                commonViewModel.insertEducationAPI(EducationalInsertReq(BuildConfig.VERSION_NAME,userPreferences.getUseID(),AppUtil.getAndroidId(requireContext()),
                    "4",selectedHighestEducationItem,highestEducationDate,
                    result.toString(),technicalEducationStatus,selectedTechEducationItemCode,selectedTechEducationDate,
                    selectedTechEducationDomainCode ))

                collectInsertEducationalResponse()


            } else if (selectedHighestEducationItem.isNotEmpty() && highestEducationDate.isNotEmpty() &&
                technicalEducationStatus.contains("Yes") && selectedTechEducationItemCode.isNotEmpty() &&
                selectedTechEducationDomainCode.isNotEmpty() &&
                result.isNotEmpty()
            ) {

                // Hit the Insert Api


                commonViewModel.insertEducationAPI(EducationalInsertReq(BuildConfig.VERSION_NAME,userPreferences.getUseID(),AppUtil.getAndroidId(requireContext()),
                    "4",selectedHighestEducationItem,highestEducationDate,
                    result.toString(),technicalEducationStatus,selectedTechEducationItemCode,selectedTechEducationDate,
                    selectedTechEducationDomainCode ))

                collectInsertEducationalResponse()



            } else toastLong("Please complete Education Info First")

        }


        binding.btnShgValidate.setOnClickListener {

            commonViewModel.shgValidateAPI(
                ShgValidateReq(
                    binding.etShgValidate.text.toString(),
                    userPreferences.getUserStateLgdCode()
                )
            )  //41358

            lifecycleScope.launch {
                delay(1000)
                if (shgValidateStatus == "Y") {

                    binding.tvShgValidate.visible()
                    binding.btnShgValidate.gone()
                    shgCode = binding.etShgValidate.text.toString()
                    binding.tvShgValidate.text = "Validate Successfully: $shgName"
                } else toastLong("Validation Failed please check your SHG Code")
            }
        }


        binding.btnjobcardnoValidate.setOnClickListener {

            val username = HashUtils.sha512("Nrega") // Encrypt the username
            val password = HashUtils.sha512("Nrg2k18") // Encrypt the password
            jobCardNo= binding.etNregaValidate.text.toString()
            val fullUrl = "https://nregarep2.nic.in/webapi/api/checkjobcard"

            if (jobCardNo.isNotEmpty()){

              //  commonViewModel.getCheckJobCardAPI(fullUrl,username,password,jobCardNo )
                commonViewModel.getCheckJobCardAPI(fullUrl,username,password,"HR-01-005-001-001/1128" )

            }
            else toastShort("Please enter jobCard")
        }

        binding.btnAddressSubmit.setOnClickListener {

            addressLine1 = binding.etAdressLine.text.toString()
            addressLine2 = binding.etAdressLine2.text.toString()
            pinCode = binding.etPinCode.text.toString()

            if (isClickedPermanentNo) {
                addressPresentLine1 = binding.etPresentAddressAdressLine.text.toString()
                addressPresentLine2 = binding.etPresentLine2.text.toString()
                pinCodePresent = binding.etPresentPinCode.text.toString()


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
                selectedVillagePresentCodeItem.isNotEmpty() && addressLine1.isNotEmpty() &&
                pinCode.isNotEmpty() && addressPresentLine1.isNotEmpty() &&
                pinCodePresent.isNotEmpty()
            ) {

                // Hit The Insert API

                commonViewModel.insertAddressAPI(AddressInsertReq(BuildConfig.VERSION_NAME,userPreferences.getUseID(),AppUtil.getAndroidId(requireContext()),"2",

                    selectedStateCodeItem,selectedDistrictCodeItem,selectedBlockCodeItem,selectedGpCodeItem,selectedVillageCodeItem,addressLine1,
                    addressLine2,pinCode,residenceImage,isPermanentStatus,  selectedStatePresentCodeItem,selectedDistrictPresentCodeItem,selectedBlockPresentCodeItem,selectedGpPresentCodeItem,selectedVillagePresentCodeItem,
                    addressPresentLine1,addressPresentLine2,pinCodePresent))




                collectInsertAddressResponse()

            } else toastLong("Please complete your address first")
        }

        binding.btnSeccSubmit.setOnClickListener {



            if (selectedSeccStateCodeItem.isNotEmpty() && selectedSeccDistrictCodeItem.isNotEmpty()
                && selectedSeccBlockCodeItem.isNotEmpty() && selectedSeccGpCodeItem.isNotEmpty()
                && selectedSeccVillageCodeItem.isNotEmpty()
            ) {

                if (seccStatus.contains("No")){
                    if (selectedAhlTin.isNotEmpty()&& selectedSeccName.isNotEmpty()){

                        commonViewModel.insertSeccAPI(SeccInsertReq(BuildConfig.VERSION_NAME,userPreferences.getUseID(),AppUtil.getAndroidId(requireContext()),
                            "3",selectedSeccStateCodeItem,selectedSeccDistrictCodeItem,selectedSeccBlockCodeItem,selectedSeccGpCodeItem,selectedSeccVillageCodeItem,
                            selectedSeccName,selectedAhlTin))
                        collectInsertSeccResponse()

                    }


                }

                else {
                    selectedSeccName=""
                    selectedSeccName = binding.etATINName.text.toString()

                    if ( selectedSeccName.isNotEmpty()){


                        commonViewModel.insertSeccAPI(
                            SeccInsertReq(
                                BuildConfig.VERSION_NAME,
                                userPreferences.getUseID(),
                                AppUtil.getAndroidId(requireContext()),
                                "3",
                                selectedSeccStateCodeItem,
                                selectedSeccDistrictCodeItem,
                                selectedSeccBlockCodeItem,
                                selectedSeccGpCodeItem,
                                selectedSeccVillageCodeItem,
                                selectedSeccName,
                                selectedAhlTin
                            )
                        )

                        collectInsertSeccResponse()



                    }
                    else

                        toastLong("Please fill Secc Name First")



                }




            } else
                toastLong("Please Complete SECC info First")

        }

        binding.btnPersonalSubmit.setOnClickListener {

            guardianName = binding.etGName.text.toString()
            motherName = binding.etMotherName.text.toString()
            guardianMobileNumber = binding.etGNumber.text.toString()
             yearlyIncomeFamily = binding.etFIncome.text.toString()

            voterIdNo = binding.etllVoterId.text.toString()
            drivingLicenceNumber = binding.etdrivingId.text.toString()


            if(guardianName.isNotEmpty()&& motherName.isNotEmpty()&& guardianMobileNumber.isNotEmpty()
                &&selectedCategoryItem.isNotEmpty()&& selectedMaritalItem.isNotEmpty()&& minorityStatus.isNotEmpty()&&
                pwdStatus.isNotEmpty()){

                //Hit Insert API
                commonViewModel.insertPersonalDataAPI(PersonalInsertReq(BuildConfig.VERSION_NAME,userPreferences.getUseID(),AppUtil.getAndroidId(requireContext()),"1" ,
                    guardianName,motherName,guardianMobileNumber,yearlyIncomeFamily,voterIdNo,voterIdImage,drivingLicenceNumber,drivingLicenceImage,selectedCategoryItem,categoryCertiImage,
                    selectedMaritalItem,minorityStatus,minorityImage,pwdStatus,pwdImage,nregaStatus,nregaImageJobCard,nregaJobCard,shgStatus,shgCode,antoyadaStatus,antoyadaImage,
                    rsbyStatus,rsbyImage,pipStatus))


                collectInsertPersonalResponse()



            }
            else toastShort("Kindly complete Personal detail Mandatory Fields")


        }


        // External Validation


        //image Upload

        binding.voterIdUpload.setOnClickListener {
            checkAndRequestPermissionsForPurpose("VOTER_ID")
        }
        binding.nregaJobUpload.setOnClickListener {

            checkAndRequestPermissionsForPurpose("NREGA_ID")

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


    private fun collectInsertPersonalResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.insertPersonalDataAPI) {
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
                        it.data?.let { insertPersResponse ->
                            when (insertPersResponse.responseCode) {
                                200 -> {

                                    showSnackBar(insertPersResponse.responseMsg)
                                    commonViewModel.getCandidateDetailsAPI(CandidateReq(BuildConfig.VERSION_NAME,userPreferences.getUseID()))
                                    commonViewModel.getSecctionAndPerAPI(SectionAndPerReq(BuildConfig.VERSION_NAME,userPreferences.getUseID(),AppUtil.getAndroidId(requireContext())))

                                    if (isPersonalVisible && personalStatus.contains("0")) {
                                        isPersonalVisible = false
                                        binding.personalExpand.visible()
                                        binding.viewSecc.visible()

                                    } else {
                                        isPersonalVisible = true
                                        binding.personalExpand.gone()
                                        binding.viewSecc.gone()
                                    }

                                }
                                301 -> {
                                    showSnackBar("Please Update from PlayStore")
                                }
                                else -> {
                                    showSnackBar("Something went wrong")
                                }
                            }
                        } ?: showSnackBar("Internal Server Error")
                    }
                }

            }
        }
    }

    private fun collectInsertAddressResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.insertAddressAPI) {
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
                        it.data?.let { insertPersResponse ->
                            when (insertPersResponse.responseCode) {
                                200 -> {

                                    showSnackBar(insertPersResponse.responseMsg)

                                    commonViewModel.getCandidateDetailsAPI(CandidateReq(BuildConfig.VERSION_NAME,userPreferences.getUseID()))

                                    commonViewModel.getSecctionAndPerAPI(SectionAndPerReq(BuildConfig.VERSION_NAME,userPreferences.getUseID(),AppUtil.getAndroidId(requireContext())))
                                    if (isAddressVisible && addressStatus.contains("0")) {
                                        isAddressVisible = false
                                        binding.expandAddress.visible()
                                        binding.viewAddress.visible()
                                    } else {
                                        isAddressVisible = true
                                        binding.expandAddress.gone()
                                        binding.viewAddress.gone()
                                    }


                                }
                                301 -> {
                                    showSnackBar("Please Update from PlayStore")
                                }
                                else -> {
                                    showSnackBar("Something went wrong")
                                }
                            }
                        } ?: showSnackBar("Internal Server Error")
                    }
                }
            }
        }
    }

    private fun collectInsertSeccResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.insertSeccAPI) {
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
                        it.data?.let { insertPersResponse ->
                            when (insertPersResponse.responseCode) {
                                200 -> {

                                    showSnackBar(insertPersResponse.responseMsg)
                                    commonViewModel.getSecctionAndPerAPI(SectionAndPerReq(BuildConfig.VERSION_NAME,userPreferences.getUseID(),AppUtil.getAndroidId(requireContext())))
                                    commonViewModel.getCandidateDetailsAPI(CandidateReq(BuildConfig.VERSION_NAME,userPreferences.getUseID()))

                                    if (isSeccInfoVisible && seccStatus.contains("0")) {

                                        isSeccInfoVisible = false
                                        binding.expandSecc.visible()
                                        binding.viewSeccc.visible()


                                    } else {
                                        isSeccInfoVisible = true
                                        binding.expandSecc.gone()
                                        binding.viewSeccc.gone()
                                    }

                                }
                                301 -> {
                                    showSnackBar("Please Update from PlayStore")
                                }
                                else -> {
                                    showSnackBar("Something went wrong")
                                }
                            }
                        } ?: showSnackBar("Internal Server Error")
                    }
                }

            }
        }
    }

    private fun collectInsertEducationalResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.insertEducationAPI) {
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
                        it.data?.let { insertPersResponse ->
                            when (insertPersResponse.responseCode) {
                                200 -> {

                                    showSnackBar(insertPersResponse.responseMsg)
                                    commonViewModel.getCandidateDetailsAPI(CandidateReq(BuildConfig.VERSION_NAME,userPreferences.getUseID()))

                                    commonViewModel.getSecctionAndPerAPI(SectionAndPerReq(BuildConfig.VERSION_NAME,userPreferences.getUseID(),AppUtil.getAndroidId(requireContext())))
                                    if (isEducationalInfoVisible && educationalStatus.contains("0")) {
                                        isEducationalInfoVisible = false
                                        binding.expandEducational.visible()
                                        binding.viewEducational.visible()
                                    } else {
                                        isEducationalInfoVisible = true
                                        binding.expandEducational.gone()
                                        binding.viewEducational.gone()
                                    }

                                }
                                301 -> {
                                    showSnackBar("Please Update from PlayStore")
                                }
                                else -> {
                                    showSnackBar("Something went wrong")
                                }
                            }
                        } ?: showSnackBar("Internal Server Error")
                    }
                }
            }
        }
    }

    private fun collectInsertEmployementResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.insertEmploymentAPI) {
                when (it) {
                    is Resource.Loading -> showProgressBar()
                    is Resource.Error -> {
                        hideProgressBar()
                    //    it.error?.let { it1 -> toastShort(it1.message) }
                    }

                    is Resource.Success -> {
                        hideProgressBar()
                        it.data?.let { insertPersResponse ->
                            when (insertPersResponse.responseCode) {
                                200 -> {

                                    showSnackBar(insertPersResponse.responseMsg)
                                    commonViewModel.getSecctionAndPerAPI(SectionAndPerReq(BuildConfig.VERSION_NAME,userPreferences.getUseID(),AppUtil.getAndroidId(requireContext())))
                                    commonViewModel.getCandidateDetailsAPI(CandidateReq(BuildConfig.VERSION_NAME,userPreferences.getUseID()))

                                    if (isEmploymentInfoVisible && employmentStatus.contains("0")) {
                                        isEmploymentInfoVisible = false
                                        binding.expandEmployment.visible()
                                        binding.viewEmployment.visible()
                                    } else {
                                        isEmploymentInfoVisible = true
                                        binding.expandEmployment.gone()
                                        binding.viewEmployment.gone()
                                    }

                                }
                                301 -> {
                                    showSnackBar("Please Update from PlayStore")
                                }
                                else -> {
                                    showSnackBar("Something went wrong")
                                }
                            }
                        } ?: showSnackBar("Internal Server Error")
                    }

                    else -> {}
                }
            }
        }
    }

    private fun collectInsertTrainingResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.insertTrainingAPI) {
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
                        it.data?.let { insertPersResponse ->
                            when (insertPersResponse.responseCode) {
                                200 -> {

                                    showSnackBar(insertPersResponse.responseMsg)
                                    commonViewModel.getCandidateDetailsAPI(CandidateReq(BuildConfig.VERSION_NAME,userPreferences.getUseID()))

                                    commonViewModel.getSecctionAndPerAPI(SectionAndPerReq(BuildConfig.VERSION_NAME,userPreferences.getUseID(),AppUtil.getAndroidId(requireContext())))
                                    if (isTrainingInfoVisible && trainingStatus.contains("1")) {
                                        isTrainingInfoVisible = false
                                        binding.expandTraining.visible()
                                        binding.viewTraining.visible()
                                    } else {
                                        isTrainingInfoVisible = true
                                        binding.expandTraining.gone()
                                        binding.viewTraining.gone()
                                    }

                                }
                                301 -> {
                                    showSnackBar("Please Update from PlayStore")
                                }
                                else -> {
                                    showSnackBar("Something went wrong")
                                }
                            }
                        } ?: showSnackBar("Internal Server Error")
                    }
                }
            }
        }
    }

    private fun collectInsertBankingResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.insertBankingAPI) {
                when (it) {
                    is Resource.Loading -> showProgressBar()
                    is Resource.Error -> {
                        hideProgressBar()
                        it.error?.let { baseErrorResponse ->
                         //   toastShort(baseErrorResponse.message)
                        }
                    }

                    is Resource.Success -> {
                        hideProgressBar()
                        it.data?.let { insertPersResponse ->
                            when (insertPersResponse.responseCode) {
                                200 -> {

                                    showSnackBar(insertPersResponse.responseMsg)
                                    commonViewModel.getCandidateDetailsAPI(CandidateReq(BuildConfig.VERSION_NAME,userPreferences.getUseID()))

                                    commonViewModel.getSecctionAndPerAPI(SectionAndPerReq(BuildConfig.VERSION_NAME,userPreferences.getUseID(),AppUtil.getAndroidId(requireContext())))
                                    if (isBankingInfoVisible && bankingStatus.contains("0")) {
                                        isBankingInfoVisible = false
                                        binding.expandBanking.visible()
                                        binding.viewBanking.visible()
                                    } else {
                                        isBankingInfoVisible = true
                                        binding.expandBanking.gone()
                                        binding.viewBanking.gone()
                                    }

                                }
                                301 -> {
                                    showSnackBar("Please Update from PlayStore")
                                }
                                else -> {
                                    showSnackBar("Something went wrong")
                                }
                            }
                        } ?: showSnackBar("Internal Server Error")
                    }
                }
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
                                    statePer.add(x.stateName)
                                    stateCode.add(x.stateCode) // Replace with actual field
                                    stateLgdCode.add(x.lgdStateCode) // Replace with actual field
                                }

                           //     stateAdapter.notifyDataSetChanged()
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

/*
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
                                districtPer.clear()
                                districtCode.clear()
                                districtLgdCode.clear()

                                for (x in districtList) {
                                    district.add(x.districtName)
                                    districtPer.add(x.districtName)
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
*/

    private suspend fun collectDistrictResponse(isPermanent: Boolean) {
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
                            val districtNames = getDistrictResponse.districtList.map { it.districtName }
                            val districtCodes = getDistrictResponse.districtList.map { it.districtCode }
                            val districtLgdCodes = getDistrictResponse.districtList.map { it.lgdDistrictCode }

                            if (isPermanent) {
                                district.clear()
                                district.addAll(districtNames)
                                Log.d("DropdownDebug", "✅ Permanent District List: $district")
                            } else {
                                districtPer.clear()
                                districtPer.addAll(districtNames)
                                Log.d("DropdownDebug", "✅ Present District List: $districtPer")
                            }

                            districtCode.clear()
                            districtCode.addAll(districtCodes)
                            districtLgdCode.clear()
                            districtLgdCode.addAll(districtLgdCodes)

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


    private fun collectAadharDetailsResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getAadhaarList) {
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
                        it.data?.let { getAadharDetailsRes ->
                            if (getAadharDetailsRes.responseCode == 200) {
                                userAadhaarDetailsList = getAadharDetailsRes.wrappedList


                                for (x in userAadhaarDetailsList) {

                              val encryptedUserName = AESCryptography.decryptIntoString(x.userName,
                                        AppConstant.Constants.ENCRYPT_KEY,
                                        AppConstant.Constants.ENCRYPT_IV_KEY)


                                    val encryptedGender = AESCryptography.decryptIntoString(x.gender,
                                        AppConstant.Constants.ENCRYPT_KEY,
                                        AppConstant.Constants.ENCRYPT_IV_KEY)



                                    val encryptedMobileNo = AESCryptography.decryptIntoString(x.mobileNo,
                                        AppConstant.Constants.ENCRYPT_KEY,
                                        AppConstant.Constants.ENCRYPT_IV_KEY)

                                    val encryptedDateOfBirth = AESCryptography.decryptIntoString(x.dateOfBirth,
                                        AppConstant.Constants.ENCRYPT_KEY,
                                        AppConstant.Constants.ENCRYPT_IV_KEY)

                                    val encryptedComAddress = AESCryptography.decryptIntoString(x.comAddress,
                                        AppConstant.Constants.ENCRYPT_KEY,
                                        AppConstant.Constants.ENCRYPT_IV_KEY)




                                    selectedStateItem=x.regState
                                    binding.TvSpinnerStateName.setText(x.regState)



                                    selectedStateCodeItem=x.regStateCode
                                    commonViewModel.getDistrictListApi(x.regStateCode)
                                    collectDistrictResponse(true)
                                    binding.profileView.tvAadhaarName.setText(encryptedUserName)
                                    binding.profileView.tvAaadharMobile.setText(encryptedMobileNo)
                                    binding.profileView.tvAaadharGender.setText(encryptedGender)
                                    binding.profileView.tvAaadharDob.setText(encryptedDateOfBirth)
                                    binding.profileView.tvAaadharAddress.setText(encryptedComAddress)

                                    val bytes: ByteArray =
                                        Base64.decode(x.imagePath, Base64.DEFAULT)
                                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                                    binding.profileView.circleImageView.setImageBitmap(bitmap)

                                }
                            } else if (getAadharDetailsRes.responseCode == 301) {
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
                                blockPer.clear()
                                blockCode.clear()
                                blockLgdCode.clear()

                                for (x in blockList) {
                                    block.add(x.blockName)
                                    blockPer.add(x.blockName)
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
                                gpPer.clear()
                                gpCode.clear()
                                gpLgdCode.clear()

                                for (x in gpList) {
                                    gp.add(x.gpName)
                                    gpPer.add(x.gpName)
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
                                villagePer.clear()
                                villageCode.clear()
                                villageLgdCode.clear()

                                for (x in villageList) {
                                    village.add(x.villageName)
                                    villagePer.add(x.villageName)
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

    @SuppressLint("SuspiciousIndentation")
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
                                val courseList = getTechEduRes.courseList
                                courseesName.clear()
                                courseesCode.clear()
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
                                val courseList = getTechEduRes.domainList
                                courseesDomainName.clear()
                                courseesDomainCode.clear()

                                for (x in courseList) {
                                    courseesDomainName.add(x.domainName)
                                    courseesDomainCode.add(x.domainCode)
                                }
                                TechEduDomaiAdapter.notifyDataSetChanged()
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


    private fun collectWhereHaveUHeardResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getWhereHaveYouHeard) {
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
                        it.data?.let { getWhereHeard ->
                            if (getWhereHeard.responseCode == 200) {
                                val heardList = getWhereHeard.surveyList

                                heardName.clear()
                                heardCode.clear()
                                for (x in heardList) {

                                    heardName.add(x.mediumName)
                                    heardCode.add(x.surveyCode)
                                }
                                HeardAdapter.notifyDataSetChanged()
                            } else if (getWhereHeard.responseCode == 301) {
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


    private fun collectDpChangeResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getImageChangeAPI) {
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
                        it.data?.let { getImageChangeAPI ->
                            if (getImageChangeAPI.responseCode == 200) {
                                showSnackBar(getImageChangeAPI.responseMsg)

                                commonViewModel.getAadhaarListAPI(
                                    AdharDetailsReq(
                                        BuildConfig.VERSION_NAME,
                                        AppUtil.getAndroidId(requireContext()),
                                        userPreferences.getUseID()
                                    )
                                )

                            } else if (getImageChangeAPI.responseCode == 301) {
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


    private fun collectSeccListResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getSeccListAPI) {
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
                        it.data?.let { getSeccList ->
                            if (getSeccList.responseCode == 200) {
                                val heardList = getSeccList.wrappedList

                                seccName.clear()
                                fatherName.clear()
                                ahlTinNo.clear()
                                for (x in heardList) {

                                    seccName.add(x.seccName)
                                    fatherName.add(x.fatherName)
                                    ahlTinNo.add(x.ahltin)
                                }

                                seccAdapter.submitList(it.data.wrappedList)


                            } else if (getSeccList.responseCode == 301) {
                                showSnackBar("Please Update from PlayStore")
                            }
                            else if (getSeccList.responseCode == 302) {
                                showSnackBar(getSeccList.responseMsg)
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

    private fun collectSetionAndPerResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getSecctionAndPerAPI) {
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
                        it.data?.let { getSecctionAndPerAPI ->
                            if (getSecctionAndPerAPI.responseCode == 200) {
                                val percentageList = getSecctionAndPerAPI.wrappedList

                                for (x in percentageList) {

                                    personalStatus= x.personalStatus.toString()
                                    educationalStatus= x.educationalStatus.toString()
                                    trainingStatus= x.trainingStatus.toString()
                                    seccStatus= x.seccStatus .toString()
                                    addressStatus= x.addressStatus.toString()
                                    employmentStatus= x.employmentStatus.toString()
                                    bankingStatus= x.bankingStatus.toString()
                                    totalPercentange= x.totalPercentage

                                }
                                // set dynamic meter


                                binding.profileView.ivProgress.setImageBitmap(
                                    createHalfCircleProgressBitmap(
                                        300, 300, totalPercentange,
                                        ContextCompat.getColor(requireContext(), R.color.color_dark_green),
                                        ContextCompat.getColor(requireContext(), R.color.color_green), 35f, 20f,
                                        ContextCompat.getColor(requireContext(), R.color.black),
                                        ContextCompat.getColor(requireContext(), R.color.color_background)
                                    )
                                )

                                // set section completed drawable

                                if (personalStatus.contains("1")){

                                    binding.tvPersonal.setDrawable(
                                        null, null,
                                        AppCompatResources.getDrawable(requireContext(), R.drawable.ic_verified), null
                                    )

                                }
                                if (addressStatus.contains("1")){

                                    binding.tv.setDrawable(
                                        null, null,
                                        AppCompatResources.getDrawable(requireContext(), R.drawable.ic_verified), null
                                    )

                                }
                                if (seccStatus.contains("1")){

                                    binding.tvSecccon.setDrawable(
                                        null, null,
                                        AppCompatResources.getDrawable(requireContext(), R.drawable.ic_verified), null
                                    )

                                }
                                if (educationalStatus.contains("1")){

                                    binding.tvEducational.setDrawable(
                                        null, null,
                                        AppCompatResources.getDrawable(requireContext(), R.drawable.ic_verified), null
                                    )

                                }

                                if (employmentStatus.contains("1")){

                                    binding.tvEmployment.setDrawable(
                                        null, null,
                                        AppCompatResources.getDrawable(requireContext(), R.drawable.ic_verified), null
                                    )

                                }

                                if (trainingStatus.contains("1")){

                                    binding.tvTraining.setDrawable(
                                        null, null,
                                        AppCompatResources.getDrawable(requireContext(), R.drawable.ic_verified), null
                                    )

                                }

                                if (bankingStatus.contains("1")){

                                    binding.tvBanking.setDrawable(
                                        null, null,
                                        AppCompatResources.getDrawable(requireContext(), R.drawable.ic_verified), null
                                    )

                                }


                            } else if (getSecctionAndPerAPI.responseCode == 301) {
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

    private fun collectNregaValidateResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.nRegaValidate) {
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
                                nregaValidateStatus = getValidateStatus.body()?.Status ?: ""
                               showSnackBar(getValidateStatus.body()?.Remarks.toString())
                              nregaJobCard=  binding.etNregaValidate.text.toString()



                            }
                        } ?: showSnackBar("Internal Server Error")
                    }
                }
            }
        }
    }


    private fun collectBankResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getBankDetailsAPI) {
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
                        it.data?.let { getBankList ->
                            if (getBankList.responseCode == 200) {
                                val bankList = getBankList.bankDetailsList

                                for (x in bankList) {



                                    val encryptedbankCode = AESCryptography.decryptIntoString(x.bankCode,
                                        AppConstant.Constants.ENCRYPT_KEY,
                                        AppConstant.Constants.ENCRYPT_IV_KEY)

                                    val encryptedbankName = AESCryptography.decryptIntoString(x.bankName,
                                        AppConstant.Constants.ENCRYPT_KEY,
                                        AppConstant.Constants.ENCRYPT_IV_KEY)

                                    val encryptedbranchCode = AESCryptography.decryptIntoString(x.branchCode,
                                        AppConstant.Constants.ENCRYPT_KEY,
                                        AppConstant.Constants.ENCRYPT_IV_KEY)

                                    val encryptedbranchName = AESCryptography.decryptIntoString(x.branchName,
                                        AppConstant.Constants.ENCRYPT_KEY,
                                        AppConstant.Constants.ENCRYPT_IV_KEY)

                                    val encryptedaccLength = AESCryptography.decryptIntoString(x.accLength,
                                        AppConstant.Constants.ENCRYPT_KEY,
                                        AppConstant.Constants.ENCRYPT_IV_KEY)

                                    bankCode=  encryptedbankCode
                                    bankName1=    encryptedbankName
                                    branchCode=  encryptedbranchCode
                                    branchName=   encryptedbranchName
                                    accLenghth=  encryptedaccLength

                                }

                                binding.btnBnakingSubmit.visible()

                                binding.etBankName.setText(bankName1)
                                binding.etBranchName.setText(branchName)
                                binding.etBankAcNo.filters = arrayOf(InputFilter.LengthFilter(accLenghth.toInt()))



                            } else if (getBankList.responseCode == 301) {
                                showSnackBar(getBankList.responseMsg)
                            }
                            else if (getBankList.responseCode == 302) {
                                showSnackBar(getBankList.responseMsg)
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

    @SuppressLint("SuspiciousIndentation")
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

    private fun collectTradeResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getTradeListAPI) {
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
                        it.data?.let { getTradeList ->
                            if (getTradeList.responseCode == 200) {
                                val   sectorList1 = getTradeList.wrappedList

                                tradeName.clear()
                                for (x in sectorList1) {
                                    tradeName.add(x.trade)

                                }



                            } else if (getTradeList.responseCode == 301) {
                                getTradeList.responseMsg?.let { it1 -> showSnackBar(it1) }
                            }
                            else if (getTradeList.responseCode == 302) {
                                getTradeList.responseMsg?.let { it1 -> showSnackBar(it1) }
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



    private fun setDropdownValue(
        autoCompleteTextView: AutoCompleteTextView,
        value: String,
        dataList: List<String>
    ) {
        Log.d("DropdownDebug", "🔍 Dropdown Data for ${autoCompleteTextView.id}: $dataList")
        Log.d("DropdownDebug", "🎯 Selected Value: $value")

        autoCompleteTextView.post {
            val adapter = ArrayAdapter(autoCompleteTextView.context, android.R.layout.simple_dropdown_item_1line, dataList)
            autoCompleteTextView.setAdapter(null) // Force refresh
            autoCompleteTextView.setAdapter(adapter) // Set updated data

            if (dataList.contains(value)) {
                autoCompleteTextView.setText(value, false)
            }
        }
    }





    private fun checkAndRequestPermissionsForPurpose(purpose: String) {
        currentRequestPurpose = purpose

        when {
            // Android 13+ (API level 33 and above)
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
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
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
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
            "PROFILE_PIC" -> openGalleryForDPId()
            "NREGA_ID" -> openGalleryForNregaId()
        }
    }


    private fun openGalleryForNregaId() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_PICK_NREGA)
    }

    private fun openGalleryForVoterId() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_PICK_VOTER_ID)
    }

    private fun openGalleryForDPId() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_PICK_PROFILE_PIC)
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
            Toast.makeText(
                requireContext(),
                "Permission denied for $currentRequestPurpose",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            when (requestCode) {
                REQUEST_PICK_VOTER_ID -> {
                    // Handle voter ID image

                    var fileName = selectedImageUri?.let { getFileName(requireContext(), it) }
                    binding.voterimageText.text = fileName

                            voterIdImage=  compressAndConvertImageToBase64(selectedImageUri)



                }
                REQUEST_PICK_NREGA -> {
                    // Handle voter ID image

                    var fileName = selectedImageUri?.let { getFileName(requireContext(), it) }
                    binding.tvNregaJob.text = fileName

                    nregaImageJobCard=  compressAndConvertImageToBase64(selectedImageUri)



                }

                REQUEST_PICK_PROFILE_PIC -> {
                // Handle voter ID image

                    profilePicIdImage=  compressAndConvertImageToBase64(selectedImageUri)

                    commonViewModel.getImageChangeAPI(ImageChangeReq(BuildConfig.VERSION_NAME,profilePicIdImage,userPreferences.getUseID()))

                    collectDpChangeResponse()







                }

                REQUEST_PICK_DRIVING_LICENSE -> {
                    var fileName = selectedImageUri?.let { getFileName(requireContext(), it) }
                    binding.drivingLicenceimageText.text = fileName

                    drivingLicenceImage=  compressAndConvertImageToBase64(selectedImageUri)



                }

                REQUEST_PICK_CATEGORY -> {
                    // Handle voter ID image


                    var fileName = selectedImageUri?.let { getFileName(requireContext(), it) }
                    binding.categoryCertimageText.text = fileName

                    categoryCertiImage=  compressAndConvertImageToBase64(selectedImageUri)



                }

                REQUEST_PICK_MINORITY -> {
                    // Handle driving license image

                    var fileName = selectedImageUri?.let { getFileName(requireContext(), it) }
                    binding.minorityimageText.text = fileName


                    minorityImage=  compressAndConvertImageToBase64(selectedImageUri)

                }


                REQUEST_PICK_PWD -> {
                    // Handle driving license image


                    val fileName = selectedImageUri?.let { getFileName(requireContext(), it) }
                    binding.pwdImageText.text = fileName

                    pwdImage=  compressAndConvertImageToBase64(selectedImageUri)

                }


                REQUEST_PICK_ANTOYADA -> {
                    // Handle driving license image

                    val fileName = selectedImageUri?.let { getFileName(requireContext(), it) }
                    binding.antyodayamageText.text = fileName


                    antoyadaImage=  compressAndConvertImageToBase64(selectedImageUri)

                }

                REQUEST_PICK_RSBY -> {


                    val fileName = selectedImageUri?.let { getFileName(requireContext(), it) }
                    binding.rsbyimageText.text = fileName


                    rsbyImage=  compressAndConvertImageToBase64(selectedImageUri)

                }


                REQUEST_PICK_RESIDENCE -> {


                    val fileName = selectedImageUri?.let { getFileName(requireContext(), it) }
                    binding.residentalimageText.text = fileName


                    residenceImage=  compressAndConvertImageToBase64(selectedImageUri)

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

    private fun handleSearchQuery(query: String) {

        commonViewModel.getSeccListAPI(
            SeccReq(
                BuildConfig.VERSION_NAME,
                selectedbSeccVillageLgdCodeItem
                ,
                query,
                userPreferences.getUseID(),
                AppUtil.getAndroidId(requireContext())
            )
        )


    }



    // This map will hold the checkbox states, ensuring persistent selection across dialog opens
    private val selectedLanguageStates = mutableMapOf<String, MutableMap<String, Boolean>>()

    private fun showStyledLanguageSelectionDialog() {
        // Create a dialog
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialoge_language)

        // Get references to UI components
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerViewLanguages)
        val btnSave = dialog.findViewById<Button>(R.id.btnSave)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)

        // Prepare the RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val languages = listOf(
            Language("Gujarati"),
            Language("English"),
            Language("Assamese"),
            Language("Bengali"),
            Language("Hindi"),
            Language("Kannada"),
            Language("Kashmiri"),
            Language("Maithili"),
            Language("Malayalam"),
            Language("Marathi"),
            Language("Meitei"),
            Language("Odia"),
            Language("Punjabi"),
            Language("Sanskrit"),
            Language("Tamil"),
            Language("Telugu"),
            Language("Urdu")
        )

        // Create adapter with the language list and the map to store checkbox states
        val adapter = LanguageRead(languages, selectedLanguageStates)
        recyclerView.adapter = adapter

        // Handle Save button click
        btnSave.setOnClickListener {
            // Prepare the result as a string for display
            binding.tvLanguages.text = ""
            result.clear()
            for (language in languages) {
                // Get the checkbox states for each language
                val languageState = selectedLanguageStates[language.name] ?: mutableMapOf()
                if (languageState["canRead"] == true || languageState["canWrite"] == true || languageState["canSpeak"] == true) {
                    // Only append the properties that are true
                    val languageResult = StringBuilder("${language.name}: ")
                    if (languageState["canRead"] == true) languageResult.append("Read ")
                    if (languageState["canWrite"] == true) languageResult.append("Write ")
                    if (languageState["canSpeak"] == true) languageResult.append("Speak ")

                    // Trim trailing spaces and append to the result
                    result.append(languageResult.trim().toString()).append("\n")
                }
            }

            // Show the result in the TextView
            binding.tvLanguages.text = result

            // Display the selected values (example: Toast or Log)
            if (result.isNotEmpty()) {
                Toast.makeText(requireContext(), result.toString(), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "No languages selected", Toast.LENGTH_SHORT).show()
            }

            dialog.dismiss()
        }

        // Handle Cancel button click
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        // Show the dialog
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

    private fun compressAndConvertImageToBase64(uri: Uri?): String {
        return try {
            // Load the image as a Bitmap
            val inputStream = uri?.let { requireContext().contentResolver.openInputStream(it) }
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            // Compress the image
            val outputStream = ByteArrayOutputStream()
            originalBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream) // 50% quality
            val compressedBytes = outputStream.toByteArray()

            // Convert to Base64 without white spaces
            Base64.encodeToString(compressedBytes, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }



    fun showYesNoDialog(context: Context, title: String, message: String, onYesClicked: () -> Unit, onNoClicked: () -> Unit) {
        // Create the AlertDialog.Builder
        val builder = AlertDialog.Builder(context)

        // Set the title and message
        builder.setTitle(title)
        builder.setMessage(message)

        // Set the positive button (Yes)
        builder.setPositiveButton("Yes") { dialog, _ ->
            onYesClicked() // Execute the Yes action
            dialog.dismiss()
        }

        // Set the negative button (No)
        builder.setNegativeButton("No") { dialog, _ ->
            onNoClicked() // Execute the No action
            dialog.dismiss()
        }

        // Optionally set the dialog to be cancelable (can be canceled by clicking outside)
        builder.setCancelable(true)

        // Create and show the dialog
        val dialog = builder.create()
        dialog.show()
    }



    object HashUtils {
        fun sha512(input: String): String {
            val bytes = MessageDigest.getInstance("SHA-512").digest(input.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }
    }



    private fun collectCandidateDetailsResponse() {
        lifecycleScope.launch {
            try {
                collectLatestLifecycleFlow(commonViewModel.getCandidateDetailsAPI) {
                    when (it) {
                        is Resource.Loading -> showProgressBar()
                        is Resource.Error -> {
                            hideProgressBar()
                            it.error?.let { baseErrorResponse ->
                                showSnackBar(baseErrorResponse.message ?: "Unknown error occurred")
                            } ?: showSnackBar("Unknown error occurred")
                        }

                        is Resource.Success -> {
                            hideProgressBar()
                            try {
                                it.data?.let { getCandidateDetailsAPI ->
                                    if (getCandidateDetailsAPI.responseCode == 200) {
                                        // Extract details from the API response
                                        userCandidatePersonalDetailsList = getCandidateDetailsAPI.personalList
                                        userCandidateAddressDetailsList = getCandidateDetailsAPI.addressList
                                        userCandidateSeccDetailsList = getCandidateDetailsAPI.seccList
                                        userCandidateEducationalDetailsList = getCandidateDetailsAPI.educationalList
                                        userCandidateEmploymentDetailsList = getCandidateDetailsAPI.employementList
                                        userCandidateTrainingDetailsList = getCandidateDetailsAPI.trainingList
                                        userCandidateBankDetailsList = getCandidateDetailsAPI.bankList

                                        for (x in userCandidatePersonalDetailsList) {
                                            userCandidatePersonalDetailsList2 = x.personaldetails

                                        }
                                        for (x in userCandidateAddressDetailsList ){

                                            userCandidateAddressDetailsList2=   x.addressDetails

                                        }

                                    } else if (getCandidateDetailsAPI.responseCode == 301) {
                                        showSnackBar("Please Update from PlayStore")
                                    } else {
                                        showSnackBar("Something went wrong")
                                    }
                                } ?: showSnackBar("Internal Server Error")
                            } catch (e: Exception) {
                                showSnackBar("Error processing response: ${e.message}")
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                hideProgressBar()
                showSnackBar("Unexpected error occurred: ${e.message}")
            }
        }
    }

    // Helper function to handle status UI
    private fun handleStatus(viewYes: View, viewNo: View, status: String) {
        if (status.contains("Yes", ignoreCase = true)) {
            viewYes.setBackgroundResource(R.drawable.card_background_selected)
            viewNo.setBackgroundResource(R.drawable.card_background)
        } else if (status.contains("No", ignoreCase = true)) {
            viewYes.setBackgroundResource(R.drawable.card_background)
            viewNo.setBackgroundResource(R.drawable.card_background_selected)
        }
    }

   /* fun getValueFromSecondList(list1: List<String>, list2: List<String>, searchString: String): String? {
        // Ensure the lists are the same size to avoid IndexOutOfBoundsException
        if (list1.size != list2.size) {
            throw IllegalArgumentException("Both lists must have the same size.")
        }

        // Find the index of the search string in the first list
        val index = list1.indexOf(searchString)

        // If the string exists in list1, return the corresponding value from list2
        return if (index != -1) {
            list2[index]
        } else {
            null // Return null if the string is not found in list1
        }
    }*/




}

