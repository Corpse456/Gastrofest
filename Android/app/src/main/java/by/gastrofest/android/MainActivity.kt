package by.gastrofest.android

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import by.gastrofest.android.factory.GastroViewModelFactory
import by.gastrofest.android.ui.GastrofestViewModel
import by.gastrofest.android.ui.components.AppNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current.applicationContext as Application
            val viewModel: GastrofestViewModel =
                viewModel(factory = GastroViewModelFactory(context))
            val uiState = viewModel.state.collectAsState().value

            AppNavHost(uiState)
        }
    }
}
