package com.online.helling.zaehler.dataaccess.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.online.helling.zaehler.dataaccess.model.Jahresstand;
import com.online.helling.zaehler.dataaccess.model.Zaehlerverbrauch;
import com.online.helling.zaehler.dataaccess.model.ZaehlerverbrauchId;

public interface ZaehlerverbrauchRepository extends CacheRepository<Zaehlerverbrauch, ZaehlerverbrauchId> {

	@Query(value="SELECT SUM(zv.verbrauch) FROM Zaehlerverbrauch zv WHERE zv.id.typ=:medium AND zv.jahrAktuell=:year ORDER BY monatAktuell")
	public BigDecimal sumVerbrauchByMediumAndJahr(@Param("medium") Integer medium, @Param("year") Integer year);

	@Query(value = "SELECT zv from Zaehlerverbrauch zv WHERE zv.id.typ=:typ and zv.aktMonatNumeric>=:startBezug and zv.aktMonatNumeric<=:endeBezug")
	public List<Zaehlerverbrauch> findAllByMediumImZeitraum(@Param("typ") Integer typ,
			@Param("startBezug") Integer startBezug, @Param("endeBezug") Integer endeBezug);

	public Iterable<Zaehlerverbrauch> findAllByOrderByAktMonatNumericDesc();

	@Query(value = "SELECT js FROM jahresstaende js WHERE js.id.typ=:medium AND js.id.jahr > 2004 ORDER BY js.id.jahr")
	public List<Jahresstand> findAllJahresstaende(@Param("medium") Integer medium);

	@Query(value = "SELECT max(zv.aktMonatNumeric) from Zaehlerverbrauch zv WHERE zv.id.typ=:typ and zv.aktMonatNumeric<:bezug")
	public Integer findBezugBefore(@Param("typ") Integer typ, @Param("bezug") Integer bezug);

	@Query(value = "SELECT max(zv.aktMonatNumeric) from Zaehlerverbrauch zv WHERE zv.id.typ=:typ and zv.aktMonatNumeric>=:bezug")
	public Integer findJuengstenBezugEqualOrAfter(@Param("typ") Integer typ, @Param("bezug") Integer bezug);

	@Query(value = "SELECT zv from Zaehlerverbrauch zv WHERE zv.id.typ=:typ and zv.aktMonatNumeric=(SELECT MAX(zvi.aktMonatNumeric) FROM Zaehlerverbrauch zvi WHERE zvi.id.typ=:typ and zvi.aktMonatNumeric<:bezug)")
	public Zaehlerverbrauch findNearestVerbrauchBefore(@Param("typ") Integer typ, @Param("bezug") Integer bezug);

	@Query(value = "SELECT zv from Zaehlerverbrauch zv WHERE zv.id.typ=:typ and zv.aktMonatNumeric=(SELECT MAX(zvi.aktMonatNumeric) FROM Zaehlerverbrauch zvi WHERE zvi.id.typ=:typ and zvi.aktMonatNumeric<=:bezug)")
	public Zaehlerverbrauch findNearestVerbrauchBeforeOrEqual(@Param("typ") Integer typ, @Param("bezug") Integer bezug);

	@Query(value = "SELECT zv from Zaehlerverbrauch zv WHERE zv.id.typ=:typ and zv.aktMonatNumeric=:bezug")
	public Zaehlerverbrauch findNearestVerbrauchEqualOrAfter(@Param("typ") Integer typ, @Param("bezug") Integer bezug);

}
