package com.urlshortner.urlshortner.service

import com.urlshortner.urlshortner.repository.CounterRepository
import com.urlshortner.urlshortner.model.OperationResult
import com.urlshortner.urlshortner.exception.RangeExhaustedException
import com.urlshortner.urlshortner.model.Counter
import org.springframework.stereotype.Service
import java.lang.Exception
import java.lang.NullPointerException
import java.util.*

@Service
class CounterServiceImpl(private val counterRepository: CounterRepository) : CounterService {

    override fun insertCounterRange(lowerLimit: Long, upperLimit: Long): OperationResult<Void?, String?>? {
        if (counterRepository.existsById(Counter.ID)) {
            // making sure counter is inserted only once in a db
            return OperationResult.FAILURE("Counter already in the db. Use reset function for updating the range")
        }
        val counter = Counter(lowerLimit, upperLimit)
        try {
            counterRepository.save(counter)
        } catch (e: Exception) {
            e.printStackTrace()
            return OperationResult.FAILURE(e.localizedMessage)
        }
        return OperationResult.SUCCESS(null)
    }

    override val counterValue: OperationResult<Long?, String?>?
        get() {
            val value: Long
            try {
                val counter = counter
                value = counter!!.currentValue
                counter.increment()
                val result = updateCounter(counter)
                if (result.isFailed) {
                    return null
                }
            } catch (rangeExhaustedException: RangeExhaustedException) {
                return OperationResult.RANGE_EXHAUSTED()
            } catch (e: Exception) {
                e.printStackTrace()
                return OperationResult.FAILURE(e.localizedMessage)
            }
            return OperationResult.SUCCESS(value)
        }

    override fun resetCounter(lowerLimit: Long, upperLimit: Long): OperationResult<Void?, String?> {
        val counter = Counter(lowerLimit,upperLimit)
        return updateCounter(counter)
    }

    private val counter: Counter?
        get() {
            val counter: Optional<Counter?> = try {
                counterRepository.findById(Counter.ID)
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
            if (counter.isEmpty) {
                throw NullPointerException("" + Counter.ID + " doesn't exist in database")
            }
            return counter.get()
        }

    private fun updateCounter(counter: Counter): OperationResult<Void?, String?> {
        try {
            counterRepository.save(counter)
        } catch (e: Exception) {
            e.printStackTrace()
            return OperationResult.FAILURE(e.localizedMessage)
        }
        return OperationResult.SUCCESS(null)
    }
}
