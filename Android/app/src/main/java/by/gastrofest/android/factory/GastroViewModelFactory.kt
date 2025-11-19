package by.gastrofest.android.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.gastrofest.android.ui.GastrofestViewModel

class GastroViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GastrofestViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GastrofestViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
