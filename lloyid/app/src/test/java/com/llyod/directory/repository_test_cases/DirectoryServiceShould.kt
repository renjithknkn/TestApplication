package com.llyod.directory.repository_test_cases

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.llyod.directory.BaseUnitTest
import com.llyod.directory.repository.models.PeopleRaw
import com.llyod.directory.repository.models.BuildingRaw
import com.llyod.directory.repository.retrofit.DirectoryApi
import com.llyod.directory.repository.retrofit.DirectoryService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class DirectoryServiceShould : BaseUnitTest(){

    val directoryApi:DirectoryApi = mock()
    val rawPeopleList : List<PeopleRaw> = mock()
    val exception = RuntimeException("Something went wrong")
    val rawRoomList : List<BuildingRaw> = mock()


    @Test
    fun getPeopleListFromDirectoryApi()= runTest{
        val directoryService = DirectoryService(directoryApi,corutinteTestRule.testDispatcher)
        directoryService.fetchPeoplesLists().first()
        verify(directoryApi, times(1)).fetchPeoplesListApi()
    }

    @Test
    fun convertPeopleListFromApiToFlow()= runTest{
        whenever(directoryApi.fetchPeoplesListApi()).thenReturn(
            rawPeopleList
        )
        val directoryService = DirectoryService(directoryApi,corutinteTestRule.testDispatcher)
        Assert.assertEquals(Result.success(rawPeopleList),directoryService.fetchPeoplesLists().first())
    }

    @Test
    fun emitFailureOnExceptionFromPeopleListApi()= runTest{
        whenever(directoryApi.fetchPeoplesListApi()).thenThrow(
            RuntimeException("Server error")
        )

        val directoryService = DirectoryService(directoryApi,corutinteTestRule.testDispatcher)
        Assert.assertEquals(exception.message,directoryService.fetchPeoplesLists().first().exceptionOrNull()?.message)
    }

    @Test
    fun getRoomListFromApi()= runTest{
        whenever(directoryApi.fetchBuildingsList()).thenReturn(rawRoomList)
        val directoryService =  DirectoryService(directoryApi,corutinteTestRule.testDispatcher)
        directoryService.fetchBuildingsLists().first()
        verify(directoryApi, times(1)).fetchBuildingsList()
    }

    @Test
    fun convertRoomListFromApiToFlow()= runTest{
        whenever(directoryApi.fetchBuildingsList()).thenReturn(
            rawRoomList
        )
        val directoryService = DirectoryService(directoryApi,corutinteTestRule.testDispatcher)
        Assert.assertEquals(Result.success(rawRoomList),directoryService.fetchBuildingsLists().first())
    }

    @Test
    fun emitFailureOnExceptionFromRoomListApi()= runTest{
        whenever(directoryApi.fetchBuildingsList()).thenThrow(
            RuntimeException("Server error")
        )

        val directoryService = DirectoryService(directoryApi,corutinteTestRule.testDispatcher)
        Assert.assertEquals(exception.message,directoryService.fetchBuildingsLists().first().exceptionOrNull()?.message)
    }

}