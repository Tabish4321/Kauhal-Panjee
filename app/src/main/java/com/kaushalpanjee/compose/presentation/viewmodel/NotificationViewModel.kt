package com.kaushalpanjee.compose.presentation.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaushalpanjee.compose.domain.usecase.GetNotificationsUseCase
import com.kaushalpanjee.compose.domain.usecase.UpdateNotificationStatusUseCase
import com.kaushalpanjee.compose.presentation.contract.InvitationContract
import com.kaushalpanjee.core.util.AppUtil.createErrorResponse
import com.kaushalpanjee.core.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
/**
 * Created by Rishi Porwal
 */

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val updateNotificationStatusUseCase: UpdateNotificationStatusUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(InvitationContract.State())
    val state: StateFlow<InvitationContract.State> = _state.asStateFlow()

    private val _sideEffects = MutableSharedFlow<InvitationContract.SideEffect>()
    val sideEffects: SharedFlow<InvitationContract.SideEffect> = _sideEffects.asSharedFlow()

    private var isLoading = false

    fun onEvent(event: InvitationContract.Event) {
        when (event) {
            InvitationContract.Event.LoadNotifications -> loadNotifications(resetPagination = true)
            InvitationContract.Event.LoadMoreNotifications -> loadMoreNotifications()
            InvitationContract.Event.RefreshNotifications -> refreshNotifications()
            is InvitationContract.Event.UpdateNotificationStatus ->
                updateNotificationStatus(event.notificationId, event.status)
        }
    }

    private fun loadNotifications(resetPagination: Boolean = true) {
        if (isLoading) return

        val currentState = _state.value

        if (resetPagination) {
            _state.update {
                it.copy(
                    currentPage = 0,
                    isLastPage = false,
                    notifications = Resource.Loading(),
                    isLoadingMore = false,
                    isRefreshing = false
                )
            }
        } else {
            if (currentState.isLastPage) return
            _state.update { it.copy(isLoadingMore = true) }
        }

        isLoading = true
        val pageToLoad = if (resetPagination) 0 else currentState.currentPage

        viewModelScope.launch {
            getNotificationsUseCase(pageToLoad, 10).collectLatest { result ->
                isLoading = false
                _state.update { state ->
                    when (result) {
                        is Resource.Success -> {
                            val newItems = result.data ?: emptyList()
                            val isLastPage = newItems.isEmpty()
                            val nextPage = if (resetPagination) 1 else state.currentPage + 1

                            val currentList = (state.notifications as? Resource.Success)?.data ?: emptyList()
                            val updatedList = if (resetPagination) newItems else currentList + newItems

                            state.copy(
                                notifications = Resource.Success(updatedList),
                                currentPage = nextPage,
                                isLastPage = isLastPage,
                                isLoadingMore = false,
                                isRefreshing = false
                            )
                        }
                        is Resource.Error -> {
                            state.copy(
                                notifications = Resource.Error(createErrorResponse()),
                                isLoadingMore = false,
                                isRefreshing = false
                            )
                        }
                        is Resource.Loading -> state
                    }
                }

                when (result) {
                    is Resource.Error -> {
                        _sideEffects.emit(
                            InvitationContract.SideEffect.ShowError(
                                result.error!!.message ?: "Failed to load notifications"
                            )
                        )
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun loadMoreNotifications() {
        if (!_state.value.isLastPage && !isLoading) {
            loadNotifications(resetPagination = false)
        }
    }

    private fun refreshNotifications() {
        _state.update { it.copy(isRefreshing = true) }
        loadNotifications(resetPagination = true)
    }

    private fun updateNotificationStatus(id: String, status: String) {
        val item = (_state.value.notifications as? Resource.Success)
            ?.data?.find { it.id == id } ?: return

        _state.update {
            it.copy(actionLoadingIds = it.actionLoadingIds + id)
        }

        viewModelScope.launch {
            updateNotificationStatusUseCase(
                notificationId = id,
                candidateId = item.candidateId,
                instituteId = item.instituteId,
                status = status
            ).collectLatest { result ->

                _state.update {
                    it.copy(actionLoadingIds = it.actionLoadingIds - id)
                }

                when (result) {
                    is Resource.Success -> {
//                        _state.update {
//                            it.copy(actionLoadingIds = it.actionLoadingIds - id)
//                        }

                        _sideEffects.emit(
                            InvitationContract.SideEffect.ShowToast(result.data?:"Success")
                        )

//                        _state.update { state ->
//                            val list =
//                                (state.notifications as? Resource.Success)?.data.orEmpty()
//
//                            state.copy(
//                                notifications = Resource.Success(
//                                    list.filterNot { it.id == id }
//                                )
//                            )
//                        }// this for local item update ...if dont want load whole list
                        loadNotifications(true)
                    }

                    is Resource.Error -> {
                        _sideEffects.emit(
                            InvitationContract.SideEffect.ShowError(
                                result.error?.message ?: "Failed"
                            )
                        )
                    }

                    is Resource.Loading -> {
                        //  loader
                    }
                }
            }
        }
    }
}