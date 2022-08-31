package com.llyod.directory.repository.retrofit

import com.llyod.directory.repository.models.PeopleRaw
import com.llyod.directory.repository.models.BuildingRaw
import retrofit2.http.GET

interface DirectoryApi {

    @GET("people")
    suspend fun fetchPeoplesListApi() : List<PeopleRaw>

    @GET("rooms")
    suspend fun fetchBuildingsList(): List<BuildingRaw>
}
