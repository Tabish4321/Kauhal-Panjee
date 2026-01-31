package com.kaushalpanjee.compose.data.remote.response

/**
 * Created by Rishi Porwal
 */
data class UserNotification(
    val id: Int?,
    val instituteId: String?,
    val instituteName: String?,
    val instituteState: String?,
    val instituteTrade: String?,
    val districtCode: String?,
    val candidateId: String?,
    val candidateName: String?,
    val createdOn: String?,
    val createdBy: String?,
    val entityCode: String?,
    val invitationFlag: String?,
    val sendStatus: String?,
    val title: String?,
    val body: String?,
    val type: String?
)