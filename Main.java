package application;
	
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;


public class Main extends Application{
		
	Stage window;
	boolean starting = true;
	GridPane gp = new GridPane();
	CellGrid gameBoard;
	Label mineLabel;
	Label flagLabel;
	int flags;
	StringProperty flagsStr = new SimpleStringProperty();
	
	@Override
	public void start(Stage primaryStage) {
		try {
			window = primaryStage;
			window.setTitle("Minesweeper");
			
			VBox vbox = new VBox();
			vbox.setPadding(new Insets(10, 10, 10, 10));
			
			HBox hbox = new HBox();
			hbox.setPadding(new Insets(5, 5, 5, 5));
			
			mineLabel = new Label();
			mineLabel.setPadding(new Insets(0, 10, 0, 0));
			
			flagLabel = new Label();
			flagLabel.textProperty().bind(flagsStr);
			
			hbox.getChildren().addAll(mineLabel, flagLabel);
		
			for(int i = 0; i < 16; i++) {
				for(int j = 0; j < 16; j++) {
					int x = i, y = j;
					Button button = new Button();
					button.setMinHeight(30);
					button.setMinWidth(30);
					button.setOnMouseClicked(event -> {
							if(event.getButton() == (MouseButton.PRIMARY))
								handlePress(x, y);
							else if(event.getButton() == (MouseButton.SECONDARY)) 
								giveOrRemoveFlag(x, y);
					});
					gp.add(button, i, j);
				}
			}
			gp.setHgap(2);
			gp.setVgap(2);
			
			vbox.getChildren().addAll(hbox, gp);
			
			Scene scene = new Scene(vbox);
						
			window.setScene(scene);
			window.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private void handlePress(int x, int y) {
		if(starting) {
			gameBoard = new CellGrid(256);
			gameBoard.setUpGameAfterFirstClick(x, y);
			mineLabel.setText("Mines: " + gameBoard.getNumberOfMines());
			flags = gameBoard.getNumberOfMines();
			flagsStr.setValue("Flags: " + flags);
			starting = false;
		} else {
			if(gameBoard.getCellAt(x, y).hasMine()) {
				revealBombs();
				if(AlertBox.display("BOOM!", "You Lost... Want to try again?")) 
					clearBoardForNewGame();
				else
					window.close();
			} else {
				gameBoard.unCoverCell(x, y);
				if(!gameBoard.getCellAt(x, y).isCovered() && gameBoard.getCellAt(x, y).hasFlag()) {
					gameBoard.getCellAt(x, y).removeFlag();
					flags++;
				}
				if(gameBoard.checkForGameWin()) {
					if(AlertBox.display("Winner!", "You Win! Want to try another?"))
						clearBoardForNewGame();
					else 
						window.close();
				}
			}
		}
		drawBoard();
	}
	
	// gives a flag if there is none but will remove a flag if a cell does have one
	private void giveOrRemoveFlag(int x, int y) {
		if(starting) return;
		if(!gameBoard.getCellAt(x, y).hasFlag() && gameBoard.getCellAt(x, y).isCovered() && flags > 0) {
			gameBoard.getCellAt(x, y).giveFlag();
			flags--;
		} else if(gameBoard.getCellAt(x, y).hasFlag() && gameBoard.getCellAt(x, y).isCovered()) {
			gameBoard.getCellAt(x, y).removeFlag();
			flags++;
		}
		drawBoard();
	}
	
	private void drawBoard() {
		for(int i = 0; i < 16; i++) {
			for(int j = 0; j < 16; j++) {
				int x = i, y = j;
				Button button = new Button("");
				if(gameBoard.getCellAt(i, j).isCovered() && !gameBoard.getCellAt(i, j).hasFlag())
					button.setStyle("-fx-background-color: #999999");
				else if(gameBoard.getCellAt(i, j).isCovered() && gameBoard.getCellAt(i, j).hasFlag()) {
					button.setStyle("-fx-background-color: #999999");
					button.setText("F");
				}
				else if(!gameBoard.getCellAt(i, j).isCovered() && gameBoard.getCellAt(i, j).getMineNumber() > 0) {
					button.setText("" + gameBoard.getCellAt(i, j).getMineNumber());
					if(gameBoard.getCellAt(i, j).getMineNumber() == 1)
						button.setStyle("-fx-text-fill: #0b5394");
					else if(gameBoard.getCellAt(i, j).getMineNumber() == 2)
						button.setStyle("-fx-text-fill: #4c237e");
					else if(gameBoard.getCellAt(i, j).getMineNumber() == 3)
						button.setStyle("-fx-text-fill: #f8a110");
					else if(gameBoard.getCellAt(i, j).getMineNumber() >= 4)
						button.setStyle("-fx-text-fill: #cc0000");
				}
				button.setMinHeight(30);
				button.setMinWidth(30);
				button.setOnMouseClicked(event -> {
					if(event.getButton() == (MouseButton.PRIMARY))
						handlePress(x, y);
					else if(event.getButton() == (MouseButton.SECONDARY)) 
						giveOrRemoveFlag(x, y);
				});
				gp.add(button, i, j);
				flagsStr.setValue("Flags: " + flags);
			}
		}
	}
	
	private void clearBoardForNewGame() {
		gameBoard = new CellGrid(256);
		starting = true;
		drawBoard();
	}
	
	private void revealBombs() {
		for(int i = 0; i < 16; i++) {
			for(int j = 0; j < 16; j++) {
				int x = i, y = j;
				Button button = new Button();
				if(gameBoard.getCellAt(x, y).hasMine())
					button.setText("B");
				button.setMinHeight(30);
				button.setMinWidth(30);
				gp.add(button, x, y);
			}
		}
	}
	
	
	
}