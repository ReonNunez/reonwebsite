package com.sample.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("db.config")
public class DBProperty {
	String dBClassName;
	String dBURL;
	String dBUSName;
	String dBUSPass;
	
	public String getdBClassName() {
		return dBClassName;
	}
	public void setdBClassName(String dBClassName) {
		this.dBClassName = dBClassName;
	}
	public String getdBURL() {
		return dBURL;
	}
	public void setdBURL(String dBURL) {
		this.dBURL = dBURL;
	}
	public String getdBUSName() {
		return dBUSName;
	}
	public void setdBUSName(String dBUSName) {
		this.dBUSName = dBUSName;
	}
	public String getdBUSPass() {
		return dBUSPass;
	}
	public void setdBUSPass(String dBUSPass) {
		this.dBUSPass = dBUSPass;
	}
	
	
	
	
}
