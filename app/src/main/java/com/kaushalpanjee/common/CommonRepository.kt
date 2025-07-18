package com.kaushalpanjee.common

import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.common.model.SendMobileOTPResponse
import com.kaushalpanjee.common.model.SendOTPRequest
import com.kaushalpanjee.common.model.SendOtpEmailReq
import com.kaushalpanjee.common.model.StateDataResponse
import com.kaushalpanjee.common.model.StateListReq
import com.kaushalpanjee.common.model.UidaiKycRequest
import com.kaushalpanjee.common.model.request.BlockReq
import com.kaushalpanjee.common.model.request.DistrictReq
import com.kaushalpanjee.common.model.request.GramPanchayatReq
import com.kaushalpanjee.common.model.request.VillageReq
import com.kaushalpanjee.common.model.response.BlockResponse
import com.kaushalpanjee.common.model.response.DistrictResponse
import com.kaushalpanjee.common.model.response.GrampanchayatList
import com.kaushalpanjee.common.model.response.VillageResponse
import com.kaushalpanjee.common.model.response.grampanchayatResponse
import com.kaushalpanjee.core.data.local.database.AppDatabase
import com.kaushalpanjee.core.data.remote.AppLevelApi
import com.kaushalpanjee.core.di.AppModule
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.core.util.networkBoundResourceWithoutDb
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import com.kaushalpanjee.common.model.UidaiResp
import com.kaushalpanjee.common.model.request.AadhaarCheckReq
import com.kaushalpanjee.common.model.request.AadhaarRekycReq
import com.kaushalpanjee.common.model.request.AddressInsertReq
import com.kaushalpanjee.common.model.request.AdharDetailsReq
import com.kaushalpanjee.common.model.request.BankingInsertReq
import com.kaushalpanjee.common.model.request.BankingReq
import com.kaushalpanjee.common.model.request.BannerReq
import com.kaushalpanjee.common.model.request.CandidateReq
import com.kaushalpanjee.common.model.request.ChangePassReq
import com.kaushalpanjee.common.model.request.EducationalInsertReq
import com.kaushalpanjee.common.model.request.EmploymentInsertReq
import com.kaushalpanjee.common.model.request.FaceCheckReq
import com.kaushalpanjee.common.model.request.GetLoginIdNdPassReq
import com.kaushalpanjee.common.model.request.GetSearchTraining
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
import com.kaushalpanjee.common.model.response.AadhaarCheckRes
import com.kaushalpanjee.common.model.response.AadhaarDetailRes
import com.kaushalpanjee.common.model.response.AadhaarEkycRes
import com.kaushalpanjee.common.model.response.BankingRes
import com.kaushalpanjee.common.model.response.BannerResponse
import com.kaushalpanjee.common.model.response.CandidateDetails
import com.kaushalpanjee.common.model.response.CreateUserRes
import com.kaushalpanjee.common.model.response.FaceResponse
import com.kaushalpanjee.common.model.response.ForgotIdOtpRes
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
import com.kaushalpanjee.common.model.response.WhereHaveYouHeardRes
import com.kaushalpanjee.core.util.AppUtil
import javax.inject.Inject

