package stardef;//package stardef;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
//import javafx.stage.ChangeListener;


import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import stardef.Baseclasses.*;
import stardef.Drawfunctions.Mainmenuview;
import stardef.Scenehandler.Mainhandler;
import stardef.Scenehandler.Mainmenuhandler;
import stardef.Scenehandler.Scenefiller;
import stardef.Scenehandler.Shipscenehandler;

import static stardef.Scenehandler.Scenefiller.*;

public class Star_Defender extends Application
{
    Mainhandler mh;


    Parent UpdateStarMap()
    {
        GridPane root=new GridPane();
        return root;
    }

    @Override
    public void start(Stage primaryStage) /*throws Expectation*/
    {
        mh=new Mainhandler();
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Star Defender");


        Pane p=new Pane();
        Scene scene=new Scene(p);

        scene.setRoot(Mainmenuview.MainMenu(scene));

        primaryStage.widthProperty().addListener((observable,oldvalue,newvalue)->{
        });
        primaryStage.heightProperty().addListener((observable,oldvalue,newvalue)->{

        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) { launch(args);  }


    //Return rgb value of single pixel.
    //Using this in the Getempireterrorities would be very slow, since it need to load the image for every single pixel
    public Color Getrgbcolorat(Image img,int x, int y)
    {
        PixelReader pixelReader= img.getPixelReader();
        return pixelReader.getColor(x, y);
    }
}