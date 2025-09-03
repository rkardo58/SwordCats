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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.superapps.cats.R
import com.superapps.cats.navigation.model.Route
import com.superapps.common.ui.theme.SwordTheme

@Composable
fun CatsBottomBar(navController: NavController) {
	val items = listOf(
		BottomNavItem(stringResource(R.string.cats), Route.Breeds.route, Icons.AutoMirrored.Filled.List),
		BottomNavItem(stringResource(R.string.favs), Route.Favourites.route, Icons.Default.Favorite)
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
					if (currentRoute == item.route) return@NavigationBarItem
					when (item.route) {
						Route.Breeds.route -> {
							if (currentRoute == Route.Favourites.route) {
								navController.popBackStack()
							} else {
								navController.navigate(Route.Breeds.route) {
									launchSingleTop = true
									restoreState = true
								}
							}
						}

						Route.Favourites.route -> {
							navController.navigate(Route.Favourites.route) {
								popUpTo(Route.Breeds.route) { saveState = true }
								launchSingleTop = true
							}
						}
					}
				}
			)
		}
	}
}

private data class BottomNavItem(val title: String, val route: String, val icon: ImageVector)

@Preview
@Composable
private fun CatsBottomBarPreview() {
	SwordTheme {
		val navController = rememberNavController()
		CatsBottomBar(navController)
	}
}
