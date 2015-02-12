package com.riambsoft.base.service.impl;

import java.util.List;
import java.util.Map;

import com.riambsoft.base.service.StateService;
import com.riambsoft.core.pojo.User;

public class StateServiceImpl implements StateService {
	
	public List load(List modules, User user) {
		
		System.out.println("modules : " + modules);
		
		return null;
	}

	public boolean sync(Map data, User user) {
		
		System.out.println("data : " + data);
		
		return false;
	}

	public boolean rename(String moduleCode, String newName, String oldName,
			User user) {
		
		System.out.println("moduleCode : " + moduleCode);
		
		return false;
	}

	public boolean clear(String moduleCode, String schemeCode, User user) {
		
		System.out.println("moduleCode : " + moduleCode);
		
		return false;
	}

	public boolean setDefault(String moduleCode, String schemeCode, User user) {
		
		System.out.println("moduleCode : " + moduleCode);
		
		return false;
	}

}
