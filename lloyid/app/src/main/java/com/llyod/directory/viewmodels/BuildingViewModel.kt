package com.llyod.directory.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llyod.directory.di.MainDispatcher
import com.llyod.directory.repository.buildings.BuildingsRepository
import com.llyod.directory.repository.models.Building
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuildingViewModel @Inject constructor(
    val repository: BuildingsRepository,
    @MainDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel () {


    private val _buildingsList  = MutableStateFlow(emptyList<Building>())
    val buildingsList : StateFlow<List<Building>> = _buildingsList

    private val _buildingListLoader = MutableStateFlow(false)
    val buildingListLoader : StateFlow<Boolean> = _buildingListLoader

    private val _buildingListErrorMessage = MutableSharedFlow<String>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST )
    var buildingListErrorMessage : SharedFlow<String> = _buildingListErrorMessage

    fun loadBuildingsList(){

        _buildingListLoader.value = true
        viewModelScope.launch(dispatcher){
            repository.getBuildingsList().collect(){
                try {
                    when {
                        it.isSuccess -> {
                            _buildingsList.value = it.getOrNull()!!
                            _buildingListErrorMessage.emit("")
                        }
                        it.isFailure -> {
                            _buildingListErrorMessage.emit(
                                it.exceptionOrNull().toString())
                        }
                    }
                }finally {
                    _buildingListLoader.value = false
                }
            }
        }
    }


}