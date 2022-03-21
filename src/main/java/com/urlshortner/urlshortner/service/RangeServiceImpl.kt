package com.urlshortner.urlshortner.service


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

@Service
class RangeServiceImpl(private val counterRepository: CounterRepository): RangeService {

    private var logger = LoggerFactory.getLogger(javaClass)

    @Value("\${zookeeper.host}:\${zookeeper.port}")
    private val host: String? = null

    @Value("\${zookeeper.znode.path.counter}")
    private val counterDataPath: String? = null

    @Value("\${zookeeper.range.limit}")
    private val limit: Int? = null

    private var client: ZooKeeperClient? = null

    @Autowired
    var counterService: CounterService? = null

    @Throws(InterruptedException::class, KeeperException::class, IOException::class)
    @PostConstruct
    private fun job() {
        client = ZooKeeperClient(host!!, counterDataPath!!, limit!!)
        val result = fetchAndInsertCounterRange()
        if(result is CounterOperationResult.Success ) {
            logger.info("Range initialisation success")
        } else {
            logger.info("Range initialisation failure: " + (result as CounterOperationResult.Failure).reason)
        }
    }

    override fun fetchAndInsertCounterRange(): CounterOperationResult<Unit> {
        val lowerLimit = counterPathValue();
        return counterService!!.insertCounterRange(lowerLimit, lowerLimit + limit!!)
    }

    override fun counterPathValue(): Long{
        client = ZooKeeperClient(host!!, counterDataPath!!, limit!!)
        val range = client!!.rangeForClient.toLong()
        logger.info("\n\n Range : \n\n$range")
        return range
    }

    override fun fetchAndResetCounter(): CounterOperationResult<Unit> {
        val lowerLimit=counterPathValue()
        return counterService!!.resetCounter(lowerLimit,lowerLimit+limit!!)
    }

}
