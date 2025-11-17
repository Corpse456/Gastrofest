package by.gastrofest.android.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import by.gastrofest.android.repository.GastroSetRepository
import by.gastrofest.parser.model.GastroSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class GastroViewModel(
    application: Application,
    private val repository: GastroSetRepository = GastroSetRepository()
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow<GastroUiState>(GastroUiState.Loading)
    val state: StateFlow<GastroUiState> = _state

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            _state.value = GastroUiState.Loading

            try {
                val fetched = repository.getGastroSets(getApplication())
                if (fetched.isNotEmpty()) {
                    _state.value = GastroUiState.Success(fetched)
                } else {
                    _state.value = GastroUiState.Error("Гастрофест закончился")
                }
            } catch (e: Exception) {
                _state.value = GastroUiState.Error(e.message ?: "Ошибка загрузки")
            }
        }
    }
}

sealed interface GastroUiState {
    data object Loading : GastroUiState
    data class Success(val sets: List<GastroSet>) : GastroUiState
    data class Error(val errorMessage: String) : GastroUiState
}
