package com.kaushalpanjee.CBT.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "options",
    foreignKeys = [
        ForeignKey(
            entity = QuestionEntity::class,
            parentColumns = ["questionId"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["questionId"])]
)
data class OptionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val questionId: String,

    val optionKey: String,

    val optionValue: String
)
