package org.skyfaced.kpm_test.startup

import android.content.Context
import androidx.startup.Initializer
import coil.Coil
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.util.CoilUtils
import okhttp3.OkHttpClient
import timber.log.Timber

class CoilInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        val cacheClient = OkHttpClient.Builder()
            .cache(CoilUtils.createDefaultCache(context))
            .build()
        val imageLoader = ImageLoader.Builder(context)
            .crossfade(true)
            .componentRegistry { add(SvgDecoder(context)) }
            .okHttpClient { cacheClient }
            .build()
        Coil.setImageLoader(imageLoader)

        Timber.d("Coil initialized")
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(TimberInitializer::class.java)
    }
}