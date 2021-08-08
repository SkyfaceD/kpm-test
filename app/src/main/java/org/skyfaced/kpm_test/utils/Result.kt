package org.skyfaced.kpm_test.utils

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()

    data class Error(
        val message: String?,
        val stackTrace: Throwable?
    ) : Result<Nothing>()

    object Loading : Result<Nothing>()
}
