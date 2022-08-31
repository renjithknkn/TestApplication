package com.llyod.directory.people

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.llyod.directory.BaseUnitTest
import com.llyod.directory.repository.people.PeopleRepository
import com.llyod.directory.repository.models.People
import com.llyod.directory.viewmodels.PeopleListViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class PeopleListViewModelShould : BaseUnitTest()  {


    //SUT - PeopleListViewModel
    lateinit var viewmodel : PeopleListViewModel

    val repository: PeopleRepository = mock()
    val peoplesList = mock<List<People>>()
    val expected = Result.success(peoplesList)
    val exception = RuntimeException("Something went wrong")


//region People List Screen unit test cases

    @Before
    fun setUp()= runTest{
        viewmodel = mockSuccessfulCase()
    }


    @Test
    fun getPeopleListFromRepository()= runTest {

        viewmodel.loadPeoplesList()
        verify(repository, Mockito.times(1)).getPeopleList()
    }

    @Test
    fun emitPeoplesListFromRepository() = runTest{


        viewmodel.loadPeoplesList()
        assertEquals(peoplesList, viewmodel.peopleListUiState.first())
    }


    @Test
    fun turnLoaderVisibilityTrueWhileFetchingPeoplesList()= runTest{

        val values = mutableListOf<Boolean>()

        val job = launch(corutinteTestRule.testDispatcher) {
            viewmodel.lisProgressBardUiState.toList(values)
        }

        viewmodel.loadPeoplesList()
        assertEquals(true, values[1]) // Assert on the list contents
        job.cancel()
    }

    @Test
    fun turnLoaderVisibilityFalseAfterFetchingPeoplesList()= runTest{


        val values = mutableListOf<Boolean>()

        val job = launch(corutinteTestRule.testDispatcher) {
            viewmodel.lisProgressBardUiState.toList(values)
        }

        viewmodel.loadPeoplesList()
        assertEquals(false, values[2]) // Assert on the list contents
        job.cancel()
    }



    @Test
    fun emitErrorWheneverReceiveError()= runTest{
        whenever(repository.getPeopleList()).thenReturn(
            flow {
                emit(Result.failure<List<People>>(exception))
            }
        )
        val viewmodel = PeopleListViewModel(repository,corutinteTestRule.testDispatcher)
        var actualResult = mutableListOf<String>()
        val job = launch(corutinteTestRule.testDispatcher) {
            viewmodel.peopleListErrorMessage.toList(actualResult)
        }

        viewmodel.loadPeoplesList()
        System.out.println("actualResult : ${actualResult.last()}")
        assertEquals(exception.toString(),actualResult.last())
        job.cancel()
    }

    private suspend fun mockSuccessfulCase(): PeopleListViewModel {
        whenever(repository.getPeopleList() ).thenReturn(
            flow {
                emit(expected)
            }
        )
        return PeopleListViewModel(repository,corutinteTestRule.testDispatcher)
    }

//endregion


//region People Detail screen unit test cases


    @Test
    fun returnPeopleDetailWithThePassedEmployeeId()= runTest{
        val peopleList = getDummyPeopleList()
        val employeeId = "4"
        val expectedEmployeeDetail = peopleList.find { it.employeeId== employeeId}

        whenever(repository.getPeopleList() ).thenReturn(
            flow {
                emit(Result.success(peopleList))
            }
        )

        val viewmodel = PeopleListViewModel(repository,corutinteTestRule.testDispatcher)
        var result  =  arrayListOf<People>()
        val job = launch(corutinteTestRule.testDispatcher) {
             viewmodel.peopleDetail.toList(result)
        }
        viewmodel.loadPeoplesList()
        viewmodel.loadPeopleDetail(employeeId)


        assertEquals(expectedEmployeeDetail,result[0])
        job.cancel()
    }


    private fun getDummyPeopleList() : List<People>{


        return listOf(
            People("1", "Maggie",
                "Future Functionality Strategist",
                "https://randomuser.me/api/portraits/women/21.jpg",
                "Crystel.Nicolas61@hotmail.com",
                "pink"
            ),
           People("2", "Armando",
                "Principal Accounts Developer",
                "https://randomuser.me/api/portraits/women/23.jpg",
                "Crystel.Nicolas61@hotmail.com",
                "sky blue"
            ),
           People("3", "Gunnar",
                "Future Functionality Strategist",
                "https://randomuser.me/api/portraits/women/21.jpg",
                "Paolo.Hudson@yahoo.com",
                "cyan"
            ),
           People("4", "Ceasar",
                "Future Interactions Supervisor",
                "https://randomuser.me/api/portraits/women/21.jpg",
                "Crystel.Nicolas61@hotmail.com",
                "indigo"
            )
        )
    }
//endregion

}