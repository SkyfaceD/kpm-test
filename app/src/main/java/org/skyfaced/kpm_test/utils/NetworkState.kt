package org.skyfaced.kpm_test.utils

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber

/**
 * https://developer.android.com/training/basics/network-ops/reading-network-state#listening-events
 */
class NetworkState(manager: ConnectivityManager) {
    private var _hasMobilNetwork = false
    val hasMobilNetwork get() = _hasMobilNetwork

    /**
     * @return true if connection available or network changed, false if connection lost
     */
    @ExperimentalCoroutinesApi
    val flow = callbackFlow {
        val networkStatusCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Timber.d("Network connection available")
                _hasMobilNetwork = true
                trySend(hasMobilNetwork)
            }

            override fun onLost(network: Network) {
                Timber.d("Network connection lost")
                _hasMobilNetwork = false
                trySend(hasMobilNetwork)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities,
            ) {
                Timber.d("Network capability changed $networkCapabilities")
                _hasMobilNetwork = networkCapabilities.hasInternet()
                trySend(hasMobilNetwork)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            manager.registerDefaultNetworkCallback(networkStatusCallback)
        } else {
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()

            manager.registerNetworkCallback(request, networkStatusCallback)
        }

        awaitClose {
            manager.unregisterNetworkCallback(networkStatusCallback)
        }
    }.distinctUntilChanged()

    //@formatter:off
    private fun NetworkCapabilities.hasInternet() =
        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
        hasTransport(NetworkCapabilities.TRANSPORT_VPN)
    //@formatter:on
}