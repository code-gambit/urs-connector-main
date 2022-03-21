package com.urlshortner.urlshortner.controller

import com.urlshortner.urlshortner.model.CounterOperationResult
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.beans.factory.annotation.Autowired
import com.urlshortner.urlshortner.service.CounterService
import com.urlshortner.urlshortner.service.RangeService
import org.springframework.web.bind.annotation.GetMapping

@RestController
@RequestMapping(path = ["test"])
class CounterController {

    @Autowired
    var counterService: CounterService? = null

    @Autowired
    var rangeService: RangeService? =null

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

    fun resetCounterValue(): String? {
        val result = rangeService!!.fetchAndResetCounter()
        return if (result is CounterOperationResult.Failure) {
            result.reason
        } else "Success"
    }
}
