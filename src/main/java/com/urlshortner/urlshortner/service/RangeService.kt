package com.urlshortner.urlshortner.service

import com.urlshortner.urlshortner.model.CounterOperationResult
import com.urlshortner.urlshortner.model.OperationResult

interface RangeService {

    /**
     * Adds the counter value in db.
     * This method should be used when the service starts and gets the range for first time
     * @return [CounterOperationResult.FAILURE] or [CounterOperationResult.SUCCESS] respectively
     */
    fun fetchAndInsertCounterRange(): OperationResult<Unit>

    /**
     * Fetches the current counter value from zookeeper.
     * @return [Long] counter value to be used
     */
    fun fetchRange(): Long

    /**
     * Resets the counter value in db.
     * This method should be used once the range is exhausted.
     * @return [CounterOperationResult.FAILURE] or [CounterOperationResult.SUCCESS] respectively
     */
    fun fetchAndResetCounter(): OperationResult<Unit>
}
