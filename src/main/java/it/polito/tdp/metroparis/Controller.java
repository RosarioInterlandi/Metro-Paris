package it.polito.tdp.metroparis;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.metroparis.model.Fermata;
import it.polito.tdp.metroparis.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class Controller {
	private Model model;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ComboBox<Fermata> boxArrivo;

	@FXML
	private ComboBox<Fermata> boxPartenza;

	@FXML
	private TextArea txtResult;

	@FXML
	void handleCalcola(ActionEvent event) {
		Fermata partenza = boxPartenza.getValue();
		Fermata arrivo = boxArrivo.getValue();
		if (partenza != null && arrivo != null && !partenza.equals(arrivo)) {
			List<Fermata> result = this.model.percorso(partenza, arrivo);
			txtResult.appendText("Percorso tra "+partenza.getNome()+" e stazione "+arrivo.getNome()+"\n\n");
			for (Fermata f : result) {
				 txtResult.appendText(f.getNome()+"\n");
			}
		}else {
			txtResult.appendText("Selezionare due stazioni diverse tra loro \n");
		}
	}

	@FXML
	void handleCrea(ActionEvent event) {
		this.model.creaGrafo();
		if (this.model.isGrafoLoaded()) {
			txtResult.setText("Grafo correttamente importato");
		}
	}

	@FXML
	void initialize() {
		assert boxArrivo != null : "fx:id=\"boxArrivo\" was not injected: check your FXML file 'Metro.fxml'.";
		assert boxPartenza != null : "fx:id=\"boxPartenza\" was not injected: check your FXML file 'Metro.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Metro.fxml'.";

	}

	public void setModel(Model m) {
		this.model = m;
		List<Fermata> fermate = this.model.getAllFermate();
		boxPartenza.getItems().setAll(fermate);
		boxArrivo.getItems().setAll(fermate);
	}

}
