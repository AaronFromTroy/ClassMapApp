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
import java.util.Optional;

public class Controller {

    public Slider zoomSlider;
    public TitledPane mainStage;
    public GridPane root;
    public Pane nodeStage;

    public void createTextNode(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog("Enter the text for the node");
        dialog.setTitle("Create Text Node");
        dialog.setHeaderText("Enter the text below.");
        dialog.setContentText("Text: ");
        Optional<String> result = dialog.showAndWait();
        Text text = new Text(result.get());
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setWrappingWidth(150.0f);
        double height = (text.getLayoutBounds().getHeight())*2/3;
        double width = (text.getLayoutBounds().getWidth())*2/3;
        double minHeight = 60.0f;
        double minWidth = 60.0f;

        if(width < minHeight)
        {
            width = minHeight;
        }
        if(height < minWidth)
        {
            height = minWidth;
        }

        Ellipse newNode = new Ellipse(0.0f, 0.0f, width, height);
        newNode.setFill(Paint.valueOf("white"));
        newNode.setStroke(Paint.valueOf("black"));
        StackPane nodePane = new StackPane();
        nodePane.getChildren().addAll(newNode, text);
        nodePane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                nodePane.setTranslateX(event.getSceneX() - nodePane.getHeight()/2);
                nodePane.setTranslateY(event.getSceneY() - nodePane.getWidth()/2);
            }
        });

        Tooltip tooltip;

        DictParser dict = new DictParser();
        dict.searchForWord(text.getText());
        if(dict.getCount() > 0)
        {
            tooltip = new Tooltip(dict.getExactDefinition());
        }
        else
        {
            tooltip = new Tooltip("Not a definable word");
        }

        Tooltip.install(nodePane,tooltip);

        nodeStage.getChildren().add(nodePane);

    }


}
