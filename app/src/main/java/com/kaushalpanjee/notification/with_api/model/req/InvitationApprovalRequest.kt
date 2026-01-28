package com.kaushalpanjee.notification.with_api.model.req

/**
 * Created by Rishi Porwal
 */
data class InvitationApprovalRequest(
    val scheme: String?,
    val candidateId: String?,
    // RSETI
    val instituteName: String?,
    val instituteTrade: String?,
    // DDU-GKY
    val centerName: String?,
    val centerTrade: String?,
    val entryCode: String?
)