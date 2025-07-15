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
import com.kaushalpanjee.common.model.request.AadhaarCheckReq
import com.kaushalpanjee.common.model.request.AddressInsertReq
import com.kaushalpanjee.common.model.request.AdharDetailsReq
import com.kaushalpanjee.common.model.request.BankingInsertReq
import com.kaushalpanjee.common.model.request.BankingReq
import com.kaushalpanjee.common.model.request.BannerReq
import com.kaushalpanjee.common.model.request.BlockReq
import com.kaushalpanjee.common.model.request.CandidateReq
import com.kaushalpanjee.common.model.request.ChangePassReq
import com.kaushalpanjee.common.model.request.DistrictReq
import com.kaushalpanjee.common.model.request.EducationalInsertReq
import com.kaushalpanjee.common.model.request.EmploymentInsertReq
import com.kaushalpanjee.common.model.request.FaceCheckReq
import com.kaushalpanjee.common.model.request.GetLoginIdNdPassReq
import com.kaushalpanjee.common.model.request.GetSearchTraining
import com.kaushalpanjee.common.model.request.GramPanchayatReq
import com.kaushalpanjee.common.model.request.ImageChangeReq
import com.kaushalpanjee.common.model.request.LoginReq
import com.kaushalpanjee.common.model.request.PersonalInsertReq
import com.kaushalpanjee.common.model.request.SeccInsertReq
import com.kaushalpanjee.common.model.request.SeccReq
import com.kaushalpanjee.common.model.request.SectionAndPerReq
import com.kaushalpanjee.common.model.request.ShgValidateReq
import com.kaushalpanjee.common.model.request.TechDomainReq
import com.kaushalpanjee.common.model.request.TechQualification
import com.kaushalpanjee.common.model.request.TokenReq
import com.kaushalpanjee.common.model.request.TradeReq
import com.kaushalpanjee.common.model.request.TrainingCenterReq
import com.kaushalpanjee.common.model.request.TrainingInsertReq
import com.kaushalpanjee.common.model.request.TrainingSearch
import com.kaushalpanjee.common.model.request.UserCreationReq
import com.kaushalpanjee.common.model.request.ValidateOtpReq
import com.kaushalpanjee.common.model.request.VillageReq
import com.kaushalpanjee.common.model.response.AadhaarCheckRes
import com.kaushalpanjee.common.model.response.AadhaarDetailRes
import com.kaushalpanjee.common.model.response.BankingRes
import com.kaushalpanjee.common.model.response.BannerResponse
import com.kaushalpanjee.common.model.response.BlockResponse
import com.kaushalpanjee.common.model.response.CandidateDetails
import com.kaushalpanjee.common.model.response.CreateUserRes
import com.kaushalpanjee.common.model.response.DistrictResponse
import com.kaushalpanjee.common.model.response.FaceResponse
import com.kaushalpanjee.common.model.response.ForgotIdOtpRes
import com.kaushalpanjee.common.model.response.GrampanchayatList
import com.kaushalpanjee.common.model.response.InsertRes
import com.kaushalpanjee.common.model.response.JobcardResponse
import com.kaushalpanjee.common.model.response.LanguageList
import com.kaushalpanjee.common.model.response.LoginRes
import com.kaushalpanjee.common.model.response.OtpValidateResponse
import com.kaushalpanjee.common.model.response.SeccDetailsRes
import com.kaushalpanjee.common.model.response.SectionAndPer
import com.kaushalpanjee.common.model.response.SectorResponse
import com.kaushalpanjee.common.model.response.ShgValidateRes
import com.kaushalpanjee.common.model.response.TechQualificationRes
import com.kaushalpanjee.common.model.response.TechnicalEduDomain
import com.kaushalpanjee.common.model.response.TokenRes
import com.kaushalpanjee.common.model.response.TradeResponse
import com.kaushalpanjee.common.model.response.TrainingCenterRes
import com.kaushalpanjee.common.model.response.VillageResponse
import com.kaushalpanjee.common.model.response.WhereHaveYouHeardRes
import com.kaushalpanjee.common.model.response.grampanchayatResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Headers

interface AppLevelApi {

    @POST(ApiConstant.GET_TOKEN_GENERATE)
    suspend fun getToken(@Body tokenReq: TokenReq):TokenRes

