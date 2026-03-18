package com.kaushalpanjee.base

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.kaushalpanjee.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView

class BaseToolbarComponent(
    private val fragment: Fragment,
    private val rootView: View
) {

    private val toolbar: Toolbar? = rootView.findViewById(R.id.universalToolbar)
    private val btnBack: MaterialButton? = rootView.findViewById(R.id.btnBack)
    private val tvTitle: TextView? = rootView.findViewById(R.id.tvTitle)
    private val langIcon: ShapeableImageView? = rootView.findViewById(R.id.changeLanguage)
    private val profileIcon: ShapeableImageView? = rootView.findViewById(R.id.profilePic)
    private val appLogo: ShapeableImageView =rootView.findViewById(R.id.logo_img)

    fun setup(
        title: String? = null,
        @StringRes titleRes: Int? = null,

        showBack: Boolean = true,
        showLang: Boolean = true,
        showProfile: Boolean = true,
        backAction: (() -> Unit)? = null,
        langAction: (() -> Unit)? = null,
        profileAction: (() -> Unit)? = null
    ) {
        when {
            title != null -> tvTitle?.text = title
            titleRes != null -> tvTitle?.setText(titleRes)
            else -> tvTitle?.text = ""
        }


        if(tvTitle?.text == "HOME") {
            appLogo.visibility = View.VISIBLE
            tvTitle.visibility= View.GONE
        }else{
            appLogo.visibility = View.GONE
            tvTitle?.visibility = View.VISIBLE

        }
        btnBack?.visibility = if (showBack) View.VISIBLE else View.GONE
        langIcon?.visibility = if (showLang) View.VISIBLE else View.GONE
        profileIcon?.visibility = if (showProfile) View.VISIBLE else View.GONE

        btnBack?.setOnClickListener {
            backAction?.invoke() ?: fragment.requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        langIcon?.setOnClickListener { langAction?.invoke() }

        profileIcon?.setOnClickListener { profileAction?.invoke() }
    }
}
