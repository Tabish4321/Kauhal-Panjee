package com.kaushalpanjee.common.model.response

data class RequesterTicketResponse(
    val wrappedList: List<RequesterTicket>,
    val responseCode: Int,
    val responseDesc: String,
    val responseMsg: String
)

data class RequesterTicket(
    val ticketId: String,
    val requesterName: String,
    val issueTitle: String,
    val raisedDate: String,
    val status: String,

    // Detail Screen Fields
    val ticketType: String,
    val schemeType: String,
    val assignedTo: String,
    val assignedDate: String,
    val priority: String,
    val assigneeComment: String,
    val description: String,
    val attachmentUrl: String?
)