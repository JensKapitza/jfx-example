package jfx;

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lang.ResourceBundleProperty;

public class Main extends Application {
	public static void main(String[] args) {
		Application.launch(args);
	}

	ResourceBundleProperty resources;
	private Scene scene;

	@Override
	public void start(Stage primaryStage) throws Exception {

		// Sprache laden
		System.out.println(Locale.GERMAN);
		resources = new ResourceBundleProperty(ResourceBundle.getBundle(
				"lang.lang", Locale.GERMAN));
		primaryStage.titleProperty().bind(resources.get("title"));

		Button b = new Button();
		b.textProperty().bind(resources.get("button"));
		b.setOnAction(ae -> {
			System.out.println(Locale.ENGLISH);
			scene.getStylesheets().clear();
			if (resources.getLocale().getLanguage().contains("en")) {

				resources.setResource(ResourceBundle.getBundle("lang.lang",
						Locale.GERMAN));
				scene.getStylesheets().add("/styles/default/css/default.css");
			} else {
				resources.setResource(ResourceBundle.getBundle("lang.lang",
						Locale.ENGLISH));
				scene.getStylesheets().add("/styles/t1/css/default.css");
			}

		});
		FXMLLoader loader = new Loader(getClass().getResource(
				"/fxml/Scene.fxml"), resources);

		AnchorPane root = loader.load();
		root.getChildren().add(b);
		scene = new Scene(root);
		scene.getStylesheets().add("/styles/default/css/default.css");
		primaryStage.setScene(scene);
		primaryStage.setWidth(400);
		primaryStage.setHeight(400);
		primaryStage.show();
	}
}
