package org.skyfaced.kpm_test.utils

fun <T> resultSuccess(data: T): Result.Success<T> {
    return Result.Success(data)
}

fun resultError(
    message: String? = null,
    t: Throwable? = null
): Result.Error {
    return Result.Error(message, t)
}

fun resultLoading() = Result.Loading

fun <T> resultHandler(data: T?): Result<T> = try {
    resultSuccess(data!!)
} catch (e: Exception) {
    resultError(e.message, e)
}