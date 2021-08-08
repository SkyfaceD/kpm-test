package org.skyfaced.kpm_test.network

import kotlinx.coroutines.flow.Flow
import org.skyfaced.kpm_test.model.network.body.AuthenticationBody
import org.skyfaced.kpm_test.model.network.body.AuthorizationBody
import org.skyfaced.kpm_test.model.network.response.AccountInformationResponse
import org.skyfaced.kpm_test.model.network.response.AuthenticationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface PeanutApi {
    @POST("/api/ClientCabinetBasic/IsAccountCredentialsCorrect")
    suspend fun signIn(@Body body: AuthenticationBody): AuthenticationResponse

    @POST("/api/ClientCabinetBasic/GetAccountInformation")
    fun accountInformation(@Body body: AuthorizationBody): Flow<AccountInformationResponse>

    @POST("/api/ClientCabinetBasic/GetLastFourNumbersPhone")
    fun lastFourNumberPhone(@Body body: AuthorizationBody): Flow<String>
}