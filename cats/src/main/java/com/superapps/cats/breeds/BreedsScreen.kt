package com.superapps.cats.breeds

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.superapps.cats.breeds.composables.BreedItem
import com.superapps.cats.breeds.model.BreedsState
import com.superapps.common.R
import com.superapps.common.ui.composables.GlassTextField
import com.superapps.common.ui.theme.SwordTheme
import com.superapps.domain.model.Breed
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun BreedsScreen(
	modifier: Modifier = Modifier,
	state: BreedsState,
	onSearchQueryChange: (String) -> Unit,
	onToggleFavorite: (String, Boolean) -> Unit,
	onBreedClick: (Breed) -> Unit
) {
	val breeds = state.breeds.collectAsLazyPagingItems()
	Column(modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
		GlassTextField(
			modifier = Modifier.fillMaxWidth().padding(16.dp),
			placeholder = stringResource(R.string.search),
			value = state.searchQuery,
			onValueChange = onSearchQueryChange,
			singleLine = true,
			trailingIcon = if (state.isLoading) {
				{
					CircularProgressIndicator(Modifier.padding(8.dp))
				}
			} else if (state.searchQuery.isNotEmpty()) {
				{
					Icon(
						imageVector = Icons.Default.Clear,
						contentDescription = "Clear",
						Modifier.clickable {
							onSearchQueryChange("")
						}
					)
				}
			} else {
				null
			}
		)
		LazyVerticalGrid(
			columns = GridCells.Fixed(2),
			modifier = Modifier.weight(1f),
			contentPadding = PaddingValues(horizontal = 16.dp),
			verticalArrangement = Arrangement.spacedBy(16.dp),
			horizontalArrangement = Arrangement.spacedBy(4.dp)
		) {
			items(
				breeds.itemCount,
				key = breeds.itemKey { it.id }
			) { index ->
				breeds[index]?.let { breed ->
					BreedItem(breed, onToggleFavorite, onBreedClick)
				}
			}
		}
	}
}

@Preview
@Composable
private fun BreedScreenPreview() {
	SwordTheme {
		BreedsScreen(
			modifier = Modifier,
			state =
			BreedsState(
				breeds = flowOf(
					PagingData.from(
						listOf(
							Breed(
								"id",
								"Breed name",
								"Breed description",
								"origin",
								listOf("Temperament"),
								"2",
								"https://www.nationalgeographic.com/animals/mammals/facts/domestic-cat",
								true
							)
						)
					)
				)
			),
			{ _ -> },
			{ _, _ -> },
			{ _ -> }
		)
	}
}
