package com.urlshortner.urlshortner.Model;

import com.urlshortner.urlshortner.Exception.RangeExhaustedException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "COUNTER")
public class Counter {

    public static long ID = 1001;

    @Id
    long id;

    @Column(name = "LOWER_LIMIT")
    long lowerLimit;

    @Column(name = "UPPER_LIMIT")
    long upperLimit;

    @Column(name = "CURRENT_VALUE")
    long currentValue;

    public Counter() {
    }

    public Counter(long id, long lowerLimit, long upperLimit, long currentValue) {
        this.id = id;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.currentValue = currentValue;
    }

    public Counter(long id, long lowerLimit, long upperLimit) {
        this.id = id;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.currentValue = lowerLimit;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(long lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public long getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(long upperLimit) {
        this.upperLimit = upperLimit;
    }

    // make sure the error is handled gracefully when fetching the current value
    public long getCurrentValue() throws RangeExhaustedException {
        if (currentValue > upperLimit) {
            throw new RangeExhaustedException();
        }
        return currentValue;
    }

    public void setCurrentValue(long currentValue) {
        this.currentValue = currentValue;
    }

    public void increment() {
        currentValue++;
    }

    public static class Builder {
        Long lowerLimit;
        Long upperLimit;

        public Builder lowerLimit(long limit) {
            this.lowerLimit = limit;
            return this;
        }

        public Builder upperLimit(long limit) {
            this.upperLimit = limit;
            return this;
        }

        public Counter build() {
            if(lowerLimit == null) {
                throw new NullPointerException("Lower limit value cant be null");
            }
            if(upperLimit == null) {
                throw new NullPointerException("Upper limit value cant be null");
            }
            Counter counter = new Counter((int) Counter.ID, lowerLimit, upperLimit);
            return counter;
        }
    }

}
