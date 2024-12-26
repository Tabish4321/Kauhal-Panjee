package com.kaushalpanjee.common.model.response

data class ShgValidateRes(var Valid :String, var shg_name :String)

/*
==========JAVA===============
nrlmwebservice/src/com/nic/DDUGKY/SHGVerifyData.java
============URL==============
https://nrlm.gov.in/nrlmwebservice/services/ddugky/dataVerify

========Request================
{"SHG_ID":"41358","lgd_state_code":"9"}
========Response===============
{"Valid":"Y","shg_name":"LAXMI MAHILA SHG"}*/

