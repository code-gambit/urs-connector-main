package com.urlshortner.urlshortner.service

import com.urlshortner.urlshortner.repository.CounterRepository
import com.urlshortner.urlshortner.exception.RangeExhaustedException
import com.urlshortner.urlshortner.model.Counter
import com.urlshortner.urlshortner.model.CounterOperationResult
import org.springframework.stereotype.Service
import java.lang.Exception
import java.lang.NullPointerException
import java.util.*

@Service
class CounterServiceImpl(private val counterRepository: CounterRepository) : CounterService {

    override fun insertCounterRange(lowerLimit: Long, upperLimit: Long): CounterOperationResult<Unit> {
        if (counterRepository.existsById(Counter.ID)) {
            // making sure counter is inserted only once in a db
            return CounterOperationResult.Failure(
                "Counter already in the db. Use reset function for updating the range")
        }

        return try {
            val counter = Counter(lowerLimit, upperLimit)
            counterRepository.save(counter)
            CounterOperationResult.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            CounterOperationResult.Failure(
                e.localizedMessage)
        }
    }

    override val counterValue: CounterOperationResult<Long>
        get() {
            return try {
                val counter = counter
                val value = counter!!.currentValue
                counter.increment()
                val result = updateCounter(counter)
                if (result is CounterOperationResult.Failure) {
                    CounterOperationResult.Failure(
                        "Not able to update counter value in db")
                }
                CounterOperationResult.Success(value)
            } catch (rangeExhaustedException: RangeExhaustedException) {
                CounterOperationResult.RangeExhausted
            } catch (e: Exception) {
                e.printStackTrace()
                CounterOperationResult.Failure(e.localizedMessage)
            }
        }

    override fun resetCounter(lowerLimit: Long, upperLimit: Long): CounterOperationResult<Unit> {
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

    private fun updateCounter(counter: Counter): CounterOperationResult<Unit> {
        return try {
            counterRepository.save(counter)
            CounterOperationResult.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            CounterOperationResult.Failure(e.localizedMessage)
        }
    }
}
