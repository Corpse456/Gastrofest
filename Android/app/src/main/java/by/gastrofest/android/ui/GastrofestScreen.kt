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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import by.gastrofest.android.factory.GastroViewModelFactory
import by.gastrofest.android.ui.components.GastroSetCard

@Composable
fun GastroScreen() {
    val context = LocalContext.current
    val viewModel: GastroViewModel = viewModel(factory = GastroViewModelFactory(context))
    val state = viewModel.state.collectAsState()

    when (val uiState = state.value) {
        is GastrofestUiState.Loading -> Text("Загрузка...")
        is GastrofestUiState.Error -> Text("Ошибка: ${uiState.errorMessage}")
        is GastrofestUiState.Success -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp)
            ) {
                items(uiState.sets) { set ->
                    GastroSetCard(set)
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}
