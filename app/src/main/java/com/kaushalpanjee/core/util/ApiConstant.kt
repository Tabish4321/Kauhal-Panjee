package com.kaushalpanjee.core.util

object ApiConstant {


    /**
     * BASE_URL_DEV="https://nrlm.gov.in/"
     * BASE_URL_STAGE="https://nrlm.gov.in/"
     * BASE_URL_PROD=""
     *
     */

    const val CONNECT_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 10L
    const val READ_TIMEOUT = 60L

    const val DEVICE_TYPE = 2 // android , 1-> ios
    const val DEVICE_TYPE_NAME = "android"



    private const val API_VERSION = "v1/"

    private const val API_FOLDER_BACKEND = "kaushalpanjee/"
    private const val API_FOLDER_CITIZEN = "panjeeapi/"
     const val  API_SMS_OTP= "${API_FOLDER_BACKEND}${API_FOLDER_CITIZEN}generateMobileOtp"
     const val  API_CREATE_USER= "${API_FOLDER_BACKEND}${API_FOLDER_CITIZEN}createUser"
     const val  API_EMAIL_OTP= "${API_FOLDER_BACKEND}${API_FOLDER_CITIZEN}generateMailOtp"
     const val  API_STATE= "${API_FOLDER_BACKEND}${API_FOLDER_CITIZEN}stateList"
     const val  API_DISTRICT= "${API_FOLDER_BACKEND}${API_FOLDER_CITIZEN}districtList"
     const val  API_BLOCK= "${API_FOLDER_BACKEND}${API_FOLDER_CITIZEN}blockList"
     const val  API_GP= "${API_FOLDER_BACKEND}${API_FOLDER_CITIZEN}gramPanchayatList"
     const val  API_VILLAGE= "${API_FOLDER_BACKEND}${API_FOLDER_CITIZEN}villageList"
     const val  API_TECH_EDUCATION= "${API_FOLDER_BACKEND}${API_FOLDER_CITIZEN}technicalQualification"
     const val  API_TECH_EDUCATION_DOMAIN= "${API_FOLDER_BACKEND}${API_FOLDER_CITIZEN}techDomain"
     const val  API_WHERE_HAVE_U_HEARD= "${API_FOLDER_BACKEND}${API_FOLDER_CITIZEN}surveyList"
     const val  API_AADHAAR_DETAILS= "${API_FOLDER_BACKEND}${API_FOLDER_CITIZEN}userDetails"
     const val  API_SECC_DETAILS= "${API_FOLDER_BACKEND}${API_FOLDER_CITIZEN}seccDetails"
     const val  API_SECTION_PERCENTAGE= "${API_FOLDER_BACKEND}${API_FOLDER_CITIZEN}sectionStatus"





}