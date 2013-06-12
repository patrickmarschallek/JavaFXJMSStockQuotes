package ui.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Dialog extends Stage {

	private Dialog() {
		super();
	}

	public static void showErrorDialog(final Stage stg, String message,
			String title, String type) {
		final Dialog dlg = new Dialog();
		Button btn = new Button();

		dlg.setTitle(title);

		// Initialize the Stage with type of modal
		dlg.initModality(Modality.APPLICATION_MODAL);

		// Set the owner of the Stage
		dlg.initOwner(stg);
		dlg.setTitle("Top Stage With Modality");
		Group root = new Group();
		Scene scene = new Scene(root, 300, 250, Color.LIGHTGREEN);

		btn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				dlg.hide();
				dlg.close();
			}
		});

		btn.setLayoutX(100);
		btn.setLayoutY(80);
		btn.setText("OK");

		root.getChildren().add(btn);
		dlg.setScene(scene);
		dlg.show();
	}
}
