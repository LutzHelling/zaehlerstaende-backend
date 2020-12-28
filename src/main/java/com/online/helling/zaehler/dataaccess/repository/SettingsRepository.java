package com.online.helling.zaehler.dataaccess.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.online.helling.zaehler.dataaccess.model.Settings;

@Component("settingsRepository")
public interface SettingsRepository extends CacheRepository<Settings, String> {
	@Query(value = "from Settings s where s.benutzer= :benutzer")
	public Settings findOne(@Param("benutzer") String benutzer);
}