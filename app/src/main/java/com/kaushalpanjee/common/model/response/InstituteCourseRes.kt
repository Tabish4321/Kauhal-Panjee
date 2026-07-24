package com.kaushalpanjee.common.model.response



data class InstituteCourseRes(
    val wrappedList: List<InstituteCourse>,
    val responseCode: Int,
    val responseDesc: String
)

data class InstituteCourse(
    val instCourseName: String,
    val instCourseId: Int
)
