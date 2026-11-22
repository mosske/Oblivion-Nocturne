package com.oblivion.nocturne.data.repository

import com.oblivion.nocturne.domain.data.model.update.UpdateData
import com.oblivion.nocturne.domain.repository.UpdateRepository
import com.oblivion.nocturne.domain.utils.LocalResource
import com.oblivion.nocturne.kotlinytmusicscraper.models.oblivion.nocturne.GithubResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

class UpdateRepositoryImpl : UpdateRepository {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
    }

    override fun checkForGithubReleaseUpdate(): Flow<LocalResource<UpdateData>> = flow {
        emit(LocalResource.Loading())
        try {
            val response: GithubResponse = httpClient
                .get("https://api.github.com/repos/mosske/Oblivion-Nocturne/releases/latest")
                .body()
            val apkAsset = response.assets.firstOrNull { it.name.endsWith(".apk", ignoreCase = true) }
            val updateData = UpdateData(
                tagName = response.tagName,
                releaseNotes = response.body,
                downloadUrl = apkAsset?.browserDownloadUrl,
                publishedAt = response.publishedAt,
            )
            emit(LocalResource.Success(updateData))
        } catch (e: Exception) {
            emit(LocalResource.Error(e.message ?: "Failed to check for updates"))
        }
    }
}
