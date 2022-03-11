package com.urlshortner.urlshortner.Service;

import com.urlshortner.urlshortner.Model.OperationResult;

public interface CounterService {

    /**
     * Adds the counter value in db.
     * This method should be used when the service starts and gets the range for first time
     * @param lowerLimit start limit of the range ex: 100000000
     * @param upperLimit end limit of the range ex: 900000000
     * @return [CounterOperationResult.FAILURE] or [CounterOperationResult.SUCCESS] respectively
     */
    OperationResult<Void, String> insertCounterRange(long lowerLimit, long upperLimit);

    /**
     * Fetches the current counter value from db and updates the db.
     * This method only returns value when entire transaction is success else null
     * @return [Long] counter value to be used
     */
    OperationResult<Long, String> getCounterValue();

    /**
     * Resets the counter value in db.
     * This method should be used once the range is exhausted.
     * @param lowerLimit start limit of the range ex: 900000001
     * @param upperLimit end limit of the range ex: 1000000000
     * @return [CounterOperationResult.FAILURE] or [CounterOperationResult.SUCCESS] respectively
     */
    OperationResult<Void, String> resetCounter(long lowerLimit, long upperLimit);

}
