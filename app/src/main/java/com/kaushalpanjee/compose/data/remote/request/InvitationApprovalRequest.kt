package com.kaushalpanjee.compose.data.remote.request

/**
 * Created by Rishi Porwal
 */
data class InvitationApprovalRequest(
    val scheme: String,
    val candidateId: String?,
    val status: String,
    val instituteId: String?,
    val instituteName: String,
    val instituteTrade: String,
    val centerName: String,
    val centerTrade: String,
    val entryCode: String
)