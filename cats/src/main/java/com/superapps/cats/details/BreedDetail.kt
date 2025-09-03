package com.superapps.cats.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.superapps.cats.R
import com.superapps.cats.composables.CatImage
import com.superapps.cats.composables.FavouriteButton
import com.superapps.cats.composables.TextRow
import com.superapps.common.ui.composables.GlassBox
import com.superapps.common.ui.composables.SwordScaffold
import com.superapps.common.ui.theme.SwordTheme
import com.superapps.domain.model.Breed

@Composable
internal fun BreedDetail(modifier: Modifier = Modifier, breed: Breed, onFavouriteClick: (Boolean) -> Unit) {
	SwordScaffold(modifier.fillMaxSize()) { paddingValues ->
		Column(
			Modifier
				.fillMaxSize()
				.padding(paddingValues)
				.padding(16.dp),
			verticalArrangement = Arrangement.spacedBy(10.dp)
		) {
			Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
				Text(breed.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
				FavouriteButton(Modifier, breed.isFavourite) {
					onFavouriteClick(!breed.isFavourite)
				}
			}

			CatImage(breed)

			GlassBox {
				Column(
					Modifier
						.fillMaxWidth()
						.padding(8.dp),
					verticalArrangement = Arrangement.spacedBy(8.dp)
				) {
					TextRow(stringResource(R.string.origin), breed.origin)
					LazyRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
						items(breed.temperament) {
							SuggestionChip(label = { Text(it) }, onClick = {})
						}
					}
					TextRow(stringResource(R.string.life_span), breed.lifespan)
					Text(breed.description)
				}
			}
		}
	}
}

@Preview
@Composable
private fun BreedDetailPreview() {
	SwordTheme {
		BreedDetail(
			breed = Breed("id", "name", "description", "origin", listOf("temperament"), "10 - 12", "image", true)
		) {
		}
	}
}
