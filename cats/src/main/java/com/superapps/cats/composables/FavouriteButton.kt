package com.superapps.cats.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.superapps.cats.R

@Composable
internal fun FavouriteButton(modifier: Modifier, isFavourite: Boolean, onClick: () -> Unit) {
	Icon(
		modifier =
		modifier.size(24.dp).clickable(
			interactionSource = remember { MutableInteractionSource() },
			indication = ripple(radius = 12.dp),
			onClick = onClick
		).background(MaterialTheme.colorScheme.background, shape = CircleShape)
			.padding(2.dp),
		imageVector =
		ImageVector.vectorResource(
			if (isFavourite) {
				R.drawable.paw_filled
			} else {
				R.drawable.paw_outline
			}
		),
		contentDescription = if (isFavourite) "Favourite" else "Not favourite"
	)
}
