
package com.kaushalpanjee.common

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.util.Base64
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.R
import com.kaushalpanjee.common.model.UidaiKycRequest
import com.kaushalpanjee.common.model.WrappedList
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.AppConstant
import com.kaushalpanjee.core.util.AppConstant.Constants.LANGUAGE
import com.kaushalpanjee.core.util.AppConstant.Constants.PRE_PRODUCTION
import com.kaushalpanjee.core.util.AppConstant.Constants.PRE_PRODUCTION_CODE
import com.kaushalpanjee.core.util.AppConstant.Constants.PRODUCTION
import com.kaushalpanjee.core.util.AppConstant.Constants.PRODUCTION_CODE
import com.kaushalpanjee.core.util.AppConstant.Constants.STAGING_CODE
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.core.util.copyToClipboard
import com.kaushalpanjee.core.util.gone
import com.kaushalpanjee.core.util.log
import com.kaushalpanjee.core.util.onRightDrawableClicked
import com.kaushalpanjee.core.util.setRightDrawablePassword
import com.kaushalpanjee.core.util.setUnderline
import com.kaushalpanjee.core.util.toastLong
import com.kaushalpanjee.core.util.toastShort
import com.kaushalpanjee.core.util.visible
import com.kaushalpanjee.databinding.FragmentEkyBinding
import com.kaushalpanjee.uidai.XstreamCommonMethods
import com.kaushalpanjee.uidai.capture.CaptureResponse
import com.kaushalpanjee.uidai.capture.FaceUtils
import com.kaushalpanjee.uidai.capture.XstreamCommonMethos
import com.kaushalpanjee.uidai.ekyc.IntentModel
import com.kaushalpanjee.uidai.ekyc.IntentResponse
import com.utilize.core.util.AESCryptography
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import rural.ekyc.ui.ekyc.models.UidaiResp
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

                selectedState = stateList[position].state_name
                binding.tvWelcome.text = getString(R.string.slected_state)
                binding.tvWelcomeMsg.text = selectedState
                binding.tvWelcomeMsg.visible()
                binding.tvWelcomeMsg.setUnderline(selectedState)
                toastShort("${stateList[position].state_name} ,  ${stateList[position].state_code}")
                binding.progressButton.root.visible()

                lifecycleScope.launch {
                    delay(1000)
                    binding.recyclerView.smoothScrollToPosition(0)

                }

            }
        })
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
      //  initEKYC()

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

    private fun listener() {
        if (binding.etAadhaar.text.length == 12) {
            binding.aadhaarVerifyButton.root.visible()
        }
        binding.aadhaarVerifyButton.root.gone()

        binding.aadhaarVerifyButton.centerButton.setOnClickListener {

            if (binding.etAadhaar.text.length != 12) {

                showSnackBar("Please enter valid aadhaar number")
            } else {
                showSnackBar("success")
               // invokeCaptureIntent()

            }

        }



        binding.progressButton.centerButton.setOnClickListener {
            binding.recyclerView.gone()
            binding.progressButton.root.gone()
            binding.etAadhaar.visible()
            if (binding.etAadhaar.text.isNotEmpty()) {
                binding.etAadhaar.setText("")
            }

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
                    true,
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
                                stateList = getStateResponse.wrappedList


                                stateAdaptor.setData(stateList)
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
                    log("handleCaptureResponse", it.getStringExtra(AppConstant.Constants.CAPTURE_INTENT_RESPONSE_DATA)!!)
                    it.getStringExtra(AppConstant.Constants.CAPTURE_INTENT_RESPONSE_DATA)
                        ?.let { it1 ->
                            handleCaptureResponse(it1)
                        binding.tvWelcome.text = it1
                            it1.copyToClipboard(requireContext())
                        }
                }
            }else toastLong("failed to capture")
        }

    private fun getTransactionID() = Random(System.currentTimeMillis()).nextInt(9999).toString()


    private fun invokeCaptureIntent() {

        val intent1 = Intent(AppConstant.Constants.CAPTURE_INTENT)
        intent1.putExtra(
            AppConstant.Constants.CAPTURE_INTENT_REQUEST, createPidOptions(getTransactionID(), "auth")
        )
        startUidaiAuthResult.launch(intent1)

       // val packageName = "com.example.otherapp" // Replace with the target app's package name
        val intent = requireContext().packageManager.getLaunchIntentForPackage(AppConstant.Constants.CAPTURE_INTENT)
        intent?.putExtra(
            AppConstant.Constants.CAPTURE_INTENT_REQUEST, createPidOptions(getTransactionID(), "auth")
        )
        if (intent != null) {
            startActivity(intent)
        }


    }


    private fun createPidOptions(txnId: String, purpose: String): String {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<PidOptions ver=\"1.0\" env=\"${getEnvironment(PRODUCTION)}\">\n" + "   <Opts fCount=\"\" fType=\"\" iCount=\"\" iType=\"\" pCount=\"\" pType=\"\" format=\"\" pidVer=\"2.0\" timeout=\"\" otp=\"\" wadh=\"${AppConstant.Constants.WADH_KEY}\" posh=\"\" />\n" + "   <CustOpts>\n" + "<Param name=\"txnId\" value=\"${txnId}\"/>\n" + "      <Param name=\"purpose\" value=\"$purpose\"/>\n" + "      <Param name=\"language\" value=\"$LANGUAGE\"/>\n" + "   </CustOpts>\n" + "</PidOptions>"
    }

    private fun getEnvironment(environmentType: String): String {
        return when (environmentType) {
            PRE_PRODUCTION -> PRE_PRODUCTION_CODE
            PRODUCTION -> PRODUCTION_CODE
            else -> STAGING_CODE
        }
    }


    private fun handleCaptureResponse(captureResponse: String) {
        showProgressBar()
        try {
            val response = CaptureResponse.fromXML(captureResponse)

            computationTime = FaceUtils.formatCaptureResponse(response.txnID, response)

            if (response.isSuccess) {
                toastShort("Success ")
                aadhaarAuth(response)
            }else toastLong("Failed")
            hideProgressBar()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun aadhaarAuth(response: CaptureResponse) {
        try {
            checkCameraPermission()
            // Pre-Production URL
            val authURL = "http://10.247.252.95:8080/NicASAServer/ASAMain"

            val authInputs: String = XstreamCommonMethos.processPidBlock(
                response.toXML(), "939625617876", false,
                requireContext()
            )

            startTime = SystemClock.elapsedRealtime()

            commonViewModel.postOnAUAFaceAuthNREGA(
                AppConstant.StaticURL.FACE_AUTH_UIADI,
                UidaiKycRequest(authInputs, authURL)
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            e.message?.let { toastShort(it) }
        }
    }


  /*  override fun onResume() {
        super.onResume()
        checkCameraPermission()
    }*/

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
                    ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)
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


  /*  private fun setConsentText() {

        val ss = SpannableString(getString(R.string.consent_text_short))
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                makeConsentDialog()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }
        }

        ss.setSpan(clickableSpan, 66, 75, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvConsent.text = ss
        binding.tvConsent.movementMethod = LinkMovementMethod.getInstance()
        binding.tvConsent.highlightColor = Color.TRANSPARENT

    }
    private fun makeConsentDialog() {
        AlertDialog.Builder(context)
            .setTitle("Consent")
            .setMessage(getString(R.string.consent_text))
            .setPositiveButton(
                "Agree"
            ) { dialog, _ ->
                dialog.dismiss()
                binding.checkBoxConsent.isChecked = true
            }.setNegativeButton("Disagree") { dialog, which ->
                dialog.dismiss()
                binding.checkBoxConsent.isChecked = false
            }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }*/



    private fun collectFaceAuthResponse(){
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.postOnAUAFaceAuthNREGA){
                when(it){
                    is Resource.Loading->{}
                    is Resource.Error->{
                        hideProgressBar()
                        it.error?.let { errorResponse ->

                            toastShort(errorResponse.message)
                        }
                    }
                    is Resource.Success->{
                        hideProgressBar()
                        it.data?.let {response: Response<UidaiResp> ->

                            if (response.isSuccessful){
                                if (TextUtils.equals(response.message(), "transaction inserted")) {
                                    //Only transaction recorded ekyc response was 0
                                    intentResponse?.kycStatus = false
                                    intentResponse?.kycTimeStamp = ""
                                    toastShort("Show Fail Status")
                                }
                                else {
                                    isKycDone = true
                                    val dateTime = response.body()?.PostOnAUA_Face_authResult

                                    val kycResp =
                                        XstreamCommonMethods.respDecodedXmlToPojoEkyc(response.body()?.PostOnAUA_Face_authResult)
                                  //  val poiType: Poa = XstreamCommonMethos.rarDecodedXmlToPojo(response.body()?.PostOnAUA_Face_authResult)

                                 //   Toast.makeText(this, poiType.getName(), Toast.LENGTH_SHORT).show()
                               //     kycResp.uidData.name

                                    if (kycResp.isSuccess){

                                        val bytes: ByteArray =
                                            Base64.decode(kycResp.uidData.pht, Base64.DEFAULT)
                                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                                      //  binding.ivPersonImg.setImageBitmap(bitmap)

                                       val  photo = kycResp.uidData.pht
                                       val  name = kycResp.uidData.name
                                       val  gender = kycResp.uidData.gender
                                       val  dob = kycResp.uidData.dob


                                        showSnackBar(name.toString()+"gender"+gender.toString())



                                        intentResponse?.partialKycStatus = true
                                        intentResponse?.partialKycTimeStamp = AppUtil.getCurrentDateTime()
                                        intentResponse?.uidaiStatusCode = ""
                                        intentResponse?.txnId = kycResp.txn


                                    }

                                   toastShort( "Last Auth Done: ${
                                        if (TextUtils.isEmpty(dateTime)) "NA" else dateTime?.let { it1 ->
                                            AppUtil.convertUTCtoIST(
                                                inDateFormat,
                                                outDateFormat,
                                                it1
                                            )
                                        }
                                    }")

                                    val scheme ="Scheme: ${userPreferences.getSchemeCode()}"
                                    toastShort(scheme)
                                    // show Success status here

                                    intentResponse?.kycStatus = true
                                    intentResponse?.kycTimeStamp = dateTime

                                }
                            }

                            else {
                                intentResponse?.kycStatus = false
                                intentResponse?.kycTimeStamp = ""
                                toastShort("Status failed")
                            }

                        }
                    }
                }
            }

        }
    }


}