package com.superapps.core.network

import com.superapps.core.ui.components.UiText

sealed class Resource<T> {
    class Success<T>(val data: T) : Resource<T>()
    class Failed<T>(val message: UiText) : Resource<T>()
}