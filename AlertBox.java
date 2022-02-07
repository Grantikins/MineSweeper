package application;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class AlertBox {
	
	private static boolean playAnother = true;

	public static boolean display(String title, String message) {
		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);  // This function blocks interaction with other windows so that the user can only interact with this window
		window.setTitle(title);
		window.setMinWidth(250);
		window.setMinHeight(150);
		
		Label label = new Label();
		label.setText(message);
		
		Button replayButton = new Button("Play Another");
		replayButton.setOnAction(e -> {
			playAnother = true;
			window.close();
		});
		Button closeButton = new Button("Close");
		closeButton.setOnAction(e -> {
			playAnother = false;
			window.close();
		});
		
		HBox hbox = new HBox(10);
		hbox.getChildren().addAll(replayButton, closeButton);
		hbox.setAlignment(Pos.CENTER);
		
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, hbox);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		window.setScene(scene); 
		window.showAndWait();
		
		return playAnother;
	}
	
}
