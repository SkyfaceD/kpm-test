package org.skyfaced.kpm_test.network

import android.content.SharedPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.skyfaced.kpm_test.model.network.body.AuthenticationBody
import org.skyfaced.kpm_test.repository.authentication.PartnerAuthenticationRepositoryImpl
import org.skyfaced.kpm_test.utils.GlobalNavigator
import org.skyfaced.kpm_test.utils.extensions.credentials
import org.skyfaced.kpm_test.utils.extensions.savePartnerToken
import java.net.HttpURLConnection

//We can't use okhttp3.Authenticator reacts only on 401 code
class PartnerTokenInterceptor(
    private val preferences: SharedPreferences,
) : Interceptor, KoinComponent {
    private val repository by inject<PartnerAuthenticationRepositoryImpl>()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        //When token is incorrect or old partner api returns 403 error code
        if (response.code == HttpURLConnection.HTTP_FORBIDDEN) {
            val credentials = preferences.credentials
            if (credentials.remember) {
                try {
                    val body = AuthenticationBody(credentials.login, credentials.password)
                    val token = runBlocking { repository.signIn(body) }
                        ?: throw IllegalArgumentException("Can't be null")
                    preferences.savePartnerToken(token)

                    val newRequest = request.newBuilder()
                        .removeHeader("passkey")
                        .addHeader("passkey", token)
                        .build()

                    response.close()
                    return chain.proceed(newRequest)
                } catch (e: Exception) {
                    GlobalNavigator.logout("Не удалось обновить аутентификационные данные")
                }
            } else {
                GlobalNavigator.logout("Аутентификационные данные устарели")
            }
        }

        return response
    }
}