    @POST(ApiConstant.API_SMS_OTP)
    suspend fun sendMobileOTP(@Body sendOTPRequest: SendOTPRequest):SendMobileOTPResponse


    @POST(ApiConstant.API_EMAIL_OTP)
    suspend fun sendEmailTP(@Body sendOtpEmailReq: SendOtpEmailReq):SendMobileOTPResponse


    @POST(ApiConstant.API_TECH_EDUCATION)
    suspend fun getTechEducationAPI(@Header("Authorization") token: String,
                                    @Body techQualification: TechQualification):TechQualificationRes


    @POST(ApiConstant.API_TECH_EDUCATION_DOMAIN)
    suspend fun getTechEducationDomainAPI(@Header("Authorization") token: String,
                                          @Body techDomainReq: TechDomainReq):TechnicalEduDomain

    @POST(ApiConstant.API_WHERE_HAVE_U_HEARD)
    suspend fun getWhereHaveYouHeardAPI(@Header("Authorization") token: String,

                                        @Body stateListReq: StateListReq ):WhereHaveYouHeardRes

    @POST(ApiConstant.API_STATE)
    suspend fun getStateListAPI(
                                @Body stateListReq: StateListReq):StateDataResponse

    @POST(ApiConstant.API_DISTRICT)
    suspend fun getDistrictListAPI(@Header("Authorization") token: String,
                                   @Body districtReq: DistrictReq):DistrictResponse

    @POST(ApiConstant.API_DISTRICT)
    suspend fun getDistrictPreListAPI(@Header("Authorization") token: String,
                                      @Body districtReq: DistrictReq):DistrictResponse


    @POST(ApiConstant.API_CREATE_USER)
    suspend fun getCreateUserAPI(@Body creationReq: UserCreationReq):CreateUserRes



    @POST(ApiConstant.API_BLOCK)
    suspend fun getBlockListAPI(@Header("Authorization") token: String,
                                @Body blockReq: BlockReq):BlockResponse

    @POST(ApiConstant.API_BLOCK)
    suspend fun getBlockPerListAPI(@Header("Authorization") token: String,
                                   @Body blockReq: BlockReq):BlockResponse





    @POST(ApiConstant.API_GP)
    suspend fun getGpListAPI(@Header("Authorization") token: String,
                             @Body gramPanchayatReq: GramPanchayatReq):grampanchayatResponse

    @POST(ApiConstant.API_GP)
    suspend fun getGpPerListAPI(@Header("Authorization") token: String,
                                @Body gramPanchayatReq: GramPanchayatReq):grampanchayatResponse




        @POST(ApiConstant.API_VILLAGE)
        suspend fun getVillageListAPI(@Header("Authorization") token: String,
                                      @Body villageReq: VillageReq):VillageResponse



    @POST(ApiConstant.API_VILLAGE)
    suspend fun getVillagePerListAPI(@Header("Authorization") token: String,
                                     @Body villageReq: VillageReq):VillageResponse


    @POST(ApiConstant.API_AADHAAR_DETAILS)
    suspend fun getAadhaarListAPI(@Header("Authorization") token: String,
                                  @Body adharDetailsReq: AdharDetailsReq):AadhaarDetailRes



    @POST(ApiConstant.API_SECC_DETAILS)
    suspend fun getSeccListAPI(@Header("Authorization") token: String,
                               @Body seccReq: SeccReq):SeccDetailsRes


    @POST(ApiConstant.API_SECTION_PERCENTAGE)
    suspend fun getSecctionAndPerAPI(@Header("Authorization") token: String,
                                     @Body sectionAndPerReq: SectionAndPerReq):SectionAndPer



    @POST(ApiConstant.BANK_DETAILS)
    suspend fun getBankDetailsAPI(@Header("Authorization") token: String,
                                  @Body bankingReq: BankingReq ):BankingRes




    @POST(ApiConstant.API_INSERT)
    suspend fun insertPersonalDataAPI(@Header("Authorization") token: String,
                                      @Body personalInsertReq: PersonalInsertReq ):InsertRes


    @POST(ApiConstant.API_INSERT)
    suspend fun insertAddressAPI(@Header("Authorization") token: String,
                                 @Body addressInsertReq: AddressInsertReq ):InsertRes



    @POST(ApiConstant.API_INSERT)
    suspend fun insertSeccAPI(@Header("Authorization") token: String,
                              @Body seccInsertReq: SeccInsertReq ):InsertRes



