package com.riambsoft.state.core;

import com.riambsoft.core.datasource.DataSourceConfig;

public class StateDataSourceConfig extends DataSourceConfig {

	private static StateDataSourceConfig config;

	private StateDataSourceConfig() {
		super();
	}

	public static synchronized DataSourceConfig getInstance() {
		if (config == null) {
			config = new StateDataSourceConfig();
		}
		return config;
	}

}
