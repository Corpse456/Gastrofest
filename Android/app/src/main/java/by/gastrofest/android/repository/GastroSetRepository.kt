package by.gastrofest.android.repository

import android.content.Context
import by.gastrofest.android.model.CachedWrapper
import by.gastrofest.parser.model.GastroSet
import by.gastrofest.parser.service.GastrofestCommonService
import by.gastrofest.parser.service.ParserService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class GastroSetRepository(
    private val cacheFileName: String = "gastro_cache.json",
    private val ttlMillis: Long = TimeUnit.DAYS.toMillis(14),
) {
    private val parser: ParserService = ParserService(object : GastrofestCommonService {})
    private val gson = Gson()

    suspend fun getGastroSets(context: Context): List<GastroSet> {
        return loadFromCache(context) ?: fetchAndCache(context)
    }

    private suspend fun fetchAndCache(context: Context): List<GastroSet> = withContext(Dispatchers.IO) {
        val gastroSets: List<GastroSet> = parser.getGastroSetsList()
        if (gastrofestIsOver(gastroSets)) {
            return@withContext emptyList()
        }

        if (gastroSets.isNotEmpty()) {
            saveToCache(context, gastroSets)
        }
        gastroSets
    }

    private suspend fun loadFromCache(context: Context): List<GastroSet>? =
        withContext(Dispatchers.IO) {
            try {
                val cacheFile = cacheFile(context)
                if (!cacheFile.exists()) {
                    return@withContext null
                }

                val raw = cacheFile.readText()
                val wrapperType = object : TypeToken<CachedWrapper<List<GastroSet>>>() {}.type
                val wrapper: CachedWrapper<List<GastroSet>> = gson.fromJson(raw, wrapperType)

                val now = System.currentTimeMillis()
                return@withContext if (isDataExistsAndActual(wrapper, now)) wrapper.data else null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    private fun isDataExistsAndActual(wrapper: CachedWrapper<List<GastroSet>>, now: Long) =
        wrapper.timestamp != null
                && (now - wrapper.timestamp) < ttlMillis
                && !wrapper.data.isNullOrEmpty()
                && !gastrofestIsOver(wrapper.data)

    private fun gastrofestIsOver(gastroSets: List<GastroSet>) =
        gastroSets.first().gastrofest?.endDate?.isBefore(LocalDate.now()) == true

    private suspend fun saveToCache(context: Context, data: List<GastroSet>) =
        withContext(Dispatchers.IO) {
            try {
                val wrapper = CachedWrapper(System.currentTimeMillis(), data)
                val json = gson.toJson(wrapper)
                cacheFile(context).writeText(json)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    private fun cacheFile(context: Context): File =
        File(context.filesDir, cacheFileName)
}
