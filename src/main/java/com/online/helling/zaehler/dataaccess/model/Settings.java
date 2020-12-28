package com.online.helling.zaehler.dataaccess.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.stereotype.Component;

/**
 * The persistent class for the settings database table.
 * 
 */
@Component
@Entity
@Table(name = "settings")
public class Settings implements Serializable {

	private static final long serialVersionUID = -4839755648200575300L;

	@Id
	private String benutzer;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Date createdAt;

	private String settings;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	private Date updatedAt;

	public Settings() {
		super();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Settings other = (Settings) obj;
		if (benutzer == null) {
			if (other.benutzer != null)
				return false;
		} else if (!benutzer.equals(other.benutzer))
			return false;
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.equals(other.createdAt))
			return false;
		if (settings == null) {
			if (other.settings != null)
				return false;
		} else if (!settings.equals(other.settings))
			return false;
		if (updatedAt == null) {
			if (other.updatedAt != null)
				return false;
		} else if (!updatedAt.equals(other.updatedAt))
			return false;
		return true;
	}

	public String getBenutzer() {
		return benutzer;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public String getSettings() {
		return settings;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((benutzer == null) ? 0 : benutzer.hashCode());
		result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result + ((settings == null) ? 0 : settings.hashCode());
		result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
		return result;
	}

	public void setBenutzer(String benutzer) {
		this.benutzer = benutzer;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public void setSettings(String settings) {
		this.settings = settings;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "Settings [benutzer=" + benutzer + ", createdAt=" + createdAt + ", settings=" + settings + ", updatedAt="
				+ updatedAt + "]";
	}

}