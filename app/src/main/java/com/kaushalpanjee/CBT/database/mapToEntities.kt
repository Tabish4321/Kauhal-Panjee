package com.kaushalpanjee.CBT.database

import com.example.myapplication.CBT.api.QuestionSet

fun mapToEntities(qs: QuestionSet): Pair<List<QuestionEntity>, List<OptionEntity>> {

    val questions = mutableListOf<QuestionEntity>()
    val options = mutableListOf<OptionEntity>()

    qs.question.forEach { q ->

        questions.add(
            QuestionEntity(
                questionId = q.question_id,
                questionValue = q.question_value,
                marks = q.marks_per_qs,
                examId = qs.exam_id
            )
        )

        q.option.forEach { op ->
            options.add(
                OptionEntity(
                    questionId = q.question_id,
                    optionKey = op.option_key,
                    optionValue = op.option_value
                )
            )
        }
    }

    return Pair(questions, options)
}