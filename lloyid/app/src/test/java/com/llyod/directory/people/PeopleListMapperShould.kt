package com.llyod.directory.people

import com.llyod.directory.repository.models.PeopleRaw
import com.llyod.directory.repository.people.PeopleListMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class PeopleListMapperShould {


    //SUT - PeopleListMapper
    private val peopleLIstMapper =  PeopleListMapper()
    //MOCKS
    val rawPeopleList = getDummyPeopleRawList()

    private val mapperResult  = peopleLIstMapper(rawPeopleList)

    @Test
    fun keepSameId(){

        assertEquals(mapperResult[0].employeeId, rawPeopleList[0].id)
    }

    @Test
    fun keepSameName(){

        assertEquals(mapperResult[0].name,rawPeopleList[0].firstName+" "+rawPeopleList[0].lastName,)
    }

    @Test
    fun keepSameAvatar(){

        assertEquals(mapperResult[0].avatar,rawPeopleList[0].avatar)
    }

    @Test
    fun keepSameJobTitle(){

        assertEquals(mapperResult[0].jobTitle,rawPeopleList[0].jobtitle)
    }

    @Test
    fun keepSameEmail(){

        assertEquals(mapperResult[0].email,rawPeopleList[0].email)
    }
    @Test
    fun keepSameFavColore(){

        assertEquals(mapperResult[0].favColor,rawPeopleList[0].favouriteColor)
    }


    private fun getDummyPeopleRawList() : List<PeopleRaw>{

        return listOf(
            PeopleRaw(
                "2022-01-25T15:13:11.387Z",
                "Maggie","Brekke",
                "https://randomuser.me/api/portraits/women/21.jpg",
                "aCrystel.Nicolas61@hotmail.com",
                "Future Functionality Strategist",
                "pink","1"
            ),
            PeopleRaw("2022-01-24T22:47:43.227Z", "Armando","Ceasar",
                "https://randomuser.me/api/portraits/women/23.jpg",
                "bCrystel.Nicolas61@hotmail.com",
                "Principal Accounts Developer",
                "sky blue","2"
            ),
            PeopleRaw("2022-01-24T17:02:23.729Z",
                "Armando", "Weber",
                "https://randomuser.me/api/portraits/women/21.jpg",
                "Paolo.Hudson@yahoo.com",
                "aFuture Functionality Strategist",
                "cyan","3"
            ),
            PeopleRaw("2022-01-25T02:27:00.553Z",
                "Gunnar","Gibson",
                "https://randomuser.me/api/portraits/women/21.jpg",
                "Ccrystel.Nicolas61@hotmail.com",
                "bFuture Interactions Supervisor",
                "indigo","4"
            )
        )
    }
}