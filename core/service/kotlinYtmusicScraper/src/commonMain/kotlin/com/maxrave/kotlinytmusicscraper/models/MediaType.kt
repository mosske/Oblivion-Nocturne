package com.oblivion.nocturne.kotlinytmusicscraper.models

sealed class MediaType {
    data object Song : MediaType()

    data object Video : MediaType()
}