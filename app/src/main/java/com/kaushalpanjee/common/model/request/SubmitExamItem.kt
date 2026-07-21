package com.kaushalpanjee.common.model.request

import com.google.gson.annotations.SerializedName


data class SubmitExamItem(

    @SerializedName("question_id")
    var question_id: String,

    @SerializedName("answer_given")
    var answer_given: String,

    @SerializedName("category")
      var category: String,

    @SerializedName("marks_per_qs")
      var marks_per_qs: Double
)
