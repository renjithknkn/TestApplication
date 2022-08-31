package com.llyod.directory.building

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever

import com.llyod.directory.BaseUnitTest
import com.llyod.directory.repository.models.Building
import com.llyod.directory.repository.models.BuildingRaw
import com.llyod.directory.repository.retrofit.DirectoryService
import com.llyod.directory.repository.buildings.BuildingListMapper
import com.llyod.directory.repository.buildings.BuildingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.verify
import org.mockito.internal.verification.VerificationModeFactory

class BuildingRepositoryShould: BaseUnitTest(){

    //SUT - RoomRepository

    val service : DirectoryService = mock()
    val buildingList = mock<List<Building>>()
    val rawRoomList = mock<List<BuildingRaw>>()
    val exception  = RuntimeException("Something went wrong")
    val buildingListMapper: BuildingListMapper = mock()

    @Test
    fun getRoomsListFromService()= runTest{
        val repository = BuildingsRepository(service,buildingListMapper,corutinteTestRule.testDispatcher)
        repository.getBuildingsList()
        verify(service, VerificationModeFactory.times(1)).fetchBuildingsLists()

    }

    @Test
    fun delegateRawPeopleListMappingToMapper()= runTest{
        val repository = mockSuccessfulCase()
        repository.getBuildingsList().first()
        verify(buildingListMapper, VerificationModeFactory.times(1)).invoke(rawRoomList)
    }

    @Test
    fun propagateError()= runTest(){
        val repository = mockFailureCase()
        Assert.assertEquals(exception,repository.getBuildingsList().first().exceptionOrNull())
    }

    private suspend fun mockFailureCase(): BuildingsRepository {
        whenever(service.fetchBuildingsLists()).thenReturn(
            flow {
                emit(
                    Result.failure<List<BuildingRaw>>(exception)
                )
            }
        )
        return BuildingsRepository(service, buildingListMapper,corutinteTestRule.testDispatcher)
    }

    private suspend fun mockSuccessfulCase(): BuildingsRepository {
        whenever(service.fetchBuildingsLists()).thenReturn(
            flow {
                emit(Result.success(rawRoomList))
            })
        whenever(buildingListMapper.invoke(rawRoomList)).thenReturn(buildingList)


        return BuildingsRepository(service, buildingListMapper,corutinteTestRule.testDispatcher)
    }
}