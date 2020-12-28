package com.online.helling.zaehler.dataaccess.repository;

import java.io.Serializable;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

public interface CacheRepository<T,ID extends Serializable> extends CrudRepository<T, ID> {

    @Cacheable("cacheName")
    T findOne(ID id);

    @Cacheable("cacheName")
    @CacheEvict(value = "cacheName", allEntries = true)
    Iterable<T> findAll();

    @Override
    @CachePut("cacheName")
    <S extends T> S save(S entity);

    @Override
    @CacheEvict("cacheName")
    void delete(T entity); 
}