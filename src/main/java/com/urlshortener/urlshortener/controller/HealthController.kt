package com.urlshortener.urlshortener.controller

import com.urlshortener.urlshortener.model.Counter
import com.urlshortener.urlshortener.model.UrlModel
import com.urlshortener.urlshortener.repository.CounterRepository
import com.urlshortener.urlshortener.repository.UrlRepository
import org.springframework.boot.SpringBootVersion
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.collections.HashMap

@RestController
class HealthController(
    private val counterRepository: CounterRepository,
    private val urlRepository: UrlRepository
) {

    private val dateFormat = SimpleDateFormat("EE MMM dd HH:mm:ss z(Z) yyyy")

    @GetMapping("/health")
    fun getHealth(): Map<String, Any> {
        return getBasicHealth()
    }

    @GetMapping("/system/health")
    fun getFullSystemHealth(): Map<String, Any> {
        val response = HashMap<String, Any>()
        try {
            val basicHealth = getBasicHealth()
            response.putAll(basicHealth)
        } catch (e: Exception) {
            response["status"] = "DOWN"
            response["info"] = "System down, unable to get health info."
            response["developerMessage"] = e.localizedMessage
        }
        try {
            performCounterOperation()
        } catch (e: Exception) {
            response["status"] = "DOWN"
            response["info"] = "Something wrong with counter service."
            response["developerMessage"] = e.localizedMessage
        }
        try {
            performUrlOperation()
        } catch (e: Exception) {
            response["status"] = "DOWN"
            response["info"] = "Something wrong with url service."
            response["developerMessage"] = e.localizedMessage
        }
        return response
    }

    private fun getBasicHealth(): Map<String, Any> {
        val response = HashMap<String, Any>()
        response["systemTime"] = dateFormat.format(Date())
        response["status"] = "UP"
        response["springBootVersion"] = SpringBootVersion.getVersion()
        return response
    }

    private fun performCounterOperation() {
        val counter = Counter(-1, -1, -1)
        counterRepository.save(counter)
        counterRepository.delete(counter)
    }

    private fun performUrlOperation() {
        val url = UrlModel("-1", "-1", 0)
        urlRepository.save(url)
        urlRepository.delete(url)
    }
}
