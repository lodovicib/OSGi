package m2dl.osgi.decorator;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import m2dl.osgi.decorator.service.impl.DecoratorService;
import m2dl.osgi.editor.service.IDecorationService;

public class Activator implements BundleActivator {

	public static final Logger logger = LogManager.getLogger(Activator.class);

	@Override
	public void start(BundleContext arg0) throws Exception {
		logger.info("Decorator start.");
		arg0.registerService(IDecorationService.class, new DecoratorService(), null);
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		// TODO Auto-generated method stub
		logger.info("Decorator will be stopted.");
	}

}
