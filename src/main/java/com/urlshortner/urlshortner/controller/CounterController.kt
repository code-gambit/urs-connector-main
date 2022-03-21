package com.urlshortner.urlshortner.controller

import com.urlshortner.urlshortner.model.OperationResult
import com.urlshortner.urlshortner.service.ShortIdService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["test"])
class CounterController {

    @Autowired
    lateinit var shortIdService: ShortIdService

    @get:GetMapping("get")
    val counterValue: String?
        get() {
            return when (val result = shortIdService.getShortId()) {
                is OperationResult.Failure -> result.reason
                is OperationResult.Success -> result.data
            }
        }
}
