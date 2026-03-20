package com.kaushalpanjee.bhashini.models

data class PipelineTask(
    val taskType: String = "translation",
    val config: Config
)
