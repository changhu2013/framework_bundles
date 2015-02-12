package com.riambsoft.console.service;

import java.io.File;
import java.util.List;

import javax.jws.*;

@WebService
public interface BundleWebService {

	public String stopBundle(String id);

	public String startBundle(String id);

	public String refreshBundle(String id);

	public String updateBundle(List<File> files, String id);

	public String uninstallBundle(String id);

}
