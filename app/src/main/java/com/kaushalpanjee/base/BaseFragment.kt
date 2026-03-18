package com.kaushalpanjee.base

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.kaushalpanjee.R
import com.kaushalpanjee.core.util.AppUtil
//import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> VB
) : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding ?: throw IllegalStateException("Binding is not available")

    // Modern progress dialog
    private var progressDialog: Dialog? = null

    // RecyclerView tracking
    protected val recyclerViewHelpers = mutableMapOf<Int, RecyclerViewHelper<*>>()

    // Swipe refresh tracking
    private val swipeRefreshHelpers = mutableMapOf<Int, SwipeRefreshHelper>()

    // Crashlytics
    //protected val crashlytics: FirebaseCrashlytics by lazy { FirebaseCrashlytics.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater(inflater)

        requireActivity().window.setSoftInputMode(
            SOFT_INPUT_ADJUST_RESIZE
        )
        //Log.d("FRAGMENT NAME", "")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Log fragment creation for crash analytics
        logFragmentEvent("Fragment_Created", this::class.java.simpleName)

        initializeViews()
        setupObservers()
        setupClickListeners()
        loadInitialData()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissProgressDialog()
        recyclerViewHelpers.clear()
        swipeRefreshHelpers.clear()
        _binding = null
    }

    // Abstract methods
    abstract fun initializeViews()
    abstract fun setupObservers()
    abstract fun setupClickListeners()
    abstract fun loadInitialData()

    // ==================== MODERN PROGRESS DIALOG ====================

//    protected fun showProgressDialog(message: String? = null, cancelable: Boolean = false) {
//        try {
//            if (isAdded && context != null) {
//                dismissProgressDialog()
//                progressDialog = ModernProgressDialog.create(requireContext(), message, cancelable)
//                progressDialog?.show()
//            }
//        } catch (e: Exception) {
//            logCrashlyticsError("showProgressDialog", e)
//        }
//    }

    protected fun dismissProgressDialog() {
        try {
            progressDialog?.dismiss()
            progressDialog = null
        } catch (e: Exception) {
            logCrashlyticsError("dismissProgressDialog", e)
        }
    }

    protected fun isProgressShowing(): Boolean {
        return progressDialog?.isShowing == true
    }

    // ==================== SWIPE REFRESH SUPPORT ====================

