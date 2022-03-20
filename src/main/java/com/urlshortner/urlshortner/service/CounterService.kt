package com.urlshortner.urlshortner.service

import com.urlshortner.urlshortner.model.CounterOperationResult

interface CounterService {

    /**
     * Fetches the current counter value from db and updates the db.
     * This method only returns value when entire transaction is success else null
     * @return [Long] counter value to be used
     */
    val counterValue: CounterOperationResult<Long>
}
