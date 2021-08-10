package org.skyfaced.kpm_test.network

import org.skyfaced.kpm_test.model.network.body.AuthenticationBody
import org.skyfaced.kpm_test.model.network.response.AnalyticSignalsResponse
import retrofit2.http.*

interface PartnerApi {
    @POST("/api/Authentication/RequestMoblieCabinetApiToken")
    suspend fun signIn(@Body body: AuthenticationBody): String

    @GET("/clientmobile/GetAnalyticSignals/{login}?tradingsystem=3")
    suspend fun analyticSignals(
        @Path("login") login: Int,
        @Header("passkey") token: String,
        @Query("pairs") currencyPair: String,
        @Query("from") from: Int,
        @Query("to") to: Int,
    ): List<AnalyticSignalsResponse>
}