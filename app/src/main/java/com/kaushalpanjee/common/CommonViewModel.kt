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
import com.kaushalpanjee.common.model.request.CandidateReq
import com.kaushalpanjee.common.model.request.ChangePassReq
import com.kaushalpanjee.common.model.request.EducationalInsertReq
import com.kaushalpanjee.common.model.request.EmploymentInsertReq
import com.kaushalpanjee.common.model.request.GetLoginIdNdPassReq
import com.kaushalpanjee.common.model.request.GetSearchTraining
import com.kaushalpanjee.common.model.request.ImageChangeReq
import com.kaushalpanjee.common.model.request.LoginReq
import com.kaushalpanjee.common.model.request.PersonalInsertReq
import com.kaushalpanjee.common.model.request.SeccInsertReq
import com.kaushalpanjee.common.model.request.SeccReq
import com.kaushalpanjee.common.model.request.SectionAndPerReq
import com.kaushalpanjee.common.model.request.ShgValidateReq
import com.kaushalpanjee.common.model.request.TechDomainReq
import com.kaushalpanjee.common.model.request.TechQualification
import com.kaushalpanjee.common.model.request.TradeReq
import com.kaushalpanjee.common.model.request.TrainingCenterReq
import com.kaushalpanjee.common.model.request.TrainingInsertReq
import com.kaushalpanjee.common.model.request.TrainingSearch
import com.kaushalpanjee.common.model.request.UserCreationReq
import com.kaushalpanjee.common.model.response.AadhaarDetailRes
import com.kaushalpanjee.common.model.response.BankingRes
import com.kaushalpanjee.common.model.response.CandidateDetails
import com.kaushalpanjee.common.model.response.CreateUserRes
import com.kaushalpanjee.common.model.response.ForgotIdOtpRes
import com.kaushalpanjee.common.model.response.InsertRes
import com.kaushalpanjee.common.model.response.JobcardResponse
import com.kaushalpanjee.common.model.response.LanguageList
import com.kaushalpanjee.common.model.response.LoginRes
import com.kaushalpanjee.common.model.response.SeccDetailsRes
import com.kaushalpanjee.common.model.response.SectionAndPer
import com.kaushalpanjee.common.model.response.SectorResponse
import com.kaushalpanjee.common.model.response.ShgValidateRes
import com.kaushalpanjee.common.model.response.TechQualificationRes
import com.kaushalpanjee.common.model.response.TechnicalEduDomain
import com.kaushalpanjee.common.model.response.TradeResponse
import com.kaushalpanjee.common.model.response.TrainingCenterRes
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

    private var _districtListPer =  MutableStateFlow<Resource<out DistrictResponse>>(Resource.Loading())
    val getDistrictListPer = _districtListPer.asStateFlow()


    fun getDistrictListPerApi(state :String){
        viewModelScope.launch {
            commonRepository.getDistrictPerListApi(state,BuildConfig.VERSION_NAME).collectLatest {
                _districtListPer.emit(it)
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


    private var _blockListPer =  MutableStateFlow<Resource<out BlockResponse>>(Resource.Loading())
    val getBlockListPer = _blockListPer.asStateFlow()


    fun getBlockPerListApi(district :String){
        viewModelScope.launch {
            commonRepository.getBlockPerListApi(district,BuildConfig.VERSION_NAME).collectLatest {
                _blockListPer.emit(it)
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


    private  var _gpListPer =  MutableStateFlow<Resource<out grampanchayatResponse>>(Resource.Loading())
    val getGpListPer = _gpListPer.asStateFlow()


    fun getGpPerListApi(block :String) {
        viewModelScope.launch {
            commonRepository.getGPPerListApi(block, BuildConfig.VERSION_NAME).collectLatest {
                _gpListPer.emit(it)
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


    private  var _villageListPer =  MutableStateFlow<Resource<out VillageResponse>>(Resource.Loading())
    val getVillageListPer = _villageListPer.asStateFlow()


    fun getVillagePerListApi(gp :String) {
        viewModelScope.launch {
            commonRepository.getVillagePerListApi(gp, BuildConfig.VERSION_NAME).collectLatest {
                _villageListPer.emit(it)
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

    private  var _getLoginAPI =  MutableStateFlow<Resource<out LoginRes>>(Resource.Loading())
    val getLoginAPI = _getLoginAPI.asStateFlow()


    fun getLoginAPI(loginReq: LoginReq) {
        viewModelScope.launch {
            commonRepository.getLoginAPI(loginReq).collectLatest {
                _getLoginAPI.emit(it)
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

    private  var _getLanguageListAPI =  MutableStateFlow<Resource<out LanguageList>>(Resource.Loading())
    val getLanguageListAPI = _getLanguageListAPI.asSharedFlow()


    fun getLanguageListAPI(){
        viewModelScope.launch {
            commonRepository.getLanguageListAPI().collectLatest {
                _getLanguageListAPI.emit(it)
            }
        }


    }
    private  var _getSectorListAPI =  MutableStateFlow<Resource<out SectorResponse>>(Resource.Loading())
    val getSectorListAPI = _getSectorListAPI.asSharedFlow()


    fun getSectorListAPI(techQualification: TechQualification){
        viewModelScope.launch {
            commonRepository.getSectorListAPI(techQualification).collectLatest {
                _getSectorListAPI.emit(it)
            }
        }


    }

    private  var _getTradeListAPI =  MutableStateFlow<Resource<out TradeResponse>>(Resource.Loading())
    val getTradeListAPI = _getTradeListAPI.asSharedFlow()


    fun getTradeListAPI(tradeReq: TradeReq){
        viewModelScope.launch {
            commonRepository.getTradeListAPI(tradeReq).collectLatest {
                _getTradeListAPI.emit(it)
            }
        }


    }

    private  var _getTrainingSearchAPI =  MutableStateFlow<Resource<out TrainingCenterRes>>(Resource.Loading())
    val getTrainingSearchAPI = _getTrainingSearchAPI.asSharedFlow()


    fun getTrainingSearchAPI(trainingSearch: TrainingSearch){
        viewModelScope.launch {
            commonRepository.getTrainingSearchAPI(trainingSearch).collectLatest {
                _getTrainingSearchAPI.emit(it)
            }
        }


    }


    private  var _getTrainingListAPI =  MutableStateFlow<Resource<out TrainingCenterRes>>(Resource.Loading())
    val getTrainingListAPI = _getTrainingListAPI.asSharedFlow()


    fun getTrainingListAPI(trainingCenterReq: TrainingCenterReq){
        viewModelScope.launch {
            commonRepository.getTrainingListAPI(trainingCenterReq).collectLatest {
                _getTrainingListAPI.emit(it)
            }
        }


    }


    private  var _getSelectedTrainingListAPI =  MutableStateFlow<Resource<out TrainingCenterRes>>(Resource.Loading())
    val getSelectedTrainingListAPI = _getSelectedTrainingListAPI.asSharedFlow()



    fun getSelectedTrainingListAPI(getSearchTraining: GetSearchTraining){
        viewModelScope.launch {
            commonRepository.getSelectedTrainingListAPI(getSearchTraining).collectLatest {
                _getSelectedTrainingListAPI.emit(it)
            }
        }


    }



    private  var _getCandidateDetailsAPI =  MutableStateFlow<Resource<out CandidateDetails>>(Resource.Loading())
    val getCandidateDetailsAPI = _getCandidateDetailsAPI.asSharedFlow()


    fun getCandidateDetailsAPI(candidateReq: CandidateReq){
        viewModelScope.launch {
            commonRepository.getCandidateDetailsAPI(candidateReq).collectLatest {
                _getCandidateDetailsAPI.emit(it)
            }
        }


    }


    private  var _getImageChangeAPI =  MutableStateFlow<Resource<out InsertRes>>(Resource.Loading())
    val getImageChangeAPI = _getImageChangeAPI.asSharedFlow()


    fun getImageChangeAPI(imageChangeReq: ImageChangeReq){
        viewModelScope.launch {
            commonRepository.getImageChangeAPI(imageChangeReq).collectLatest {
                _getImageChangeAPI.emit(it)
            }
        }


    }


    private  var _getChangePass =  MutableStateFlow<Resource<out InsertRes>>(Resource.Loading())
    val getChangePass = _getChangePass.asSharedFlow()


    fun getChangePass(changePassReq: ChangePassReq){
        viewModelScope.launch {
            commonRepository.getChangePass(changePassReq).collectLatest {
                _getChangePass.emit(it)
            }
        }

    }


    private  var _getChangePassOtp =  MutableStateFlow<Resource<out ForgotIdOtpRes>>(Resource.Loading())
    val getChangePassOtp = _getChangePassOtp.asSharedFlow()


    fun getChangePassOtp(getLoginIdNdPassReq: GetLoginIdNdPassReq){
        viewModelScope.launch {
            commonRepository.getChangePassOtp(getLoginIdNdPassReq).collectLatest {
                _getChangePassOtp.emit(it)
            }
        }

    }


    private  var _getLoginIdPass =  MutableStateFlow<Resource<out InsertRes>>(Resource.Loading())
    val getLoginIdPass = _getLoginIdPass.asSharedFlow()


    fun getLoginIdPass(getLoginIdNdPassReq: GetLoginIdNdPassReq){
        viewModelScope.launch {
            commonRepository.getLoginIdPass(getLoginIdNdPassReq).collectLatest {
                _getLoginIdPass.emit(it)
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
    private var _nRegaValidate = MutableSharedFlow<Resource<out Response<JobcardResponse>>>()
    val nRegaValidate = _nRegaValidate.asSharedFlow()

    fun getCheckJobCardAPI( url: String,username: String, password: String,jobcardNo: String){
        viewModelScope.launch {

            commonRepository.getCheckJobCardAPI(url,username,password,jobcardNo).collectLatest {
                _nRegaValidate.emit(it)
            }
        }
    }




}