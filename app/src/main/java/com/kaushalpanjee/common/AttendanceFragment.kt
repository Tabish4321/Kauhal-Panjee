package com.kaushalpanjee.common

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.util.Base64
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.R
import com.kaushalpanjee.common.model.UidaiKycRequest
import com.kaushalpanjee.common.model.UidaiResp
import com.kaushalpanjee.common.model.request.AadhaarRekycReq
import com.kaushalpanjee.common.model.request.AdharDetailsReq
import com.kaushalpanjee.common.model.request.InsertOjtReq
import com.kaushalpanjee.common.model.response.IntentModel
import com.kaushalpanjee.common.model.response.IntentResponse
import com.kaushalpanjee.common.model.response.UserDetails
import com.kaushalpanjee.core.basecomponent.BaseFragment
import com.kaushalpanjee.core.util.AESCryptography
import com.kaushalpanjee.core.util.AppConstant
import com.kaushalpanjee.core.util.AppConstant.Constants.LANGUAGE
import com.kaushalpanjee.core.util.AppConstant.Constants.PRODUCTION
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.core.util.UserPreferences
import com.kaushalpanjee.core.util.decodeBase64
import com.kaushalpanjee.core.util.log
import com.kaushalpanjee.core.util.toastLong
import com.kaushalpanjee.core.util.toastShort
import com.kaushalpanjee.databinding.AttendanceFragmentBinding
import com.kaushalpanjee.model.kyc_resp_pojo.XstreamCommonMethods
import com.kaushalpanjee.model.kyc_resp_pojo.XstreamCommonMethods.respDecodedXmlToPojoAuth
import com.kaushalpanjee.uidai.capture.CaptureResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.getValue


