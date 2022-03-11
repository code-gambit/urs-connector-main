package com.urlshortner.urlshortner.Repository;

import com.urlshortner.urlshortner.Model.Counter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounterRepository extends CrudRepository<Counter, Long> {
}
