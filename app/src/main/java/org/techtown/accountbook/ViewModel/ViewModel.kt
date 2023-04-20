package org.techtown.accountbook.ViewModel

import org.techtown.accountbook.Repository.DBRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.techtown.accountbook.Model.SpendingChartingData
import org.techtown.accountbook.Model.SpendingData
import org.techtown.accountbook.Model.SpendingUiData
import org.techtown.accountbook.Repository.ResultState

class ViewModel(private val dbRepository: DBRepository) : ViewModel() {

    private var mSpendingUiData: MutableStateFlow<ResultState<List<SpendingUiData>>> = MutableStateFlow(ResultState.Loading())
    var spendingUiData: StateFlow<ResultState<List<SpendingUiData>>> = mSpendingUiData

    private var mSpendingData: MutableStateFlow<ResultState<List<SpendingData>>> = MutableStateFlow(ResultState.Loading())
    var spendingData: StateFlow<ResultState<List<SpendingData>>> = mSpendingData

    private var mSpendingChartingData: MutableStateFlow<ResultState<List<SpendingChartingData>>> = MutableStateFlow(ResultState.Loading())
    var spendingChartingData: StateFlow<ResultState<List<SpendingChartingData>>> = mSpendingChartingData

    fun insertData(data: SpendingData)= viewModelScope.launch {
        dbRepository.insertSpendingData(data)
    }

    fun getUiData(year: Int,month: Int) = viewModelScope.launch {
        mSpendingUiData.value = ResultState.Loading()
        dbRepository.getUiDataFlow(year,month)
            .catch { error->
                mSpendingUiData.value = ResultState.Error(error.message?:"")
            }
            .collect{ value ->
                mSpendingUiData.value = value
            }
    }
    fun getChartingData(year: Int,month: Int) = viewModelScope.launch {
        mSpendingChartingData.value = ResultState.Loading()
        dbRepository.getChartingData(year, month)
            .catch {
                mSpendingChartingData.value = ResultState.Error(it.message?:"")
            }
            .collect{
                mSpendingChartingData.value = it
            }
    }

    fun getPagingData(year: Int,month: Int) : Flow<PagingData<SpendingData>>{
        return dbRepository.getPagingData(year, month).cachedIn(viewModelScope)
    }
    fun deleteDB(){
        viewModelScope.launch {
            dbRepository.deleteAll()
        }
    }
}