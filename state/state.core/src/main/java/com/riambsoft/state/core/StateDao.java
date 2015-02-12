package com.riambsoft.state.core;

import com.riambsoft.core.pojo.User;

public interface StateDao {

	public String createId(String userId, String moduleCode, String schemeCode);

	public void save(State state);

	public void delById(String id);

	public void setDefault(String id);
	
	public void rename(String id, String newSchemeCode);
	
	public State getStateByModuleAndUser(String module, User user);

}
