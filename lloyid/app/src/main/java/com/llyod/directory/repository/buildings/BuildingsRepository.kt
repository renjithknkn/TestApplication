package com.llyod.directory.repository.buildings

import com.llyod.directory.di.IoDispatcher
import com.llyod.directory.repository.models.Building
import com.llyod.directory.repository.retrofit.DirectoryService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BuildingsRepository @Inject constructor(
    private val service: DirectoryService,
    val buildingListMapper: BuildingListMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getBuildingsList(): Flow<Result<List<Building>>> {

        return service.fetchBuildingsLists().map{
            if(it.isSuccess) {
                Result.success(buildingListMapper.invoke(it.getOrNull()!!))
            }else{
                Result.failure(it.exceptionOrNull()!!)
            }
        }.flowOn(ioDispatcher)

    }
}