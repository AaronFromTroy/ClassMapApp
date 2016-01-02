package sample;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.*;
import java.security.Timestamp;
import java.util.*;
import java.util.List;
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
    public Pane newNodeStage2;
    int[] array = {0, 5, 1, 6, 2, 7, 3, 8, 4, 9};
    java.util.List<MapNode> masterNode = new ArrayList<>();
    java.util.List<java.util.List<MapNode>> nodeList = new ArrayList<>();
    public ArrayList<MapNode> masterNodeList = new ArrayList<MapNode>();
    public MapNode daroot;
    public Accordion topicMenuAccordion;
    private boolean firstTimePublic = true;
    int prevPaneCordX, prevPaneCordY, prevMouseCordX, prevMouseCordY,diffX, diffY;

    int randomNumber;
    double factor;

    File path = new File("./Images/Drag-icon.png");
    javafx.scene.image.Image dragPicture = new javafx.scene.image.Image(path.toURI().toString());
    ImageView newDragView = new ImageView(dragPicture);


    public Pane trythisout;


    private class ZoomHandler implements EventHandler<ScrollEvent> {

        private Node nodeToZoom;

        private ZoomHandler(Node nodeToZoom) {
            this.nodeToZoom = nodeToZoom;
        }

        @Override
        public void handle(ScrollEvent scrollEvent) {
            //if (scrollEvent.isControlDown()) {
                    if(factor<=0.45)
                        newDragView.setVisible(false);
                    else
                        newDragView.setVisible(true);
                    final double scale = calculateScale(scrollEvent);
                    nodeToZoom.setScaleX(scale);
                    nodeToZoom.setScaleY(scale);
                    scrollEvent.consume();
            //}
        }

        private double calculateScale(ScrollEvent scrollEvent) {
            double scale = nodeToZoom.getScaleX() + scrollEvent.getDeltaY() / 400;
            factor = scale;
            if(scale<=0.1 ) {
                scale = 0.1;
            }
            if(scale>1)
                scale = 1;
            return scale;
        }
    }

    @FXML
    protected void initialize() {

        nodeStage.addEventFilter(ScrollEvent.ANY, new ZoomHandler(newNodeStage));
        nodeStage.addEventFilter(ScrollEvent.ANY, new ZoomHandler(newNodeStage2));
    }


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
            nodeStage.getChildren().add(newDragView);
        }

    }

    public void showHome(ActionEvent actionEvent){
        if(!firstTimePublic) {
            recursiveShow(daroot);
        }
    }

    EventHandler<MouseEvent> doNothing =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                }
            };

    private void recursiveDisplay(MapNode rootNode) {

        int children = rootNode.children.size();

        if (rootNode.uniqueId == 1 )
        {
            rootNode.setLayer(0);
            newNodeStage.getChildren().add(((TextNode)(rootNode)).getNodePane());
            ((TextNode)(rootNode)).getNodePane().setTranslateX(410);
            ((TextNode)(rootNode)).getNodePane().setTranslateY(225);
            nodeList.add(masterNode);
            ((TextNode) rootNode).getNodePane().setOnMousePressed(doNothing);
            ((TextNode) rootNode).getNodePane().setOnMouseDragged(doNothing);
            ((TextNode) rootNode).getNodePane().setOnMouseReleased(doNothing);

        }

        else if (rootNode.getLayer() == 1) {

            if (rootNode.getParentNode().getNoOfChildren() == ClassMap.noOfCircle*10){
                expand();

            }

            if (ClassMap.circleX.size() % 2 == 0) {
                randomNumber = 0;

            } else randomNumber = ClassMap.circleX.size() / 2;

            double newTranslateX = ClassMap.circleX.get(randomNumber);
            double newTranslateY = ClassMap.circleY.get(randomNumber);
            ClassMap.circleX.remove(randomNumber);
            ClassMap.circleY.remove(randomNumber);

            if(rootNode.type.toString().equals("string")){
                newNodeStage.getChildren().add(((TextNode)(rootNode)).getNodePane());
                ((TextNode)(rootNode)).getNodePane().setTranslateX(newTranslateX+410);
                ((TextNode)(rootNode)).getNodePane().setTranslateY(newTranslateY+225);

                if(rootNode.getParentNode().getType().toString().equals("string")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    TextNode textNode =(TextNode) rootNode.getParentNode();

                    line.startXProperty().bind(textNode.getNodePane().layoutXProperty().add((textNode.getNodePane().translateXProperty())));
                    line.startYProperty().bind(textNode.getNodePane().layoutYProperty().add(textNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((TextNode)(rootNode)).getNodePane().layoutXProperty().add(((TextNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((TextNode)(rootNode)).getNodePane().layoutXProperty().add(((TextNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    newNodeStage2.getChildren().addAll(line);
                }
                ((TextNode) rootNode).getNodePane().setOnMousePressed(doNothing);
                ((TextNode) rootNode).getNodePane().setOnMouseDragged(doNothing);
                ((TextNode) rootNode).getNodePane().setOnMouseReleased(doNothing);

            }

            //System.out.println(rootNode.uniqueId);


            if(rootNode.type.toString().equals("image")){
                newNodeStage.getChildren().add(((ImageNode)(rootNode)).getNodePane());
                ((ImageNode)(rootNode)).getNodePane().setTranslateX(newTranslateX+410);
                ((ImageNode)(rootNode)).getNodePane().setTranslateY(newTranslateY+225);
                ((ImageNode) rootNode).getNodePane().setOnMousePressed(doNothing);
                ((ImageNode) rootNode).getNodePane().setOnMouseDragged(doNothing);
                ((ImageNode) rootNode).getNodePane().setOnMouseReleased(doNothing);

            }

            if(rootNode.type.toString().equals("link")) {
                newNodeStage.getChildren().add(((VideoNode) (rootNode)).getNodePane());
                ((VideoNode)(rootNode)).getNodePane().setTranslateX(newTranslateX+410);
                ((VideoNode)(rootNode)).getNodePane().setTranslateY(newTranslateY+225);
                //System.out.println(((VideoNode)(rootNode)).getContents());
            }

            rootNode.setQuadrant(array[masterNode.size()%10] + 1);
            rootNode.setCircleNo(ClassMap.noOfCircle);
            rootNode.setExpansion(2*rootNode.getParentNode().getExpansionconst());
            rootNode.setChildLimit(2*rootNode.getParentNode().getExpansionconst());
            if (masterNode.size() >= 10){
                rootNode.setOffset(1);
            }
            masterNode.add(rootNode);
            rootNode.getParentNode().setNoOfChildren(rootNode.getParentNode().getNoOfChildren()+1);

        }

        else {
            if (rootNode.getParentNode().getNoOfChildren()== rootNode.getParentNode().getChildLimit()){
                expandChildren(rootNode.getParentNode());
            }
            List<Double> X = newCalculateX(rootNode.getParentNode().getCircleNo()*rootNode.getParentNode().getExpansion());
            List<Double> Y = newCalculateY(rootNode.getParentNode().getCircleNo()*rootNode.getParentNode().getExpansion());

            double newTranslateX = X.get(((rootNode.getQuadrant()-1)*rootNode.getParentNode().getCircleNo()*rootNode.getParentNode().getExpansion())+rootNode.getParentNode().getNoOfChildren()+(rootNode.getParentNode().getOffset()*rootNode.getParentNode().getExpansion()));
            double newTranslateY = Y.get(((rootNode.getQuadrant()-1)*rootNode.getParentNode().getCircleNo()*rootNode.getParentNode().getExpansion())+rootNode.getParentNode().getNoOfChildren()+(rootNode.getParentNode().getOffset()*rootNode.getParentNode().getExpansion()));

            if(rootNode.type.toString().equals("string")){
                newNodeStage.getChildren().add(((TextNode)(rootNode)).getNodePane());
                ((TextNode)(rootNode)).getNodePane().setTranslateX(newTranslateX+410);
                ((TextNode)(rootNode)).getNodePane().setTranslateY(newTranslateY+225);

                if(rootNode.getParentNode().getType().toString().equals("string")){
                    Line line = new Line();
                    line.setStroke(javafx.scene.paint.Color.BLACK);
                    TextNode textNode =(TextNode) rootNode.getParentNode();

                    line.startXProperty().bind(textNode.getNodePane().layoutXProperty().add(textNode.getNodePane().translateXProperty()));
                    line.startYProperty().bind(textNode.getNodePane().layoutYProperty().add(textNode.getNodePane().translateYProperty()));
                    line.endXProperty().bind(((TextNode)(rootNode)).getNodePane().layoutXProperty().add(((TextNode)(rootNode)).getNodePane().translateXProperty()));
                    line.endYProperty().bind(((TextNode)(rootNode)).getNodePane().layoutXProperty().add(((TextNode)(rootNode)).getNodePane().translateYProperty()));

                    line.setStrokeWidth(4);
                    newNodeStage2.getChildren().addAll(line);
                }
                ((TextNode) rootNode).getNodePane().setOnMousePressed(doNothing);
                ((TextNode) rootNode).getNodePane().setOnMouseDragged(doNothing);
                ((TextNode) rootNode).getNodePane().setOnMouseReleased(doNothing);
            }
            // System.out.println(rootNode.uniqueId);


            if(rootNode.type.toString().equals("image")){
                newNodeStage.getChildren().add(((ImageNode)(rootNode)).getNodePane());
                ((ImageNode)(rootNode)).getNodePane().setTranslateX(newTranslateX+410);
                ((ImageNode)(rootNode)).getNodePane().setTranslateY(newTranslateY+225);
            }

            if(rootNode.type.toString().equals("link")) {
                newNodeStage.getChildren().add(((VideoNode) (rootNode)).getNodePane());
                ((VideoNode)(rootNode)).getNodePane().setTranslateX(newTranslateX+410);
                ((VideoNode)(rootNode)).getNodePane().setTranslateY(newTranslateY+225);
                //System.out.println(((VideoNode)(rootNode)).getContents());
            }
            if (nodeList.size() == rootNode.getLayer()-1) {
                List<MapNode> node = new ArrayList<>();
                node.add(rootNode);
                nodeList.add(node);
            } else {
                nodeList.get(rootNode.getLayer()-1).add(rootNode);
            }
            rootNode.setQuadrant(rootNode.getParentNode().getQuadrant());
            rootNode.setCircleNo(rootNode.getParentNode().getCircleNo()*rootNode.getParentNode().getExpansion());
            rootNode.setExpansion(2*rootNode.getParentNode().getExpansionconst());
            rootNode.setChildLimit(2*rootNode.getParentNode().getExpansionconst());
            rootNode.setChildno((rootNode.getParentNode().getNoOfChildren()+rootNode.getParentNode().getChildno()) * 2);
            if(rootNode.getParentNode().getOffset()!=0)
            {
                rootNode.setOffset((rootNode.getParentNode().getNoOfChildren()+rootNode.getParentNode().getOffset()) * 2);
            }
            rootNode.getParentNode().setNoOfChildren(rootNode.getParentNode().getNoOfChildren()+1);
        }
        // System.out.println("Layer "+ rootNode.getLayer());

        for (int i = 0; i < children; i++) {

            if (!rootNode.children.isEmpty()){
                rootNode.children.get(i).setLayer(rootNode.getLayer()+1);
                rootNode.children.get(i).setQuadrant(rootNode.getQuadrant());
                recursiveDisplay(rootNode.children.get(i));
            }
        }
    }

    public List newCalculateX(int a) {
        List<Double> newcircleX = new ArrayList<>();
        double add = 6.28319 / (a * 10);
        for (double i = 0; i < 6.2831; i = i + add) {
            double x = Math.cos(i) * ClassMap.radius * a;
            newcircleX.add(x);
        }
        return newcircleX;
    }

    public List newCalculateY(int a) {
        List<Double> newcircleY = new ArrayList<>();
        double add = 6.28319 / (a * 10);
        for (double i = 0; i < 6.2831; i = i + add) {
            double y = Math.sin(i) * ClassMap.radius * a;
            newcircleY.add(y);

        }
        return newcircleY;
    }

    public void expand(){
        ClassMap.noOfCircle++;

        ClassMap.calculate();
        int j = 0;
        for (int i = 0; i < nodeList.get(0).size(); i++) {

            if (i % 10 == 0 && i != 0) {
                j++;
            }
            if (nodeList.get(0).get(i).getType().equals("string")){
                ((TextNode)nodeList.get(0).get(i)).getNodePane().setTranslateX(ClassMap.circleX.get(((nodeList.get(0).get(i).getQuadrant() - 1) * ClassMap.noOfCircle) + j) + 410);
                ((TextNode)nodeList.get(0).get(i)).getNodePane().setTranslateY(ClassMap.circleY.get(((nodeList.get(0).get(i).getQuadrant() - 1) * ClassMap.noOfCircle) + j) + 225);
                nodeList.get(0).get(i).setCircleNo(ClassMap.noOfCircle);
            }
            else if (nodeList.get(0).get(i).getType().equals("image")){
                ((ImageNode)nodeList.get(0).get(i)).getNodePane().setTranslateX(ClassMap.circleX.get(((nodeList.get(0).get(i).getQuadrant() - 1) * ClassMap.noOfCircle) + j) + 410);
                ((ImageNode)nodeList.get(0).get(i)).getNodePane().setTranslateY(ClassMap.circleY.get(((nodeList.get(0).get(i).getQuadrant() - 1) * ClassMap.noOfCircle) + j) + 225);
                nodeList.get(0).get(i).setCircleNo(ClassMap.noOfCircle);

            }
            else if (nodeList.get(0).get(i).getType().equals("link")){
                ((VideoNode)nodeList.get(0).get(i)).getNodePane().setTranslateX(ClassMap.circleX.get(((nodeList.get(0).get(i).getQuadrant() - 1) * ClassMap.noOfCircle) + j) + 410);
                ((VideoNode)nodeList.get(0).get(i)).getNodePane().setTranslateY(ClassMap.circleY.get(((nodeList.get(0).get(i).getQuadrant() - 1) * ClassMap.noOfCircle) + j) + 225);
                nodeList.get(0).get(i).setCircleNo(ClassMap.noOfCircle);
            }
        }

        for (int i = 0; i < ClassMap.circleX.size(); i++) {
            for (int k = 0; k < ClassMap.noOfCircle - 1; k++) {
                ClassMap.circleX.remove(i);
                ClassMap.circleY.remove(i);
            }

        }
        expandChildren();
    }
    public  void expandChildren(){

        int add;
        int off;
        for (int j =1 ; j< nodeList.size();j++) {

            for (int i = 0; i < nodeList.get(j).size(); i++) {

                List<Double> X = newCalculateX(nodeList.get(j).get(i).getParentNode().getCircleNo()*nodeList.get(j).get(i).getParentNode().getExpansion());
                List<Double> Y = newCalculateY(nodeList.get(j).get(i).getParentNode().getCircleNo()*nodeList.get(j).get(i).getParentNode().getExpansion());

                if ( nodeList.get(j).get(i).getChildno() > 0) {
                    add = nodeList.get(j).get(i).getChildno() / 2;
                } else add = 0;

                if ( nodeList.get(j).get(i).getOffset() > 0) {
                    off = nodeList.get(j).get(i).getOffset() / 2;
                } else off = 0;

                double newTranslateX = X.get(((nodeList.get(j).get(i).getQuadrant() - 1) * nodeList.get(j).get(i).getParentNode().getCircleNo() * nodeList.get(j).get(i).getParentNode().getExpansion()) + add+off);
                double newTranslateY = Y.get(((nodeList.get(j).get(i).getQuadrant() - 1) * nodeList.get(j).get(i).getParentNode().getCircleNo() * nodeList.get(j).get(i).getParentNode().getExpansion()) + add+off);

                if (nodeList.get(j).get(i).type.toString().equals("string")) {
                    ((TextNode) (nodeList.get(j).get(i))).getNodePane().setTranslateX(newTranslateX + 410);
                    ((TextNode) (nodeList.get(j).get(i))).getNodePane().setTranslateY(newTranslateY + 225);
                    nodeList.get(j).get(i).setCircleNo(nodeList.get(j).get(i).getParentNode().getCircleNo()*nodeList.get(j).get(i).getParentNode().getExpansion());
                }
                //System.out.println(nodeList.get(j).get(i).uniqueId);


                if (nodeList.get(j).get(i).type.toString().equals("image")) {
                    ((ImageNode) (nodeList.get(j).get(i))).getNodePane().setTranslateX(newTranslateX + 410);
                    ((ImageNode) (nodeList.get(j).get(i))).getNodePane().setTranslateY(newTranslateY + 225);
                    nodeList.get(j).get(i).setCircleNo(nodeList.get(j).get(i).getParentNode().getCircleNo()*nodeList.get(j).get(i).getParentNode().getExpansion());
                }

                if (nodeList.get(j).get(i).type.toString().equals("link")) {
                    ((VideoNode) (nodeList.get(j).get(i))).getNodePane().setTranslateX(newTranslateX + 410);
                    ((VideoNode) (nodeList.get(j).get(i))).getNodePane().setTranslateY(newTranslateY + 225);
                    nodeList.get(j).get(i).setCircleNo(nodeList.get(j).get(i).getParentNode().getCircleNo()*nodeList.get(j).get(i).getParentNode().getExpansion());
                    // System.out.println(((VideoNode) (nodeList.get(j).get(i))).getContents());
                }
            }
        }
    }
    public void expandChildren(MapNode node){
        int add;
        int off;
        if (node.getLayer()==1){
            node.getParentNode().setExpansionconst(node.getParentNode().getExpansionconst()*2);
            for (int i =0;i<nodeList.get(node.getLayer()-1).size();i++){
                nodeList.get(node.getLayer()-1).get(i).setExpansion(2*node.getParentNode().getExpansionconst());
                nodeList.get(node.getLayer()-1).get(i).setChildLimit(2*node.getParentNode().getExpansionconst());

            }
        }
        else{
            for (int i =0;i<nodeList.get(node.getLayer()-2).size();i++ ){
                nodeList.get(node.getLayer()-2).get(i).setExpansionconst(nodeList.get(node.getLayer()-2).get(i).getExpansionconst()*2);
            }

            for (int i =0;i<nodeList.get(node.getLayer()-1).size();i++){
                nodeList.get(node.getLayer()-1).get(i).setExpansion(2*node.getParentNode().getExpansionconst());
                nodeList.get(node.getLayer()-1).get(i).setChildLimit(2*node.getParentNode().getExpansionconst());

            }

        }

        for (int j =node.getLayer() ; j< nodeList.size();j++) {

            for (int i = 0; i < nodeList.get(j).size(); i++) {

                List<Double> X = newCalculateX(nodeList.get(j).get(i).getParentNode().getCircleNo()*nodeList.get(j).get(i).getParentNode().getExpansion());
                List<Double> Y = newCalculateY(nodeList.get(j).get(i).getParentNode().getCircleNo()*nodeList.get(j).get(i).getParentNode().getExpansion());

                if ( nodeList.get(j).get(i).getChildno() > 0) {
                    add = nodeList.get(j).get(i).getChildno() / 2;
                } else add = 0;

                if ( nodeList.get(j).get(i).getOffset() > 0) {
                    off = nodeList.get(j).get(i).getOffset() / 2;
                } else off = 0;

                double newTranslateX = X.get(((nodeList.get(j).get(i).getQuadrant() - 1) * nodeList.get(j).get(i).getParentNode().getCircleNo() * nodeList.get(j).get(i).getParentNode().getExpansion()) + add+off);
                double newTranslateY = Y.get(((nodeList.get(j).get(i).getQuadrant() - 1) * nodeList.get(j).get(i).getParentNode().getCircleNo() * nodeList.get(j).get(i).getParentNode().getExpansion()) + add+off);

                if (nodeList.get(j).get(i).type.toString().equals("string")) {
                    ((TextNode) (nodeList.get(j).get(i))).getNodePane().setTranslateX(newTranslateX + 410);
                    ((TextNode) (nodeList.get(j).get(i))).getNodePane().setTranslateY(newTranslateY + 225);
                    nodeList.get(j).get(i).setCircleNo(nodeList.get(j).get(i).getParentNode().getCircleNo()*nodeList.get(j).get(i).getParentNode().getExpansion());
                }
                //System.out.println(nodeList.get(j).get(i).uniqueId);


                if (nodeList.get(j).get(i).type.toString().equals("image")) {
                    ((ImageNode) (nodeList.get(j).get(i))).getNodePane().setTranslateX(newTranslateX + 410);
                    ((ImageNode) (nodeList.get(j).get(i))).getNodePane().setTranslateY(newTranslateY + 225);
                    nodeList.get(j).get(i).setCircleNo(nodeList.get(j).get(i).getParentNode().getCircleNo()*nodeList.get(j).get(i).getParentNode().getExpansion());
                }

                if (nodeList.get(j).get(i).type.toString().equals("link")) {
                    ((VideoNode) (nodeList.get(j).get(i))).getNodePane().setTranslateX(newTranslateX + 410);
                    ((VideoNode) (nodeList.get(j).get(i))).getNodePane().setTranslateY(newTranslateY + 225);
                    nodeList.get(j).get(i).setCircleNo(nodeList.get(j).get(i).getParentNode().getCircleNo()*nodeList.get(j).get(i).getParentNode().getExpansion());
                    //System.out.println(((VideoNode) (nodeList.get(j).get(i))).getContents());
                }
            }
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
            if(factor<0.45) {

            }
            else {
                newNodeStage.setLayoutX(x);
                newNodeStage.setLayoutY(y);
                newNodeStage2.setLayoutX(x);
                newNodeStage2.setLayoutY(y);
            }
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

