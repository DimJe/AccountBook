package org.techtown.accountbook.Model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SpendingDataDao {

    @Query("SELECT * FROM spending")
    suspend fun getAll() : List<SpendingData>

    @Insert
    suspend fun insertData(spendingData: SpendingData)

    @Query("SELECT * FROM spending WHERE year=:year")
    suspend fun getByYear(year: Int) : List<SpendingData>

    @Query("SELECT * FROM spending WHERE month=:month")
    suspend fun getByMonth(month: Int) : List<SpendingData>

    @Query("DELETE FROM spending")
    suspend fun clearTable()

    @Query("SELECT * FROM spending WHERE type=:type")
    suspend fun getByType(type: String) : List<SpendingData>
}