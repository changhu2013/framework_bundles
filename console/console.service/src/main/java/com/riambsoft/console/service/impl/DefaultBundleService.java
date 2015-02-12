package com.riambsoft.console.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.util.tracker.ServiceTracker;

import com.riambsoft.console.service.BundleService;
import com.riambsoft.core.log.Log;
import com.riambsoft.core.log.LogFactory;
import com.riambsoft.core.util.RSUtil;

public class DefaultBundleService implements BundleService {

	private BundleContext context;

	private final Map<String, ServiceTracker> services = new HashMap<String, ServiceTracker>();

	private Log logger = LogFactory.getLog(DefaultBundleService.class);

	public DefaultBundleService(BundleContext context) {
		super();
		this.context = context;
	}

	public List<Map<String, Object>> getAllBundles() throws Exception {

		logger.debug("getAllBundles");

		List<Map<String, Object>> bundles = new ArrayList<Map<String, Object>>();

		Bundle[] bs = context.getBundles();

		for (Bundle b : bs) {

			Map<String, Object> bundle = new HashMap<String, Object>();
			bundle.put("id", "" + b.getBundleId());
			bundle.put("symbolicName", b.getSymbolicName());
			bundle.put("version", b.getVersion().toString());
			bundle.put("state", "" + b.getState());
			bundle.put("location", b.getLocation());
			Date d = new Date(b.getLastModified());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			bundle.put("lastModification", String.valueOf(sdf.format(d)));
			bundle.put("registeredServices", getServices(b) == null ? "None"
					: getServices(b));
			Map<String, String> relatedPackages = getImportedPackages(b);
			String importedPackages = relatedPackages.get("importedPackages");
			bundle.put("importedPackages", importedPackages);
			String exportedPackages = relatedPackages.get("exportedPackages");
			bundle.put("exportedPackages", exportedPackages);
			String importingBundles = relatedPackages.get("importingBundles");
			bundle.put("importingBundles", importingBundles);
			bundles.add(bundle);
		}

		logger.debug("getAllBundles " + bundles);
		return bundles;
	}

	public List<List<String>> getServices(Bundle b) {

		logger.debug("getServices " + b);

		ServiceReference[] refs = b.getRegisteredServices();
		if ((refs == null) || (refs.length == 0)) {
			return null;
		}
		List<List<String>> services = new ArrayList<List<String>>();
		for (int i = 0; i < refs.length; ++i) {
			Object sid = refs[i].getProperty("service.id");
			List<String> info = new ArrayList<String>();
			if (sid != null) {
				info.add("Service " + propertyToString(sid));
			} else {
				break;
			}

			Object type = refs[i].getProperty("objectClass");
			if (type != null)
				info.add("Service Type : " + propertyToString(type));
			Object pid = refs[i].getProperty("service.pid");
			if (pid != null)
				info.add("Service PID : " + propertyToString(pid));
			Object svendor = refs[i].getProperty("service.vendor");
			if (svendor != null)
				info.add("Service Vendor : " + propertyToString(svendor));
			services.add(info);
		}

		logger.debug("getServices " + b + " " + services);

		return services;
	}

	public String propertyToString(Object value) {
		StringBuffer dest = new StringBuffer();
		if (value instanceof Object[]) {
			Object[] values = (Object[]) (Object[]) value;
			for (int j = 0; j < values.length; ++j) {
				if (j > 0) {
					dest.append(", ");
				}
				dest.append(values[j]);
			}
		} else if (value != null) {
			dest.append(value);
		}
		return dest.toString();
	}

