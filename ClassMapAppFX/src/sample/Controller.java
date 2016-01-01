package sample;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseDragEvent;
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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
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

    public TitledPane mainStage;
    public GridPane root;
    public Pane nodeStage;
    public Pane newNodeStage;
    public ArrayList<MapNode> masterNodeList = new ArrayList<MapNode>();
    public MapNode daroot;
    public Accordion topicMenuAccordion;
    private boolean firstTimePublic = true;
    int prevPaneCordX, prevPaneCordY, prevMouseCordX, prevMouseCordY,diffX, diffY;

    public Pane trythisout;



    public void createTextNode(ActionEvent actionEvent){

        if(!firstTimePublic) {

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
        if(!firstTimePublic) {
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
    }

    public void createImageNodeFile(ActionEvent actionEvent) throws FileNotFoundException {
        if(!firstTimePublic) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open an image file");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp"));
            File openedFile = fileChooser.showOpenDialog(null);

            ImageNode newNode = new ImageNode(openedFile);
            DataConnection.addImageNode(newNode);

            newNode.setTypeToImage();

            //masterNodeList.add(newNode);

            nodeStage.getChildren().add(newNode.getNodePane());
        }
    }

    public void drawWorld(ActionEvent actionEvent) throws InterruptedException {
        if(firstTimePublic) {
            daroot = DataConnection.populate();
            recursiveDisplay(daroot);
            firstTimePublic = false;
            populateList();
        }
    }

    public void showHome(ActionEvent actionEvent){
        if(!firstTimePublic) {
            recursiveShow(daroot);
        }
    }

    private void recursiveDisplay(MapNode rootNode) {

        int children = rootNode.children.size();

        if(rootNode.type.toString().equals("string")) {
            if(rootNode.uniqueId == 1) {

            }
            newNodeStage.getChildren().add(((TextNode) (rootNode)).getNodePane());
        }
        //System.out.println(rootNode.uniqueId);


        if(rootNode.type.toString().equals("image"))
            newNodeStage.getChildren().add(((ImageNode)(rootNode)).getNodePane());

        if(rootNode.type.toString().equals("link")) {
            newNodeStage.getChildren().add(((VideoNode) (rootNode)).getNodePane());
            //System.out.println(((VideoNode)(rootNode)).getContents());
        }

        for (int i = 0; i < children; i++) {
            if (!rootNode.children.isEmpty())
                recursiveDisplay(rootNode.children.get(i));
        }
    }

    public void populateList() {

        if(DataConnection.getTopic == Boolean.FALSE)
        {
            DataConnection.setTopic();
            for(int x = 0; x < DataConnection.topicNameList.size(); x++)
            {
                WebView web = new WebView();
                WebEngine engine = web.getEngine();
                engine.loadContent(DataConnection.htmlList.get(x).toString());
                TitledPane temp = new TitledPane(DataConnection.topicNameList.get(x).toString(),web);
                topicMenuAccordion.getPanes().addAll(temp);
            }
        }
    }

    public void hideNodes(ActionEvent actionEvent) {
        if(!firstTimePublic) {
            recursiveHide(daroot);
        }
    }

    public void recursiveHide(MapNode rootNode) {
        int children = rootNode.children.size();

        if(rootNode.type.toString().equals("string"))
            ((TextNode)(rootNode)).setVisible();

        if(rootNode.type.toString().equals("image"))
            ((ImageNode)(rootNode)).setVisible();

        if(rootNode.type.toString().equals("link"))
            ((VideoNode)(rootNode)).setVisible();

        for (int i = 0; i < children; i++) {
            if (!rootNode.children.isEmpty())
                recursiveHide(rootNode.children.get(i));
        }
    }

    public void recursiveShow(MapNode rootNode) {
        int children = rootNode.children.size();

        if(rootNode.type.toString().equals("string"))
            ((TextNode)(rootNode)).makeVisible();

        if(rootNode.type.toString().equals("image"))
            ((ImageNode)(rootNode)).makeVisible();

        if(rootNode.type.toString().equals("link"))
            ((VideoNode)(rootNode)).makeVisible();

        for (int i = 0; i < children; i++) {
            if (!rootNode.children.isEmpty())
                recursiveShow(rootNode.children.get(i));
        }
    }

    public void nodeDragMousePressed(MouseEvent m)
    {
        prevPaneCordX= (int) newNodeStage.getLayoutX();
        prevPaneCordY= (int) newNodeStage.getLayoutY();
        prevMouseCordX= (int) m.getX();
        prevMouseCordY= (int) m.getY();
    }

    // set this method on Mouse Drag event for newNodeStage
    public void nodeDragMouseDragged(MouseEvent m)
    {
        diffX= (int) (m.getX()- prevMouseCordX);
        diffY= (int) (m.getY()-prevMouseCordY );
        int x = (int) (diffX+newNodeStage.getLayoutX()-root.getLayoutX());
        int y = (int) (diffY+newNodeStage.getLayoutY()-root.getLayoutY());
        if(m.getSceneX() > 0 && m.getSceneY() > 0) {
            newNodeStage.setLayoutX(x);
            newNodeStage.setLayoutY(y);
        }
    }

    public void createVideoNode(ActionEvent actionEvent) {
        if(!firstTimePublic) {

            TextInputDialog dialog = new TextInputDialog("Enter YouTube URL");
            dialog.setTitle("Create Video Node");
            dialog.setHeaderText("Enter the URL below.");
            dialog.setContentText("URL: ");
            Optional<String> result = dialog.showAndWait();

            VideoNode newNode = new VideoNode(result.get());
            DataConnection.addVideoNode(newNode);

            nodeStage.getChildren().add(newNode.getNodePane());
        }
    }
}

