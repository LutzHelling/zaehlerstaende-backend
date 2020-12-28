package com.online.helling.zaehler.dataaccess.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.stereotype.Component;

/**
 * The persistent class for the zaehlerstand database table.
 * 
 */
@Component
@Entity
@Table(name = "zaehlerstand")
@NamedQuery(name = "Zaehlerstand.findAll", query = "SELECT z FROM Zaehlerstand z")
public class Zaehlerstand implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", updatable = false, nullable = false)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateneinreichung;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", updatable = false, nullable = true)
	private Date updatedAt;

	@Column(name = "zaehlerstand")
	private BigDecimal stand;

	private Integer typ;

	// bi-directional many-to-one association to Zaehler
	@ManyToOne
	@JoinColumn(name = "zaehler_id", referencedColumnName = "zaehler_id")
	private Zaehler zaehler;

	public Integer getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getDateneinreichung() {
		return this.dateneinreichung;
	}

	public void setDateneinreichung(Date dateneinreichung) {
		this.dateneinreichung = dateneinreichung;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public BigDecimal getStand() {
		return this.stand;
	}

	public void setStand(BigDecimal zaehlerstand) {
		this.stand = zaehlerstand;
	}

	public Zaehler getZaehler() {
		return this.zaehler;
	}

	public void setZaehler(Zaehler zaehler) {
		this.zaehler = zaehler;
	}

	@Override
	public String toString() {
		return "Zaehlerstand [id=" + id + ", createdAt=" + createdAt + ", dateneinreichung=" + dateneinreichung
				+ ", updatedAt=" + updatedAt + ", stand=" + stand + ", typ=" + typ + ", zaehler=" + zaehler + "]";
	}

	public Integer getTyp() {
		return typ;
	}

	public void setTyp(Integer typ) {
		this.typ = typ;
	}

}