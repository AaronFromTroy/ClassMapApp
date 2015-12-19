package sample;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.sql.Timestamp;

public class TextNode extends MapNode{

    private StackPane nodePane;
    private String contents;

    TextNode(String in)
    {
        Text text = new Text(in);
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
        nodePane = new StackPane();
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
    }

    TextNode(int id, int pid, String in, Timestamp date_created)
    {
        uniqueId = id;
        parent = pid;
        contents = in;
        timeCreated = date_created;
        Text text = new Text(in);
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
        nodePane = new StackPane();
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
    }

    public void setTypeToText()
    {
        this.type = classification.String;
    }

    public StackPane getNodePane()
    {
        return nodePane;
    }

    public String getContents() {
        return this.contents;
    }
}
