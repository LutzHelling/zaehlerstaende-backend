package com.online.helling.zaehler.util;

import java.io.Serializable;
import java.util.Date;

public class StandMitZeitraum implements Serializable {
	private static final long serialVersionUID = 404834450055291295L;
	private long wasserverbrauch;
	private long gasverbrauch;
	private long stromverbrauch;
	private String zeitraumText;
	private Date bezug;

	public Date getBezug() {
		return bezug;
	}

	public void setBezug(Date bezug) {
		this.bezug = bezug;
	}

	public StandMitZeitraum(Date bezug, String zeitraumText) {
		this.zeitraumText = zeitraumText;
		this.bezug = bezug;
	}

	public long getWasserverbrauch() {
		return wasserverbrauch;
	}

	public void setWasserverbrauch(long wasserverbrauch) {
		this.wasserverbrauch = wasserverbrauch;
	}

	public long getGasverbrauch() {
		return gasverbrauch;
	}

	public void setGasverbrauch(long gasverbrauch) {
		this.gasverbrauch = gasverbrauch;
	}

	public long getStromverbrauch() {
		return stromverbrauch;
	}

	public void setStromverbrauch(long stromverbrauch) {
		this.stromverbrauch = stromverbrauch;
	}

	public String getZeitraumText() {
		return zeitraumText;
	}

	public void setVerbrauch(Medium medium, long jahressumme) {
		switch (medium) {
		case Wasser:
			setWasserverbrauch(jahressumme);
			break;
		case Gas:
			setGasverbrauch(jahressumme);
			break;
		case Strom:
			setStromverbrauch(jahressumme);
			break;
		}

	}

}
