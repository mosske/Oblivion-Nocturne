package com.oblivion.nocturne.ui.navigation.graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.oblivion.nocturne.ui.navigation.destination.home.AppLogDestination
import com.oblivion.nocturne.ui.navigation.destination.home.CreditDestination
import com.oblivion.nocturne.ui.navigation.destination.home.ListenTogetherDestination
import com.oblivion.nocturne.ui.navigation.destination.home.ListenTogetherSettingsDestination
import com.oblivion.nocturne.ui.navigation.destination.home.MoodDestination
import com.oblivion.nocturne.ui.navigation.destination.home.NotificationDestination
import com.oblivion.nocturne.ui.navigation.destination.home.SettingsDestination
import com.oblivion.nocturne.ui.screen.home.ListenTogetherScreen
import com.oblivion.nocturne.ui.screen.home.ListenTogetherSettingsScreen
import com.oblivion.nocturne.ui.screen.home.MoodScreen
import com.oblivion.nocturne.ui.screen.home.NotificationScreen
import com.oblivion.nocturne.ui.screen.home.SettingScreen
import com.oblivion.nocturne.ui.screen.other.AppLogScreen
import com.oblivion.nocturne.ui.screen.other.CreditScreen

fun NavGraphBuilder.homeScreenGraph(
    innerPadding: PaddingValues,
    navController: NavController,
) {
    composable<CreditDestination> {
        CreditScreen(
            paddingValues = innerPadding,
            navController = navController,
        )
    }
    composable<AppLogDestination> {
        AppLogScreen(
            paddingValues = innerPadding,
            navController = navController,
        )
    }
    composable<MoodDestination> { entry ->
        val params = entry.toRoute<MoodDestination>().params
        MoodScreen(
            navController = navController,
            params = params,
        )
    }
    composable<ListenTogetherDestination> {
        ListenTogetherScreen(
            navController = navController,
            innerPadding = innerPadding,
        )
    }
    composable<ListenTogetherSettingsDestination> {
        ListenTogetherSettingsScreen(
            navController = navController,
            innerPadding = innerPadding,
        )
    }
    composable<NotificationDestination> {
        NotificationScreen(
            navController = navController,
        )
    }
    composable<SettingsDestination> {
        SettingScreen(
            navController = navController,
            innerPadding = innerPadding,
        )
    }
}