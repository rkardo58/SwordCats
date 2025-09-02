package com.superapps.common.ui.components

import com.superapps.common.Resource

sealed class State<T> {
    class Loading<T> : State<T>()

    data class Success<T>(
        val data: T,
    ) : State<T>()

    data class Error<T>(
        val message: UiText,
    ) : State<T>()

    fun isLoading(): Boolean = this is Loading

    fun isSuccessful(): Boolean = this is Success

    fun isFailed(): Boolean = this is Error

    fun <T> getData(oldData: T?) =
        when (this) {
            is Success -> data
            else -> oldData
        }

    fun <T> getError() =
        when (this) {
            is Error -> message
            else -> null
        }

    companion object {
        fun <T> loading() = Loading<T>()

        fun <T> success(data: T) = Success(data)

        fun <T> error(message: UiText) = Error<T>(message)

        fun <T> fromResource(resource: Resource<T>): State<T> =
            when (resource) {
                is Resource.Success -> success(resource.data)
                is Resource.Failed -> error(resource.message)
            }
    }
}
