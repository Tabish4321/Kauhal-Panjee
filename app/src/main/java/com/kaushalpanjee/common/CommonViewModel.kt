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
import com.kaushalpanjee.common.model.request.AadhaarCheckReq
import com.kaushalpanjee.common.model.request.AadhaarRekycReq
import com.kaushalpanjee.common.model.request.AddressInsertReq
import com.kaushalpanjee.common.model.request.AdharDetailsReq
import com.kaushalpanjee.common.model.request.BankingInsertReq
import com.kaushalpanjee.common.model.request.BankingReq
import com.kaushalpanjee.common.model.request.BannerReq
import com.kaushalpanjee.common.model.request.CandidateReq
import com.kaushalpanjee.common.model.request.ChangePassReq
import com.kaushalpanjee.common.model.request.EducationalInsertReq
import com.kaushalpanjee.common.model.request.EmploymentInsertReq
import com.kaushalpanjee.common.model.request.FaceCheckReq
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
import com.kaushalpanjee.common.model.request.UpdatePasswordForReq
import com.kaushalpanjee.common.model.request.UserCreationReq
import com.kaushalpanjee.common.model.request.ValidateOtpReq
import com.kaushalpanjee.common.model.response.AadhaarCheckForRes
import com.kaushalpanjee.common.model.response.AadhaarCheckRes
import com.kaushalpanjee.common.model.response.AadhaarDetailRes
import com.kaushalpanjee.common.model.response.AadhaarEkycRes
import com.kaushalpanjee.common.model.response.BankingRes
import com.kaushalpanjee.common.model.response.BannerResponse
import com.kaushalpanjee.common.model.response.CandidateDetails
import com.kaushalpanjee.common.model.response.CreateUserRes
import com.kaushalpanjee.common.model.response.FaceResponse
import com.kaushalpanjee.common.model.response.ForgotIdOtpRes
import com.kaushalpanjee.common.model.response.InsertRes
import com.kaushalpanjee.common.model.response.JobcardResponse
import com.kaushalpanjee.common.model.response.LanguageList
import com.kaushalpanjee.common.model.response.LoginRes
import com.kaushalpanjee.common.model.response.OtpValidateResponse
import com.kaushalpanjee.common.model.response.SeccDetailsRes
import com.kaushalpanjee.common.model.response.SectionAndPer
import com.kaushalpanjee.common.model.response.SectorResponse
import com.kaushalpanjee.common.model.response.ShgValidateRes
import com.kaushalpanjee.common.model.response.TechQualificationRes
import com.kaushalpanjee.common.model.response.TechnicalEduDomain
import com.kaushalpanjee.common.model.response.TokenRes
import com.kaushalpanjee.common.model.response.TradeResponse
import com.kaushalpanjee.common.model.response.TrainingCenterRes
import com.kaushalpanjee.common.model.response.UpdateEmailRes
import com.kaushalpanjee.common.model.response.UpdatePasswordForRes
import com.kaushalpanjee.common.model.response.WhereHaveYouHeardRes
import com.kaushalpanjee.core.util.AppConstant
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(private val commonRepository: CommonRepository) :ViewModel() {



    private var _getToken = MutableSharedFlow<Resource<out TokenRes>>()
    val getToken = _getToken.asSharedFlow()


    fun getToken(imeiNo:String , appVersion:String){
        viewModelScope.launch {
            commonRepository.getToken(imeiNo,appVersion).collectLatest {
                _getToken.emit(it)
            }
        }


    }



    private val _checkAadhaarFor = MutableSharedFlow<Resource<out AadhaarCheckForRes>>()
    val checkAadhaarFor = _checkAadhaarFor.asSharedFlow()

    fun checkAadhaarFor(aadhaarNumber: String, appVersion: String) {
        viewModelScope.launch {
            commonRepository.checkAadhaarFor(aadhaarNumber, appVersion).collectLatest {
                _checkAadhaarFor.emit(it)
            }
        }
    }



    private var _updatePasswordForget = MutableSharedFlow<Resource<out UpdatePasswordForRes>>()
    val updatePasswordForget = _updatePasswordForget.asSharedFlow()


    fun updatePasswordForget(updatePasswordForReq: UpdatePasswordForReq){
        viewModelScope.launch {
            commonRepository.updatePasswordForget(updatePasswordForReq).collectLatest {
                _updatePasswordForget.emit(it)
            }
        }


    }



    private var _sendMobileOTP = MutableSharedFlow<Resource<out SendMobileOTPResponse>>()
    val sendMobileOTP = _sendMobileOTP.asSharedFlow()


    fun sendMobileOTP(mobileNumber:String , appVersion:String, imeiNo :String){
        viewModelScope.launch {
            commonRepository.sendMobileOTP(mobileNumber,appVersion,imeiNo).collectLatest {
                _sendMobileOTP.emit(it)
            }
        }


    }

    private var _sendEmailOTP = MutableSharedFlow<Resource<out SendMobileOTPResponse>>()
    val sendEmailOTP = _sendEmailOTP.asSharedFlow()

    fun sendEmailOTP(email:String,appVersion: String, imeiNo :String){
        viewModelScope.launch {
            commonRepository.sendEmailOTP(email,appVersion,imeiNo).collectLatest {
                _sendEmailOTP.emit(it)
            }
        }}

         private var _techEducation = MutableSharedFlow<Resource<out TechQualificationRes>>()
         val techEducation = _techEducation.asSharedFlow()

         fun getTechEducation(appVersion: String,header :String,loginId :String){
            viewModelScope.launch {
            commonRepository.getTechEducationAPI(BuildConfig.VERSION_NAME,loginId,header).collectLatest {
                _techEducation.emit(it)
            }
        }

    }

    private var _getUpdateEmailAPI = MutableSharedFlow<Resource<out UpdateEmailRes>>()
    val getUpdateEmailAPI = _getUpdateEmailAPI.asSharedFlow()

    fun getUpdateEmailAPI(header :String,appVersion: String,loginId :String,imeiNo: String,email: String){
        viewModelScope.launch {
            commonRepository.getUpdateEmailAPI(header,appVersion,loginId, imeiNo,email).collectLatest {
                _getUpdateEmailAPI.emit(it)
            }
        }

    }



    private var _techEducationDomain = MutableSharedFlow<Resource<out TechnicalEduDomain>>()
    val techEducationDomain = _techEducationDomain.asSharedFlow()

    fun getTechEducationDomainAPI(appVersion: String,qual :String,header :String,loginId :String){
        viewModelScope.launch {
            commonRepository.getTechEducationDomainAPI(BuildConfig.VERSION_NAME,qual,header,loginId).collectLatest {
                _techEducationDomain.emit(it)
            }
        }

    }

    private var _stateList =  MutableStateFlow<Resource<out StateDataResponse>>(Resource.Loading())
    val getStateList = _stateList.asStateFlow()


    fun getStateListApi(){
        viewModelScope.launch {
            commonRepository.getStateListApi(BuildConfig.VERSION_NAME,"").collectLatest {
                _stateList.emit(it)
            }
        }
    }
//Bearer LXaLQDGlmOJlb14mZbYxHw==
    private var _WhereHaveYouHeard =  MutableStateFlow<Resource<out WhereHaveYouHeardRes>>(Resource.Loading())
    val getWhereHaveYouHeard = _WhereHaveYouHeard.asStateFlow()



    fun getWhereHaveYouHeardAPI(header :String,loginId :String){
        viewModelScope.launch {
            commonRepository.getWhereHaveYouHeardAPI(BuildConfig.VERSION_NAME,header,loginId).collectLatest {
                _WhereHaveYouHeard.emit(it)
            }
        }
    }

    private var _districtList =  MutableStateFlow<Resource<out DistrictResponse>>(Resource.Loading())
    val getDistrictList = _districtList.asStateFlow()


    fun getDistrictListApi(state :String,header :String,loginId :String){
        viewModelScope.launch {
            commonRepository.getDistrictListApi(state,BuildConfig.VERSION_NAME,header,loginId).collectLatest {
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


    fun getBlockListApi(district :String,header :String,loginId :String){
        viewModelScope.launch {
            commonRepository.getBlockListApi(district,BuildConfig.VERSION_NAME,header,loginId).collectLatest {
                _blockList.emit(it)
            }
        }}


    private var _blockListPer =  MutableStateFlow<Resource<out BlockResponse>>(Resource.Loading())
    val getBlockListPer = _blockListPer.asStateFlow()


    fun getBlockPerListApi(district :String,header :String,loginId :String){
        viewModelScope.launch {
            commonRepository.getBlockPerListApi(district,BuildConfig.VERSION_NAME,header,loginId).collectLatest {
                _blockListPer.emit(it)
            }
        }}


    private  var _gpList =  MutableStateFlow<Resource<out grampanchayatResponse>>(Resource.Loading())
        val getGpList = _gpList.asStateFlow()


        fun getGpListApi(block :String,header :String,loginId :String) {
            viewModelScope.launch {
                commonRepository.getGPListApi(block, BuildConfig.VERSION_NAME,header,loginId).collectLatest {
                    _gpList.emit(it)
                }
            }


        }


    private  var _villageList =  MutableStateFlow<Resource<out VillageResponse>>(Resource.Loading())
    val getVillageList = _villageList.asStateFlow()


    fun getVillageListApi(gp :String,header :String,loginId :String) {
        viewModelScope.launch {
            commonRepository.getVillageListApi(gp, BuildConfig.VERSION_NAME,header,loginId).collectLatest {
                _villageList.emit(it)
            }
        }


    }





    private  var _aadhaarList =  MutableStateFlow<Resource<out AadhaarDetailRes>>(Resource.Loading())
    val getAadhaarList = _aadhaarList.asStateFlow()


    fun getAadhaarListAPI(adharDetailsReq: AdharDetailsReq,header :String) {
        viewModelScope.launch {
            commonRepository.getAadhaarListAPI(adharDetailsReq,header).collectLatest {
                _aadhaarList.emit(it)
            }
        }


    }


    private  var _seccListAPI =  MutableStateFlow<Resource<out SeccDetailsRes>>(Resource.Loading())
    val getSeccListAPI = _seccListAPI.asStateFlow()


    fun getSeccListAPI(seccReq: SeccReq,header :String) {
        viewModelScope.launch {
            commonRepository.getSeccListAPI(seccReq,header).collectLatest {
                _seccListAPI.emit(it)
            }
        }


    }


    private  var _getSecctionAndPerAPI =  MutableStateFlow<Resource<out SectionAndPer>>(Resource.Loading())
    val getSecctionAndPerAPI = _getSecctionAndPerAPI.asStateFlow()


    fun getSecctionAndPerAPI(sectionAndPerReq: SectionAndPerReq,header :String) {
        viewModelScope.launch {
            commonRepository.getSecctionAndPerAPI(sectionAndPerReq,header).collectLatest {
                _getSecctionAndPerAPI.emit(it)
            }
        }


    }



    private  var _insertPersonalDataAPI =  MutableStateFlow<Resource<out InsertRes>>(Resource.Loading())
    val insertPersonalDataAPI = _insertPersonalDataAPI.asStateFlow()


    fun insertPersonalDataAPI(personalInsertReq: PersonalInsertReq,header :String) {
        viewModelScope.launch {
            commonRepository.insertPersonalDataAPI(personalInsertReq,header).collectLatest {
                _insertPersonalDataAPI.emit(it)
            }
        }


    }


    private  var _insertAddressAPI =  MutableStateFlow<Resource<out InsertRes>>(Resource.Loading())
    val insertAddressAPI = _insertAddressAPI.asStateFlow()


    fun insertAddressAPI(addressInsertReq: AddressInsertReq,header :String) {
        viewModelScope.launch {
            commonRepository.insertAddressAPI(addressInsertReq,header).collectLatest {
                _insertAddressAPI.emit(it)
            }
        }


    }

    private  var _insertSeccAPI =  MutableStateFlow<Resource<out InsertRes>>(Resource.Loading())
    val insertSeccAPI = _insertSeccAPI.asStateFlow()


    fun insertSeccAPI(seccInsertReq: SeccInsertReq,header :String) {
        viewModelScope.launch {
            commonRepository.insertSeccAPI(seccInsertReq,header).collectLatest {
                _insertSeccAPI.emit(it)
            }
        }


    }



    private  var _insertEducationAPI =  MutableStateFlow<Resource<out InsertRes>>(Resource.Loading())
    val insertEducationAPI = _insertEducationAPI.asStateFlow()


    fun insertEducationAPI(educationalInsertReq: EducationalInsertReq,header :String) {
        viewModelScope.launch {
            commonRepository.insertEducationAPI(educationalInsertReq,header).collectLatest {
                _insertEducationAPI.emit(it)
            }
        }


    }

    private  var _insertEmploymentAPI =  MutableStateFlow<Resource<out InsertRes>>(Resource.Loading())
    val insertEmploymentAPI = _insertEmploymentAPI.asStateFlow()


    fun insertEmploymentAPI(employmentInsertReq: EmploymentInsertReq,header :String) {
        viewModelScope.launch {
            commonRepository.insertEmploymentAPI(employmentInsertReq,header).collectLatest {
                _insertEmploymentAPI.emit(it)
            }
        }


    }


    private  var _insertTrainingAPI =  MutableStateFlow<Resource<out InsertRes>>(Resource.Loading())
    val insertTrainingAPI = _insertTrainingAPI.asStateFlow()


    fun insertTrainingAPI(trainingInsertReq: TrainingInsertReq,header :String) {
        viewModelScope.launch {
            commonRepository.insertTrainingAPI(trainingInsertReq,header).collectLatest {
                _insertTrainingAPI.emit(it)
            }
        }


    }




    private  var _insertBankingAPI =  MutableStateFlow<Resource<out InsertRes>>(Resource.Loading())
    val insertBankingAPI = _insertBankingAPI.asStateFlow()


    fun insertBankingAPI(bankingInsertReq: BankingInsertReq,header :String) {
        viewModelScope.launch {
            commonRepository.insertBankingAPI(bankingInsertReq,header).collectLatest {
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


    fun getBankDetailsAPI(bankingReq: BankingReq,header :String ){
        viewModelScope.launch {
            commonRepository.getBankDetailsAPI(bankingReq,header).collectLatest {
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

    private  var _getOtpValidateApi =  MutableStateFlow<Resource<out OtpValidateResponse>>(Resource.Loading())
    val getOtpValidateApi = _getOtpValidateApi.asSharedFlow()


    fun getOtpValidateApi(validateOtpReq: ValidateOtpReq){
        viewModelScope.launch {
            commonRepository.getOtpValidateApi(validateOtpReq).collectLatest {
                _getOtpValidateApi.emit(it)
            }
        }


    }


    private  var _getAadhaarCheck =  MutableStateFlow<Resource<out AadhaarCheckRes>>(Resource.Loading())
    val getAadhaarCheck = _getAadhaarCheck.asSharedFlow()


    fun getAadhaarCheck(aadhaarCheckReq: AadhaarCheckReq){
        viewModelScope.launch {
            commonRepository.getAadhaarCheck(aadhaarCheckReq).collectLatest {
                _getAadhaarCheck.emit(it)
            }
        }
    }

    private  var _updateFaceApi =  MutableStateFlow<Resource<out FaceResponse>>(Resource.Loading())
    val updateFaceApi = _updateFaceApi.asSharedFlow()

    fun updateFaceApi(faceCheckReq: FaceCheckReq){
        viewModelScope.launch {
            commonRepository.updateFaceApi(faceCheckReq).collectLatest {
                _updateFaceApi.emit(it)
            }
        }


    }



    private  var _getSectorListAPI =  MutableStateFlow<Resource<out SectorResponse>>(Resource.Loading())
    val getSectorListAPI = _getSectorListAPI.asSharedFlow()


    fun getSectorListAPI(techQualification: TechQualification,header :String){
        viewModelScope.launch {
            commonRepository.getSectorListAPI(techQualification,header).collectLatest {
                _getSectorListAPI.emit(it)
            }
        }


    }

    private  var _getBannerAPI =  MutableStateFlow<Resource<out BannerResponse>>(Resource.Loading())
    val getBannerAPI = _getBannerAPI.asSharedFlow()


    fun getBannerAPI(token:String,bannerReq: BannerReq){
        viewModelScope.launch {
            commonRepository.getBannerAPI(token,bannerReq).collectLatest {
                _getBannerAPI.emit(it)
            }
        }


    }


    private  var _getTradeListAPI =  MutableStateFlow<Resource<out TradeResponse>>(Resource.Loading())
    val getTradeListAPI = _getTradeListAPI.asSharedFlow()


    fun getTradeListAPI(tradeReq: TradeReq,header :String){
        viewModelScope.launch {
            commonRepository.getTradeListAPI(tradeReq,header).collectLatest {
                _getTradeListAPI.emit(it)
            }
        }


    }

    private  var _getTrainingSearchAPI =  MutableStateFlow<Resource<out TrainingCenterRes>>(Resource.Loading())
    val getTrainingSearchAPI = _getTrainingSearchAPI.asSharedFlow()


    fun getTrainingSearchAPI(trainingSearch: TrainingSearch,header :String){
        viewModelScope.launch {
            commonRepository.getTrainingSearchAPI(trainingSearch,header).collectLatest {
                _getTrainingSearchAPI.emit(it)
            }
        }


    }


    private  var _getTrainingListAPI =  MutableStateFlow<Resource<out TrainingCenterRes>>(Resource.Loading())
    val getTrainingListAPI = _getTrainingListAPI.asSharedFlow()


    fun getTrainingListAPI(trainingCenterReq: TrainingCenterReq,header :String){
        viewModelScope.launch {
            commonRepository.getTrainingListAPI(trainingCenterReq,header).collectLatest {
                _getTrainingListAPI.emit(it)
            }
        }


    }


    private  var _getSelectedTrainingListAPI =  MutableStateFlow<Resource<out TrainingCenterRes>>(Resource.Loading())
    val getSelectedTrainingListAPI = _getSelectedTrainingListAPI.asSharedFlow()



    fun getSelectedTrainingListAPI(getSearchTraining: GetSearchTraining,header :String){
        viewModelScope.launch {
            commonRepository.getSelectedTrainingListAPI(getSearchTraining,header).collectLatest {
                _getSelectedTrainingListAPI.emit(it)

            }
        }


    }



    private  var _getCandidateDetailsAPI =  MutableStateFlow<Resource<out CandidateDetails>>(Resource.Loading())
    val getCandidateDetailsAPI = _getCandidateDetailsAPI.asSharedFlow()


    fun getCandidateDetailsAPI(candidateReq: CandidateReq,header :String){
        viewModelScope.launch {
            commonRepository.getCandidateDetailsAPI(candidateReq,header).collectLatest {
                _getCandidateDetailsAPI.emit(it)
            }
        }


    }


    private  var _getImageChangeAPI =  MutableStateFlow<Resource<out InsertRes>>(Resource.Loading())
    val getImageChangeAPI = _getImageChangeAPI.asSharedFlow()


    fun getImageChangeAPI(imageChangeReq: ImageChangeReq,header :String){
        viewModelScope.launch {
            commonRepository.getImageChangeAPI(imageChangeReq,header).collectLatest {
                _getImageChangeAPI.emit(it)
            }
        }


    }


    private  var _getChangePass =  MutableStateFlow<Resource<out InsertRes>>(Resource.Loading())
    val getChangePass = _getChangePass.asSharedFlow()


    fun getChangePass(changePassReq: ChangePassReq,header :String){
        viewModelScope.launch {
            commonRepository.getChangePass(changePassReq,header).collectLatest {
                _getChangePass.emit(it)
            }
        }

    }


    private  var _aadhaarRekycApi =  MutableStateFlow<Resource<out AadhaarEkycRes>>(Resource.Loading())
    val aadhaarRekycApi = _aadhaarRekycApi.asSharedFlow()


    fun aadhaarRekycApi(aadhaarRekycReq: AadhaarRekycReq,header :String){
        viewModelScope.launch {
            commonRepository.aadhaarRekycApi(aadhaarRekycReq,header).collectLatest {
                _aadhaarRekycApi.emit(it)
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