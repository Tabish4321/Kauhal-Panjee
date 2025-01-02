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
import com.kaushalpanjee.common.model.request.AddressInsertReq
import com.kaushalpanjee.common.model.request.AdharDetailsReq
import com.kaushalpanjee.common.model.request.BankingInsertReq
import com.kaushalpanjee.common.model.request.BankingReq
import com.kaushalpanjee.common.model.request.BlockReq
import com.kaushalpanjee.common.model.request.DistrictReq
import com.kaushalpanjee.common.model.request.EducationalInsertReq
import com.kaushalpanjee.common.model.request.EmploymentInsertReq
import com.kaushalpanjee.common.model.request.GramPanchayatReq
import com.kaushalpanjee.common.model.request.PersonalInsertReq
import com.kaushalpanjee.common.model.request.SeccInsertReq
import com.kaushalpanjee.common.model.request.SeccReq
import com.kaushalpanjee.common.model.request.SectionAndPerReq
import com.kaushalpanjee.common.model.request.ShgValidateReq
import com.kaushalpanjee.common.model.request.TechDomainReq
import com.kaushalpanjee.common.model.request.TechQualification
import com.kaushalpanjee.common.model.request.TrainingInsertReq
import com.kaushalpanjee.common.model.request.UserCreationReq
import com.kaushalpanjee.common.model.request.VillageReq
import com.kaushalpanjee.common.model.response.AadhaarDetailRes
import com.kaushalpanjee.common.model.response.BankingRes
import com.kaushalpanjee.common.model.response.BlockResponse
import com.kaushalpanjee.common.model.response.CreateUserRes
import com.kaushalpanjee.common.model.response.DistrictResponse
import com.kaushalpanjee.common.model.response.GrampanchayatList
import com.kaushalpanjee.common.model.response.InsertRes
import com.kaushalpanjee.common.model.response.SeccDetailsRes
import com.kaushalpanjee.common.model.response.SectionAndPer
import com.kaushalpanjee.common.model.response.ShgValidateRes
import com.kaushalpanjee.common.model.response.TechQualificationRes
import com.kaushalpanjee.common.model.response.TechnicalEduDomain
import com.kaushalpanjee.common.model.response.VillageResponse
import com.kaushalpanjee.common.model.response.WhereHaveYouHeardRes
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

    @POST(ApiConstant.API_WHERE_HAVE_U_HEARD)
    suspend fun getWhereHaveYouHeardAPI(@Body stateListReq: StateListReq ):WhereHaveYouHeardRes

    @POST(ApiConstant.API_STATE)
    suspend fun getStateListAPI(@Body stateListReq: StateListReq):StateDataResponse

    @POST(ApiConstant.API_DISTRICT)
    suspend fun getDistrictListAPI(@Body districtReq: DistrictReq):DistrictResponse


    @POST(ApiConstant.API_CREATE_USER)
    suspend fun getCreateUserAPI(@Body creationReq: UserCreationReq):CreateUserRes



    @POST(ApiConstant.API_BLOCK)
    suspend fun getBlockListAPI(@Body blockReq: BlockReq):BlockResponse




    @POST(ApiConstant.API_GP)
    suspend fun getGpListAPI(@Body gramPanchayatReq: GramPanchayatReq):grampanchayatResponse



    @POST(ApiConstant.API_VILLAGE)
    suspend fun getVillageListAPI(@Body villageReq: VillageReq):VillageResponse



    @POST(ApiConstant.API_AADHAAR_DETAILS)
    suspend fun getAadhaarListAPI(@Body adharDetailsReq: AdharDetailsReq):AadhaarDetailRes



    @POST(ApiConstant.API_SECC_DETAILS)
    suspend fun getSeccListAPI(@Body seccReq: SeccReq):SeccDetailsRes


    @POST(ApiConstant.API_SECTION_PERCENTAGE)
    suspend fun getSecctionAndPerAPI(@Body sectionAndPerReq: SectionAndPerReq):SectionAndPer



    @POST(ApiConstant.BANK_DETAILS)
    suspend fun getBankDetailsAPI(@Body bankingReq: BankingReq ):BankingRes




    @POST(ApiConstant.API_INSERT)
    suspend fun insertPersonalDataAPI(@Body personalInsertReq: PersonalInsertReq ):InsertRes


    @POST(ApiConstant.API_INSERT)
    suspend fun insertAddressAPI(@Body addressInsertReq: AddressInsertReq ):InsertRes



    @POST(ApiConstant.API_INSERT)
    suspend fun insertSeccAPI(@Body seccInsertReq: SeccInsertReq ):InsertRes



    @POST(ApiConstant.API_INSERT)
    suspend fun insertEducatio0nAPI(@Body educationalInsertReq: EducationalInsertReq ):InsertRes



    @POST(ApiConstant.API_INSERT)
    suspend fun insertEmploymentAPI(@Body employmentInsertReq: EmploymentInsertReq):InsertRes



    @POST(ApiConstant.API_INSERT)
    suspend fun insertTrainingAPI(@Body trainingInsertReq: TrainingInsertReq ):InsertRes



    @POST(ApiConstant.API_INSERT)
    suspend fun insertBankingAPI(@Body bankingInsertReq: BankingInsertReq):InsertRes


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