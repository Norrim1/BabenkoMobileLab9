package com.example.tasks.data.datastore

import java.time.LocalDate

enum class SortType {
    DATE_ASC,
    DATE_DESC,
    NAME_ASC,
    NAME_DESC
}

data class TaskSettings(
    val sortType: SortType = SortType.DATE_ASC,
    val selectedDate: LocalDate? = null
)
