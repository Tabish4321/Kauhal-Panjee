package com.kaushalpanjee.common.model.response

data class TechQualificationRes(val courseList: MutableList<Course>,  val responseCode: Int,val responseDesc: String,val responseMsg: String)

data  class Course (
    var qualName :String ,
    var qualCode: String )
