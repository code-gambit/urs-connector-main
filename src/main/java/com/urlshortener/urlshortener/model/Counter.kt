package com.urlshortener.urlshortener.model

import com.urlshortener.urlshortener.exception.RangeExhaustedException
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "COUNTER")
open class Counter(
    @Column(name = "LOWER_LIMIT")
    open var lowerLimit: Long,

    @Column(name = "UPPER_LIMIT")
    open var upperLimit: Long,

    @Id
    open val id: Int = ID
) {

    @Column(name = "CURRENT_VALUE")
    open var currentValue: Long = lowerLimit
        get() {
            return if (field <= upperLimit) {
                field
            } else {
                throw RangeExhaustedException()
            }
        }

    fun increment() {
        currentValue++
    }

    companion object { val ID = 1001; }
}
