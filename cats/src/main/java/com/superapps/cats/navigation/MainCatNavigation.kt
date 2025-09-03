package com.superapps.cats.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.superapps.cats.breeds.BreedsScreen
import com.superapps.cats.breeds.BreedsViewModel
import com.superapps.cats.composables.CatsBottomBar
import com.superapps.cats.details.BreedDetail
import com.superapps.cats.details.BreedDetailViewModel
import com.superapps.cats.favourites.FavouriteViewModel
import com.superapps.cats.favourites.FavouritesScreen
import com.superapps.common.ui.composables.SwordScaffold
import com.superapps.domain.model.Breed

fun NavGraphBuilder.cats(navController: NavController) {
	navigation("cats", "main") {
		composable("cats") {
			SwordScaffold(bottomBar = {
				CatsBottomBar(navController = navController)
			}) { paddingValues ->
				val viewmodel = hiltViewModel<BreedsViewModel>()
				val state = viewmodel.state.collectAsStateWithLifecycle().value

				BreedsScreen(
					modifier = Modifier.padding(paddingValues),
					state = state,
					onSearchQueryChange = viewmodel::onSearchQueryChange,
					onToggleFavorite = viewmodel::onToggleFavorite,
					onBreedClick = {
						navController.navigate(it)
					}
				)
			}
		}
		composable("favs") {
			SwordScaffold(bottomBar = {
				CatsBottomBar(navController = navController)
			}) { paddingValues ->
				val viewmodel = hiltViewModel<FavouriteViewModel>()
				val state = viewmodel.state.collectAsStateWithLifecycle()
				BackHandler {
					navController.navigate("cats")
				}
				LifecycleResumeEffect(true) {
					viewmodel.getFavourites()

					onPauseOrDispose {
					}
				}
				FavouritesScreen(
					modifier = Modifier.padding(paddingValues),
					state = state.value,
					onFavouriteClick = { id, _ ->
						viewmodel.removeFromFav(id)
					},
					onBreedClick = {
						navController.navigate(it)
					}
				)
			}
		}
		composable<Breed> {
			val breed = it.toRoute<Breed>()
			val viewmodel = hiltViewModel<BreedDetailViewModel>()
			LaunchedEffect(true) {
				viewmodel.setBreed(breed)
			}
			val state = viewmodel.state.collectAsStateWithLifecycle()

			BreedDetail(breed = state.value) {
				viewmodel.onFavouriteClick(it)
			}
		}
	}
}
