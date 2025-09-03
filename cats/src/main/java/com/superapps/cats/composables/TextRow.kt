package com.superapps.cats.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.superapps.common.ui.theme.SwordTheme

@Composable
internal fun TextRow(label: String, value: String) {
	Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(2.dp)) {
		Text(label, fontWeight = FontWeight.Bold)
		Text(value)
	}
}

@Preview
@Composable
private fun TextRowPrev() {
	SwordTheme {
		TextRow("Origin", "Spain")
	}
}
