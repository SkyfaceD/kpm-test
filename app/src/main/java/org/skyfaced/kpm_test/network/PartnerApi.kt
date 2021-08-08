package org.skyfaced.kpm_test.network

import kotlinx.coroutines.flow.Flow
import org.skyfaced.kpm_test.model.network.body.AuthenticationBody
import org.skyfaced.kpm_test.model.network.response.AnalyticSignalResponse
import retrofit2.http.*

interface PartnerApi {
    @POST("/api/Authentication/RequestMoblieCabinetApiToken")
    suspend fun signIn(@Body body: AuthenticationBody): String

    @GET("/clientmobile/GetAnalyticSignals/{login}?tradingsystem=3")
    fun analyticsSignals(
        @Path("login") login: Int,
        @Query("pairs") pairs: String,
        @Query("from") from: Long,
        @Query("to") to: Long
    ): Flow<AnalyticSignalResponse>
}