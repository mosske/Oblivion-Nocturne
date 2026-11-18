package com.oblivion.nocturne.ui.navigation.graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.oblivion.nocturne.ui.navigation.destination.library.LibraryDynamicPlaylistDestination
import com.oblivion.nocturne.ui.screen.library.LibraryDynamicPlaylistScreen

@ExperimentalMaterial3Api
fun NavGraphBuilder.libraryScreenGraph(
    innerPadding: PaddingValues,
    navController: NavController,
) {
    composable<LibraryDynamicPlaylistDestination> { entry ->
        val data = entry.toRoute<LibraryDynamicPlaylistDestination>()
        LibraryDynamicPlaylistScreen(
            innerPadding = innerPadding,
            navController = navController,
            type = data.type,
        )
    }
}