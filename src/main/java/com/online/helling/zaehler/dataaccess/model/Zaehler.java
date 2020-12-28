package com.online.helling.zaehler.dataaccess.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the zaehler database table.
 * 
 */
@Entity
@Table(name="zaehler")
@NamedQuery(name = "Zaehler.findAll", query = "SELECT z FROM Zaehler z")
public class Zaehler implements Serializable {

	private static final long serialVersionUID = -4568484793910080727L;

	private String bezeichnung;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	private Date updatedAt;

	@Id
	@Column(name = "zaehler_id")
	private Integer zaehlerId;

	@Column(name = "aktiv")
	private Integer aktiv;

	// bi-directional many-to-one association to Zaehlerstand
	@OneToMany(mappedBy = "zaehler")
	private List<Zaehlerstand> zaehlerstaende;

	@Column(name = "typ")
	private Integer typ;

	public Zaehler() {
		super();
	}

	public Zaehlerstand addZaehlerstand(Zaehlerstand zaehlerstand) {
		getZaehlerstands().add(zaehlerstand);
		zaehlerstand.setZaehler(this);
		return zaehlerstand;
	}

	public int getAktiv() {
		return aktiv;
	}

	public String getBezeichnung() {
		return this.bezeichnung;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public Integer getTyp() {
		return typ;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public int getZaehlerId() {
		return zaehlerId;
	}

	public List<Zaehlerstand> getZaehlerstands() {
		return this.zaehlerstaende;
	}

	public Zaehlerstand removeZaehlerstand(Zaehlerstand zaehlerstand) {
		getZaehlerstands().remove(zaehlerstand);
		zaehlerstand.setZaehler(null);

		return zaehlerstand;
	}

	public void setAktiv(int aktiv) {
		this.aktiv = aktiv;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public void setTyp(Integer typ) {
		this.typ = typ;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setZaehlerId(int zaehlerId) {
		this.zaehlerId = zaehlerId;
	}

	public void setZaehlerstands(List<Zaehlerstand> zaehlerstaende) {
		this.zaehlerstaende = zaehlerstaende;
	}

	@Override
	public String toString() {
		return "Zaehler [bezeichnung=" + bezeichnung + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ ", zaehlerId=" + zaehlerId + ", zaehlerstands=" + zaehlerstaende + ", typ=" + typ + "]";
	}

}