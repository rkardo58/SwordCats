package com.superapps.cats.breeds

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.superapps.cats.breeds.composables.BreedItem
import com.superapps.cats.breeds.model.BreedsState
import com.superapps.common.ui.composables.GlassTextField
import com.superapps.common.ui.theme.SwordTheme
import com.superapps.domain.model.Breed

@Composable
internal fun BreedsScreen(
    modifier: Modifier = Modifier,
    state: BreedsState,
) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        GlassTextField(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            placeholder = "Search",
            value = state.searchQuery,
            onValueChange = {},
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(items = state.breeds.list, key = { it.id }) { breed ->
                BreedItem(breed)
            }
        }
    }
}

@Preview
@Composable
private fun BreedScreenPreview() {
    SwordTheme {
        BreedsScreen(
            state =
                BreedsState(
                    breeds =
                        BreedsState.Breeds(
                            listOf(
                                Breed(
                                    "id",
                                    "Breed name",
                                    "Breed description",
                                    "https://www.nationalgeographic.com/animals/mammals/facts/domestic-cat",
                                ),
                            ),
                        ),
                ),
        )
    }
}
