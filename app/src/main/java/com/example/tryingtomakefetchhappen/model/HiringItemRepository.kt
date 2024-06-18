package com.example.tryingtomakefetchhappen.model

import com.example.tryingtomakefetchhappen.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.onSubscription
import retrofit2.HttpException
import java.time.Clock
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HiringItemRepository @Inject constructor(
    private val clock: Clock,
) {

    private var timeOfLastRefresh = 0L

    private val _hiringItemsResult =
        MutableStateFlow<Result<List<HiringItem>>>(Result.success(emptyList()))
    val hiringItemsResult: SharedFlow<Result<List<HiringItem>>>
        get() = _hiringItemsResult.onSubscription {
            // Whenever there's a new subscriber (typically ViewModels), see if we need fresh data.
            fetchHiringItemsIfNecessary()
        }

    /**
     * Refresh the data from the server. Useful when user manually requests a refresh.
     */
    suspend fun forceRefresh() {
        fetchHiringItems()
    }

    /**
     * Load data from the server if we have no cached data, or if the cached data is expired.
     */
    private suspend fun fetchHiringItemsIfNecessary() {
        val noCachedData = _hiringItemsResult.value.getOrNull().isNullOrEmpty()
        val cachedDataExpired = timeOfLastRefresh + TTL_MILLIS < currentMillis()
        if (noCachedData || cachedDataExpired) {
            fetchHiringItems()
        }
    }

    private suspend fun fetchHiringItems() {
        val result = try {
            val hiringItems = RetrofitClient.hiringApi.getHiringItems()
            timeOfLastRefresh = currentMillis()
            Result.success(onlyValidItemsIn(hiringItems))
        } catch (e: HttpException) {
            Result.failure(e)
        }
        _hiringItemsResult.value = result
    }

    private fun onlyValidItemsIn(items: List<HiringItem>): List<HiringItem> {
        return items.filter { !it.name.isNullOrBlank() }
    }

    private fun currentMillis() = clock.millis()

    companion object {
        private val TTL_MILLIS = TimeUnit.HOURS.toMillis(1)
    }
}