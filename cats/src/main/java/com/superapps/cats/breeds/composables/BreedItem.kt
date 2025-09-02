package com.superapps.cats.breeds.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.superapps.cats.composables.FavouriteButton
import com.superapps.common.ui.composables.GlassBox
import com.superapps.common.ui.theme.SwordTheme
import com.superapps.domain.model.Breed

@Composable
internal fun BreedItem(breed: Breed) {
    GlassBox(Modifier.fillMaxWidth()) {
        val painter =
            rememberAsyncImagePainter(
                model = breed.imageUrl,
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
                            .aspectRatio(1f).clip(RoundedCornerShape(6)),
                    contentScale = ContentScale.Crop,
                )
            else -> RockingCat()
        }

        FavouriteButton(
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp),
            false,
        ) {
        }

        Text(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.8f)
                    .padding(bottom = 10.dp)
                    .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(50))
                    .padding(8.dp),
            text = breed.name,
        )
    }
}

@Preview
@Composable
private fun BreedItemPreview() {
    SwordTheme {
        BreedItem(
            breed = Breed("id", "Breed name", "Breed description", "https://www.nationalgeographic.com/animals/mammals/facts/domestic-cat"),
        )
    }
}
