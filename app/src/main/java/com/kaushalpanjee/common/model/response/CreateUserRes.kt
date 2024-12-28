package com.kaushalpanjee.common.model.response

data class CreateUserRes(
    val wrappedList: List<User>,
    val responseCode: Int,
    val responseDesc: String,
    val responseMsg: String
)

data class User(
    val userId: String,
    val password: String
)