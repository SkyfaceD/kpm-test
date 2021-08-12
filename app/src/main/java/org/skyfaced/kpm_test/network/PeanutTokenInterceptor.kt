package org.skyfaced.kpm_test.network

import android.content.SharedPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.Buffer
import org.json.JSONObject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.skyfaced.kpm_test.model.network.body.AuthenticationBody
import org.skyfaced.kpm_test.repository.authentication.PeanutAuthenticationRepositoryImpl
import org.skyfaced.kpm_test.utils.GlobalNavigator
import org.skyfaced.kpm_test.utils.extensions.credentials
import org.skyfaced.kpm_test.utils.extensions.savePeanutToken
import java.net.HttpURLConnection

class PeanutTokenInterceptor(
    private val preferences: SharedPreferences,
) : Interceptor, KoinComponent {
    private val repository by inject<PeanutAuthenticationRepositoryImpl>()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        //In case with peanut api 500 code the only way where we can find token expiration
        //Code 401 trigger only in authentication request, ignore them
        if (response.code == HttpURLConnection.HTTP_INTERNAL_ERROR) {
            val credentials = preferences.credentials
            if (credentials.remember) {
                try {
                    val body = AuthenticationBody(credentials.login, credentials.password)
                    val token = runBlocking { repository.signIn(body) }
                        ?: throw IllegalArgumentException("Can't be null")
                    preferences.savePeanutToken(token)

                    val newBody = request.body.updateToken(token)
                    val newRequest = request.newBuilder()
                        .post(newBody)
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

    private fun RequestBody?.updateToken(token: String): RequestBody {
        if (this == null) throw IllegalArgumentException("Request body can't be null")

        val copy = this
        val buffer = Buffer()
        copy.writeTo(buffer)

        return JSONObject(buffer.readUtf8()).apply {
            put("token", token)
        }.toString().toRequestBody(contentType())
    }
}