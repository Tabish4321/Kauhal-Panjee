package com.kaushalpanjee.common

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.SystemClock
import android.util.Base64
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.R
import com.kaushalpanjee.common.model.UidaiKycRequest
import com.kaushalpanjee.common.model.UidaiResp
import com.kaushalpanjee.common.model.WrappedList
import com.kaushalpanjee.common.model.request.UserCreationReq
import com.kaushalpanjee.common.model.response.IntentModel
import com.kaushalpanjee.common.model.response.IntentResponse
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.AESCryptography
import com.kaushalpanjee.core.util.AppConstant
import com.kaushalpanjee.core.util.AppConstant.Constants.LANGUAGE
import com.kaushalpanjee.core.util.AppConstant.Constants.PRE_PRODUCTION_CODE
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.DownloadHelper
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.core.util.copyToClipboard
import com.kaushalpanjee.core.util.decodeBase64
import com.kaushalpanjee.core.util.gone
import com.kaushalpanjee.core.util.log
import com.kaushalpanjee.core.util.onRightDrawableClicked
import com.kaushalpanjee.core.util.setRightDrawablePassword
import com.kaushalpanjee.core.util.setUnderline
import com.kaushalpanjee.core.util.toastLong
import com.kaushalpanjee.core.util.toastShort
import com.kaushalpanjee.core.util.visible
import com.kaushalpanjee.databinding.FragmentEkyBinding
import com.kaushalpanjee.model.kyc_resp_pojo.XstreamCommonMethods
import com.kaushalpanjee.model.kyc_resp_pojo.XstreamCommonMethods.respDecodedXmlToPojoAuth
import com.kaushalpanjee.uidai.capture.CaptureResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

const val CAMERA_REQUEST = 101

@AndroidEntryPoint
class EKYCFragment : BaseFragment<FragmentEkyBinding>(FragmentEkyBinding::inflate) {
    private val commonViewModel: CommonViewModel by viewModels()

    private var stateList: MutableList<WrappedList> = mutableListOf()

    private var selectedState = ""
    private var showPassword = true
    private var name = ""
    private var dob = ""
    private var gender = ""
    private var careOf = ""
    private var state = ""
    private var dist = ""
    private var block = ""
    private var po = ""
    private var pinCode = ""
    private var street = ""
    private var village = ""
    private var photo = ""
    private var selectedStateCode = ""
    private var selectedStateLgdCode = ""
    private lateinit var layoutManager : LinearLayoutManager


    private val stateAdaptor by lazy {
        StateAdaptor(object : StateAdaptor.ItemClickListener {
            override fun onItemClick(position: Int) {

                selectedState = stateList[position].stateName
                binding.tvWelcome.text = getString(R.string.slected_state)
                binding.tvWelcomeMsg.text = selectedState

                binding.tvWelcomeMsg.setUnderline(selectedState)

                selectedStateCode = stateList[position].stateCode
                selectedStateLgdCode = stateList[position].lgdStateCode

                binding.progressButton.root.visible()
                binding.tvWelcomeMsg.visible()

                layoutManager.scrollToPosition(0)


            }
        })
    }

    private lateinit var onBackPressedCallback: OnBackPressedCallback

    private lateinit var progressBar: ProgressBar
    private lateinit var downloadHelper: DownloadHelper

