package com.kaushalpanjee.compose.data.repository

import com.kaushalpanjee.common.model.request.ChangePassReq
import com.kaushalpanjee.common.model.response.InsertRes
import com.kaushalpanjee.compose.data.mapper.toUiModel
import com.kaushalpanjee.compose.data.network.safeApiFlow
import com.kaushalpanjee.compose.domain.repository.ChangePasswordRepository
import com.kaushalpanjee.compose.ui.model.NotificationUiModel
import com.kaushalpanjee.core.data.remote.AppLevelApi
import com.kaushalpanjee.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChangePassWordRepoImpl@Inject constructor(
    private val apiService: AppLevelApi
)  : ChangePasswordRepository {

    override fun getChangePass(
        changePassReq: ChangePassReq,
        header: String
    ) :Flow<Resource<out InsertRes>> =
        safeApiFlow(
           apiCall = { apiService.getChangePass(header, changePassReq) },
            mapper =  { it-> it }
        )
}