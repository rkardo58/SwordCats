package com.superapps.common.ui.components

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UiText {
    data class DynamicString(
        val value: String,
    ) : UiText()

    class StringResource(
        @param:StringRes val id: Int,
        vararg val args: Any,
    ) : UiText()

    @Composable
    fun asString(): String =
        when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(id, *args)
        }

    fun asString(context: Context): String =
        when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(id, *args)
        }
}
