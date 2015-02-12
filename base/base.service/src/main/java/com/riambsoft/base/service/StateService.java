package com.riambsoft.base.service;

import java.util.List;
import java.util.Map;

import com.riambsoft.core.RSMethod;
import com.riambsoft.core.RSParam;
import com.riambsoft.core.RSResult;
import com.riambsoft.core.pojo.User;
import com.riambsoft.core.web.RSController;
import com.riambsoft.core.web.RSParamUser;
import com.riambsoft.core.web.RSServiceName;

@RSController("base")
@RSServiceName("state")
public interface StateService {

	@RSMethod
	@RSResult
	public List load(@RSParam("modules") List modules, 
			@RSParamUser User user);

	@RSMethod
	@RSResult
	public boolean sync(@RSParam("data") Map data, 
			@RSParamUser User user);

	@RSMethod
	@RSResult
	public boolean rename(@RSParam("moduleCode") String moduleCode, 
			@RSParam("newName") String newName, 
			@RSParam("oldName") String oldName,
			@RSParamUser User user);

	@RSMethod
	@RSResult
	public boolean clear(@RSParam("moduleCode") String moduleCode, 
			@RSParam("schemeCode") String schemeCode, 
			@RSParamUser User user);

	@RSMethod
	@RSResult
	public boolean setDefault(@RSParam("moduleCode") String moduleCode, 
			@RSParam("schemeCode") String schemeCode, 
			@RSParamUser User user);

}
