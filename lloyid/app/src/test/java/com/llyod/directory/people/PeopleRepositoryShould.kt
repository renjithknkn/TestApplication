package com.llyod.directory.people

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever

import com.llyod.directory.BaseUnitTest
import com.llyod.directory.repository.models.People
import com.llyod.directory.repository.models.PeopleRaw
import com.llyod.directory.repository.people.PeopleListMapper
import com.llyod.directory.repository.people.PeopleRepository
import com.llyod.directory.repository.retrofit.DirectoryService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.verify
import org.mockito.internal.verification.VerificationModeFactory

class PeopleRepositoryShould: BaseUnitTest(){

    //SUT - PeopleRepository

    val service : DirectoryService = mock()
    val peopleList = mock<List<People>>()
    val rawPeopleList = mock<List<PeopleRaw>>()
    val exception  = RuntimeException("Something went wrong")
    val peopleListMapper: PeopleListMapper = mock()

    @Test
    fun getPeoplesListFromService()= runTest{
        val repository = PeopleRepository(service,peopleListMapper,corutinteTestRule.testDispatcher)
        repository.getPeopleList()
        verify(service, VerificationModeFactory.times(1)).fetchPeoplesLists()

    }

    @Test
    fun delegateRawPeopleListMappingToMapper()= runTest{
        val repository = mockSuccessfulCase()
        repository.getPeopleList().first()
        verify(peopleListMapper, VerificationModeFactory.times(1)).invoke(rawPeopleList)
    }

    @Test
    fun propagateError()= runTest(){
        val repository = mockFailureCase()
        Assert.assertEquals(exception,repository.getPeopleList().first().exceptionOrNull())
    }

    private suspend fun mockFailureCase(): PeopleRepository {
        whenever(service.fetchPeoplesLists()).thenReturn(
            flow {
                emit(
                    Result.failure<List<PeopleRaw>>(exception)
                )
            }
        )
        return PeopleRepository(service, peopleListMapper,corutinteTestRule.testDispatcher)
    }

    private suspend fun mockSuccessfulCase(): PeopleRepository {
        whenever(service.fetchPeoplesLists()).thenReturn(
            flow {
                emit(Result.success(rawPeopleList))
            })
        whenever(peopleListMapper.invoke(rawPeopleList)).thenReturn(peopleList)


        return PeopleRepository(service, peopleListMapper,corutinteTestRule.testDispatcher)
    }
}