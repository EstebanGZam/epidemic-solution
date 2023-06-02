package model;

import com.example.epidemicsolution.controller.ImplementationType;
import com.example.epidemicsolution.model.Map;
import javafx.scene.control.RadioButton;
import org.junit.jupiter.api.Test;

public class MapTest {
	private Map map;
	private RadioButton palmiraRB, praderaRB, candelariaRB, floridaRB, caliRB;


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

}
