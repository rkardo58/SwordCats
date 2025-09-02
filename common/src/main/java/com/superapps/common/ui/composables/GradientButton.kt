package com.superapps.common.ui.composables

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.superapps.common.ui.theme.Black
import com.superapps.common.ui.theme.Main
import com.superapps.common.ui.theme.MainBlue
import com.superapps.common.ui.theme.SwordTheme

@Composable
fun GradientButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(50)
    val transition = rememberInfiniteTransition(label = "shimmer")

    val offsetX by transition.animateFloat(
        initialValue = -100f,
        targetValue = 200f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(durationMillis = 2000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
        label = "shimmerOffset",
    )

    Button(
        modifier =
            modifier.clip(shape).drawBehind {
                val brush =
                    Brush.linearGradient(
                        colors =
                            listOf(
                                MainBlue.copy(0.6f),
                                Main.copy(0.6f),
                                Color.LightGray.copy(0.1f),
                            ),
                        start = Offset(offsetX, 0f),
                        end = Offset(offsetX + size.width, size.height),
                    )
                drawRect(
                    brush = brush,
                    size = size,
                )
            },
        onClick = onClick,
        colors =
            ButtonDefaults.buttonColors(
                Color.Transparent,
                contentColor = Black,
            ),
        enabled = enabled,
        border =
            BorderStroke(
                width = 1.dp,
                brush =
                    Brush.verticalGradient(
                        colors =
                            listOf(
                                Color.White.copy(alpha = 0.8f),
                                Color.Transparent,
                            ),
                    ),
            ),
        shape = shape,
    ) {
        Text(
            text,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(10.dp),
        )
    }
}

@Preview
@Composable
private fun GlassButtonPreview() {
    SwordTheme {
        GradientButton(modifier = Modifier.fillMaxWidth(), text = "Save") {}
    }
}
