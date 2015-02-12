package com.riambsoft.state.core;

import com.riambsoft.core.pojo.RsPojo;

public class State extends RsPojo {

	private static final long serialVersionUID = -5494206669012841493L;

	private String userId;

	private String moduleCode;

	private String schemeCode;

	private boolean isDefault;

	private String stateData;

	public State() {
		super();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getSchemeCode() {
		return schemeCode;
	}

	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getStateData() {
		return stateData;
	}

	public void setStateData(String stateData) {
		this.stateData = stateData;
	}
}
