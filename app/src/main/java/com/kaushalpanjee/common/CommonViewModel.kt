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
import com.kaushalpanjee.common.model.request.AddressInsertReq
import com.kaushalpanjee.common.model.request.AdharDetailsReq
import com.kaushalpanjee.common.model.request.BankingInsertReq
import com.kaushalpanjee.common.model.request.BankingReq
import com.kaushalpanjee.common.model.request.EducationalInsertReq
import com.kaushalpanjee.common.model.request.EmploymentInsertReq
import com.kaushalpanjee.common.model.request.PersonalInsertReq
import com.kaushalpanjee.common.model.request.SeccInsertReq
import com.kaushalpanjee.common.model.request.SeccReq
import com.kaushalpanjee.common.model.request.SectionAndPerReq
import com.kaushalpanjee.common.model.request.ShgValidateReq
import com.kaushalpanjee.common.model.request.TrainingInsertReq
import com.kaushalpanjee.common.model.request.UserCreationReq
import com.kaushalpanjee.common.model.response.AadhaarDetailRes
import com.kaushalpanjee.common.model.response.BankingRes
import com.kaushalpanjee.common.model.response.CreateUserRes
import com.kaushalpanjee.common.model.response.InsertRes
import com.kaushalpanjee.common.model.response.SeccDetailsRes
import com.kaushalpanjee.common.model.response.SectionAndPer
import com.kaushalpanjee.common.model.response.ShgValidateRes
import com.kaushalpanjee.common.model.response.TechQualificationRes
import com.kaushalpanjee.common.model.response.TechnicalEduDomain
import com.kaushalpanjee.common.model.response.WhereHaveYouHeardRes
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

    private var _WhereHaveYouHeard =  MutableStateFlow<Resource<out WhereHaveYouHeardRes>>(Resource.Loading())
    val getWhereHaveYouHeard = _WhereHaveYouHeard.asStateFlow()



    fun getWhereHaveYouHeardAPI(){
        viewModelScope.launch {
            commonRepository.getWhereHaveYouHeardAPI(BuildConfig.VERSION_NAME).collectLatest {
                _WhereHaveYouHeard.emit(it)
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


    private var _userCreation =  MutableStateFlow<Resource<out CreateUserRes>>(Resource.Loading())
    val getuserCreation = _userCreation.asStateFlow()


    fun getCreateUserAPI(userCreationReq: UserCreationReq){
        viewModelScope.launch {
            commonRepository.getCreateUserAPI(userCreationReq).collectLatest {
                _userCreation.emit(it)
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



    private  var _aadhaarList =  MutableStateFlow<Resource<out AadhaarDetailRes>>(Resource.Loading())
    val getAadhaarList = _aadhaarList.asStateFlow()


    fun getAadhaarListAPI(adharDetailsReq: AdharDetailsReq) {
        viewModelScope.launch {
            commonRepository.getAadhaarListAPI(adharDetailsReq).collectLatest {
                _aadhaarList.emit(it)
            }
        }


    }


    private  var _seccListAPI =  MutableStateFlow<Resource<out SeccDetailsRes>>(Resource.Loading())
    val getSeccListAPI = _seccListAPI.asStateFlow()


    fun getSeccListAPI(seccReq: SeccReq) {
        viewModelScope.launch {
            commonRepository.getSeccListAPI(seccReq).collectLatest {
                _seccListAPI.emit(it)
            }
        }


    }


    private  var _getSecctionAndPerAPI =  MutableStateFlow<Resource<out SectionAndPer>>(Resource.Loading())
    val getSecctionAndPerAPI = _getSecctionAndPerAPI.asStateFlow()


    fun getSecctionAndPerAPI(sectionAndPerReq: SectionAndPerReq) {
        viewModelScope.launch {
            commonRepository.getSecctionAndPerAPI(sectionAndPerReq).collectLatest {
                _getSecctionAndPerAPI.emit(it)
            }
        }


    }



    private  var _insertPersonalDataAPI =  MutableStateFlow<Resource<out InsertRes>>(Resource.Loading())
    val insertPersonalDataAPI = _insertPersonalDataAPI.asStateFlow()


    fun insertPersonalDataAPI(personalInsertReq: PersonalInsertReq) {
        viewModelScope.launch {
            commonRepository.insertPersonalDataAPI(personalInsertReq).collectLatest {
                _insertPersonalDataAPI.emit(it)
            }
        }


    }


    private  var _insertAddressAPI =  MutableStateFlow<Resource<out InsertRes>>(Resource.Loading())
    val insertAddressAPI = _insertAddressAPI.asStateFlow()


    fun insertAddressAPI(addressInsertReq: AddressInsertReq) {
        viewModelScope.launch {
            commonRepository.insertAddressAPI(addressInsertReq).collectLatest {
                _insertAddressAPI.emit(it)
            }
        }


    }

    private  var _insertSeccAPI =  MutableStateFlow<Resource<out InsertRes>>(Resource.Loading())
    val insertSeccAPI = _insertSeccAPI.asStateFlow()


    fun insertSeccAPI(seccInsertReq: SeccInsertReq) {
        viewModelScope.launch {
            commonRepository.insertSeccAPI(seccInsertReq).collectLatest {
                _insertSeccAPI.emit(it)
            }
        }


    }



    private  var _insertEducationAPI =  MutableStateFlow<Resource<out InsertRes>>(Resource.Loading())
    val insertEducationAPI = _insertEducationAPI.asStateFlow()


    fun insertEducationAPI(educationalInsertReq: EducationalInsertReq) {
        viewModelScope.launch {
            commonRepository.insertEducationAPI(educationalInsertReq).collectLatest {
                _insertEducationAPI.emit(it)
            }
        }


    }

    private  var _insertEmploymentAPI =  MutableStateFlow<Resource<out InsertRes>>(Resource.Loading())
    val insertEmploymentAPI = _insertEmploymentAPI.asStateFlow()


    fun insertEmploymentAPI(employmentInsertReq: EmploymentInsertReq) {
        viewModelScope.launch {
            commonRepository.insertEmploymentAPI(employmentInsertReq).collectLatest {
                _insertEmploymentAPI.emit(it)
            }
        }


    }


    private  var _insertTrainingAPI =  MutableStateFlow<Resource<out InsertRes>>(Resource.Loading())
    val insertTrainingAPI = _insertTrainingAPI.asStateFlow()


    fun insertTrainingAPI(trainingInsertReq: TrainingInsertReq) {
        viewModelScope.launch {
            commonRepository.insertTrainingAPI(trainingInsertReq).collectLatest {
                _insertTrainingAPI.emit(it)
            }
        }


    }




    private  var _insertBankingAPI =  MutableStateFlow<Resource<out InsertRes>>(Resource.Loading())
    val insertBankingAPI = _insertBankingAPI.asStateFlow()


    fun insertBankingAPI(bankingInsertReq: BankingInsertReq) {
        viewModelScope.launch {
            commonRepository.insertBankingAPI(bankingInsertReq).collectLatest {
                _insertBankingAPI.emit(it)
            }
        }


    }



    private  var _getBankDetailsAPI =  MutableStateFlow<Resource<out BankingRes>>(Resource.Loading())
    val getBankDetailsAPI = _getBankDetailsAPI.asSharedFlow()


    fun getBankDetailsAPI(bankingReq: BankingReq ){
        viewModelScope.launch {
            commonRepository.getBankDetailsAPI(bankingReq).collectLatest {
                _getBankDetailsAPI.emit(it)
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