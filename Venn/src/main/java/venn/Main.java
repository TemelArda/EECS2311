package venn;



import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application{
	public static final int WIDTH = 1200;
	public static final int HEIGHT = 960;
	public static double sWidth;
	public static double sHeight;
	public static Stage primaryStage;
	Scene s;
	@Override
	public void start(Stage primaryStage) throws Exception {

		Parent fxml = FXMLLoader.load(getClass().getResource("App.fxml"));
		s = new Scene(fxml, WIDTH, HEIGHT);
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("VENN DIAGRAM");
		this.primaryStage.setScene(s);
		this.primaryStage.show();
		sWidth = s.getWidth();
		sHeight = s.getHeight();

	}
	public static void showAddStage() {
		Parent fxml;
		Parent fxml2;
		try {
			fxml = FXMLLoader.load(Main.class.getResource("getData.fxml"));
			

			Stage secondStage = new Stage();
			secondStage.setTitle("Enter Data");
			secondStage.initModality(Modality.WINDOW_MODAL);
			secondStage.setScene(new Scene(fxml));
			
			secondStage.initOwner(primaryStage);
			secondStage.showAndWait();;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void showEditStage() {
		Parent fxml;
		try {
			fxml = FXMLLoader.load(Main.class.getResource("EditText.fxml"));
			Stage secondStage = new Stage();
			secondStage.setTitle("Edit the Entry");
			secondStage.initModality(Modality.WINDOW_MODAL);
			secondStage.setScene(new Scene(fxml));
			secondStage.initOwner(primaryStage);
			secondStage.showAndWait();;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[]args) {
		launch(args);
		
	}

}	
