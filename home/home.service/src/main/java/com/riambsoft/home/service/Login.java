package com.riambsoft.home.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.riambsoft.core.RSMethod;
import com.riambsoft.core.RSParam;
import com.riambsoft.core.RSResult;
import com.riambsoft.core.pojo.RsApp;
import com.riambsoft.core.pojo.User;
import com.riambsoft.core.web.RSController;
import com.riambsoft.core.web.RSParamHttpSession;
import com.riambsoft.core.web.RSParamUser;
import com.riambsoft.core.web.RSServiceName;

@RSController("home")
@RSServiceName("login")
public interface Login {

	/**
	 * 返回信息中用户信息键名
	 */
	public final static String USER = "USER";

	/**
	 * 返回信息中成功与否标记的键名
	 */
	public final static String SUCC = "SUCC";

	/**
	 * 返回信息中失败标记的值
	 */
	public final static String SUCC_FALSE = "false";

	/**
	 * 返回信息中成功标记的值
	 */
	public final static String SUCC_TRUE = "true";

	/**
	 * 失败时返回消息内容的键名
	 */
	public final static String MESSAGE = "MESSAGE";

	/**
	 * 登陆系统
	 * 
	 * @param userCode
	 * @param password
	 * @param session
	 * @return
	 */
	@RSMethod
	@RSResult
	public User login(@RSParam("userCode") String userCode,
			@RSParam("password") String password,
			@RSParamHttpSession HttpSession session);

	/**
	 * 登出系统
	 * 
	 * @param session
	 */
	@RSMethod
	@RSResult
	public void logout();

	/**
	 * 获取当前登陆用户的信息
	 * 
	 * @param user
	 * @return
	 */
	@RSResult
	@RSMethod
	public User getUserInfo(@RSParamUser User user);

	/**
	 * 获取当前登陆用户有权限的WEB应用程序列表
	 * 
	 * @param user
	 * @return
	 */
	@RSResult
	@RSMethod
	public List<RsApp> getWebApps(@RSParamUser User user);

}
