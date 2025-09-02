package com.superapps.cats.breeds

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.superapps.core.ui.composables.GlassTextField

@Composable
fun BreedsScreen(modifier: Modifier = Modifier) {
    Column(Modifier.fillMaxSize()) {
        GlassTextField()
        LazyColumn(
            modifier = modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(breeds) { breed ->
                BreedItem(breed)
            }
        }
    }
}