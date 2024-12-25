package com.kaushalpanjee.common

import com.kaushalpanjee.common.model.SendMobileOTPResponse
import com.kaushalpanjee.common.model.StateDataResponse
import com.kaushalpanjee.common.model.UidaiKycRequest
import com.kaushalpanjee.core.data.local.database.AppDatabase
import com.kaushalpanjee.core.data.remote.AppLevelApi
import com.kaushalpanjee.core.di.AppModule
import com.kaushalpanjee.core.util.Resource
import com.kaushalpanjee.core.util.networkBoundResourceWithoutDb
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import com.kaushalpanjee.common.model.UidaiResp
import javax.inject.Inject

class CommonRepository @Inject constructor(
    @AppModule.PreLoginAppLevelApi private val appLevelApi: AppLevelApi,
    private val database: AppDatabase
    ){

    suspend fun sendMobileOTP(mobileNumber : String): Flow<Resource<out SendMobileOTPResponse>> {
       return networkBoundResourceWithoutDb {
            appLevelApi.sendMobileOTP(mobileNumber)
        }
    }

    suspend fun sendEmailOTP(email : String): Flow<Resource<out SendMobileOTPResponse>> {
        return networkBoundResourceWithoutDb {
            appLevelApi.sendEmailTP(email)
        }
    }

    suspend fun getStateListApi(): Flow<Resource<out StateDataResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getStateListAPI()
        }
    }


    suspend fun postOnAUAFaceAuthNREGA(url:String, uidaiKycRequest: UidaiKycRequest): Flow<Resource<out Response<UidaiResp>>> {
        return networkBoundResourceWithoutDb {
            appLevelApi.postOnAUAFaceAuthNREGA(url,uidaiKycRequest)
        }
    }

}