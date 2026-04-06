package com.kaushalpanjee.CBT.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey
    val questionId: String,

    val questionValue: String,

    val marks: Double,

    val examId: String
)
