package com.llyod.directory.repository.buildings

import com.llyod.directory.repository.models.Building
import com.llyod.directory.repository.models.BuildingRaw
import javax.inject.Inject

class BuildingListMapper  @Inject constructor() :Function1<List<BuildingRaw>,List<Building>>{

    override fun invoke(rawPopleListLIst: List<BuildingRaw>): List<Building> {
        return rawPopleListLIst.map {
            Building(
                it.id,
                it.maxOccupancy,
                it.isOccupied
            )
        }
    }

}
