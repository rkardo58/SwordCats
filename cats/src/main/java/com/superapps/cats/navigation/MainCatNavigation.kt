package com.superapps.cats.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.superapps.cats.breeds.BreedsScreen
import com.superapps.cats.breeds.BreedsViewModel

fun NavGraphBuilder.cats() {
    navigation("cats", "main") {
        composable("cats") {
            val viewmodel = hiltViewModel<BreedsViewModel>()
            val state = viewmodel.state.collectAsStateWithLifecycle()
            BreedsScreen(state = state.value)
        }
    }
}
