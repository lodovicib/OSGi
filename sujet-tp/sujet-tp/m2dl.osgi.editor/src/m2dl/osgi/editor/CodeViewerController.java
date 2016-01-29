package m2dl.osgi.editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import m2dl.osgi.editor.service.IColorService;
import m2dl.osgi.editor.service.IDecorationService;

public class CodeViewerController {

	// Gestion des gros commentaires
	boolean comment = false;
	private String fileName;
	private BundleContext bundleContext;

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	/**
	 * The main window of the application.
	 */
	private Stage primaryStage;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	/**
	 * Radio button: indicate if the html bundle is started.
	 */
	@FXML
	private RadioMenuItem radioMenuJava;

	/**
	 * Radio button: indicate if the decorator bundle is started.
	 */
	@FXML
	private RadioMenuItem radioMenuDecorator;

	/**
	 * The viewer to display the content of the opened file.
	 */
	@FXML
	private WebView webViewer;

	/**
	 * The radio button: indicate if the css bundle is started.
	 */
	@FXML
	private RadioMenuItem radioMenuCSS;

	/**
	 * The button "À propos" have been clicked.
	 *
	 * @param event
	 */
	@FXML
	void fireMenuAPropos(ActionEvent event) {
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(primaryStage);
		final VBox dialogVbox = new VBox(50);
		dialogVbox.setAlignment(Pos.CENTER);
		dialogVbox.getChildren().add(new Text("This is a modulable code viewer"));
		final Scene dialogScene = new Scene(dialogVbox, 300, 80);
		dialog.setScene(dialogScene);
		dialog.show();
	}

	@FXML
	void fireMenuCloseFile(ActionEvent event) {
		webViewer.getEngine().loadContent("");
	}

	@FXML
	void fireMenuExit(ActionEvent event) {
		System.exit(0);
	}

