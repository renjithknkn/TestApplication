package com.llyod.directory.building

import com.llyod.directory.repository.models.BuildingRaw
import com.llyod.directory.repository.buildings.BuildingListMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class BuildingListMapperShould {


    //SUT - RoomListMapper
    private val buildingListmapper = BuildingListMapper()

    //MOCKS
    val rawBuildngList = getDummyBuildingRawList()

    private val mapperResult = buildingListmapper(rawBuildngList)

    @Test
    fun keepSameId() {
        assertEquals(mapperResult[0].id, rawBuildngList[0].id)
    }

    @Test
    fun keepSameMaxOccupancy() {
        assertEquals(mapperResult[0].maxOccupancy,rawBuildngList[0].maxOccupancy)
    }

    @Test
    fun keepSameOccupiedStatus() {

        assertEquals(mapperResult[0].isOccupied, rawBuildngList[0].isOccupied)
    }


    private fun getDummyBuildingRawList(): List<BuildingRaw> {

        return listOf(
            BuildingRaw(
                "2022-01-25T15:13:11.387Z",
                false,
                44832,
                "1"
            ),
            BuildingRaw(
                "2022-01-25T04:00:35.500Z",
                false,
                75480,
                "2"
            ),
            BuildingRaw(
                "2022-01-25T14:37:26.128Z",
                true,
                53539,
                "3"
            ),

            BuildingRaw(
                "2022-01-24T20:52:50.765Z",
                true,
                534539,
                 "4"
            )
        )
    }
}