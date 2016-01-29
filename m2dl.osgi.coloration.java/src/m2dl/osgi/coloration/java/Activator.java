package m2dl.osgi.coloration.java;

import java.util.Hashtable;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import m2dl.osgi.coloration.java.service.impl.JavaColorationService;
import m2dl.osgi.editor.service.IColorService;

public class Activator implements BundleActivator {

	public static final Logger logger = LogManager.getLogger(Activator.class);

	@Override
	public void start(BundleContext arg0) throws Exception {
		logger.info("Java coloration starting");
		final IColorService myService = new JavaColorationService();
		final Hashtable<String, String> dictionnary = new Hashtable<>();
		dictionnary.put("type", "java");
		dictionnary.put("name", "IColorService");

		arg0.registerService(IColorService.class.getName(), myService, dictionnary);
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		logger.info("Java coloration will be stopted.");
	}

}
