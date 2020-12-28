package com.online.helling.zaehler.dataaccess.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.online.helling.zaehler.dataaccess.model.Zaehler;

@Component("zRepository")

public interface ZaehlerRepository extends CacheRepository<Zaehler, Integer> {
	@Query(value = "from Zaehler z where z.zaehlerId= :zaehlerId")
	public Zaehler findOne(@Param("zaehlerId") Integer zaehlerId);

	@Query(value = "from Zaehler z where z.typ= :typ and aktiv=1")
	public Zaehler findOneByTyp(@Param("typ") Integer typ);
}