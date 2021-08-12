package org.skyfaced.kpm_test.utils

/**
 * https://stackoverflow.com/questions/62271385/okhttpinterceptor-navigating-from-kotlin-interceptor-to-login-fragment
 */
object GlobalNavigator {
    private var handler: GlobalNavigationHandler? = null

    fun registerHandler(handler: GlobalNavigationHandler) {
        this.handler = handler
    }

    fun unregisterHandler() {
        handler = null
    }

    fun logout(why: String) {
        handler?.logout(why)
    }
}

//TODO Pass enum and control error message in activity
interface GlobalNavigationHandler {
    fun logout(why: String)
}