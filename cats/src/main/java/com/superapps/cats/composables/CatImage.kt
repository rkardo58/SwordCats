package com.superapps.cats.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.superapps.cats.R
import com.superapps.common.ui.theme.SwordTheme
import com.superapps.domain.model.Breed

@Composable
internal fun CatImage(breed: Breed) {
	val painter =
		rememberAsyncImagePainter(
			model = breed.imageUrl
		)

	val state = painter.state.collectAsState().value

	when (state) {
		is AsyncImagePainter.State.Success ->
			Image(
				painter = painter,
				contentDescription = breed.name,
				modifier =
				Modifier
					.fillMaxWidth()
					.aspectRatio(1f)
					.clip(RoundedCornerShape(6)),
				contentScale = ContentScale.Crop
			)

		is AsyncImagePainter.State.Loading -> RockingCat()
		else -> Image(
			painter = painterResource(R.drawable.error_cat),
			contentDescription = breed.name,
			modifier = Modifier
				.fillMaxWidth()
				.aspectRatio(1f)
				.clip(RoundedCornerShape(6)),
			contentScale = ContentScale.Crop
		)
	}
}

@Preview
@Composable
private fun CatImagePreview() {
	SwordTheme {
		CatImage(
			Breed(
				"id",
				"name",
				"description",
				"origin",
				listOf("temperament"),
				"10 - 12",
				"image",
				true
			)
		)
	}
}
