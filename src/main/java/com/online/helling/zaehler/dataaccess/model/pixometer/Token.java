package com.online.helling.zaehler.dataaccess.model.pixometer;

public class Token {
	private float user_id;
	private String scope;
	private String access_token;
	private String url;
	private float expires_in;
	private String token_type;

	// Getter Methods

	public float getUser_id() {
		return user_id;
	}

	public String getScope() {
		return scope;
	}

	public String getAccessToken() {
		return access_token;
	}

	public String getUrl() {
		return url;
	}

	public float getExpires_in() {
		return expires_in;
	}

	public String getToken_type() {
		return token_type;
	}

	// Setter Methods

	public void setUser_id(float user_id) {
		this.user_id = user_id;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setExpires_in(float expires_in) {
		this.expires_in = expires_in;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}
}
