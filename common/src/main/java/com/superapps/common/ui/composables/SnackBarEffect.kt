package com.superapps.common.ui.composables

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.superapps.common.ui.components.UiText
import kotlinx.coroutines.launch

@Composable
fun SnackBarEffect(error: UiText?, snackbarHostState: SnackbarHostState) {
	val scope = rememberCoroutineScope()
	val context = LocalContext.current

	LaunchedEffect(error) {
		if (error != null) {
			snackbarHostState.currentSnackbarData?.dismiss()
			scope.launch {
				snackbarHostState.showSnackbar(error.asString(context))
			}
		}
	}
}
