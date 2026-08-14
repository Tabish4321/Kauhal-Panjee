package com.kaushalpanjee.core.basecomponent













import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.kaushalpanjee.R
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.UserPreferences
import com.kaushalpanjee.core.util.log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

abstract class BaseFragment<VB : ViewBinding>(private val bindingInflater: (inflater: LayoutInflater) -> VB) :
    Fragment() {
    private var progress1: Dialog? = null

    private var _binding: VB? = null
    val binding: VB get() = _binding as VB
    private val progress: AlertDialog? by lazy {
        AppUtil.getProgressDialog(context)
    }

    private var baseActivity: BaseActivity<VB>? = null
    fun getActivityContext(): BaseActivity<VB>? = baseActivity
    private var loadingCount = 0

    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater)
        if (_binding == null)
            throw IllegalArgumentException("Binding cannot be null")

        // 🔹 Prevent screenshots and screen recording for all fragments
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.baseActivity = activity as BaseActivity<VB>
    }

 /*   fun showProgressBar() {
        if (context != null && isAdded && progress?.isShowing == false) {
            progress?.show()
        }
    }

    fun hideProgressBar() {
        if (progress?.isShowing == true) {
            progress?.dismiss()
        }
    }*/


    fun showProgressBar() {
        if (!isAdded) return
        val act = activity ?: return
        if (act.isFinishing || act.isDestroyed) return

        initProgress()

        if (progress?.isShowing != true) {
            act.runOnUiThread {
                progress?.show()
            }
        }
    }
    fun hideProgressBar() {
        val act = activity ?: return
        if (act.isFinishing || act.isDestroyed) return

        if (progress?.isShowing == true) {
            act.runOnUiThread {
                progress?.dismiss()
            }
        }
    }


    fun showSnackBar(message: String) {
        try {
            val safeMessage = message ?: return

            val snackBar = Snackbar.make(binding.root, safeMessage, Snackbar.LENGTH_SHORT)
            snackBar.view.setPadding(0, 0, 0, 0)
            snackBar.view.elevation = 0f
            snackBar.view.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.shape_rectangle_grey)
            snackBar.show()
        }catch (e: Exception){}

    }

    fun hideSoftKeyboard() {
        if (requireActivity().currentFocus == null) {
            return
        }
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }

    fun hideKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            requireActivity().window.currentFocus?.windowToken,
            0
        )
    }

    suspend fun <T> collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
            flow.collectLatest(collect)
        }
    }

    fun compressImageFile(file: File): File? {
        var compressedFile: File? = null
        if (!file.exists()) file.mkdirs()
        val actualSize = file.length() / 1024
        log("ActualSizeofFile :", "$actualSize KB")

        val bitmap = BitmapFactory.decodeFile(file.path)
        val compressedBitmap: Bitmap = getResizedBitmap(bitmap)
        if (compressedBitmap != null) {
            compressedFile = bitmapToFile(requireActivity(), compressedBitmap)
            if (compressedFile != null) {
                val compressedSize = compressedFile.length() / 1024
                log("ActualSizeofFile :", "$compressedSize KB")
            }
        }
        return compressedFile
    }

    private fun bitmapToFile(activity: Activity, bitmap: Bitmap): File? {
        var file: File? = null
        try {
            file = File(
                activity.externalCacheDir.toString() + File.separator
                        + "/" + System.currentTimeMillis() + ".jpg"
            )
            file.createNewFile()
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // You can also save it in JPEG
            val bitmapData = bos.toByteArray()
            val fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
        } catch (e: java.lang.Exception) {
            log("BitmapToFileExp", e.toString())
        }
        return file
    }

    private fun getResizedBitmap(bitmap: Bitmap, maxSize: Int = 500): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        height = maxSize
        width = (height * bitmapRatio).toInt()
        log("ProfilePicWidthAndHeight", "height: $height, width: $width")

        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }




    private fun initProgress() {
        if (progress1 == null && activity != null) {
            progress1 = Dialog(requireActivity()).apply {
                setContentView(R.layout.layout_progress)
                setCancelable(false)
            }
        }
    }

}







