package com.riambsoft.console.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.riambsoft.core.RSMethod;
import com.riambsoft.core.RSParam;
import com.riambsoft.core.RSResult;
import com.riambsoft.core.web.RSController;
import com.riambsoft.core.web.RSServiceName;

@RSController("console")
@RSServiceName("bundle")
public interface BundleService extends BundleWebService {

	@RSMethod
	@RSResult("bundles")
	public List<Map<String, Object>> getAllBundles() throws Exception;

	@RSMethod
	@RSResult("bundle")
	public Map<String, String> getBundle(@RSParam("id") String id);

	@RSMethod(accessControl = true, accessDesc = "停止服务")
	@RSResult("stop")
	public String stopBundle(@RSParam("id") String id);

	@RSMethod(accessControl = true, accessDesc = "启动服务")
	@RSResult("start")
	public String startBundle(@RSParam("id") String id);

	@RSMethod
	@RSResult("refresh")
	public String refreshBundle(@RSParam("id") String id);

	@RSMethod
	@RSResult("success")
	public String updateBundle(@RSParam("files") List<File> files, @RSParam("id") String id);

	@RSMethod
	@RSResult("success")
	public String installBundle(@RSParam("files") List<File> files);

	@RSMethod
	@RSResult("uninstall")
	public String uninstallBundle(@RSParam("id") String id);

}
