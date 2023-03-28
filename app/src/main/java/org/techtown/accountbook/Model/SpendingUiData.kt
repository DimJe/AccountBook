package org.techtown.accountbook.Model

import androidx.room.ColumnInfo

data class SpendingUiData(
    @ColumnInfo(name = "year") val year:Int,
    @ColumnInfo(name = "month") val month:Int,
    @ColumnInfo(name = "day") val day:Int,
    @ColumnInfo(name = "money") val money:Int
)
