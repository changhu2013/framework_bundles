package com.riambsoft.state.dao.impl;

import javax.sql.DataSource;

import com.riambsoft.core.pojo.User;
import com.riambsoft.state.core.State;
import com.riambsoft.state.core.StateDao;

public class StateDaoImpl implements StateDao {

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String createId(String userId, String moduleCode, String schemeCode) {
		return userId + "_" + moduleCode + "_" + schemeCode;
	}

	public void save(State state) {

	}

	public void delById(String id) {
	}

	public void setDefault(String id) {

	}

	public void rename(String id, String newSchemeCode) {

	}

	public State getStateByModuleAndUser(String module, User user) {
		return null;
	}

}
