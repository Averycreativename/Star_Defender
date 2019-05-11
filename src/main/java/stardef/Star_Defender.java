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
        //System.out.println(myship.name+" "+m);
        //SaveEmpires(empires,"TEST.xml");
        //this.getClass().getResource("src/main/resources/Stylesheets/Simplestylesheet.css");

        //System.out.println(empires.getempires().get(0).nameofempire);
        primaryStage.setTitle("Star Defender");


        Pane p=new Pane();
        Scene scene=new Scene(p);

        //scene.setRoot(Mainmenuhandler.MainMenu(scene));

        //scene.setRoot(Mainmenuhandler.MapChooser(scene));
        scene.setRoot(Mainmenuhandler.MainMenu(scene));
      /*Scale scale = new Scale(2,2);
      scale.setPivotX(scene.getWidth()/4);
      scale.setPivotY(scene.getHeight()/4);
      scene.getRoot().getTransforms().setAll(scale);*/

      //repositioning (it's a last thing i want to do)
        primaryStage.widthProperty().addListener((observable,oldvalue,newvalue)->{
            //System.out.println("width changed");
          //this is working   //AnchorPane.setLeftAnchor(primaryStage.sceneProperty().get().getRoot().getChildrenUnmodifiable().get(0),(double) primaryStage.getWidth()/4);

        });
        primaryStage.heightProperty().addListener((observable,oldvalue,newvalue)->{
            //System.out.println("height changed");
            //System.out.println(primaryStage.sceneProperty().get().getRoot().getChildrenUnmodifiable().get(0).getParent().getClass());//anchorpane

            //System.out.println(primaryStage.getWidth());
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