    @POST(ApiConstant.API_INSERT)
    suspend fun insertEducatio0nAPI(@Header("Authorization") token: String,
                                    @Body educationalInsertReq: EducationalInsertReq ):InsertRes



    @POST(ApiConstant.API_INSERT)
    suspend fun insertEmploymentAPI(@Header("Authorization") token: String,
                                    @Body employmentInsertReq: EmploymentInsertReq):InsertRes



    @POST(ApiConstant.API_INSERT)
    suspend fun insertTrainingAPI(@Header("Authorization") token: String,
                                  @Body trainingInsertReq: TrainingInsertReq ):InsertRes



    @POST(ApiConstant.API_INSERT)
    suspend fun insertBankingAPI(@Header("Authorization") token: String,
                                 @Body bankingInsertReq: BankingInsertReq):InsertRes



    @POST(ApiConstant.API_LOGIN)
    suspend fun getLoginAPI(@Body loginReq: LoginReq):LoginRes




    @POST(ApiConstant.API_SECTOR)
    suspend fun getSectorListAPI(@Header("Authorization") token: String,
                                 @Body techQualification: TechQualification):SectorResponse



    @POST(ApiConstant.API_TRADE)
    suspend fun getTradeListAPI(@Header("Authorization") token: String,
                                @Body tradeReq: TradeReq):TradeResponse



    @POST(ApiConstant.API_TRAINING_SEARCH)
    suspend fun getTrainingSearchAPI(@Header("Authorization") token: String,
                                     @Body trainingSearch: TrainingSearch):TrainingCenterRes



    @POST(ApiConstant.API_TRAINING_LIST)
    suspend fun getTrainingListAPI(@Header("Authorization") token: String,
                                   @Body trainingCenterReq: TrainingCenterReq):TrainingCenterRes

    @POST(ApiConstant.API_GET_SEARCH_TRAINING)
    suspend fun getSelectedTrainingListAPI(@Header("Authorization") token: String,
                                           @Body getSearchTraining: GetSearchTraining):TrainingCenterRes




    @POST(ApiConstant.API_CANDIDATE_DETAILS)
    suspend fun getCandidateDetailsAPI(@Header("Authorization") token: String,
                                       @Body candidateReq: CandidateReq):CandidateDetails




    @POST(ApiConstant.API_CHANGE_IMAGE_CANDIDATE)
    suspend fun getImageChangeAPI(@Header("Authorization") token: String,
                                  @Body imageChangeReq: ImageChangeReq):InsertRes



    @POST(ApiConstant.API_CHANGE_PASSWORD)
    suspend fun getChangePass(@Header("Authorization") token: String,
                              @Body changePassReq: ChangePassReq):InsertRes


    @POST(ApiConstant.Forgot_PASSWORD_OTP)
    suspend fun getChangePassOtp(@Body getLoginIdNdPassReq: GetLoginIdNdPassReq):ForgotIdOtpRes



    @POST(ApiConstant.GET_LOGINID_PASS)
    suspend fun getLoginIdPass(@Body getLoginIdNdPassReq: GetLoginIdNdPassReq):InsertRes


    @POST(ApiConstant.LANGUAGE_LIST)
    suspend fun getLanguageListAPI(@Body appVersion :String):LanguageList

    @POST(ApiConstant.API_OTP_VALIDATE)
    suspend fun getOtpValidateApi(@Body validateOtpReq: ValidateOtpReq): OtpValidateResponse

    @POST(ApiConstant.API_OTP_checkUserExistance)
    suspend fun getAadhaarCheck(@Body aadhaarCheckReq: AadhaarCheckReq):AadhaarCheckRes


    @POST(ApiConstant.API_UPDATE_FACE)
    suspend fun updateFaceApi(@Body faceCheckReq: FaceCheckReq):FaceResponse


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

    @FormUrlEncoded
    @POST
    @Headers("Content-Type: application/x-www-form-urlencoded")
    suspend fun checkJobcard(
        @Url url: String, // Full URL passed manually
        @Header("username") username: String,
        @Header("password") password: String,
        @Field("jobcardno") jobcardNo: String
    ): Response<JobcardResponse>

    @POST(ApiConstant.API_OTP_getBanner)
    suspend fun getBannerAPI(@Header("Authorization") token: String,
                             @Body bannerReq: BannerReq): BannerResponse





}