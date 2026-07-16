package com.kaushalpanjee.cbt.api.answers

import com.google.gson.annotations.SerializedName
import com.kaushalpanjee.cbt.submit.SubmitExamItem

data class CbtAnsersSubmit(

    @SerializedName("cand_id")
    var cand_id: String,

    @SerializedName("batch_id")
    var batch_id: String,

    @SerializedName("exam_id")
    var exam_id: String,

    @SerializedName("question_set_id")
    var question_set_id: String,

    @SerializedName("Ques_and_ans")
    var Ques_and_ans: List<SubmitExamItem>
)
