package com.oblivion.nocturne.domain.data.model.searchResult

import com.oblivion.nocturne.domain.data.type.SearchResultType

data class SearchSuggestions(
    val queries: List<String>,
    val recommendedItems: List<SearchResultType>,
)