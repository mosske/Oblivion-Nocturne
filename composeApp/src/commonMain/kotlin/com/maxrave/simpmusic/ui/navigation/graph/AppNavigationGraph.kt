package com.oblivion.nocturne.ui.navigation.graph

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.oblivion.nocturne.ui.navigation.destination.home.AnalyticsDestination
import com.oblivion.nocturne.ui.navigation.destination.home.HomeDestination
import com.oblivion.nocturne.ui.navigation.destination.home.WrappedDestination
import com.oblivion.nocturne.ui.theme.ForceDarkContent
import com.oblivion.nocturne.ui.navigation.destination.library.LibraryDestination
import com.oblivion.nocturne.ui.navigation.destination.library.MixForYouDestination
import com.oblivion.nocturne.ui.navigation.destination.player.FullscreenDestination
import com.oblivion.nocturne.ui.navigation.destination.login.DiscordLoginDestination
import com.oblivion.nocturne.ui.navigation.destination.login.LastfmLoginDestination
import com.oblivion.nocturne.ui.navigation.destination.login.LoginDestination
import com.oblivion.nocturne.ui.navigation.destination.login.SpotifyLoginDestination
import com.oblivion.nocturne.ui.navigation.destination.search.SearchDestination
import com.oblivion.nocturne.ui.screen.home.HomeScreen
import com.oblivion.nocturne.ui.screen.home.analytics.AnalyticsScreen
import com.oblivion.nocturne.ui.screen.home.wrapped.WrappedScreen
import com.oblivion.nocturne.ui.screen.library.LibraryScreen
import com.oblivion.nocturne.ui.screen.library.MixForYouScreen
import com.oblivion.nocturne.ui.screen.login.DiscordLoginScreen
import com.oblivion.nocturne.ui.screen.login.DiscordOAuthLoginScreen
import com.oblivion.nocturne.ui.screen.login.LastfmLoginScreen
import com.oblivion.nocturne.ui.screen.login.LoginScreen
import com.oblivion.nocturne.ui.screen.login.SpotifyLoginScreen
import com.oblivion.nocturne.ui.screen.other.SearchScreen
import com.oblivion.nocturne.ui.screen.player.FullscreenPlayer
import org.koin.compose.koinInject
import com.oblivion.nocturne.viewModel.SharedViewModel
import org.oblivion.nocturne.discord.isDiscordOAuthAvailable

