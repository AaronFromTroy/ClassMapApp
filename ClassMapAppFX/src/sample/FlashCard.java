package sample;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Created by Aaron on 1/23/2016.
 */
public class FlashCard {

    private int uniqueId;
    private Image imgContent;
    private String content;
    private String description;
    private String type;

    public FlashCard(MapNode incomingNode) {
        if (incomingNode.getType().equals("string")) {
            content = ((TextNode)incomingNode).getContents();
            description = ((TextNode)incomingNode).getDescription();
            type = "Text";
        }
        else if (incomingNode.getType().equals("link")) {
            content = ((VideoNode)incomingNode).getContents();
            description = ((VideoNode)incomingNode).getDescription();
            type = "Video";
        }
        else if (incomingNode.getType().equals("image")) {
            imgContent = ((ImageNode)incomingNode).getImage();
            description = ((ImageNode)incomingNode).getDescription();
            type = "Image";
        }
        else if (incomingNode.getType().equals("topic")) {
            imgContent = ((ImageNode)incomingNode).getImage();
            description = ((ImageNode)incomingNode).getDescription();
            type = "Topic";
        }

    }

    private void drawFlashCard() {
        Stage cardWindow = new Stage();
        cardWindow.setTitle(type + " Card");
        cardWindow.getIcons().add(new Image("sample/OrangeIcon.png"));

        GridPane masterGrid = new GridPane();
        if (type.equals("Text") || type.equals("Topic")) {
            Label contentLabel = new Label(content);
            contentLabel.setStyle("-fx-text-file: #000000;");
            //masterGrid.add();
        }
        else if (type.equals("Image")) {
            ImageView setImage = new ImageView(imgContent);
        }
    }

}
