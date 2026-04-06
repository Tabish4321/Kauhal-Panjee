package com.kaushalpanjee.CBT.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface CBTDao {

    // ✅ INSERT QUESTIONS
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>)

    // ✅ INSERT OPTIONS
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOptions(options: List<OptionEntity>)

    // ✅ GET ALL QUESTIONS (UI observe karega)
    @Query("SELECT * FROM questions")
    fun getQuestions(): LiveData<List<QuestionEntity>>

    // ✅ GET OPTIONS BY QUESTION ID
    @Query("SELECT * FROM options WHERE questionId = :qId")
    suspend fun getOptionsByQuestionId(qId: String): List<OptionEntity>

    // ✅ DELETE ALL (refresh case)
    @Query("DELETE FROM questions")
    suspend fun clearQuestions()

    @Query("DELETE FROM options")
    suspend fun clearOptions()

    // ✅ OPTIONAL (better clear)
    @Transaction
    suspend fun clearAll() {
        clearOptions()
        clearQuestions()
    }
}