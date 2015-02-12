package com.riambsoft.base.service.impl;

import com.riambsoft.base.service.UserManager;
import com.riambsoft.core.dao.StoreDao;
import com.riambsoft.core.dao.UserDao;

public class UserManagerImpl extends UserManager {

	private UserDao userDao;

	public UserManagerImpl(StoreDao storeDao) {
		super(storeDao);
		this.userDao = (UserDao) storeDao;
	}
}
