package org.techtown.accountbook.ViewModel

import android.util.Log
import org.techtown.accountbook.Repository.DBRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.techtown.accountbook.Model.SpendingData
import org.techtown.accountbook.Model.SpendingUiData
import org.techtown.accountbook.Repository.ResultState

class ViewModel(private val dbRepository: DBRepository) : ViewModel() {

    private var mSpendingUiData: MutableStateFlow<ResultState<List<SpendingUiData>>> = MutableStateFlow(ResultState.Loading())
    var spendingUiData: StateFlow<ResultState<List<SpendingUiData>>> = mSpendingUiData

    private var mSpendingData: MutableStateFlow<ResultState<List<SpendingData>>> = MutableStateFlow(ResultState.Loading())
    var spendingData: StateFlow<ResultState<List<SpendingData>>> = mSpendingData

    fun insertData(data: SpendingData)= viewModelScope.launch {
        dbRepository.insertSpendingData(data)
    }

    fun getUiData(year: Int,month: Int) = viewModelScope.launch {
        mSpendingUiData.value = ResultState.Loading()
        dbRepository.getUiData(year,month)
            .catch { error->
                mSpendingUiData.value = ResultState.Error(error.message?:"")
            }
            .collect{ value ->
                mSpendingUiData.value = value
            }
    }
    fun deleteDB(){
        viewModelScope.launch {
            dbRepository.deleteAll()
        }
    }
}