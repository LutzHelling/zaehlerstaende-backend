package com.online.helling.zaehler.dataaccess.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.online.helling.zaehler.dataaccess.model.Zaehlerstand;

public interface ZaehlerstandRepository extends CacheRepository<Zaehlerstand, Integer> {

	@Query(value="from Zaehlerstand zs where zs.id= :id")
	public Zaehlerstand findOne(@Param("id") Integer id);
}