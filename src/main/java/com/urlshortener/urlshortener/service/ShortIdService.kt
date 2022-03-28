package com.urlshortener.urlshortener.service

import com.urlshortener.urlshortener.model.OperationResult

interface ShortIdService {

    /**
     * Generates the short id
     * @return [OperationResult] containing string data as short id for success
     */
    fun getShortId(): OperationResult<String>
}
