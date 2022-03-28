package com.urlshortener.urlshortener.repository

import com.urlshortener.urlshortener.model.Counter
import org.springframework.data.repository.CrudRepository

interface CounterRepository : CrudRepository<Counter, Int>
