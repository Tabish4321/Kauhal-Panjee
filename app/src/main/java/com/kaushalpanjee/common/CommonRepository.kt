package com.kaushalpanjee.common

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
import com.kaushalpanjee.common.model.request.ShgValidateReq
import com.kaushalpanjee.common.model.request.TechDomainReq
import com.kaushalpanjee.common.model.request.TechQualification
import com.kaushalpanjee.common.model.response.ShgValidateRes
import com.kaushalpanjee.common.model.response.TechQualificationRes
import com.kaushalpanjee.common.model.response.TechnicalEduDomain
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




    suspend fun getDistrictListApi(stateCode: String,appVersion: String): Flow<Resource<out DistrictResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getDistrictListAPI(DistrictReq(stateCode, appVersion))
        }
    }


    suspend fun getBlockListApi(districtCode: String,appVersion: String): Flow<Resource<out BlockResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getBlockListAPI(BlockReq(districtCode, appVersion))
        }
    }



    suspend fun getGPListApi(blockCode: String,appVersion: String): Flow<Resource<out grampanchayatResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getGpListAPI(GramPanchayatReq(blockCode, appVersion))
        }
    }



    suspend fun getVillageListApi(gpCode: String,appVersion: String): Flow<Resource<out VillageResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getVillageListAPI(VillageReq(gpCode, appVersion))
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

}