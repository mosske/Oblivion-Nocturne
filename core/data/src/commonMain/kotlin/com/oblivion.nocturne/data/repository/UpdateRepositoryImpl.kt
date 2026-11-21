package com.oblivion.nocturne.data.repository

import com.oblivion.nocturne.domain.data.model.update.UpdateData
import com.oblivion.nocturne.domain.repository.UpdateRepository
import com.oblivion.nocturne.kotlinytmusicscraper.models.simpmusic.GithubResponse
import com.oblivion.nocturne.ktorext.getEngine
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okio.Path.Companion.toPath
import okio.FileSystem
import kotlinx.coroutines.Dispatchers
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import com.oblivion.nocturne.domain.utils.Resource

private val httpClient = HttpClient(getEngine()) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        })
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 60_000
        connectTimeoutMillis = 15_000
    }
}

class UpdateRepositoryImpl : UpdateRepository {
    override fun checkForGithubReleaseUpdate(): Flow<Resource<UpdateData>> = flow {
        emit(Resource.Loading)
        try {
            val response = httpClient.get("https://github.com/mosske/Oblivion-Nocturne/releases/latest").body<GithubResponse>()
            
            val tagName = response.tagName ?: "latest"
            val releaseTime = response.publishedAt
            val body = response.body
            
            // Extract the APK URL from assets
            val assets = response.assets ?: emptyList()
            var downloadUrl: String? = null
            
            for (asset in assets) {
                val assetName = asset?.name ?: ""
                if (assetName.endsWith(".apk")) {
                    if (assetName.contains("universal", ignoreCase = true)) {
                        downloadUrl = asset?.browserDownloadUrl
                        break
                    } else if (downloadUrl == null) {
                        downloadUrl = asset?.browserDownloadUrl
                    }
                }
            }
            
            val updateData = UpdateData(
                tagName = tagName,
                releaseTime = releaseTime,
                body = body,
                downloadUrl = downloadUrl
            )
            emit(Resource.Success(updateData))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error occurred"))
        }
    }.flowOn(Dispatchers.Default)
}
