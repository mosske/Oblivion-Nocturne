package com.oblivion.nocturne.domain.data.model.home

import com.oblivion.nocturne.domain.data.model.home.chart.Chart
import com.oblivion.nocturne.domain.data.model.mood.Mood
import com.oblivion.nocturne.domain.utils.Resource

data class HomeResponse(
    val homeItem: Resource<ArrayList<HomeItem>>,
    val exploreMood: Resource<Mood>,
    val exploreChart: Resource<Chart>,
)