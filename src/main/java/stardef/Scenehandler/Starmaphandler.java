package stardef.Scenehandler;

import com.sun.glass.ui.Screen;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import stardef.Baseclasses.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Starmaphandler {

    public static List<Vector2D> Getstarlocation(Image img) {
        PixelReader pixelReader = img.getPixelReader();
        int w = (int) img.getWidth();
        int h = (int) img.getHeight();
        List<Vector2D> coord = new ArrayList<Vector2D>();
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                Color color = pixelReader.getColor(x, y);
                if (!samergbcolor(color, Color.BLACK))//i only need the rgb code
                {
                    coord.add(new Vector2D(x, y));
                }
            }
        }
        return coord;
    }

    static boolean samergbcolor(Color a, Color b) {
        if (a.getRed() == b.getRed() || a.getGreen() == b.getGreen() || a.getBlue() == b.getBlue()) {
            return true;
        }
        return false;
    }

    static GridPane fillstarmap(List<Vector2D> backgroundlayer, int width, int height) {
        GridPane gp = new GridPane();
        Image empty = new Image("file:src/main/resources/Sprites/Tiles/Space_16.png");
        Image star = new Image("file:src/main/resources/Sprites/Tiles/Star_16.png");
        int nextstar = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                ImageView iv = new ImageView();
                if (nextstar == backgroundlayer.size())
                {
                    iv.setImage(empty);
                    gp.add(iv, y, x);
                }
                else if (!Vector2D.Equal(backgroundlayer.get(nextstar), new Vector2D(y, x)))
                {
                    iv.setImage(empty);
                    gp.add(iv, y, x);
                }
                else
                    {
                        iv.setImage(star);
                        gp.add(iv, y, x);
                        nextstar=nextstar+1;
                    }
            }
        }
        return gp;
    }

    public static void Starmapanimation(AnchorPane root,GridPane gp,List<Vector2D> booleans, String url,Vector2D Currentpos,String linecolor)
    {
        ImageView iv0=new ImageView(new Image("file:src/main/resources/Sprites/Tiles/"+url));
        Line line=new Line();
        line.setStyle("-fx-stroke: "+linecolor+";");
        double constantX=(double) Screen.getMainScreen().getWidth()/4;
        for (Node node : gp.getChildren()) {

            node.setOnMouseEntered((MouseEvent t) -> {
                Vector2D v2d=new Vector2D(GridPane.getColumnIndex(node),GridPane.getRowIndex(node));

                if(booleans.stream().anyMatch(x->x.x==v2d.x&&x.y==v2d.y))
                {
                    gp.add(iv0, v2d.x, v2d.y);
                    root.getChildren().add(line);
                    line.setStartX(gp.getChildren().get(64*Currentpos.x+Currentpos.y).getLayoutX()+gp.getMinWidth()/8+constantX);
                    line.setStartY(gp.getChildren().get(64*Currentpos.x+Currentpos.y).getLayoutY()+gp.getMinHeight()/8);
                    line.setEndX(node.getLayoutX()+gp.getMinWidth()/8+constantX);
                    line.setEndY(node.getLayoutY()+gp.getMinHeight()/8);

                    node.setOnMouseClicked((MouseEvent clicked) ->{
                    //Logger.info("Player clicked on: "+GridPane.getColumnIndex(node)+" "+GridPane.getRowIndex(node));
                    if(Vector2D.Equal(Mainhandler.starmapposition,new Vector2D(GridPane.getColumnIndex(node),GridPane.getRowIndex(node))))
                    {
                        Mainhandler.ssh.scene.setRoot(Mainhandler.ssh.GameScreen);
                    }
                    else
                        {
                            Mainhandler.starmapposition=new Vector2D(GridPane.getColumnIndex(node),GridPane.getRowIndex(node));
                            Mainhandler.ssh=new Shipscenehandler(Mainhandler.ssh.scene);
                            Mainhandler.ssh.scene.setRoot(Mainhandler.ssh.GameScreen);
                        }
                    });

                    node.setOnMouseExited((MouseEvent z) -> {
                        root.getChildren().remove(root.getChildren().size()-1);
                        gp.getChildren().remove(gp.getChildren().size()-1);
                    });
                }


            });


        }
    }

}

