package org.techtown.accountbook.Repository

import android.util.Log
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
    suspend fun deleteAll(){
        dao.clearTable()
    }
}