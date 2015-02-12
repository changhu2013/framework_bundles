package com.riambsoft.home.service.impl;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.riambsoft.core.auth.RSAuth;
import com.riambsoft.core.dao.RsAppDao;
import com.riambsoft.core.pojo.RsApp;
import com.riambsoft.core.pojo.User;
import com.riambsoft.framework.core.web.ServiceController;
import com.riambsoft.home.service.Login;

public class LoginImpl implements Login {

	private RSAuth auth;

	private RsAppDao rsAppDao;

	public LoginImpl(RSAuth auth, RsAppDao rsAppDao) {
		super();
		this.auth = auth;
		this.rsAppDao = rsAppDao;
	}

	public User login(String userCode, String password, HttpSession session) {
		User user = auth.validate(userCode, password);
		session.removeAttribute(ServiceController.HTTP_SESSION_KEY_FOR_USER);
		session.setAttribute(ServiceController.HTTP_SESSION_KEY_FOR_USER, user);

		return user;
	}

	public void logout(HttpSession session) {
		session.removeAttribute(ServiceController.HTTP_SESSION_KEY_FOR_USER);
	}

	public User getUserInfo(User user) {
		return user;
	}

	public List<RsApp> getWebApps(User user) {
		return rsAppDao.getRsApps(user);
	}

}
