package com.superapps.cats.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.superapps.cats.composables.FavouriteButton

@Composable
internal fun BreedDetail(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize()) {
        Row {
            Text("")
            FavouriteButton(Modifier, true) {
            }
        }
    }
}
