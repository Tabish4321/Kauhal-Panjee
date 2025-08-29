package com.kaushalpanjee.common.model.response


data class CandidateDetails(
    val personalList: List<Personal> = emptyList(),
    val addressList: List<Address> = emptyList(),
    val seccList: List<Secc> = emptyList(),
    val educationalList: List<Educational> = emptyList(),
    val employementList: List<Employment> = emptyList(),
    val trainingList: List<Training> = emptyList(),
    val bankList: List<Bank> = emptyList(),
    val responseCode: Int,
    val responseDesc: String
)

data class Personal(
    val personaldetails: List<PersonalDetail> = emptyList(),
    val dlImagePath: String,
    val rsbyCardPath: String,
    val categoryCertPath: String,
    val minorityCertPath: String,
    val rationCardPath: String,
    val naregaCardPath: String,
    val pmaygAttachment: String,
    val pipCert: String,
    val VoterImagePath: String,
    val disablityCertPath: String,
    val shgImage: String
)


data class PersonalDetail(
    val isNarega: String,
    val antyodaya: String,
    val isRSBY: String,
    val dlNo: String,
    val motherName: String,
    val naregaJobCard: String,
    val castCategory: String,
    val isMinority: String,
    val isPIP: String,
    val guardianName: String,
    val isSHG: String,
    val isPmayg: String,
    val annualFamilyIncome: String,
    val shgNo: String,
    val voterId: String,
    val guardianMobilNo: String,
    val isDisablity: String,
    val maritalStatus: String
)

data class Address(
    val residenceCertPath: String,
    val addressDetails: List<AddressDetail> = emptyList()
)

data class AddressDetail(
    val permanentVillageCode: String,
    val permanentDistrictCode: String,
    val presentBlockName: String,
    val permanentBlockName: String,
    val presentPinCode: String,
    val permanentGPCode: String,
    val permanentStateCode: String,
    val presentBlcokCode: String,
    val presentDistrictCode: String,
    val presentGPCode: String,
    val presentVillageCode: String,
    val presentGPName: String,
    val presentStateName: String,
    val permanentStateName: String,
    val presentDistrictName: String,
    val permanentVillageName: String,
    val isPresentAddressSame: String,
    val permanentPinCode: String,
    val permanentDistrictName: String,
    val presentStateCode: String,
    val presentStreet1: String,
    val permanentStreet1: String,
    val presentVillageName: String,
    val permanentGPName: String,
    val permanentStreet2: String,
    val presentStreet2: String,
    val permanentBlcokCode: String,

    val permanentUlbCode: String,
    val presentUlbCode: String,

    val permanentWardCode: String,
    val presentWardCode: String,


    val permanentLocality: String,
    val presentLocality: String,
)

data class Secc(
    val seccDistrictCode: String,
    val seccVillageName: String,
    val seccGPCode: String,
    val seccBlcokCode: String,
    val seccLgdVillCode: String,
    val seccAHLTIN: String,
    val seccVillageCode: String,
    val seccGPName: String,
    val seccDistrictName: String,
    val seccStateCode: String,
    val seccStateName: String,
    val seccBlockName: String,
    val seccCandidateName: String
)

data class Educational(
    val passingTechYear: String,
    val techDomain: String,
    val techDomainId: String,
    val isTechEducate: String,
    val highesteducation: String,
    val language: String,
    val monthYearOfPassing: String,
    val techQualification: String,
    val techQualificationId: String
)

data class Employment(
    val preferJobLocation: String,
    val empPreference: String,
    val monthlyEarning: String,
    val isEmployeed: String,
    val expectedSalary: String,
    val empNature: String,
    val intrestedIn: String
)

data class Training(
    val isPreTraining: String,
    val sectorId: String,
    val trade: String,
    val preCompTraining: String,
    val compTrainingDuration: String,
    val sectorName: String,
    val hearedAboutScheme: String,
    val hearedFrom: String
)

data class Bank(
    val bankCode: String,
    val bankBranchCode: String,
    val bankAccNumber: String,
    val panNo: String,
    val bankBranchName: String,
    val bankName: String,
    val ifscCode: String
)
