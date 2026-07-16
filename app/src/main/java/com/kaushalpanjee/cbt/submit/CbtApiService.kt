package com.kaushalpanjee.cbt.submit
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.google.gson.JsonObject
interface CbtApiService {

    @POST("demobackend/ddugkyapp/cbtApi/submitExam")
    suspend fun submitExam(
        @Body body: SubmitExamRequest
    ): Response<JsonObject>

}
//interface CbtApiService {
//
//    @POST("demobackend/ddugkyapp/cbtApi/submitExam")
//    suspend fun submitExam(
//        @Body body: List<SubmitExamItem>
//    ): Response<JsonObject>
//}