package model;

import com.example.epidemicsolution.controller.ImplementationType;
import com.example.epidemicsolution.model.Map;
import javafx.application.Application;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MapTest {
	private static Map map;
	private RadioButton palmiraRB, praderaRB, candelariaRB, floridaRB, caliRB;

	@BeforeAll
	public static void initJavaFX() {
		Thread thread = new Thread(() -> Application.launch(JavaFXTestApplication.class));
		thread.setDaemon(true);
		thread.start();
	}

	public static class JavaFXTestApplication extends Application {
		@Override
		public void start(Stage primaryStage) {
		}
	}


	public void setupStage1() {
		map = Map.getInstance(ImplementationType.LIST);
		// Initialization of the radioButtons
		palmiraRB = new RadioButton();
		praderaRB = new RadioButton();
		candelariaRB = new RadioButton();
		floridaRB = new RadioButton();
		caliRB = new RadioButton();

		// Set the position of the radio buttons with their corresponding coordinates on the loaded map.
		palmiraRB.setLayoutX(438);
		palmiraRB.setLayoutY(532);

		praderaRB.setLayoutX(464);
		praderaRB.setLayoutY(569);

		candelariaRB.setLayoutX(401);
		candelariaRB.setLayoutY(586);

		floridaRB.setLayoutX(472);
		floridaRB.setLayoutY(607);

		caliRB.setLayoutX(336);
		caliRB.setLayoutY(578);

	}

	@Test
	public void testUpdateRoute1() {
		setupStage1();
		Assertions.assertTrue(map.updateRoute(praderaRB, candelariaRB, 50));
	}

	// Cali and pradera do not have a direct connection route, so it is expected that a false
	@Test
	public void testUpdateRoute2() {
		setupStage1();
		Assertions.assertFalse(map.updateRoute(praderaRB, caliRB, 50));
	}

	@Test
	public void testDistanceBetweenTwoCities1() {
		setupStage1();
		Assertions.assertEquals(1, map.distanceBetweenTwoCities(palmiraRB, praderaRB));
	}

	// Verify that the intermediate quarantine centers from one city to another are the same regardless of
	// the city marked as start and destination.
	@Test
	public void testDistanceBetweenTwoCities2() {
		setupStage1();

		Assertions.assertEquals(2, map.distanceBetweenTwoCities(caliRB, floridaRB));
		Assertions.assertEquals(2, map.distanceBetweenTwoCities(floridaRB, caliRB));
	}

	// There are no intermediate quarantine.
	@Test
	public void testDistanceBetweenTwoCities3() {
		setupStage1();

		Assertions.assertEquals(0, map.distanceBetweenTwoCities(floridaRB, floridaRB));
	}


}
