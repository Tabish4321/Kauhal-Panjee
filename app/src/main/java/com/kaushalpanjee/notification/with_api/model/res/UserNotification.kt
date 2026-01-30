package com.kaushalpanjee.notification.with_api.model.res

import com.google.gson.annotations.SerializedName

/**
 * Created by Rishi Porwal
 */
data class UserNotification(

    val id: Int?,

    // Institute info
    val instituteId: String?,
    val instituteName: String?,
    val instituteState: String?,
    val instituteTrade: String?,
    val districtCode: String?,

    // Candidate info
    val candidateId: String?,
    val candidateName: String?,

    // Metadata
    val createdOn: String?,
    val createdBy: String?,

    val entityCode: String?,

    // Invitation / status
    val invitationFlag: String?,   // P / A / R
    val sendStatus: String?,        // SENT / FAILED / etc

    // Notification content
    val title: String?,
    val body: String?,
    val type: String?               // INVITATION
)




//invitationStatus

//"id": 1,
//"instituteId": "101",
//"instituteName": "RSETI Ghaziabad",
//"instituteState": "31",
//"instituteTrade": "658",
//"districtCode": "3120",
//"candidateId": "2505000007",
//"candidateName": "Walvinder Singh",
//"createdOn": "2026-01-29T12:14:11.050903",
//"createdBy": "NIC",
//"entityCode": "RS-31",
//"invitationFlag": "P",
//"sendStatus": null