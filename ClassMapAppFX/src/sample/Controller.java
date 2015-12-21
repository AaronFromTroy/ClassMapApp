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
import java.io.*;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;




public class Controller {

    public Slider zoomSlider;
    public TitledPane mainStage;
    public GridPane root;
    public Pane nodeStage;
    public ArrayList<MapNode> masterNodeList = new ArrayList<MapNode>();
    public MapNode daroot;


    public void createTextNode(ActionEvent actionEvent){


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
        DataConnection.addImageNode(newNode);

        newNode.setTypeToImage();

        //masterNodeList.add(image); I think this is still necessary but not for the database

        nodeStage.getChildren().add(newNode.getNodePane());
    }

    public void createImageNodeFile(ActionEvent actionEvent) throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open an image file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg","*.gif","*.bmp"));
        File openedFile = fileChooser.showOpenDialog(null);

        ImageNode newNode = new ImageNode(openedFile);
        DataConnection.addImageNode(newNode);

        newNode.setTypeToImage();

        //masterNodeList.add(newNode);

       nodeStage.getChildren().add(newNode.getNodePane());
    }

    public void drawWorld(ActionEvent actionEvent) throws InterruptedException {
        daroot = DataConnection.populate();
        recursiveDisplay(daroot);
    }

    private void recursiveDisplay(MapNode rootNode) {

        int children = rootNode.children.size();

        if(rootNode.type.toString().equals("string"))
            nodeStage.getChildren().add(((TextNode)(rootNode)).getNodePane());
        System.out.println(rootNode.uniqueId);

        if(rootNode.type.toString().equals("image"))
            nodeStage.getChildren().add(((ImageNode)(rootNode)).getNodePane());

        for (int i = 0; i < children; i++) {
            if (!rootNode.children.isEmpty())
                recursiveDisplay(rootNode.children.get(i));
        }
    }
}
