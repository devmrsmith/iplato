package com.iplato.spacex.repository

import com.iplato.spacex.api.BaseApiResponse
import com.iplato.spacex.api.SpaceXService
import javax.inject.Inject


class Repository @Inject constructor(private val apiService: SpaceXService) :
    BaseApiResponse() {

    suspend fun fetchShips() = getResult { apiService.getShips("10", "0") }
}