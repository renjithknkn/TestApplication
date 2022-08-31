package com.llyod.directory.building

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.llyod.directory.BaseUnitTest
import com.llyod.directory.repository.models.Building
import com.llyod.directory.repository.buildings.BuildingsRepository
import com.llyod.directory.viewmodels.BuildingViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class BuildingListViewModelShould : BaseUnitTest()  {


    //SUT - RoomListViewModel
    lateinit var viewmodel : BuildingViewModel

    val repository: BuildingsRepository = mock()
    val roomsList = mock<List<Building>>()
    val expected = Result.success(roomsList)
    val exception = RuntimeException("Something went wrong")


//region Rooms List Screen unit test cases
    @Before
    fun setUp()= runTest{
    viewmodel = mockSuccessfulCase()
    }


    @Test
    fun getRoomsListFromRepository()= runTest {

        viewmodel.loadBuildingsList()
        verify(repository, Mockito.times(1)).getBuildingsList()
    }

    @Test
    fun emitRoomsListFromRepository() = runTest{


        viewmodel.loadBuildingsList()
        assertEquals(roomsList, viewmodel.buildingsList.first())
    }


    @Test
    fun turnLoaderVisibilityTrueWhileFetchingRoomsList()= runTest{

        val values = mutableListOf<Boolean>()

        val job = launch(corutinteTestRule.testDispatcher) {
            viewmodel.buildingListLoader.toList(values)
        }

        viewmodel.loadBuildingsList()
        assertEquals(true, values[1]) // Assert on the list contents
        job.cancel()
    }

    @Test
    fun turnLoaderVisibilityFalseAfterFetchingRoomsList()= runTest{


        val values = mutableListOf<Boolean>()

        val job = launch(corutinteTestRule.testDispatcher) {
            viewmodel.buildingListLoader.toList(values)
        }

        viewmodel.loadBuildingsList()
        assertEquals(false, values[2]) // Assert on the list contents
        job.cancel()
    }



    @Test
    fun emitErrorWheneverReceiveError()= runTest{
        whenever(repository.getBuildingsList()).thenReturn(
            flow {
                emit(Result.failure<List<Building>>(exception))
            }
        )
        val viewmodel = BuildingViewModel(repository,corutinteTestRule.testDispatcher)
        var actualResult = mutableListOf<String>()
        val job = launch(corutinteTestRule.testDispatcher) {
            viewmodel.buildingListErrorMessage.toList(actualResult)
        }

        viewmodel.loadBuildingsList()
        System.out.println("actualResult : ${actualResult.last()}")
        assertEquals(exception.toString(),actualResult.last())
        job.cancel()
    }

    private suspend fun mockSuccessfulCase(): BuildingViewModel {
        whenever(repository.getBuildingsList() ).thenReturn(
            flow {
                emit(expected)
            }
        )
        return BuildingViewModel(repository,corutinteTestRule.testDispatcher)
    }

//endregion


}