package org.skyfaced.kpm_test.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import org.skyfaced.kpm_test.utils.PARTNER_URL
import org.skyfaced.kpm_test.utils.PEANUT_URL
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

@ExperimentalSerializationApi
class ApplicationNetwork {
    private val jsonConverter = Json.asConverterFactory("application/json".toMediaType())
    private val logInterceptor = HttpLoggingInterceptor { message ->
        Timber.tag("OkHttp").d(message)
    }.setLevel(HttpLoggingInterceptor.Level.BODY)
    private val TIMEOUT = 15_000L

    private val peanutClient: OkHttpClient
        get() {
            return OkHttpClient.Builder()
                .addInterceptor(logInterceptor)
                .protocols(listOf(Protocol.HTTP_1_1))
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build()
        }

    fun peanutApi(): PeanutApi {
        return Retrofit.Builder()
            .baseUrl(PEANUT_URL)
            .client(peanutClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(jsonConverter)
            .build()
            .create(PeanutApi::class.java)
    }

    private val partnerClient: OkHttpClient
        get() {
            return OkHttpClient.Builder()
                .addInterceptor(logInterceptor)
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build()
        }

    fun partnerApi(): PartnerApi {
        return Retrofit.Builder()
            .baseUrl(PARTNER_URL)
            .client(partnerClient)
            .addConverterFactory(jsonConverter)
            .build()
            .create(PartnerApi::class.java)
    }
}