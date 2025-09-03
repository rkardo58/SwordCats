package com.superapps.swordcats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.superapps.cats.navigation.cats
import com.superapps.cats.navigation.model.CATS_NAVIGATION_GRAPH_ROUTE
import com.superapps.common.ui.theme.SwordTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		Timber.d("onCreate")
		enableEdgeToEdge()
		setContent {
			SwordTheme {
				val navController = rememberNavController()
				NavHost(
					navController = navController,
					startDestination = CATS_NAVIGATION_GRAPH_ROUTE
				) {
					cats(navController)
				}
			}
		}
	}

	override fun onResume() {
		super.onResume()
		Timber.d("onResume")
	}

	override fun onPause() {
		super.onPause()
		Timber.d("onPause")
	}

	override fun onDestroy() {
		super.onDestroy()
		Timber.d("onDestroy")
	}
}
