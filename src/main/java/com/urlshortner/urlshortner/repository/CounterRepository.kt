package com.urlshortner.urlshortner.repository

import com.urlshortner.urlshortner.model.Counter
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

interface CounterRepository : CrudRepository<Counter, Int>
