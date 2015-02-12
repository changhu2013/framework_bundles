package com.riambsoft.base.service;

import com.riambsoft.core.dao.StoreDao;
import com.riambsoft.core.service.StoreService;
import com.riambsoft.core.web.RSController;
import com.riambsoft.core.web.RSServiceName;


@RSController("base")
@RSServiceName("user")
public abstract class UserManager extends StoreService{

	public UserManager(StoreDao storeDao) {
		super(storeDao);
	}
	
}
