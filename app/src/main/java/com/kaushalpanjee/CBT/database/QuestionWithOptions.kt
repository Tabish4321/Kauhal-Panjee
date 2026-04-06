package com.kaushalpanjee.CBT.database

import androidx.room.Embedded
import androidx.room.Relation

data class QuestionWithOptions(
    @Embedded val question: QuestionEntity,
    @Relation(
        parentColumn = "questionId",
        entityColumn = "questionId"
    )
    val options: List<OptionEntity>
)
