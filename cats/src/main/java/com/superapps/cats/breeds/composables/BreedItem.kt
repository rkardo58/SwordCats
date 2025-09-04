package com.superapps.cats.breeds.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.superapps.cats.composables.CatImage
import com.superapps.cats.composables.FavouriteButton
import com.superapps.common.ui.composables.GlassBox
import com.superapps.common.ui.theme.SwordTheme
import com.superapps.domain.model.Breed

@Composable
internal fun BreedItem(breed: Breed, onToggleFavorite: (String, Boolean) -> Unit, onBreedClick: (Breed) -> Unit) {
	GlassBox(
		modifier = Modifier.fillMaxWidth(),
		onClick = {
			onBreedClick(breed)
		}
	) {
		CatImage(breed)

		FavouriteButton(
			modifier =
			Modifier
				.align(Alignment.TopEnd)
				.padding(10.dp),
			breed.isFavourite
		) {
			onToggleFavorite(breed.id, !breed.isFavourite)
		}

		Text(
			modifier =
			Modifier
				.align(Alignment.BottomCenter)
				.fillMaxWidth(0.8f)
				.padding(bottom = 10.dp)
				.background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(50))
				.padding(6.dp),
			text = breed.name,
			textAlign = TextAlign.Center,
			maxLines = 2,
			overflow = TextOverflow.Ellipsis
		)
	}
}

@Preview
@Composable
private fun BreedItemPreview() {
	SwordTheme {
		BreedItem(
			breed = Breed(
				"id",
				"Breed name",
				"Breed description",
				"https://www.nationalgeographic.com/animals/mammals/facts/domestic-cat",
				listOf("Temperament"),
				"",
				"",
				true
			),
			{ _, _ -> }
		) {
		}
	}
}
