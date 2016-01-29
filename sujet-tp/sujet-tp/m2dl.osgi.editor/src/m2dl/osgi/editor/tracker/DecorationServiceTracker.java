package m2dl.osgi.editor.tracker;

import java.util.Optional;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import m2dl.osgi.editor.service.IDecorationService;

public class DecorationServiceTracker {
	public BundleContext context;
	public ServiceTracker<IDecorationService, IDecorationService> serviceTrackerColoration;

	public DecorationServiceTracker(BundleContext context) {
		this.context = context;
		ServiceTrackerCustomizer<IDecorationService, IDecorationService> myService = new GenericServiceTrackerCustomizer<IDecorationService>(
				context);
		serviceTrackerColoration = new ServiceTracker<>(context, IDecorationService.class.getName(), myService);
		serviceTrackerColoration.open();
	}

	public Optional<IDecorationService> getService() {
		Optional<IDecorationService> service = Optional.empty();
		if (serviceTrackerColoration.getService() != null) {
			service = Optional.of(serviceTrackerColoration.getService());
		}
		return service;
	}

	/**
	 * Close the tracker that have been launched.
	 */
	public void closeTrackers() {
		if (serviceTrackerColoration != null) {
			serviceTrackerColoration.close();
		}
	}
}
