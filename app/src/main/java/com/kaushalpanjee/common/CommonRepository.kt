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
import com.kaushalpanjee.common.model.request.AddressInsertReq
import com.kaushalpanjee.common.model.request.AdharDetailsReq
import com.kaushalpanjee.common.model.request.BankingInsertReq
import com.kaushalpanjee.common.model.request.BankingReq
import com.kaushalpanjee.common.model.request.CandidateReq
import com.kaushalpanjee.common.model.request.ChangePassReq
import com.kaushalpanjee.common.model.request.EducationalInsertReq
import com.kaushalpanjee.common.model.request.EmploymentInsertReq
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
import com.kaushalpanjee.common.model.request.TradeReq
import com.kaushalpanjee.common.model.request.TrainingCenterReq
import com.kaushalpanjee.common.model.request.TrainingInsertReq
import com.kaushalpanjee.common.model.request.TrainingSearch
import com.kaushalpanjee.common.model.request.UserCreationReq
import com.kaushalpanjee.common.model.response.AadhaarDetailRes
import com.kaushalpanjee.common.model.response.BankingRes
import com.kaushalpanjee.common.model.response.CandidateDetails
import com.kaushalpanjee.common.model.response.CreateUserRes
import com.kaushalpanjee.common.model.response.ForgotIdOtpRes
import com.kaushalpanjee.common.model.response.InsertRes
import com.kaushalpanjee.common.model.response.JobcardResponse
import com.kaushalpanjee.common.model.response.LanguageList
import com.kaushalpanjee.common.model.response.LoginRes
import com.kaushalpanjee.common.model.response.SeccDetailsRes
import com.kaushalpanjee.common.model.response.SectionAndPer
import com.kaushalpanjee.common.model.response.SectorResponse
import com.kaushalpanjee.common.model.response.ShgValidateRes
import com.kaushalpanjee.common.model.response.TechQualificationRes
import com.kaushalpanjee.common.model.response.TechnicalEduDomain
import com.kaushalpanjee.common.model.response.TradeResponse
import com.kaushalpanjee.common.model.response.TrainingCenterRes
import com.kaushalpanjee.common.model.response.WhereHaveYouHeardRes
import javax.inject.Inject

