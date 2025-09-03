package com.superapps.common.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.superapps.common.ui.theme.SwordTheme

@Composable
fun GlassBox(
	modifier: Modifier = Modifier,
	onClick: (() -> Unit)? = null,
	shape: Shape = RoundedCornerShape(10),
	content: @Composable BoxScope.() -> Unit
) {
	Box(
		modifier =
		modifier
			.then(
				if (onClick != null) {
					Modifier.clip(shape).clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = ripple(),
						onClick = onClick
					)
				} else {
					Modifier
				}
			).background(
				color = Color.LightGray.copy(0.1f),
				shape = shape
			).border(
				1.dp,
				brush =
				Brush.linearGradient(
					listOf(Color.White.copy(alpha = 0.6f), Color.Transparent, Color.White.copy(alpha = 0.6f))
				),
				shape = shape
			).padding(10.dp)
	) {
		Box(
			modifier = Modifier
				.align(Alignment.Center)
				.width(40.dp)
				.fillMaxHeight()
				.rotate(45f)
				.background(
					Brush.verticalGradient(
						colors = listOf(
							Color.Transparent,
							Color.White.copy(alpha = 0.25f),
							Color.Transparent
						),
						startY = 0f,
						endY = 1000f
					)
				)
		)
		content()
	}
}

@Preview(backgroundColor = 0xFF000000)
@Composable
private fun GlassBoxPrev() {
	SwordTheme {
		GlassBox(Modifier.size(60.dp)) {
			Box(Modifier.matchParentSize().background(Color.Red))
		}
	}
}
