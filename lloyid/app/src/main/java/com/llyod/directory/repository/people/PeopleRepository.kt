package com.llyod.directory.repository.people

import com.llyod.directory.di.IoDispatcher
import com.llyod.directory.repository.models.People
import com.llyod.directory.repository.retrofit.DirectoryService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PeopleRepository @Inject constructor(
    private val service: DirectoryService,
    val peopleListMapper: PeopleListMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getPeopleList(): Flow<Result<List<People>>> {


        return service.fetchPeoplesLists().map{
            if(it.isSuccess) {
                Result.success(peopleListMapper.invoke(it.getOrNull()!!))
            }else{
                Result.failure(it.exceptionOrNull()!!)
            }
        }.flowOn(ioDispatcher)

    }
}