class CommonRepository @Inject constructor(
    @AppModule.PreLoginAppLevelApi private val appLevelApi: AppLevelApi,
    private val database: AppDatabase
    ){

    suspend fun sendMobileOTP(mobileNumber : String, appVersion :String): Flow<Resource<out SendMobileOTPResponse>> {
       return networkBoundResourceWithoutDb {
            appLevelApi.sendMobileOTP(SendOTPRequest(mobileNumber,appVersion))
        }
    }

    suspend fun sendEmailOTP(email : String,appVersion: String): Flow<Resource<out SendMobileOTPResponse>> {
        return networkBoundResourceWithoutDb {
            appLevelApi.sendEmailTP(SendOtpEmailReq(email,appVersion))
        }
    }

    suspend fun getTechEducationAPI(appVersion: String): Flow<Resource<out TechQualificationRes>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getTechEducationAPI(TechQualification(appVersion))
        }

    }

    suspend fun getTechEducationDomainAPI(appVersion: String,qual : String): Flow<Resource<out TechnicalEduDomain>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getTechEducationDomainAPI(TechDomainReq(appVersion,qual))
        }

    }

    suspend fun getStateListApi(appVersion: String): Flow<Resource<out StateDataResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getStateListAPI(StateListReq(appVersion))
        }
    }



    suspend fun getWhereHaveYouHeardAPI(appVersion: String): Flow<Resource<out WhereHaveYouHeardRes>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getWhereHaveYouHeardAPI(StateListReq(appVersion))
        }
    }

    suspend fun getDistrictListApi(stateCode: String,appVersion: String): Flow<Resource<out DistrictResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getDistrictListAPI(DistrictReq(stateCode, appVersion))
        }
    }


    suspend fun getDistrictPerListApi(stateCode: String,appVersion: String): Flow<Resource<out DistrictResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getDistrictPreListAPI(DistrictReq(stateCode, appVersion))
        }
    }



    suspend fun getCreateUserAPI(userCreationReq: UserCreationReq): Flow<Resource<out CreateUserRes>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getCreateUserAPI(userCreationReq)
        }
    }


    suspend fun getBlockListApi(districtCode: String,appVersion: String): Flow<Resource<out BlockResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getBlockListAPI(BlockReq(districtCode, appVersion))
        }
    }


    suspend fun getBlockPerListApi(districtCode: String,appVersion: String): Flow<Resource<out BlockResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getBlockPerListAPI(BlockReq(districtCode, appVersion))
        }
    }



    suspend fun getGPListApi(blockCode: String,appVersion: String): Flow<Resource<out grampanchayatResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getGpListAPI(GramPanchayatReq(blockCode, appVersion))
        }
    }


    suspend fun getGPPerListApi(blockCode: String,appVersion: String): Flow<Resource<out grampanchayatResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getGpPerListAPI(GramPanchayatReq(blockCode, appVersion))
        }
    }




    suspend fun getVillageListApi(gpCode: String,appVersion: String): Flow<Resource<out VillageResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getVillageListAPI(VillageReq(gpCode, appVersion))
        }
    }


    suspend fun getVillagePerListApi(gpCode: String,appVersion: String): Flow<Resource<out VillageResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getVillagePerListAPI(VillageReq(gpCode, appVersion))
        }
    }



    suspend fun getAadhaarListAPI(adharDetailsReq: AdharDetailsReq): Flow<Resource<out AadhaarDetailRes>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getAadhaarListAPI(adharDetailsReq)
        }
    }

    suspend fun getSeccListAPI(seccReq: SeccReq): Flow<Resource<out SeccDetailsRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getSeccListAPI(seccReq)
        }
    }


    suspend fun getSecctionAndPerAPI(sectionAndPerReq: SectionAndPerReq): Flow<Resource<out SectionAndPer>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getSecctionAndPerAPI(sectionAndPerReq)
        }
    }


    suspend fun insertPersonalDataAPI(personalInsertReq: PersonalInsertReq): Flow<Resource<out InsertRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.insertPersonalDataAPI(personalInsertReq)
        }
    }



    suspend fun insertAddressAPI(addressInsertReq: AddressInsertReq): Flow<Resource<out InsertRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.insertAddressAPI(addressInsertReq)
        }
    }

    suspend fun insertSeccAPI(seccInsertReq: SeccInsertReq): Flow<Resource<out InsertRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.insertSeccAPI(seccInsertReq)
        }
    }


    suspend fun insertEducationAPI(educationalInsertReq: EducationalInsertReq): Flow<Resource<out InsertRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.insertEducatio0nAPI(educationalInsertReq)
        }
    }

    suspend fun insertEmploymentAPI(employmentInsertReq: EmploymentInsertReq): Flow<Resource<out InsertRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.insertEmploymentAPI(employmentInsertReq)
        }
    }


    suspend fun insertTrainingAPI(trainingInsertReq: TrainingInsertReq): Flow<Resource<out InsertRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.insertTrainingAPI(trainingInsertReq)
        }
    }



    suspend fun insertBankingAPI(bankingInsertReq: BankingInsertReq): Flow<Resource<out InsertRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.insertBankingAPI(bankingInsertReq)
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



    suspend fun getSectorListAPI(techQualification: TechQualification): Flow<Resource<out SectorResponse>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getSectorListAPI(techQualification)
        }
    }


    suspend fun getTradeListAPI(tradeReq: TradeReq): Flow<Resource<out TradeResponse>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getTradeListAPI(tradeReq)
        }
    }




    suspend fun getTrainingSearchAPI(trainingSearch: TrainingSearch): Flow<Resource<out TrainingCenterRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getTrainingSearchAPI(trainingSearch)
        }
    }




    suspend fun getTrainingListAPI(trainingCenterReq: TrainingCenterReq): Flow<Resource<out TrainingCenterRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getTrainingListAPI(trainingCenterReq)
        }
    }



    suspend fun getSelectedTrainingListAPI(getSearchTraining: GetSearchTraining): Flow<Resource<out TrainingCenterRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getSelectedTrainingListAPI(getSearchTraining)
        }
    }


    suspend fun getCandidateDetailsAPI(candidateReq: CandidateReq): Flow<Resource<out CandidateDetails>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getCandidateDetailsAPI(candidateReq)
        }
    }


    suspend fun getImageChangeAPI(imageChangeReq: ImageChangeReq): Flow<Resource<out InsertRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getImageChangeAPI(imageChangeReq)
        }
    }


    suspend fun getChangePass(changePassReq: ChangePassReq): Flow<Resource<out InsertRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getChangePass(changePassReq)
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






    suspend fun getBankDetailsAPI(bankingReq: BankingReq): Flow<Resource<out BankingRes>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getBankDetailsAPI(bankingReq)
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