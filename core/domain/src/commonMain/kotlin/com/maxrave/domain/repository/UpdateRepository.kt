package com.oblivion.nocturne.domain.repository

import com.oblivion.nocturne.domain.data.model.update.UpdateData
import com.oblivion.nocturne.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface UpdateRepository {
    fun checkForGithubReleaseUpdate(): Flow<Resource<UpdateData>>
}