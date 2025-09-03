package com.superapps.common.ui.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.superapps.common.ui.theme.Black
import com.superapps.common.ui.theme.SwordTheme

@Composable
fun GlassTextField(
	modifier: Modifier = Modifier,
	value: String,
	enabled: Boolean = true,
	readOnly: Boolean = false,
	textStyle: TextStyle = LocalTextStyle.current,
	placeholder: String? = null,
	leadingIcon: @Composable (() -> Unit)? = null,
	trailingIcon: @Composable (() -> Unit)? = null,
	prefix: @Composable (() -> Unit)? = null,
	suffix: @Composable (() -> Unit)? = null,
	supportingText: @Composable (() -> Unit)? = null,
	isError: Boolean = false,
	visualTransformation: VisualTransformation = VisualTransformation.None,
	keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
	keyboardActions: KeyboardActions = KeyboardActions.Default,
	singleLine: Boolean = false,
	maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
	minLines: Int = 1,
	interactionSource: MutableInteractionSource? = null,
	onValueChange: (String) -> Unit
) {
	TextField(
		value,
		onValueChange,
		modifier.glassBorder(isError, enabled),
		enabled,
		readOnly,
		textStyle,
		null,
		placeholder?.let {
			@Composable { Text(it) }
		},
		leadingIcon,
		trailingIcon,
		prefix,
		suffix,
		supportingText,
		isError,
		visualTransformation,
		keyboardOptions,
		keyboardActions,
		singleLine,
		maxLines,
		minLines,
		interactionSource,
		RoundedCornerShape(50),
		TextFieldDefaults.colors(
			focusedContainerColor = Color.LightGray.copy(0.1f),
			unfocusedContainerColor = Color.LightGray.copy(0.1f),
			disabledContainerColor = Color.LightGray.copy(0.2f),
			errorContainerColor = Color.LightGray.copy(0.1f, Color.LightGray.red * 1.5f),
			unfocusedLeadingIconColor = Black,
			focusedLeadingIconColor = Black,
			errorIndicatorColor = Color.Transparent,
			disabledIndicatorColor = Color.Transparent,
			focusedIndicatorColor = Color.Transparent,
			unfocusedIndicatorColor = Color.Transparent,
			focusedTextColor = Black,
			unfocusedTextColor = Black,
			disabledTextColor = Black.copy(0.5f),
			errorTextColor = Black.copy(red = Black.red * 1.5f)
		)
	)
}

@Composable
private fun Modifier.glassBorder(isError: Boolean, enabled: Boolean): Modifier {
	val baseColor =
		when {
			isError && enabled -> Color.Red.copy(0.5f)
			!enabled -> Color.LightGray.copy(0.6f)
			else -> Color.White.copy(0.8f)
		}

	val colorList =
		listOf(
			baseColor,
			Color.Transparent,
			baseColor
		)

	return this.border(
		1.dp,
		brush = Brush.linearGradient(colorList),
		shape = RoundedCornerShape(50)
	)
}

@Preview(showBackground = true)
@Composable
private fun GlassTextFieldPrev() {
	SwordTheme {
		var text by remember { mutableStateOf("Food") }
		GlassTextField(value = text, leadingIcon = {
			Icon(Icons.Default.Search, contentDescription = "search")
		}) {
		}
	}
}