	/**
	 * The button to load a bundle have been clicked.
	 *
	 * @param event
	 */
	@FXML
	void fireMenuLoadBundle(ActionEvent event) {
		final FileChooser fileChooser = new FileChooser();
		final File selectedFile = fileChooser.showOpenDialog(primaryStage);

		/*
		 * TODO complete this section to load the selected bundle.
		 */
		if (selectedFile != null) {
			Activator.logger.info("File selected: " + selectedFile.toURI().toString());
			try {
				// bundles.add(
				bundleContext.installBundle(selectedFile.toURI().toString());
			} catch (BundleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Activator.logger.info("File selection cancelled.");
		}
	}

	List<Bundle> bundles = new ArrayList<>();
	File selectedFile;

	/**
	 * The button to open a file have been clicked.
	 *
	 * @param event
	 */
	@FXML
	void fireMenuOpenFile(ActionEvent event) {
		webViewer.getEngine().loadContent("");
		final FileChooser fileChooser = new FileChooser();
		selectedFile = fileChooser.showOpenDialog(primaryStage);

		if (selectedFile != null) {
			fileName = selectedFile.getName();
			Activator.logger.info("File selected: " + selectedFile.getPath());
			try {
				InputStream ips = new FileInputStream(selectedFile);
				InputStreamReader ipsr = new InputStreamReader(ips);
				BufferedReader br = new BufferedReader(ipsr);
				String ligne;
				content = "<html><head></head><body>";
				while ((ligne = br.readLine()) != null) {
					content += ligne + "<br />";
				}
				br.close();
				content += "</body></html>";
				webViewer.getEngine().loadContent(content);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		} else {
			Activator.logger.info("File selection cancelled.");
		}
	}

	String content;
	String fileDecor;

	@FXML
	void fireRadioMenuCSS(ActionEvent event) {
		/*
		 * If the css bundle is stated -> stop it otherwise start it (if it has
		 * been loaded before)
		 */
		Bundle cssBundle = bundleContext.getBundle("m2dl.osgi.coloration.css");
		// ( ( MyService ) context . ge tSe r vice ( re ferences [ 0 ] ) ) .
		// sayHello ( ) ;
		// Optional<IColorService> service =
		// Activator.getInstance().getColorationService("CSS");
		// System.out.println(service);
		try {
			ServiceReference<?>[] references = bundleContext.getServiceReferences(IColorService.class.getName(),
					"(type=*)");
			ServiceReference<?> sr = bundleContext.getServiceReference(IColorService.class);
			System.out.println("test : " + sr);
			// String[] propertyKeys = sr.getPropertyKeys();
			// System.out.println(bundleContext.getService(IColorService.class));
			// for (String key : propertyKeys) {
			// Object prop = sr.getProperty(key);
			System.out.println(sr);
			// think about whether this is the Foo we want....
			// }
		} catch (InvalidSyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (cssBundle != null) {
			System.out.println(cssBundle.getState());
			if (cssBundle.getState() == Bundle.STARTING) {
				try {
					cssBundle.stop();
				} catch (BundleException e) {
					e.printStackTrace();
				}
			} else {
				try {
					cssBundle.start();
					try {
						Thread.currentThread();
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (BundleException e) {
					e.printStackTrace();
				}
				// Optional<IColorService> service =
				// Activator.getInstance().getColorationService("Css");
				/*
				 * if (service.isPresent() && fileDecor != null) { String
				 * fileCSS = service.get().colorize(fileDecor);
				 * webViewer.getEngine().loadContent("");
				 * webViewer.getEngine().loadContent(fileCSS); }
				 */
			}
		}
	}

	@FXML
	void fireRadioMenuDecorator(ActionEvent event) {
		/*
		 * If the decorator bundle is stated -> stop it otherwise start it (if
		 * it has been loaded before)
		 */
		Bundle decoBundle = bundles.get(0);
		if (decoBundle != null && decoBundle.getState() == Bundle.STARTING) {
			try {
				decoBundle.stop();
			} catch (BundleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			decoBundle.start();
			try {
				Thread.currentThread();
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (BundleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Optional<IDecorationService> service = Activator.getInstance().getDecorationService();
		if (service.isPresent()) {
			fileDecor = service.get().decorate(selectedFile);
			webViewer.getEngine().loadContent("");
			webViewer.getEngine().loadContent(fileDecor);
		}
	}

	@FXML
	void fireRadioMenuJava(ActionEvent event) {
		/*
		 * If the Java bundle is started -> stop it otherwise start it (if it
		 * has been loaded before)
		 */
		Bundle javaBundle = bundles.get(1); // FIXME: differencier les bundles
		if (javaBundle != null && javaBundle.getState() == Bundle.STARTING) {
			try {
				javaBundle.stop();
			} catch (BundleException e) {
				e.printStackTrace();
			}
		}
		try {
			javaBundle.start();
			try {
				Thread.currentThread();
				Thread.sleep(9000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (BundleException e) {
			e.printStackTrace();
		}
		// Optional<IColorService> service =
		// Activator.getInstance().getColorationService("Java");
		/*
		 * if (service.isPresent() && fileDecor != null) { String fileJava =
		 * service.get().colorize(fileDecor);
		 * webViewer.getEngine().loadContent("");
		 * webViewer.getEngine().loadContent(fileJava); }
		 */
	}

	@FXML
	void initialize() {
		assert radioMenuJava != null : "fx:id=\"radioMenuJava\" was not injected: check your FXML file 'main-window-exercice.fxml'.";
		assert radioMenuDecorator != null : "fx:id=\"radioMenuDecorator\" was not injected: check your FXML file 'main-window-exercice.fxml'.";
		assert webViewer != null : "fx:id=\"webViewer\" was not injected: check your FXML file 'main-window-exercice.fxml'.";
		assert radioMenuCSS != null : "fx:id=\"radioMenuCSS\" was not injected: check your FXML file 'main-window-exercice.fxml'.";

	}

	public void setPrimaryStage(final Stage _stage) {
		primaryStage = _stage;
	}

}
