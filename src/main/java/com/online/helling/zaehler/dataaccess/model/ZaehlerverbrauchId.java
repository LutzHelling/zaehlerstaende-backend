package com.online.helling.zaehler.dataaccess.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class ZaehlerverbrauchId implements Serializable {

	private static final long serialVersionUID = 8536087915144154300L;

	@Column(name = "typ")
	@NotNull
	@Basic(optional = false)
	private int typ;

	@Column(name = "akt_date")
	@Temporal(TemporalType.DATE)
	@Size(max = 10)
	private Date aktDate;

	public void setTyp(int typ) {
		this.typ = typ;
	}

	public int getTyp() {
		return typ;
	}

	public Date getAktDate() {
		return aktDate;
	}

	public void setAktDate(Date aktDate) {
		this.aktDate = aktDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aktDate == null) ? 0 : aktDate.hashCode());
		result = prime * result + typ;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ZaehlerverbrauchId other = (ZaehlerverbrauchId) obj;
		if (aktDate == null) {
			if (other.aktDate != null)
				return false;
		} else if (!aktDate.equals(other.aktDate))
			return false;
		if (typ != other.typ)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ZaehlerverbrauchId [typ=" + typ + ", aktDate=" + aktDate + "]";
	}

}