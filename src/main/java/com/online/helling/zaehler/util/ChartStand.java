package com.online.helling.zaehler.util;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

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

	private static final DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	private static final DecimalFormat df = new DecimalFormat("0.000");

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
