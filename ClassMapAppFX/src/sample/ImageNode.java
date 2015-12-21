package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.lang.System;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.nio.file.Files;
import java.io.File;

/**
 * Created by James Davis on 12/19/2015.
 */
public class ImageNode extends MapNode {
    private GridPane nodePane;
    private byte[] imgToByte;
    private String formattedDate;
    private Image image;

    public ImageNode(String in)
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        formattedDate = sdf.format(date);
        this.type = type.image;

        image = new Image(in);

        this.drawNode();



    }

    public ImageNode(File in)
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        formattedDate = sdf.format(date);
        this.type = type.image;


        this.image = new Image(in.toURI().toString());
        this.drawNode();

    }

    public ImageNode(int id, int pid, Timestamp created)
    {
        this.type = type.image;
        uniqueId = id;
        parent = pid;
        timeCreated = created;

    }

    public void drawNode() {
        ImageView viewer = new ImageView(image);
        viewer.setPreserveRatio(Boolean.TRUE);
        viewer.setFitHeight(80.0f);
        double height = viewer.getBoundsInParent().getHeight();
        double width = viewer.getBoundsInParent().getWidth() * 2/3;

        Ellipse newNode = new Ellipse(0.0f, 0.0f, width, height);
        newNode.setFill(Paint.valueOf("white"));
        newNode.setStroke(Paint.valueOf("black"));

        Image arrow;
        ImageView arrowView;
        if(this.getUserVote() == Boolean.TRUE)
        {
            File path = new File("C:\\Users\\acous\\Documents\\GitHub\\ClassMapApp\\ClassMapAppFX\\Images\\Voted.png");
            arrow = new Image(path.toURI().toString());

        }
        else
        {
            File path = new File("C:\\Users\\acous\\Documents\\GitHub\\ClassMapApp\\ClassMapAppFX\\Images\\NoVote.png");
            arrow = new Image(path.toURI().toString());
        }

        arrowView = new ImageView(arrow);
        arrowView.setPreserveRatio(Boolean.TRUE);
        arrowView.setFitHeight(20.0f);
        Text numberOfVotes = new Text(" "+votes);
        numberOfVotes.setStyle("-fx-font: 22 arial");
        HBox arr = new HBox();
        arr.getChildren().addAll(arrowView,numberOfVotes);

        StackPane stack = new StackPane();
        stack.getChildren().addAll(newNode, viewer);

        nodePane = new GridPane();
        nodePane.add(arr,0,0);
        nodePane.add(stack,0,1);

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
        this.type = classification.image;
    }

    public GridPane getNodePane()
    {
        return this.nodePane;
    }

    public String getFormattedDate() { return this.formattedDate; }

    public byte[] imageToByteArray()
    {
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, "jpg", s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s.toByteArray();

    }

    public void setImage(Image node) { this.image = node; }

}
