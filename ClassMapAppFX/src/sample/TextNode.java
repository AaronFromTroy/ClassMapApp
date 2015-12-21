package sample;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.io.File;
import java.sql.Timestamp;
import java.util.Optional;

public class TextNode extends MapNode{

    private GridPane nodePane;
    private String contents;

    TextNode(String in)
    {
        contents = in;
        this.type = type.String;

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

        StackPane stack = new StackPane();
        stack.getChildren().addAll(newNode, text);

        Image arrow;
        ImageView arrowView;
        if(this.getUserVote() == Boolean.TRUE)
        {
            File path = new File("C:\\Users\\crims_000\\Documents\\GitHub\\ClassMapApp\\ClassMapAppFX\\Images\\Voted.png");
            arrow = new Image(path.toURI().toString());

        }
        else
        {
            File path = new File("C:\\Users\\crims_000\\Documents\\GitHub\\ClassMapApp\\ClassMapAppFX\\Images\\NoVote.png");
            arrow = new Image(path.toURI().toString());
        }

        arrowView = new ImageView(arrow);
        arrowView.setPreserveRatio(Boolean.TRUE);
        arrowView.setFitHeight(20.0f);

        nodePane = new GridPane();
        nodePane.add(arrowView,0,0);
        nodePane.add(stack,0,1);

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

        StackPane stack = new StackPane();
        stack.getChildren().addAll(newNode, text);

        Image arrow;
        ImageView arrowView;
        if(this.getUserVote() == Boolean.TRUE)
        {
            File path = new File("C:\\Users\\crims_000\\Documents\\GitHub\\ClassMapApp\\ClassMapAppFX\\Images\\Voted.png");
            arrow = new Image(path.toURI().toString());

        }
        else
        {
            File path = new File("C:\\Users\\crims_000\\Documents\\GitHub\\ClassMapApp\\ClassMapAppFX\\Images\\NoVote.png");
            arrow = new Image(path.toURI().toString());
        }

        arrowView = new ImageView(arrow);
        arrowView.setPreserveRatio(Boolean.TRUE);
        arrowView.setFitHeight(20.0f);

        nodePane = new GridPane();
        nodePane.add(arrowView,0,0);
        nodePane.add(arrowView,0,1);

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

    public GridPane getNodePane()
    {
        return nodePane;
    }

    public String getContents() {
        return this.contents;
    }
}
