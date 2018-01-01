import com.sun.javafx.geom.Shape;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;

/*
temp proof of concenpt
 */
public interface Drawable {

    public void drawInit(ObservableList<Node> l);

    public void drawUpdate();
}
