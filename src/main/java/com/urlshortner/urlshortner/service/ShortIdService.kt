package com.urlshortner.urlshortner.service

import com.urlshortner.urlshortner.model.OperationResult

interface ShortIdService {

    /**
     * Generates the short id
     * @return [OperationResult] containing string data as short id for success
     */
    fun getShortId(): OperationResult<String>
}
