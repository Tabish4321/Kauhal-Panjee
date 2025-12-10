package com.kaushalpanjee.common

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.R
import com.kaushalpanjee.common.model.request.SectionAndPerReq
import com.kaushalpanjee.core.util.AppUtil
import com.kaushalpanjee.core.util.UserPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CertificateActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private val TAG = "CERTIFICATE"
    @Inject
    lateinit var userPreferences: UserPreferences


    // === Replace this with the real certificate URL in production (HTTPS).
    // For this debug/demo we use the local path you uploaded:
    private var certificateUrl = ""

    // If you have a server host constraint, set allowedHost = "yourserver.com"
    // For local file use null or check differently
    private val allowedHost: String? = null // "yourserver.com"
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_certificate)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        certificateUrl = intent.getStringExtra("CERT_URL").toString()





        webView = findViewById(R.id.certificateWebView)
        val progress = findViewById<View>(R.id.progress)

        // --- WebView settings: enable JS (required for many UIs) but harden other features ---
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = false
        settings.cacheMode = WebSettings.LOAD_NO_CACHE

        // Block dangerous access
        settings.allowFileAccess = false
        settings.allowContentAccess = false

        // No external windows
        settings.setSupportMultipleWindows(false)
        settings.javaScriptCanOpenWindowsAutomatically = false

        // Mixed content block
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW

        // Remove cookies (prevent session leak)
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookies(null)
        cookieManager.flush()

        // Prevent file downloads (we will block download triggers)
        webView.setDownloadListener { url, _, _, _, _ ->
            // Block or show safe message. DO NOT allow automatic downloads of sensitive docs.
            Log.w(TAG, "Download attempt blocked: $url")
            AlertDialog.Builder(this)
                .setTitle("Download blocked")
                .setMessage("Direct downloads are disabled for security reasons.")
                .setPositiveButton("OK", null)
                .show()
        }

        // WebViewClient to enforce navigation policy + show/hide progress
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url ?: return true
                // Restrict navigation:
                if (allowedHost != null) {
                    // allow only same host
                    if (url.host == allowedHost) {
                        return false // allow
                    } else {
                        Log.w(TAG, "Blocked navigation to: $url")
                        return true // block
                    }
                } else {
                    // if using local file, allow file:// and https only
                    val scheme = url.scheme
                    return when {
                        scheme == "file" -> false
                        scheme == "https" -> false
                        else -> {
                            Log.w(TAG, "Blocked navigation (non-https/file): $url")
                            true
                        }
                    }
                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progress.visibility = View.GONE
                Log.d(TAG, "Page loaded: $url")
            }

            override fun onPageCommitVisible(view: WebView?, url: String?) {
                // extra hook if needed
            }
        }

        // Block hardware Back from closing app while cert is shown? (optional)
        webView.isFocusableInTouchMode = true
        webView.requestFocus()

        // Clear any previous state first
        webView.clearHistory()
        webView.clearCache(true)

        // Load the certificate URL
        progress.visibility = View.VISIBLE
        Log.d(TAG, "certificateUrl:"+certificateUrl+userPreferences.getUseID())
        webView.loadUrl(certificateUrl+userPreferences.getUseID())
       // webView.loadUrl(certificateUrl+"2506027615")

    }

    override fun onDestroy() {
        super.onDestroy()
        // clear all traces for audit
        try {
            webView.loadUrl("about:blank")
            webView.clearHistory()
            webView.removeAllViews()
            webView.destroy()
        } catch (e: Exception) {
            Log.w(TAG, "Error destroying WebView: ${e.message}")
        }

        // Clear cookies again
        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()
    }

    // Optional: block BACK press if you want to force the user to take action first
    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        // If you want to allow going back within webview:
        if (webView.canGoBack()) {
            webView.goBack()
            return
        }
        // else block or ask user
        // super.onBackPressed()
        // For security you might want to prevent leaving:
        AlertDialog.Builder(this)
            .setTitle("Exit")
            .setMessage("Are you sure you want to exit the certificate view?")
            .setPositiveButton("Yes") { _, _ -> super.onBackPressed() }
            .setNegativeButton("No", null)
            .show()
    }

}