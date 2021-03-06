package com.urlshortener.urlshortener.service

import com.urlshortener.urlshortener.model.CounterOperationResult
import com.urlshortener.urlshortener.model.OperationResult
import com.urlshortener.urlshortener.utility.ExtensionFunction.toBase62
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ShortIdServiceImpl : ShortIdService {

    @Autowired
    lateinit var counterService: CounterService

    @Autowired
    lateinit var rangeService: RangeService

    override fun getShortId(): OperationResult<String> {
        return when (val result = counterService.counterValue) {
            is CounterOperationResult.Failure -> {
                OperationResult.Failure(result.reason)
            }
            is CounterOperationResult.RangeExhausted -> {
                when (val resetResult = resetCounterValue()) {
                    is OperationResult.Success -> {
                        getShortId()
                    }
                    is OperationResult.Failure -> {
                        resetResult
                    }
                }
            }
            is CounterOperationResult.Success -> {
                val shortId = result.data.toBase62()
                OperationResult.Success(shortId)
            }
        }
    }

    fun resetCounterValue(): OperationResult<Unit> {
        val result = rangeService.fetchAndResetCounter()
        return if (result is OperationResult.Failure) {
            result
        } else OperationResult.Success(Unit)
    }
}
