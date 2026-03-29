package com.kaushalpanjee.notification.with_api.model.res

import com.google.gson.annotations.SerializedName

/**
 * Created by Rishi Porwal
 */
data class MARKUNHAPPY(
    val status: String?,
    val message: String?,
    val showDialog: Boolean?,
    val showHappyUnhappy: Boolean?
)