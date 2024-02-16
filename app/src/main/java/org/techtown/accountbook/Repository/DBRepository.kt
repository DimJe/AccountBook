package org.techtown.accountbook.Repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.techtown.accountbook.Model.SpendingData
import org.techtown.accountbook.Model.SpendingDataDao
import javax.inject.Inject

class DBRepository @Inject constructor(private val dao: SpendingDataDao) {

    suspend fun insertSpendingData(data: SpendingData){
        dao.insertData(data)
    }
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
        }.flow.filterNotNull()
    }
    suspend fun deleteAll(){
        dao.clearTable()
    }

}