//    protected fun setupSwipeRefresh(
//        swipeRefreshLayout: SwipeRefreshLayout,
//        onRefresh: () -> Unit
//    ) {
//        try {
//            swipeRefreshLayout.setColorSchemeResources(
//                R.color.login_btn,
//                R.color.colorPrimarynew,
//                R.color.color_dark_blue
//            )
//
//            swipeRefreshLayout.setOnRefreshListener {
//                logFragmentEvent("SwipeRefresh_Triggered", this::class.java.simpleName)
//                onRefresh()
//            }
//
//            swipeRefreshHelpers[swipeRefreshLayout.id] = SwipeRefreshHelper(swipeRefreshLayout, onRefresh)
//        } catch (e: Exception) {
//            logCrashlyticsError("setupSwipeRefresh", e)
//        }
//    }

    protected fun setSwipeRefreshing(swipeRefreshLayoutId: Int, refreshing: Boolean) {
        try {
            swipeRefreshHelpers[swipeRefreshLayoutId]?.setRefreshing(refreshing)
        } catch (e: Exception) {
            logCrashlyticsError("setSwipeRefreshing", e)
        }
    }

    protected fun stopAllSwipeRefresh() {
        swipeRefreshHelpers.values.forEach { helper ->
            helper.setRefreshing(false)
        }
    }

    // ==================== FIREBASE CRASHLYTICS SUPPORT ====================

    protected fun logCrashlyticsError(methodName: String, exception: Exception) {
//        try {
//            val className = this::class.java.simpleName
//            crashlytics.log("$className.$methodName: ${exception.message}")
//            crashlytics.recordException(exception)
//
//            // Also log to console for debugging
//            Log.e(className, "Error in $methodName: ${exception.message}", exception)
//        } catch (e: Exception) {
//            // Fallback logging
//            Log.e("BaseFragment", "Error logging to Crashlytics: ${e.message}")
//        }
    }

    protected fun logFragmentEvent(eventName: String, fragmentName: String? = null) {
//        try {
//            val className = fragmentName ?: this::class.java.simpleName
//            crashlytics.log("Fragment_Event: $eventName in $className")
//
//            // Set custom key for better crash reporting
//            crashlytics.setCustomKey("current_fragment", className)
//            crashlytics.setCustomKey("last_event", eventName)
//        } catch (e: Exception) {
//            Log.e("BaseFragment", "Error logging event: ${e.message}")
//        }
    }

    protected fun setUserIdentifier(userId: String) {
//        try {
//            crashlytics.setUserId(userId)
//        } catch (e: Exception) {
//            logCrashlyticsError("setUserIdentifier", e)
//        }
    }

    protected fun setCustomKey(key: String, value: String) {
//        try {
//            crashlytics.setCustomKey(key, value)
//        } catch (e: Exception) {
//            logCrashlyticsError("setCustomKey", e)
//        }
    }

    protected fun logNetworkCall(url: String, method: String = "GET") {
//        try {
//            crashlytics.log("Network_Call: $method $url")
//            crashlytics.setCustomKey("last_network_call", "$method $url")
//        } catch (e: Exception) {
//            logCrashlyticsError("logNetworkCall", e)
//        }
    }

    // ==================== RECYCLERVIEW SUPPORT ====================

    protected fun <T, VB : ViewBinding> setupRecyclerView(
        recyclerView: RecyclerView,
        items: List<T> = emptyList(),
        layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext()),
        bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
        onBind: (item: T, binding: VB, position: Int) -> Unit,
        onItemClick: ((item: T, position: Int) -> Unit)? = null,
        onViewClick: ((view: View, item: T, position: Int) -> Unit)? = null,
        diffChecker: ((old: T, new: T) -> Boolean)? = null,
        noDataTitle: String? = null,
        noDataDescription: String? = null,
        noDataIconRes: Int? = null
    ): BaseRecyclerAdapter<T, VB> {

        recyclerView.layoutManager = layoutManager

        val adapter = BaseRecyclerAdapter(
            items = items,
            bindingInflater = bindingInflater,
            onBind = onBind,
            onItemClick = onItemClick,
            onViewClick = onViewClick,
            diffChecker = diffChecker,
            recyclerViewParent = recyclerView.parent as? ViewGroup,
            noDataTitle = noDataTitle,
            noDataDescription = noDataDescription,
            noDataIconRes = noDataIconRes
        )

        recyclerView.adapter = adapter

        // Store helper for easy updates
        recyclerViewHelpers[recyclerView.id] = RecyclerViewHelper(adapter, items)
        return adapter
    }



    protected fun <T, VB : ViewBinding> setupRecyclerView(
        recyclerView: RecyclerView,
        items: List<T> = emptyList(),
        noDataConfig: NoDataConfig? = null,
        layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext()),
        bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
        onBind: (T, VB, Int) -> Unit,
        onItemClick: (T, Int) -> Unit = { _, _ -> },
        onViewClick: (View, T, Int) -> Unit = { _, _, _ -> },
        diffChecker: ((T, T) -> Boolean)? = null
    ): BaseRecyclerAdapter<T, VB> {

        return setupRecyclerView(
            recyclerView = recyclerView,
            items = items,
            layoutManager = layoutManager,
            bindingInflater = bindingInflater,
            onBind = onBind,
            onItemClick = onItemClick,
            onViewClick = onViewClick,
            diffChecker = diffChecker,
            noDataTitle = noDataConfig?.title,
            noDataDescription = noDataConfig?.description,
            noDataIconRes = noDataConfig?.iconRes
        )
    }




    protected fun <T> updateRecyclerViewData(
        recyclerViewId: Int,
        newItems: List<T>
    ) {
        try {
            val helper = recyclerViewHelpers[recyclerViewId] as? RecyclerViewHelper<T>
            helper?.update(newItems)
        } catch (e: Exception) {
            logCrashlyticsError("updateRecyclerViewData", e)
        }
    }


    @Suppress("UNCHECKED_CAST")
    protected fun <T, VB : ViewBinding> getRecyclerViewAdapter(
        recyclerViewId: Int
    ): BaseRecyclerAdapter<T, VB>? {
        return (recyclerViewHelpers[recyclerViewId] as? RecyclerViewHelper<T>)?.adapter as? BaseRecyclerAdapter<T, VB>
    }

    protected fun <T> getRecyclerViewItems(recyclerViewId: Int): List<T> {
        return (recyclerViewHelpers[recyclerViewId] as? RecyclerViewHelper<T>)?.currentItems ?: emptyList()
    }

    protected fun clearRecyclerViewData(recyclerViewId: Int) {
        updateRecyclerViewData(recyclerViewId, emptyList<Any>())
    }

    // ==================== UTILITY METHODS ====================

    protected fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    protected fun showLongToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    protected fun showSuccessToast(message: String) {
        showToast(" $message")
    }

    protected fun showErrorToast(message: String) {
        showToast(" $message")
        logCrashlyticsError("showErrorToast", Exception(message))
    }

    protected fun <T> handleApiResponse(
        responseCode: Int,
        data: T? = null,
        onSuccess: ((T?) -> Unit)? = null,
        onNoData: (() -> Unit)? = null,
        onUpgradeRequired: (() -> Unit)? = null,


        onSessionExpired: (() -> Unit)? = null,
        onCustomResponse: ((Int) -> Unit)? = null
    ) {
        try {
            when (responseCode) {
                200 -> onSuccess?.invoke(data)
                202 -> onNoData?.invoke() ?: showToast("No data available.")
                301 -> onUpgradeRequired?.invoke() ?: showToast("Please upgrade your app.")
                401 -> onSessionExpired?.invoke() ?: handleSessionExpired()
                else -> onCustomResponse?.invoke(responseCode) ?: showToast("Unexpected response: $responseCode")
            }
        } catch (e: Exception) {
            logCrashlyticsError("handleApiResponse", e)
        }
    }

    protected fun handleSessionExpired() {
        try {
            AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
        } catch (e: Exception) {
            logCrashlyticsError("handleSessionExpired", e)
            //showToast("Session expired. Please login again.")
            showSessionExpiredDialogFallback()

        }
    }

    private fun showSessionExpiredDialogFallback() {
        AlertDialog.Builder(requireContext())
            .setTitle("Session Expired")
            .setMessage("Your session has expired. Please login again.")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                AppUtil.logoutUser(findNavController(), requireContext())
            }
            .show()
    }


    protected fun showBase64ImageDialog(
        base64ImageString: String?,
        title: String = "Image",
        context: Context = requireContext()
    ) {
        try {
            val bitmap: Bitmap? = if (!base64ImageString.isNullOrBlank()) {
                try {
                    val cleanBase64 = base64ImageString
                        .replace("data:image/png;base64,", "")
                        .replace("data:image/jpg;base64,", "")
                        .replace("data:image/jpeg;base64,", "")
                        .replace("\\s".toRegex(), "")

                    val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                } catch (e: Exception) {
                    null
                }
            } else {
                null
            }

            val dialog = ImagePreviewDialogFragment.newInstance(title, bitmap)
            dialog.show(parentFragmentManager, "ImagePreviewDialog")

        } catch (e: Exception) {
            logCrashlyticsError("showBase64ImageDialog", e)
        }
    }



    protected fun openBase64Pdf(base64: String, context: Context = requireContext()) {
        try {
            val cleanBase64 = base64
                .replace("data:application/pdf;base64,", "")
                .trim()

            val pdfBytes = Base64.decode(cleanBase64, Base64.DEFAULT)

            if (pdfBytes.isEmpty() || !String(pdfBytes.copyOfRange(0, 4)).startsWith("%PDF")) {
                showToast("Invalid PDF data")
                return
            }

            val pdfFile = File.createTempFile("temp_", ".pdf", context.cacheDir)
            pdfFile.outputStream().use { it.write(pdfBytes) }

            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                pdfFile
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(Intent.createChooser(intent, "Open PDF with"))
            } else {
                showToast("No PDF viewer installed")
            }

        } catch (e: Exception) {
            logCrashlyticsError("openBase64Pdf", e)
            showToast("Failed to open PDF")
        }
    }

    // View visibility helpers
    protected fun View.show() {
        visibility = View.VISIBLE
    }

    protected fun View.hide() {
        visibility = View.GONE
    }

    protected fun View.invisible() {
        visibility = View.INVISIBLE
    }

    protected fun View.isVisible(): Boolean = visibility == View.VISIBLE

    protected fun View.isGone(): Boolean = visibility == View.GONE

    // Safe context
    protected fun safeContext(): Context? {
        return if (isAdded) requireContext() else null
    }

    // Coroutine helpers
    protected fun launchIO(block: suspend () -> Unit) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                block()
            } catch (e: Exception) {
                logCrashlyticsError("launchIO", e)
            }
        }
    }

    protected fun launchMain(block: suspend () -> Unit) {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                block()
            } catch (e: Exception) {
                logCrashlyticsError("launchMain", e)
            }
        }
    }

    // Validation helpers
    protected fun String?.isValid(): Boolean {
        return !this.isNullOrBlank() && this != "null" && this != "N/A"
    }

    protected fun String?.toSafeInt(default: Int = 0): Int {
        return try {
            this?.toIntOrNull() ?: default
        } catch (e: Exception) {
            default
        }
    }

    protected fun String?.toSafeDouble(default: Double = 0.0): Double {
        return try {
            this?.toDoubleOrNull() ?: default
        } catch (e: Exception) {
            default
        }
    }

    // Common text helper
    protected fun safeText(value: String?): String {
        return if (value.isNullOrBlank() || value.equals("null", ignoreCase = true)) {
            "N/A"
        } else value
    }

    // ==================== INNER CLASSES ====================

    protected class RecyclerViewHelper<T>(
        val adapter: RecyclerView.Adapter<*>,
        var currentItems: List<T>
    ) {
        fun update(newItems: List<T>) {
            currentItems = newItems
            if (adapter is BaseRecyclerAdapter<*, *>) {
                @Suppress("UNCHECKED_CAST")
                (adapter as BaseRecyclerAdapter<T, *>).update(newItems)
            } else {
                adapter.notifyDataSetChanged()
            }
        }
    }

    private class RecyclerViewHelperN(
        val update: (List<Any>) -> Unit
    ) {
        fun updateItems(newItems: List<Any>) {
            update(newItems)
        }
    }


    private class SwipeRefreshHelper(
        private val swipeRefreshLayout: SwipeRefreshLayout,
        private val onRefresh: () -> Unit
    ) {
        fun setRefreshing(refreshing: Boolean) {
            swipeRefreshLayout.isRefreshing = refreshing
        }
    }


    // ================= TOOLBAR SUPPORT =================

    protected fun setupToolbar(
        root: View,
        title: String?=null,
        @StringRes titleRes: Int? = null,
        showBack: Boolean = true,
        showLang: Boolean = false,
        showProfile: Boolean = false,
        backClick: (() -> Unit)? = null,
        langClick: (() -> Unit)? = null,
        profileClick: (() -> Unit)? = null,
    ): BaseToolbarComponent {
        return BaseToolbarComponent(this, root).apply {
            setup(
                title = title,
                titleRes = titleRes,
                showBack = showBack,
                showLang = showLang,
                showProfile = showProfile,
                backAction = backClick,
                langAction = langClick,
                profileAction = profileClick
            )
        }
    }

    // Helper function to get string resources with format
    fun getStrings(resId: Int, vararg args: Any): String {
        return requireContext().getString(resId, *args)
    }



}

// Data class for no data configuration
data class NoDataConfig(
    val title: String? = null,
    val description: String? = null,
    val iconRes: Int? = null
)