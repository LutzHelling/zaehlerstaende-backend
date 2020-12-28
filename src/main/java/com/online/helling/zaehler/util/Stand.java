package com.online.helling.zaehler.util;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * Beschreibt einen Messwert über dessen Stichtag und Wert
 * 
 * @author lutz
 *
 */
public class Stand implements Serializable {

	private static final long serialVersionUID = 8373692881951274450L;

	public Date datum;

	public double jahresverbrauch;

	public double vorjahresverbrauch;

	public Stand(Date datum) {
		this.datum = datum;
	}

	@Override
	public String toString() {
		return "Stand [datum=" + datum + ", jahreswert=" + jahresverbrauch + ", vorjahreswert=" + vorjahresverbrauch
				+ "]";
	}

}
