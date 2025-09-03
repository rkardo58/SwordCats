package com.superapps.cats.composables

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.superapps.cats.R
import com.superapps.common.ui.theme.SwordTheme

@Composable
internal fun RockingCat() {
	val infiniteTransition = rememberInfiniteTransition(label = "rotate")
	val rotation by infiniteTransition.animateFloat(
		initialValue = -15f,
		targetValue = 15f,
		animationSpec =
		infiniteRepeatable(
			animation = tween(600, easing = FastOutSlowInEasing),
			repeatMode = RepeatMode.Reverse
		),
		label = "rotation"
	)

	Image(
		painter = painterResource(R.drawable.cat_playing),
		contentDescription = null,
		modifier =
		Modifier
			.fillMaxWidth()
			.padding(20.dp)
			.aspectRatio(1f)
			.graphicsLayer { rotationZ = rotation }
	)
}

@Preview
@Composable
private fun RockingCatPreview() {
	SwordTheme {
		RockingCat()
	}
}
