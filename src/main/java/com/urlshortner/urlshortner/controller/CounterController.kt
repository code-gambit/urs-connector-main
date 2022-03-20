package com.urlshortner.urlshortner.controller

import com.urlshortner.urlshortner.model.CounterOperationResult
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.beans.factory.annotation.Autowired
import com.urlshortner.urlshortner.service.CounterService
import org.springframework.web.bind.annotation.GetMapping

@RestController
@RequestMapping(path = ["test"])
class CounterController {

    @Autowired
    var counterService: CounterService? = null

    @GetMapping("")
    fun test(): String? {
        val result = counterService!!.insertCounterRange(1, 5)
        return if (result is CounterOperationResult.Failure) {
            result.reason
        } else "Success"
    }

    @get:GetMapping("get")
    val counterValue: String?
        get() {
            return when (val result = counterService!!.counterValue) {
                is CounterOperationResult.Failure -> {
                    result.reason
                }
                is CounterOperationResult.RangeExhausted -> {
                    "Reset: " + resetCounterValue()
                }
                else -> {
                    "Counter Value: " + (result as CounterOperationResult.Success).data
                }
            }
        }

    @GetMapping("reset")
    fun resetCounterValue(): String? {
        val result = counterService!!.resetCounter(6, 10)
        return if (result is CounterOperationResult.Failure) {
            result.reason
        } else "Success"
    }
}
