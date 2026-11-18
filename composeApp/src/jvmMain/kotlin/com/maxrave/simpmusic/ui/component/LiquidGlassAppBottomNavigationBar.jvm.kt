package com.oblivion.nocturne.ui.component

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.oblivion.nocturne.expect.ui.PlatformBackdrop
import com.oblivion.nocturne.viewModel.SharedViewModel
import kotlin.reflect.KClass

@Composable
actual fun LiquidGlassAppBottomNavigationBar(
    startDestination: Any,
    navController: NavController,
    backdrop: PlatformBackdrop,
    viewModel: SharedViewModel,
    isScrolledToTop: Boolean,
    showAnalyticsTab: Boolean,
    showMixForYouTab: Boolean,
    onOpenNowPlaying: () -> Unit,
    liquidGlass: Boolean,
    reloadDestinationIfNeeded: (KClass<*>) -> Unit
) {
}