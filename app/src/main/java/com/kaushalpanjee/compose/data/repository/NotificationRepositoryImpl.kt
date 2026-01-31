package com.kaushalpanjee.compose.data.repository



import com.kaushalpanjee.compose.data.mapper.toUiModel
import com.kaushalpanjee.compose.data.remote.request.InvitationApprovalRequest
import com.kaushalpanjee.compose.domain.repository.NotificationRepository
import com.kaushalpanjee.compose.ui.model.NotificationUiModel
import com.kaushalpanjee.core.data.remote.AppLevelApi
import com.kaushalpanjee.core.util.AppUtil.createErrorResponse
import com.kaushalpanjee.core.util.Resource
import com.utilize.core.domain.model.response.BaseErrorResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
/**
 * Created by Rishi Porwal
 */
class NotificationRepositoryImpl @Inject constructor(
    private val apiService: AppLevelApi
) : NotificationRepository {

    override fun getNotifications(page: Int, size: Int): Flow<Resource<List<NotificationUiModel>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = apiService.getNotifications(page, size)
                val notifications = response.content?.map { it.toUiModel() } ?: emptyList()
                emit(Resource.Success(notifications))
            } catch (e: Exception) {
                emit(Resource.Error(createErrorResponse(e)))
            }
        }
    }

    override fun updateNotificationStatus(
        notificationId: String,
        candidateId: String?,
        instituteId: String?,
        status: String
    ): Flow<Resource<String>> =flow {
            emit(Resource.Loading())
            try {
                val request = InvitationApprovalRequest(
                    scheme = "RSETI",
                    candidateId = candidateId,
                    status = status,
                    instituteId = instituteId,
                    instituteName = "",
                    instituteTrade = "",
                    centerName = "",
                    centerTrade = "",
                    entryCode = ""
                )
                    apiService.updateNotificationStatus(request)
                    emit(Resource.Success("Success"))
                } catch (e: Exception) {
                    emit(Resource.Error(createErrorResponse(e)))
                }

        }

    }




