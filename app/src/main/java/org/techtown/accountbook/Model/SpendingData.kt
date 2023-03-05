package org.techtown.accountbook.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "spending")
data class SpendingData(
    val year: Int,
    val month: Int,
    val day: Int,
    val type: String,
    val money: Int
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}