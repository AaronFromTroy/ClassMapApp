package sample;

import javafx.application.Application;
import static javafx.application.Application.launch;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by acous on 12/30/2015.
 */
public class VideoNode extends MapNode{
    private GridPane nodePane;
    private byte[] imgToByte;
    private Image image;
    private String contents;
    private String content_Url;

    public VideoNode(String in)
    {
        this.contents = in;
        this.contents = getContent();
        this.type = type.link;
        this.setUserVote(Boolean.TRUE);
        this.incrementVoteCounter();
        this.createdBy = DataConnection.loggedUser.getUser();
        this.nodePerm = DataConnection.loggedUser.getAccount();
        content_Url = "<iframe width=\"560\" height=\"315\" src=\"http://www.youtube.com/embed/" + contents
                + "\" frameborder=\"0\" allowfullscreen></iframe>";

        this.drawNode();

    }

    public VideoNode(int id, int pid, String in, Timestamp date_created, String user, String accountType)
    {
        this.uniqueId = id;
        this.parent = pid;
        this.contents = in;
        this.timeCreated = date_created;
        this.type = type.link;
        this.createdBy = user;
        this.nodePerm = accountType;

        content_Url = "<iframe width=\"560\" height=\"315\" src=\"http://www.youtube.com/embed/" + contents
                + "\" frameborder=\"0\" allowfullscreen></iframe>";

        this.drawNode();
    }


    public void drawNode() {
        File icon = new File("./Images/youtube-icon.png");
        image = new Image(icon.toURI().toString());
        ImageView viewer = new ImageView(image);
        viewer.setPreserveRatio(Boolean.TRUE);
        viewer.setFitWidth(80.0f);
        double height = viewer.getBoundsInParent().getHeight();
        double width = viewer.getBoundsInParent().getWidth() * 2/3;

        Ellipse newNode = new Ellipse(0.0f, 0.0f, width, height);
        if(this.nodePerm.equals("student")) {
            newNode.setFill(Paint.valueOf("white"));
            newNode.setStroke(Paint.valueOf("black"));
        }
        else {
            newNode.setFill(Paint.valueOf("black"));
            newNode.setStroke(Paint.valueOf("black"));
        }

        Image arrow;
        ImageView arrowView;
        if(this.getUserVote() == Boolean.TRUE)
        {
            File path = new File("./Images/arrow-up-icon_voted.png");
            arrow = new Image(path.toURI().toString());

        }
        else
        {
            File path = new File("./Images/arrow-up-icon.png");
            arrow = new Image(path.toURI().toString());
        }

        arrowView = new ImageView(arrow);
        arrowView.setPreserveRatio(Boolean.TRUE);
        arrowView.setFitHeight(20.0f);
        Text numberOfVotes = new Text(" "+votes);
        numberOfVotes.setStyle("-fx-font: 20 arial");
        HBox arr = new HBox();
        arr.getChildren().addAll(arrowView,numberOfVotes);


        arr.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(getUserVote() == Boolean.TRUE)
                {
                    File path = new File("./Images/arrow-up-icon.png");
                    Image newArrow = new Image(path.toURI().toString());
                    ImageView newArrowView = new ImageView(newArrow);
                    newArrowView.setPreserveRatio(Boolean.TRUE);
                    newArrowView.setFitHeight(20.0f);
                    decrementVoteCounter();
                    Text numberOfVotes = new Text(""+(votes));
                    numberOfVotes.setStyle("-fx-font: 20 arial");

                    arr.getChildren().remove(0, 2);
                    arr.getChildren().addAll(newArrowView, numberOfVotes);
                    setUserVote(false);
                    try {
                        sendSelf();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    //setVisible();

                }
                else
                {
                    File path = new File("./Images/arrow-up-icon_voted.png");
                    Image newArrow = new Image(path.toURI().toString());
                    ImageView newArrowView = new ImageView(newArrow);
                    newArrowView.setPreserveRatio(Boolean.TRUE);
                    newArrowView.setFitHeight(20.0f);
                    incrementVoteCounter();
                    Text numberOfVotes = new Text(""+(votes));
                    numberOfVotes.setStyle("-fx-font: 20 arial");
                    arr.getChildren().remove(0, 2);
                    arr.getChildren().addAll(newArrowView, numberOfVotes);
                    setUserVote(true);
                    try {
                        sendSelf();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    //setVisible();
                }
            }
        });

        StackPane stack = new StackPane();
        stack.getChildren().addAll(newNode, viewer);
        stack.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                showStage();
            }
        });
        nodePane = new GridPane();
        nodePane.add(arr,0,0);
        nodePane.add(stack,0,1);

        nodePane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent m) {

                nodePane.setLayoutX(m.getSceneX() - nodePane.getWidth()/2);
                nodePane.setLayoutY(m.getSceneY()-nodePane.getHeight());
            }
        });
    }

    public String getContents() {
        return this.contents;
    }

    public void sendSelf() throws SQLException { DataConnection.addUpvote(this); }

    public void makeVisible() {
        this.nodePane.setVisible(true);
    }

    public void setVisible() {
        if (getUserVote() == true) {
            this.nodePane.setVisible(true);
        }
        else
            this.nodePane.setVisible(false);
    }

    public void showStage(){
        Image logo = new Image("sample/OrangeIcon.png");
        Stage newStage = new Stage();
        newStage.setTitle("Video Viewer");
        newStage.getIcons().add(logo);
        newStage.setResizable(true);

        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.loadContent(content_Url);

        StackPane root = new StackPane();
        root.getChildren().add(webView);
        Scene scene = new Scene(root, 550, 350);
        newStage.setScene(scene);
        newStage.show();


    }

    public GridPane getNodePane()
    {
        return nodePane;
    }

    private String getContent() {
        String temp;
        if(contents.contains("https://www.youtube.com/watch?v=")) {
            temp = contents.replace("https://www.youtube.com/watch?v=", "");
        }
        else if(contents.contains("http://www.youtube.com/watch?v=")) {
            temp = contents.replace("http://www.youtube.com/watch?v=", "");
        }
        else{
            temp = null;
        }
        return temp;
    }
}
