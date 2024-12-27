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
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.R
import com.kaushalpanjee.common.model.UidaiKycRequest
import com.kaushalpanjee.common.model.UidaiResp
import com.kaushalpanjee.common.model.WrappedList
import com.kaushalpanjee.common.model.response.IntentModel
import com.kaushalpanjee.common.model.response.IntentResponse
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.AESCryptography
import com.kaushalpanjee.core.util.AppConstant
import com.kaushalpanjee.core.util.AppConstant.Constants.LANGUAGE
import com.kaushalpanjee.core.util.AppConstant.Constants.PRE_PRODUCTION_CODE
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

    private val stateAdaptor by lazy {
        StateAdaptor(object : StateAdaptor.ItemClickListener {
            override fun onItemClick(position: Int) {

                selectedState = stateList[position].stateName
                binding.tvWelcome.text = getString(R.string.slected_state)
                binding.tvWelcomeMsg.text = selectedState

                binding.tvWelcomeMsg.setUnderline(selectedState)
                toastShort("${stateList[position].stateName} ,  ${stateList[position].stateCode}")
                binding.progressButton.root.visible()
                binding.tvWelcomeMsg.visible()

                lifecycleScope.launch {
                    delay(1000)
                    binding.recyclerView.smoothScrollToPosition(0)

                }

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
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
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

        /* binding.progressBackButton.setOnClickListener {

             findNavController().navigateUp()
            // findNavController().navigate(R.id.loginFragment)
         }*/


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

    private var stateCode = ""
    private var applicantId = ""
    private var ekycId = ""
    private var isKycDone = false

    private val inDateFormat = "yyyy-MM-dd'T'HH:mm:ss"
    private val outDateFormat = "dd MMM yyyy HH:mm:ss"
    private var startTime: Long = 0
    private var computationTime: String = ""
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

        /*   userPreferences.saveSchemeCode(intentModel.scheme)

           stateCode = intentModel.State_Code
           applicantId = intentModel.applicant_id
           ekycId = intentModel.ekyc_id
   */
        collectFaceAuthResponse()
        // setConsentText()
    }

    private val startUidaiAuthResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data

                intent?.let {
                    log(
                        "handleCaptureResponse",
                        it.getStringExtra(AppConstant.Constants.CAPTURE_INTENT_RESPONSE_DATA)!!
                    )
                    it.getStringExtra(AppConstant.Constants.CAPTURE_INTENT_RESPONSE_DATA)
                        ?.let { it1 ->

                            handleCaptureResponse(it1)

                        }
                }
            } else toastLong("failed to capture")
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
        showProgressBar()
        /* try {
             val response = CaptureResponse.fromXML(captureResponse)

            computationTime = FaceUtils.formatCaptureResponse(response.txnID, response)
             val poiType  = XstreamCommonMethos.respPoiTypeToPojo(captureResponse)

             log("EKYCDATA", poiType.name)

             toastShort("Response Parsed")

             if (response.isSuccess) {
                 toastShort("Success ")
                 aadhaarAuth(response)
             }else toastLong("Failed")


         } catch (e: java.lang.Exception) {
             e.printStackTrace()
             toastShort("Error handleCaptureResponse ")
             e.message.toString().copyToClipboard(requireContext())
         }*/
        checkCameraPermission()

        val response = CaptureResponse.fromXML(captureResponse)

        if (response.isSuccess) {
            toastShort("Success ")

        } else toastLong("Failed")


        val poiType = XstreamCommonMethods.processPidBlockEkyc(
            response.toXML(), "939625617876",
            false, requireContext()
        )

        log("EKYCDATA", poiType)

        toastShort("Response Parsed")

        // Pre-Production URL
        val authURL = "http://10.247.252.95:8080/NicASAServer/ASAMain"

        /* val authInputs: String = XstreamCommonMethods.processPidBlock(
             response.toXML(), "939625617876", false,
             requireContext()
         )*/

        startTime = SystemClock.elapsedRealtime()

        toastShort("Hitting API")

        commonViewModel.postOnAUAFaceAuthNREGA(
            AppConstant.StaticURL.FACE_AUTH_UIADI,
            UidaiKycRequest(poiType, authURL)
        )

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
            collectLatestLifecycleFlow(commonViewModel.postOnAUAFaceAuthNREGA) {
                when (it) {
                    is Resource.Loading -> {

                    }

                    is Resource.Error -> {
                        hideProgressBar()
                        it.error?.let { errorResponse ->

                            toastShort(errorResponse.message)
                            log("EKYCDATA", errorResponse.message)
                        }
                    }

                    is Resource.Success -> {
                        hideProgressBar()

                        it.data?.body()?.let { uidaiData: UidaiResp ->

                            /*val serializer = Persister()
                            val kycResponse: KycResponse = serializer.read(KycResponse::class.java,
                                uidaiData.PostOnAUA_Face_authResult)*/

                            val kycResp =
                                XstreamCommonMethods.respDecodedXmlToPojoEkyc(uidaiData.PostOnAUA_Face_authResult)

                            uidaiData.PostOnAUA_Face_authResult.copyToClipboard(requireContext())


                            log("EKYCDATA", kycResp.toString())


                            if (kycResp.isSuccess) {
                                val bytes: ByteArray =
                                    Base64.decode(kycResp.uidData.pht, Base64.DEFAULT)
                                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                                userPhotoUIADI = bitmap
                                kycResp.uidData.pht?.let { image->
                                    ekycImage = image
                                }

                                log("EKYCDATA", userPhotoUIADI.toString())
                                log("EKYCDATA", ekycImage)

                                //log("EKYCDATA", kycResponse.err)
                                //log("EKYCDATA", kycResponse.txn)


                                kycResp.uidData.setName()

                                kycResp.uidData.poi.name?.let { it1 -> toastShort(it1) }
                                kycResp.uidData.poi.gender?.let { it1 -> toastShort(it1) }
                                kycResp.uidData.poi.dob?.let { it1 -> toastShort(it1) }

                                kycResp.uidData.poa.careof ?.let { it1 -> toastShort(it1) }


                                showBottomSheet(bitmap)

                                toastShort("EKYCDATA: Success")


                            } else {

                                //log("EKYCDATA", kycResp.err)
                               // log("EKYCDATA", kycResp.txn)

                                val decodedRar = decodeBase64(kycResp.rar)
                                decodedRar?.let {decodedRarParsed->
                                    val authRes = respDecodedXmlToPojoAuth(decodedRarParsed)

                                    val errorDesc = XstreamCommonMethods.getAuthErrorDescription(authRes.info)
                                    log("EKYCDATA", errorDesc)

                                    toastShort("EKYCDATA: Failed")

                                }?:toastShort("Getting Error")

                            }

                        } ?: toastShort(getString(R.string.something_went_wrong_at_uidai_site))


                    }
                }
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
        name: String = "Parsed",
        gender: String = "Gender",
        dateOdBirth: String = "01-01-01",
       /* careOf: String*/
    ) {
        // Initialize the BottomSheetDialog
        val bottomSheetDialog = BottomSheetDialog(requireContext())

        // Inflate the layout
        val view = layoutInflater.inflate(R.layout.bottom_sheet_layout,null)
        // Handle button click inside Bottom Sheet
        binding.tvWelcome.setOnClickListener {
            bottomSheetDialog.dismiss() // Close Bottom Sheet
        }

        val imageView = view.findViewById<ImageView>(R.id.circleImageView)
        val nameView = view.findViewById<TextView>(R.id.eKYCCandidateName)
        val genderView = view.findViewById<TextView>(R.id.eKYCGender)
        val dobView = view.findViewById<TextView>(R.id.eKYCDob)
        val careOfView = view.findViewById<TextView>(R.id.eKYCCareOf)

        imageView.setImageBitmap(image)
        nameView.text = name
        genderView.text = gender
        dobView.text = dateOdBirth
        //careOfView.text = careOf

        // Set the view and show the Bottom Sheet
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }
}