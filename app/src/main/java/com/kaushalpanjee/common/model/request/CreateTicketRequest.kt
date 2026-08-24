package com.kaushalpanjee.common.model.request

data class CreateTicketRequest(
    val loginId: String,
    val roleName: String,
    val ticketTypeId: Int,
    val schemeCd: String,
    val issueTitle: String,
    val description: String,
    val ticketPriorityId: Int = 3
)