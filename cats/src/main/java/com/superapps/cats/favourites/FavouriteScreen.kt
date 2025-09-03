package com.superapps.cats.favourites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.superapps.cats.R
import com.superapps.cats.breeds.composables.BreedItem
import com.superapps.cats.favourites.model.FavouriteState
import com.superapps.common.ui.theme.SwordTheme
import com.superapps.domain.model.Breed

@Composable
fun FavouritesScreen(
	modifier: Modifier = Modifier,
	state: FavouriteState,
	onFavouriteClick: (String, Boolean) -> Unit,
	onBreedClick: (Breed) -> Unit
) {
	LazyVerticalGrid(
		columns = GridCells.Fixed(2),
		modifier = modifier.fillMaxSize(),
		contentPadding = PaddingValues(horizontal = 16.dp),
		verticalArrangement = Arrangement.spacedBy(16.dp)
	) {
		if (state.averageLifeSpan > 0) {
			item(span = { GridItemSpan(this.maxLineSpan) }) {
				Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
					Text(stringResource(R.string.average_life_span), fontWeight = FontWeight.Bold)
					Text((stringResource(R.string.life_span_value, state.averageLifeSpan)))
				}
			}
		}
		items(state.breeds) {
			BreedItem(breed = it, onToggleFavorite = onFavouriteClick, onBreedClick = onBreedClick)
		}
	}
}

@Preview
@Composable
private fun FavouritesScreenPreview() {
	SwordTheme {
		FavouritesScreen(
			Modifier.fillMaxSize(),
			FavouriteState(breeds = listOf(Breed("id", "name", "description", "origin", listOf("temperament"), "10 - 12", "image", true))),
			{ _, _, -> }
		) {

		}
	}
}