	public Map<String, String> getImportedPackages(Bundle b) {
		Map<String, Bundle> usingBundles = new TreeMap<String, Bundle>();
		PackageAdmin packageAdmin = getPackageAdmin();
		if (packageAdmin == null) {
			return null;
		}
		Map<String, String> relatedPackages = new HashMap<String, String>();

		// 获得此bundle所导出的包
		ExportedPackage[] exports = packageAdmin.getExportedPackages(b);
		String exportedPackages = "";
		if ((exports != null) && (exports.length > 0)) {
			for (int j = 0; j < exports.length; ++j) {
				int i;
				ExportedPackage export = exports[j];
				exportedPackages += export.getName() + ", "
						+ export.getVersion() + "; ";
				Bundle[] ubList = export.getImportingBundles();
				if (ubList != null) {
					for (i = 0; i < ubList.length; ++i) {
						Bundle ub = ubList[i];
						String name = ub.getSymbolicName();
						if (name == null)
							name = ub.getLocation();
						usingBundles.put(name, ub);
					}
				}
			}
		}
		relatedPackages.put(
				"exportedPackages",
				exportedPackages.length() > 0 ? exportedPackages.substring(0,
						exportedPackages.lastIndexOf(";")) : "None");

		exports = packageAdmin.getExportedPackages((Bundle) null);
		String importedPackages = "";
		if ((exports != null) && (exports.length > 0)) {
			for (int i = 0; i < exports.length; ++i) {
				ExportedPackage ep = exports[i];

				// 获得引入这个抛出的包的所有bundle
				Bundle[] importers = ep.getImportingBundles();
				for (int j = 0; (importers != null) && (j < importers.length); ++j) {
					// 如果当前bundle引入了这个包
					if (importers[j].getBundleId() == b.getBundleId()) {
						importedPackages += ep.getName() + ", "
								+ ep.getVersion() + "; ";
						break;
					}
				}
			}
		}
		relatedPackages.put(
				"importedPackages",
				importedPackages.length() > 0 ? importedPackages.substring(0,
						importedPackages.lastIndexOf(";")) : "None");

		String importingBundles = "";
		if (!(usingBundles.isEmpty())) {
			for (Iterator<Bundle> ui = usingBundles.values().iterator(); ui
					.hasNext();) {
				Bundle usingBundle = (Bundle) ui.next();
				importingBundles += usingBundle.getSymbolicName() + ", "
						+ usingBundle.getVersion() + "; ";
			}
		}
		relatedPackages.put(
				"importingBundles",
				importingBundles.length() > 0 ? importingBundles.substring(0,
						importingBundles.lastIndexOf(";")) : "None");

		return relatedPackages;
	}

	public Map<String, String> getBundle(String id) {

		Bundle b = context.getBundle(Long.parseLong(id));
		Map<String, String> bundle = new HashMap<String, String>();

		bundle.put("id", "" + b.getBundleId());
		bundle.put("symbolicName", b.getSymbolicName());
		bundle.put("version", b.getVersion().toString());
		bundle.put("state", "" + b.getState());
		bundle.put("location", b.getLocation());

		return bundle;
	}

	public String stopBundle(String id) {
		Bundle b = context.getBundle(Long.parseLong(id));
		try {
			b.stop();
			return "success";
		} catch (Exception e) {
			return "";
		}
	}

	public String startBundle(String id) {
		Bundle b = context.getBundle(Long.parseLong(id));
		try {
			b.start();
			return "success";
		} catch (Exception e) {
			return "";
		}
	}

	public String refreshBundle(String id) {
		Bundle b = context.getBundle(Long.parseLong(id));
		getPackageAdmin().refreshPackages(new Bundle[] { b });
		return "success";
	}

