package by.gastrofest.android.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import by.gastrofest.android.ui.GastrofestUiState

@Composable
fun GastroSetDetailsScreen(id: Long, state: GastrofestUiState) {
    val set = (state as? GastrofestUiState.Success)
        ?.sets
        ?.find { it.id == id }

    if (set == null) {
        Text("Сет не найден")
        return
    }

    Column(modifier = Modifier.padding(16.dp)) {
        set.theme?.let { Text(it, style = MaterialTheme.typography.headlineLarge) }

        Spacer(Modifier.height(16.dp))

        Text("Блюда:", style = MaterialTheme.typography.titleMedium)

        set.mealsDescriptions?.forEach { item ->
            Text("• $item", modifier = Modifier.padding(top = 4.dp))
        }
    }
}
