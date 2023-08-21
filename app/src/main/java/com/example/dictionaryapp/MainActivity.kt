package com.example.dictionaryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dictionaryapp.core.components.DialogState
import com.example.dictionaryapp.core.components.DictionaryAlertDialog
import com.example.dictionaryapp.feature.dictionary.presentation.WordInfoViewModel
import com.example.dictionaryapp.feature.dictionary.presentation.components.SearchField
import com.example.dictionaryapp.feature.dictionary.presentation.components.WordInfoItem
import com.example.dictionaryapp.ui.theme.DictionaryAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DictionaryAppTheme {
                DictionaryApp()
            }
        }
    }

    @Composable
    fun DictionaryApp(viewModel: WordInfoViewModel = hiltViewModel()) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {

            val state = viewModel.state.value
            var dialogState by rememberSaveable { mutableStateOf(DialogState()) }
            val scaffoldState = rememberScaffoldState()

            LaunchedEffect(key1 = true) {
                viewModel.eventFlow.collectLatest { event ->
                    when (event) {
                        is WordInfoViewModel.UIEvent.ShowAlertDialog -> {
                            dialogState = DialogState(
                                isVisible = true,
                                title = event.uiError.title,
                                message = event.uiError.message,
                                onDismiss = {
                                    dialogState = dialogState.copy(isVisible = false)
                                }
                            )
                        }
                    }
                }
            }

            Scaffold(
                scaffoldState = scaffoldState
            ) {
                Box(
                    modifier = Modifier
                        .padding(it)
                        .background(MaterialTheme.colors.background)
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    if (state.isError && dialogState.isVisible) {
                        DictionaryAlertDialog(dialogState)
                    }
                    if (state.wordInfoItems.isEmpty() && !state.isLoading && !state.isError) {
                        Text(
                            text = "Let's learn new word! Start typing...",
                            modifier = Modifier.align(
                                Alignment.Center
                            )
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        SearchField()
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            itemsIndexed(state.wordInfoItems) { idx, wordInfo ->
                                WordInfoItem(wordInfo = wordInfo)
                                if (idx < state.wordInfoItems.lastIndex)
                                    Divider(color = MaterialTheme.colors.secondary)
                            }
                        }
                    }
                }
            }
        }
    }
}