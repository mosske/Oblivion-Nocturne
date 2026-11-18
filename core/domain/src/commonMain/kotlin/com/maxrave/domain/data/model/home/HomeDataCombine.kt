package com.oblivion.nocturne.domain.data.model.home

import com.oblivion.nocturne.domain.data.model.home.chart.Chart
import com.oblivion.nocturne.domain.data.model.mood.Mood
import com.oblivion.nocturne.domain.utils.Resource

data class HomeDataCombine(
    val home: Resource<Triple<String?, List<HomeItem>, String?>>,
    val mood: Resource<Mood>,
    val chart: Resource<Chart>,
    val newRelease: Resource<List<HomeItem>>,
)