package com.example.ncov19traking.api

import retrofit2.Response

sealed class ApiResponse<T> {

    companion object {
        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204)
                    ApiSuccessEmptyResponse()
                else ApiSuccessResponse(body)
            } else {
                val errorMessage = if (response.errorBody()?.toString().isNullOrEmpty())
                    response.message()
                else response.errorBody().toString()
                ApiErrorResponse(response.code(), errorMessage ?: "Unknown error")
            }
        }
    }
}

class ApiSuccessEmptyResponse<T> : ApiResponse<T>()
class ApiSuccessResponse<T>(val data: T) : ApiResponse<T>()
class ApiErrorResponse<T>(val code: Int, val message: String) : ApiResponse<T>()