package com.urlshortner.urlshortner.Service;

import com.urlshortner.urlshortner.Exception.RangeExhaustedException;
import com.urlshortner.urlshortner.Model.Counter;
import com.urlshortner.urlshortner.Model.OperationResult;
import com.urlshortner.urlshortner.Repository.CounterRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CounterServiceImpl implements CounterService {

    private final CounterRepository counterRepository;

    public CounterServiceImpl(CounterRepository counterRepository) {
        this.counterRepository = counterRepository;
    }

    @Override
    public OperationResult<Void, String> insertCounterRange(long lowerLimit, long upperLimit) {
        if (counterRepository.existsById(Counter.ID)) {
            // making sure counter is inserted only once in a db
            return OperationResult.FAILURE("Counter already in the db. Use reset function for updating the range");
        }
        Counter counter = new Counter.Builder()
                .lowerLimit(lowerLimit)
                .upperLimit(upperLimit)
                .build();
        try {
            counterRepository.save(counter);
        } catch (Exception e) {
            e.printStackTrace();
            return OperationResult.FAILURE(e.getLocalizedMessage());
        }
        return OperationResult.SUCCESS(null);
    }

    @Override
    public OperationResult<Long, String> getCounterValue() {
        long value;
        try {
            Counter counter = getCounter();
            value = counter.getCurrentValue();
            counter.increment();
            OperationResult<Void, String> result = updateCounter(counter);
            if(result.isFailed()) {
                return null;
            }
        } catch (RangeExhaustedException rangeExhaustedException) {
            return OperationResult.RANGE_EXHAUSTED();
        }
        catch (Exception e) {
            e.printStackTrace();
            return OperationResult.FAILURE(e.getLocalizedMessage());
        }
        return OperationResult.SUCCESS(value);
    }

    @Override
    public OperationResult<Void, String> resetCounter(long lowerLimit, long upperLimit) {
        Counter counter = new Counter.Builder()
                .lowerLimit(lowerLimit)
                .upperLimit(upperLimit)
                .build();
        OperationResult<Void, String> result = updateCounter(counter);
        return result;
    }

    private Counter getCounter() {
        Optional<Counter> counter;
        try {
            counter = counterRepository.findById(Counter.ID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if(counter.isEmpty()) {
            throw new NullPointerException("" + Counter.ID + " doesn't exist in database");
        }
        return counter.get();
    }

    private OperationResult<Void, String> updateCounter(Counter counter) {
        try {
            counterRepository.save(counter);
        }  catch (Exception e) {
            e.printStackTrace();
            return OperationResult.FAILURE(e.getLocalizedMessage());
        }
        return OperationResult.SUCCESS(null);
    }


}
