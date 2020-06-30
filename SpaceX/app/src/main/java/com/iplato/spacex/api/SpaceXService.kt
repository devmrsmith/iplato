package com.iplato.spacex.api

import com.iplato.spacex.data.SpaceXShips
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit Service implementing required SpaceX public Ship API
 * https://api.spacexdata.com/v3/ships
 * https://docs.spacexdata.com/?version=latest
 */
interface SpaceXService {

    @GET("v3/ships")
    suspend fun getShips(
        @Query("limit") limit: String?,
        @Query("offset") offset: String?
    ): Response<SpaceXShips>

}