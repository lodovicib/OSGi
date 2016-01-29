package m2dl.osgi.coloration.css;

import java.util.Hashtable;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import m2dl.osgi.coloration.css.service.impl.CSSColorationService;
import m2dl.osgi.editor.service.IColorService;

public class Activator implements BundleActivator {

	private static BundleContext context;
	public static final Logger logger = LogManager.getLogger(Activator.class);

	static BundleContext getContext() {
		return context;
	}

	public void start(final BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		logger.info("CSS coloration starting");
		Hashtable<String, String> dictionnary = new Hashtable<String, String>();
		dictionnary.put("type", "css");
		dictionnary.put("name", "IColorService");

		bundleContext.registerService(IColorService.class.getName(), new CSSColorationService(), dictionnary);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
}
