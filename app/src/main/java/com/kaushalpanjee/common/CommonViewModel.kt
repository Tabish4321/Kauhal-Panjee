package com.kaushalpanjee.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaushalpanjee.BuildConfig
import com.kaushalpanjee.common.model.SendMobileOTPResponse
import com.kaushalpanjee.common.model.StateDataResponse
import com.kaushalpanjee.common.model.UidaiKycRequest
import com.kaushalpanjee.common.model.response.BlockResponse
import com.kaushalpanjee.common.model.response.DistrictList
import com.kaushalpanjee.common.model.response.DistrictResponse
import com.kaushalpanjee.common.model.response.GrampanchayatList
import com.kaushalpanjee.common.model.response.VillageResponse
import com.kaushalpanjee.common.model.response.grampanchayatResponse
import com.kaushalpanjee.core.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Response
import com.kaushalpanjee.common.model.UidaiResp
import com.kaushalpanjee.common.model.request.ShgValidateReq
import com.kaushalpanjee.common.model.response.ShgValidateRes
import com.kaushalpanjee.common.model.response.TechQualificationRes
import com.kaushalpanjee.common.model.response.TechnicalEduDomain
import com.kaushalpanjee.core.util.AppConstant
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(private val commonRepository: CommonRepository) :ViewModel() {


    private var _sendMobileOTP = MutableSharedFlow<Resource<out SendMobileOTPResponse>>()
    val sendMobileOTP = _sendMobileOTP.asSharedFlow()


    fun sendMobileOTP(mobileNumber:String , appVersion:String){
        viewModelScope.launch {
            commonRepository.sendMobileOTP(mobileNumber,appVersion).collectLatest {
                _sendMobileOTP.emit(it)
            }
        }


    }

    private var _sendEmailOTP = MutableSharedFlow<Resource<out SendMobileOTPResponse>>()
    val sendEmailOTP = _sendEmailOTP.asSharedFlow()

    fun sendEmailOTP(email:String,appVersion: String){
        viewModelScope.launch {
            commonRepository.sendEmailOTP(email,appVersion).collectLatest {
                _sendEmailOTP.emit(it)
            }
        }}

         private var _techEducation = MutableSharedFlow<Resource<out TechQualificationRes>>()
         val techEducation = _techEducation.asSharedFlow()

         fun getTechEducation(appVersion: String){
            viewModelScope.launch {
            commonRepository.getTechEducationAPI(BuildConfig.VERSION_NAME).collectLatest {
                _techEducation.emit(it)
            }
        }

    }


    private var _techEducationDomain = MutableSharedFlow<Resource<out TechnicalEduDomain>>()
    val techEducationDomain = _techEducationDomain.asSharedFlow()

    fun getTechEducationDomainAPI(appVersion: String,qual :String){
        viewModelScope.launch {
            commonRepository.getTechEducationDomainAPI(BuildConfig.VERSION_NAME,qual).collectLatest {
                _techEducationDomain.emit(it)
            }
        }

    }

    private var _stateList =  MutableStateFlow<Resource<out StateDataResponse>>(Resource.Loading())
    val getStateList = _stateList.asStateFlow()


    fun getStateListApi(){
        viewModelScope.launch {
            commonRepository.getStateListApi(BuildConfig.VERSION_NAME).collectLatest {
                _stateList.emit(it)
            }
        }
    }


    private var _districtList =  MutableStateFlow<Resource<out DistrictResponse>>(Resource.Loading())
    val getDistrictList = _districtList.asStateFlow()


    fun getDistrictListApi(state :String){
        viewModelScope.launch {
            commonRepository.getDistrictListApi(state,BuildConfig.VERSION_NAME).collectLatest {
                _districtList.emit(it)
            }
        }
    }



    private var _blockList =  MutableStateFlow<Resource<out BlockResponse>>(Resource.Loading())
    val getBlockList = _blockList.asStateFlow()


    fun getBlockListApi(district :String){
        viewModelScope.launch {
            commonRepository.getBlockListApi(district,BuildConfig.VERSION_NAME).collectLatest {
                _blockList.emit(it)
            }
        }}


      private  var _gpList =  MutableStateFlow<Resource<out grampanchayatResponse>>(Resource.Loading())
        val getGpList = _gpList.asStateFlow()


        fun getGpListApi(block :String) {
            viewModelScope.launch {
                commonRepository.getGPListApi(block, BuildConfig.VERSION_NAME).collectLatest {
                    _gpList.emit(it)
                }
            }


        }



    private  var _villageList =  MutableStateFlow<Resource<out VillageResponse>>(Resource.Loading())
    val getVillageList = _villageList.asStateFlow()


    fun getVillageListApi(gp :String) {
        viewModelScope.launch {
            commonRepository.getVillageListApi(gp, BuildConfig.VERSION_NAME).collectLatest {
                _villageList.emit(it)
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


    private var _shgValidate = MutableSharedFlow<Resource<out Response<ShgValidateRes>>>()
    val shgValidate = _shgValidate.asSharedFlow()


    fun shgValidateAPI(shgValidateReq: ShgValidateReq){
        viewModelScope.launch {
            commonRepository.shgValidateAPI("https://nrlm.gov.in/nrlmwebservice/services/ddugky/dataVerify", shgValidateReq).collectLatest {
                _shgValidate.emit(it)
            }
        }
    }


}