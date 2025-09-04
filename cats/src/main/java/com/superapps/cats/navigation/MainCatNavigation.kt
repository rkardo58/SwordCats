package com.superapps.cats.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.superapps.cats.navigation.model.CATS_NAVIGATION_GRAPH_ROUTE
import com.superapps.cats.navigation.model.Route
import com.superapps.common.ui.composables.SnackBarEffect
import com.superapps.common.ui.composables.SwordScaffold
import com.superapps.domain.model.Breed

fun NavGraphBuilder.cats(navController: NavController) {
	navigation(Route.Breeds.route, CATS_NAVIGATION_GRAPH_ROUTE) {
		composable(Route.Breeds.route) {
			val snackbarHostState = remember { SnackbarHostState() }
			SwordScaffold(
				bottomBar = {
					CatsBottomBar(navController = navController)
				},
				snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
			) { paddingValues ->
				val viewmodel = hiltViewModel<BreedsViewModel>()
				val state = viewmodel.state.collectAsStateWithLifecycle().value

				SnackBarEffect(state.error, snackbarHostState)

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
		composable(Route.Favourites.route) {
			val snackbarHostState = remember { SnackbarHostState() }
			SwordScaffold(
				bottomBar = {
					CatsBottomBar(navController = navController)
				},
				snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
			) { paddingValues ->
				val viewmodel = hiltViewModel<FavouriteViewModel>()
				val state = viewmodel.state.collectAsStateWithLifecycle().value

				SnackBarEffect(state.error, snackbarHostState)

				FavouritesScreen(
					modifier = Modifier.padding(paddingValues),
					state = state,
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
