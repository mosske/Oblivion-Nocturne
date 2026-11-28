package com.oblivion.nocturne.ui.screen.home
import kotlin.time.Instant

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.eygraber.uri.toKmpUri
import com.oblivion.nocturne.common.LIMIT_CACHE_SIZE
import com.oblivion.nocturne.common.QUALITY
import com.oblivion.nocturne.common.SUPPORTED_LANGUAGE
import com.oblivion.nocturne.common.SUPPORTED_LOCATION
import com.oblivion.nocturne.common.SponsorBlockType
import com.oblivion.nocturne.common.VIDEO_QUALITY
import com.oblivion.nocturne.domain.extension.now
import com.oblivion.nocturne.domain.data.model.lyrics.RomanizationDictionaryState
import com.oblivion.nocturne.domain.data.model.lyrics.RomanizationLanguage
import com.oblivion.nocturne.domain.manager.DataStoreManager
import com.oblivion.nocturne.domain.manager.DataStoreManager.Values.TRUE
import com.oblivion.nocturne.domain.repository.ImportProgress
import com.oblivion.nocturne.domain.utils.LocalResource
import com.oblivion.nocturne.logger.Logger
import com.oblivion.nocturne.Platform
import com.maxrave.simpmusic.expect.ui.LoginSyncDialog
import com.maxrave.simpmusic.expect.ui.filePickerResult
import com.maxrave.simpmusic.expect.ui.fileSaverResult
import com.maxrave.simpmusic.expect.ui.isLyricsBlurSupported
import com.maxrave.simpmusic.expect.ui.isWallpaperDynamicColorSupported
import com.maxrave.simpmusic.expect.ui.openEqResult
import com.maxrave.simpmusic.expect.ui.rememberLocalNetworkPermission
import com.oblivion.nocturne.extension.barBlurStyle
import com.oblivion.nocturne.extension.bytesToMB
import com.oblivion.nocturne.extension.displayString
import com.oblivion.nocturne.extension.isTwoLetterCode
import com.oblivion.nocturne.extension.isValidProxyHost
import com.oblivion.nocturne.getPlatform
import com.oblivion.nocturne.ui.component.ActionButton
import com.oblivion.nocturne.ui.component.AmbientThemeGlow
import com.oblivion.nocturne.ui.component.CenterLoadingBox
import com.oblivion.nocturne.ui.component.EndOfPage
import com.oblivion.nocturne.ui.component.LoadingDialog
import com.oblivion.nocturne.ui.component.RippleIconButton
import com.oblivion.nocturne.ui.component.SettingGroup
import com.oblivion.nocturne.ui.component.SettingItem
import com.oblivion.nocturne.ui.icon.ArrowBackIosNew
import com.oblivion.nocturne.ui.icon.Close
import com.oblivion.nocturne.ui.icon.Error
import com.oblivion.nocturne.ui.icon.PeopleAlt
import com.oblivion.nocturne.ui.icon.PlaylistAdd
import com.oblivion.nocturne.ui.icon.SimpIcons
import com.oblivion.nocturne.ui.navigation.destination.home.AppLogDestination
import com.oblivion.nocturne.ui.navigation.destination.home.CreditDestination
import com.oblivion.nocturne.ui.navigation.destination.login.DiscordLoginDestination
import com.oblivion.nocturne.ui.navigation.destination.login.LastfmLoginDestination
import com.oblivion.nocturne.ui.navigation.destination.login.LoginDestination
import com.oblivion.nocturne.ui.navigation.destination.login.SpotifyLoginDestination
import com.oblivion.nocturne.ui.theme.md_theme_dark_primary
import com.oblivion.nocturne.ui.theme.parseThemeColorHex
import com.oblivion.nocturne.ui.theme.typo
import com.oblivion.nocturne.utils.VersionManager
import com.oblivion.nocturne.viewModel.ImportViewModel
import com.oblivion.nocturne.viewModel.SettingAlertState
import com.oblivion.nocturne.viewModel.SettingBasicAlertState
import com.oblivion.nocturne.viewModel.SettingsViewModel
import com.oblivion.nocturne.viewModel.SharedViewModel
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.ui.compose.ChipColors
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.m3.libraryColors
import com.mikepenz.aboutlibraries.ui.compose.produceLibraries
import dev.chrisbanes.haze.HazeInput
import dev.chrisbanes.haze.blur.hazeBlur
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import com.oblivion.nocturne.ui.icon.Person
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import oblivionnocturne.composeapp.generated.resources.Res
import oblivionnocturne.composeapp.generated.resources.oblivion_owl
import oblivionnocturne.composeapp.generated.resources.third_party_libraries
import oblivionnocturne.composeapp.generated.resources.upload_your_listening_history_to_youtube_music_server_it_will_make_yt_music_recommendation_system_better_working_only_if_logged_in
import oblivionnocturne.composeapp.generated.resources.animated_artwork_info
import oblivionnocturne.composeapp.generated.resources.cancel
import oblivionnocturne.composeapp.generated.resources.canvas_info
import oblivionnocturne.composeapp.generated.resources.categories_sponsor_block
import oblivionnocturne.composeapp.generated.resources.change
import oblivionnocturne.composeapp.generated.resources.change_language_warning
import oblivionnocturne.composeapp.generated.resources.check_for_update
import oblivionnocturne.composeapp.generated.resources.checking
import oblivionnocturne.composeapp.generated.resources.clear
import oblivionnocturne.composeapp.generated.resources.clear_canvas_cache
import oblivionnocturne.composeapp.generated.resources.clear_downloaded_cache
import oblivionnocturne.composeapp.generated.resources.clear_listening_history
import oblivionnocturne.composeapp.generated.resources.clear_listening_history_confirm
import oblivionnocturne.composeapp.generated.resources.clear_listening_history_description
import oblivionnocturne.composeapp.generated.resources.clear_player_cache
import oblivionnocturne.composeapp.generated.resources.clear_thumbnail_cache
import oblivionnocturne.composeapp.generated.resources.combine_local_and_youtube_liked_songs
import oblivionnocturne.composeapp.generated.resources.combine_local_and_youtube_liked_songs_description
import oblivionnocturne.composeapp.generated.resources.content
import oblivionnocturne.composeapp.generated.resources.content_country
import oblivionnocturne.composeapp.generated.resources.contributor_email
import oblivionnocturne.composeapp.generated.resources.contributor_name
import oblivionnocturne.composeapp.generated.resources.crossfade
import oblivionnocturne.composeapp.generated.resources.crossfade_auto
import oblivionnocturne.composeapp.generated.resources.crossfade_description
import oblivionnocturne.composeapp.generated.resources.crossfade_dj_mode
import oblivionnocturne.composeapp.generated.resources.crossfade_dj_mode_description
import oblivionnocturne.composeapp.generated.resources.crossfade_duration
import oblivionnocturne.composeapp.generated.resources.crossfade_skip_album
import oblivionnocturne.composeapp.generated.resources.crossfade_skip_album_description
import oblivionnocturne.composeapp.generated.resources.custom_ai_model_id
import oblivionnocturne.composeapp.generated.resources.custom_color
import oblivionnocturne.composeapp.generated.resources.custom_model_id_messages
import oblivionnocturne.composeapp.generated.resources.daily
import oblivionnocturne.composeapp.generated.resources.database
import oblivionnocturne.composeapp.generated.resources.default_models
import oblivionnocturne.composeapp.generated.resources.developer_blog_tagline
import oblivionnocturne.composeapp.generated.resources.discord_integration
import oblivionnocturne.composeapp.generated.resources.download_quality
import oblivionnocturne.composeapp.generated.resources.downloaded_cache
import oblivionnocturne.composeapp.generated.resources.enable_animated_artwork
import oblivionnocturne.composeapp.generated.resources.enable_canvas
import oblivionnocturne.composeapp.generated.resources.enable_liquid_glass_effect
import oblivionnocturne.composeapp.generated.resources.enable_liquid_glass_effect_description
import oblivionnocturne.composeapp.generated.resources.enable_rich_presence
import oblivionnocturne.composeapp.generated.resources.enable_scrobbling
import oblivionnocturne.composeapp.generated.resources.enable_sponsor_block
import oblivionnocturne.composeapp.generated.resources.enable_spotify_lyrics
import oblivionnocturne.composeapp.generated.resources.equalizer
import oblivionnocturne.composeapp.generated.resources.equalizer_description
import oblivionnocturne.composeapp.generated.resources.equalizer_type
import oblivionnocturne.composeapp.generated.resources.equalizer_type_built_in
import oblivionnocturne.composeapp.generated.resources.equalizer_type_system
import oblivionnocturne.composeapp.generated.resources.free_space
import oblivionnocturne.composeapp.generated.resources.gemini
import oblivionnocturne.composeapp.generated.resources.guest
import oblivionnocturne.composeapp.generated.resources.http
import oblivionnocturne.composeapp.generated.resources.import_data
import oblivionnocturne.composeapp.generated.resources.import_data_intro
import oblivionnocturne.composeapp.generated.resources.import_failed
import oblivionnocturne.composeapp.generated.resources.import_playlists_from_other_apps
import oblivionnocturne.composeapp.generated.resources.import_progress_songs
import oblivionnocturne.composeapp.generated.resources.import_reading_file
import oblivionnocturne.composeapp.generated.resources.import_result
import oblivionnocturne.composeapp.generated.resources.import_result_skipped
import oblivionnocturne.composeapp.generated.resources.intro_login_to_discord
import oblivionnocturne.composeapp.generated.resources.intro_login_to_lastfm
import oblivionnocturne.composeapp.generated.resources.intro_login_to_spotify
import oblivionnocturne.composeapp.generated.resources.invalid
import oblivionnocturne.composeapp.generated.resources.invalid_api_key
import oblivionnocturne.composeapp.generated.resources.invalid_host
import oblivionnocturne.composeapp.generated.resources.invalid_language_code
import oblivionnocturne.composeapp.generated.resources.invalid_port
import oblivionnocturne.composeapp.generated.resources.keep_backups
import oblivionnocturne.composeapp.generated.resources.keep_backups_format
import oblivionnocturne.composeapp.generated.resources.keep_your_youtube_playlist_offline
import oblivionnocturne.composeapp.generated.resources.keep_your_youtube_playlist_offline_description
import oblivionnocturne.composeapp.generated.resources.kill_service_on_exit
import oblivionnocturne.composeapp.generated.resources.kill_service_on_exit_description
import oblivionnocturne.composeapp.generated.resources.language
import oblivionnocturne.composeapp.generated.resources.last_backup
import oblivionnocturne.composeapp.generated.resources.last_checked_at
import oblivionnocturne.composeapp.generated.resources.lastfm_integration
import oblivionnocturne.composeapp.generated.resources.limit_player_cache
import oblivionnocturne.composeapp.generated.resources.listening_history
import oblivionnocturne.composeapp.generated.resources.local_tracking_description
import oblivionnocturne.composeapp.generated.resources.local_tracking_title
import oblivionnocturne.composeapp.generated.resources.log_in_to_discord
import oblivionnocturne.composeapp.generated.resources.log_in_to_lastfm
import oblivionnocturne.composeapp.generated.resources.log_in_to_spotify
import oblivionnocturne.composeapp.generated.resources.log_out
import oblivionnocturne.composeapp.generated.resources.log_out_from_discord
import oblivionnocturne.composeapp.generated.resources.log_out_from_lastfm
import oblivionnocturne.composeapp.generated.resources.log_out_from_spotify
import oblivionnocturne.composeapp.generated.resources.log_out_warning
import oblivionnocturne.composeapp.generated.resources.logged_in
import oblivionnocturne.composeapp.generated.resources.logged_in_as
import oblivionnocturne.composeapp.generated.resources.login_sync_android_description
import oblivionnocturne.composeapp.generated.resources.login_sync_android_title
import oblivionnocturne.composeapp.generated.resources.login_sync_desktop_description
import oblivionnocturne.composeapp.generated.resources.login_sync_desktop_title
import oblivionnocturne.composeapp.generated.resources.login_sync_section
import oblivionnocturne.composeapp.generated.resources.lrclib
import oblivionnocturne.composeapp.generated.resources.lyrics
import oblivionnocturne.composeapp.generated.resources.lyrics_offset
import oblivionnocturne.composeapp.generated.resources.lyrics_offset_invalid
import oblivionnocturne.composeapp.generated.resources.lyrics_offset_message
import oblivionnocturne.composeapp.generated.resources.lyrics_offset_value
import oblivionnocturne.composeapp.generated.resources.lyrics_romanization
import oblivionnocturne.composeapp.generated.resources.lyrics_romanization_description
import oblivionnocturne.composeapp.generated.resources.lyrics_style
import oblivionnocturne.composeapp.generated.resources.lyrics_style_apple_music
import oblivionnocturne.composeapp.generated.resources.lyrics_style_classic
import oblivionnocturne.composeapp.generated.resources.main_lyrics_provider
import oblivionnocturne.composeapp.generated.resources.manage_your_youtube_accounts
import oblivionnocturne.composeapp.generated.resources.monthly
import oblivionnocturne.composeapp.generated.resources.never
import oblivionnocturne.composeapp.generated.resources.no_account
import oblivionnocturne.composeapp.generated.resources.normalize_volume
import oblivionnocturne.composeapp.generated.resources.not_available_while_casting
import oblivionnocturne.composeapp.generated.resources.now_playing_style
import oblivionnocturne.composeapp.generated.resources.now_playing_style_apple_music
import oblivionnocturne.composeapp.generated.resources.now_playing_style_m3_expressive
import oblivionnocturne.composeapp.generated.resources.now_playing_style_soundcloud
import oblivionnocturne.composeapp.generated.resources.now_playing_style_spotify
import oblivionnocturne.composeapp.generated.resources.oblivionnocturne_lyrics
import oblivionnocturne.composeapp.generated.resources.ok
import oblivionnocturne.composeapp.generated.resources.open_system_equalizer
import oblivionnocturne.composeapp.generated.resources.openai
import oblivionnocturne.composeapp.generated.resources.openai_api_compatible
import oblivionnocturne.composeapp.generated.resources.original_audio
import oblivionnocturne.composeapp.generated.resources.other_app
import oblivionnocturne.composeapp.generated.resources.play_explicit_content
import oblivionnocturne.composeapp.generated.resources.play_explicit_content_description
import oblivionnocturne.composeapp.generated.resources.play_video_for_video_track_instead_of_audio_only
import oblivionnocturne.composeapp.generated.resources.playback
import oblivionnocturne.composeapp.generated.resources.player_cache
import oblivionnocturne.composeapp.generated.resources.preferred_audio_language
import oblivionnocturne.composeapp.generated.resources.preferred_audio_language_message
import oblivionnocturne.composeapp.generated.resources.proxy
import oblivionnocturne.composeapp.generated.resources.proxy_description
import oblivionnocturne.composeapp.generated.resources.proxy_host
import oblivionnocturne.composeapp.generated.resources.proxy_host_message
import oblivionnocturne.composeapp.generated.resources.proxy_password
import oblivionnocturne.composeapp.generated.resources.proxy_password_message
import oblivionnocturne.composeapp.generated.resources.proxy_port
import oblivionnocturne.composeapp.generated.resources.proxy_port_message
import oblivionnocturne.composeapp.generated.resources.proxy_type
import oblivionnocturne.composeapp.generated.resources.proxy_username
import oblivionnocturne.composeapp.generated.resources.proxy_username_message
import oblivionnocturne.composeapp.generated.resources.quality
import oblivionnocturne.composeapp.generated.resources.radio_audio_only
import oblivionnocturne.composeapp.generated.resources.radio_audio_only_description
import oblivionnocturne.composeapp.generated.resources.requires_android_12
import oblivionnocturne.composeapp.generated.resources.restore_your_data
import oblivionnocturne.composeapp.generated.resources.restore_your_saved_data
import oblivionnocturne.composeapp.generated.resources.rich_presence_info
import oblivionnocturne.composeapp.generated.resources.romanization_belarusian
import oblivionnocturne.composeapp.generated.resources.romanization_bulgarian
import oblivionnocturne.composeapp.generated.resources.romanization_chinese
import oblivionnocturne.composeapp.generated.resources.romanization_hindi
import oblivionnocturne.composeapp.generated.resources.romanization_japanese
import oblivionnocturne.composeapp.generated.resources.romanization_japanese_dict_downloading
import oblivionnocturne.composeapp.generated.resources.romanization_japanese_dict_failed
import oblivionnocturne.composeapp.generated.resources.romanization_korean
import oblivionnocturne.composeapp.generated.resources.romanization_kyrgyz
import oblivionnocturne.composeapp.generated.resources.romanization_macedonian
import oblivionnocturne.composeapp.generated.resources.romanization_punjabi
import oblivionnocturne.composeapp.generated.resources.romanization_russian
import oblivionnocturne.composeapp.generated.resources.romanization_serbian
import oblivionnocturne.composeapp.generated.resources.romanization_ukrainian
import oblivionnocturne.composeapp.generated.resources.save
import oblivionnocturne.composeapp.generated.resources.save_all_your_playlist_data
import oblivionnocturne.composeapp.generated.resources.save_playback_state
import oblivionnocturne.composeapp.generated.resources.save_shuffle_and_repeat_mode
import oblivionnocturne.composeapp.generated.resources.scrobbling_info
import oblivionnocturne.composeapp.generated.resources.send_back_listening_data_to_google
import oblivionnocturne.composeapp.generated.resources.set
import oblivionnocturne.composeapp.generated.resources.settings
import oblivionnocturne.composeapp.generated.resources.signed_in
import oblivionnocturne.composeapp.generated.resources.skip_no_music_part
import oblivionnocturne.composeapp.generated.resources.skip_silent
import oblivionnocturne.composeapp.generated.resources.skip_sponsor_part_of_video
import oblivionnocturne.composeapp.generated.resources.smart_downloads
import oblivionnocturne.composeapp.generated.resources.smart_downloads_description
import oblivionnocturne.composeapp.generated.resources.smart_downloads_needs_local_tracking
import oblivionnocturne.composeapp.generated.resources.smart_downloads_size
import oblivionnocturne.composeapp.generated.resources.smart_downloads_size_value
import oblivionnocturne.composeapp.generated.resources.socks
import oblivionnocturne.composeapp.generated.resources.sponsorBlock
import oblivionnocturne.composeapp.generated.resources.sponsor_block_intro
import oblivionnocturne.composeapp.generated.resources.spotify
import oblivionnocturne.composeapp.generated.resources.spotify_canvas_cache
import oblivionnocturne.composeapp.generated.resources.spotify_lyrícs_info
import oblivionnocturne.composeapp.generated.resources.storage
import oblivionnocturne.composeapp.generated.resources.such_as_music_video_lyrics_video_podcasts_and_more
import oblivionnocturne.composeapp.generated.resources.sync_follow_to_youtube
import oblivionnocturne.composeapp.generated.resources.sync_follow_to_youtube_description
import oblivionnocturne.composeapp.generated.resources.theme
import oblivionnocturne.composeapp.generated.resources.theme_color
import oblivionnocturne.composeapp.generated.resources.theme_color_custom
import oblivionnocturne.composeapp.generated.resources.theme_color_default
import oblivionnocturne.composeapp.generated.resources.theme_color_wallpaper
import oblivionnocturne.composeapp.generated.resources.theme_mode_dark
import oblivionnocturne.composeapp.generated.resources.theme_mode_light
import oblivionnocturne.composeapp.generated.resources.theme_mode_system
import oblivionnocturne.composeapp.generated.resources.thumbnail_cache
import oblivionnocturne.composeapp.generated.resources.translation_language
import oblivionnocturne.composeapp.generated.resources.translation_language_message
import oblivionnocturne.composeapp.generated.resources.unknown
import oblivionnocturne.composeapp.generated.resources.update_channel
import oblivionnocturne.composeapp.generated.resources.use_ai_translation
import oblivionnocturne.composeapp.generated.resources.use_ai_translation_description
import oblivionnocturne.composeapp.generated.resources.use_your_system_equalizer
import oblivionnocturne.composeapp.generated.resources.user_interface
import oblivionnocturne.composeapp.generated.resources.version
import oblivionnocturne.composeapp.generated.resources.version_format
import oblivionnocturne.composeapp.generated.resources.video_download_quality
import oblivionnocturne.composeapp.generated.resources.video_quality
import oblivionnocturne.composeapp.generated.resources.warning
import oblivionnocturne.composeapp.generated.resources.weekly
import oblivionnocturne.composeapp.generated.resources.what_segments_will_be_skipped
import oblivionnocturne.composeapp.generated.resources.youtube_account
import oblivionnocturne.composeapp.generated.resources.youtube_subtitle_language
import oblivionnocturne.composeapp.generated.resources.youtube_subtitle_language_message
import oblivionnocturne.composeapp.generated.resources.youtube_transcript
import oblivionnocturne.composeapp.generated.resources.base_url
import oblivionnocturne.composeapp.generated.resources.configured
import oblivionnocturne.composeapp.generated.resources.custom_base_url
import oblivionnocturne.composeapp.generated.resources.custom_base_url_message
import oblivionnocturne.composeapp.generated.resources.custom_headers
import oblivionnocturne.composeapp.generated.resources.custom_headers_json
import oblivionnocturne.composeapp.generated.resources.custom_headers_message
import oblivionnocturne.composeapp.generated.resources.headers_json
import oblivionnocturne.composeapp.generated.resources.invalid_json_format
import oblivionnocturne.composeapp.generated.resources.invalid_url_format
import oblivionnocturne.composeapp.generated.resources.not_set
import oblivionnocturne.composeapp.generated.resources.about_us
import oblivionnocturne.composeapp.generated.resources.add_an_account
import oblivionnocturne.composeapp.generated.resources.ai
import oblivionnocturne.composeapp.generated.resources.ai_api_key
import oblivionnocturne.composeapp.generated.resources.ai_provider
import oblivionnocturne.composeapp.generated.resources.anonymous
import oblivionnocturne.composeapp.generated.resources.app_log
import oblivionnocturne.composeapp.generated.resources.app_log_description
import oblivionnocturne.composeapp.generated.resources.app_name
import oblivionnocturne.composeapp.generated.resources.developer_option
import oblivionnocturne.composeapp.generated.resources.audio
import oblivionnocturne.composeapp.generated.resources.audio_delay
import oblivionnocturne.composeapp.generated.resources.audio_delay_description
import oblivionnocturne.composeapp.generated.resources.audio_reverb
import oblivionnocturne.composeapp.generated.resources.audio_reverb_description
import oblivionnocturne.composeapp.generated.resources.audio_eight_d
import oblivionnocturne.composeapp.generated.resources.audio_eight_d_description
import oblivionnocturne.composeapp.generated.resources.eight_d_unavailable
import oblivionnocturne.composeapp.generated.resources.author
import oblivionnocturne.composeapp.generated.resources.auto_backup
import oblivionnocturne.composeapp.generated.resources.auto_backup_description
import oblivionnocturne.composeapp.generated.resources.auto_check_for_update
import oblivionnocturne.composeapp.generated.resources.auto_check_for_update_description
import oblivionnocturne.composeapp.generated.resources.auto_download_liked_songs
import oblivionnocturne.composeapp.generated.resources.auto_download_liked_songs_description
import oblivionnocturne.composeapp.generated.resources.backup
import oblivionnocturne.composeapp.generated.resources.backup_downloaded
import oblivionnocturne.composeapp.generated.resources.backup_downloaded_description
import oblivionnocturne.composeapp.generated.resources.backup_frequency
import oblivionnocturne.composeapp.generated.resources.balance_media_loudness
import oblivionnocturne.composeapp.generated.resources.better_lyrics
import oblivionnocturne.composeapp.generated.resources.blog_notification_description
import oblivionnocturne.composeapp.generated.resources.blog_notification_title
@Composable
fun SettingScreen(
    navController: NavController,
    innerPadding: PaddingValues = PaddingValues(),
    viewModel: SettingsViewModel = koinViewModel(),
    sharedViewModel: SharedViewModel = koinInject(),
) {
    var width by rememberSaveable { mutableIntStateOf(0) }
    val uriHandler = LocalUriHandler.current
    val platformContext = LocalPlatformContext.current
    val density = LocalDensity.current
    val str_ai_api_key = stringResource(Res.string.ai_api_key)
    val str_ai_provider = stringResource(Res.string.ai_provider)
    val str_backup_frequency = stringResource(Res.string.backup_frequency)
    val str_better_lyrics = stringResource(Res.string.better_lyrics)
    val str_cancel = stringResource(Res.string.cancel)
    val str_categories_sponsor_block = stringResource(Res.string.categories_sponsor_block)
    val str_change = stringResource(Res.string.change)
    val str_change_language_warning = stringResource(Res.string.change_language_warning)
    val str_clear = stringResource(Res.string.clear)
    val str_clear_canvas_cache = stringResource(Res.string.clear_canvas_cache)
    val str_clear_downloaded_cache = stringResource(Res.string.clear_downloaded_cache)
    val str_clear_listening_history = stringResource(Res.string.clear_listening_history)
    val str_clear_listening_history_confirm = stringResource(Res.string.clear_listening_history_confirm)
    val str_clear_player_cache = stringResource(Res.string.clear_player_cache)
    val str_clear_thumbnail_cache = stringResource(Res.string.clear_thumbnail_cache)
    val str_content_country = stringResource(Res.string.content_country)
    val str_contributor_email = stringResource(Res.string.contributor_email)
    val str_contributor_name = stringResource(Res.string.contributor_name)
    val str_crossfade_auto = stringResource(Res.string.crossfade_auto)
    val str_crossfade_duration = stringResource(Res.string.crossfade_duration)
    val str_custom_ai_model_id = stringResource(Res.string.custom_ai_model_id)
    val str_custom_model_id_messages = stringResource(Res.string.custom_model_id_messages)
    val str_daily = stringResource(Res.string.daily)
    val str_download_quality = stringResource(Res.string.download_quality)
    val str_equalizer_type = stringResource(Res.string.equalizer_type)
    val str_gemini = stringResource(Res.string.gemini)
    val str_http = stringResource(Res.string.http)
    val str_invalid = stringResource(Res.string.invalid)
    val str_invalid_api_key = stringResource(Res.string.invalid_api_key)
    val str_invalid_host = stringResource(Res.string.invalid_host)
    val str_invalid_language_code = stringResource(Res.string.invalid_language_code)
    val str_invalid_port = stringResource(Res.string.invalid_port)
    val str_keep_backups = stringResource(Res.string.keep_backups)
    val str_language = stringResource(Res.string.language)
    val str_limit_player_cache = stringResource(Res.string.limit_player_cache)
    val str_log_out = stringResource(Res.string.log_out)
    val str_log_out_from_discord = stringResource(Res.string.log_out_from_discord)
    val str_log_out_from_lastfm = stringResource(Res.string.log_out_from_lastfm)
    val str_log_out_from_spotify = stringResource(Res.string.log_out_from_spotify)
    val str_log_out_warning = stringResource(Res.string.log_out_warning)
    val str_lrclib = stringResource(Res.string.lrclib)
    val str_lyrics_offset = stringResource(Res.string.lyrics_offset)
    val str_lyrics_offset_invalid = stringResource(Res.string.lyrics_offset_invalid)
    val str_lyrics_offset_message = stringResource(Res.string.lyrics_offset_message)
    val str_lyrics_romanization = stringResource(Res.string.lyrics_romanization)
    val str_lyrics_style = stringResource(Res.string.lyrics_style)
    val str_main_lyrics_provider = stringResource(Res.string.main_lyrics_provider)
    val str_monthly = stringResource(Res.string.monthly)
    val str_now_playing_style = stringResource(Res.string.now_playing_style)
    val str_oblivionnocturne_lyrics = stringResource(Res.string.oblivionnocturne_lyrics)
    val str_ok = stringResource(Res.string.ok)
    val str_openai = stringResource(Res.string.openai)
    val str_openai_api_compatible = stringResource(Res.string.openai_api_compatible)
    val str_preferred_audio_language = stringResource(Res.string.preferred_audio_language)
    val str_preferred_audio_language_message = stringResource(Res.string.preferred_audio_language_message)
    val str_proxy_host = stringResource(Res.string.proxy_host)
    val str_proxy_host_message = stringResource(Res.string.proxy_host_message)
    val str_proxy_password = stringResource(Res.string.proxy_password)
    val str_proxy_password_message = stringResource(Res.string.proxy_password_message)
    val str_proxy_port = stringResource(Res.string.proxy_port)
    val str_proxy_port_message = stringResource(Res.string.proxy_port_message)
    val str_proxy_type = stringResource(Res.string.proxy_type)
    val str_proxy_username = stringResource(Res.string.proxy_username)
    val str_proxy_username_message = stringResource(Res.string.proxy_username_message)
    val str_quality = stringResource(Res.string.quality)
    val str_save = stringResource(Res.string.save)
    val str_set = stringResource(Res.string.set)
    val str_smart_downloads_size = stringResource(Res.string.smart_downloads_size)
    val str_socks = stringResource(Res.string.socks)
    val str_theme = stringResource(Res.string.theme)
    val str_theme_color = stringResource(Res.string.theme_color)
    val str_translation_language = stringResource(Res.string.translation_language)
    val str_translation_language_message = stringResource(Res.string.translation_language_message)
    val str_video_download_quality = stringResource(Res.string.video_download_quality)
    val str_video_quality = stringResource(Res.string.video_quality)
    val str_warning = stringResource(Res.string.warning)
    val str_weekly = stringResource(Res.string.weekly)
    val str_youtube_subtitle_language = stringResource(Res.string.youtube_subtitle_language)
    val str_youtube_subtitle_language_message = stringResource(Res.string.youtube_subtitle_language_message)
    val str_youtube_transcript = stringResource(Res.string.youtube_transcript)
    // Backup and restore
    val formatter =
        LocalDateTime.Format {
            byUnicodePattern("yyyyMMddHHmmss")
        }
    val appName = stringResource(Res.string.app_name)

    val backupLauncher =
        fileSaverResult(
            "${appName}_${
                now().format(
                    formatter,
                )
            }.backup",
            "application/octet-stream",
        ) { uri ->
            uri?.let {
                viewModel.backup(it.toKmpUri())
            }
        }

    val restoreLauncher =
        filePickerResult { uri ->
            uri?.let {
                viewModel.restore(it.toKmpUri())
            }
        }

    // Import playlists converted on the web. filePickerResult offers every file type, because a
    // converted .json arrives with whatever MIME its source assigned it, and an application/json
    // filter would hide it on some hosts.
    val importViewModel: ImportViewModel = koinViewModel()
    val importState by importViewModel.importState.collectAsStateWithLifecycle()
    val importLauncher =
        filePickerResult { uri ->
            uri?.let {
                importViewModel.import(it.toKmpUri())
            }
        }

    // Open equalizer
    val resultLauncher = openEqResult(viewModel.getAudioSessionId())

    val language by viewModel.language.collectAsStateWithLifecycle()
    val location by viewModel.location.collectAsStateWithLifecycle()
    val quality by viewModel.quality.collectAsStateWithLifecycle()
    val downloadQuality by viewModel.downloadQuality.collectAsStateWithLifecycle()
    val autoDownloadLikedSongs by viewModel.autoDownloadLikedSongs.collectAsStateWithLifecycle()
    val smartDownloadsEnabled by viewModel.smartDownloadsEnabled.collectAsStateWithLifecycle()
    val smartDownloadsCount by viewModel.smartDownloadsCount.collectAsStateWithLifecycle()
    val videoDownloadQuality by viewModel.videoDownloadQuality.collectAsStateWithLifecycle()
    val keepYoutubePlaylistOffline by viewModel.keepYouTubePlaylistOffline.collectAsStateWithLifecycle()
    val localTrackingEnabled by viewModel.localTrackingEnabled.collectAsStateWithLifecycle(initialValue = false)
    val blogNotificationEnabled by viewModel.blogNotificationEnabled.collectAsStateWithLifecycle()
    val combineLocalAndYouTubeLiked by viewModel.combineLocalAndYouTubeLiked.collectAsStateWithLifecycle()
    val playVideo by remember { viewModel.playVideoInsteadOfAudio.map { it == TRUE } }.collectAsStateWithLifecycle(initialValue = false)
    val radioAudioOnly by remember { viewModel.radioAudioOnly.map { it == TRUE } }.collectAsStateWithLifecycle(initialValue = false)
    val videoQuality by viewModel.videoQuality.collectAsStateWithLifecycle()
    val sendData by remember { viewModel.sendBackToGoogle.map { it == TRUE } }.collectAsStateWithLifecycle(initialValue = false)
    val normalizeVolume by remember { viewModel.normalizeVolume.map { it == TRUE } }.collectAsStateWithLifecycle(initialValue = false)
    val skipSilent by remember { viewModel.skipSilent.map { it == TRUE } }.collectAsStateWithLifecycle(initialValue = false)
    val savePlaybackState by remember { viewModel.savedPlaybackState.map { it == TRUE } }.collectAsStateWithLifecycle(initialValue = false)
    val killServiceOnExit by remember { viewModel.killServiceOnExit.map { it == TRUE } }.collectAsStateWithLifecycle(initialValue = true)
    val mainLyricsProvider by viewModel.mainLyricsProvider.collectAsStateWithLifecycle()
    val lyricsOffsetMs by viewModel.lyricsOffsetMs.collectAsStateWithLifecycle()
    val youtubeSubtitleLanguage by viewModel.youtubeSubtitleLanguage.collectAsStateWithLifecycle()
    val preferredAudioLanguage by viewModel.preferredAudioLanguage.collectAsStateWithLifecycle()
    val spotifyLoggedIn by viewModel.spotifyLogIn.collectAsStateWithLifecycle()
    val spotifyLyrics by viewModel.spotifyLyrics.collectAsStateWithLifecycle()
    val spotifyCanvas by viewModel.spotifyCanvas.collectAsStateWithLifecycle()
    val amAnimatedArtwork by viewModel.amAnimatedArtwork.collectAsStateWithLifecycle()
    val enableSponsorBlock by remember { viewModel.sponsorBlockEnabled.map { it == TRUE } }.collectAsStateWithLifecycle(initialValue = false)
    val skipSegments by viewModel.sponsorBlockCategories.collectAsStateWithLifecycle()
    val playerCache by viewModel.cacheSize.collectAsStateWithLifecycle()
    val downloadedCache by viewModel.downloadedCacheSize.collectAsStateWithLifecycle()
    val thumbnailCache by viewModel.thumbCacheSize.collectAsStateWithLifecycle()
    val canvasCache by viewModel.canvasCacheSize.collectAsStateWithLifecycle()
    val limitPlayerCache by viewModel.playerCacheLimit.collectAsStateWithLifecycle()
    val fraction by viewModel.fraction.collectAsStateWithLifecycle()
    val lastCheckUpdate by viewModel.lastCheckForUpdate.collectAsStateWithLifecycle()
    val explicitContentEnabled by viewModel.explicitContentEnabled.collectAsStateWithLifecycle()
    val usingProxy by viewModel.usingProxy.collectAsStateWithLifecycle()
    val proxyType by viewModel.proxyType.collectAsStateWithLifecycle()
    val proxyHost by viewModel.proxyHost.collectAsStateWithLifecycle()
    val proxyPort by viewModel.proxyPort.collectAsStateWithLifecycle()
    val proxyUsername by viewModel.proxyUsername.collectAsStateWithLifecycle()
    val proxyPassword by viewModel.proxyPassword.collectAsStateWithLifecycle()
    val autoCheckUpdate by viewModel.autoCheckUpdate.collectAsStateWithLifecycle()
    val aiProvider by viewModel.aiProvider.collectAsStateWithLifecycle()
    val isHasApiKey by viewModel.isHasApiKey.collectAsStateWithLifecycle()
    val useAITranslation by viewModel.useAITranslation.collectAsStateWithLifecycle()
    val translationLanguage by viewModel.translationLanguage.collectAsStateWithLifecycle()
    val customModelId by viewModel.customModelId.collectAsStateWithLifecycle()
    val customOpenAIBaseUrl by viewModel.customOpenAIBaseUrl.collectAsStateWithLifecycle()
    val customOpenAIHeaders by viewModel.customOpenAIHeaders.collectAsStateWithLifecycle()
    val localNetworkPermission = rememberLocalNetworkPermission()
    val helpBuildLyricsDatabase by viewModel.helpBuildLyricsDatabase.collectAsStateWithLifecycle()
    val contributor by viewModel.contributor.collectAsStateWithLifecycle()
    val backupDownloaded by viewModel.backupDownloaded.collectAsStateWithLifecycle()
    val autoBackupEnabled by viewModel.autoBackupEnabled.collectAsStateWithLifecycle()
    val autoBackupFrequency by viewModel.autoBackupFrequency.collectAsStateWithLifecycle()
    val autoBackupMaxFiles by viewModel.autoBackupMaxFiles.collectAsStateWithLifecycle()
    val autoBackupLastTime by viewModel.autoBackupLastTime.collectAsStateWithLifecycle()
    val updateChannel by viewModel.updateChannel.collectAsStateWithLifecycle()
    val enableLiquidGlass by viewModel.enableLiquidGlass.collectAsStateWithLifecycle()
    val themeMode by sharedViewModel.getThemeMode().collectAsStateWithLifecycle(DataStoreManager.THEME_MODE_DARK)
    val themeColorSource by sharedViewModel.getThemeColorSource().collectAsStateWithLifecycle(DataStoreManager.THEME_COLOR_DEFAULT)
    val customThemeColorHex by sharedViewModel.getCustomThemeColor().collectAsStateWithLifecycle(DataStoreManager.DEFAULT_THEME_COLOR_HEX)
    val nowPlayingStyle by sharedViewModel.getNowPlayingStyle().collectAsStateWithLifecycle(DataStoreManager.NOW_PLAYING_STYLE_SPOTIFY)
    val lyricsStyle by sharedViewModel.getLyricsStyle().collectAsStateWithLifecycle(DataStoreManager.LYRICS_STYLE_CLASSIC)
    val romanizationStored by sharedViewModel.getRomanizationLanguages().collectAsStateWithLifecycle("")
    val japaneseDictionaryState by viewModel.japaneseDictionaryState.collectAsStateWithLifecycle()
    var showColorPickerDialog by rememberSaveable { mutableStateOf(false) }
    val discordLoggedIn by viewModel.discordLoggedIn.collectAsStateWithLifecycle()
    val loggedIn by viewModel.loggedIn.collectAsStateWithLifecycle()
    val syncFollowToYouTube by viewModel.syncFollowToYouTube.collectAsStateWithLifecycle()
    val equalizerEnabled by viewModel.equalizerEnabled.collectAsStateWithLifecycle()
    val equalizerType by viewModel.equalizerType.collectAsStateWithLifecycle()
    val delayEnabled by viewModel.delayEnabled.collectAsStateWithLifecycle()
    val reverbEnabled by viewModel.reverbEnabled.collectAsStateWithLifecycle()
    val eightDEnabled by viewModel.eightDEnabled.collectAsStateWithLifecycle()
    val eightDAvailable by viewModel.eightDAvailable.collectAsStateWithLifecycle()
    val lastfmLoggedIn by viewModel.lastfmLoggedIn.collectAsStateWithLifecycle()
    val lastfmUsername by viewModel.lastfmUsername.collectAsStateWithLifecycle()
    val lastfmScrobbleEnabled by viewModel.lastfmScrobbleEnabled.collectAsStateWithLifecycle()
    val richPresenceEnabled by viewModel.richPresenceEnabled.collectAsStateWithLifecycle()

    val crossfadeEnabled by viewModel.crossfadeEnabled.collectAsStateWithLifecycle()
    val crossfadeDuration by viewModel.crossfadeDuration.collectAsStateWithLifecycle()
    val crossfadeDjMode by viewModel.crossfadeDjMode.collectAsStateWithLifecycle()
    val crossfadeSkipAlbum by viewModel.crossfadeSkipAlbum.collectAsStateWithLifecycle()
    val castState by viewModel.castState.collectAsStateWithLifecycle()

    val isCheckingUpdate by sharedViewModel.isCheckingUpdate.collectAsStateWithLifecycle()

    val hazeState =
        rememberHazeState()

    val lastCheckFormatted = remember(lastCheckUpdate) {
        val lastCheckLong = lastCheckUpdate?.toLong() ?: 0L
        Instant.fromEpochMilliseconds(lastCheckLong).toString()
    }
    val checkForUpdateSubtitle = if (isCheckingUpdate) {
        stringResource(Res.string.checking)
    } else {
        stringResource(Res.string.last_checked_at, lastCheckFormatted)
    }
    var showYouTubeAccountDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var showLoginSyncDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var showThirdPartyLibraries by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(true) {
        viewModel.getAllGoogleAccount()
    }

    LaunchedEffect(true) {
        viewModel.getData()
        viewModel.getThumbCacheSize(platformContext)
    }

    val settingListState = rememberLazyListState()
    // The toolbar is part of the list and scrolls away with it; a pinned, frosted copy takes over
    // once the Devices group has gone under it — Playlist's and Artist's way. The group's height
    // (top padding, header, card) is the scroll that carries it fully under the bar.
    var devicesGroupHeightPx by remember { mutableIntStateOf(0) }
    val showPinnedBar by remember {
        derivedStateOf {
            settingListState.firstVisibleItemIndex > 0 ||
                settingListState.firstVisibleItemScrollOffset > devicesGroupHeightPx
        }
    }
    // The list's side margin, which the in-list toolbar stretches back across.
    val listSidePadding = 16.dp
    // Home-family ambient ground, and like Home's it SCROLLS AWAY with the content instead of
    // hanging off the ceiling. Still a sibling (so it sits behind the floating bar), but its draw
    // rides the list: exact tracking while item 0 is on screen, parked off-screen after. Item 0 is
    // taller than the glow, so the glow has fully left before the branch ever switches — no jump.
    // graphicsLayer reads the state in the DRAW phase, so scrolling redraws without recomposing.
    // Tinted with the app's own primary rather than the song playing: this page is about the app.
    AmbientThemeGlow(
        tint = MaterialTheme.colorScheme.primary,
        modifier =
            Modifier.graphicsLayer {
                translationY =
                    if (settingListState.firstVisibleItemIndex == 0) {
                        -settingListState.firstVisibleItemScrollOffset.toFloat()
                    } else {
                        -size.height
                    }
            },
    )
    LazyColumn(
        state = settingListState,
        contentPadding = innerPadding,
        modifier =
            Modifier
                .padding(horizontal = listSidePadding)
                .hazeSource(hazeState),
    ) {
        item(key = "user_interface") {
            Column {
                // Was its own item. Folded in so item 0 is taller than the glow — the glow's
                // translation tracks item 0's offset exactly and parks once it scrolls past, and a
                // 64dp item 0 would have switched branches while the glow was still half-visible.
                // The toolbar's in-list copy, stretched across the list's side margin so it sits exactly
                // where the pinned copy appears; the list's top padding already clears the status bar.
                SettingTopBar(
                    onBack = { navController.navigateUp() },
                    windowInsets = WindowInsets(0),
                    modifier =
                        Modifier.layout { measurable, constraints ->
                            val bleed = listSidePadding.roundToPx()
                            val width = constraints.maxWidth + 2 * bleed
                            val placeable = measurable.measure(constraints.copy(minWidth = width, maxWidth = width))
                            layout(constraints.maxWidth, placeable.height) { placeable.place(-bleed, 0) }
                        },
                )
                // Above every section, and inside item 0 rather than an item of its own, for the
                // glow reason above.
                SettingGroup(
                    title = stringResource(Res.string.login_sync_section),
                    modifier = Modifier.onSizeChanged { devicesGroupHeightPx = it.height },
                ) {
                    SettingItem(
                        title =
                            stringResource(
                                if (getPlatform() == Platform.Android) Res.string.login_sync_android_title else Res.string.login_sync_desktop_title,
                            ),
                        subtitle =
                            stringResource(
                                if (getPlatform() == Platform.Android) {
                                    Res.string.login_sync_android_description
                                } else {
                                    Res.string.login_sync_desktop_description
                                },
                            ),
                        onClick = { showLoginSyncDialog = true },
                    )
                }
                SettingGroup(title = stringResource(Res.string.user_interface)) {
                    val themeModeLabels =
                        listOf(
                            DataStoreManager.THEME_MODE_SYSTEM to stringResource(Res.string.theme_mode_system),
                            DataStoreManager.THEME_MODE_DARK to stringResource(Res.string.theme_mode_dark),
                            DataStoreManager.THEME_MODE_LIGHT to stringResource(Res.string.theme_mode_light),
                        )
                    SettingItem(
                        title = stringResource(Res.string.theme),
                        subtitle = themeModeLabels.firstOrNull { it.first == themeMode }?.second ?: "",
                        onClick = {
                            viewModel.setAlertData(
                                SettingAlertState(
                                    title = str_theme,
                                    selectOne =
                                        SettingAlertState.SelectData(
                                            listSelect = themeModeLabels.map { (it.first == themeMode) to it.second },
                                        ),
                                    confirm = str_change to { state ->
                                            val selected = state.selectOne?.getSelected()
                                            themeModeLabels.firstOrNull { it.second == selected }?.first?.let {
                                                sharedViewModel.setThemeMode(it)
                                            }
                                        },
                                    dismiss = str_cancel,
                                ),
                            )
                        },
                    )
                    // The Apple Music treatments ARE the blur — the frosted page behind the player, and
                    // the depth of field on the lyrics — and Modifier.blur is a documented no-op below
                    // Android 12, so on an older device they render as a flat, wrong-looking version of
                    // themselves. The requirement is spelled out on the option itself rather than left
                    // for the user to discover after switching.
                    val requiresAndroid12 = " (" + stringResource(Res.string.requires_android_12) + ")"
                    val nowPlayingStyleLabels =
                        listOf(
                            DataStoreManager.NOW_PLAYING_STYLE_SPOTIFY to stringResource(Res.string.now_playing_style_spotify),
                            DataStoreManager.NOW_PLAYING_STYLE_M3_EXPRESSIVE to stringResource(Res.string.now_playing_style_m3_expressive),
                            DataStoreManager.NOW_PLAYING_STYLE_APPLE_MUSIC to
                                stringResource(Res.string.now_playing_style_apple_music) + requiresAndroid12,
                            DataStoreManager.NOW_PLAYING_STYLE_SOUNDCLOUD to stringResource(Res.string.now_playing_style_soundcloud),
                        )
                    SettingItem(
                        title = stringResource(Res.string.now_playing_style),
                        subtitle = nowPlayingStyleLabels.firstOrNull { it.first == nowPlayingStyle }?.second ?: "",
                        onClick = {
                            viewModel.setAlertData(
                                SettingAlertState(
                                    title = str_now_playing_style,
                                    selectOne =
                                        SettingAlertState.SelectData(
                                            listSelect = nowPlayingStyleLabels.map { (it.first == nowPlayingStyle) to it.second },
                                        ),
                                    confirm = str_change to { state ->
                                            val selected = state.selectOne?.getSelected()
                                            nowPlayingStyleLabels.firstOrNull { it.second == selected }?.first?.let {
                                                sharedViewModel.setNowPlayingStyle(it)
                                            }
                                        },
                                    dismiss = str_cancel,
                                ),
                            )
                        },
                    )
                    // Hidden outright below Android 12 rather than offered with one option: the Apple
                    // Music treatment IS the blur, and Modifier.blur is a documented no-op there, so
                    // the choice would be between Classic and a broken-looking Classic.
                    if (isLyricsBlurSupported()) {
                        val lyricsStyleLabels =
                            listOf(
                                DataStoreManager.LYRICS_STYLE_CLASSIC to stringResource(Res.string.lyrics_style_classic),
                                DataStoreManager.LYRICS_STYLE_APPLE_MUSIC to
                                    stringResource(Res.string.lyrics_style_apple_music) + requiresAndroid12,
                            )
                        SettingItem(
                            title = stringResource(Res.string.lyrics_style),
                            subtitle = lyricsStyleLabels.firstOrNull { it.first == lyricsStyle }?.second ?: "",
                            onClick = {
                                viewModel.setAlertData(
                                    SettingAlertState(
                                        title = str_lyrics_style,
                                        selectOne =
                                            SettingAlertState.SelectData(
                                                listSelect = lyricsStyleLabels.map { (it.first == lyricsStyle) to it.second },
                                            ),
                                        confirm = str_change to { state ->
                                                val selected = state.selectOne?.getSelected()
                                                lyricsStyleLabels.firstOrNull { it.second == selected }?.first?.let {
                                                    sharedViewModel.setLyricsStyle(it)
                                                }
                                            },
                                        dismiss = str_cancel,
                                    ),
                                )
                            },
                        )
                    }

                    // Independent of BOTH style settings, and not gated on Android 12: this changes
                    // what the words SAY, not how they are drawn, so it applies to every style on
                    // every version. Sits next to them because a user looking for "something about
                    // lyrics" looks in one place.
                    val romanizationLabels =
                        listOf(
                            RomanizationLanguage.JAPANESE to stringResource(Res.string.romanization_japanese),
                            RomanizationLanguage.KOREAN to stringResource(Res.string.romanization_korean),
                            RomanizationLanguage.CHINESE to stringResource(Res.string.romanization_chinese),
                            RomanizationLanguage.HINDI to stringResource(Res.string.romanization_hindi),
                            RomanizationLanguage.PUNJABI to stringResource(Res.string.romanization_punjabi),
                            RomanizationLanguage.RUSSIAN to stringResource(Res.string.romanization_russian),
                            RomanizationLanguage.UKRAINIAN to stringResource(Res.string.romanization_ukrainian),
                            RomanizationLanguage.SERBIAN to stringResource(Res.string.romanization_serbian),
                            RomanizationLanguage.BULGARIAN to stringResource(Res.string.romanization_bulgarian),
                            RomanizationLanguage.BELARUSIAN to stringResource(Res.string.romanization_belarusian),
                            RomanizationLanguage.KYRGYZ to stringResource(Res.string.romanization_kyrgyz),
                            RomanizationLanguage.MACEDONIAN to stringResource(Res.string.romanization_macedonian),
                        )
                    val romanizationSelected = RomanizationLanguage.parse(romanizationStored)
                    SettingItem(
                        title = stringResource(Res.string.lyrics_romanization),
                        // Two different jobs for one line. Off, the row has to explain what the
                        // feature IS — nobody guesses "romanization" from the title alone. On, the only
                        // question worth answering at a glance is which of the twelve are picked, and
                        // the explanation has served its purpose.
                        subtitle =
                            if (romanizationSelected.isEmpty()) {
                                stringResource(Res.string.lyrics_romanization_description)
                            } else {
                                val selectedNames =
                                    romanizationLabels.filter { it.first in romanizationSelected }.joinToString(", ") { it.second }
                                // Japanese is the one language with a dictionary pack to fetch; while
                                // that is in flight — or has failed — the row says so, instead of
                                // listing Japanese as if it were already live.
                                when {
                                    RomanizationLanguage.JAPANESE !in romanizationSelected -> selectedNames
                                    japaneseDictionaryState == RomanizationDictionaryState.DOWNLOADING ->
                                        "$selectedNames — ${stringResource(Res.string.romanization_japanese_dict_downloading)}"
                                    japaneseDictionaryState == RomanizationDictionaryState.FAILED ->
                                        "$selectedNames — ${stringResource(Res.string.romanization_japanese_dict_failed)}"
                                    else -> selectedNames
                                }
                            },
                        onClick = {
                            viewModel.setAlertData(
                                SettingAlertState(
                                    title = str_lyrics_romanization,
                                    // NO `message` here, deliberately. The dialog picks its body with
                                    // an if/else-if chain that tests `message` FIRST, and that branch
                                    // renders only the text and an optional textField — a multipleSelect
                                    // passed alongside it is never reached, so the dialog came up with
                                    // the description and no languages at all.
                                    multipleSelect =
                                        SettingAlertState.SelectData(
                                            listSelect =
                                                romanizationLabels.map { (language, label) ->
                                                    (language in romanizationSelected) to label
                                                },
                                        ),
                                    confirm =
                                        str_save to { state ->
                                            val chosen = state.multipleSelect?.getListSelected().orEmpty()
                                            val languages =
                                                romanizationLabels.filter { it.second in chosen }.map { it.first }.toSet()
                                            sharedViewModel.setRomanizationLanguages(languages)
                                            // Japanese needs its dictionary pack on disk. A no-op when
                                            // it is already there (or bundled, as on Desktop) — and the
                                            // retry after a FAILED attempt is simply confirming again.
                                            if (RomanizationLanguage.JAPANESE in languages) {
                                                viewModel.downloadJapaneseDictionaryIfNeeded()
                                            }
                                        },
                                    dismiss = str_cancel,
                                ),
                            )
                        },
                    )
                    val colorSourceLabels =
                        buildList {
                            add(DataStoreManager.THEME_COLOR_DEFAULT to stringResource(Res.string.theme_color_default))
                            if (isWallpaperDynamicColorSupported()) {
                                add(DataStoreManager.THEME_COLOR_WALLPAPER to stringResource(Res.string.theme_color_wallpaper))
                            }
                            add(DataStoreManager.THEME_COLOR_CUSTOM to stringResource(Res.string.theme_color_custom))
                        }
                    SettingItem(
                        title = stringResource(Res.string.theme_color),
                        subtitle = colorSourceLabels.firstOrNull { it.first == themeColorSource }?.second ?: "",
                        onClick = {
                            viewModel.setAlertData(
                                SettingAlertState(
                                    title = str_theme_color,
                                    selectOne =
                                        SettingAlertState.SelectData(
                                            listSelect = colorSourceLabels.map { (it.first == themeColorSource) to it.second },
                                        ),
                                    confirm = str_change to { state ->
                                            val selected = state.selectOne?.getSelected()
                                            colorSourceLabels.firstOrNull { it.second == selected }?.first?.let {
                                                sharedViewModel.setThemeColorSource(it)
                                                if (it == DataStoreManager.THEME_COLOR_CUSTOM) {
                                                    showColorPickerDialog = true
                                                }
                                            }
                                        },
                                    dismiss = str_cancel,
                                ),
                            )
                        },
                    )
                    if (themeColorSource == DataStoreManager.THEME_COLOR_CUSTOM) {
                        SettingItem(
                            title = stringResource(Res.string.custom_color),
                            subtitle = "#${customThemeColorHex.takeLast(6)}",
                            smallSubtitle = true,
                            onClick = { showColorPickerDialog = true },
                        )
                    }
                    SettingItem(
                        title = stringResource(Res.string.enable_liquid_glass_effect),
                        subtitle = stringResource(Res.string.enable_liquid_glass_effect_description),
                        smallSubtitle = true,
                        switch = (enableLiquidGlass to { viewModel.setEnableLiquidGlass(it) }),
                    )
                }
            }
        }
        item(key = "content") {
            SettingGroup(title = stringResource(Res.string.content)) {
                SettingItem(
                    title = stringResource(Res.string.youtube_account),
                    subtitle = stringResource(Res.string.manage_your_youtube_accounts),
                    onClick = {
                        viewModel.getAllGoogleAccount()
                        showYouTubeAccountDialog = true
                    },
                )
                SettingItem(
                    title = stringResource(Res.string.language),
                    subtitle = SUPPORTED_LANGUAGE.getLanguageFromCode(language ?: "en-US"),
                    onClick = {
                        viewModel.setAlertData(
                            SettingAlertState(
                                title = str_language,
                                selectOne =
                                    SettingAlertState.SelectData(
                                        listSelect =
                                            SUPPORTED_LANGUAGE.items.map {
                                                (it.toString() == SUPPORTED_LANGUAGE.getLanguageFromCode(language ?: "en-US")) to it.toString()
                                            },
                                    ),
                                confirm = str_change to { state ->
                                        val code = SUPPORTED_LANGUAGE.getCodeFromLanguage(state.selectOne?.getSelected() ?: "English")
                                        viewModel.setBasicAlertData(
                                            SettingBasicAlertState(
                                                title = str_warning,
                                                message = str_change_language_warning,
                                                confirm =
                                                    str_change to {
                                                        sharedViewModel.activityRecreate()
                                                        viewModel.setBasicAlertData(null)
                                                        viewModel.changeLanguage(code)
                                                    },
                                                dismiss = str_cancel,
                                            ),
                                        )
                                    },
                                dismiss = str_cancel,
                            ),
                        )
                    },
                )
                SettingItem(
                    title = stringResource(Res.string.content_country),
                    subtitle = location ?: "",
                    onClick = {
                        viewModel.setAlertData(
                            SettingAlertState(
                                title = str_content_country,
                                selectOne =
                                    SettingAlertState.SelectData(
                                        listSelect =
                                            SUPPORTED_LOCATION.items.map { item ->
                                                (item.toString() == location) to item.toString()
                                            },
                                    ),
                                confirm = str_change to { state ->
                                        viewModel.changeLocation(
                                            state.selectOne?.getSelected() ?: "US",
                                        )
                                    },
                                dismiss = str_cancel,
                            ),
                        )
                    },
                )
                SettingItem(
                    title = stringResource(Res.string.preferred_audio_language),
                    subtitle = preferredAudioLanguage.ifEmpty { stringResource(Res.string.original_audio) },
                    onClick = {
                        viewModel.setAlertData(
                            SettingAlertState(
                                title = str_preferred_audio_language,
                                textField =
                                    SettingAlertState.TextFieldData(
                                        label = str_preferred_audio_language,
                                        value = preferredAudioLanguage,
                                        // Empty is valid here: it means "original audio".
                                        verifyCodeBlock = {
                                            (it.isEmpty() || it.isTwoLetterCode()) to
                                                str_invalid_language_code
                                        },
                                    ),
                                message = str_preferred_audio_language_message,
                                confirm = str_change to { state ->
                                        viewModel.setPreferredAudioLanguage(state.textField?.value ?: "")
                                    },
                                dismiss = str_cancel,
                            ),
                        )
                    },
                )
                SettingItem(
                    title = stringResource(Res.string.quality),
                    subtitle = quality ?: "",
                    smallSubtitle = true,
                    onClick = {
                        viewModel.setAlertData(
                            SettingAlertState(
                                title = str_quality,
                                selectOne =
                                    SettingAlertState.SelectData(
                                        listSelect =
                                            QUALITY.items.map { item ->
                                                (item.toString() == quality) to item.toString()
                                            },
                                    ),
                                confirm = str_change to { state ->
                                        viewModel.changeQuality(state.selectOne?.getSelected())
                                    },
                                dismiss = str_cancel,
                            ),
                        )
                    },
                )
                SettingItem(
                    title = stringResource(Res.string.download_quality),
                    subtitle = downloadQuality ?: "",
                    smallSubtitle = true,
                    onClick = {
                        viewModel.setAlertData(
                            SettingAlertState(
                                title = str_download_quality,
                                selectOne =
                                    SettingAlertState.SelectData(
                                        listSelect =
                                            QUALITY.items.map { item ->
                                                (item.toString() == downloadQuality) to item.toString()
                                            },
                                    ),
                                confirm = str_change to { state ->
                                        state.selectOne?.getSelected()?.let { viewModel.setDownloadQuality(it) }
                                    },
                                dismiss = str_cancel,
                            ),
                        )
                    },
                )
                SettingItem(
                    title = stringResource(Res.string.video_quality),
                    subtitle = videoQuality ?: "",
                    onClick = {
                        viewModel.setAlertData(
                            SettingAlertState(
                                title = str_video_quality,
                                selectOne =
                                    SettingAlertState.SelectData(
                                        listSelect =
                                            VIDEO_QUALITY.items.map { item ->
                                                (item.toString() == videoQuality) to item.toString()
                                            },
                                    ),
                                confirm = str_change to { state ->
                                        viewModel.changeVideoQuality(state.selectOne?.getSelected() ?: "")
                                    },
                                dismiss = str_cancel,
                            ),
                        )
                    },
                )
                SettingItem(
                    title = stringResource(Res.string.video_download_quality),
                    subtitle = videoDownloadQuality ?: "",
                    onClick = {
                        viewModel.setAlertData(
                            SettingAlertState(
                                title = str_video_download_quality,
                                selectOne =
                                    SettingAlertState.SelectData(
                                        listSelect =
                                            VIDEO_QUALITY.items.map { item ->
                                                (item.toString() == videoDownloadQuality) to item.toString()
                                            },
                                    ),
                                confirm = str_change to { state ->
                                        viewModel.setVideoDownloadQuality(state.selectOne?.getSelected() ?: "")
                                    },
                                dismiss = str_cancel,
                            ),
                        )
                    },
                )
                SettingItem(
                    title = stringResource(Res.string.auto_download_liked_songs),
                    subtitle = stringResource(Res.string.auto_download_liked_songs_description),
                    smallSubtitle = true,
                    switch = (autoDownloadLikedSongs to { viewModel.setAutoDownloadLikedSongs(it) }),
                )
                // Smart downloads are Android only.
                if (getPlatform() == Platform.Android) {
                    SettingItem(
                        title = stringResource(Res.string.smart_downloads),
                        subtitle =
                            stringResource(
                                if (localTrackingEnabled) {
                                    Res.string.smart_downloads_description
                                } else {
                                    Res.string.smart_downloads_needs_local_tracking
                                },
                            ),
                        smallSubtitle = true,
                        switch = (smartDownloadsEnabled to { viewModel.setSmartDownloadsEnabled(it) }),
                        // Songs are picked from local tracking, so it gates switching on. Switching off
                        // stays possible without it, or the downloaded songs could not be cleared.
                        isEnable = localTrackingEnabled || smartDownloadsEnabled,
                    )
                    if (smartDownloadsEnabled) {
                        SettingItem(
                            title = stringResource(Res.string.smart_downloads_size),
                            subtitle = stringResource(Res.string.smart_downloads_size_value, smartDownloadsCount),
                            smallSubtitle = true,
                            onClick = {
                                viewModel.setAlertData(
                                    SettingAlertState(
                                        title = str_smart_downloads_size,
                                        selectOne =
                                            SettingAlertState.SelectData(
                                                listSelect =
                                                    listOf(25, 50, 100).map { count ->
                                                        (count == smartDownloadsCount) to count.toString()
                                                    },
                                            ),
                                        confirm = str_change to { state ->
                                                state.selectOne?.getSelected()?.toIntOrNull()?.let { viewModel.setSmartDownloadsCount(it) }
                                            },
                                        dismiss = str_cancel,
                                    ),
                                )
                            },
                        )
                    }
                }
                SettingItem(
                    title = stringResource(Res.string.play_video_for_video_track_instead_of_audio_only),
                    subtitle = stringResource(Res.string.such_as_music_video_lyrics_video_podcasts_and_more),
                    smallSubtitle = true,
                    switch = (playVideo to { viewModel.setPlayVideoInsteadOfAudio(it) }),
                )
                SettingItem(
                    title = stringResource(Res.string.radio_audio_only),
                    subtitle = stringResource(Res.string.radio_audio_only_description),
                    smallSubtitle = true,
                    switch = (radioAudioOnly to { viewModel.setRadioAudioOnly(it) }),
                )
                SettingItem(
                    title = stringResource(Res.string.sync_follow_to_youtube),
                    subtitle = stringResource(Res.string.sync_follow_to_youtube_description),
                    smallSubtitle = true,
                    switch = (syncFollowToYouTube to { viewModel.setSyncFollowToYouTube(it) }),
                    // Writing to someone's YouTube account needs a session, so the row is dead
                    // while signed out. Clearing the stored flag is NOT done from here: the reset
                    // belongs to the logout itself (SettingsViewModel.setUsedAccount /
                    // logOutAllYouTube), which runs whether or not Settings is ever opened.
                    isEnable = loggedIn == DataStoreManager.TRUE,
                )
                SettingItem(
                    title = stringResource(Res.string.send_back_listening_data_to_google),
                    subtitle =
                        stringResource(
                            Res.string
                                .upload_your_listening_history_to_youtube_music_server_it_will_make_yt_music_recommendation_system_better_working_only_if_logged_in,
                        ),
                    smallSubtitle = true,
                    switch = (sendData to { viewModel.setSendBackToGoogle(it) }),
                )
                SettingItem(
                    title = stringResource(Res.string.play_explicit_content),
                    subtitle = stringResource(Res.string.play_explicit_content_description),
                    switch = (explicitContentEnabled to { viewModel.setExplicitContentEnabled(it) }),
                )
                SettingItem(
                    title = stringResource(Res.string.keep_your_youtube_playlist_offline),
                    subtitle = stringResource(Res.string.keep_your_youtube_playlist_offline_description),
                    switch = (keepYoutubePlaylistOffline to { viewModel.setKeepYouTubePlaylistOffline(it) }),
                )
                /*
                SettingItem(
                    title = stringResource(Res.string.combine_local_and_youtube_liked_songs),
                    subtitle = stringResource(Res.string.combine_local_and_youtube_liked_songs_description),
                    switch = (combineLocalAndYouTubeLiked to { viewModel.setCombineLocalAndYouTubeLiked(it) })
                )
                 */
                SettingItem(
                    title = stringResource(Res.string.proxy),
                    subtitle = stringResource(Res.string.proxy_description),
                    switch = (usingProxy to { viewModel.setUsingProxy(it) }),
                )
                Crossfade(usingProxy) { it ->
                    if (it) {
                        Column {
                            SettingItem(
                                title = stringResource(Res.string.proxy_type),
                                subtitle =
                                    when (proxyType) {
                                        DataStoreManager.ProxyType.PROXY_TYPE_HTTP -> stringResource(Res.string.http)
                                        DataStoreManager.ProxyType.PROXY_TYPE_SOCKS -> stringResource(Res.string.socks)
                                    },
                                onClick = {
                                    viewModel.setAlertData(
                                        SettingAlertState(
                                            title = str_proxy_type,
                                            selectOne =
                                                SettingAlertState.SelectData(
                                                    listSelect =
                                                        listOf(
                                                            (proxyType == DataStoreManager.ProxyType.PROXY_TYPE_HTTP) to
                                                                str_http,
                                                            (proxyType == DataStoreManager.ProxyType.PROXY_TYPE_SOCKS) to
                                                                str_socks,
                                                        ),
                                                ),
                                            confirm = str_change to { state ->
                                                    viewModel.setProxy(
                                                        if (state.selectOne?.getSelected() == str_socks) {
                                                            DataStoreManager.ProxyType.PROXY_TYPE_SOCKS
                                                        } else {
                                                            DataStoreManager.ProxyType.PROXY_TYPE_HTTP
                                                        },
                                                        proxyHost,
                                                        proxyPort,
                                                    )
                                                },
                                            dismiss = str_cancel,
                                        ),
                                    )
                                },
                            )
                            SettingItem(
                                title = stringResource(Res.string.proxy_host),
                                subtitle = proxyHost,
                                onClick = {
                                    viewModel.setAlertData(
                                        SettingAlertState(
                                            title = str_proxy_host,
                                            message = str_proxy_host_message,
                                            textField =
                                                SettingAlertState.TextFieldData(
                                                    label = str_proxy_host,
                                                    value = proxyHost,
                                                    verifyCodeBlock = {
                                                        isValidProxyHost(it) to str_invalid_host
                                                    },
                                                ),
                                            confirm = str_change to { state ->
                                                    viewModel.setProxy(
                                                        proxyType,
                                                        state.textField?.value ?: "",
                                                        proxyPort,
                                                    )
                                                },
                                            dismiss = str_cancel,
                                        ),
                                    )
                                },
                            )
                            SettingItem(
                                title = stringResource(Res.string.proxy_port),
                                subtitle = proxyPort.toString(),
                                onClick = {
                                    viewModel.setAlertData(
                                        SettingAlertState(
                                            title = str_proxy_port,
                                            message = str_proxy_port_message,
                                            textField =
                                                SettingAlertState.TextFieldData(
                                                    label = str_proxy_port,
                                                    value = proxyPort.toString(),
                                                    verifyCodeBlock = {
                                                        (it.toIntOrNull() != null) to str_invalid_port
                                                    },
                                                ),
                                            confirm = str_change to { state ->
                                                    viewModel.setProxy(
                                                        proxyType,
                                                        proxyHost,
                                                        state.textField?.value?.toIntOrNull() ?: 0,
                                                    )
                                                },
                                            dismiss = str_cancel,
                                        ),
                                    )
                                },
                            )
                            SettingItem(
                                title = stringResource(Res.string.proxy_username),
                                subtitle = proxyUsername,
                                onClick = {
                                    viewModel.setAlertData(
                                        SettingAlertState(
                                            title = str_proxy_username,
                                            message = str_proxy_username_message,
                                            textField =
                                                SettingAlertState.TextFieldData(
                                                    label = str_proxy_username,
                                                    value = proxyUsername,
                                                ),
                                            confirm = str_change to { state ->
                                                    viewModel.setProxyCredentials(
                                                        state.textField?.value ?: "",
                                                        proxyPassword,
                                                    )
                                                },
                                            dismiss = str_cancel,
                                        ),
                                    )
                                },
                            )
                            SettingItem(
                                title = stringResource(Res.string.proxy_password),
                                subtitle =
                                    if (proxyPassword.isEmpty()) {
                                        ""
                                    } else {
                                        "\u2022".repeat(proxyPassword.length)
                                    },
                                onClick = {
                                    viewModel.setAlertData(
                                        SettingAlertState(
                                            title = str_proxy_password,
                                            message = str_proxy_password_message,
                                            textField =
                                                SettingAlertState.TextFieldData(
                                                    label = str_proxy_password,
                                                    value = proxyPassword,
                                                ),
                                            confirm = str_change to { state ->
                                                    viewModel.setProxyCredentials(
                                                        proxyUsername,
                                                        state.textField?.value ?: "",
                                                    )
                                                },
                                            dismiss = str_cancel,
                                        ),
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
        if (getPlatform() == Platform.Android) {
            item(key = "audio") {
                SettingGroup(title = stringResource(Res.string.audio)) {
                    SettingItem(
                        title = stringResource(Res.string.normalize_volume),
                        subtitle = stringResource(Res.string.balance_media_loudness),
                        switch = (normalizeVolume to { viewModel.setNormalizeVolume(it) }),
                    )
                    SettingItem(
                        title = stringResource(Res.string.skip_silent),
                        subtitle = stringResource(Res.string.skip_no_music_part),
                        switch = (skipSilent to { viewModel.setSkipSilent(it) }),
                    )
                    val equalizerTypeLabels =
                        listOf(
                            DataStoreManager.EQUALIZER_TYPE_BUILT_IN to stringResource(Res.string.equalizer_type_built_in),
                            DataStoreManager.EQUALIZER_TYPE_SYSTEM to stringResource(Res.string.equalizer_type_system),
                        )
                    SettingItem(
                        title = stringResource(Res.string.equalizer_type),
                        subtitle = equalizerTypeLabels.firstOrNull { it.first == equalizerType }?.second ?: "",
                        onClick = {
                            viewModel.setAlertData(
                                SettingAlertState(
                                    title = str_equalizer_type,
                                    selectOne =
                                        SettingAlertState.SelectData(
                                            listSelect = equalizerTypeLabels.map { (it.first == equalizerType) to it.second },
                                        ),
                                    confirm = str_change to { state ->
                                            val selected = state.selectOne?.getSelected()
                                            equalizerTypeLabels.firstOrNull { it.second == selected }?.first?.let {
                                                viewModel.setEqualizerType(it)
                                            }
                                        },
                                    dismiss = str_cancel,
                                ),
                            )
                        },
                    )
                    AnimatedVisibility(visible = equalizerType == DataStoreManager.EQUALIZER_TYPE_SYSTEM) {
                        SettingItem(
                            title = stringResource(Res.string.open_system_equalizer),
                            subtitle =
                                if (castState.isRemote) {
                                    stringResource(Res.string.not_available_while_casting)
                                } else {
                                    stringResource(Res.string.use_your_system_equalizer)
                                },
                            isEnable = !castState.isRemote,
                            onClick = {
                                coroutineScope.launch {
                                    resultLauncher.launch()
                                }
                            },
                        )
                    }
                }
            }
        }
        item(key = "playback") {
            SettingGroup(title = stringResource(Res.string.playback)) {
                // Under Playback rather than Audio because that whole group sits inside an
                // Android-only branch — "Open system equalizer" is an Android feature — and this
                // one is on both platforms: mpv's `af` chain on Desktop, an AudioProcessor in the
                // Media3 sink on Android, driven from the same stored curve.
                AnimatedVisibility(visible = getPlatform() != Platform.Android || equalizerType != DataStoreManager.EQUALIZER_TYPE_SYSTEM) {
                    Column {
                        SettingItem(
                            title = stringResource(Res.string.equalizer),
                            subtitle = stringResource(Res.string.equalizer_description),
                            smallSubtitle = true,
                            switch = (equalizerEnabled to { viewModel.setEqualizerEnabled(it) }),
                        )
                        // Only while on. A curve that visibly does nothing is worse than no curve —
                        // and the stored bands survive the switch, so turning it back on returns to
                        // the shape the user built rather than to flat.
                        AnimatedVisibility(visible = equalizerEnabled) {
                            EqualizerSection()
                        }
                    }
                }
                // Beside the equalizer rather than in its own group: all three are the same kind of
                // thing — one stored setting reshaping the audio on both backends — and a user
                // hunting for "reverb" looks wherever the sound settings are, not under a heading
                // they have to guess.
                SettingItem(
                    title = stringResource(Res.string.audio_delay),
                    subtitle = stringResource(Res.string.audio_delay_description),
                    smallSubtitle = true,
                    switch = (delayEnabled to { viewModel.setDelayEnabled(it) }),
                )
                // Only while on, like the curve — and the three values survive the switch, so
                // turning it back on returns to the echo the user dialled in.
                AnimatedVisibility(visible = delayEnabled) {
                    DelaySection()
                }
                SettingItem(
                    title = stringResource(Res.string.audio_reverb),
                    subtitle = stringResource(Res.string.audio_reverb_description),
                    smallSubtitle = true,
                    switch = (reverbEnabled to { viewModel.setReverbEnabled(it) }),
                )
                // Same again: the room and the wet level outlive the switch.
                AnimatedVisibility(visible = reverbEnabled) {
                    ReverbSection()
                }
                SettingItem(
                    title = stringResource(Res.string.audio_eight_d),
                    subtitle =
                        when {
                            castState.isRemote -> stringResource(Res.string.not_available_while_casting)
                            !eightDAvailable -> stringResource(Res.string.eight_d_unavailable)
                            else -> stringResource(Res.string.audio_eight_d_description)
                        },
                    smallSubtitle = true,
                    switch = (eightDEnabled to viewModel::setEightDEnabled),
                    // A saved on-state may still be switched off when the native filter is absent.
                    isEnable = !castState.isRemote && (eightDAvailable || eightDEnabled),
                )
                AnimatedVisibility(visible = eightDEnabled && eightDAvailable && !castState.isRemote) {
                    EightDSection()
                }
                SettingItem(
                    title = stringResource(Res.string.save_playback_state),
                    subtitle = stringResource(Res.string.save_shuffle_and_repeat_mode),
                    switch = (savePlaybackState to { viewModel.setSavedPlaybackState(it) }),
                )
                if (getPlatform() == Platform.Android) {
                    SettingItem(
                        title = stringResource(Res.string.kill_service_on_exit),
                        subtitle = stringResource(Res.string.kill_service_on_exit_description),
                        switch = (killServiceOnExit to { viewModel.setKillServiceOnExit(it) }),
                    )
                }
                // Crossfade (all platforms)
                SettingItem(
                    title = stringResource(Res.string.crossfade),
                    subtitle =
                        if (castState.isRemote) {
                            stringResource(Res.string.not_available_while_casting)
                        } else {
                            stringResource(Res.string.crossfade_description)
                        },
                    smallSubtitle = true,
                    switch = (crossfadeEnabled to { viewModel.setCrossfadeEnabled(it) }),
                    isEnable = !castState.isRemote,
                )
                AnimatedVisibility(visible = crossfadeEnabled) {
                    Column {
                        SettingItem(
                            title = stringResource(Res.string.crossfade_duration),
                            subtitle =
                                if (castState.isRemote) {
                                    stringResource(Res.string.not_available_while_casting)
                                } else if (crossfadeDuration == DataStoreManager.CROSSFADE_DURATION_AUTO) {
                                    stringResource(Res.string.crossfade_auto)
                                } else {
                                    "${crossfadeDuration / 1000}s"
                                },
                            isEnable = !castState.isRemote,
                            onClick = {
                                viewModel.setAlertData(
                                    SettingAlertState(
                                        title = str_crossfade_duration,
                                        selectOne =
                                            SettingAlertState.SelectData(
                                                listSelect =
                                                    listOf(
                                                        (crossfadeDuration == DataStoreManager.CROSSFADE_DURATION_AUTO) to
                                                            str_crossfade_auto,
                                                        (crossfadeDuration == 1000) to "1s",
                                                        (crossfadeDuration == 2000) to "2s",
                                                        (crossfadeDuration == 3000) to "3s",
                                                        (crossfadeDuration == 5000) to "5s",
                                                        (crossfadeDuration == 8000) to "8s",
                                                        (crossfadeDuration == 10000) to "10s",
                                                        (crossfadeDuration == 12000) to "12s",
                                                        (crossfadeDuration == 15000) to "15s",
                                                        (crossfadeDuration == 20000) to "20s",
                                                        (crossfadeDuration == 30000) to "30s",
                                                    ),
                                            ),
                                        confirm = str_change to { state ->
                                                val duration =
                                                    when (state.selectOne?.getSelected()) {
                                                        str_crossfade_auto,
                                                        -> DataStoreManager.CROSSFADE_DURATION_AUTO
                                                        "1s" -> 1000
                                                        "2s" -> 2000
                                                        "3s" -> 3000
                                                        "5s" -> 5000
                                                        "8s" -> 8000
                                                        "10s" -> 10000
                                                        "12s" -> 12000
                                                        "15s" -> 15000
                                                        "20s" -> 20000
                                                        "30s" -> 30000
                                                        else -> 5000
                                                    }
                                                viewModel.setCrossfadeDuration(duration)
                                            },
                                        dismiss = str_cancel,
                                    ),
                                )
                            },
                        )
//                        if (getPlatform() == Platform.Android) {
                        SettingItem(
                            title = stringResource(Res.string.crossfade_dj_mode),
                            subtitle =
                                if (castState.isRemote) {
                                    stringResource(Res.string.not_available_while_casting)
                                } else {
                                    stringResource(Res.string.crossfade_dj_mode_description)
                                },
                            smallSubtitle = true,
                            switch = ((crossfadeDjMode) to { viewModel.setCrossfadeDjMode(it) }),
                            isEnable = !castState.isRemote,
                        )
                        SettingItem(
                            title = stringResource(Res.string.crossfade_skip_album),
                            subtitle =
                                if (castState.isRemote) {
                                    stringResource(Res.string.not_available_while_casting)
                                } else {
                                    stringResource(Res.string.crossfade_skip_album_description)
                                },
                            smallSubtitle = true,
                            switch = ((crossfadeSkipAlbum) to { viewModel.setCrossfadeSkipAlbum(it) }),
                            isEnable = !castState.isRemote,
                        )
//                        }
                    }
                }
            }
        }
        // Deliberately not part of "storage" further down, which is Android-only: tracking and the
        // rows it leaves behind exist on Desktop just the same. The switch that produces the history
        // and the button that erases it belong together.
        item(key = "listening_history") {
            SettingGroup(title = stringResource(Res.string.listening_history)) {
                SettingItem(
                    title = stringResource(Res.string.local_tracking_title),
                    subtitle = stringResource(Res.string.local_tracking_description),
                    switch = (localTrackingEnabled to { viewModel.setLocalTrackingEnabled(it) }),
                )
                SettingItem(
                    title = stringResource(Res.string.clear_listening_history),
                    subtitle = stringResource(Res.string.clear_listening_history_description),
                    onClick = {
                        viewModel.setBasicAlertData(
                            SettingBasicAlertState(
                                title = str_clear_listening_history,
                                message = str_clear_listening_history_confirm,
                                confirm =
                                    str_clear to {
                                        viewModel.clearListeningHistory()
                                    },
                                dismiss = str_cancel,
                            ),
                        )
                    },
                )
            }
        }
        item(key = "lyrics") {
            SettingGroup(title = stringResource(Res.string.lyrics)) {
                SettingItem(
                    title = stringResource(Res.string.main_lyrics_provider),
                    subtitle =
                        when (mainLyricsProvider) {
                            DataStoreManager.OblivionNocturne -> stringResource(Res.string.oblivionnocturne_lyrics)
                            DataStoreManager.YOUTUBE -> stringResource(Res.string.youtube_transcript)
                            DataStoreManager.LRCLIB -> stringResource(Res.string.lrclib)
                            DataStoreManager.BETTER_LYRICS -> stringResource(Res.string.better_lyrics)
                            else -> stringResource(Res.string.unknown)
                        },
                    onClick = {
                        viewModel.setAlertData(
                            SettingAlertState(
                                title = str_main_lyrics_provider,
                                selectOne =
                                    SettingAlertState.SelectData(
                                        listSelect =
                                            listOf(
                                                (mainLyricsProvider == DataStoreManager.OblivionNocturne) to
                                                    str_oblivionnocturne_lyrics,
                                                (mainLyricsProvider == DataStoreManager.YOUTUBE) to
                                                    str_youtube_transcript,
                                                (mainLyricsProvider == DataStoreManager.LRCLIB) to str_lrclib,
                                                (mainLyricsProvider == DataStoreManager.BETTER_LYRICS) to
                                                    str_better_lyrics,
                                            ),
                                    ),
                                confirm = str_change to { state ->
                                        viewModel.setLyricsProvider(
                                            when (state.selectOne?.getSelected()) {
                                                str_oblivionnocturne_lyrics -> DataStoreManager.OblivionNocturne
                                                str_youtube_transcript -> DataStoreManager.YOUTUBE
                                                str_lrclib -> DataStoreManager.LRCLIB
                                                str_better_lyrics -> DataStoreManager.BETTER_LYRICS
                                                else -> DataStoreManager.OblivionNocturne
                                            },
                                        )
                                    },
                                dismiss = str_cancel,
                            ),
                        )
                    },
                )

                SettingItem(
                    title = stringResource(Res.string.lyrics_offset),
                    subtitle =
                        stringResource(
                            Res.string.lyrics_offset_value,
                            if (lyricsOffsetMs > 0) "+$lyricsOffsetMs" else lyricsOffsetMs.toString(),
                        ),
                    onClick = {
                        viewModel.setAlertData(
                            SettingAlertState(
                                title = str_lyrics_offset,
                                // The dialog renders its text field INSIDE the `message != null`
                                // branch, so a state carrying a textField and no message opens an
                                // empty box with no error anywhere.
                                message = str_lyrics_offset_message,
                                textField =
                                    SettingAlertState.TextFieldData(
                                        label = str_lyrics_offset,
                                        value = lyricsOffsetMs.toString(),
                                        // Only that it is a whole number — no range. How far a
                                        // listener's own audio path lags is theirs to say.
                                        verifyCodeBlock = {
                                            (it.trim().toIntOrNull() != null) to
                                                str_lyrics_offset_invalid
                                        },
                                    ),
                                confirm = str_change to { state ->
                                        state.textField
                                            ?.value
                                            ?.trim()
                                            ?.toIntOrNull()
                                            ?.let { viewModel.setLyricsOffsetMs(it) }
                                    },
                                dismiss = str_cancel,
                            ),
                        )
                    },
                )

                SettingItem(
                    title = stringResource(Res.string.translation_language),
                    subtitle = translationLanguage ?: "",
                    onClick = {
                        viewModel.setAlertData(
                            SettingAlertState(
                                title = str_translation_language,
                                textField =
                                    SettingAlertState.TextFieldData(
                                        label = str_translation_language,
                                        value = translationLanguage ?: "",
                                        verifyCodeBlock = {
                                            (it.length == 2 && it.isTwoLetterCode()) to
                                                str_invalid_language_code
                                        },
                                    ),
                                message = str_translation_language_message,
                                confirm = str_change to { state ->
                                        viewModel.setTranslationLanguage(state.textField?.value ?: "")
                                    },
                                dismiss = str_cancel,
                            ),
                        )
                    },
                    isEnable = true,
                )
                SettingItem(
                    title = stringResource(Res.string.youtube_subtitle_language),
                    subtitle = youtubeSubtitleLanguage,
                    onClick = {
                        viewModel.setAlertData(
                            SettingAlertState(
                                title = str_youtube_subtitle_language,
                                textField =
                                    SettingAlertState.TextFieldData(
                                        label = str_youtube_subtitle_language,
                                        value = youtubeSubtitleLanguage,
                                        verifyCodeBlock = {
                                            (it.length == 2 && it.isTwoLetterCode()) to
                                                str_invalid_language_code
                                        },
                                    ),
                                message = str_youtube_subtitle_language_message,
                                confirm = str_change to { state ->
                                        viewModel.setYoutubeSubtitleLanguage(state.textField?.value ?: "")
                                    },
                                dismiss = str_cancel,
                            ),
                        )
                    },
                )
                SettingItem(
                    switch = (helpBuildLyricsDatabase to { viewModel.setHelpBuildLyricsDatabase(it) }),
                )
                SettingItem(
                    title = stringResource(Res.string.contributor_name),
                    subtitle = contributor.first.ifEmpty { stringResource(Res.string.anonymous) },
                    isEnable = helpBuildLyricsDatabase,
                    onClick = {
                        viewModel.setAlertData(
                            SettingAlertState(
                                title = str_contributor_name,
                                textField =
                                    SettingAlertState.TextFieldData(
                                        label = str_contributor_name,
                                        value = "",
                                    ),
                                message = "",
                                confirm =
                                    str_set to { state ->
                                        viewModel.setContributorName(state.textField?.value ?: "")
                                    },
                                dismiss = str_cancel,
                            ),
                        )
                    },
                )
                SettingItem(
                    title = stringResource(Res.string.contributor_email),
                    subtitle = contributor.second.ifEmpty { stringResource(Res.string.anonymous) },
                    isEnable = helpBuildLyricsDatabase,
                    onClick = {
                        viewModel.setAlertData(
                            SettingAlertState(
                                title = str_contributor_email,
                                textField =
                                    SettingAlertState.TextFieldData(
                                        label = str_contributor_email,
                                        value = "",
                                        verifyCodeBlock = {
                                            if (it.isNotEmpty()) {
                                                (it.contains("@")) to str_invalid
                                            } else {
                                                true to ""
                                            }
                                        },
                                    ),
                                message = "",
                                confirm =
                                    str_set to { state ->
                                        viewModel.setContributorEmail(state.textField?.value ?: "")
                                    },
                                dismiss = str_cancel,
                            ),
                        )
                    },
                )
            }
        }
        item(key = "AI") {
            SettingGroup(title = stringResource(Res.string.ai)) {
                SettingItem(
                    title = stringResource(Res.string.ai_provider),
                    subtitle =
                        when (aiProvider) {
                            DataStoreManager.AI_PROVIDER_OPENAI -> stringResource(Res.string.openai)
                            DataStoreManager.AI_PROVIDER_GEMINI -> stringResource(Res.string.gemini)
                            DataStoreManager.AI_PROVIDER_CUSTOM_OPENAI -> stringResource(Res.string.openai_api_compatible)
                            else -> stringResource(Res.string.unknown)
                        },
                    onClick = {
                        viewModel.setAlertData(
                            SettingAlertState(
                                title = str_ai_provider,
                                selectOne =
                                    SettingAlertState.SelectData(
                                        listSelect =
                                            listOf(
                                                (mainLyricsProvider == DataStoreManager.AI_PROVIDER_OPENAI) to
                                                    str_openai,
                                                (mainLyricsProvider == DataStoreManager.AI_PROVIDER_GEMINI) to
                                                    str_gemini,
                                                (mainLyricsProvider == DataStoreManager.AI_PROVIDER_CUSTOM_OPENAI) to
                                                    str_openai_api_compatible,
                                            ),
                                    ),
                                confirm = str_change to { state ->
                                        viewModel.setAIProvider(
                                            when (state.selectOne?.getSelected()) {
                                                str_openai -> DataStoreManager.AI_PROVIDER_OPENAI
                                                str_gemini -> DataStoreManager.AI_PROVIDER_GEMINI
                                                str_openai_api_compatible,
                                                -> DataStoreManager.AI_PROVIDER_CUSTOM_OPENAI

                                                else -> DataStoreManager.AI_PROVIDER_OPENAI
                                            },
                                        )
                                    },
                                dismiss = str_cancel,
                            ),
                        )
                    },
                )
                SettingItem(
                    title = stringResource(Res.string.ai_api_key),
                    subtitle = if (isHasApiKey) "XXXXXXXXXX" else "N/A",
                    onClick = {
                        viewModel.setAlertData(
                            SettingAlertState(
                                title = str_ai_api_key,
                                textField =
                                    SettingAlertState.TextFieldData(
                                        label = str_ai_api_key,
                                        value = "",
                                        verifyCodeBlock = {
                                            (it.isNotEmpty()) to str_invalid_api_key
                                        },
                                    ),
                                message = "",
                                confirm =
                                    str_set to { state ->
                                        viewModel.setAIApiKey(state.textField?.value ?: "")
                                    },
                                dismiss = str_cancel,
                            ),
                        )
                    },
                )
                SettingItem(
                    title = stringResource(Res.string.custom_ai_model_id),
                    subtitle = customModelId.ifEmpty { stringResource(Res.string.default_models) },
                    onClick = {
                        viewModel.setAlertData(
                            SettingAlertState(
                                title = str_custom_ai_model_id,
                                textField =
                                    SettingAlertState.TextFieldData(
                                        label = str_custom_ai_model_id,
                                        value = "",
                                        verifyCodeBlock = {
                                            (it.isNotEmpty() && !it.contains(" ")) to str_invalid
                                        },
                                    ),
                                message = str_custom_model_id_messages,
                                confirm =
                                    str_set to { state ->
                                        viewModel.setCustomModelId(state.textField?.value ?: "")
                                    },
                                dismiss = str_cancel,
                            ),
                        )
                    },
                )
                // Custom OpenAI Base URL - only show when Custom OpenAI is selected
                if (aiProvider == DataStoreManager.AI_PROVIDER_CUSTOM_OPENAI) {
                    val defaultBaseUrl = "https://api.openai.com/v1/"
                    val customBaseUrlTitle = stringResource(Res.string.custom_base_url)
                    val baseUrlLabel = stringResource(Res.string.base_url)
                    val invalidUrlFormat = stringResource(Res.string.invalid_url_format)
                    val customBaseUrlMessage = stringResource(Res.string.custom_base_url_message, defaultBaseUrl)
                    val customHeadersJsonTitle = stringResource(Res.string.custom_headers_json)
                    val headersJsonLabel = stringResource(Res.string.headers_json)
                    val invalidJsonFormat = stringResource(Res.string.invalid_json_format)
                    // The example JSON stays out of the translated text, so no translation can break it.
                    val customHeadersMessage =
                        stringResource(Res.string.custom_headers_message) + "\n{\"key1\":\"value1\",\"key2\":\"value2\"}"
                    SettingItem(
                        title = customBaseUrlTitle,
                        subtitle = customOpenAIBaseUrl.ifEmpty { defaultBaseUrl },
                        onClick = {
                            viewModel.setAlertData(
                                SettingAlertState(
                                    title = customBaseUrlTitle,
                                    textField =
                                        SettingAlertState.TextFieldData(
                                            label = baseUrlLabel,
                                            value = customOpenAIBaseUrl,
                                            verifyCodeBlock = {
                                                (it.isEmpty() || it.startsWith("http")) to invalidUrlFormat
                                            },
                                        ),
                                    message = customBaseUrlMessage,
                                    confirm =
                                        str_set to { state ->
                                            val baseUrl = state.textField?.value ?: ""
                                            viewModel.setCustomOpenAIBaseUrl(baseUrl)
                                            localNetworkPermission.requestIfNeeded(baseUrl)
                                        },
                                    dismiss = str_cancel,
                                ),
                            )
                        },
                    )
                    SettingItem(
                        title = stringResource(Res.string.custom_headers),
                        subtitle = stringResource(if (customOpenAIHeaders.isNotEmpty()) Res.string.configured else Res.string.not_set),
                        onClick = {
                            viewModel.setAlertData(
                                SettingAlertState(
                                    title = customHeadersJsonTitle,
                                    textField =
                                        SettingAlertState.TextFieldData(
                                            label = headersJsonLabel,
                                            value = customOpenAIHeaders,
                                            verifyCodeBlock = { input ->
                                                if (input.isEmpty()) {
                                                    true to null
                                                } else {
                                                    try {
                                                        // Simple validation: check if it looks like JSON
                                                        val trimmed = input.trim()
                                                        (trimmed.startsWith("{") && trimmed.endsWith("}")) to invalidJsonFormat
                                                    } catch (e: Exception) {
                                                        false to invalidJsonFormat
                                                    }
                                                }
                                            },
                                        ),
                                    message = customHeadersMessage,
                                    confirm =
                                        str_set to { state ->
                                            viewModel.setCustomOpenAIHeaders(state.textField?.value ?: "")
                                        },
                                    dismiss = str_cancel,
                                ),
                            )
                        },
                    )
                }
                SettingItem(
                    title = stringResource(Res.string.use_ai_translation),
                    subtitle = stringResource(Res.string.use_ai_translation_description),
                    switch = (useAITranslation to { viewModel.setAITranslation(it) }),
                    isEnable = isHasApiKey,
                )
            }
        }
        item(key = "spotify") {
            // Hidden entirely in "YouTube-only" mode — Spotify has nothing to offer without its source.
            if (!sharedViewModel.youtubeOnly.value) {
                SettingGroup(title = stringResource(Res.string.spotify)) {
                    SettingItem(
                        // The title follows the state: a row that still reads "Log in" while logged in
                        // gives no clue that tapping it signs you out.
                    title =
                        if (spotifyLoggedIn) {
                            stringResource(Res.string.log_out_from_spotify)
                        } else {
                            stringResource(Res.string.log_in_to_spotify)
                        },
                    subtitle =
                        if (spotifyLoggedIn) {
                            stringResource(Res.string.logged_in)
                        } else {
                            stringResource(Res.string.intro_login_to_spotify)
                        },
                    onClick = {
                        if (spotifyLoggedIn) {
                            viewModel.confirmLogOut(
                                confirmLabel = str_log_out_from_spotify,
                            ) { viewModel.setSpotifyLogIn(false) }
                        } else {
                            navController.navigate(SpotifyLoginDestination)
                        }
                    },
                )
                SettingItem(
                    title = stringResource(Res.string.enable_spotify_lyrics),
                    subtitle = stringResource(Res.string.spotify_lyrícs_info),
                    switch = (spotifyLyrics to { viewModel.setSpotifyLyrics(it) }),
                    isEnable = spotifyLoggedIn,
                )
                SettingItem(
                    title = stringResource(Res.string.enable_canvas),
                    subtitle = stringResource(Res.string.canvas_info),
                    switch = (spotifyCanvas to { viewModel.setSpotifyCanvas(it) }),
                    isEnable = spotifyLoggedIn,
                )
                // Sits with the canvas because it replaces it, but carries no isEnable: the two
                // rows above need a Spotify session and this one needs no account at all, so
                // gating it on spotifyLoggedIn would lock it away from the users it works for.
                SettingItem(
                    title = stringResource(Res.string.enable_animated_artwork),
                    subtitle = stringResource(Res.string.animated_artwork_info),
                    switch = (amAnimatedArtwork to { viewModel.setAMAnimatedArtwork(it) }),
                )
            }
        }
            }
        item(key = "discord") {
            SettingGroup(title = stringResource(Res.string.discord_integration)) {
                SettingItem(
                    title =
                        if (discordLoggedIn) {
                            stringResource(Res.string.log_out_from_discord)
                        } else {
                            stringResource(Res.string.log_in_to_discord)
                        },
                    subtitle =
                        if (discordLoggedIn) {
                            stringResource(Res.string.logged_in)
                        } else {
                            stringResource(Res.string.intro_login_to_discord)
                        },
                    onClick = {
                        if (discordLoggedIn) {
                            viewModel.confirmLogOut(
                                confirmLabel = str_log_out_from_discord,
                            ) { viewModel.logOutDiscord() }
                        } else {
                            navController.navigate(DiscordLoginDestination)
                        }
                    },
                )
                SettingItem(
                    title = stringResource(Res.string.enable_rich_presence),
                    subtitle = stringResource(Res.string.rich_presence_info),
                    switch = (richPresenceEnabled to { viewModel.setDiscordRichPresenceEnabled(it) }),
                    isEnable = discordLoggedIn,
                )
            }
        }
        // Hidden entirely when the build carries no Last.fm credentials — a FOSS build, or a full
        // build whose local.properties has no key.
        if (viewModel.lastfmAvailable) {
            item(key = "lastfm") {
                SettingGroup(title = stringResource(Res.string.lastfm_integration)) {
                    SettingItem(
                        title =
                            if (lastfmLoggedIn) {
                                stringResource(Res.string.log_out_from_lastfm)
                            } else {
                                stringResource(Res.string.log_in_to_lastfm)
                            },
                        subtitle =
                            if (lastfmLoggedIn) {
                                stringResource(Res.string.logged_in_as, lastfmUsername)
                            } else {
                                stringResource(Res.string.intro_login_to_lastfm)
                            },
                        onClick = {
                            if (lastfmLoggedIn) {
                                viewModel.confirmLogOut(
                                    confirmLabel = str_log_out_from_lastfm,
                                ) { viewModel.logOutLastfm() }
                            } else {
                                navController.navigate(LastfmLoginDestination)
                            }
                        },
                    )
                    SettingItem(
                        title = stringResource(Res.string.enable_scrobbling),
                        subtitle = stringResource(Res.string.scrobbling_info),
                        switch = (lastfmScrobbleEnabled to { viewModel.setLastfmScrobbleEnabled(it) }),
                        isEnable = lastfmLoggedIn,
                    )
                }
            }
        }
        item(key = "sponsor_block") {
            SettingGroup(
                title = stringResource(Res.string.sponsorBlock),
                // The project's note sits under the card, where a grouped list keeps its explanations.
                footer = {
                    val beforeUrl = stringResource(Res.string.sponsor_block_intro).substringBefore("https://sponsor.ajay.app/")
                    val afterUrl = stringResource(Res.string.sponsor_block_intro).substringAfter("https://sponsor.ajay.app/")
                    Text(
                        buildAnnotatedString {
                            append(beforeUrl)
                            withLink(
                                LinkAnnotation.Url(
                                    "https://sponsor.ajay.app/",
                                    TextLinkStyles(style = SpanStyle(color = MaterialTheme.colorScheme.primary)),
                                ),
                            ) {
                                append("https://sponsor.ajay.app/")
                            }
                            append(afterUrl)
                        },
                        style = typo().bodySmall,
                    )
                },
            ) {
                SettingItem(
                    title = stringResource(Res.string.enable_sponsor_block),
                    subtitle = stringResource(Res.string.skip_sponsor_part_of_video),
                    switch = (enableSponsorBlock to { viewModel.setSponsorBlockEnabled(it) }),
                )
                val listName =
                    SponsorBlockType.toList().map { it.displayString() }
                SettingItem(
                    title = stringResource(Res.string.categories_sponsor_block),
                    subtitle = stringResource(Res.string.what_segments_will_be_skipped),
                    onClick = {
                        viewModel.setAlertData(
                            SettingAlertState(
                                title = str_categories_sponsor_block,
                                multipleSelect =
                                    SettingAlertState.SelectData(
                                        listSelect =
                                            listName
                                                .mapIndexed { index, item ->
                                                    (
                                                        skipSegments?.contains(
                                                            SponsorBlockType.toList().getOrNull(index)?.value,
                                                        ) == true
                                                    ) to item
                                                }.also {
                                                    Logger.w("SettingScreen", "SettingAlertState: $skipSegments")
                                                    Logger.w("SettingScreen", "SettingAlertState: $it")
                                                },
                                    ),
                                confirm =
                                    str_save to { state ->
                                        viewModel.setSponsorBlockCategories(
                                            state.multipleSelect
                                                ?.getListSelected()
                                                ?.map { selected ->
                                                    listName.indexOf(selected)
                                                }?.mapNotNull { s ->
                                                    SponsorBlockType.toList().getOrNull(s).let {
                                                        it?.value
                                                    }
                                                }?.toCollection(ArrayList()) ?: arrayListOf(),
                                        )
                                    },
                                dismiss = str_cancel,
                            ),
                        )
                    },
                    isEnable = enableSponsorBlock,
                )
            }
        }
        if (getPlatform() == Platform.Android) {
            item(key = "storage") {
                SettingGroup(title = stringResource(Res.string.storage)) {
                    SettingItem(
                        title = stringResource(Res.string.player_cache),
                        subtitle = "${playerCache.bytesToMB()} MB",
                        onClick = {
                            viewModel.setBasicAlertData(
                                SettingBasicAlertState(
                                    title = str_clear_player_cache,
                                    message = null,
                                    confirm =
                                        str_clear to {
                                            viewModel.clearPlayerCache()
                                        },
                                    dismiss = str_cancel,
                                ),
                            )
                        },
                    )
                    SettingItem(
                        title = stringResource(Res.string.downloaded_cache),
                        subtitle = "${downloadedCache.bytesToMB()} MB",
                        onClick = {
                            viewModel.setBasicAlertData(
                                SettingBasicAlertState(
                                    title = str_clear_downloaded_cache,
                                    message = null,
                                    confirm =
                                        str_clear to {
                                            viewModel.clearDownloadedCache()
                                        },
                                    dismiss = str_cancel,
                                ),
                            )
                        },
                    )
                    SettingItem(
                        title = stringResource(Res.string.thumbnail_cache),
                        subtitle = "${thumbnailCache.bytesToMB()} MB",
                        onClick = {
                            viewModel.setBasicAlertData(
                                SettingBasicAlertState(
                                    title = str_clear_thumbnail_cache,
                                    message = null,
                                    confirm =
                                        str_clear to {
                                            viewModel.clearThumbnailCache(platformContext)
                                        },
                                    dismiss = str_cancel,
                                ),
                            )
                        },
                    )
                    SettingItem(
                        title = stringResource(Res.string.spotify_canvas_cache),
                        subtitle = "${canvasCache.bytesToMB()} MB",
                        onClick = {
                            viewModel.setBasicAlertData(
                                SettingBasicAlertState(
                                    title = str_clear_canvas_cache,
                                    message = null,
                                    confirm =
                                        str_clear to {
                                            viewModel.clearCanvasCache()
                                        },
                                    dismiss = str_cancel,
                                ),
                            )
                        },
                    )
                    SettingItem(
                        title = stringResource(Res.string.limit_player_cache),
                        subtitle = LIMIT_CACHE_SIZE.getItemFromData(limitPlayerCache).toString(),
                        onClick = {
                            viewModel.setAlertData(
                                SettingAlertState(
                                    title = str_limit_player_cache,
                                    selectOne =
                                        SettingAlertState.SelectData(
                                            listSelect =
                                                LIMIT_CACHE_SIZE.items.map { item ->
                                                    (item == LIMIT_CACHE_SIZE.getItemFromData(limitPlayerCache)) to item.toString()
                                                },
                                        ),
                                    confirm = str_change to { state ->
                                            viewModel.setPlayerCacheLimit(
                                                LIMIT_CACHE_SIZE.getDataFromItem(state.selectOne?.getSelected()),
                                            )
                                        },
                                    dismiss = str_cancel,
                                ),
                            )
                        },
                    )
                    Box(
                        Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 16.dp,
                        ),
                    ) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(0.dp),
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .onGloballyPositioned { layoutCoordinates ->
                                        with(density) {
                                            width =
                                                layoutCoordinates.size.width
                                                    .toDp()
                                                    .value
                                                    .toInt()
                                        }
                                    },
                        ) {
                            item {
                                Box(
                                    modifier =
                                        Modifier
                                            .width(
                                                (fraction.otherApp * width).dp,
                                            ).background(
                                                md_theme_dark_primary,
                                            ).fillMaxHeight(),
                                )
                            }
                            item {
                                Box(
                                    modifier =
                                        Modifier
                                            .width(
                                                (fraction.downloadCache * width).dp,
                                            ).background(
                                                Color(0xD540FF17),
                                            ).fillMaxHeight(),
                                )
                            }
                            item {
                                Box(
                                    modifier =
                                        Modifier
                                            .width(
                                                (fraction.playerCache * width).dp,
                                            ).background(
                                                Color(0xD5FFFF00),
                                            ).fillMaxHeight(),
                                )
                            }
                            item {
                                Box(
                                    modifier =
                                        Modifier
                                            .width(
                                                (fraction.canvasCache * width).dp,
                                            ).background(
                                                Color.Cyan,
                                            ).fillMaxHeight(),
                                )
                            }
                            item {
                                Box(
                                    modifier =
                                        Modifier
                                            .width(
                                                (fraction.thumbCache * width).dp,
                                            ).background(
                                                Color.Magenta,
                                            ).fillMaxHeight(),
                                )
                            }
                            item {
                                Box(
                                    modifier =
                                        Modifier
                                            .width(
                                                (fraction.appDatabase * width).dp,
                                            ).background(
                                                Color.White,
                                            ),
                                )
                            }
                            item {
                                Box(
                                    modifier =
                                        Modifier
                                            .width(
                                                (fraction.freeSpace * width).dp,
                                            ).background(
                                                Color.DarkGray,
                                            ).fillMaxHeight(),
                                )
                            }
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    ) {
                        Box(
                            Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(
                                    md_theme_dark_primary,
                                ),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(text = stringResource(Res.string.other_app), style = typo().bodySmall)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    ) {
                        Box(
                            Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(
                                    Color.Green,
                                ),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(text = stringResource(Res.string.downloaded_cache), style = typo().bodySmall)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    ) {
                        Box(
                            Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(
                                    Color.Yellow,
                                ),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(text = stringResource(Res.string.player_cache), style = typo().bodySmall)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    ) {
                        Box(
                            Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(
                                    Color.Cyan,
                                ),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(text = stringResource(Res.string.spotify_canvas_cache), style = typo().bodySmall)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    ) {
                        Box(
                            Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(
                                    Color.Magenta,
                                ),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(text = stringResource(Res.string.thumbnail_cache), style = typo().bodySmall)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    ) {
                        Box(
                            Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(
                                    Color.White,
                                ),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(text = stringResource(Res.string.database), style = typo().bodySmall)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    ) {
                        Box(
                            Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(
                                    Color.LightGray,
                                ),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(text = stringResource(Res.string.free_space), style = typo().bodySmall)
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
        item(key = "backup") {
            SettingGroup(
                title = stringResource(Res.string.backup),
                footer = {
                    val beforeUrl = stringResource(Res.string.import_data_intro).substringBefore("https://www.github.com/mosske/Oblivion-Nocturne/tools")
                    val afterUrl = stringResource(Res.string.import_data_intro).substringAfter("https://www.github.com/mosske/Oblivion-Nocturne/tools")
                    Text(
                        buildAnnotatedString {
                            append(beforeUrl)
                            withLink(
                                LinkAnnotation.Url(
                                    "https://www.github.com/mosske/Oblivion-Nocturne/tools",
                                    TextLinkStyles(style = SpanStyle(color = MaterialTheme.colorScheme.primary)),
                                ),
                            ) {
                                append("https://www.github.com/mosske/Oblivion-Nocturne/tools")
                            }
                            append(afterUrl)
                        },
                        style = typo().bodySmall,
                    )
                },
            ) {
                SettingItem(
                    title = stringResource(Res.string.backup_downloaded),
                    subtitle = stringResource(Res.string.backup_downloaded_description),
                    switch = (backupDownloaded to { viewModel.setBackupDownloaded(it) }),
                )
                // Auto Backup (Android only)
                if (getPlatform() == Platform.Android) {
                    SettingItem(
                        title = stringResource(Res.string.auto_backup),
                        subtitle = stringResource(Res.string.auto_backup_description),
                        switch = (autoBackupEnabled to { viewModel.setAutoBackupEnabled(it) }),
                    )
                    AnimatedVisibility(visible = autoBackupEnabled) {
                        Column {
                            SettingItem(
                                title = stringResource(Res.string.backup_frequency),
                                subtitle =
                                    when (autoBackupFrequency) {
                                        DataStoreManager.AUTO_BACKUP_FREQUENCY_DAILY -> stringResource(Res.string.daily)
                                        DataStoreManager.AUTO_BACKUP_FREQUENCY_WEEKLY -> stringResource(Res.string.weekly)
                                        DataStoreManager.AUTO_BACKUP_FREQUENCY_MONTHLY -> stringResource(Res.string.monthly)
                                        else -> stringResource(Res.string.daily)
                                    },
                                onClick = {
                                    viewModel.setAlertData(
                                        SettingAlertState(
                                            title = str_backup_frequency,
                                            selectOne =
                                                SettingAlertState.SelectData(
                                                    listSelect =
                                                        listOf(
                                                            (autoBackupFrequency == DataStoreManager.AUTO_BACKUP_FREQUENCY_DAILY) to
                                                                str_daily,
                                                            (autoBackupFrequency == DataStoreManager.AUTO_BACKUP_FREQUENCY_WEEKLY) to
                                                                str_weekly,
                                                            (autoBackupFrequency == DataStoreManager.AUTO_BACKUP_FREQUENCY_MONTHLY) to
                                                                str_monthly,
                                                        ),
                                                ),
                                            confirm = str_change to { state ->
                                                    val frequency =
                                                        when (state.selectOne?.getSelected()) {
                                                            str_daily,
                                                            -> DataStoreManager.AUTO_BACKUP_FREQUENCY_DAILY
                                                            str_weekly,
                                                            -> DataStoreManager.AUTO_BACKUP_FREQUENCY_WEEKLY
                                                            str_monthly,
                                                            -> DataStoreManager.AUTO_BACKUP_FREQUENCY_MONTHLY
                                                            else -> DataStoreManager.AUTO_BACKUP_FREQUENCY_DAILY
                                                        }
                                                    viewModel.setAutoBackupFrequency(frequency)
                                                },
                                            dismiss = str_cancel,
                                        ),
                                    )
                                },
                            )
                            SettingItem(
                                title = stringResource(Res.string.keep_backups),
                                subtitle = stringResource(Res.string.keep_backups_format, "$autoBackupMaxFiles"),
                                onClick = {
                                    viewModel.setAlertData(
                                        SettingAlertState(
                                            title = str_keep_backups,
                                            selectOne =
                                                SettingAlertState.SelectData(
                                                    listSelect =
                                                        listOf(
                                                            (autoBackupMaxFiles == 3) to "3",
                                                            (autoBackupMaxFiles == 5) to "5",
                                                            (autoBackupMaxFiles == 10) to "10",
                                                            (autoBackupMaxFiles == 15) to "15",
                                                        ),
                                                ),
                                            confirm = str_change to { state ->
                                                    val maxFiles = state.selectOne?.getSelected()?.toIntOrNull() ?: 5
                                                    viewModel.setAutoBackupMaxFiles(maxFiles)
                                                },
                                            dismiss = str_cancel,
                                        ),
                                    )
                                },
                            )
                            SettingItem(
                                title = stringResource(Res.string.last_backup),
                                subtitle =
                                    if (autoBackupLastTime == 0L) {
                                        stringResource(Res.string.never)
                                    } else {
                                Instant.fromEpochMilliseconds(autoBackupLastTime).toString()
                                    },
                            )
                        }
                    }
                }
                SettingItem(
                    title = stringResource(Res.string.backup),
                    subtitle = stringResource(Res.string.save_all_your_playlist_data),
                    onClick = {
                        coroutineScope.launch {
                            backupLauncher.launch()
                        }
                    },
                )
                SettingItem(
                    title = stringResource(Res.string.restore_your_data),
                    subtitle = stringResource(Res.string.restore_your_saved_data),
                    onClick = {
                        coroutineScope.launch {
                            restoreLauncher.launch()
                        }
                    },
                )
                SettingItem(
                    title = stringResource(Res.string.import_data),
                    subtitle = stringResource(Res.string.import_playlists_from_other_apps),
                    onClick = {
                        coroutineScope.launch {
                            importLauncher.launch()
                        }
                    },
                )
            }
        }
        item(key = "about_us") {
            SettingGroup(title = stringResource(Res.string.about_us)) {
                SettingItem(
                    title = stringResource(Res.string.version),
                    subtitle = stringResource(Res.string.version_format, VersionManager.getVersionName()),
                    onClick = {
                        navController.navigate(CreditDestination)
                    },
                )
                SettingItem(
                    title = stringResource(Res.string.auto_check_for_update),
                    subtitle = stringResource(Res.string.auto_check_for_update_description),
                    switch = (autoCheckUpdate to { viewModel.setAutoCheckUpdate(it) }),
                )
                val updateChannelTitle = stringResource(Res.string.update_channel)
                    SettingItem(
                    title = updateChannelTitle,
                    subtitle = "OblivionNocturne GitHub Release",
                    onClick = {
                        viewModel.setAlertData(
                            SettingAlertState(
                                title = updateChannelTitle,
                                selectOne =
                                    SettingAlertState.SelectData(
                                        listSelect =
                                            listOf(
                                                true to "OblivionNocturne GitHub Release",
                                            ),
                                    ),
                                confirm = str_change to { state ->
                                        viewModel.setUpdateChannel(
                                            when (state.selectOne?.getSelected()) {
                                                "OblivionNocturne GitHub Release" -> DataStoreManager.GITHUB
                                                else -> DataStoreManager.GITHUB
                                            },
                                        )
                                    },
                                dismiss = str_cancel,
                            ),
                        )
                    },
                )
                SettingItem(
                    title = stringResource(Res.string.check_for_update),
                    subtitle = checkForUpdateSubtitle,
                    onClick = {
                        sharedViewModel.checkForUpdate()
                    },
                )
                SettingItem(
                    title = stringResource(Res.string.author),
                    subtitle = stringResource(Res.string.developer_blog_tagline),
                    onClick = {
                        uriHandler.openUri("https://github.com/mosske/Oblivion-Nocturne")
                    },
                )
                if (getPlatform() == Platform.Android) {
                    SettingItem(
                        title = stringResource(Res.string.blog_notification_title),
                        subtitle = stringResource(Res.string.blog_notification_description),
                        switch = (blogNotificationEnabled to { viewModel.setBlogNotificationEnabled(it) }),
                    )
                }
            }
        }
        item(key = "developer_option") {
            SettingGroup(title = stringResource(Res.string.developer_option)) {
                SettingItem(
                    title = stringResource(Res.string.app_log),
                    subtitle = stringResource(Res.string.app_log_description),
                    onClick = {
                        navController.navigate(AppLogDestination)
                    },
                )
            }
        }
        item(key = "end") {
            EndOfPage()
        }
    }
    importState?.let { progress ->
        ImportProgressDialog(
            progress = progress,
            onDismiss = importViewModel::dismiss,
        )
    }
    val showLoadingDialog by viewModel.showLoadingDialog.collectAsStateWithLifecycle()
    if (showLoadingDialog.first) {
        LoadingDialog(
            true,
            showLoadingDialog.second,
        )
    }
    val basisAlertData by viewModel.basicAlertData.collectAsStateWithLifecycle()
    if (basisAlertData != null) {
        val alertBasicState = basisAlertData ?: return
        AlertDialog(
            onDismissRequest = { viewModel.setBasicAlertData(null) },
            title = {
                Text(
                    text = alertBasicState.title,
                    style = typo().titleSmall,
                )
            },
            text = {
                if (alertBasicState.message != null) {
                    Text(text = alertBasicState.message)
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        alertBasicState.confirm.second.invoke()
                        viewModel.setBasicAlertData(null)
                    },
                ) {
                    Text(text = alertBasicState.confirm.first)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.setBasicAlertData(null)
                    },
                ) {
                    Text(text = alertBasicState.dismiss)
                }
            },
        )
    }
    if (showColorPickerDialog) {
        // The primary colour of each music app, as its brand publishes it. Names are brand names and
        // are not translated.
        val presetColors =
            listOf(
                "FF1ED760" to "Spotify",
                "FFFF5500" to "SoundCloud",
                "FFFA243C" to "Apple Music",
                "FFFF0000" to "YouTube Music",
                "FFA238FF" to "Deezer",
                "FF3668FF" to "Pandora",
                "FFFFA200" to "Audiomack",
                "FF1DA0C3" to "Bandcamp",
            )
        var pendingHex by rememberSaveable { mutableStateOf(customThemeColorHex.takeLast(6)) }
        val parsedColor = parseThemeColorHex(pendingHex)
        AlertDialog(
            onDismissRequest = { showColorPickerDialog = false },
            title = { Text(text = stringResource(Res.string.custom_color), style = typo().titleSmall) },
            text = {
                Column {
                    // Four across: at five, a cell is too narrow for "SoundCloud" under its swatch.
                    presetColors.chunked(4).forEach { rowColors ->
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            rowColors.forEach { (hex, name) ->
                                val color = parseThemeColorHex(hex) ?: Color.Gray
                                val isSelected = pendingHex.equals(hex.takeLast(6), ignoreCase = true)
                                Column(
                                    modifier =
                                        Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { pendingHex = hex.takeLast(6) }
                                            .padding(vertical = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Box(
                                        modifier =
                                            Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                                .background(color)
                                                .border(
                                                    width = if (isSelected) 3.dp else 0.dp,
                                                    color = if (isSelected) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                                    shape = CircleShape,
                                                ),
                                    )
                                    Spacer(Modifier.height(6.dp))
                                    Text(
                                        text = name,
                                        style = typo().bodySmall,
                                        color =
                                            if (isSelected) {
                                                MaterialTheme.colorScheme.onSurface
                                            } else {
                                                MaterialTheme.colorScheme.onSurfaceVariant
                                            },
                                        textAlign = TextAlign.Center,
                                        maxLines = 2,
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    TextField(
                        value = pendingHex,
                        onValueChange = { pendingHex = it.removePrefix("#").take(8).uppercase() },
                        label = { Text("HEX") },
                        prefix = { Text("#") },
                        singleLine = true,
                        isError = parsedColor == null,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = parsedColor != null,
                    onClick = {
                        parsedColor?.let {
                            val argb = "FF${pendingHex.takeLast(6).uppercase()}"
                            sharedViewModel.setCustomThemeColor(argb)
                            sharedViewModel.setThemeColorSource(DataStoreManager.THEME_COLOR_CUSTOM)
                        }
                        showColorPickerDialog = false
                    },
                ) { Text(text = stringResource(Res.string.change)) }
            },
            dismissButton = {
                TextButton(onClick = { showColorPickerDialog = false }) {
                    Text(text = str_cancel)
                }
            },
        )
    }
    if (showLoginSyncDialog) {
        LoginSyncDialog(onDismiss = { showLoginSyncDialog = false })
    }
    if (showYouTubeAccountDialog) {
        BasicAlertDialog(
            onDismissRequest = { },
            modifier = Modifier.wrapContentSize(),
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                tonalElevation = AlertDialogDefaults.TonalElevation,
                shadowElevation = 1.dp,
            ) {
                val googleAccounts by viewModel.googleAccounts.collectAsStateWithLifecycle(
                    minActiveState = Lifecycle.State.RESUMED,
                )
                LaunchedEffect(googleAccounts) {
                    Logger.w(
                        "SettingScreen",
                        "LaunchedEffect: ${
                            googleAccounts.data?.map {
                                it.name to it.isUsed
                            }
                        }",
                    )
                }
                LazyColumn(modifier = Modifier.padding(8.dp)) {
                    item {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                        ) {
                            IconButton(
                                onClick = { showYouTubeAccountDialog = false },
                                colors =
                                    IconButtonDefaults.iconButtonColors().copy(
                                        contentColor = MaterialTheme.colorScheme.onSurface,
                                    ),
                                modifier =
                                    Modifier
                                        .align(Alignment.CenterStart)
                                        .fillMaxHeight(),
                            ) {
                                Icon(SimpIcons.Close, null, tint = MaterialTheme.colorScheme.onSurface)
                            }
                            Text(
                                stringResource(Res.string.youtube_account),
                                style = typo().titleMedium,
                                modifier =
                                    Modifier
                                        .align(Alignment.Center)
                                        .wrapContentHeight(align = Alignment.CenterVertically)
                                        .wrapContentWidth(),
                            )
                        }
                    }
                    if (googleAccounts is LocalResource.Success) {
                        val data = googleAccounts.data
                        if (data.isNullOrEmpty()) {
                            item {
                                Text(
                                    stringResource(Res.string.no_account),
                                    style = typo().bodyMedium,
                                    textAlign = TextAlign.Center,
                                    modifier =
                                        Modifier
                                            .padding(12.dp)
                                            .fillMaxWidth(),
                                )
                            }
                        } else {
                            items(data) {
                                Row(
                                    modifier =
                                        Modifier
                                            .padding(vertical = 8.dp)
                                            .clickable {
                                                viewModel.setUsedAccount(it)
                                            },
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Spacer(Modifier.width(24.dp))
                                    AsyncImage(
                                        model =
                                            ImageRequest
                                                .Builder(LocalPlatformContext.current)
                                                .data(it.thumbnailUrl)
                                                .crossfade(550)
                                                .build(),
                                        placeholder = painterResource(Res.drawable.oblivion_owl),
                                        contentDescription = it.name,
                                        modifier =
                                            Modifier
                                                .size(48.dp)
                                                .clip(CircleShape),
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Column(Modifier.weight(1f)) {
                                        Text(it.name, style = typo().labelMedium, color = MaterialTheme.colorScheme.onBackground)
                                        Text(it.email, style = typo().bodySmall)
                                    }
                                    Spacer(Modifier.width(12.dp))
                                    AnimatedVisibility(it.isUsed) {
                                        Text(
                                            stringResource(Res.string.signed_in),
                                            style = typo().bodySmall,
                                            maxLines = 2,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.widthIn(0.dp, 64.dp),
                                        )
                                    }
                                    Spacer(Modifier.width(24.dp))
                                }
                            }
                        }
                    } else {
                        item {
                            CenterLoadingBox(
                                Modifier
                                    .fillMaxWidth()
                                    .height(80.dp),
                            )
                        }
                    }
                    item {
                        Column {
                            ActionButton(
                                icon = SimpIcons.Person,
                                text = Res.string.guest,
                            ) {
                                viewModel.setUsedAccount(null)
                                showYouTubeAccountDialog = false
                            }
                            ActionButton(
                                icon = SimpIcons.Close,
                                text = Res.string.log_out,
                            ) {
                                viewModel.setBasicAlertData(
                                    SettingBasicAlertState(
                                        title = str_warning,
                                        message = str_log_out_warning,
                                        confirm =
                                            str_log_out to {
                                                viewModel.logOutAllYouTube()
                                                showYouTubeAccountDialog = false
                                            },
                                        dismiss = str_cancel,
                                    ),
                                )
                            }
                            ActionButton(
                                icon = SimpIcons.PlaylistAdd,
                                text = Res.string.add_an_account,
                            ) {
                                showYouTubeAccountDialog = false
                                navController.navigate(LoginDestination)
                            }
                        }
                    }
                }
            }
        }
    }
    val alertData by viewModel.alertData.collectAsStateWithLifecycle()
    if (alertData != null) {
        val alertState = alertData ?: return
        // AlertDialog
        AlertDialog(
            onDismissRequest = { viewModel.setAlertData(null) },
            title = {
                Text(
                    text = alertState.title,
                    style = typo().titleSmall,
                )
            },
            text = {
                if (alertState.message != null) {
                    Column {
                        Text(text = alertState.message)
                        if (alertState.textField != null) {
                            val verify =
                                alertState.textField.verifyCodeBlock?.invoke(
                                    alertState.textField.value,
                                ) ?: (true to null)
                            TextField(
                                value = alertState.textField.value,
                                onValueChange = {
                                    viewModel.setAlertData(
                                        alertState.copy(
                                            textField =
                                                alertState.textField.copy(
                                                    value = it,
                                                ),
                                        ),
                                    )
                                },
                                isError = !verify.first,
                                label = { Text(text = alertState.textField.label) },
                                supportingText = {
                                    if (!verify.first) {
                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = verify.second ?: "",
                                            color = MaterialTheme.colorScheme.error,
                                        )
                                    }
                                },
                                trailingIcon = {
                                    if (!verify.first) {
                                        SimpIcons.Error
                                    }
                                },
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            vertical = 6.dp,
                                        ),
                            )
                        }
                    }
                } else if (alertState.selectOne != null) {
                    LazyColumn(
                        Modifier
                            .padding(vertical = 6.dp)
                            .heightIn(0.dp, 500.dp),
                    ) {
                        items(alertState.selectOne.listSelect) { item ->
                            val onSelect = {
                                viewModel.setAlertData(
                                    alertState.copy(
                                        selectOne =
                                            alertState.selectOne.copy(
                                                listSelect =
                                                    alertState.selectOne.listSelect.toMutableList().map {
                                                        if (it == item) {
                                                            true to it.second
                                                        } else {
                                                            false to it.second
                                                        }
                                                    },
                                            ),
                                    ),
                                )
                            }
                            Row(
                                Modifier
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        onSelect.invoke()
                                    }.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                RadioButton(
                                    selected = item.first,
                                    onClick = {
                                        onSelect.invoke()
                                    },
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = item.second,
                                    style = typo().bodyMedium,
                                    maxLines = 1,
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight(align = Alignment.CenterVertically)
                                            .basicMarquee(
                                                iterations = Int.MAX_VALUE,
                                                animationMode = MarqueeAnimationMode.Immediately,
                                            ).focusable(),
                                )
                            }
                        }
                    }
                } else if (alertState.multipleSelect != null) {
                    LazyColumn(
                        Modifier.padding(vertical = 6.dp),
                    ) {
                        items(alertState.multipleSelect.listSelect) { item ->
                            val onCheck = {
                                viewModel.setAlertData(
                                    alertState.copy(
                                        multipleSelect =
                                            alertState.multipleSelect.copy(
                                                listSelect =
                                                    alertState.multipleSelect.listSelect.toMutableList().map {
                                                        if (it == item) {
                                                            !it.first to it.second
                                                        } else {
                                                            it
                                                        }
                                                    },
                                            ),
                                    ),
                                )
                            }
                            Row(
                                Modifier
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        onCheck.invoke()
                                    }.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Checkbox(
                                    checked = item.first,
                                    onCheckedChange = {
                                        onCheck.invoke()
                                    },
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(text = item.second, style = typo().bodyMedium, maxLines = 1)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        alertState.confirm.second.invoke(alertState)
                        viewModel.setAlertData(null)
                    },
                    enabled =
                        if (alertState.textField?.verifyCodeBlock != null) {
                            alertState.textField.verifyCodeBlock
                                .invoke(
                                    alertState.textField.value,
                                ).first
                        } else {
                            true
                        },
                ) {
                    Text(text = alertState.confirm.first)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.setAlertData(null)
                    },
                ) {
                    Text(text = alertState.dismiss)
                }
            },
        )
    }

    if (showThirdPartyLibraries) {
        val libraries by produceLibraries {
            Res.readBytes("files/aboutlibraries.json").decodeToString()
        }
        val lazyListState = rememberLazyListState()
        val canScrollBackward by remember {
            derivedStateOf {
                lazyListState.canScrollBackward
            }
        }
        val sheetState =
            rememberModalBottomSheetState(
                skipPartiallyExpanded = true,
                confirmValueChange = {
                    !canScrollBackward
                },
            )
        val coroutineScope = rememberCoroutineScope()
        ModalBottomSheet(
            modifier =
                Modifier
                    .fillMaxHeight(),
            onDismissRequest = {
                showThirdPartyLibraries = false
            },
            containerColor = MaterialTheme.colorScheme.surface,
            dragHandle = {},
            scrimColor = Color.Black.copy(alpha = .5f),
            sheetState = sheetState,
            contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
            shape = RectangleShape,
        ) {
            // Capture theme colors here: the ChipColors getters below run outside composition.
            val surfaceContainerHighestColor = MaterialTheme.colorScheme.surfaceContainerHighest
            val onSurfaceColor = MaterialTheme.colorScheme.onSurface
            LibrariesContainer(
                libraries?.copy(
                    libraries =
                        libraries
                            ?.libraries
                            ?.distinctBy {
                                it.name
                            }?.toImmutableList() ?: emptyList<Library>().toImmutableList(),
                ),
                Modifier.fillMaxSize(),
                lazyListState = lazyListState,
                contentPadding = innerPadding,
                colors =
                    LibraryDefaults.libraryColors(
                        licenseChipColors =
                            object : ChipColors {
                                override val containerColor: Color
                                    get() = surfaceContainerHighestColor
                                override val contentColor: Color
                                    get() = onSurfaceColor
                            },
                    ),
                header = {
                    item {
                        TopAppBar(
                            windowInsets = WindowInsets(0, 0, 0, 0),
                            title = {
                                Text(
                                    text = stringResource(Res.string.third_party_libraries),
                                    style = typo().titleMedium,
                                )
                            },
                            navigationIcon = {
                                Box(Modifier.padding(horizontal = 5.dp)) {
                                    RippleIconButton(
                                        SimpIcons.ArrowBackIosNew,
                                        Modifier
                                            .size(32.dp),
                                        true,
                                        tint = MaterialTheme.colorScheme.onSurface,
                                    ) {
                                        coroutineScope.launch {
                                            sheetState.hide()
                                            showThirdPartyLibraries = false
                                        }
                                    }
                                }
                            },
                        )
                    }
                },
            )
        }
    }

    // The pinned copy of the toolbar, frosted: the frost waits for the Devices group to go under it,
    // because an always-on frost dimmed the glow behind the bar into a black band, which is exactly
    // where the glow carries its colour.
    // Captured outside the haze scope — HazeEffectScope is not composable (same move as
    // AlbumScreen's mutedPaletteBg capture).
    val settingBarTint = MaterialTheme.colorScheme.background
    AnimatedVisibility(
        visible = showPinnedBar,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically(),
    ) {
        SettingTopBar(
            onBack = { navController.navigateUp() },
            // The house recipe from AlbumScreen's bars, thinned: ultraThin's built-in tint stacked
            // on this page's dark ground read as a solid lid. 0.3 keeps the blur doing the work and
            // the tint only settling legibility.
            modifier = Modifier.hazeBlur(HazeInput.Sources(hazeState), barBlurStyle(settingBarTint, 0.3f)),
        )
    }
}

/**
 * The page's toolbar. Drawn twice, the way Playlist and Artist draw theirs: once inside the list,
 * where it scrolls away with the content, and once pinned above it.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingTopBar(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(Res.string.settings),
                style = typo().titleMedium,
            )
        },
        navigationIcon = {
            Box(Modifier.padding(horizontal = 5.dp)) {
                RippleIconButton(
                    SimpIcons.ArrowBackIosNew,
                    Modifier.size(32.dp),
                    true,
                    tint = MaterialTheme.colorScheme.onSurface,
                ) {
                    onBack()
                }
            }
        },
        windowInsets = windowInsets,
        modifier = modifier,
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
            ),
    )
}

/**
 * Progress and outcome of a playlist import.
 *
 * Only dismissible once the import has finished — cancelling mid-write would leave the database
 * half-populated with no way to tell the user which half.
 */
@Composable
private fun ImportProgressDialog(
    progress: ImportProgress,
    onDismiss: () -> Unit,
) {
    val finished = progress is ImportProgress.Success || progress is ImportProgress.Error
    AlertDialog(
        onDismissRequest = { if (finished) onDismiss() },
        properties =
            DialogProperties(
                dismissOnBackPress = finished,
                dismissOnClickOutside = finished,
            ),
        title = {
            Text(
                text =
                    stringResource(
                        if (progress is ImportProgress.Error) Res.string.import_failed else Res.string.import_data,
                    ),
                style = typo().titleSmall,
            )
        },
        text = {
            Column {
                when (progress) {
                    is ImportProgress.Preparing -> {
                        Text(
                            text = stringResource(Res.string.import_reading_file),
                            style = typo().bodyMedium,
                        )
                        Spacer(Modifier.height(12.dp))
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }

                    is ImportProgress.Importing -> {
                        Text(
                            text = stringResource(Res.string.import_progress_songs, progress.processed, progress.total),
                            style = typo().bodyMedium,
                        )
                        Spacer(Modifier.height(12.dp))
                        LinearProgressIndicator(
                            progress = {
                                if (progress.total > 0) progress.processed.toFloat() / progress.total else 0f
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    is ImportProgress.Success -> {
                        Text(
                            text =
                                stringResource(
                                    Res.string.import_result,
                                    progress.result.playlistsCreated,
                                    progress.result.songsImported,
                                ),
                            style = typo().bodyMedium,
                        )
                        if (progress.result.skippedEntries > 0) {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = stringResource(Res.string.import_result_skipped, progress.result.skippedEntries),
                                style = typo().bodySmall,
                            )
                        }
                    }

                    is ImportProgress.Error -> {
                        Text(
                            text = progress.message,
                            style = typo().bodyMedium,
                        )
                    }
                }
            }
        },
        confirmButton = {
            if (finished) {
                TextButton(onClick = onDismiss) {
                    Text(text = str_ok)
                }
            }
        },
    )
}
