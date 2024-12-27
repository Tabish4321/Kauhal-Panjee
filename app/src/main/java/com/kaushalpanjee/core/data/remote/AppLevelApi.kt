package com.kaushalpanjee.core.data.remote

import com.kaushalpanjee.common.model.SendMobileOTPResponse
import com.kaushalpanjee.common.model.SendOTPRequest
import com.kaushalpanjee.common.model.SendOtpEmailReq
import com.kaushalpanjee.common.model.StateDataResponse
import com.kaushalpanjee.common.model.StateListReq
import com.kaushalpanjee.core.util.ApiConstant
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url
import com.kaushalpanjee.common.model.UidaiKycRequest
import com.kaushalpanjee.common.model.UidaiResp
import com.kaushalpanjee.common.model.request.BlockReq
import com.kaushalpanjee.common.model.request.DistrictReq
import com.kaushalpanjee.common.model.request.GramPanchayatReq
import com.kaushalpanjee.common.model.request.ShgValidateReq
import com.kaushalpanjee.common.model.request.TechDomainReq
import com.kaushalpanjee.common.model.request.TechQualification
import com.kaushalpanjee.common.model.request.VillageReq
import com.kaushalpanjee.common.model.response.BlockResponse
import com.kaushalpanjee.common.model.response.DistrictResponse
import com.kaushalpanjee.common.model.response.GrampanchayatList
import com.kaushalpanjee.common.model.response.ShgValidateRes
import com.kaushalpanjee.common.model.response.TechQualificationRes
import com.kaushalpanjee.common.model.response.TechnicalEduDomain
import com.kaushalpanjee.common.model.response.VillageResponse
import com.kaushalpanjee.common.model.response.grampanchayatResponse

interface AppLevelApi {

    @POST(ApiConstant.API_SMS_OTP)
    suspend fun sendMobileOTP(@Body sendOTPRequest: SendOTPRequest):SendMobileOTPResponse


    @POST(ApiConstant.API_EMAIL_OTP)
    suspend fun sendEmailTP(@Body sendOtpEmailReq: SendOtpEmailReq):SendMobileOTPResponse


    @POST(ApiConstant.API_TECH_EDUCATION)
    suspend fun getTechEducationAPI(@Body techQualification: TechQualification):TechQualificationRes


    @POST(ApiConstant.API_TECH_EDUCATION_DOMAIN)
    suspend fun getTechEducationDomainAPI(@Body techDomainReq: TechDomainReq):TechnicalEduDomain


    @POST(ApiConstant.API_STATE)
    suspend fun getStateListAPI(@Body stateListReq: StateListReq):StateDataResponse

    @POST(ApiConstant.API_DISTRICT)
    suspend fun getDistrictListAPI(@Body districtReq: DistrictReq):DistrictResponse



    @POST(ApiConstant.API_BLOCK)
    suspend fun getBlockListAPI(@Body blockReq: BlockReq):BlockResponse




    @POST(ApiConstant.API_GP)
    suspend fun getGpListAPI(@Body gramPanchayatReq: GramPanchayatReq):grampanchayatResponse



    @POST(ApiConstant.API_VILLAGE)
    suspend fun getVillageListAPI(@Body villageReq: VillageReq):VillageResponse


    @POST
    suspend fun postOnAUAFaceAuthNREGA(
        @Url url: String,
        @Body request: UidaiKycRequest
    ): Response<UidaiResp>


    @POST
    suspend fun shgValidateAPI(
        @Url url: String,
        @Body request: ShgValidateReq
    ): Response<ShgValidateRes>


}