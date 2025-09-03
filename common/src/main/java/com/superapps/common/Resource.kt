package com.superapps.common

import com.superapps.common.ui.components.UiText

sealed class Resource<T> {
	class Success<T>(val data: T) : Resource<T>()

	class Failed<T>(val message: UiText) : Resource<T>()
}
