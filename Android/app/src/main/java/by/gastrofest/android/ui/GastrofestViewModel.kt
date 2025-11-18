package by.gastrofest.android.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.gastrofest.android.repository.GastroSetRepository
import by.gastrofest.parser.model.GastroSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class GastroViewModel(
    private val context: Context
) : ViewModel() {

    private val repository: GastroSetRepository = GastroSetRepository()
    private val _state = MutableStateFlow<GastrofestUiState>(GastrofestUiState.Loading)
    val state: StateFlow<GastrofestUiState> = _state

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            _state.value = GastrofestUiState.Loading

            try {
                val fetched = repository.getGastroSets(context)
                if (fetched.isNotEmpty()) {
                    _state.value = GastrofestUiState.Success(fetched)
                } else {
                    _state.value = GastrofestUiState.Error("Гастрофест закончился")
                }
            } catch (e: Exception) {
                _state.value = GastrofestUiState.Error(e.message ?: "Ошибка загрузки")
            }
        }
    }
}

sealed interface GastrofestUiState {
    data object Loading : GastrofestUiState
    data class Success(val sets: List<GastroSet>) : GastrofestUiState
    data class Error(val errorMessage: String) : GastrofestUiState
}
