package org.techtown.accountbook.Repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.techtown.accountbook.Model.SpendingData
import org.techtown.accountbook.Model.SpendingDataDao
import org.techtown.accountbook.Model.SpendingUiData

class DBRepository(private val dao: SpendingDataDao) {

    suspend fun insertSpendingData(data: SpendingData){
        dao.insertData(data)
    }
    fun getUiData(year: Int,month: Int): Flow<ResultState<List<SpendingUiData>>> = flow{
        try {
            emit(ResultState.Success(dao.getUiData(year,month)))
        }catch (e:Exception){
            emit(ResultState.Error(e.message?:""))
        }
    }.flowOn(Dispatchers.IO)

    fun getUiDataFlow(year: Int,month: Int) = flow{
        dao.getUiDataFlow(year, month).collect{
            emit(ResultState.Success(it))
        }
    }.flowOn(Dispatchers.IO)

    fun getChartingData(year: Int,month: Int) = flow{
        dao.getChartingData(year, month).collect{
            emit(ResultState.Success(it))
        }
    }.flowOn(Dispatchers.IO)

    fun getPagingData(year: Int,month: Int): Flow<PagingData<SpendingData>>{
        return Pager(config = PagingConfig(
            pageSize = 10,
            enablePlaceholders =  false,
            maxSize = 50
        )){
            dao.getSpendingData(year, month)
        }.flow
    }
    suspend fun deleteAll(){
        dao.clearTable()
    }

}