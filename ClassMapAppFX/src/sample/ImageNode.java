package sample;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by James Davis on 12/19/2015.
 */
public class ImageNode extends MapNode {
    private StackPane nodePane;
    private byte[] image;

    public ImageNode(String in)
    {
        Image image = new Image(in);

        ImageView viewer = new ImageView(image);
        viewer.setPreserveRatio(Boolean.TRUE);
        viewer.setFitHeight(80.0f);
        double height = viewer.getBoundsInParent().getHeight();
        double width = viewer.getBoundsInParent().getWidth() * 2/3;

        Ellipse newNode = new Ellipse(0.0f, 0.0f, width, height);
        newNode.setFill(Paint.valueOf("white"));
        newNode.setStroke(Paint.valueOf("black"));
        nodePane = new StackPane();
        nodePane.getChildren().addAll(newNode, viewer);

        nodePane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                nodePane.setTranslateX(event.getSceneX() - nodePane.getHeight()/2);
                nodePane.setTranslateY(event.getSceneY() - nodePane.getWidth()/2);
            }
        });



    }

    public ImageNode(File in)
    {
        Image image = new Image(in.toURI().toString());

        ImageView viewer = new ImageView(image);
        viewer.setPreserveRatio(Boolean.TRUE);
        viewer.setFitHeight(80.0f);
        double height = viewer.getBoundsInParent().getHeight();
        double width = viewer.getBoundsInParent().getWidth() * 2/3;

        Ellipse newNode = new Ellipse(0.0f, 0.0f, width, height);
        newNode.setFill(Paint.valueOf("white"));
        newNode.setStroke(Paint.valueOf("black"));
        nodePane = new StackPane();
        nodePane.getChildren().addAll(newNode, viewer);

        nodePane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                nodePane.setTranslateX(event.getSceneX() - nodePane.getHeight()/2);
                nodePane.setTranslateY(event.getSceneY() - nodePane.getWidth()/2);
            }
        });



    }

    public void setTypeToImage()
    {
        this.type = classification.Image;
    }

    public StackPane getNodePane()
    {
        return this.nodePane;
    }

}
