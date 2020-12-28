package com.online.helling.zaehler.dataaccess.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class JahresstandId implements Serializable {
	private static final long serialVersionUID = -612960763209982600L;
	public int typ;
	@Column(name="Jahr")
	public int jahr;
}
