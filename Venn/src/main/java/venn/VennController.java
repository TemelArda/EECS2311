
package venn;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.assertj.core.util.Arrays;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;





public class VennController {
	@FXML
	private Button newEntry;
	@FXML
	private Pane textSpace;
	@FXML
	private Button dlt;
	@FXML 
	private AnchorPane pane;
	@FXML
	private Button selectFile;
	@FXML
	private MenuItem answer;
	@FXML 
	private MenuItem deleteSet;
	@FXML
	private Button submit;
	@FXML
	private Button ansLabels;
	
	static SetCircle cir1;
	static SetCircle cir2;	
	Rectangle selection;
	

	@FXML
	private static double counter;

	private static final int MAX_RAD = 225;

	double rectX, rectY;
	boolean selecting = true;
	
	class DragContext {
        double x;
        double y;
        DragContext(double x, double y){
        	this.x = x;
        	this.y = y;
        }
    }
	 
	private ArrayList<DragContext> multipleDrag = new ArrayList<DragContext>();
	
	private List<String> answerSet1 = new ArrayList<String>();
	private ArrayList<String> answerSet2 = new ArrayList<String>();
	
	private static DraggableText selected = null;
	public static ArrayList<DraggableText> entries = new ArrayList<DraggableText>();
	public ArrayList<DraggableText> selectedTxts = new ArrayList<DraggableText>();
	
	
	@FXML
	private void initialize() {

		Main.calculateSceneSize();
		int radius = MAX_RAD;
		Color c1 = Color.web("#b4ffff");
		Color c2 = Color.web("#ffc4ff");
		System.out.println("sWidth: " + Main.sWidth + "sHeight: " + Main.sHeight);
		double px = Main.sWidth/2 ;//+ 50;
		double py = Main.sHeight/2 +  (2*radius)/4;
		cir1 = new SetCircle(px - 150, py, radius,"Set1", c1);
		cir2 = new SetCircle(px + 150, py, radius,"Set2", c2);
		Group circles = new Group();
		circles.getChildren().addAll(cir1, cir2, cir1.getName(), cir1.getNum(), cir2.getName(), cir2.getNum());
		pane.getChildren().add(circles);
		
		counter=1.0;



		pane.setOnMousePressed(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent m){
				boolean found = false;
				for(int i = 0; i < entries.size() && !found; i++) {
					if(entries.get(i).getBoundsInParent().contains(m.getX(), m.getY())) {
						selected = (DraggableText) entries.get(i);
						found = true;
						
					}else {
						selected = null;
					}
				}

				selection = new Rectangle();
				rectX = m.getX();
				rectY = m.getY();
				selection.setX(rectX);
				selection.setY(rectY);
				selection.setFill(Color.AQUA);
				selection.setOpacity(0.5);
				pane.getChildren().add(selection);
				if(selectedTxts.size() > 0) {
					for(DraggableText txt:selectedTxts) {
						double x = txt.getTranslateX() - m.getSceneX();
						double y = txt.getTranslateY() - m.getSceneY();
						multipleDrag.add(new DragContext(x, y));
					}
				}
			}
		});
		
		
		pane.setOnMouseReleased(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent m){
				if(selected !=null) {
					for(int i =0; i < entries.size(); i++) {
						if(selected.collision(entries.get(i)) && entries.get(i) != selected){
							selected.setTranslateX(entries.get(i).getBoundsInParent().getMaxX() + 10);
						}
					}
					if(selected.collision(dlt)) {
						pane.getChildren().remove(selected);
						entries.remove(selected);
					}
				}
				if(selection != null) {
					pane.getChildren().remove(selection);
					selection = null;
				}
				if(selectedTxts.size() > 0 && selecting) {
					for(DraggableText lbl:selectedTxts) {
						lbl.changeColor(lbl.getColor().darker());
					
						}
					selecting = false;
				}else if(!selecting && selectedTxts.size() > 0) {

					for(DraggableText lbl : selectedTxts) {
						lbl.changeColor(lbl.getColor().brighter());
						
					}
					multipleDrag.removeAll(multipleDrag);
					selectedTxts.removeAll(selectedTxts);
					selecting = true;
				}
				
			}
		});
		pane.setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent m) {
				
				if(selected != null || selectedTxts.size() > 0) {
					if(selected != null) {
						if(cir1.inBound(selected) && !cir1.isElem(selected)) {
							cir1.addElem(selected);
							System.out.println(cir1.elems.toString());
						}else if(!cir1.inBound(selected) && cir1.isElem(selected)) {
							cir1.removeElem(selected);
						}
						if(cir2.inBound(selected)&& !cir2.isElem(selected)) {
							cir2.addElem(selected);
						
						}else if(!cir2.inBound(selected) && cir2.isElem(selected)) {
							cir2.removeElem(selected);
							
						}
					} 
					if(selectedTxts.size() > 0){
						for(DraggableText txt:selectedTxts) {
							if(cir1.inBound(txt) && !cir1.isElem(txt)) {
								cir1.addElem(txt);
						
								System.out.println(cir1.elems.toString());
							}else if(!cir1.inBound(txt) && cir1.isElem(txt)) {
								cir1.removeElem(txt);
								
							}
							if(cir2.inBound(txt)&& !cir2.isElem(txt)) {
								cir2.addElem(txt);
							}else if(!cir2.inBound(txt) && cir2.isElem(txt)) {
								cir2.removeElem(txt);
							}
						}
					}
					
				} 
				if(selecting && selected == null){
					
					if(m.getX() > rectX && m.getY() > rectY) {
						selection.setWidth( m.getX()- rectX);
						selection.setHeight( m.getY()- rectY);
					}else if(m.getX() < rectX && m.getY() < rectY){
						selection.setX(m.getX());
						selection.setWidth(rectX - m.getX());
						selection.setY(m.getY());
						selection.setHeight(rectY - m.getY());
						
					}else if(m.getY() < rectY){
						selection.setY(m.getY());
						selection.setHeight(rectY - m.getY());
						selection.setWidth( m.getX()- rectX);
					}else {
						selection.setX(m.getX());
						selection.setWidth(rectX - m.getX());
						selection.setHeight( m.getY()- rectY);
					}
					for(DraggableText lbl:entries) {
						if(!selectedTxts.contains(lbl) && selection.contains(lbl.getBoundsInParent().getMinX(), lbl.getBoundsInParent().getMinY())) {
							selectedTxts.add(lbl);
						}else if(selectedTxts.contains(lbl) && !selection.contains(lbl.getBoundsInParent().getMinX(), lbl.getBoundsInParent().getMinY())) {
							selectedTxts.remove(lbl);
						}
					}
				}if(!selecting) {
					int i = 0;
					for(DraggableText txt:selectedTxts) {
						double newX = m.getSceneX() + multipleDrag.get(i).x;
			            double newY = m.getSceneY() + multipleDrag.get(i).y;
						txt.setTranslateX(newX);
						txt.setTranslateY(newY);
						i++;
					}
				}
				if(cir1.localToScene(cir1.getBoundsInLocal()).contains(new Point2D(m.getSceneX(), m.getSceneY()))) {
					if(cir1.getSetSize() > 0)
						cir1.setOpacity(0.8);
				}else {
					if(cir1.getSetSize() == 0)
						cir1.setOpacity(0.5);
				}
				if(cir2.localToScene(cir2.getBoundsInLocal()).contains(new Point2D(m.getSceneX(), m.getSceneY()))) {
					if(cir2.getSetSize() > 0)
						cir2.setOpacity(0.8);
				}else {
					if(cir2.getSetSize() == 0)
						cir2.setOpacity(0.5);
				}
			}
		});
		
		pane.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {				
                if (event.getCode() == KeyCode.BACK_SPACE) {
                	deleteSelected();
                	System.out.println("Deleting selected");
                }
            }
		});
		
	}
	public static DraggableText getSelected() {
		return selected;
	}
	
	public void openNewScene(ActionEvent e) {
		Main.showAddStage();
	}
	
	public void exitProgram()
	{
		Platform.exit();
	}
	public String captureData(ActionEvent event)
	{	
		String path = "";
		Color c = Color.WHITE;
		DraggableText newTxt;
		FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        try {
        path = selectedFile.getPath();
        }
        catch(NullPointerException e){
        	return "null pointer";
        }
       
        File file = new File(path);
        BufferedReader br;
		try {
			String st;
			br = new BufferedReader(new FileReader(file));
			
			while ((st = br.readLine()) != null) {

				  System.out.println(st);

				  	 newTxt = new DraggableText(st, c, 400);
					 newTxt.setFont(Font.font("Roboto Slab", FontWeight.NORMAL, 15));
					 newTxt.getStyleClass().add("createdText");
					 Pane ts = (Pane) pane.lookup("#textSpace");
					 
					 this.findEmpty(newTxt);
					 VennController.entries.add(newTxt);

					 pane.getChildren().add(newTxt);


//					 System.out.print("x:"+newTxt.getLayoutX()+"   y: "+newTxt.getLayoutY()+"\n");
//					 System.out.print("x:"+x+"   y: "+y+"\n");
//					 System.out.print(counter+" "+counter/16);
					 counter++;
					 
				}
		}        
         catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		return path;
	}
	public String exportData(ActionEvent event) throws FileNotFoundException
	{
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt, extensions)","*.txt");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(null);
//		if(file != null)
//		{
//			
//		}
			for(DraggableText a:VennController.entries) {
				SaveFile(a.getText()+"\n",file);
			}
		BufferedReader rd = new BufferedReader(new FileReader(file));
		try {
			System.out.println("\n"+rd.readLine());
		}
		catch(Exception ex){
			
		}
	
		return "";
	}
	
	private void SaveFile(String content, File file){
        try {
            FileWriter fileWriter;
           
            fileWriter = new FileWriter(file,true);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
           
        }
          
    }	
	
	
	public void getAnswers() {

		String path = "";
		FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        try {
        path = selectedFile.getPath();
        }
        catch(NullPointerException e){
        	System.out.println("couldn't get the path");
        }
        File file = new File(path);
        BufferedReader br;
		try {
			String st;
			int i = 0;
			br = new BufferedReader(new FileReader(file));

			while ( i < 2 && ((st = br.readLine()) != null)) {
				String [] temp = st.split("\\s+");
				if(i == 0 ) {
					Collections.addAll(answerSet1, temp);
				}else {
					Collections.addAll(answerSet2, temp);
				}
				i++;
			}
			Alert a = new Alert(AlertType.INFORMATION);
			a.setTitle("Answer information");
			if(!answerSet1.isEmpty() && !answerSet2.isEmpty()) {
				a.setHeaderText("Answers have been set and saved.");
			}else {
				a.setHeaderText("Answers couldn't been saved please try again.");
			}
			a.showAndWait();
			Collections.sort(answerSet1);
			Collections.sort(answerSet2);
			System.out.println("Set1: " + answerSet1.toString());
			System.out.println("Set2: " + answerSet2.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void deleteAnswerSets() {
		if(!answerSet1.isEmpty())
			answerSet1.removeAll(answerSet1);
		if(!answerSet2.isEmpty())
			answerSet2.removeAll(answerSet2);
		Alert a = new Alert(AlertType.INFORMATION);
		a.setTitle("Answer information");
		a.setHeaderText("Answers were deleted you may add a new set of answers");
		a.showAndWait();
	}
	private void deleteSelected() {
		if(!selecting && this.selectedTxts.size() > 0) {
			for(DraggableText t: this.selectedTxts) {
				
				cir1.removeElem(t);
				cir2.removeElem(t);
				entries.remove(t);
				pane.getChildren().remove(t);
			}
			this.selectedTxts.removeAll(this.selectedTxts);
			this.selecting = true;
		}
	}
	public static void sceneChanged() {
		cir1.setCenter(Main.s.getWidth()/2, Main.s.getHeight()/2 +  (2*cir1.getRadius())/4);
		cir2.setCenter((cir1.getCenterX()  + cir1.getRadius() ), Main.s.getHeight()/2 +  (2*cir1.getRadius())/4 );
	}

	public void submit() {
		Alert a = new Alert(AlertType.ERROR);
		a.setTitle("Submit Error");
		if(cir1.elems.isEmpty() || cir2.elems.isEmpty()) {
			a.setHeaderText("One of the sets are Empty");
			a.showAndWait();
		}else if(answerSet1.isEmpty() || answerSet2.isEmpty()) {
			a.setHeaderText("Answer sets are empty. Import answers using Edit > Add Answers.");
			a.showAndWait();
		}else {
			if(answerSet1.equals(cir1.getSetText()) && answerSet2.equals(cir2.getSetText())) {
				a.setAlertType(AlertType.CONFIRMATION);
				a.setHeaderText("Correct answer!!");
				a.showAndWait();
			}else {
				a.setAlertType(AlertType.CONFIRMATION);
				a.setHeaderText("Sorry wrong answer, Try again.");
				a.showAndWait();
			}
		}
	}
	public void getAnswerLabels() {
		counter = 0;
		Alert a = new Alert(AlertType.ERROR);
		int i = 0; int j = 0;
		double k = 0;
		DraggableText newTxt;
		if(answerSet1.isEmpty() || answerSet2.isEmpty()) {
			a.setTitle("Answers could not be imported");
			a.setHeaderText("Answer sets have not been imported.");
			a.showAndWait();
		}
			
		if(entries.size() > 0) {
			a.setTitle("Answers could not be imported");
			a.setHeaderText("Can only import answers if there are no other labels in the scene.");
			a.showAndWait();
		}else {
			while(i < answerSet1.size() && j < answerSet2.size()) {
				k = Math.floor(Math.random() * 2);
				if(k == 1) {
					newTxt = new DraggableText(answerSet1.get(i), Color.WHITE, 400);
					i++;
				}else {
					newTxt = new DraggableText(answerSet2.get(j), Color.WHITE, 400);
					j++;
				}
				newTxt.setFont(Font.font("Roboto Slab", FontWeight.NORMAL, 15));
				newTxt.getStyleClass().add("createdText");
				findEmpty(newTxt);
				VennController.entries.add(newTxt);
				pane.getChildren().add(newTxt);
				counter++;
			}
			while(j < answerSet2.size()) {
				newTxt = new DraggableText(answerSet2.get(j), Color.WHITE, 400);
				j++;
				newTxt.setFont(Font.font("Roboto Slab", FontWeight.NORMAL, 15));
				newTxt.getStyleClass().add("createdText");
				findEmpty(newTxt);
				VennController.entries.add(newTxt);
				pane.getChildren().add(newTxt);
				counter++;
			}
			while(i < answerSet1.size()) {
				newTxt = new DraggableText(answerSet1.get(i), Color.WHITE, 400);
				i++;
				newTxt.setFont(Font.font("Roboto Slab", FontWeight.NORMAL, 15));
				newTxt.getStyleClass().add("createdText");
				findEmpty(newTxt);
				VennController.entries.add(newTxt);
				pane.getChildren().add(newTxt);
				counter++;
			}
		}
	}
	
	private void findEmpty(DraggableText newTxt) {
		 double x = textSpace.getBoundsInParent().getMinX();
		 double y = textSpace.getBoundsInParent().getMinY();
		
		 if(VennController.entries.size() != 0) {
			 DraggableText prev = VennController.entries.get(VennController.entries.size() - 1);
			 if(entries.size()/16.0 <=1) {
			 newTxt.setTranslateX(x);
			 newTxt.setTranslateY(prev.getBoundsInParent().getMaxY() + 30);
			 }
			 else if(entries.size()/16.0 ==2 || counter/16.0==3 || counter/16.0==4)
			 {
				 DraggableText prev1 = VennController.entries.get(15);
				 newTxt.setTranslateX(prev1.getBoundsInParent().getMinX() + 150*(counter/16.0-1));
				 newTxt.setTranslateY(prev1.getBoundsInParent().getMaxY());
			 }
			 else if(counter/16.0 > 1 && counter/16.0 <2) {
				 DraggableText prev1 = VennController.entries.get((int)counter%16-1);
				 newTxt.setTranslateX(prev1.getBoundsInParent().getMinX() + 150);
				 newTxt.setTranslateY(prev1.getBoundsInParent().getMaxY());
				 
			 }
			 else if(counter/16.0 > 2 && counter/16.0<3) {
				 DraggableText prev1 = VennController.entries.get((int)counter%16-1);
				 newTxt.setTranslateX(prev1.getBoundsInParent().getMinX() + 300);
				 newTxt.setTranslateY(prev1.getBoundsInParent().getMaxY());
				 
			 }
			 else if(counter/16.0 > 3 && counter/16.0<4) {
				 DraggableText prev1 = VennController.entries.get((int)counter%16-1);
				 newTxt.setTranslateX(prev1.getBoundsInParent().getMinX() + 450);
				 newTxt.setTranslateY(prev1.getBoundsInParent().getMaxY());							 
			 }
			 else if(counter/16.0 > 4 && counter/16.0<5) {
				 DraggableText prev1 = VennController.entries.get((int)counter%16-1);
				 newTxt.setTranslateX(prev1.getBoundsInParent().getMinX() + 600);
				 newTxt.setTranslateY(prev1.getBoundsInParent().getMaxY());							 
			 }
		 }else {
		 newTxt.setTranslateX(x);

		 newTxt.setTranslateY(y);
		 }
	}
	
}
