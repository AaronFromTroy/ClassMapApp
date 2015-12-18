package sample;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.control.*;
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

import java.awt.*;
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

        newNode.setTime();

        masterNodeList.add(newNode);

        nodeStage.getChildren().add(newNode.getNodePane());

        printMasterList();

    }

    public void printMasterList()
    {
        int size = masterNodeList.size();

        for(int x = 0; x < size; x++)
        {
            System.out.println(masterNodeList.get(x).getSeconds());
        }
    }


}
