package com.oblivion.nocturne.data.repository

import com.oblivion.nocturne.domain.data.model.taste.TasteInput
import com.oblivion.nocturne.domain.data.model.taste.TasteProfile
import com.oblivion.nocturne.domain.extension.now
import com.oblivion.nocturne.domain.manager.DataStoreManager
import com.oblivion.nocturne.domain.repository.TasteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import org.oblivion.nocturne.aiservice.AiClient

internal class TasteRepositoryImpl(
    private val aiClient: AiClient,
    private val dataStoreManager: DataStoreManager,
) : TasteRepository {
    private val json = Json { ignoreUnknownKeys = true }

    // A stored reading that no longer decodes (a field a later version renamed) counts as none, so
    // the card offers a fresh one instead of failing.
    override val tasteProfile: Flow<TasteProfile?> =
        dataStoreManager.tasteProfile.map { stored ->
            stored?.let { runCatching { json.decodeFromString<TasteProfile>(it) }.getOrNull() }
        }

    override suspend fun describeTaste(
        input: TasteInput,
        artistImages: List<String>,
    ): Result<TasteProfile> =
        aiClient.describeTaste(input).map { reading ->
            TasteProfile(
                headline = reading.headline,
                summary = reading.summary,
                artistImages = artistImages,
                generatedAt = now(),
            ).also { dataStoreManager.setTasteProfile(json.encodeToString(it)) }
        }
}
