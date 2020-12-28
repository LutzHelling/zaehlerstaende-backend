package com.online.helling.zaehler.dataaccess.model;

import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.springframework.stereotype.Component;

@Component
@Entity(name="jahresstaende")
public class Jahresstand {

	@EmbeddedId
	public JahresstandId id;
	
	public BigDecimal verbrauch;
}
