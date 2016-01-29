package m2dl.osgi.editor.tracker;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class GenericServiceTrackerCustomizer<SERVICE> implements ServiceTrackerCustomizer<SERVICE, SERVICE> {

	private BundleContext bundleContext;

	private SERVICE myService;

	public GenericServiceTrackerCustomizer(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
		this.myService = null;
	}

	@Override
	public SERVICE addingService(ServiceReference<SERVICE> reference) {
		// FIXME: use reference to match properties, allow activator to choose
		// service by using properties
		myService = (SERVICE) bundleContext.getService(reference);

		System.out.println("A new \"MyService\" appeared with the extention type = " + reference.getProperty("type"));

		return myService;
	}

	@Override
	public void modifiedService(ServiceReference<SERVICE> reference, SERVICE service) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removedService(ServiceReference<SERVICE> reference, SERVICE service) {
		bundleContext.ungetService(reference);
		myService = null;
	}
}
