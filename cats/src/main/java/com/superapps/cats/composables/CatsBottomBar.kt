package com.superapps.cats.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.superapps.cats.R

@Composable
fun CatsBottomBar(navController: NavController) {
	val items = listOf(
		BottomNavItem(stringResource(R.string.cats), "cats", Icons.AutoMirrored.Filled.List),
		BottomNavItem(stringResource(R.string.favs), "favs", Icons.Default.Favorite)
	)

	NavigationBar(
		containerColor = MaterialTheme.colorScheme.primary,
		contentColor = MaterialTheme.colorScheme.onPrimary
	) {
		val navBackStackEntry by navController.currentBackStackEntryAsState()
		val currentRoute = navBackStackEntry?.destination?.route

		items.forEach { item ->
			NavigationBarItem(
				icon = { Icon(item.icon, contentDescription = item.title) },
				label = { Text(item.title) },
				selected = currentRoute == item.route,
				onClick = {
					if (currentRoute != item.route) {
						navController.navigate(item.route) {
							popUpTo(navController.graph.startDestinationId) { saveState = true }
							launchSingleTop = true
							restoreState = true
						}
					}
				}
			)
		}
	}
}

data class BottomNavItem(val title: String, val route: String, val icon: ImageVector)
