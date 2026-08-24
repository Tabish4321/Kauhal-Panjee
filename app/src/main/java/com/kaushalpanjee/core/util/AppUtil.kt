package com.kaushalpanjee.core.util

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.util.Base64
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kaushalpanjee.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import java.util.TimeZone
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.Settings
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.d2k.samiksha.SamikshaSdk
import com.d2k.samiksha.model.ConsentRequest
import com.google.gson.Gson
import com.utilize.core.util.FileUtils.Companion.getFileName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import java.io.File
import java.security.MessageDigest
import java.security.SecureRandom

object AppUtil {

    fun showUpdateDialog(
        context: Context
    ) {

        val builder =
            android.app.AlertDialog.Builder(context)

        builder.setTitle("Update Available")

        builder.setMessage(
            "A new version of the app is available. Please update to continue."
        )

        builder.setPositiveButton("Update") { dialog, _ ->

            val appPackageName =
                "com.kaushalpanjee"

            try {

                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "market://details?id=$appPackageName"
                    )
                )

                intent.setPackage("com.android.vending")

                context.startActivity(intent)

            } catch (e: Exception) {

                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "https://play.google.com/store/apps/details?id=$appPackageName&hl=en_IN"
                    )
                )

                context.startActivity(intent)
            }

            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->

            dialog.dismiss()
        }

        builder.setCancelable(false)

        builder.create().show()
    }

    fun validatePAN(pan: String): Boolean {
        val regex = Regex("[A-Z]{5}[0-9]{4}[A-Z]{1}")
        return regex.matches(pan)
    }

    @SuppressLint("HardwareIds")
    fun getAndroidId(context: Context): String {

        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun getCurrentDateForAttendance(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMMM yyyy, EEEE", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun sha512Hash(input: String): String {
        val digest = MessageDigest.getInstance("SHA-512")
        val hashBytes = digest.digest(input.toByteArray(Charsets.UTF_8))

        // Convert bytes to hex string
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    fun createFileName(userId: Int?): String {
        return "${userId}_${System.currentTimeMillis()}.jpg"
    }

    // Add this function to your class
    fun convertUriToBase64(uri: Uri, context: Context): String {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    fun getTimeZone(): String {
        return TimeZone.getDefault().id
    }

    fun getTimeZoneOffset(): Int {
        val offset: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ZonedDateTime.now().offset.totalSeconds / 60
        } else {
            val tz = TimeZone.getDefault()
            val cal = GregorianCalendar.getInstance(tz)
            tz.getOffset(cal.timeInMillis) / 1000 * 60
        }
        return offset
    }

    fun getAndroidDeviceInfo(): String {
        return "MODEL : ${Build.MODEL}, MANUFACTURER : ${Build.MANUFACTURER}, DEVICE : ${Build.DEVICE}"
    }

    fun getProgressDialog(context: Context?): AlertDialog? {
        if (context == null) return null
        return MaterialAlertDialogBuilder(context)
            .setView(R.layout.layout_progress)
            .setBackground(ColorDrawable(Color.TRANSPARENT))
            .setCancelable(false)
            .create()
    }


    fun changeAppLanguage(context: Context, languageCode: String) {
        val locale = Locale(languageCode) // For example, "en" for English, "es" for Spanish, etc.
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale) // Set the locale for the app

        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    }

    fun getLoginStatus(context: Context): Boolean {
        // Get the SharedPreferences instance
        val sharedPreferences =
            context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        // Retrieve the login status (false is the default value if not found)
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }


    fun saveLoginStatus(context: Context, isLoggedIn: Boolean) {
        // Get the SharedPreferences instance
        val sharedPreferences =
            context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        // Save the login status
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.apply()  // Use apply() for asynchronous saving
    }


    fun saveStateCode(context: Context, stateCode: String) {
        val sharedPreferences = context.getSharedPreferences("STATE_CODE", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("STATE_CODE", stateCode)
        editor.apply()
    }

    fun getStateCode(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("STATE_CODE", Context.MODE_PRIVATE)
        return sharedPreferences.getString("STATE_CODE", "N/A") ?: "N/A" // Default to English
    }

    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
        }
        return false
    }


    fun formatScheduleTimeIntoDateTime(dateTimeString: String): Pair<String, String> {
        // Parse the input string into a ZonedDateTime object
        val zonedDateTime = ZonedDateTime.parse(dateTimeString)

        // Define the formatter for the date part
        val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.getDefault())

        // Format the date part
        val formattedDate = zonedDateTime.format(dateFormatter)

        // Define the formatter for the time part
        val timeFormatter = DateTimeFormatter.ofPattern("hh:mma", Locale.ENGLISH)

        // Format the time part
        val formattedTime = zonedDateTime.format(timeFormatter).lowercase()

        return Pair(formattedDate, formattedTime)
    }


    fun parseDateTime(dateString: String, timeString: String): String {
        // Define the formatter for parsing the input date
        val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
        // Parse the date string into a LocalDateTime object
        val datePart = LocalDateTime.parse(dateString, dateFormatter)

        // Define the formatter for parsing the input time
        val timeFormatter = DateTimeFormatter.ofPattern("h : mm a", Locale.ENGLISH)
        // Parse the time string into a LocalDateTime object
        val timePart = LocalDateTime.parse(timeString, timeFormatter)

        // Combine the date and time parts
        val combinedDateTime = datePart.withHour(timePart.hour).withMinute(timePart.minute)

        // Format the combined date and time to ISO format with 'Z' to indicate UTC time
        val isoDateTime =
            combinedDateTime.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT)

        return isoDateTime
    }


    fun formatUtcDateTimeIntoReminderDate(input: String): String {
        // Parse the input date-time string
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val parsedDateTime = LocalDateTime.parse(input, inputFormatter)

        // Define the output formatter
        val outputFormatter =
            DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mma", Locale.ENGLISH)

        // Format the parsed date-time
        val formattedDateTime = parsedDateTime.format(outputFormatter)

        return formattedDateTime
    }

    fun formatUtcDateTimeIntoReminderDateNew(input: String): String {
        // Parse the input date-time string without milliseconds and timezone
        val inputFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH)
        val parsedDateTime = ZonedDateTime.parse(input, inputFormatter)

        // Define the output formatter (without timezone information)
        val outputFormatter =
            DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mma", Locale.ENGLISH)

        // Format the parsed date-time
        val formattedDateTime = parsedDateTime.format(outputFormatter)

        return formattedDateTime
    }


    fun formatReminderDateIntoUtcDateTime(input: String): String {

        val inputFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy, hh:mma", Locale.ENGLISH)
        val parsedDateTime = LocalDateTime.parse(input, inputFormatter)

        // Convert the local date-time to a ZonedDateTime in the system default time zone
        val localZonedDateTime = parsedDateTime.atZone(ZoneId.systemDefault())

        // Convert the ZonedDateTime to UTC
        val utcZonedDateTime = localZonedDateTime.withZoneSameInstant(ZoneId.of("UTC"))

        // Define the output formatter
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

        // Format the parsed date-time
        val formattedDateTime = utcZonedDateTime.format(outputFormatter)

        return formattedDateTime
    }

    fun combineDateTime(date: String, time: String): String {
        // Parse the date and time strings
        val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("hh:mma")

        val parsedDate = LocalDate.parse(date, dateFormatter)
        val parsedTime = LocalTime.parse(time, timeFormatter)

        // Combine date and time
        val dateTime = parsedDate.atTime(parsedTime)

        // Define the output formatter
        ///val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        ///val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)

        // Format the combined date-time
        val formattedDateTime = dateTime.format(outputFormatter)

        return formattedDateTime//.toLowerCase()  // Convert AM/PM to lowercase as per your requirement
    }

    fun formatTimeToLowercaseAMPM(inputTime: String): String {
        // Check if the input time ends with "AM" or "PM"
        return when {
            inputTime.endsWith("AM", ignoreCase = true) -> inputTime.replace("AM", "am")
            inputTime.endsWith("PM", ignoreCase = true) -> inputTime.replace("PM", "pm")
            else -> inputTime // Return the original string if it doesn't end with AM or PM
        }
    }

    fun convertUTCtoIST(utcFormat: String, istFormat: String, dateToFormat: String): String {
        val utcFormat: DateFormat = SimpleDateFormat(utcFormat)
        utcFormat.timeZone = TimeZone.getTimeZone("GMT")

        val indianFormat: DateFormat = SimpleDateFormat(istFormat)
        utcFormat.timeZone = TimeZone.getTimeZone("IST")

        val timestamp: Date = utcFormat.parse(dateToFormat)
        return indianFormat.format(timestamp)
    }


    fun getCurrentDateTime(): String {
        val calendar = Calendar.getInstance();
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return dateFormat.format(calendar.getTime());

    }


    fun saveLanguagePreference(context: Context, languageCode: String) {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("language_code", languageCode)
        editor.apply()
    }

    fun getSavedLanguagePreference(context: Context): String {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("language_code", "en") ?: "en" // Default to English
    }


    fun saveTokenPreference(context: Context, tokenCode: String) {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token_code", tokenCode)
        editor.apply()
    }

    fun getSavedTokenPreference(context: Context): String {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token_code", "") ?: "" // Default to English
    }




    fun saveOtpTokenPreference(context: Context, tokenCode: String) {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token_otp_code", tokenCode)
        editor.apply()
    }

    fun getSavedOtpTokenPreference(context: Context): String {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token_otp_code", "") ?: "" // Default to English
    }







    fun saveAadhaarPreference(context: Context, tokenCode: String) {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("aadhaar_code", tokenCode)
        editor.apply()
    }

    fun getSavedAadhaarPreference(context: Context): String {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("aadhaar_code", "") ?: "" // Default to English
    }


    fun saveMobileNoPreference(context: Context, mobileNo: String) {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("mobile_no", mobileNo)
        editor.apply()
    }

    fun getSavedMobileNoPreference(context: Context): String {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("mobile_no", "") ?: "" // Default to English
    }


    fun saveEmailPreference(context: Context, email: String) {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.apply()
    }

    fun getSavedEmailPreference(context: Context): String {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("email", "") ?: "" // Default to English
    }


    inline fun <reified T> fromJson(json: String): T {
        val gson = Gson()
        return gson.fromJson(json, T::class.java)
    }

    fun <T> toJson(model: T): String {
        val gson = Gson()
        return gson.toJson(model)
    }

    fun generateOTP(): Int {
        val secureRandom = SecureRandom()
        return secureRandom.nextInt(9000) + 1000 // Ensures a 4-digit number (1000 - 9999)
    }

    private var isSessionDialogShown = false // Flag to prevent multiple dialogs

    fun showSessionExpiredDialog(navController: NavController, context: Context) {
        if (isSessionDialogShown) return // Prevent showing multiple dialogs

        isSessionDialogShown = true // Set flag to true when dialog is shown

        val builder = androidx.appcompat.app.AlertDialog.Builder(context)
        builder.setTitle("Session Expired")
        builder.setMessage("Your session has expired. Please log in again.")
        builder.setCancelable(false) // Prevent dismissing on outside touch or back press

        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            logoutUser(navController, context)
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun saveBackgroundTime(context: Context, time: Long) {
        context.getSharedPreferences("session", Context.MODE_PRIVATE)
            .edit()
            .putLong("background_time", time)
            .apply()
    }

    fun getBackgroundTime(context: Context): Long {
        return context.getSharedPreferences("session", Context.MODE_PRIVATE)
            .getLong("background_time", 0L)
    }

    fun logoutUser(navController: NavController, context: Context) {
        // Clear user session data
        AppUtil.saveLoginStatus(context, false)

        // Navigate to login and reset the flag after navigation
        navController.navigate(
            R.id.loginFragment,
            null,
            NavOptions.Builder()
                .setPopUpTo(navController.graph.startDestinationId, true) // Clear everything
                .build()
        )

        isSessionDialogShown = false // Reset flag after navigation
    }

    //  sha256/KY00gO3RItl8kWF7tuMBl13Q4kXD+pZanVHy6o1XR1c= , sha256/AlSQhgtJirc8ahLyekmtX+Iw+v46yPYRLJt9Cq1GlB0=
    fun printSslPin() {
        val client = OkHttpClient()
        val request = okhttp3.Request.Builder()
            .url("https://kaushal.rural.gov.in/")
            .build()
        Thread {
            try {
                val response = client.newCall(request).execute()
                val handshake = response.handshake
                val certs = handshake?.peerCertificates
                certs?.forEach {
                    val pin = okhttp3.CertificatePinner.pin(it)
                    println("SSL PIN 👉 $pin")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }


    fun base64ToBitmap(base64: String): Bitmap? {
        return try {
            val cleanBase64 = base64.substringAfter(",") // handles data:image/png;base64,...
            val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    fun uriToBase64(
        context: Context,
        uri: Uri
    ): String {

        return try {

            val input =
                context.contentResolver.openInputStream(uri)

            val bytes = input?.readBytes()

            input?.close()

            android.util.Base64.encodeToString(
                bytes,
                android.util.Base64.NO_WRAP
            )

        } catch (e: Exception) {

            ""

        }

    }


    //     Ajit Ranjan some Data use in 21/07/2026
    private val _examStarted = MutableStateFlow(false)
    val examStarted: StateFlow<Boolean> = _examStarted

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    private val _timeLeft = MutableStateFlow(1800) // 30 minutes in seconds
    //     private val _timeLeft = MutableStateFlow(60)
//      private val _timeLeft = MutableStateFlow(15)


    private val _examFinished = MutableStateFlow(false)


    private val _showReviewDialog = MutableStateFlow(false)


    private val _reviewQuestions = MutableStateFlow<Set<String>>(emptySet())
    private val _showSuccessDialog = MutableStateFlow(false)
    private val _editMode = MutableStateFlow(false)
    val editMode: StateFlow<Boolean> = _editMode
    private val _submissionLoading = MutableStateFlow(false)
    val submissionLoading: StateFlow<Boolean> = _submissionLoading

    private val _submissionError = MutableStateFlow<String?>(null)
    val submissionError: StateFlow<String?> = _submissionError

    // Answer tracking
    private val _answers = MutableStateFlow<Map<String, String>>(emptyMap())
    val answers: StateFlow<Map<String, String>> = _answers

    private val _markedQuestions = MutableStateFlow<Set<String>>(emptySet())
    val markedQuestions: StateFlow<Set<String>> = _markedQuestions

    //    val timeLeft: StateFlow<Int> = _timeLeft
//    val examFinished: StateFlow<Boolean> = _examFinished
    private val _questionStatus = MutableStateFlow<Map<String, String>>(emptyMap())
//    private var timeLeft = MutableStateFlow(60) // Example: 60 seconds
//    private var examFinished = MutableStateFlow(false)
//    private var showSuccessDialog = MutableStateFlow(false)


    val timeLeft = MutableStateFlow(60)
    val examFinished = MutableStateFlow(false)
    val showSuccessDialog = MutableStateFlow(false)


    // UI Event Methods
    fun startExam() {
        _examStarted.value = true
    }

    fun nextQuestion(maxQuestions: Int) {
        if (_currentIndex.value < maxQuestions - 1) {
            _currentIndex.value += 1
        }
    }

    fun previousQuestion() {
        if (_currentIndex.value > 0) {
            _currentIndex.value -= 1
        }
    }

    fun goToQuestion(index: Int) {
        _currentIndex.value = index
    }

    fun clearSubmissionError() {
        _submissionError.value = null
    }


    fun closeReviewDialog() {
        _showReviewDialog.value = false
    }

    fun closeSuccessDialog() {
        _showSuccessDialog.value = false
    }

    // Answer Management
    fun selectAnswer(questionId: String, optionKey: String) {
        val currentAnswers = _answers.value.toMutableMap()
        currentAnswers[questionId] = optionKey
        _answers.value = currentAnswers
    }

    fun clearAnswer(questionId: String) {
        val currentAnswers = _answers.value.toMutableMap()
        currentAnswers.remove(questionId)
        _answers.value = currentAnswers

        val currentStatus = _questionStatus.value.toMutableMap()
        currentStatus.remove(questionId)
        _questionStatus.value = currentStatus
    }

    fun markQuestion(questionId: String) {
        val currentMarked = _markedQuestions.value.toMutableSet()
        if (currentMarked.contains(questionId)) {
            currentMarked.remove(questionId)
        } else {
            currentMarked.add(questionId)
        }
        _markedQuestions.value = currentMarked
    }


    fun ReviewQuestion(questionId: String) {

        val currentReview = _reviewQuestions.value.toMutableSet()

        if (currentReview.contains(questionId)) {
            currentReview.remove(questionId)
        } else {
            currentReview.add(questionId)
        }

        _reviewQuestions.value = currentReview
    }


    fun saveAndNext(questionId: String, actionText: String, maxQuestions: Int) {
        val currentStatus = _questionStatus.value.toMutableMap()
        currentStatus[questionId] = actionText
        _questionStatus.value = currentStatus

        if (_currentIndex.value < maxQuestions - 1) {
            _currentIndex.value += 1
        }
    }

    // Timer Management
    fun startTimer(
        scope: CoroutineScope,
        timeLeft: MutableStateFlow<Int>,
        examFinished: MutableStateFlow<Boolean>,
        showSuccessDialog: MutableStateFlow<Boolean>
    ) {
        scope.launch(Dispatchers.Default) {
            while (!examFinished.value && timeLeft.value > 0) {
                delay(1000)
                timeLeft.value--
            }

            if (timeLeft.value == 0) {
                examFinished.value = true
                showSuccessDialog.value = true
            }
        }
    }


    fun uriToFile(
        context: Context,
        uri: Uri
    ): File? {

        return try {

            val contentResolver = context.contentResolver

            val fileName = getFileName(
                context,
                uri
            )

            val file = File(
                context.cacheDir,
                fileName
            )

            contentResolver.openInputStream(uri)?.use { inputStream ->

                file.outputStream().use { outputStream ->

                    inputStream.copyTo(outputStream)
                }
            }

            file

        } catch (e: Exception) {

            e.printStackTrace()

            null
        }
    }

}

