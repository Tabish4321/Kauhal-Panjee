package com.kaushalpanjee.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaushalpanjee.common.model.SendMobileOTPResponse
import com.kaushalpanjee.common.model.StateDataResponse
import com.kaushalpanjee.common.model.UidaiKycRequest
import com.kaushalpanjee.core.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Response
import rural.ekyc.ui.ekyc.models.UidaiResp
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(private val commonRepository: CommonRepository) :ViewModel() {


    private var _sendMobileOTP = MutableSharedFlow<Resource<out SendMobileOTPResponse>>()
    val sendMobileOTP = _sendMobileOTP.asSharedFlow()


    fun sendMobileOTP(mobileNumber:String){
        viewModelScope.launch {
            commonRepository.sendMobileOTP(mobileNumber).collectLatest {
                _sendMobileOTP.emit(it)
            }
        }


    }

    private var _sendEmailOTP = MutableSharedFlow<Resource<out SendMobileOTPResponse>>()
    val sendEmailOTP = _sendEmailOTP.asSharedFlow()

    fun sendEmailOTP(email:String){
        viewModelScope.launch {
            commonRepository.sendEmailOTP(email).collectLatest {
                _sendEmailOTP.emit(it)
            }
        }}

    private var _stateList =  MutableStateFlow<Resource<out StateDataResponse>>(Resource.Loading())
    val getStateList = _stateList.asStateFlow()


    fun getStateListApi(){
        viewModelScope.launch {
            commonRepository.getStateListApi().collectLatest {
                _stateList.emit(it)
            }
        }
    }

    private var _postOnAUAFaceAuthNREGA = MutableSharedFlow<Resource<out Response<UidaiResp>>>()
    val postOnAUAFaceAuthNREGA = _postOnAUAFaceAuthNREGA.asSharedFlow()


    fun postOnAUAFaceAuthNREGA(url:String, uidaiKycRequest: UidaiKycRequest){
        viewModelScope.launch {
            commonRepository.postOnAUAFaceAuthNREGA(url, uidaiKycRequest).collectLatest {
                _postOnAUAFaceAuthNREGA.emit(it)
            }
        }
    }


}