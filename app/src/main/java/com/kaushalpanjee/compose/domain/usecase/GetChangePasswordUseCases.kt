package com.kaushalpanjee.compose.domain.usecase

import com.kaushalpanjee.common.model.request.ChangePassReq
import com.kaushalpanjee.common.model.response.InsertRes
import com.kaushalpanjee.compose.domain.repository.ChangePasswordRepository
import com.kaushalpanjee.compose.domain.repository.NotificationRepository
import com.kaushalpanjee.compose.ui.model.NotificationUiModel
import com.kaushalpanjee.core.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChangePasswordUseCases @Inject constructor(
    private val repository: ChangePasswordRepository
) {
    operator fun invoke(changePassword: ChangePassReq,header:String):  Flow<Resource<out InsertRes>> {
        return repository.getChangePass(changePassword,header)
    }
}




