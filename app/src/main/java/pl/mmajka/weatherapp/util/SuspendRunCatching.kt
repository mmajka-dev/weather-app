package pl.mmajka.weatherapp.util

import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <R> suspendRunCatching(crossinline block: suspend () -> R): Result<R> =
    try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }