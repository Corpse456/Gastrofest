package by.gastrofest.android.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import by.gastrofest.android.ui.GastrofestUiState
import coil.compose.AsyncImage

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

        // Название заведения
        Text(
            text = set.participant?.title ?: "Без названия",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(6.dp))

        // Адрес
        set.participant?.address?.let {
            Text(text = "📍 $it", style = MaterialTheme.typography.bodyMedium)
        }

        // Телефон
        set.participant?.phone?.let {
            Text(text = "📞 $it", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.height(16.dp))

        // Тема
        set.theme?.let {
            Text(text = it, style = MaterialTheme.typography.titleMedium)
        }

        Spacer(Modifier.height(8.dp))

        // Вес
        set.weight?.let {
            Text(text = "Вес сета: ${it} г", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.height(16.dp))

        // Фото блюд
        if (!set.mealsImages.isNullOrEmpty()) {
            Text(
                text = "Фото блюд:",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))

            LazyRow() {
                items(set.mealsImages!!) { img ->
                    AsyncImage(
                        model = img,
                        contentDescription = null,
                        modifier = Modifier
                            .height(140.dp)
                            .padding(end = 8.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }

        // Описание блюд
        Text("Состав:", style = MaterialTheme.typography.titleMedium)
        set.mealsDescriptions?.forEach {
            Text("• $it", modifier = Modifier.padding(top = 4.dp))
        }

        // Время работы
        Text("График работы:", style = MaterialTheme.typography.titleMedium)
        set.participant?.workingHours?.forEach {
            Text("• ${it.weekDays}: ${it.openTime} - ${it.closeTime}", modifier = Modifier.padding(top = 4.dp))
        }
    }
}

