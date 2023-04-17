package org.techtown.accountbook.Model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SpendingDataDao {

    @Query("SELECT * FROM spending")
    fun getAll() : List<SpendingData>

    @Insert
    suspend fun insertData(spendingData: SpendingData)

    @Query("SELECT * FROM spending WHERE year=:year")
    fun getByYear(year: Int) : Flow<List<SpendingData>>

    @Query("SELECT * FROM spending WHERE month=:month")
    fun getByMonth(month: Int) : Flow<List<SpendingData>>

    @Query("DELETE FROM spending")
    suspend fun clearTable()

    @Query("SELECT * FROM spending WHERE type=:type")
    fun getByType(type: String) : Flow<List<SpendingData>>

    @Query("SELECT year,month,day,sum(money) as money\n" +
            "FROM spending \n" +
            "WHERE year =:year AND month =:month\n" +
            "group by day;")
    suspend fun getUiData(year: Int,month: Int) : List<SpendingUiData>

    @Query("SELECT year,month,day,sum(money) as money\n" +
            "FROM spending \n" +
            "WHERE year =:year AND month =:month\n" +
            "group by day;")
    fun getUiDataFlow(year: Int,month: Int) : Flow<List<SpendingUiData>>

    //차트 그릴떄 필요한 데이터 덩는 쿼리 만들어야함 해당 월의 날짜와 금액 ex) (1,3000),(2,54440)....
}