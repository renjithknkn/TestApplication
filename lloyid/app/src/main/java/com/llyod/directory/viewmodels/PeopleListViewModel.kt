package com.llyod.directory.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llyod.directory.di.MainDispatcher
import com.llyod.directory.repository.people.PeopleRepository
import com.llyod.directory.repository.models.People
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
class PeopleListViewModel @Inject constructor(
    val repository: PeopleRepository,
    @MainDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _peopleListUiState = MutableStateFlow(emptyList<People>())
    val peopleListUiState: StateFlow<List<People>> = _peopleListUiState

    private val _lisProgressBardUiState = MutableStateFlow(false)
    val lisProgressBardUiState: StateFlow<Boolean> = _lisProgressBardUiState

    private val _peopleListErrorMessage = MutableStateFlow("")
    var peopleListErrorMessage: StateFlow<String> = _peopleListErrorMessage

    private val _peopleDetail =
        MutableSharedFlow<People>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val peopleDetail: SharedFlow<People> = _peopleDetail

    fun loadPeoplesList() {
        if(_peopleListUiState.value.isEmpty()) {
            setLoaderStateVisible()
            viewModelScope.launch(dispatcher) {
                repository.getPeopleList().collect() {
                    try {
                        when {
                            it.isSuccess -> {
                                _peopleListUiState.value = it.getOrNull()!!
                                _peopleListErrorMessage.value = ""
                            }
                            it.isFailure -> {
                                _peopleListErrorMessage.value = it.exceptionOrNull().toString()
                            }
                        }
                    } finally {
                        setLoaderStateHide()
                    }

                }
            }
        }
    }

    fun loadPeopleDetail(employeeId: String) {
        viewModelScope.launch(dispatcher) {
            val people = peopleListUiState.value.find { it.employeeId == employeeId }
            people.let {
                _peopleDetail.emit(it!!)
            }
        }
    }

    private fun setLoaderStateVisible() {
        _lisProgressBardUiState.value = true
    }

    private fun setLoaderStateHide() {
        _lisProgressBardUiState.value = false
    }
}