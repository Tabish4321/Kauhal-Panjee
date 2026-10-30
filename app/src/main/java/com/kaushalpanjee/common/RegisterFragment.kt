package com.kaushalpanjee.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.R
import com.kaushalpanjee.common.compose.registration.RegistrationComposeScreen
import com.kaushalpanjee.common.compose.registration.RegistrationSuccessPopup
import com.kaushalpanjee.common.model.UidaiKycRequest
import com.kaushalpanjee.common.model.UidaiResp
import com.kaushalpanjee.common.model.request.InsertAadhaarTxnReq
import com.kaushalpanjee.common.model.request.UserCreationReq
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.AESCryptography
import com.kaushalpanjee.core.util.AppConstant
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.core.util.copyToClipboard
import com.kaushalpanjee.core.util.decodeBase64
import com.kaushalpanjee.core.util.toastLong
import com.kaushalpanjee.core.util.toastShort
import com.kaushalpanjee.databinding.FragmentRegisterBinding
import com.kaushalpanjee.model.kyc_resp_pojo.XstreamCommonMethods
import com.kaushalpanjee.model.kyc_resp_pojo.XstreamCommonMethods.respDecodedXmlToPojoAuth
import com.kaushalpanjee.uidai.capture.CaptureResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val REGISTER_CAMERA_REQUEST = 101

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    private val commonViewModel: CommonViewModel by viewModels()

    private var currentAadhaar = ""
    private var selectedStateCode = ""
    private var selectedStateLgdCode = ""
    private var candidateId = ""
    private var appTxn = ""
    private var userCreationRequestJson = ""

    private var userPhotoUIADI: Bitmap? = null
    private var name = "N/A"
    private var gender = "N/A"
    private var dob = "N/A"
    private var careOf = "N/A"
    private var state = "N/A"
    private var dist = "N/A"
    private var block = "N/A"
    private var village = "N/A"
    private var street = "N/A"
    private var po = "N/A"
    private var pinCode = "N/A"
    private var photo = ""

    private var isWelcomeScreen = true
    private var lastBackPressedAt = 0L
    private var faceAuthRequestInProgress = false

    private val cameraPermission = arrayOf(Manifest.permission.CAMERA)

    private val faceRdLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            handleFaceRdResult(result)
        }

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val granted = result[Manifest.permission.CAMERA] == true
            if (granted) launchFaceRdCapture()
            else toastShort("Camera permission is required for Aadhaar face authentication.")
        }

    private val backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (!isWelcomeScreen) {
                // Let the Compose BackHandler move to the previous registration step.
                isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
                isEnabled = true
                return
            }

            val now = android.os.SystemClock.elapsedRealtime()
            if (now - lastBackPressedAt <= 2_000L) {
                isEnabled = false
                requireActivity().finish()
            } else {
                lastBackPressedAt = now
                toastShort("Press back again to exit")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    RegistrationComposeScreen(
                        commonViewModel = commonViewModel,
                        onBack = { findNavController().popBackStack() },
                        onAadhaarCheckSuccess = { aadhaar, stateCode, stateLgdCode, candidate ->
                            currentAadhaar = aadhaar
                            selectedStateCode = stateCode
                            selectedStateLgdCode = stateLgdCode
                            candidateId = candidate
                            startFaceRdFlow()
                        },
                        onLoginClick = {
                            findNavController().navigate(
                                RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                            )
                        },
                        onWelcomeStateChanged = { isWelcomeScreen = it }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backCallback)
        collectFaceAuthResponse()
        collectInsertAadhaarTxnResponse()
        collectUserCreationResponse()
    }

    private fun startFaceRdFlow() {
        if (currentAadhaar.length != 12 || selectedStateCode.isBlank()) {
            toastShort("Aadhaar or selected state is missing.")
            return
        }
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            cameraPermissionLauncher.launch(cameraPermission)
            return
        }
        launchFaceRdCapture()
    }

    private fun launchFaceRdCapture() {
        try {
            val intent = Intent(AppConstant.Constants.CAPTURE_INTENT)
            intent.putExtra(
                AppConstant.Constants.CAPTURE_INTENT_REQUEST,
                createPidOptions(getTransactionID(), "auth")
            )
            faceRdLauncher.launch(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            toastShort("Failed to open FaceRD capture app.")
        }
    }

    private fun handleFaceRdResult(result: ActivityResult) {
        try {
            if (result.resultCode != Activity.RESULT_OK) {
                toastLong("Face authentication capture was cancelled or failed.")
                return
            }
            val captureResponse = result.data?.getStringExtra(
                AppConstant.Constants.CAPTURE_INTENT_RESPONSE_DATA
            )
            if (captureResponse.isNullOrBlank()) {
                toastShort("Capture response is empty.")
                return
            }
            handleCaptureResponse(captureResponse)
        } catch (e: Exception) {
            e.printStackTrace()
            toastShort("Unable to process FaceRD response.")
        }
    }

    private fun getTransactionID(): String {
        val randomNumber = (100000000000L + SecureRandom().nextDouble() * 900000000000L).toLong()
        val date = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
        appTxn = "KaushalPanjee${randomNumber}${date}AEAD"
        return appTxn
    }

    private fun createPidOptions(txnId: String, purpose: String): String {
        return """<?xml version="1.0" encoding="UTF-8"?>
<PidOptions ver="1.0" env="${AppConstant.Constants.PRODUCTION}">
   <Opts fCount="" fType="" iCount="" iType="" pCount="" pType="" format="" pidVer="2.0" timeout="" otp="" wadh="${AppConstant.Constants.WADH_KEY}" posh="" />
   <CustOpts>
      <Param name="txnId" value="$txnId"/>
      <Param name="purpose" value="$purpose"/>
      <Param name="language" value="${AppConstant.Constants.LANGUAGE}"/>
   </CustOpts>
</PidOptions>"""
    }

    private fun handleCaptureResponse(captureResponse: String) {
        try {
            val capture = CaptureResponse.fromXML(captureResponse)
            if (!capture.isSuccess) {
                toastLong(getString(R.string.kyc_failed_msg))
                return
            }

            val poiType = XstreamCommonMethods.processPidBlockEkyc(
                capture.toXML(),
                currentAadhaar,
                false,
                requireContext()
            )
            val authUrl = "http://10.247.252.93:8080/NicASAServer/ASAMain"
            faceAuthRequestInProgress = true
            commonViewModel.postOnAUAFaceAuthNREGA(
                AppConstant.StaticURL.FACE_AUTH_UIADI,
                UidaiKycRequest(poiType, authUrl)
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
            toastShort("Camera permission is required for this feature.")
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            toastShort("Invalid Capture Response format.")
        } catch (e: Exception) {
            e.printStackTrace()
            toastShort("An error occurred while processing FaceRD response.")
        }
    }

    private fun collectFaceAuthResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.postOnAUAFaceAuthNREGA) { resource ->
                when (resource) {
                    is Resource.Loading -> {}
                    is Resource.Error -> {
                        hideProgressBar()
                        toastShort(resource.error?.message ?: "Face authentication failed.")
                        resource.data?.body()?.let { uidaiData ->
                            runCatching {
                                val response = XstreamCommonMethods.respDecodedXmlToPojoEkyc(
                                    uidaiData.PostOnAUA_Face_authResult
                                )
                                commonViewModel.insertAadhaarTxn(
                                    InsertAadhaarTxnReq(response.txn, appTxn, response.ret, response.code)
                                )
                            }.onFailure { it.printStackTrace() }
                        }
                    }
                    is Resource.Success -> {
                        hideProgressBar()
                        val uidaiData = resource.data?.body()
                        if (uidaiData == null) {
                            toastShort("Server error from UIDAI. Please try again.")
                            return@collectLatestLifecycleFlow
                        }
                        try {
                            val response = XstreamCommonMethods.respDecodedXmlToPojoEkyc(
                                uidaiData.PostOnAUA_Face_authResult
                            )
                            commonViewModel.insertAadhaarTxn(
                                InsertAadhaarTxnReq(response.txn, appTxn, response.ret, response.code)
                            )

                            if (!response.isSuccess) {
                                val decoded = decodeBase64(response.rar)
                                val description = decoded?.let {
                                    val auth = respDecodedXmlToPojoAuth(it)
                                    XstreamCommonMethods.getAuthErrorDescription(auth.info)
                                }
                                toastShort(description ?: "Aadhaar face authentication failed.")
                                return@collectLatestLifecycleFlow
                            }

                            val uid = response.uidData
                            val bytes = android.util.Base64.decode(uid.pht, android.util.Base64.DEFAULT)
                            userPhotoUIADI = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                            photo = uid.pht ?: ""
                            name = uid.poi.name ?: "N/A"
                            gender = uid.poi.gender ?: "N/A"
                            dob = uid.poi.dob ?: "N/A"
                            careOf = uid.poa.co ?: "N/A"
                            state = uid.poa.state ?: "N/A"
                            dist = uid.poa.dist ?: "N/A"
                            block = uid.poa.subdist ?: "N/A"
                            village = uid.poa.vtc ?: "N/A"
                            street = uid.poa.loc ?: "N/A"
                            po = uid.poa.po ?: "N/A"
                            pinCode = uid.poa.pc ?: "N/A"

                            createUserAfterFaceAuth()
                        } catch (e: Exception) {
                            e.printStackTrace()
                            toastShort("Error processing Aadhaar KYC response.")
                        }
                    }
                }
            }
        }
    }

    private fun createUserAfterFaceAuth() {
        lifecycleScope.launch {
            try {
                val email = AppUtil.getSavedEmailPreference(requireContext())
                val phone = AppUtil.getSavedMobileNoPreference(requireContext())

                userPreferences.updateUserStateLgdCode(null)
                userPreferences.updateUserStateLgdCode(selectedStateLgdCode)
                AppUtil.saveStateCode(requireContext(), selectedStateCode)

                fun encrypt(value: String) = AESCryptography.encryptIntoBase64String(
                    value,
                    AppConstant.Constants.ENCRYPT_KEY,
                    AppConstant.Constants.ENCRYPT_IV_KEY
                )

                val request = UserCreationReq(
                    encrypt(currentAadhaar),
                    encrypt(name),
                    encrypt(gender),
                    encrypt(dob),
                    encrypt(state),
                    encrypt(selectedStateCode),
                    encrypt(dist),
                    encrypt(block),
                    encrypt(po),
                    encrypt(village),
                    encrypt(pinCode),
                    encrypt(phone),
                    encrypt(email),
                    encrypt(careOf),
                    encrypt(street),
                    BuildConfig.VERSION_NAME,
                    photo,
                    AppUtil.getAndroidId(requireContext()),
                    encrypt(selectedStateLgdCode),
                    true
                )

                userCreationRequestJson = GsonBuilder()
                    .setPrettyPrinting()
                    .serializeNulls()
                    .create()
                    .toJson(request)

                commonViewModel.getCreateUserAPI(request)
            } catch (e: Exception) {
                e.printStackTrace()
                hideProgressBar()
                toastShort("Unable to prepare user registration request.")
            }
        }
    }

    private fun collectInsertAadhaarTxnResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.insertAadhaarTxn) {
                when (it) {
                    is Resource.Loading -> {}
                    is Resource.Error -> {
                        hideProgressBar()
                        toastShort("Error in Aadhaar transaction API.")
                    }
                    is Resource.Success -> {
                        hideProgressBar()
                        it.data?.let { response ->
                            when (response.responseCode) {
                                200 -> toastShort(response.responseMsg)
                                301 -> toastShort("Please update from Play Store.")
                                else -> toastShort(response.responseDesc)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun collectUserCreationResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getuserCreation) { result ->
                when (result) {
                    is Resource.Loading -> {}
                    is Resource.Error -> {
                        hideProgressBar()
                        val error = JsonObject().apply {
                            addProperty("type", "error")
                            addProperty("code", result.error?.code)
                            addProperty("message", result.error?.message)
                        }
                        saveUserCreationApiLog(error)
                        toastShort("User creation failed, please try again.")
                    }
                    is Resource.Success -> {
                        hideProgressBar()
                        saveUserCreationApiLog(result.data)
                        val response = result.data ?: return@collectLatestLifecycleFlow
                        when (response.responseCode) {
                            200 -> {
                                response.wrappedList.forEach { item ->
                                    userPreferences.updateUserId(null)
                                    userPreferences.updateUserId(item.userId)
                                    AppUtil.saveTokenPreference(
                                        requireContext(),
                                        "Bearer " + item.appCode
                                    )
                                }
                                AppUtil.saveEmailPreference(requireContext(), "")
                                userPhotoUIADI?.let { bitmap ->
                                    showRegistrationSuccess(bitmap, name, gender, dob, careOf)
                                } ?: toastLong("Registration completed. Please login.")
                                toastLong("Your username and password have been sent to your email.")
                            }
                            301 -> showUpdateDialog()
                            333 -> toastShort(response.responseDesc)
                            else -> toastShort(response.responseDesc)
                        }
                    }
                }
            }
        }
    }

    private fun showRegistrationSuccess(
        bitmap: Bitmap,
        candidateName: String,
        candidateGender: String,
        candidateDob: String,
        candidateCareOf: String
    ) {
        val dialog = BottomSheetDialog(requireContext())

        val composeView = ComposeView(requireContext()).apply {
            setContent {
                RegistrationSuccessPopup(
                    bitmap = bitmap,
                    candidateName = candidateName,
                    candidateGender = candidateGender,
                    candidateDob = candidateDob,
                    candidateCareOf = candidateCareOf,
                    onContinue = {
                        AppUtil.saveLoginStatus(requireContext(), true)
                        dialog.dismiss()

                        findNavController().navigate(
                            RegisterFragmentDirections
                                .actionRegisterFragmentToMainHomePage()
                        )
                    }
                )
            }
        }

        dialog.setContentView(composeView)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)

        dialog.show()
    }

    private fun showUpdateDialog() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Update Available")
            .setMessage("A new version of the app is available. Please update to continue.")
            .setPositiveButton("Update") { dialog, _ ->
                val packageName = "com.kaushalpanjee"
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
                } catch (_: Exception) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$packageName&hl=en_IN")
                        )
                    )
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .show()
    }

    private fun saveUserCreationApiLog(response: Any?) {
        try {
            val gson = GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create()

            val requestJson: JsonElement = try {
                gson.fromJson(
                    userCreationRequestJson,
                    JsonElement::class.java
                ) ?: JsonObject()
            } catch (e: Exception) {
                JsonObject()
            }

            val responseJson: JsonElement =
                gson.toJsonTree(response)

            val combined = JsonObject().apply {
                add("request", requestJson)
                add("response", responseJson)
            }

            val finalJson = gson.toJson(combined)

            requireContext()
                .getSharedPreferences(
                    "app_preferences",
                    Context.MODE_PRIVATE
                )
                .edit()
                .putString("user_creation_api_log", finalJson)
                .apply()

            finalJson.copyToClipboard(requireContext())

            toastLong("User creation request and response saved and copied.")

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
