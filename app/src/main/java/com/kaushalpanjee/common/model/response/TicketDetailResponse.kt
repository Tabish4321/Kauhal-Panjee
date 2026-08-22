package com.kaushalpanjee.common.model.response

data class TicketDetailResponse(

    val wrappedList: List<TicketDetail>,
    val responseCode: Int,
    val responseDesc: String,
    val responseMsg: String
)



data class TicketDetail(

    val ticketId:String,

    val requesterName:String,

    val raisedDate:String,

    val ticketType:String,

    val scheme:String,

    val module:String,

    val subModule:String,

    val issueTitle:String,

    val assignedDate:String,

    val assignedTo:String,

    val priority:String,

    val status:String,

    val assigneeComment:String

)
