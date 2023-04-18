package org.techtown.accountbook.Model

import androidx.room.ColumnInfo

data class SpendingChartingData(
    @ColumnInfo(name = "day") val day:Int,
    @ColumnInfo(name = "money") val money:Int,
)