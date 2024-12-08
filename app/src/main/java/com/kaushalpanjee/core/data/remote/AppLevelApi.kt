package com.kaushalpanjee.core.data.remote

import com.kaushalpanjee.common.model.SendMobileOTPResponse
import com.kaushalpanjee.common.model.StateDataResponse
import com.kaushalpanjee.core.util.ApiConstant
import retrofit2.http.GET
import retrofit2.http.Query

interface AppLevelApi {

    @GET(ApiConstant.API_SMS_OTP)
    suspend fun sendMobileOTP(@Query ("mobileNo") mobileNumber : String):SendMobileOTPResponse


    @GET(ApiConstant.API_EMAIL_OTP)
    suspend fun sendEmailTP(@Query ("email") mailOtp : String):SendMobileOTPResponse

    @GET(ApiConstant.API_STATE)
    suspend fun getStateListAPI():StateDataResponse

}