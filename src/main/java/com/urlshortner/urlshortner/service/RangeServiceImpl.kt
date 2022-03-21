package com.urlshortner.urlshortner.service

import com.urlshortner.urlshortner.model.CounterOperationResult
import com.urlshortner.urlshortner.model.OperationResult
import com.urlshortner.zooKeeper.ZooKeeperClient
import org.apache.zookeeper.KeeperException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.IOException
import java.lang.InterruptedException
import javax.annotation.PostConstruct
import kotlin.Throws

@Service
class RangeServiceImpl : RangeService {

    private var logger = LoggerFactory.getLogger(javaClass)

    @Value("\${zookeeper.host}:\${zookeeper.port}")
    private val host: String? = null

    @Value("\${zookeeper.znode.path.counter}")
    private val counterDataPath: String? = null

    @Value("\${zookeeper.range.limit}")
    private val limit: Int? = null

    private lateinit var client: ZooKeeperClient

    @Autowired
    lateinit var counterService: CounterService

    @Throws(InterruptedException::class, KeeperException::class, IOException::class)
    @PostConstruct
    private fun job() {
        client = ZooKeeperClient(host!!, counterDataPath!!, limit!!)
        when (val result = fetchAndInsertCounterRange()) {
            is OperationResult.Failure -> logger.info("Range initialisation failure: " + result.reason)
            is OperationResult.Success -> logger.info("Range initialisation success")
        }
    }

    override fun fetchAndInsertCounterRange(): OperationResult<Unit> {
        val lowerLimit = fetchRange()
        return when (val counterResult = counterService.insertCounterRange(lowerLimit, lowerLimit + limit!!)) {
            is CounterOperationResult.Success -> {
                OperationResult.Success(Unit)
            }
            is CounterOperationResult.Failure -> {
                OperationResult.Failure(counterResult.reason)
            }
            else -> {
                OperationResult.Failure("Emergency this should not be displayed")
            }
        }
    }

    override fun fetchRange(): Long {
        client = ZooKeeperClient(host!!, counterDataPath!!, limit!!)
        val range = client.rangeForClient.toLong()
        logger.info("\n\n Range : \n\n$range")
        return range
    }

    override fun fetchAndResetCounter(): OperationResult<Unit> {
        val lowerLimit = fetchRange()
        return when (val counterResult = counterService.resetCounter(lowerLimit, lowerLimit + limit!!)) {
            is CounterOperationResult.Success -> OperationResult.Success(Unit)
            is CounterOperationResult.Failure -> OperationResult.Failure(counterResult.reason)
            CounterOperationResult.RangeExhausted -> OperationResult.Failure("Emergency this should not be displayed")
        }
    }
}
