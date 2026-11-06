package com.kaushalpanjee.common.model.response


data class getShgDetailsRes(
    val wrappedList: List<ShgDetails>? = emptyList(),
    val responseCode: Int? = null,
    val responseDesc: String? = null
)

data class ShgDetails(
    val id: Long? = null,
    val shg_name: String? = null,
    val shg_name_local: String? = null,
    val nic_shg_code: String? = null,
    val vo_id: Long? = null,
    val vo_name: String? = null,
    val vo_code: String? = null,
    val clf_id: Long? = null,
    val clf_name: String? = null,
    val clf_code: String? = null,
    val shg_members: List<ShgMember>? = emptyList()
)

data class ShgMember(
    val member_id: Long? = null,
    val member_code: String? = null,
    val member_name: String? = null,
    val gender: String? = null,
    val relation_name: String? = null,
    val dob: String? = null,
    val rtoken: String? = null,
    val aadhar_verified: Boolean? = null,
    val member_designations: List<ShgMemberDesignation>? = emptyList(),
    val member_phones: List<ShgMemberPhone>? = emptyList(),
    val member_addresses: List<ShgMemberAddress>? = emptyList()
)

data class ShgMemberDesignation(
    val designation: String? = null
)

data class ShgMemberPhone(
    val phone_no: String? = null
)

data class ShgMemberAddress(
    val state_code: String? = null,
    val district_code: String? = null,
    val block_code: String? = null,
    val panchayat_code: String? = null,
    val village_code: String? = null
)