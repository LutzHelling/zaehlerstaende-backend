package com.online.helling.zaehler.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

public class VersionInfo implements Serializable {
	
	private static final long serialVersionUID = -1412029224955990798L;

	private Properties prop = new Properties();

	public VersionInfo() {
		this.prop = new Properties();
		try (InputStream resourceAsStream = this.getClass()
				.getResourceAsStream("/static/tools/version.properties")) {
			this.prop.load(resourceAsStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getVersion() {
		return this.prop.get("version").toString();
	}
}