	public String updateBundle(List<File> files, String id) {
		ClassLoader loader = RSUtil.getFrameworkClassLoader();
		String url = ""+loader.getResource(".");
		for(int i=0; i<files.size(); i++){
			File file = files.get(i);
			String fileName = file.getName();
			File dest = new File(url.substring(6,url.indexOf("classes"))+"eclipse/features/"+fileName);
			
		    Bundle b = context.getBundle(Long.parseLong(id)); 
		    String opath=null,ofname=null;
		    if(dest.exists()){
				dest.delete();
			}else{
		        opath = b.getLocation(); 
		        ofname=opath.substring(opath.indexOf("features")+9);
		        File old = new File(url.substring(6,url.indexOf("classes"))+"eclipse/features/"+ofname);
		        old.delete();
			}
			try{
				FileInputStream in=new FileInputStream(file);  
				FileOutputStream out= new FileOutputStream(url.substring(6,url.indexOf("classes"))+"eclipse/features/"+fileName);  
		        BufferedInputStream bufferedIn=new BufferedInputStream(in);  
		        BufferedOutputStream bufferedOut=new BufferedOutputStream(out);  
		        byte[] by=new byte[1];
		        while (bufferedIn.read(by)!=-1) {  
		        	bufferedOut.write(by);  
		        }  
		        //将缓冲区中的数据全部写出  
		        bufferedOut.flush();  
		        bufferedIn.close();  
		        bufferedOut.close();  
		        
		        // 根据文件路径创建缓冲输入流
		        BufferedReader br = new BufferedReader(new FileReader(url.substring(6)+"bundles.properties"));
		        String line = null;
		        StringBuffer buf = new StringBuffer();
		        try {
		        	// 循环读取文件的每一行, 对需要修改的行进行修改, 放入缓冲对象中
		        	int flag=0;
		        	while ((line = br.readLine()) != null){
		        		if(flag!=0){
			        		buf.append("\r\n");
		        		}
		        		StringBuffer s = new StringBuffer(line);
		        		if(s.indexOf(ofname)>0){
		        			s.replace(s.indexOf(ofname), s.indexOf(ofname)+ofname.length(), fileName);
		        		}
		        		buf.append(s);
		        		flag++;
		        	}
		        	RandomAccessFile prop = new RandomAccessFile(url.substring(6)+"bundles.properties", "rw");
		        	prop.writeBytes(buf.toString());
		        	prop.close();
		        }catch(Exception e){
		        	e.printStackTrace();
					return "false";
		        }finally {
		        	if (br != null) {
		        		try {
		        			br.close();
		        		} catch (Exception e) {
		        			br = null;
		        		}
		        	}
		        }
		        try {
		        	b.update(new FileInputStream(url.substring(6,url.indexOf("classes"))+"eclipse/features/"+fileName));
		        } catch (Exception e) {
		        	e.printStackTrace();
		        	return "false";
		        }
			} catch(Exception e){
					e.printStackTrace();
					return "false";
			}
		}
		return "true";
	}

	public String installBundle(List<File> files) {
		
		ClassLoader loader = RSUtil.getFrameworkClassLoader();
		String url = ""+loader.getResource(".");
		for(int i=0; i<files.size(); i++){
			File file = files.get(i);
			String fileName = file.getName();
			System.out.println( "------f----->" + file);
			File dest = new File(url.substring(6,url.indexOf("classes"))+"eclipse/features/"+fileName);
			//file.renameTo(dest);
			if(dest.exists()){
				dest.delete();
			}
			try{
				FileInputStream in=new FileInputStream(file);  
				FileOutputStream out= new FileOutputStream(url.substring(6,url.indexOf("classes"))+"eclipse/features/"+fileName);  
	            BufferedInputStream bufferedIn=new BufferedInputStream(in);  
	            BufferedOutputStream bufferedOut=new BufferedOutputStream(out);  
	            byte[] by=new byte[1];
	            while (bufferedIn.read(by)!=-1) {  
	                bufferedOut.write(by);  
	            }  
	            //将缓冲区中的数据全部写出  
	            bufferedOut.flush();  
	            bufferedIn.close();  
	            bufferedOut.close();  
			} catch(Exception e){
				e.printStackTrace();
				return "false";
			}
			try {
				RandomAccessFile rf = new RandomAccessFile(url.substring(6)+"bundles.properties","rw");
	            rf.seek(rf.length() - 1);
	            String line = rf.readLine();
	            if(line.length()>0){
	            	rf.seek(rf.length());
	            }else{
	            	rf.seek(rf.length() - 2);
	            }
	            rf.write((",reference\\:file\\:features/"+fileName+"@start").getBytes());
	            rf.close();
	            String location = url.substring(0, url.indexOf("classes"))+"eclipse/features/"+fileName;
	            context.installBundle(location);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "false";
			}
		}
		return "true";
	}

	public String uninstallBundle(String id) {
		Bundle b = context.getBundle(Long.parseLong(id));
		try {
			b.uninstall();
			return "success";
		} catch (Exception e) {
			return "";
		}
	}

	private final PackageAdmin getPackageAdmin() {
		return ((PackageAdmin) getService(PackageAdmin.class.getName()));
	}

	public final Object getService(String serviceName) {
		ServiceTracker serviceTracker = (ServiceTracker) this.services
				.get(serviceName);
		if (serviceTracker == null) {
			serviceTracker = new ServiceTracker(getBundleContext(),
					serviceName, null);
			serviceTracker.open();

			this.services.put(serviceName, serviceTracker);
		}

		return serviceTracker.getService();
	}

	protected BundleContext getBundleContext() {
		return this.context;
	}

}
