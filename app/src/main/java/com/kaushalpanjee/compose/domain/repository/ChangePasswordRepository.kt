package com.kaushalpanjee.compose.domain.repository

import com.kaushalpanjee.common.model.request.ChangePassReq
import com.kaushalpanjee.common.model.response.InsertRes
import com.kaushalpanjee.core.util.Resource
import kotlinx.coroutines.flow.Flow


interface ChangePasswordRepository {
   fun  getChangePass(changePassReq: ChangePassReq, header: String): Flow<Resource<out InsertRes>>

}