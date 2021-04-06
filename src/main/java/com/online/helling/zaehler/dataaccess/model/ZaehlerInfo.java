package com.online.helling.zaehler.dataaccess.model;

import java.math.BigDecimal;
import java.util.Date;

public class ZaehlerInfo {
	public int medium;
	public String nummer;
	public String bezeichnung;
	public Date letzteAblesung;
	public BigDecimal letzterStand;
	public String einheit;
	public double verbrauch12Monate;
}
