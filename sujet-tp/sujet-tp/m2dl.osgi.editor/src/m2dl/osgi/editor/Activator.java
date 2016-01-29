package m2dl.osgi.editor;

import java.util.Optional;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import m2dl.osgi.editor.service.IColorService;
import m2dl.osgi.editor.service.IDecorationService;
import m2dl.osgi.editor.tracker.ColorationServiceTracker;
import m2dl.osgi.editor.tracker.DecorationServiceTracker;

public class Activator implements BundleActivator {

	private static Activator instance;

	private BundleContext context;
	private ColorationServiceTracker colorTracker;
	private DecorationServiceTracker decoTracker;
	/**
	 * To know if the window is running.
	 */
	private static Boolean windowIsRunning = false;
	/**
	 * The logger.
	 */
	public static final Logger logger = LogManager.getLogger(Activator.class);

	public static Activator getInstance() {
		return instance;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		instance = this;
		context = bundleContext;
		// colorTracker = new ColorationServiceTracker(context);
		// decoTracker = new DecorationServiceTracker(context);

		final ServiceTrackerCustomizer<IColorService, IColorService> colorTracker = new ColorationServiceTracker(
				context);

		final ServiceTracker<IColorService, IColorService> mainService = new ServiceTracker<IColorService, IColorService>(
				context, IColorService.class.getName(), colorTracker);
		mainService.open();

		System.out.println("A tracker for \"IColorService\" is started.");

		// final IColorService myService = new EditorServiceImpl();

		/// final Hashtable<String, String> dictionnary = new Hashtable<>();
		// dictionnary.put("my.metadata.type", "my.metadata.value");

		/// bundleContext.registerService(EditorService.class.getName(),
		/// myService, dictionnary);
		// System.out.println("My bundle is started and registered");
		/*
		 * Configuring the logger.
		 */
		BasicConfigurator.configure();
		/*
		 * Starting only one JavaFX window.
		 */

		if (!windowIsRunning) {
			new JFXPanel();
			Platform.runLater(() -> {
				try {
					/*
					 * Init the JavaFX nodes.
					 */
					final FXMLLoader loader = new FXMLLoader(
							CodeViewerController.class.getResource("main-window-exercice.fxml"));

					loader.load();

					final VBox root = (VBox) loader.getRoot();
					final Stage primaryStage = new Stage();
					final Scene scene = new Scene(root, 400, 400);

					final CodeViewerController controller = loader.getController();
					controller.setBundleContext(bundleContext);
					controller.setPrimaryStage(primaryStage);

					/*
					 * Setup the window
					 */
					primaryStage.setTitle("The first OSGi modulable text editor");
					primaryStage.setScene(scene);
					/*
					 * Display the window.
					 */
					primaryStage.show();
					windowIsRunning = true;
					logger.info("The editor is running");

				} catch (final Exception e) {
					logger.debug("Error during loading the window");
					e.printStackTrace();
				}
			});
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		/*
		 * Stopping the editor.
		 */
		logger.info("The editor will be stopted.");
	}

	/*
	 * public Optional<IColorService> getColorationService(String type) { return
	 * colorTracker.getService(type); }
	 */

	public Optional<IDecorationService> getDecorationService() {
		return decoTracker.getService();
	}
}

/*
 * Get the menu bar from the window. File "main-window-exercice.java", line 15:
 * <OSGiMenuBar fx:id="MenuActions" VBox.vgrow="NEVER">
 */
// final OSGiMenuBar osgiMenuBar = (OSGiMenuBar)
// scene.lookup("#MenuActions");
// /*
// * Export the OSGiMenuBar service.
// */
// final Hashtable<String, String> dictionnary = new
// Hashtable<>();
// dictionnary.put("m2dl.osgi.editor.javafx-node",
// OSGiMenuBar.class.getName());
// bundleContext.registerService(IOSGiMenuBar.class.getName(),
// osgiMenuBar, dictionnary);
// logger.info(osgiMenuBar + " is now available");
// /*
// * TODO THE OTHER OSGi SERVICES BASED ON THE WINDOW HAVE
// TO
// * BE DECLARED HERE!
// */
//
