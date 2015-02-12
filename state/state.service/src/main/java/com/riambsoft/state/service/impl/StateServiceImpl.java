package com.riambsoft.state.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.riambsoft.core.pojo.User;
import com.riambsoft.state.core.State;
import com.riambsoft.state.core.StateDao;
import com.riambsoft.state.core.StateService;

public class StateServiceImpl implements StateService {

	private StateDao stateDao;

	public StateServiceImpl() {
		super();
	}

	public void setStateDao(StateDao stateDao) {
		this.stateDao = stateDao;
	}

	public List<State> load(List<String> modules, User user) {
		List<State> states = new ArrayList<State>();
		for (String module : modules) {
			states.add(stateDao.getStateByModuleAndUser(module, user));
		}
		return states;
	}

	public boolean sync(State state, User user) {
		String id = state.getId();
		if (id == null || "".equals(id)) {
			id = stateDao.createId(user.getId(), state.getModuleCode(),
					state.getSchemeCode());
			state.setId(id);
		}
		state.setUserId(user.getId());

		stateDao.save(state);

		return true;
	}

	public boolean rename(String moduleCode, String newName, String oldName,
			User user) {

		String id = stateDao.createId(user.getId(), moduleCode, oldName);

		stateDao.rename(id, newName);

		return true;
	}

	public boolean clear(String moduleCode, String schemeCode, User user) {

		String id = stateDao.createId(user.getId(), moduleCode, schemeCode);

		stateDao.delById(id);

		return true;
	}

	public boolean setDefault(String moduleCode, String schemeCode, User user) {

		String id = stateDao.createId(user.getId(), moduleCode, schemeCode);

		stateDao.setDefault(id);

		return true;
	}

}
