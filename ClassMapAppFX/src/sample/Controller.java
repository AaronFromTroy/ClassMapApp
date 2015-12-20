package sample;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Optional;

public class Controller {

    public Slider zoomSlider;
    public TitledPane mainStage;
    public GridPane root;
    public Pane nodeStage;
    public ArrayList<MapNode> masterNodeList = new ArrayList<MapNode>();

    public void createTextNode(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog("Enter the text for the node");
        dialog.setTitle("Create Text Node");
        dialog.setHeaderText("Enter the text below.");
        dialog.setContentText("Text: ");
        Optional<String> result = dialog.showAndWait();

        TextNode newNode = new TextNode(result.get());
        DataConnection.addTextNode(newNode);

        newNode.setTypeToText();

        //masterNodeList.add(newNode); I think this is still necessary but not for the database

        nodeStage.getChildren().add(newNode.getNodePane());

    }

    public void printMasterList()
    {
        int size = masterNodeList.size();

        for(int x = 0; x < size; x++)
        {
            System.out.println(masterNodeList.get(x).getSeconds());
        }
    }


    public void createImageNodeURL(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog("http://");
        dialog.setTitle("Create Image Node from URL");
        dialog.setHeaderText("Enter the URL below.");
        dialog.setContentText("URL: ");
        Optional<String> result = dialog.showAndWait();

        ImageNode newNode = new ImageNode(result.get());


        newNode.setTypeToImage();

        //masterNodeList.add(image); I think this is still necessary but not for the database

        nodeStage.getChildren().add(newNode.getNodePane());
    }

    public void createImageNodeFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open an image file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg","*.gif","*.bmp"));
        File openedFile = fileChooser.showOpenDialog(null);

        ImageNode newNode = new ImageNode(openedFile);

        newNode.setTypeToImage();

        //masterNodeList.add(newNode);

       nodeStage.getChildren().add(newNode.getNodePane());
    }
}