class CommonRepository @Inject constructor(
    @AppModule.PreLoginAppLevelApi private val appLevelApi: AppLevelApi,
    private val database: AppDatabase
    ){

    suspend fun getToken(imeiNo : String, appVersion :String): Flow<Resource<out TokenRes>> {
        return networkBoundResourceWithoutDb {
            appLevelApi.getToken(TokenReq(appVersion,imeiNo))
        }
    }
    suspend fun sendMobileOTP(mobileNumber : String, appVersion :String, imeiNo :String): Flow<Resource<out SendMobileOTPResponse>> {
       return networkBoundResourceWithoutDb {
            appLevelApi.sendMobileOTP(SendOTPRequest(imeiNo,mobileNumber,appVersion))
        }
    }

    suspend fun sendEmailOTP(email : String,appVersion: String, imeiNo :String): Flow<Resource<out SendMobileOTPResponse>> {
        return networkBoundResourceWithoutDb {
            appLevelApi.sendEmailTP(SendOtpEmailReq(imeiNo,email,appVersion))
        }
    }

    suspend fun getTechEducationAPI(appVersion: String,loginId :String,header :String): Flow<Resource<out TechQualificationRes>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getTechEducationAPI(header,TechQualification(appVersion,loginId))
        }

    }

    suspend fun getTechEducationDomainAPI(appVersion: String,qual : String,header :String,loginId :String): Flow<Resource<out TechnicalEduDomain>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getTechEducationDomainAPI(header,TechDomainReq(appVersion,qual,loginId))
        }

    }

    suspend fun getStateListApi(appVersion: String,loginId:String): Flow<Resource<out StateDataResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getStateListAPI(StateListReq(appVersion,loginId))
        }
    }



    suspend fun getWhereHaveYouHeardAPI(appVersion: String,header :String,loginId :String): Flow<Resource<out WhereHaveYouHeardRes>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getWhereHaveYouHeardAPI(header,StateListReq(appVersion,loginId))
        }
    }

    suspend fun getDistrictListApi(stateCode: String,appVersion: String,header :String,loginId :String): Flow<Resource<out DistrictResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getDistrictListAPI(header,DistrictReq(stateCode, appVersion,loginId))
        }
    }


    suspend fun getDistrictPerListApi(stateCode: String,appVersion: String,header :String,loginId :String): Flow<Resource<out DistrictResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getDistrictPreListAPI(header,DistrictReq(stateCode, appVersion,loginId))
        }
    }



    suspend fun getCreateUserAPI(userCreationReq: UserCreationReq): Flow<Resource<out CreateUserRes>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getCreateUserAPI(userCreationReq)
        }
    }


    suspend fun getBlockListApi(districtCode: String,appVersion: String,header :String,loginId :String): Flow<Resource<out BlockResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getBlockListAPI(header,BlockReq(districtCode, appVersion,loginId))
        }
    }


    suspend fun getBlockPerListApi(districtCode: String,appVersion: String,header :String,loginId :String): Flow<Resource<out BlockResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getBlockPerListAPI(header,BlockReq(districtCode, appVersion,loginId))
        }
    }



    suspend fun getGPListApi(blockCode: String,appVersion: String,header :String,loginId :String): Flow<Resource<out grampanchayatResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getGpListAPI(header,GramPanchayatReq(blockCode, appVersion,loginId))
        }
    }


    suspend fun getGPPerListApi(blockCode: String,appVersion: String,header :String,loginId :String): Flow<Resource<out grampanchayatResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getGpPerListAPI(header,GramPanchayatReq(blockCode, appVersion,loginId))
        }
    }




    suspend fun getVillageListApi(gpCode: String,appVersion: String,header :String,loginId :String): Flow<Resource<out VillageResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getVillageListAPI(header,VillageReq(gpCode, appVersion,loginId))
        }
    }


    suspend fun getVillagePerListApi(gpCode: String,appVersion: String,header :String,loginId :String): Flow<Resource<out VillageResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getVillagePerListAPI(header,VillageReq(gpCode, appVersion,loginId))
        }
    }



    suspend fun getAadhaarListAPI(adharDetailsReq: AdharDetailsReq,header :String): Flow<Resource<out AadhaarDetailRes>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getAadhaarListAPI(header,adharDetailsReq)
        }
    }

    suspend fun getSeccListAPI(seccReq: SeccReq,header :String): Flow<Resource<out SeccDetailsRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getSeccListAPI(header,seccReq)
        }
    }


    suspend fun getSecctionAndPerAPI(sectionAndPerReq: SectionAndPerReq,header :String): Flow<Resource<out SectionAndPer>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getSecctionAndPerAPI(header,sectionAndPerReq)
        }
    }


    suspend fun insertPersonalDataAPI(personalInsertReq: PersonalInsertReq,header :String): Flow<Resource<out InsertRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.insertPersonalDataAPI(header,personalInsertReq)
        }
    }



    suspend fun insertAddressAPI(addressInsertReq: AddressInsertReq,header :String): Flow<Resource<out InsertRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.insertAddressAPI(header,addressInsertReq)
        }
    }

    suspend fun insertSeccAPI(seccInsertReq: SeccInsertReq,header :String): Flow<Resource<out InsertRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.insertSeccAPI(header,seccInsertReq)
        }
    }


    suspend fun insertEducationAPI(educationalInsertReq: EducationalInsertReq,header :String): Flow<Resource<out InsertRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.insertEducatio0nAPI(header,educationalInsertReq)
        }
    }

    suspend fun insertEmploymentAPI(employmentInsertReq: EmploymentInsertReq,header :String): Flow<Resource<out InsertRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.insertEmploymentAPI(header,employmentInsertReq)
        }
    }


    suspend fun insertTrainingAPI(trainingInsertReq: TrainingInsertReq,header :String): Flow<Resource<out InsertRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.insertTrainingAPI(header,trainingInsertReq)
        }
    }



    suspend fun insertBankingAPI(bankingInsertReq: BankingInsertReq,header :String): Flow<Resource<out InsertRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.insertBankingAPI(header,bankingInsertReq)
        }
    }




    suspend fun getLoginAPI(loginReq: LoginReq): Flow<Resource<out LoginRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getLoginAPI(loginReq)
        }
    }


    suspend fun getLanguageListAPI(): Flow<Resource<out LanguageList>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getLanguageListAPI(BuildConfig.VERSION_NAME)
        }
    }
    suspend fun getOtpValidateApi(validateOtpReq: ValidateOtpReq): Flow<Resource<out OtpValidateResponse>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getOtpValidateApi(validateOtpReq)
        }
    }

    suspend fun getAadhaarCheck(aadhaarCheckReq: AadhaarCheckReq): Flow<Resource<out AadhaarCheckRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getAadhaarCheck(aadhaarCheckReq)
        }
    }

    suspend fun updateFaceApi(faceCheckReq: FaceCheckReq): Flow<Resource<out FaceResponse>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.updateFaceApi(faceCheckReq)
        }
    }



    suspend fun getSectorListAPI(techQualification: TechQualification,header :String): Flow<Resource<out SectorResponse>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getSectorListAPI(header,techQualification)
        }
    }
    suspend fun getBannerAPI(token:String,bannerReq: BannerReq): Flow<Resource<out BannerResponse>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getBannerAPI(token,bannerReq)
        }
    }


    suspend fun getTradeListAPI(tradeReq: TradeReq,header :String): Flow<Resource<out TradeResponse>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getTradeListAPI(header,tradeReq)
        }
    }




    suspend fun getTrainingSearchAPI(trainingSearch: TrainingSearch,header :String): Flow<Resource<out TrainingCenterRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getTrainingSearchAPI(header,trainingSearch)
        }
    }




    suspend fun getTrainingListAPI(trainingCenterReq: TrainingCenterReq,header :String): Flow<Resource<out TrainingCenterRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getTrainingListAPI(header,trainingCenterReq)
        }
    }



    suspend fun getSelectedTrainingListAPI(getSearchTraining: GetSearchTraining,header :String): Flow<Resource<out TrainingCenterRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getSelectedTrainingListAPI(header,getSearchTraining)
        }
    }


    suspend fun getCandidateDetailsAPI(candidateReq: CandidateReq,header :String): Flow<Resource<out CandidateDetails>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getCandidateDetailsAPI(header,candidateReq)
        }
    }


    suspend fun getImageChangeAPI(imageChangeReq: ImageChangeReq,header :String): Flow<Resource<out InsertRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getImageChangeAPI(header,imageChangeReq)
        }
    }


    suspend fun getChangePass(changePassReq: ChangePassReq,header :String): Flow<Resource<out InsertRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getChangePass(header,changePassReq)
        }
    }




    suspend fun aadhaarRekycApi(aadhaarRekycReq: AadhaarRekycReq,header :String): Flow<Resource<out AadhaarEkycRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.aadhaarRekycApi(header,aadhaarRekycReq)
        }
    }
    suspend fun getChangePassOtp(loginIdNdPassReq: GetLoginIdNdPassReq): Flow<Resource<out ForgotIdOtpRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getChangePassOtp(loginIdNdPassReq)
        }
    }

    suspend fun getLoginIdPass(loginIdNdPassReq: GetLoginIdNdPassReq): Flow<Resource<out InsertRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getLoginIdPass(loginIdNdPassReq)
        }
    }






    suspend fun getBankDetailsAPI(bankingReq: BankingReq,header :String): Flow<Resource<out BankingRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getBankDetailsAPI(header,bankingReq)
        }
    }



    suspend fun postOnAUAFaceAuthNREGA(url:String, uidaiKycRequest: UidaiKycRequest): Flow<Resource<out Response<UidaiResp>>> {
        return networkBoundResourceWithoutDb {
            appLevelApi.postOnAUAFaceAuthNREGA(url,uidaiKycRequest)
        }
    }


    suspend fun shgValidateAPI(url:String, shgValidateReq: ShgValidateReq): Flow<Resource<out Response<ShgValidateRes>>> {
        return networkBoundResourceWithoutDb {
            appLevelApi.shgValidateAPI(url,shgValidateReq)
        }
    }



    fun getCheckJobCardAPI( url: String, username: String, password: String,jobcardNo: String): Flow<Resource<out Response<JobcardResponse>>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.checkJobcard(url,username,password,jobcardNo)
        }
    }




}