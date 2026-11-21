package com.oblivion.nocturne.domain.repository
 
import com.oblivion.nocturne.domain.data.model.update.UpdateData
import com.oblivion.nocturne.domain.utils.LocalResource
import com.oblivion.nocturne.domain.data.model.download.DownloadProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.Flow
 
interface UpdateRepository {
    fun checkForGithubReleaseUpdate(): Flow<LocalResource<UpdateData>>
}
