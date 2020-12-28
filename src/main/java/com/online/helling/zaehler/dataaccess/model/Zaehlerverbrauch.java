/*

 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.online.helling.zaehler.dataaccess.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.stereotype.Component;

import com.online.helling.zaehler.dataaccess.converter.StringToIntegerConverter;
import com.online.helling.zaehler.util.Medium;

/**
 *
 * @author Lutz
 */
@Component
@Entity
@Table(name = "zaehlerverbrauch")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Zaehlerverbrauch.findAll", query = "SELECT z FROM Zaehlerverbrauch z"),
		@NamedQuery(name = "Zaehlerverbrauch.findByZaehlerId", query = "SELECT z FROM Zaehlerverbrauch z WHERE z.zaehlerId = :zaehlerId"),
		@NamedQuery(name = "Zaehlerverbrauch.findOne", query = "SELECT z FROM Zaehlerverbrauch z WHERE z.id = :zaehlerId"),
		@NamedQuery(name = "Zaehlerverbrauch.findByTyp", query = "SELECT z FROM Zaehlerverbrauch z WHERE z.id.typ = :typ") })
public class Zaehlerverbrauch implements Serializable {

	private static final long serialVersionUID = -3417934779122061765L;

	@EmbeddedId
	private ZaehlerverbrauchId id;

	@Column(name = "zaehler_id")
	private Integer zaehlerId;

	@Column(name = "zaehlerstand")
	private BigDecimal zaehlerstand;

	@Column(name = "vorigerstand")
	private BigDecimal vorigerstand;

	@Column(name = "vor_date")
	@Temporal(TemporalType.DATE)
	@Size(max = 10)
	private Date vorDate;

	@Column(name = "anz_tage")
	private Integer anzTage;

	@Column(name = "verbrauch")
	private BigDecimal verbrauch;

	@Column(name = "jahr_aktuell")
	private Integer jahrAktuell;

	@Column(name = "monat_aktuell")
	private Integer monatAktuell;

	@Column(name = "tagesverbrauch_im_durchschnitt")
	private BigDecimal tagesverbrauchImDurchschnitt;

	@Size(max = 6)
	@Column(name = "aktuellermonat_technisch")
	private String aktMonatTechnisch;

	@Column(name = "aktuellermonat_technisch", insertable = false, updatable = false)
	@Convert(converter = StringToIntegerConverter.class)
	private Integer aktMonatNumeric;

	@Size(max = 6)
	@Column(name = "vormonat_technisch")
	private String vorMonatTechnisch;

	@Column(name = "vormonat_technisch", insertable = false, updatable = false)
	@Convert(converter = StringToIntegerConverter.class)
	private Integer vorMonatNumeric;

	private transient boolean isInterpoliert;

	public Integer getAktMonatNumeric() {
		return aktMonatNumeric;
	}

	public String getAktMonatTechnisch() {
		return aktMonatTechnisch;
	}

	public Integer getAnzTage() {
		return anzTage;
	}

	public ZaehlerverbrauchId getId() {
		return id;
	}

	public BigDecimal getTagesverbrauchImDurchschnitt() {
		return tagesverbrauchImDurchschnitt;
	}

	public BigDecimal getVerbrauch() {
		return verbrauch;
	}

	public Date getVorDate() {
		return vorDate;
	}

	public BigDecimal getVorigerstand() {
		return vorigerstand;
	}

	public Integer getVorMonatNumeric() {
		return vorMonatNumeric;
	}

	public String getVorMonatTechnisch() {
		return vorMonatTechnisch;
	}

	public Integer getZaehlerId() {
		return zaehlerId;
	}

	public BigDecimal getZaehlerstand() {
		return zaehlerstand;
	}

	public boolean isInterpoliert() {
		return isInterpoliert;
	}

	public void setAktMonatNumeric(Integer aktMonatNumeric) {
		this.aktMonatNumeric = aktMonatNumeric;
	}

	public void setAktMonatTechnisch(String aktMonatTechnisch) {
		this.aktMonatTechnisch = aktMonatTechnisch;
	}

	public void setAnzTage(Integer anzTage) {
		this.anzTage = anzTage;
	}

	public void setId(ZaehlerverbrauchId id) {
		this.id = id;
	}

	public void setInterpoliert(boolean isInterpoliert) {
		this.isInterpoliert = isInterpoliert;
	}

	public void setTagesverbrauchImDurchschnitt(BigDecimal tagesverbrauchImDurchschnitt) {
		this.tagesverbrauchImDurchschnitt = tagesverbrauchImDurchschnitt;
	}

	public void setVerbrauch(BigDecimal verbrauch) {
		this.verbrauch = verbrauch;
	}

	public Date getAktDate() {
		return getId().getAktDate();
	}

	public int getTyp() {
		return getId().getTyp();
	}

	public void setVorDate(Date vorDate) {
		this.vorDate = vorDate;
	}

	public void setVorigerstand(BigDecimal vorigerstand) {
		this.vorigerstand = vorigerstand;
	}

	public void setVorMonatNumeric(Integer vorMonatNumeric) {
		this.vorMonatNumeric = vorMonatNumeric;
	}

	public void setVorMonatTechnisch(String vorMonatTechnisch) {
		this.vorMonatTechnisch = vorMonatTechnisch;
	}

	public void setZaehlerId(Integer zaehlerId) {
		this.zaehlerId = zaehlerId;
	}

	public void setZaehlerstand(BigDecimal zaehlerstand) {
		this.zaehlerstand = zaehlerstand;
	}

	public String getMedium() {
		return Medium.fromInt(getTyp()).toString();
	}

	@Override
	public String toString() {
		return "Zaehlerverbrauch [id=" + id + ", zaehlerId=" + zaehlerId + ", zaehlerstand=" + zaehlerstand
				+ ", vorigerstand=" + vorigerstand + ", vorDate=" + vorDate + ", anzTage=" + anzTage + ", verbrauch="
				+ verbrauch + ", tagesverbrauchImDurchschnitt=" + tagesverbrauchImDurchschnitt + ", aktMonatTechnisch="
				+ aktMonatTechnisch + ", aktMonatNumeric=" + aktMonatNumeric + ", vorMonatTechnisch="
				+ vorMonatTechnisch + ", vorMonatNumeric=" + vorMonatNumeric + ", isInterpoliert=" + isInterpoliert
				+ "]";
	}

}