    private var isStateSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        WindowInsetsControllerCompat(
            requireActivity().window,
            requireActivity().window.decorView
        ).isAppearanceLightStatusBars = false


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressButton.root.gone()
        init()

    }

    private fun init() {
        listener()
        setUI()
        collectStateResponse()
        collectUserCreationResponse()
        addTextWatchers()
        commonViewModel.getStateListApi()
        initEKYC()
        //  startAppDownload()


        onBackPressedCallback = object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {

                if (isStateSelected) {
                    binding.progressButton.root.gone()
                    binding.tvWelcomeMsg.visible()
                    binding.recyclerView.visible()
                    binding.etAadhaar.gone()
                    binding.aadhaarVerifyButton.root.gone()
                    isStateSelected = false
                } else if (findNavController().currentBackStackEntry != null) {

                    findNavController().navigateUp()

                } else {
                    requireActivity().onBackPressed()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )


    }

    private fun setUI() {

        layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = stateAdaptor

        binding.etAadhaar.setRightDrawablePassword(
            false,
            ContextCompat.getDrawable(requireContext(), R.drawable.icon_aadhaar), null,
            ContextCompat.getDrawable(requireContext(), R.drawable.close_eye), null
        )
    }


    /* private fun startAppDownload(){
         progressBar = binding.progressBar

         downloadHelper = DownloadHelper(requireContext())

         val url = "https://play.google.com/store/apps/details?id=in.gov.uidai.facerd"
         downloadHelper.startDownload(url, progressBar, object  : DownloadHelper.DownloadListener{
             override fun onDownloadComplete() {
                 binding.progressBar.gone()
                 toastShort(getString(R.string.download_complete))
             }

             override fun onProgress(progress: Int) {
                 binding.progressBar.visible()
                 binding.progressBar.progress = progress
             }

             override fun onDownloadFailed() {
                 binding.progressBar.gone()
                 toastShort(getString(R.string.download_failed))
             }
         })
     }*/


    private fun listener() {

        if (binding.etAadhaar.text.length == 12) {
            binding.aadhaarVerifyButton.root.visible()
        }
        binding.aadhaarVerifyButton.root.gone()

        binding.aadhaarVerifyButton.centerButton.setOnClickListener {

            if (binding.etAadhaar.text.length != 12) {

                showSnackBar("Please enter valid aadhaar number")
            } else {
                //  showSnackBar("success")
                invokeCaptureIntent()

            }

        }



        binding.progressButton.centerButton.setOnClickListener {
            binding.recyclerView.gone()
            binding.progressButton.root.gone()
            binding.etAadhaar.visible()
            if (binding.etAadhaar.text.isNotEmpty()) {
                binding.etAadhaar.setText("")
            }

            isStateSelected = true

        }

        binding.tvWelcomeMsg.setOnClickListener {
            binding.recyclerView.visible()

            binding.tvWelcomeMsg.text = selectedState
            binding.etAadhaar.gone()
            binding.progressButton.root.visible()
            binding.etAadhaar.gone()
            binding.aadhaarVerifyButton.root.gone()
        }

        binding.etAadhaar.onRightDrawableClicked {

            log("onRightDrawableClicked", "onRightDrawableClicked")

            if (showPassword) {
                showPassword = false
                binding.etAadhaar.setRightDrawablePassword(
                    true,
                    ContextCompat.getDrawable(requireContext(), R.drawable.icon_aadhaar), null,
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_open_eye), null
                )
            } else {
                showPassword = true

                binding.etAadhaar.setRightDrawablePassword(
                    false,
                    ContextCompat.getDrawable(requireContext(), R.drawable.icon_aadhaar), null,
                    ContextCompat.getDrawable(requireContext(), R.drawable.close_eye), null
                )

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
                                stateList = getStateResponse.stateList


                                stateAdaptor.setData(stateList)
                            } else if (getStateResponse.responseCode == 301) {

                                showSnackBar("Please Update from PlayStore")
                            } else showSnackBar("Something went wrong")

                        } ?: showSnackBar("Internal Sever Error")
                    }
                }
            }
        }
    }

    private fun addTextWatchers() {

        binding.etAadhaar.doOnTextChanged { text, _, _, _ ->

            if (text?.length == 12) {
                binding.aadhaarVerifyButton.root.visible()
            } else binding.aadhaarVerifyButton.root.gone()

        }

    }

    private var intentResponse: IntentResponse? = null
    private val neededPermissions = arrayOf(Manifest.permission.CAMERA)
    private var startTime: Long = 0
    private var userPhotoUIADI: Bitmap? = null
    private var ekycImage: String = ""

    private fun initEKYC() {


        intentResponse = IntentResponse(
            kycStatus = false,
            faceAuthStatus = false,
            partialKycStatus = false,
            uidaiStatusCode = "",
            txnId = "",
            kycTimeStamp = "",
            faceAuthTimeStamp = "",
            partialKycTimeStamp = "",
            similarity = 0.0,
            BuildConfig.VERSION_NAME
        )
        var request = requireArguments().getString("pmayg_request")

        if (request == null) {
            request = requireActivity().intent.getStringExtra("request")
        }

        val intentModel = Gson().fromJson(
            request?.let {
                AESCryptography.decryptIntoString(
                    it,
                    AppConstant.Constants.CRYPT_ID,
                    AppConstant.Constants.CRYPT_IV
                )
            },
            IntentModel::class.java
        )

        collectFaceAuthResponse()
        // setConsentText()
    }

    private val startUidaiAuthResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            try {
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent = result.data

                    if (intent != null) {
                        val captureResponse =
                            intent.getStringExtra(AppConstant.Constants.CAPTURE_INTENT_RESPONSE_DATA)

                        if (!captureResponse.isNullOrEmpty()) {
                            log("handleCaptureResponse", captureResponse)
                            handleCaptureResponse(captureResponse)
                        } else {
                            log("handleCaptureResponse", "Capture response data is null or empty.")
                            toastShort("Capture response is empty.")
                        }
                    } else {
                        log("handleCaptureResponse", "Intent data is null.")
                        toastShort("Failed to get capture response data.")
                    }
                } else {
                    toastLong("Failed to capture data.")
                    log("handleCaptureResponse", "Activity result code: ${result.resultCode}")
                }
            } catch (e: NullPointerException) {
                e.printStackTrace()
                toastShort("Error: Missing data in result.")
                log("startUidaiAuthResult", "NullPointerException: ${e.message}")
            } catch (e: Exception) {
                e.printStackTrace()
                toastShort("An error occurred while processing the result.")
                log("startUidaiAuthResult", "Exception: ${e.message}")
            }
        }


    private fun getTransactionID() = Random(System.currentTimeMillis()).nextInt(9999).toString()

    private fun invokeCaptureIntent() {

        try {
            val intent1 = Intent(AppConstant.Constants.CAPTURE_INTENT)
            intent1.putExtra(
                AppConstant.Constants.CAPTURE_INTENT_REQUEST,
                createPidOptions(getTransactionID(), "auth")
            )
            startUidaiAuthResult.launch(intent1)

            // val packageName = "com.example.otherapp" // Replace with the target app's package name
            val intent =
                requireContext().packageManager.getLaunchIntentForPackage(AppConstant.Constants.CAPTURE_INTENT)
            intent?.putExtra(
                AppConstant.Constants.CAPTURE_INTENT_REQUEST,
                createPidOptions(getTransactionID(), "auth")
            )
            if (intent != null) {
                startActivity(intent)
            }
        } catch (exp: Exception) {
            log("EKYCDATA", exp.toString())
        }

    }

    private fun createPidOptions(txnId: String, purpose: String): String {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<PidOptions ver=\"1.0\" env=\"${PRE_PRODUCTION_CODE}\">\n" + "   <Opts fCount=\"\" fType=\"\" iCount=\"\" iType=\"\" pCount=\"\" pType=\"\" format=\"\" pidVer=\"2.0\" timeout=\"\" otp=\"\" wadh=\"${AppConstant.Constants.WADH_KEY}\" posh=\"\" />\n" + "   <CustOpts>\n" + "      <Param name=\"txnId\" value=\"${txnId}\"/>\n" + "      <Param name=\"purpose\" value=\"$purpose\"/>\n" + "      <Param name=\"language\" value=\"$LANGUAGE}\"/>\n" + "   </CustOpts>\n" + "</PidOptions>"
    }


    private fun handleCaptureResponse(captureResponse: String) {
        try {

            // Check if camera permission is granted
            checkCameraPermission()

            // Parse the capture response XML to an object
            val response = CaptureResponse.fromXML(captureResponse)

            if (response.isSuccess) {
                showProgressBar()
                // Process the response to generate the PoiType or other required fields
                val poiType = XstreamCommonMethods.processPidBlockEkyc(
                    response.toXML(),
                    binding.etAadhaar.text.toString(),
                    false,
                    requireContext()
                )

                // Define Pre-Production URL (use a constant or environment configuration in production)
                val authURL = "http://10.247.252.95:8080/NicASAServer/ASAMain"

                // Record the start time for elapsed time computation
                startTime = SystemClock.elapsedRealtime()

                // Post the processed data for Face Authentication
                commonViewModel.postOnAUAFaceAuthNREGA(
                    AppConstant.StaticURL.FACE_AUTH_UIADI,
                    UidaiKycRequest(poiType, authURL)
                )
                // Handle Aadhaar authentication or additional processing here if required
            } else {
                toastLong(getString(R.string.kyc_failed_msg))
            }


        } catch (e: SecurityException) {
            // Handle camera permission-related issues
            hideProgressBar()
            e.printStackTrace()
            toastShort("Camera permission is required for this feature.")
            log("EKYCDATA", "SecurityException: ${e.message}")
        } catch (e: IllegalArgumentException) {
            // Handle cases where the response parsing might fail
            hideProgressBar()
            e.printStackTrace()
            toastShort("Invalid Capture Response format.")
            log("EKYCDATA", "IllegalArgumentException: ${e.message}")
        } catch (e: Exception) {
            // Catch all other exceptions
            hideProgressBar()
            e.printStackTrace()
            toastShort("An error occurred while processing the response.")
            log("EKYCDATA", "Exception: ${e.message}")
            // e.message?.copyToClipboard(requireContext())
        } finally {
            // Ensure progress bar is hidden after processing
            hideProgressBar()
            toastShort(getString(R.string.kyc_failed_msg))
        }
    }


    private fun checkCameraPermission(): Boolean {
        val permissionsNotGranted = java.util.ArrayList<String>()
        for (permission in neededPermissions) {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsNotGranted.add(permission)
            }
        }
        if (permissionsNotGranted.isNotEmpty()) {
            var shouldShowAlert = false
            for (permission in permissionsNotGranted) {
                shouldShowAlert =
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        permission
                    )
            }
            if (shouldShowAlert) {
                showPermissionAlert(permissionsNotGranted.toTypedArray())
            } else {
                requestPermissions(permissionsNotGranted.toTypedArray())
            }
            return false
        }
        return true
    }

    private fun showPermissionAlert(permissions: Array<String>) {
        val alertBuilder = AlertDialog.Builder(requireActivity())
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle("Permission Required")
        alertBuilder.setMessage("You must grant permission to access camera to run this application")
        alertBuilder.setPositiveButton(
            android.R.string.yes
        ) { _, _ -> requestPermissions(permissions) }
        val alert = alertBuilder.create()
        alert.show()
    }

    private fun requestPermissions(permissions: Array<String>) {
        ActivityCompat.requestPermissions(
            requireActivity(),
            permissions,
            CAMERA_REQUEST
        )
    }


    private fun collectFaceAuthResponse() {
        lifecycleScope.launch {
            try {
                collectLatestLifecycleFlow(commonViewModel.postOnAUAFaceAuthNREGA) { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                        }

                        is Resource.Error -> {
                            hideProgressBar()
                            resource.error?.let { errorResponse ->
                                toastShort(errorResponse.message)
                                log("EKYCDATA", errorResponse.message ?: "Unknown error message")
                            } ?: run {
                                toastShort("Nothing to show pls try again")
                            }
                        }

                        is Resource.Success -> {

                            resource.data?.body()?.let { uidaiData: UidaiResp ->
                                try {
                                    val kycResp = XstreamCommonMethods.respDecodedXmlToPojoEkyc(
                                        uidaiData.PostOnAUA_Face_authResult
                                    )

                                    //  uidaiData.PostOnAUA_Face_authResult.copyToClipboard(requireContext())

                                    log("EKYCDATA", kycResp.toString())

                                    if (kycResp.isSuccess) {
                                        val bytes: ByteArray =
                                            Base64.decode(kycResp.uidData.pht, Base64.DEFAULT)
                                        val bitmap =
                                            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                                        userPhotoUIADI = bitmap
                                        ekycImage = kycResp.uidData.pht ?: ""

                                        log("EKYCDATA", userPhotoUIADI.toString())
                                        log("EKYCDATA", ekycImage)


                                        name = kycResp.uidData.poi.name ?: "N/A"
                                        photo = kycResp.uidData.pht ?: "N/A"
                                        gender = kycResp.uidData.poi.gender ?: "N/A"
                                        dob = kycResp.uidData.poi.dob ?: "N/A"
                                        careOf = kycResp.uidData.poa.co ?: "N/A"
                                        state = kycResp.uidData.poa.state ?: "N/A"
                                        dist = kycResp.uidData.poa.dist ?: "N/A"
                                        block = kycResp.uidData.poa.subdist ?: "N/A"
                                        village = kycResp.uidData.poa.vtc ?: "N/A"
                                        street = kycResp.uidData.poa.loc ?: "N/A"
                                        po = kycResp.uidData.poa.po ?: "N/A"
                                        pinCode = kycResp.uidData.poa.pc ?: "N/A"


                                        // Get the last 4 digits
                                        val input = binding.etAadhaar.text.toString()
                                        val lastFourDigits = input.takeLast(4)
                                        // Create the masked string
                                        val maskedString =
                                            "*".repeat(input.length - 4) + lastFourDigits

                                        // Output the result
                                        lifecycleScope.launch {
                                            val email = arguments?.getString("email")
                                            val phone = arguments?.getString("phone")

                                            userPreferences.updateUserStateLgdCode(null)
                                            userPreferences.updateUserStateLgdCode(
                                                selectedStateLgdCode
                                            )


                                            val encryptedMaskedString =   AESCryptography.encryptIntoBase64String(binding.etAadhaar.text.toString(), AppConstant.Constants.ENCRYPT_KEY, AppConstant.Constants.ENCRYPT_IV_KEY)
                                            val encryptedName =   AESCryptography.encryptIntoBase64String(name, AppConstant.Constants.ENCRYPT_KEY, AppConstant.Constants.ENCRYPT_IV_KEY)
                                            val encryptedGender =   AESCryptography.encryptIntoBase64String(gender, AppConstant.Constants.ENCRYPT_KEY, AppConstant.Constants.ENCRYPT_IV_KEY)
                                            val encryptedDob =   AESCryptography.encryptIntoBase64String(dob, AppConstant.Constants.ENCRYPT_KEY, AppConstant.Constants.ENCRYPT_IV_KEY)
                                            val encryptedState =   AESCryptography.encryptIntoBase64String(state, AppConstant.Constants.ENCRYPT_KEY, AppConstant.Constants.ENCRYPT_IV_KEY)
                                            val encryptedSelectedStateCode =   AESCryptography.encryptIntoBase64String(selectedStateCode, AppConstant.Constants.ENCRYPT_KEY, AppConstant.Constants.ENCRYPT_IV_KEY)
                                            val encryptedDist =   AESCryptography.encryptIntoBase64String(dist, AppConstant.Constants.ENCRYPT_KEY, AppConstant.Constants.ENCRYPT_IV_KEY)
                                            val encryptedBlock =   AESCryptography.encryptIntoBase64String(block, AppConstant.Constants.ENCRYPT_KEY, AppConstant.Constants.ENCRYPT_IV_KEY)
                                            val encryptedPo =   AESCryptography.encryptIntoBase64String(po, AppConstant.Constants.ENCRYPT_KEY, AppConstant.Constants.ENCRYPT_IV_KEY)
                                            val encryptedVillage =   AESCryptography.encryptIntoBase64String(village, AppConstant.Constants.ENCRYPT_KEY, AppConstant.Constants.ENCRYPT_IV_KEY)
                                            val encryptedPinCode =   AESCryptography.encryptIntoBase64String(pinCode, AppConstant.Constants.ENCRYPT_KEY, AppConstant.Constants.ENCRYPT_IV_KEY)
                                            val encryptedPhone = phone?.let {
                                                AESCryptography.encryptIntoBase64String(
                                                    it, AppConstant.Constants.ENCRYPT_KEY, AppConstant.Constants.ENCRYPT_IV_KEY)
                                            }
                                            val encryptedEmail = email?.let {
                                                AESCryptography.encryptIntoBase64String(
                                                    it, AppConstant.Constants.ENCRYPT_KEY, AppConstant.Constants.ENCRYPT_IV_KEY)
                                            }
                                            val encryptedCareOf =   AESCryptography.encryptIntoBase64String(careOf, AppConstant.Constants.ENCRYPT_KEY, AppConstant.Constants.ENCRYPT_IV_KEY)
                                            val encryptedStreet =   AESCryptography.encryptIntoBase64String(street, AppConstant.Constants.ENCRYPT_KEY, AppConstant.Constants.ENCRYPT_IV_KEY)
                                            val encryptedLdgdCode =   AESCryptography.encryptIntoBase64String(selectedStateLgdCode, AppConstant.Constants.ENCRYPT_KEY, AppConstant.Constants.ENCRYPT_IV_KEY)

                                            commonViewModel.getCreateUserAPI(UserCreationReq(
                                                encryptedMaskedString,
                                                encryptedName,
                                                encryptedGender,
                                                encryptedDob,
                                                encryptedState,
                                                encryptedSelectedStateCode,
                                                encryptedDist,
                                                encryptedBlock,
                                                encryptedPo,
                                                encryptedVillage,
                                                encryptedPinCode,
                                                encryptedPhone,
                                                encryptedEmail,
                                                encryptedCareOf,
                                                encryptedStreet,
                                                BuildConfig.VERSION_NAME,
                                                photo,
                                                AppUtil.getAndroidId(requireContext()),encryptedLdgdCode
                                            )
                                            )




                                        }

                                        hideProgressBar()
                                        showBottomSheet(bitmap, name, gender, dob, careOf)

                                        // toastShort("Ekyc Completed")
                                    } else {
                                        hideProgressBar()
                                        val decodedRar = decodeBase64(kycResp.rar)
                                        decodedRar?.let { decodedRarParsed ->
                                            val authRes = respDecodedXmlToPojoAuth(decodedRarParsed)
                                            val errorDesc =
                                                XstreamCommonMethods.getAuthErrorDescription(authRes.info)
                                            log("EKYCDATA", errorDesc)

                                            toastShort("EKYCDATA: Failed")
                                        } ?: toastShort("Getting Error")
                                    }
                                } catch (e: Exception) {
                                    hideProgressBar()
                                    e.printStackTrace()
                                    log("EKYCDATA", "Error processing KYC response: ${e.message}")
                                    toastShort("Error processing KYC response")
                                }
                            }
                                ?: toastShort(getString(R.string.something_went_wrong_at_uidai_site))
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                hideProgressBar()
                log("EKYCDATA", "Unhandled error: ${e.message}")
                toastShort("An unexpected error occurred. Please try again.")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Make sure to remove the callback to avoid memory leaks
        onBackPressedCallback.remove()
    }

    private fun showBottomSheet(
        image: Bitmap,
        name: String,
        gender: String,
        dateOfBirth: String,
        careOf: String
    ) {
        // Initialize the BottomSheetDialog
        val bottomSheetDialog = BottomSheetDialog(requireContext())

        // Inflate the layout
        val view = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)

        // Set the view to the BottomSheetDialog
        bottomSheetDialog.setContentView(view)

        // Prevent the BottomSheetDialog from closing when touching outside
        bottomSheetDialog.setCanceledOnTouchOutside(false)

        // Prevent the BottomSheetDialog from closing on back press
        bottomSheetDialog.setCancelable(false)

        // Intercept the back press using a key listener on the window of the dialog
        bottomSheetDialog.setOnShowListener {
            val dialogWindow = bottomSheetDialog.window
            dialogWindow?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            )

            // Set OnKeyListener to prevent back press dismiss
            dialogWindow?.decorView?.setOnKeyListener { _, keyCode, event ->
                keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP
            }
        }

        // Find views in the inflated layout
        val imageView = view.findViewById<ImageView>(R.id.circleImageView)
        val nameView = view.findViewById<TextView>(R.id.eKYCCandidateName)
        val genderView = view.findViewById<TextView>(R.id.eKYCGender)
        val dobView = view.findViewById<TextView>(R.id.eKYCDob)
        val careOfView = view.findViewById<TextView>(R.id.eKYCCareOf)
        val okButton = view.findViewById<TextView>(R.id.tvLogin)

        // Set data to views
        imageView.setImageBitmap(image)
        nameView.text = name
        genderView.text = gender
        dobView.text = dateOfBirth
        careOfView.text = careOf

        // Handle OK button click
        okButton.setOnClickListener {
            // findNavController().navigate(EKYCFragmentDirections.actionEkycFragmentToMainHomePage())

            AppUtil.saveLoginStatus(requireContext(), true)

            val navController = findNavController()

            // Pop `thirdFragment`
            navController.popBackStack(R.id.loginFragment, true)


            // Pop `secondFragment`
            navController.popBackStack(R.id.registerFragment, true)

            // Pop `firstFragment`
            navController.popBackStack(R.id.ekycFragment, true)

            // Finally navigate to `targetFragment`
            navController.navigate(R.id.mainHomePage)
            bottomSheetDialog.dismiss() // Close the BottomSheetDialog
        }

        // Show the BottomSheetDialog
        bottomSheetDialog.show()
    }

    private fun collectUserCreationResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getuserCreation) {
                when (it) {
                    is Resource.Loading -> {}
                    is Resource.Error -> {
                        // hideProgressBar()
                        it.error?.let { baseErrorResponse ->
                            showSnackBar(baseErrorResponse.message)
                            toastShort("error in create Api")

                        }
                    }

                    is Resource.Success -> {
                        // hideProgressBar()
                        it.data?.let { getUserCreationRes ->
                            if (getUserCreationRes.responseCode == 200) {
                                for (x in getUserCreationRes.wrappedList) {
                                    userPreferences.updateUserId(null)
                                    userPreferences.updateUserId(x.userId)

                                    toastLong("userId: ${x.userId}" + "password: ${x.password}")

                                }
                                toastLong("Your username and password have been sent to your email.")

                            }

                        } ?: showSnackBar("Internal Sever Error")
                    }
                }
            }
        }
    }


}
