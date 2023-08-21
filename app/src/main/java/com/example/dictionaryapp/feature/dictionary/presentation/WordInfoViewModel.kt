package com.example.dictionaryapp.feature.dictionary.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionaryapp.core.connectivity.NetworkConnectivityChecker
import com.example.dictionaryapp.core.util.Resource
import com.example.dictionaryapp.feature.dictionary.domain.model.UIError
import com.example.dictionaryapp.feature.dictionary.domain.usecases.GetWordInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class WordInfoViewModel @Inject constructor(
    private val getWordInfoUseCase: GetWordInfoUseCase,
    private val networkConnectivityChecker: NetworkConnectivityChecker
) : ViewModel() {


    private val _searchQueryState = MutableStateFlow("")
    val searchQueryState: StateFlow<String> = _searchQueryState

    private val _state = mutableStateOf(WordInfoState())
    val state: State<WordInfoState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val networkConnectivityFlow: Flow<Boolean>
        get() = networkConnectivityChecker.networkConnectivityFlow()

    init {
        searchQueryState
            .debounce(500)
            .onEach { query ->
                if (query.isBlank())
                    _state.value = WordInfoState()
            }
            .filter { it.isNotBlank() }
            .distinctUntilChanged { old, new ->
                if (old == new) {
                    return@distinctUntilChanged state.value.wordInfoItems.isNotEmpty()
                            || state.value.isLoading
                }
                false
            }
            .flatMapLatest { query -> getWordInfoUseCase(query) }
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = WordInfoState(
                            isLoading = true,
                            wordInfoItems = result.data ?: emptyList()
                        )
                    }

                    is Resource.Success -> {
                        _state.value = WordInfoState(wordInfoItems = result.data)
                    }

                    is Resource.Error -> {
                        _state.value = WordInfoState(isError = true)
                        _eventFlow.emit(UIEvent.ShowAlertDialog(result.uiError))
                    }
                }
            }
            .launchIn(viewModelScope)

        networkConnectivityFlow
            .onEach { isConnected ->
                if (isConnected) {
                    val value = searchQueryState.value
                    _searchQueryState.value = ""
                    _searchQueryState.value = value
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(userEvent: UserEvent) {
        when (userEvent) {
            is UserEvent.SearchQuery -> _searchQueryState.value = userEvent.query
        }
    }

    sealed interface UIEvent {
        data class ShowAlertDialog(val uiError: UIError) : UIEvent
    }

    sealed interface UserEvent {
        data class SearchQuery(val query: String) : UserEvent
    }
}