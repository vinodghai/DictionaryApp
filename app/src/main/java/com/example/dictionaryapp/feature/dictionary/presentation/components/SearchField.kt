package com.example.dictionaryapp.feature.dictionary.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dictionaryapp.feature.dictionary.presentation.WordInfoViewModel

@Composable
fun SearchField(viewModel: WordInfoViewModel = hiltViewModel()) {
    val query = viewModel.searchQueryState.collectAsState().value
    SearchFieldText(query = query, onValueChange = { searchText ->
        viewModel.onEvent(
            WordInfoViewModel.UserEvent.SearchQuery(
                searchText
            )
        )
    })
}

@Composable
private fun SearchFieldText(query: String, onValueChange: (value: String) -> Unit) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = query,
        onValueChange = onValueChange,
        placeholder = {
            Text("Search...")
        })
}