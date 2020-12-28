package com.online.helling.zaehler.util;

public enum Medium {

	Strom(1), Gas(2), Wasser(3);

	private int typ;

	private Medium(int typ) {
		this.typ = typ;
	}

	public int getTyp() {
		return this.typ;
	}

	public static Medium fromInt(int medium) {
		for (Medium curMedium : Medium.values()) {
			if (curMedium.getTyp() == medium)
				return curMedium;
		}
		throw new IllegalArgumentException();
	}

	public static String getEinheit(Medium medium) {
		switch (medium) {
		case Gas:
			return "m³";
		case Wasser:
			return "m³";
		case Strom:
			return "KWh";
		default:
			return "?";
		}
	}
}