////import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
//
//import com.kaushalpanjee.R
//import android.app.Activity
//import android.app.AlertDialog
//import android.app.Dialog
//import android.content.Context
//import android.content.Intent
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.net.Uri
//import android.os.Bundle
//import android.util.Base64
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.view.inputmethod.InputMethodManager
//import android.widget.Toast
//import androidx.annotation.StringRes
//import androidx.core.content.ContextCompat
//import androidx.core.content.FileProvider
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.Lifecycle
//import androidx.lifecycle.lifecycleScope
//import androidx.lifecycle.repeatOnLifecycle
//import androidx.navigation.fragment.findNavController
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import androidx.viewbinding.ViewBinding
//import com.google.android.material.snackbar.Snackbar
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.collectLatest
//import kotlinx.coroutines.launch
//import java.io.ByteArrayOutputStream
//import java.io.File
//import java.io.FileOutputStream
//import javax.inject.Inject
//import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
//import com.kaushalpanjee.core.util.AppUtil
//import com.kaushalpanjee.core.util.UserPreferences
//
//
//abstract class BaseFragment<VB : ViewBinding>(
//    private val bindingInflater: (inflater: LayoutInflater) -> VB
//) : Fragment()
//{
//
//    private var _binding: VB? = null
//    protected val binding: VB
//        get() = _binding ?: throw IllegalStateException("Binding is not available")
//
//    // BaseActivity reference
//    private var baseActivity: BaseActivity<*>? = null
//    fun getActivityContext(): BaseActivity<*>? = baseActivity
//
//    @Inject
//    lateinit var userPreferences: UserPreferences
//
//    // Progress dialog
//    private var progressDialog: AlertDialog? = null
//    private var progress1: Dialog? = null
//
//    // RecyclerView tracking
////    protected val recyclerViewHelpers = mutableMapOf<Int, RecyclerViewHelper<*>>()
//
//    // Swipe refresh tracking
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        baseActivity = activity as? BaseActivity<*>
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = bindingInflater(inflater)
//
//        requireActivity().window.setSoftInputMode(SOFT_INPUT_ADJUST_RESIZE)
//
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        logFragmentEvent("Fragment_Created", this::class.java.simpleName)
//
//        initializeViews()
//        setupObservers()
//        setupClickListeners()
//        loadInitialData()
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        dismissProgressDialog()
//        hideProgressBar()
////        recyclerViewHelpers.clear()
//        _binding = null
//    }
//
//    // ==================== ABSTRACT METHODS ====================
//
//    abstract fun initializeViews()
//    abstract fun setupObservers()
//    abstract fun setupClickListeners()
//    abstract fun loadInitialData()
//
//    // ==================== PROGRESS ====================
//
//    /**
//     * Old BaseFragment progress bar support
//     */
//    fun showProgressBar() {
//        if (!isAdded) return
//        val act = activity ?: return
//        if (act.isFinishing || act.isDestroyed) return
//
//        initProgress()
//
//        if (progress1?.isShowing != true) {
//            act.runOnUiThread {
//                progress1?.show()
//            }
//        }
//    }
//
//    fun hideProgressBar() {
//        val act = activity ?: return
//        if (act.isFinishing || act.isDestroyed) return
//
//        if (progress1?.isShowing == true) {
//            act.runOnUiThread {
//                progress1?.dismiss()
//            }
//        }
//    }
//
//    private fun initProgress() {
//        if (progress1 == null && activity != null) {
//            progress1 = Dialog(requireActivity()).apply {
//                setContentView(R.layout.layout_progress)
//                setCancelable(false)
//            }
//        }
//    }
//
//    /**
//     * New BaseFragment progress dialog support
//     * Agar aap ModernProgressDialog use karna chahte ho to isko use karo.
//     */
//
//    protected fun dismissProgressDialog() {
//        try {
//            progressDialog?.dismiss()
//            progressDialog = null
//        } catch (e: Exception) {
//            logCrashlyticsError("dismissProgressDialog", e)
//        }
//    }
//
//    protected fun isProgressShowing(): Boolean {
//        return progressDialog?.isShowing == true || progress1?.isShowing == true
//    }
//
//    // ==================== SNACKBAR ====================
//
//    fun showSnackBar(message: String) {
//        try {
//            val snackBar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
//            snackBar.view.setPadding(0, 0, 0, 0)
//            snackBar.view.elevation = 0f
//            snackBar.view.background =
//                ContextCompat.getDrawable(requireContext(), R.drawable.shape_rectangle_grey)
//            snackBar.show()
//        } catch (e: Exception) {
//            logCrashlyticsError("showSnackBar", e)
//        }
//    }
//
//    // ==================== KEYBOARD HELPERS ====================
//
//    fun hideSoftKeyboard() {
//        try {
//            val currentFocus = requireActivity().currentFocus ?: return
//            val inputMethodManager =
//                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
//        } catch (e: Exception) {
//            logCrashlyticsError("hideSoftKeyboard", e)
//        }
//    }
//
//    fun hideKeyboard() {
//        try {
//            val inputMethodManager =
//                requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
//            inputMethodManager.hideSoftInputFromWindow(
//                requireActivity().window.currentFocus?.windowToken,
//                0
//            )
//        } catch (e: Exception) {
//            logCrashlyticsError("hideKeyboard", e)
//        }
//    }
//
//    // ==================== FLOW COLLECTOR ====================
//
//    suspend fun <T> collectLatestLifecycleFlow(
//        flow: Flow<T>,
//        collect: suspend (T) -> Unit
//    ) {
//        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
//            flow.collectLatest(collect)
//        }
//    }
//
//    // ==================== IMAGE COMPRESS HELPERS ====================
//
//    fun compressImageFile(file: File): File? {
//        var compressedFile: File? = null
//        try {
//            if (!file.exists()) return null
//
//            val bitmap = BitmapFactory.decodeFile(file.path) ?: return null
//            val compressedBitmap = getResizedBitmap(bitmap)
//
//            compressedFile = bitmapToFile(requireActivity(), compressedBitmap)
//        } catch (e: Exception) {
//            logCrashlyticsError("compressImageFile", e)
//        }
//        return compressedFile
//    }
//
//    private fun bitmapToFile(activity: Activity, bitmap: Bitmap): File? {
//        return try {
//            val file = File(
//                activity.externalCacheDir.toString() + File.separator +
//                        "${System.currentTimeMillis()}.jpg"
//            )
//            file.createNewFile()
//
//            val bos = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos)
//            val bitmapData = bos.toByteArray()
//
//            FileOutputStream(file).use { fos ->
//                fos.write(bitmapData)
//                fos.flush()
//            }
//            file
//        } catch (e: Exception) {
//            logCrashlyticsError("bitmapToFile", e)
//            null
//        }
//    }
//
//    private fun getResizedBitmap(bitmap: Bitmap, maxSize: Int = 500): Bitmap {
//        val width = bitmap.width
//        val height = bitmap.height
//        val bitmapRatio = width.toFloat() / height.toFloat()
//
//        val finalHeight = maxSize
//        val finalWidth = (finalHeight * bitmapRatio).toInt()
//
//        return Bitmap.createScaledBitmap(bitmap, finalWidth, finalHeight, true)
//    }
//
//    // ==================== SWIPE REFRESH SUPPORT ====================
//
//
//
//    // ==================== FIREBASE CRASHLYTICS SUPPORT ====================
//
//    protected fun logCrashlyticsError(methodName: String, exception: Exception) {
//        // FirebaseCrashlytics logging if needed
//    }
//
//    protected fun logFragmentEvent(eventName: String, fragmentName: String? = null) {
//        // FirebaseCrashlytics event logging if needed
//    }
//
//    protected fun setUserIdentifier(userId: String) {
//        // FirebaseCrashlytics set user id
//    }
//
//    protected fun setCustomKey(key: String, value: String) {
//        // FirebaseCrashlytics custom key
//    }
//
//    protected fun logNetworkCall(url: String, method: String = "GET") {
//        // FirebaseCrashlytics network call logging
//    }
//
//    // ==================== RECYCLERVIEW SUPPORT ====================
//
//
//
//
//
//
//    // ==================== UTILITY METHODS ====================
//
//    protected fun showToast(message: String) {
//        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
//    }
//
//    protected fun showLongToast(message: String) {
//        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
//    }
//
//    protected fun showSuccessToast(message: String) {
//        showToast(message)
//    }
//
//    protected fun showErrorToast(message: String) {
//        showToast(message)
//        logCrashlyticsError("showErrorToast", Exception(message))
//    }
//
//    protected fun <T> handleApiResponse(
//        responseCode: Int,
//        data: T? = null,
//        onSuccess: ((T?) -> Unit)? = null,
//        onNoData: (() -> Unit)? = null,
//        onUpgradeRequired: (() -> Unit)? = null,
//        onSessionExpired: (() -> Unit)? = null,
//        onCustomResponse: ((Int) -> Unit)? = null
//    ) {
//        try {
//            when (responseCode) {
//                200 -> onSuccess?.invoke(data)
//                202 -> onNoData?.invoke() ?: showToast("No data available.")
//                301 -> onUpgradeRequired?.invoke() ?: showToast("Please upgrade your app.")
//                401 -> onSessionExpired?.invoke() ?: handleSessionExpired()
//                else -> onCustomResponse?.invoke(responseCode)
//                    ?: showToast("Unexpected response: $responseCode")
//            }
//        } catch (e: Exception) {
//            logCrashlyticsError("handleApiResponse", e)
//        }
//    }
//
//    protected fun handleSessionExpired() {
//        try {
//            AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
//        } catch (e: Exception) {
//            logCrashlyticsError("handleSessionExpired", e)
//            showSessionExpiredDialogFallback()
//        }
//    }
//
//    private fun showSessionExpiredDialogFallback() {
//        AlertDialog.Builder(requireContext())
//            .setTitle("Session Expired")
//            .setMessage("Your session has expired. Please login again.")
//            .setCancelable(false)
//            .setPositiveButton("OK") { dialog, _ ->
//                dialog.dismiss()
//                AppUtil.logoutUser(findNavController(), requireContext())
//            }
//            .show()
//    }
//
//
//
//
//    // ==================== VIEW HELPERS ====================
//
//    protected fun View.show() {
//        visibility = View.VISIBLE
//    }
//
//    protected fun View.hide() {
//        visibility = View.GONE
//    }
//
//    protected fun View.invisible() {
//        visibility = View.INVISIBLE
//    }
//
//    protected fun View.isVisible(): Boolean = visibility == View.VISIBLE
//    protected fun View.isGone(): Boolean = visibility == View.GONE
//
//    // ==================== SAFE CONTEXT ====================
//
//    protected fun safeContext(): Context? {
//        return if (isAdded) requireContext() else null
//    }
//
//    // ==================== COROUTINE HELPERS ====================
//
//    protected fun launchIO(block: suspend () -> Unit) {
//        lifecycleScope.launch(Dispatchers.IO) {
//            try {
//                block()
//            } catch (e: Exception) {
//                logCrashlyticsError("launchIO", e)
//            }
//        }
//    }
//
//    protected fun launchMain(block: suspend () -> Unit) {
//        lifecycleScope.launch(Dispatchers.Main) {
//            try {
//                block()
//            } catch (e: Exception) {
//                logCrashlyticsError("launchMain", e)
//            }
//        }
//    }
//
//    // ==================== VALIDATION HELPERS ====================
//
//    protected fun String?.isValid(): Boolean {
//        return !this.isNullOrBlank() && this != "null" && this != "N/A"
//    }
//
//    protected fun String?.toSafeInt(default: Int = 0): Int {
//        return this?.toIntOrNull() ?: default
//    }
//
//    protected fun String?.toSafeDouble(default: Double = 0.0): Double {
//        return this?.toDoubleOrNull() ?: default
//    }
//
//    protected fun safeText(value: String?): String {
//        return if (value.isNullOrBlank() || value.equals("null", ignoreCase = true)) {
//            "N/A"
//        } else value
//    }
//
//    // ==================== INNER CLASSES ====================
//
//
//
////    private class SwipeRefreshHelper(
////        private val swipeRefreshLayout: SwipeRefreshLayout,
////        private val onRefresh: () -> Unit
////    ) {
////        fun setRefreshing(refreshing: Boolean) {
////            swipeRefreshLayout.isRefreshing = refreshing
////        }
////    }
//
//    // ================= TOOLBAR SUPPORT =================
//
//
//
//    fun getStrings(resId: Int, vararg args: Any): String {
//        return requireContext().getString(resId, *args)
//    }
//}
//
//// ==================== NO DATA CONFIG ====================
//
//data class NoDataConfig(
//    val title: String? = null,
//    val description: String? = null,
//    val iconRes: Int? = null
//)