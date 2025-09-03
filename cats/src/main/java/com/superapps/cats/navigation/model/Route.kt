package com.superapps.cats.navigation.model

internal sealed class Route(val route: String) {
	object Breeds : Route("breeds")
	object Favourites : Route("favourites")
}

const val CATS_NAVIGATION_GRAPH_ROUTE = "cats"
