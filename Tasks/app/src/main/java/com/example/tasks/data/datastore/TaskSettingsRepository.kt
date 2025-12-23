package com.example.tasks.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

private val Context.dataStore by preferencesDataStore("task_settings")

class TaskSettingsRepository(
    private val context: Context
) {

    private object Keys {
        val SORT_TYPE = stringPreferencesKey("sort_type")
        val SELECTED_DATE = stringPreferencesKey("selected_date")
    }

    val settingsFlow: Flow<TaskSettings> =
        context.dataStore.data.map { prefs ->
            TaskSettings(
                sortType = prefs[Keys.SORT_TYPE]
                    ?.let { SortType.valueOf(it) }
                    ?: SortType.DATE_ASC,
                selectedDate = prefs[Keys.SELECTED_DATE]
                    ?.let { LocalDate.parse(it) }
            )
        }

    suspend fun saveSortType(sortType: SortType) {
        context.dataStore.edit {
            it[Keys.SORT_TYPE] = sortType.name
        }
    }

    suspend fun saveSelectedDate(date: LocalDate?) {
        context.dataStore.edit {
            if (date == null) {
                it.remove(Keys.SELECTED_DATE)
            } else {
                it[Keys.SELECTED_DATE] = date.toString()
            }
        }
    }
}
