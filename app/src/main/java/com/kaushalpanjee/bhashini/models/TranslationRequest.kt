package com.kaushalpanjee.bhashini.models

data class TranslationRequest(
    val pipelineTasks: List<PipelineTask>,
    val inputData: InputData
)
