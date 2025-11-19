package by.gastrofest.android.ui

import androidx.compose.runtime.Composable
import by.gastrofest.android.ui.components.ErrorView
import by.gastrofest.android.ui.components.GastrofestList
import by.gastrofest.android.ui.components.LoadingView
import by.gastrofest.parser.model.GastroSet

@Composable
fun GastrofestScreen(
    state: GastrofestUiState,
    onItemClick: (GastroSet) -> Unit
) {
    when (state) {
        is GastrofestUiState.Loading -> LoadingView()
        is GastrofestUiState.Error -> ErrorView(state.errorMessage)
        is GastrofestUiState.Success -> GastrofestList(state.sets, onItemClick)
    }
}
