package com.urlshortner.urlshortner.service

import com.urlshortner.urlshortner.model.CounterOperationResult

interface RangeService {
    /**
     * Adds the counter value in db.
     * This method should be used when the service starts and gets the range for first time
     * @return [CounterOperationResult.FAILURE] or [CounterOperationResult.SUCCESS] respectively
     */

    fun insertCounterRange(): CounterOperationResult<Unit>

    /**
     * Fetches the current counter value from zookeeper.
     * @return [Long] counter value to be used
     */
    fun counterPathValue(): Long

    /**
     * Resets the counter value in db.
     * This method should be used once the range is exhausted.
     * @param lowerLimit start limit of the range ex: 900000001
     * @param upperLimit end limit of the range ex: 1000000000
     * @return [CounterOperationResult.FAILURE] or [CounterOperationResult.SUCCESS] respectively
     */
    fun resetCounter(): CounterOperationResult<Unit>
}