@Composable
@ExperimentalMaterial3Api
@ExperimentalFoundationApi
fun AppNavigationGraph(
    innerPadding: PaddingValues,
    navController: NavHostController,
    startDestination: Any = HomeDestination,
    hideNavBar: () -> Unit = { },
    showNavBar: (shouldShowNowPlayingSheet: Boolean) -> Unit = { },
    showNowPlayingSheet: () -> Unit = {},
    onScrolling: (onTop: Boolean) -> Unit = {},
    sharedViewModel: SharedViewModel = koinInject(),
) {
    NavHost(
        navController,
        startDestination = startDestination,
        enterTransition = {
            fadeIn() + slideInHorizontally { -it }
        },
        exitTransition = {
            fadeOut() + slideOutHorizontally { it }
        },
        popEnterTransition = {
            fadeIn() + slideInHorizontally { -it }
        },
        popExitTransition = {
            fadeOut() + slideOutHorizontally { it }
        },
    ) {
        // Bottom bar destinations
        composable<HomeDestination> {
            HomeScreen(
                onScrolling = onScrolling,
                navController = navController,
            )
        }
        composable<SearchDestination> {
            SearchScreen(
                navController = navController,
            )
        }
        composable<LibraryDestination> {
            LibraryScreen(
                innerPadding = innerPadding,
                navController = navController,
                onScrolling = onScrolling,
            )
        }
        // Only reachable as a tab while signed in to YouTube
        composable<MixForYouDestination> {
            MixForYouScreen(
                innerPadding = innerPadding,
                navController = navController,
                onScrolling = onScrolling,
            )
        }
        // Only reachable as a tab while local tracking is enabled.
        // ForceDarkContent for the same reason as album/playlist/artist: the page background comes
        // from the artwork via toImmersiveBackground(), which always lands dark, so the light
        // theme's dark-on-light text and icons would be unreadable on it.
        composable<AnalyticsDestination> {
            ForceDarkContent {
                AnalyticsScreen(
                    navController = navController,
                    innerPadding = innerPadding,
                )
            }
        }
        // Reached only from the Analytics screen's entry banner, so it inherits that screen's
        // gate on local tracking. ForceDarkContent for a different reason than Analytics: the reel
        // is drawn on its own near-black ground whatever the user's theme is, because it is an
        // event and because every card is also a share image that has to survive leaving the app.
        composable<WrappedDestination> {
            ForceDarkContent {
                WrappedScreen(
                    navController = navController,
                    hideNavBar = hideNavBar,
                    // Deliberately not the fullscreen player's `showNavBar(true)` + open sheet:
                    // leaving the reel goes back to Analytics, and raising the Now Playing sheet
                    // over it would be a screen the user never asked for.
                    showNavBar = { showNavBar(false) },
                )
            }
        }
        composable<FullscreenDestination> {
            ForceDarkContent {
                FullscreenPlayer(
                    navController,
                    hideNavBar = hideNavBar,
                    showNavBar = {
                        showNavBar.invoke(true)
                        showNowPlayingSheet.invoke()
                    },
                )
            }
        }
        // Home screen graph
        homeScreenGraph(
            innerPadding = innerPadding,
            navController = navController,
        )
        // Library screen graph
        libraryScreenGraph(
            innerPadding = innerPadding,
            navController = navController,
        )
        // List screen graph
        listScreenGraph(
            innerPadding = innerPadding,
            navController = navController,
        )
        // Login screen graph. Inlined directly here (rather than a separate builder) because the
        // destinations must be conditionally registered from runtime state: Spotify is hidden in
        // "YouTube-only" mode so it has no login surface at all, and Discord uses OAuth or the legacy
        // WebView login depending on availability. These conditional composables require @Composable
        // context, which this NavHost content lambda provides.
        composable<LoginDestination> {
            LoginScreen(
                innerPadding = innerPadding,
                navController = navController,
                hideBottomNavigation = hideNavBar,
                showBottomNavigation = { showNavBar(false) },
            )
        }

        // Spotify is hidden entirely in YouTube-only mode (it has nothing to offer without its source).
        composable<SpotifyLoginDestination> {
            if (!sharedViewModel.youtubeOnly.value) {
                SpotifyLoginScreen(
                    innerPadding = innerPadding,
                    navController = navController,
                    hideBottomNavigation = hideNavBar,
                    showBottomNavigation = { showNavBar(false) },
                )
            }
        }

        // Discord: OAuth (device code flow) when a build carries the app; the WebView login otherwise.
        composable<DiscordLoginDestination> {
            if (isDiscordOAuthAvailable()) {
                DiscordOAuthLoginScreen(
                    innerPadding = innerPadding,
                    navController = navController,
                    hideBottomNavigation = hideNavBar,
                    showBottomNavigation = { showNavBar(false) },
                )
            } else {
                DiscordLoginScreen(
                    innerPadding = innerPadding,
                    navController = navController,
                    hideBottomNavigation = hideNavBar,
                    showBottomNavigation = { showNavBar(false) },
                )
            }
        }

        // Last.fm desktop auth flow (asks for a request token, trades it for a session key on return).
        composable<LastfmLoginDestination> {
            LastfmLoginScreen(
                innerPadding = innerPadding,
                navController = navController,
                hideBottomNavigation = hideNavBar,
                showBottomNavigation = { showNavBar(false) },
            )
        }
    }
}