package com.oblivion.nocturne.data.repository

import com.oblivion.nocturne.domain.data.model.update.UpdateData
import com.oblivion.nocturne.domain.repository.UpdateRepository
import com.oblivion.nocturne.domain.utils.LocalResource
import com.oblivion.nocturne.kotlinytmusicscraper.models.oblivion.nocturne.GithubResponse
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json

class UpdateRepositoryImpl : UpdateRepository {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override fun checkForGithubReleaseUpdate(): Flow<LocalResource<UpdateData>> = flow {
        emit(LocalResource.Loading())
        try {
            val connection = (URL("https://api.github.com/repos/mosske/Oblivion-Nocturne/releases/latest").openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                setRequestProperty("Accept", "application/vnd.github+json")
                connectTimeout = 15_000
                readTimeout = 15_000
            }
            val rawJson = connection.inputStream.bufferedReader().use { it.readText() }
            val response = json.decodeFromString<GithubResponse>(rawJson)
            val apkAsset = response.assets?.firstOrNull { it?.name?.endsWith(".apk", ignoreCase = true) == true }
            val updateData = UpdateData(
                tagName = response.tagName ?: "",
                body = response.body ?: "",
                downloadUrl = apkAsset?.browserDownloadUrl,
                releaseTime = response.publishedAt ?: "",
            )
            emit(LocalResource.Success(updateData))
        } catch (e: Exception) {
            emit(LocalResource.Error(e.message ?: "Failed to check for updates"))
        }
    }.flowOn(Dispatchers.IO)
}
