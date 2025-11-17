package by.gastrofest.android.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GastroScreen(viewModel: GastroViewModel = viewModel()) {
    val state = viewModel.state.collectAsState()

    when (val uiState = state.value) {
        is GastroUiState.Loading -> Text("Загрузка...")
        is GastroUiState.Error -> Text("Ошибка: ${uiState.errorMessage}")
        is GastroUiState.Success -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp)
            ) {
                items(uiState.sets) { set ->
                    Text(set.participant?.title ?: "Без названия")
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}
