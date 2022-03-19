package com.urlshortner.urlshortner.controller

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
        return if (result!!.isFailed) {
            result.failureData
        } else "Success"
    }

    @get:GetMapping("get")
    val counterValue: String?
        get() {
            val result = counterService!!.counterValue
            if (result!!.isFailed) {
                return result.failureData
            } else if (result.isRangeExhausted) {
                return "Reset: " + resetCounterValue()
            }
            return "Counter Value: " + result.successData
        }

    @GetMapping("reset")
    fun resetCounterValue(): String? {
        val result = counterService!!.resetCounter(6, 10)
        return if (result!!.isFailed) {
            result.failureData
        } else "Success"
    }
}
