//package lobby;
//
//import game.model.game.model.team.Role;
//import game.model.game.model.team.Team;
//import javafx.animation.AnimationTimer;
//import javafx.geometry.Pos;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.canvas.Canvas;
//import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.control.ChoiceBox;
//import javafx.scene.effect.DropShadow;
//import javafx.scene.effect.GaussianBlur;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Circle;
//import javafx.scene.shape.Shape;
//import javafx.scene.text.Font;
//import javafx.scene.text.FontWeight;
//import javafx.scene.text.Text;
//
//public class MainMenuScene extends Scene {
//    public MainMenuScene(Parent root) {
//        super(root);
//    }
//
//    public Scene mainMenuScene;
//
//
//    private Font font = Font.font("", FontWeight.BOLD, 36);
//    private static Color textColor = Color.web("b5de0f");
//
//    private VBox menuBox;
//
//    private int currentItem = 0;
//    private AnimationTimer animator;
//
//    ChoiceBox<Team> teamChoice;
//    ChoiceBox<Role> roleChoice;
//
//    boolean connected = false;
//
//    Canvas bgCanvas = new Canvas();
//    GraphicsContext bgGC = bgCanvas.getGraphicsContext2D();
//    Canvas midCanvas = new Canvas();
//    GraphicsContext midGC = midCanvas.getGraphicsContext2D();
//
//
//    MenuItem[] items = new MenuItem[]{
//            new MenuItem("JOIN GAME"),
//            new MenuItem("NEW GAME"),
//            new MenuItem("TEST GAME"),
//            new MenuItem("HOW TO PLAY"),
//            new MenuItem("EXIT")};
//
//    private class TriCircle extends Parent {
//        private TriCircle() {
//            Shape shape1 = Shape.subtract(new Circle(8), new Circle(2));
//            shape1.setFill(textColor);
//
//            Shape shape2 = Shape.subtract(new Circle(8), new Circle(2));
//            shape2.setFill(textColor);
//            shape2.setTranslateX(5);
//
//            Shape shape3 = Shape.subtract(new Circle(8), new Circle(2));
//            shape3.setFill(textColor);
//            shape3.setTranslateX(2.5);
//            shape3.setTranslateY(-5);
//
//            getChildren().addAll(shape1, shape2, shape3);
//
//            setEffect(new GaussianBlur(2));
//        }
//    }
//
//    private class MenuItem extends HBox {
//        private TriCircle c1 = new TriCircle(), c2 = new TriCircle();
//        private Text text;
//        private Runnable script;
//
//        private MenuItem(String name) {
//            super(15);
//            setAlignment(Pos.CENTER);
//
//            text = new Text(name);
//            text.setFont(font);
//            text.setStyle("-fx-stroke: black; -fx-stroke-width: 2px");
//            text.setEffect(new DropShadow(5, 0, 5, Color.BLACK));
//
//            getChildren().addAll(c1, text, c2);
//            setActive(false);
//            setOnActivate(() -> System.out.println(name + " activated"));
//
//            this.setOnMouseClicked(l -> {
//                this.activate();
//            });
//
//            this.setOnMouseMoved(l -> {
//                int i = 0;
//                for(MenuItem m : items) {
//                    if(m == this)
//                        currentItem = i;
//                    else
//                        m.setActive(false);
//                    i++;
//                }
//                this.setActive(true);
//            });
//        }
//
//        private void updateSize() {
//            text.setFont(font);
//        }
//
//        public void setActive(boolean b) {
//            c1.setVisible(b);
//            c2.setVisible(b);
//            text.setFill(b ? Color.WHITE : textColor);
//        }
//
//        public void setOnActivate(Runnable r) {
//            script = r;
//        }
//
//        public void activate() {
//            if (script != null)
//                script.run();
//        }
//    }
//}
