package com.kaushalpanjee.core.data.remote

import com.kaushalpanjee.common.model.SendMobileOTPResponse
import com.kaushalpanjee.common.model.StateDataResponse
import com.kaushalpanjee.core.util.ApiConstant
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url
import com.kaushalpanjee.common.model.UidaiKycRequest
import rural.ekyc.ui.ekyc.models.UidaiResp

interface AppLevelApi {

    @GET(ApiConstant.API_SMS_OTP)
    suspend fun sendMobileOTP(@Query ("mobileNo") mobileNumber : String):SendMobileOTPResponse


    @GET(ApiConstant.API_EMAIL_OTP)
    suspend fun sendEmailTP(@Query ("email") mailOtp : String):SendMobileOTPResponse

    @GET(ApiConstant.API_STATE)
    suspend fun getStateListAPI():StateDataResponse


    @POST
    suspend fun postOnAUAFaceAuthNREGA(
        @Url url: String,
        @Body request: UidaiKycRequest
    ): Response<UidaiResp>

}