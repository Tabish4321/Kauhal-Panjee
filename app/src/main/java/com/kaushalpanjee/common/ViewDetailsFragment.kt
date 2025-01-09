package com.kaushalpanjee.common

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.R
import com.kaushalpanjee.common.model.request.AdharDetailsReq
import com.kaushalpanjee.common.model.request.CandidateReq
import com.kaushalpanjee.common.model.request.SectionAndPerReq
import com.kaushalpanjee.common.model.response.Address
import com.kaushalpanjee.common.model.response.AddressDetail
import com.kaushalpanjee.common.model.response.Bank
import com.kaushalpanjee.common.model.response.CandidateDetails
import com.kaushalpanjee.common.model.response.Educational
import com.kaushalpanjee.common.model.response.Employment
import com.kaushalpanjee.common.model.response.Personal
import com.kaushalpanjee.common.model.response.PersonalDetail
import com.kaushalpanjee.common.model.response.Secc
import com.kaushalpanjee.common.model.response.Training
import com.kaushalpanjee.common.model.response.UserDetails
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.core.util.UserPreferences
import com.kaushalpanjee.core.util.createHalfCircleProgressBitmap
import com.kaushalpanjee.core.util.gone
import com.kaushalpanjee.core.util.setDrawable
import com.kaushalpanjee.core.util.visible
import com.kaushalpanjee.databinding.FragmentViewDetailsBinding
import kotlinx.coroutines.launch

class ViewDetailsFragment : BaseFragment<FragmentViewDetailsBinding>(FragmentViewDetailsBinding::inflate) {


    private val commonViewModel: CommonViewModel by activityViewModels()


    //Boolean Values
    private var isPersonalVisible = false
    private var isAddressVisible = false
    private var isEducationalInfoVisible = false
    private var isEmploymentInfoVisible = false
    private var isTrainingInfoVisible = false
    private var isBankingInfoVisible = false
    private var isSeccInfoVisible = false
    private var userAadhaarDetailsList: List<UserDetails> = mutableListOf()
    private var userCandidatePersonalDetailsList: List<Personal> = mutableListOf()
    private var userCandidatePersonalDetailsList2: List<PersonalDetail> = mutableListOf()
    private var userCandidateAddressDetailsList: List<Address> = mutableListOf()
    private var userCandidateAddressDetailsList2: List<AddressDetail> = mutableListOf()
    private var userCandidateSeccDetailsList: List<Secc> = mutableListOf()
    private var userCandidateEducationalDetailsList: List<Educational> = mutableListOf()
    private var userCandidateEmploymentDetailsList: List<Employment> = mutableListOf()
    private var userCandidateTrainingDetailsList: List<Training> = mutableListOf()
    private var userCandidateBankDetailsList: List<Bank> = mutableListOf()

    private var personalStatus = ""
    private var educationalStatus = ""
    private var trainingStatus = ""
    private var seccStatus = ""
    private var addressStatus = ""
    private var employmentStatus = ""
    private var bankingStatus = ""
    private var totalPercentange = 0.0f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferences = UserPreferences(requireContext())

        init()

