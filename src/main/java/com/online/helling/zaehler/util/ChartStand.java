package com.online.helling.zaehler.util;

import java.math.BigDecimal;

/**
 * Dient zum transportieren eines Stands für die Rest-Schnittstelle zum Anzeigen
 * von Charts. Hier werden verschiedene X-Achsenskalen verwendet (Jahr, Datum).
 * Hierfür wurde die Klasse generisch definiert.
 * 
 * @author lutz
 *
 * @param <Medium>
 * @param <T>
 * @param <BigDecimal>
 */
public class ChartStand<T> {

	private Medium medium;
	private T time;
	private BigDecimal wert;

	public ChartStand(Medium medium, T time, BigDecimal wert) {
		this.medium = medium;
		this.time = time;
		this.wert = wert;
	}

	public Medium getMedium() {
		return medium;
	}

	public T getTime() {
		return time;
	}

	public BigDecimal getWert() {
		return wert;
	}

}