class AttendanceFragment:
    BaseFragment<AttendanceFragmentBinding>(AttendanceFragmentBinding::inflate)  {

    private val commonViewModel: CommonViewModel by activityViewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val neededPermissions = arrayOf(Manifest.permission.CAMERA)

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
    private var batchId = ""
    private var workplaceId = ""
    private var employerId = ""
    private var checkIn = ""
    private var totalHours = ""
    private var checkOut = ""
    private var attendanceFlag = ""
    private var userAadhaarDetailsList: List<UserDetails> = mutableListOf()
    private var decryptedAadhaar = ""
    private var intentResponse: IntentResponse? = null
    private var startTime: Long = 0
    private var userPhotoUIADI: Bitmap? = null
    private var ekycImage: String = ""

    private var latitude: Double = 0.0
      private var longitude: Double = 0.0
      var radius: Float = 100f




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreferences = UserPreferences(requireContext())
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        init()
        initEKYC()

    }


    private fun init(){

        decryptedAadhaar = AESCryptography.decryptIntoString(
            AppUtil.getSavedAadhaarPreference(requireContext()),
            AppConstant.Constants.ENCRYPT_KEY,
            AppConstant.Constants.ENCRYPT_IV_KEY
        )

        listener()

        commonViewModel.getAadhaarListAPI(
            AdharDetailsReq
                (
                BuildConfig.VERSION_NAME,
                AppUtil.getAndroidId(requireContext()),
                userPreferences.getUseID()
            ), AppUtil.getSavedTokenPreference(requireContext())
        )


        collectAadharDetailsResponse()


    }
    private fun listener(){
        startClock()
        binding.tvCurrentDate.text= AppUtil.getCurrentDateForAttendance()

        binding.progressBackButton.setOnClickListener {
            findNavController().navigateUp()
        }



        binding.btnCheckIn.setOnClickListener {



            if (attendanceFlag=="checkin"){
                //for audit
                showProgressBar()
                invokeCaptureIntent()

            }

            else toastShort("Checkin Already marked")


        }

        binding.btnCheckOut.setOnClickListener {


            if (attendanceFlag=="checkout"){

                //for audit
                showProgressBar()
                invokeCaptureIntent()


            }


            else toastShort("Kindly mark check in First")




        }



    }



    private fun startClock() {
        lifecycleScope.launch {
            while (isAdded) { // Check if fragment is attached
                val currentTime = SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(Date())
                binding.currentTime.text = currentTime
                delay(1000) // Update every second
            }
        }
    }



    private fun showAlertGeoFancingDialog(context: Context, title: String, message: String) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ ->
            findNavController().navigateUp()
        }

        val dialog = builder.create()
        dialog.setCancelable(false)  // Prevent outside touch dismissal
        dialog.setCanceledOnTouchOutside(false) // Extra safety: disable outside clicks
        dialog.show()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun showBottomSheet(
        image: Bitmap?,
        name: String,
        gender: String,
        dateOfBirth: String,
        careOf: String
    ) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())

        // Inflate the layout
        val view = layoutInflater.inflate(R.layout.attendance_bottom_sheet, null)
        bottomSheetDialog.setContentView(view)

        // Prevent closing when tapping outside
        bottomSheetDialog.setCanceledOnTouchOutside(false)

        // Find views
        val imageView = view.findViewById<ImageView>(R.id.circleImageView)
        val nameView = view.findViewById<TextView>(R.id.attendancCandidateName)
        val genderView = view.findViewById<TextView>(R.id.attendancGender)
        val dobView = view.findViewById<TextView>(R.id.attendancCDob)
        val careOfView = view.findViewById<TextView>(R.id.attendancCareOf)
        val okButton = view.findViewById<TextView>(R.id.tvLogin)

        // Set data
        imageView.setImageBitmap(image)
        nameView.text = name
        genderView.text = gender
        dobView.text = dateOfBirth
        careOfView.text = careOf

        // Handle OK button click
        okButton.setOnClickListener {

            bottomSheetDialog.dismiss()
            findNavController().navigateUp()
        }

        // Handle back button press
        bottomSheetDialog.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                // Show a confirmation dialog before closing
                AlertDialog.Builder(requireContext())
                    .setTitle("Exit")
                    .setMessage("Do you want to close this screen?")
                    .setPositiveButton("Yes") { _, _ ->
                        bottomSheetDialog.dismiss()
                    }
                    .setNegativeButton("No", null)
                    .show()
                return@setOnKeyListener true
            }
            false
        }

        // Show the BottomSheetDialog
        bottomSheetDialog.show()
    }


    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1001
            )
        }
    }

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun getCurrentLocation(onLocationResult: (Location?) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            showSnackBar("❌ Location permission not granted")
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            onLocationResult(location)
        }.addOnFailureListener {
            onLocationResult(null)
        }
    }

    private fun isUserInsideGeofence(
        currentLocation: Location,
        lat: Double,
        lng: Double,
        radius: Float
    ): Boolean {
        val targetLocation = Location("").apply {
            latitude = lat
            longitude = lng
        }
        val distance = currentLocation.distanceTo(targetLocation)
        return distance <= radius
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


    private fun loadBase64Image(base64String: String?, imageView: ImageView) {
        if (base64String.isNullOrEmpty()) {
            return  // Avoid processing if the string is null or empty
        }

        try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

            // Set bitmap to ImageView
            imageView.setImageBitmap(bitmap)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }


    private fun collectAadharDetailsResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getAadhaarList) {
                when (it) {
                    is Resource.Loading -> showProgressBar()
                    is Resource.Error -> {
                        hideProgressBar()
                        it.error?.let { error ->
                            showSnackBar(error.message ?: "Unknown Error")
                        }
                    }

                    is Resource.Success -> {
                        hideProgressBar()
                        it.data?.let { getAadharDetailsRes ->
                            if (getAadharDetailsRes.responseCode == 200) {
                                userAadhaarDetailsList = getAadharDetailsRes.wrappedList

                                if (userAadhaarDetailsList.isNotEmpty()) {

                                        for (x in userAadhaarDetailsList) {


                                            val decryptedUserName =
                                                AESCryptography.decryptIntoString(
                                                    x.userName,
                                                    AppConstant.Constants.ENCRYPT_KEY,
                                                    AppConstant.Constants.ENCRYPT_IV_KEY
                                                ) ?: "N/A"




                                            val decryptedGender = AESCryptography.decryptIntoString(
                                                x.gender,
                                                AppConstant.Constants.ENCRYPT_KEY,
                                                AppConstant.Constants.ENCRYPT_IV_KEY
                                            ) ?: "N/A"

                                            val decryptedMobileNo =
                                                AESCryptography.decryptIntoString(
                                                    x.mobileNo,
                                                    AppConstant.Constants.ENCRYPT_KEY,
                                                    AppConstant.Constants.ENCRYPT_IV_KEY
                                                ) ?: "N/A"

                                            val decryptedDob = AESCryptography.decryptIntoString(
                                                x.dateOfBirth,
                                                AppConstant.Constants.ENCRYPT_KEY,
                                                AppConstant.Constants.ENCRYPT_IV_KEY
                                            ) ?: "N/A"

                                            val decryptedAddress =
                                                AESCryptography.decryptIntoString(
                                                    x.comAddress,
                                                    AppConstant.Constants.ENCRYPT_KEY,
                                                    AppConstant.Constants.ENCRYPT_IV_KEY
                                                ) ?: "N/A"

                                            val decryptedEmail = AESCryptography.decryptIntoString(
                                                x.emailId,
                                                AppConstant.Constants.ENCRYPT_KEY,
                                                AppConstant.Constants.ENCRYPT_IV_KEY
                                            ) ?: "N/A"



                                            // Set Data to UI

                                            binding.tvAaadharMobile.text =
                                                decryptedMobileNo
                                            binding.tvAaadharGender.text =
                                                decryptedGender
                                            binding.tvAaadharDob.text = decryptedDob

                                            binding.tvEmailMobile.text = decryptedEmail
                                            binding.tvWorkplace.text = x.workplaceName

                                            batchId = x.batchId
                                            workplaceId = x.workplaceId
                                            employerId = x.employerId


                                            checkIn = x.checkIn
                                            checkOut = x.checkOut
                                            totalHours = x.totalHours
                                            attendanceFlag = x.attendanceFlag

                                            binding.tvCheckInValue.text = x.checkIn
                                            binding.tvCheckOutValue.text = x.checkOut
                                            binding.tvTotalHoursValue.text = x.totalHours

                                            latitude = x.latitute.toDouble()
                                            longitude = x.longitute.toDouble()
                                           // radius = x.radius.toFloat()
                                            radius = 1000000000f


                                            binding.tvAadhaarName.text=decryptedUserName


                                            if (x.imagePath != null) {

                                                val bytes: ByteArray =
                                                    Base64.decode(x.imagePath, Base64.DEFAULT)
                                                val bitmap = BitmapFactory.decodeByteArray(
                                                    bytes,
                                                    0,
                                                    bytes.size
                                                )
                                                binding.circleImageView.setImageBitmap(
                                                    bitmap
                                                )
                                            }


                                            loadBase64Image(x.imagePath, binding.circleImageView)

                                            checkLocationPermission()

                                            getCurrentLocation { location ->
                                                if (location != null) {
                                                    val isInside = isUserInsideGeofence(location, latitude, longitude, radius)
                                                    // val isInside = isUserInsideGeofence(location, 26.2153, 84.3588, radius)
                                                    if (isInside) {




                                                    } else {
                                                        showAlertGeoFancingDialog(requireContext(),"Alert","❌ You are outside the institute area")

                                                    }
                                                } else {
                                                    toastLong("❌ Failed to retrieve current location")
                                                    showAlertGeoFancingDialog(requireContext(),"Alert","❌ Failed to retrieve current location Kindly on your gps from settings")
                                                }
                                            }


                                        }


                                } else {
                                    Log.e("AadhaarDetails", "List is empty!")
                                }
                            } else if (getAadharDetailsRes.responseCode == 401) {
                                AppUtil.showSessionExpiredDialog(
                                    findNavController(),
                                    requireContext()
                                )

                            } else {
                                showSnackBar("Something went wrong")
                            }
                        } ?: showSnackBar("Internal Server Error")
                    }
                }
            }
        }
    }


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
                    AppConstant.Constants.CRYPT_ID,AppConstant.Constants.CRYPT_IV

                )
            },
            IntentModel::class.java
        )


    }

    private fun invokeCaptureIntent() {

        try {
            val intent1 = Intent(AppConstant.Constants.CAPTURE_INTENT)
            intent1.putExtra(
                AppConstant.Constants.CAPTURE_INTENT_REQUEST,
                createPidOptions(getTransactionID(), "auth")
            )
            startUidaiAuthResult.launch(intent1)

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
            hideProgressBar()
            log("EKYCDATA", exp.toString())
        }

    }

    private fun getTransactionID(): String {
        val secureRandom = SecureRandom()
        return secureRandom.nextInt(9999).toString()
    }

    private fun createPidOptions(txnId: String, purpose: String): String {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<PidOptions ver=\"1.0\" env=\"$PRODUCTION\">\n" + "   <Opts fCount=\"\" fType=\"\" iCount=\"\" iType=\"\" pCount=\"\" pType=\"\" format=\"\" pidVer=\"2.0\" timeout=\"\" otp=\"\" wadh=\"${AppConstant.Constants.WADH_KEY}\" posh=\"\" />\n" + "   <CustOpts>\n" + "      <Param name=\"txnId\" value=\"${txnId}\"/>\n" + "      <Param name=\"purpose\" value=\"$purpose\"/>\n" + "      <Param name=\"language\" value=\"$LANGUAGE}\"/>\n" + "   </CustOpts>\n" + "</PidOptions>"
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
                    //decryptedAadhaar,
                    "939625617876",
                    false,
                    requireContext()
                )

                // Define Pre-Production URL (use a constant or environment configuration in production)
                //  val authURL = "http://10.247.252.95:8080/NicASAServer/ASAMain" //preProd
                val authURL = "http://10.247.252.93:8080/NicASAServer/ASAMain"  //Prod

                // Record the start time for elapsed time computation
                startTime = SystemClock.elapsedRealtime()

                // Post the processed data for Face Authentication
                commonViewModel.postOnAUAFaceAuthNREGA(
                    AppConstant.StaticURL.FACE_AUTH_UIADI,
                    UidaiKycRequest(poiType, authURL)
                )
                collectFaceAuthResponse()
                // Handle Aadhaar authentication or additional processing here if required
            } else {
                hideProgressBar()

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
        }
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


                                    log("EKYCDATA", kycResp.toString())

                                    if (kycResp.isSuccess) {
                                        val bytes: ByteArray =
                                            Base64.decode(kycResp.uidData.pht, Base64.DEFAULT)
                                        var bitmap =
                                            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                                        userPhotoUIADI = bitmap
                                        ekycImage = kycResp.uidData.pht ?: ""




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



                                        val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                                        val currentTime = LocalTime.now()
                                        val formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))  // ✅ 24-hour format\
                                        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")


                                        hideProgressBar()

                                        if (attendanceFlag== "checkin")
                                        {


                                            commonViewModel.insertOjtAttendance(
                                                InsertOjtReq(
                                                    BuildConfig.VERSION_NAME,
                                                    userPreferences.getUseID(),
                                                    batchId.toInt(),
                                                    workplaceId.toInt(),
                                                    employerId.toInt(),
                                                    AppUtil.getAndroidId(requireContext()),
                                                    formattedTime,
                                                    "",
                                                    currentDate,
                                                    "",
                                                    latitude.toString(),
                                                    longitude.toString(),
                                                    ""
                                                ),AppUtil.getSavedTokenPreference(requireContext()))
                                        }

                                        else{

                                            val checkInTime = LocalTime.parse(checkIn, timeFormatter)
                                            val checkOutTime = LocalTime.parse(formattedTime, timeFormatter)
                                            val duration = Duration.between(checkInTime, checkOutTime)


                                            val hours = duration.toHours()
                                            val minutes = (duration.toMinutes() % 60)
                                            val seconds = (duration.seconds % 60)

                                            val totalHoursValue = String.format("%02d:%02d:%02d", hours, minutes, seconds)


                                            commonViewModel.insertOjtAttendance(
                                                InsertOjtReq(
                                                    BuildConfig.VERSION_NAME,
                                                    userPreferences.getUseID(),
                                                    batchId.toInt(),
                                                    workplaceId.toInt(),
                                                    employerId.toInt(),
                                                    AppUtil.getAndroidId(requireContext()),
                                                    "",
                                                    formattedTime,
                                                    currentDate,
                                                    totalHoursValue,
                                                    latitude.toString(),
                                                    longitude.toString(),
                                                    ""
                                                ),AppUtil.getSavedTokenPreference(requireContext()))

                                        }

                                        collectInsertResponse()


                                    }
                                    else {
                                        hideProgressBar()
                                        val decodedRar = decodeBase64(kycResp.rar)
                                        decodedRar?.let { decodedRarParsed ->
                                            val authRes = respDecodedXmlToPojoAuth(decodedRarParsed)
                                            val errorDesc =
                                                XstreamCommonMethods.getAuthErrorDescription(authRes.info)
                                            log("EKYCDATA", errorDesc)

                                            toastShort("EKYCDATA: Failed")
                                            findNavController().navigateUp()
                                        } ?: toastShort("Getting Error")
                                    }
                                }
                                catch (e: Exception) {
                                    hideProgressBar()
                                    toastShort("going back2")
                                    findNavController().navigateUp()
                                    e.printStackTrace()
                                    log("EKYCDATA", "Error processing KYC response: ${e.message}")
                                    toastShort("Error processing KYC response")

                                }
                            } ?: toastShort("Server error from uidai. Please try again.")

                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                hideProgressBar()
                toastShort("going back4")
                findNavController().navigateUp()

                log("EKYCDATA", "Unhandled error: ${e.message}")
                toastShort("An unexpected error occurred. Please try again.")

            }
        }




    }

    private fun collectInsertResponse() {

        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.insertOjtAttendance) {
                when (it) {
                    is Resource.Loading -> {}
                    is Resource.Error -> {
                        it.error?.let { baseErrorResponse ->
                            showSnackBar(baseErrorResponse.message)
                            toastShort("error in create Api")
                        }
                    }

                    is Resource.Success -> {
                        it.data?.let { getInsertRes ->
                            if (getInsertRes.responseCode == 200) {

                                showSnackBar(getInsertRes.responseDesc)

                                showBottomSheet(userPhotoUIADI, name, gender, dob, careOf)

                                showSnackBar(getInsertRes.responseDesc)

                            }
                            else if (getInsertRes.responseCode == 301) {


                                //Update app
                                showUpdateDialog()

                            }
                            else
                                showSnackBar(getInsertRes.responseDesc)


                        } ?: showSnackBar("Internal Sever Error")
                    }
                }
            }
        }
    }

    private fun showUpdateDialog() {
        val builder = AlertDialog.Builder(requireContext()) // 🔥 use requireContext() inside Fragment
        builder.setTitle("Update Available")
        builder.setMessage("A new version of the app is available. Please update to continue.")

        builder.setPositiveButton("Update") { dialog, _ ->
            val appPackageName = "com.kaushalpanjee"
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
                intent.setPackage("com.android.vending")
                startActivity(intent)
            } catch (e: Exception) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName&hl=en_IN"))
                startActivity(intent)
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.setCancelable(false)
        builder.create().show()
    }

}


