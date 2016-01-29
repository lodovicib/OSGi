package m2dl.osgi.editor.tracker;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import m2dl.osgi.editor.service.IColorService;

public class ColorationServiceTracker implements ServiceTrackerCustomizer<IColorService, IColorService> {

	private final BundleContext context;

	public ColorationServiceTracker(BundleContext _context) {
		System.out.println("et oui");
		context = _context;
		System.out.println("Je commence " + context.getProperty("type"));
	}

	@Override
	public IColorService addingService(ServiceReference<IColorService> serviceReference) {

		System.out.println("Je passe ici ");
		final IColorService service = context.getService(serviceReference);

		System.out.println(
				"A new \"IColorService\" appeared with the extention type = " + serviceReference.getProperty("type"));

		service.getType();

		return service;
	}

	@Override
	public void modifiedService(ServiceReference<IColorService> arg0, IColorService arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removedService(ServiceReference<IColorService> arg0, IColorService arg1) {
		// TODO Auto-generated method stub

	}

	/*
	 * { public BundleContext context; public ServiceTracker<IColorService,
	 * IColorService> serviceTrackerColoration;
	 * 
	 * public ColorationServiceTracker(BundleContext context) { this.context =
	 * context;
	 * 
	 * ServiceTrackerCustomizer<IColorService, IColorService> myService = new
	 * GenericServiceTrackerCustomizer<IColorService>( context);
	 * serviceTrackerColoration = new ServiceTracker<>(context,
	 * IColorService.class.getName(), myService);
	 * serviceTrackerColoration.open(); System.out.println("service : " +
	 * serviceTrackerColoration.getService()); }
	 * 
	 * public Optional<IColorService> getService(String type) {
	 * Optional<IColorService> service = Optional.empty();
	 * System.out.println(serviceTrackerColoration.getService());
	 * System.out.println(type.equals(serviceTrackerColoration.getService().
	 * getType())); if (serviceTrackerColoration.getService() != null) { // &&
	 * type.equals(serviceTrackerColoration.getService().getType())) // {
	 * service = Optional.of(serviceTrackerColoration.getService()); } return
	 * service; }
	 * 
	 * public void closeTrackers() { if (serviceTrackerColoration != null) {
	 * serviceTrackerColoration.close(); } }
	 */
}
