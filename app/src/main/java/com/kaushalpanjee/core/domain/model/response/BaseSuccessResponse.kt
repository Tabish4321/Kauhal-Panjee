package com.utilize.core.domain.model.response

abstract class BaseSuccessResponse{
    abstract val message: String
    abstract val success: Boolean
}