package com.example.tryingtomakefetchhappen.datastate

/**
 * A standardized way to convert a [Result] from a Repository (Model layer) to a [DataState]
 * (ViewModel/View layer).
 */
abstract class DataStateTransformer<T, S> {
    /**
     * Copy the given DataState with updates from the most recent [Result] from a repository.
     * If we were previously loading fresh data, we will set isLoading to false once data
     * arrives, making the assumption that it is data from a server and not cached data.
     * If the [Result] contains data, we will update the data. If it does not (which can
     * happen when there is an error) we will continue to use the previous data.
     * If the [Result] contains an error, we will update the error. Otherwise, we will assume
     * any previous error is no longer applicable and will set the error to null.
     */
    fun updateFromResult(dataState: DataState<T>, result: Result<S>): DataState<T> {
        val newDataPresent = result.getOrNull() != null
        return dataState.copy(
            isLoading = if (newDataPresent) false else dataState.isLoading,
            data = result.getOrNull()?.let { transformData(it) } ?: dataState.data,
            error = transformError(result.exceptionOrNull())
        )
    }

    abstract fun transformData(sourceData: S): T?

    open fun transformError(error: Throwable?): Throwable? {
        return error
    }
}

/**
 * A simplified version of [DataStateTransformer] for when the source data is the same as the
 * destination data. This will still use functionality for deciding when to update the properties
 * of the [DataState].
 */
open class SimpleDataStateTransformer<T> : DataStateTransformer<T, T>() {
    override fun transformData(sourceData: T): T? {
        return sourceData
    }
}

data class DataState<T>(
    val isLoading: Boolean = true,
    val data: T? = null,
    val error: Throwable? = null
)