        collectAadharDetailsResponse()
        collectSetionAndPerResponse()
        collectCandidateDetailsResponse()


    }

    private fun init()
    {
        listeners()

        commonViewModel.getSecctionAndPerAPI(
            SectionAndPerReq(BuildConfig.VERSION_NAME,userPreferences.getUseID(),
                AppUtil.getAndroidId(requireContext()))
        )
        commonViewModel.getAadhaarListAPI(
            AdharDetailsReq(
                BuildConfig.VERSION_NAME,
                AppUtil.getAndroidId(requireContext()),
                userPreferences.getUseID()
            )
        )

        commonViewModel.getCandidateDetailsAPI(CandidateReq(BuildConfig.VERSION_NAME,userPreferences.getUseID()))
    }

    private fun listeners() {



        binding.llTopPersonal.setOnClickListener {

            if (isPersonalVisible){
                isPersonalVisible = false
                binding.personalExpand.visible()
                binding.viewSecc.visible()

            } else {
                isPersonalVisible = true
                binding.personalExpand.gone()
                binding.viewSecc.gone()
            }
        }

        binding.llTopSecc.setOnClickListener {
            if (isSeccInfoVisible) {

                isSeccInfoVisible = false
                binding.expandSecc.visible()
                binding.viewSeccc.visible()



            } else {
                isSeccInfoVisible = true
                binding.expandSecc.gone()
                binding.viewSeccc.gone()
            }

        }

        binding.llTopAddress.setOnClickListener {

            if (isAddressVisible) {
                isAddressVisible = false
                binding.expandAddress.visible()
                binding.viewAddress.visible()
            } else {
                isAddressVisible = true
                binding.expandAddress.gone()
                binding.viewAddress.gone()
            }
        }

        binding.llTopEducational.setOnClickListener {


            if (isEducationalInfoVisible) {
                isEducationalInfoVisible = false
                binding.expandEducational.visible()
                binding.viewEducational.visible()
            } else {
                isEducationalInfoVisible = true
                binding.expandEducational.gone()
                binding.viewEducational.gone()
            }
        }

        binding.llTopEmployment.setOnClickListener {

            if (isEmploymentInfoVisible) {
                isEmploymentInfoVisible = false
                binding.expandEmployment.visible()
                binding.viewEmployment.visible()
            } else {
                isEmploymentInfoVisible = true
                binding.expandEmployment.gone()
                binding.viewEmployment.gone()
            }
        }

        binding.llTopBanking.setOnClickListener {

            if (isBankingInfoVisible ) {
                isBankingInfoVisible = false
                binding.expandBanking.visible()
                binding.viewBanking.visible()
            } else {
                isBankingInfoVisible = true
                binding.expandBanking.gone()
                binding.viewBanking.gone()
            }
        }

        binding.llTopTraining.setOnClickListener {

            if (isTrainingInfoVisible ) {
                isTrainingInfoVisible = false
                binding.expandTraining.visible()
                binding.viewTraining.visible()
            } else {
                isTrainingInfoVisible = true
                binding.expandTraining.gone()
                binding.viewTraining.gone()
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

                                    binding.tvAadhaarName.setText(x.userName)
                                    binding.tvAaadharMobile.setText(x.mobileNo)
                                    binding.tvAaadharGender.setText(x.gender)
                                    binding.tvAaadharDob.setText(x.dateOfBirth)
                                    binding.tvAaadharAddress.setText(x.comAddress)

                                    val bytes: ByteArray =
                                        Base64.decode(x.imagePath, Base64.DEFAULT)
                                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                                    binding.circleImageView.setImageBitmap(bitmap)

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

                                binding.ivProgress.setImageBitmap(
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

                                            try {
                                                // Decode and display images
                                                val bitmapCategory = decodeBase64Image(x.categoryCertPath)
                                                binding.categoryCertificateUpload.setImageBitmap(bitmapCategory)

                                               /* val bytes: ByteArray =
                                                    Base64.decode(x.categoryCertPath, Base64.DEFAULT)
                                                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                                                binding.categoryCertificateUpload.setImageBitmap(bitmap)
*/

                                                val bitmapMinority = decodeBase64Image(x.minorityCertPath)
                                                binding.minorityImageUpload.setImageBitmap(bitmapMinority)

                                                val bitmapDisability = decodeBase64Image(x.disablityCertPath)
                                                binding.pwdImageUpload.setImageBitmap(bitmapDisability)

                                                val bitmapDl = decodeBase64Image(x.dlImagePath)
                                                binding.pwdImageUpload.setImageBitmap(bitmapDl)

                                                val bitmapRsby = decodeBase64Image(x.rsbyCardPath)
                                                binding.rsbyUpload.setImageBitmap(bitmapRsby)

                                                val bitmapVoterId = decodeBase64Image(x.VoterImagePath)
                                                binding.rsbyUpload.setImageBitmap(bitmapVoterId)

                                                val bitmapRation = decodeBase64Image(x.rationCardPath)
                                                binding.rsbyUpload.setImageBitmap(bitmapRation)
                                            } catch (e: Exception) {
                                                showSnackBar("Error decoding image: ${e.message}")
                                            }
                                        }

                                        for (x in userCandidatePersonalDetailsList2) {
                                            try {
                                                // Populate the UI
                                                binding.etGName.setText(x.guardianName)
                                                binding.etGNumber.setText(x.guardianMobilNo)
                                                binding.etMotherName.setText(x.motherName)
                                                binding.etFIncome.setText(x.annualFamilyIncome)
                                                binding.etllVoterId.setText(x.voterId)
                                                binding.etdrivingId.setText(x.dlNo)
                                                binding.llCategory.setText(x.castCategory)
                                                binding.llMarital.setText(x.maritalStatus)
                                                binding.etShgValidate.setText(x.shgNo)

                                                val minorityStatus = x.isMinority
                                                val pwdStatus = x.isDisablity
                                                val nregaJobCardStatus = x.isNarega
                                                val shgStatus = x.isSHG
                                                val antyodayaStatus = x.antyodaya
                                                val rsbyStatus = x.isRSBY
                                                val pipStatus = x.isPIP

                                                // Set the UI based on conditions
                                                handleStatus(binding.optionMinorityYesSelect, binding.optionMinorityNoSelect, minorityStatus)
                                                handleStatus(binding.optionPwdYesSelect, binding.optionPwdNoSelect, pwdStatus)
                                                handleStatus(binding.optionNregaJobYesSelect, binding.optionNregaJobNoSelect, nregaJobCardStatus)
                                                handleStatus(binding.optionShgYesSelect, binding.optionShgNoSelect, shgStatus)
                                                handleStatus(binding.optionAntyodayaYesSelect, binding.optionAntyodayaNoSelect, antyodayaStatus)
                                                handleStatus(binding.optionllRsbyYesSelect, binding.optionllRsbyNoSelect, rsbyStatus)
                                                handleStatus(binding.optionPipYesSelect, binding.optionPipNoSelect, pipStatus)
                                            } catch (e: Exception) {
                                                showSnackBar("Error setting data: ${e.message}")
                                            }
                                        }

                                        for (x in userCandidateAddressDetailsList ){

                                            userCandidateAddressDetailsList2=   x.addressDetails

                                            try {
                                               /* val bitmapResidence = decodeBase64Image(x.residenceCertPath)
                                                binding.uploadResidenceImage.setImageBitmap(bitmapResidence)
*/
                                                val bytes: ByteArray =
                                                    Base64.decode(x.residenceCertPath, Base64.DEFAULT)
                                                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                                                binding.uploadResidenceImage.setImageBitmap(bitmap)
                                            } catch (e: Exception) {
                                                showSnackBar("Error decoding image: ${e.message}")
                                            }


                                        }


                                        for (x in userCandidateAddressDetailsList2){

                                            binding.statespinn.setText(x.presentStateName)
                                            binding.etDist.setText(x.presentDistrictName)
                                            binding.etBlock.setText(x.presentBlockName)
                                            binding.etGp.setText(x.presentGPName)
                                            binding.etVillage.setText(x.presentVillageName)
                                            binding.etAdressLine.setText(x.presentStreet1)
                                            binding.etAdressLine2.setText(x.presentStreet2)
                                            binding.etPinCode.setText(x.presentPinCode)
                                            val adreessStatus = x.isPresentAddressSame

                                            binding.etPrState.setText(x.permanentStateName)
                                            binding.etPrDist.setText(x.permanentDistrictName)
                                            binding.etPrBlock.setText(x.permanentBlockName)
                                            binding.etPrGp.setText(x.permanentGPName)
                                            binding.etPrVillage.setText(x.permanentVillageName)
                                            binding.etPresentAddressAdressLine.setText(x.permanentStreet1)
                                            binding.etPresentLine2.setText(x.permanentStreet2)
                                            binding.etPresentPinCode.setText(x.permanentPinCode)

                                            handleStatus(binding.optionllSamePermanentYesSelect, binding.optionSamePermanentNoSelect, adreessStatus)





                                        }

                                        for (x in userCandidateSeccDetailsList){

                                            binding.stateesecc.setText(x.seccStateName)
                                            binding.etSeccDistrict.setText(x.seccDistrictName)
                                            binding.etBloch.setText(x.seccBlockName)
                                            binding.etSeccgp.setText(x.seccGPName)
                                            binding.etSeccVillage.setText(x.seccVillageName)
                                            binding.etATINNo.setText(x.seccAHLTIN)



                                        }

                                        for (x in userCandidateEducationalDetailsList ){

                                            binding.ethighestEdu.setText(x.highesteducation)
                                            binding.tvClickYearOfPassing.setText(x.monthYearOfPassing)
                                            binding.tvLanguages.setText(x.language)
                                            binding.etTechEduc.setText(x.techQualification)
                                            binding.etTechDomain.setText(x.techDomain)
                                            binding.tvClickYearOfPassingTech.setText(x.passingTechYear)
                                        }

                                        for (x in userCandidateEmploymentDetailsList ){

                                            val currentluempo = x.isEmployeed
                                            val natureEmp = x.empNature

                                            if (natureEmp.contains("Self Employed")){
                                                binding.optionnatureOfEmplYesSelect.setBackgroundResource(R.drawable.card_background_selected)
                                                binding.optionnatureOfEmpldNoSelect.setBackgroundResource(R.drawable.card_background)
                                            }
                                            else if (natureEmp.contains("Salary")){

                                                binding.optionnatureOfEmpldNoSelect.setBackgroundResource(R.drawable.card_background_selected)
                                                binding.optionnatureOfEmplYesSelect.setBackgroundResource(R.drawable.card_background)
                                            }

                                            handleStatus(binding.optionrecievedAnyTrainingBeforeYesSelect, binding.optioRecievedAnyTrainingBeforeNoSelect, currentluempo)

                                            binding.etIntrestedIn.setText(x.intrestedIn)
                                            binding.etEmploPref.setText(x.empPreference)
                                            binding.etJobLoc.setText(x.preferJobLocation)
                                            binding.etCurrentEarning.setText(x.monthlyEarning)
                                            binding.etExpectationSalary.setText(x.expectedSalary)


                                        }

                                        for (x in userCandidateTrainingDetailsList){

                                            val recievedTrainingBeforeStatus = x.isPreTraining
                                            val heardStatus = x.hearedAboutScheme

                                            // Set the UI based on conditions
                                            handleStatus(binding.optionrecievedAnyTrainingBeforeYesSelect, binding.optioRecievedAnyTrainingBeforeNoSelect, recievedTrainingBeforeStatus)
                                            handleStatus(binding.optionHaveYouHeardYes, binding.optionHaveYouHeardNo, heardStatus)

                                            binding.etPrevComTra.setText(x.preCompTraining)
                                            binding.etWhereHaveHeard.setText(x.hearedFrom)
                                            binding.etSector.setText(x.sectorName)
                                            binding.etTrade.setText(x.trade)
                                        }


                                        for (x in userCandidateBankDetailsList){

                                            binding.etIfsc.setText(x.ifscCode)
                                            binding.etBankName.setText(x.bankName)
                                            binding.etBranchName.setText(x.bankBranchName)
                                            binding.etBankAcNo.setText(x.bankAccNumber)
                                            binding.etPanNumber.setText(x.panNo)
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
    private fun decodeBase64Image(base64String: String?): Bitmap? {
        if (base64String.isNullOrEmpty()) return null
        return try {
            val cleanedBase64String = base64String.substringAfter(",")
            val bytes = Base64.decode(cleanedBase64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } catch (e: IllegalArgumentException) {
            Log.e("DecodeError", "Invalid Base64 string: ${e.message}")
            null
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



}