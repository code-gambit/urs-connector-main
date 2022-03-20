package com.urlshortner.urlshortner.service

import com.urlshortner.urlshortner.model.Counter
import com.urlshortner.urlshortner.model.CounterOperationResult
import com.urlshortner.urlshortner.repository.CounterRepository
import com.urlshortner.zooKeeper.ZooKeeperClient
import kotlin.Throws
import java.lang.InterruptedException
import org.apache.zookeeper.KeeperException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.IOException
import javax.annotation.PostConstruct
import kotlin.Exception

@Service
class RangeServiceImpl(private val counterRepository: CounterRepository): RangeService {

    private var logger = LoggerFactory.getLogger(javaClass)

    @Value("\${zookeeper.host}:\${zookeeper.port}")
    private val host: String? = null

    @Value("\${zookeeper.counterDataPath}")
    private val counterDataPath: String? = null

    @Value("\${zookeeper.range.limit}")
    private val limit: Int? = null

    private var client: ZooKeeperClient? = null

    @Autowired
    var counterService: CounterService? = null

    @Throws(InterruptedException::class, KeeperException::class, IOException::class)
    @PostConstruct
    private fun job() {
        logger.info("Host: $host")
        client = ZooKeeperClient(host!!, counterDataPath!!, limit!!)
        logger.info("\nZooKeeperClient object created\n")
    }

    override fun insertCounterRange(): CounterOperationResult<Unit> {
        if (counterRepository.existsById(Counter.ID)) {
            // making sure counter is inserted only once in a db
            return CounterOperationResult.Failure(
                "Counter already in the db. Use reset function for updating the range")
        }
        return try {
            val rangeStart= counterPathValue()
            val counter = Counter(rangeStart, rangeStart+limit!!)
            counterRepository.save(counter)
            CounterOperationResult.Success(Unit)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            CounterOperationResult.Failure(
                e.localizedMessage)
        }
    }

    override fun counterPathValue(): Long{
        client = ZooKeeperClient(host!!, counterDataPath!!, limit!!)
        val range = client!!.rangeForClient.toLong()
        logger.info("\n\n Range : \n\n$range")
        return range
    }

    override fun resetCounter(): CounterOperationResult<Unit> {
        val lowerLimit=counterPathValue()
        val counter = Counter(lowerLimit,lowerLimit+limit!!)
        return updateCounter(counter)
    }

    private fun updateCounter(counter: Counter): CounterOperationResult<Unit> {
        return try {
            counterRepository.save(counter)
            CounterOperationResult.Success(Unit)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            CounterOperationResult.Failure(e.localizedMessage)
        }
    }
}
