package game.view;

import game.model.game.model.ClientGameModel;
import game.model.game.model.GameModel;
import game.model.game.model.team.TeamResourceData;
import images.Images;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import util.Const;

import static images.Images.CURSOR_IMAGE;

public class ResourcePanel extends FlowPane {
    private ClientGameModel model;
    private Text woodTxt;
    private Text stoneTxt;
    private Text metalTxt;
    private ImageView woodImg;
    private ImageView stoneImg;
    private ImageView metalImg;

    public ResourcePanel(ClientGameModel model) {
        super(16,4);
        this.model = model;
        this.setCursor(new ImageCursor(CURSOR_IMAGE, 0, 0));
        this.setBackground(model.getTeam().getPanelBG());
        woodTxt = new Text("0");
        woodTxt.setFont(new Font(20));
        woodTxt.setFill(Color.WHITE);
        stoneTxt = new Text("0");
        stoneTxt.setFont(new Font(20));
        stoneTxt.setFill(Color.WHITE);
        metalTxt = new Text("0");
        metalTxt.setFont(new Font(20));
        metalTxt.setFill(Color.WHITE);
        woodImg = new ImageView(Images.WOOD_ICON);
        woodImg.setFitWidth(32);
        woodImg.setFitHeight(32);
        stoneImg = new ImageView(Images.STONE_ICON);
        stoneImg.setFitWidth(32);
        stoneImg.setFitHeight(32);
        metalImg = new ImageView(Images.METAL_ICON);
        metalImg.setFitWidth(32);
        metalImg.setFitHeight(32);

        this.getChildren().addAll(new Text(""), woodImg, woodTxt, stoneImg, stoneTxt, metalImg, metalTxt);
    }

    public void draw() {
        this.woodTxt.setText(model.getResourceData().getResource(TeamResourceData.Resource.WOOD) + "");
        this.stoneTxt.setText(model.getResourceData().getResource(TeamResourceData.Resource.STONE) + "");
        this.metalTxt.setText(model.getResourceData().getResource(TeamResourceData.Resource.METAL) + "");
    }

    public void stop() {
        model = null;
    }
}
