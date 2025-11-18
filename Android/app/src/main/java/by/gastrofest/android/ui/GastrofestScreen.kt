package by.gastrofest.android.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import by.gastrofest.android.factory.GastroViewModelFactory
import by.gastrofest.android.ui.components.ErrorView
import by.gastrofest.android.ui.components.GastroList
import by.gastrofest.android.ui.components.LoadingView

@Composable
fun GastroScreen() {
    val context = LocalContext.current
    val viewModel: GastroViewModel = viewModel(factory = GastroViewModelFactory(context))

    when (val state = viewModel.state.collectAsState().value) {
        is GastrofestUiState.Loading -> LoadingView()
        is GastrofestUiState.Error -> ErrorView(state.errorMessage)
        is GastrofestUiState.Success -> GastroList(state.sets)
    }
}
