package com.llyod.directory.repository.retrofit

import com.llyod.directory.di.IoDispatcher
import com.llyod.directory.repository.models.PeopleRaw
import com.llyod.directory.repository.models.BuildingRaw
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DirectoryService @Inject constructor(
    private val directoryApi: DirectoryApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun fetchPeoplesLists(): Flow<Result<List<PeopleRaw>>> {
        return flow {
            emit(Result.success(directoryApi.fetchPeoplesListApi()))
        }.catch {
            emit(Result.failure(RuntimeException("Something went wrong")))
        }.flowOn(ioDispatcher)
    }

    suspend fun fetchBuildingsLists(): Flow<Result<List<BuildingRaw>>> {
        return flow {
            emit(Result.success(directoryApi.fetchBuildingsList()))
        }.catch {
            emit(Result.failure(RuntimeException("Something went wrong")))
        }.flowOn(ioDispatcher)
    }

}