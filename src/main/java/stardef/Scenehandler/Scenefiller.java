package stardef.Scenehandler;

import javafx.geometry.HPos;
import javafx.stage.Screen;
import stardef.Baseclasses.Xmlloader.Xmlloader;
import javafx.geometry.Pos;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import stardef.Baseclasses.*;

import java.util.ArrayList;
import java.util.List;

public class Scenefiller {

    public static Empires LoadEmpires(String name)
    {
        Empires empires=Xmlloader.XMLfileToEmpires(name);
        return empires;
    }

    public static void SaveEmpires(Empires empires,String name)
    {
        Xmlloader.jaxbObjectToXMLfile((Empires)empires,name+".xml");
        System.out.print(Xmlloader.jaxbObjectToXML((Empires)empires));
    }
    public static void SaveSpaceship(Spaceship sp,String name)
    {
        Xmlloader.jaxbObjectToXMLfile((Spaceship)sp,name+".xml");
        System.out.print(Xmlloader.jaxbObjectToXML((Spaceship)sp));
    }
    public static Spaceship LoadSpaceship(String name)
    {
        Spaceship sps=Xmlloader.XMLfileToShipdata(name);
        return sps;
    }

    public static AnchorPane createStarMap(Image img, List<Empire> empires,Vector2D playerposition,boolean startingscene)
    {
        AnchorPane root=new AnchorPane();
        List<Vector2D> booleans=Starmaphandler.Getstarlocation(img);
        Getempireterrorities(empires,booleans,img);
        GridPane gp=Starmaphandler.fillstarmap(booleans,(int)img.getWidth(),(int)img.getHeight());
        gp.setMinSize(img.getWidth(),img.getHeight());

        Starmaphandler.Starmapanimation(root,gp,booleans,"highlightedstar.gif",playerposition,"blue");
        gp.setAlignment(Pos.CENTER);
        root.getChildren().add(gp);
        gp.setAlignment(Pos.CENTER);
        return root;
    }

    public static AnchorPane LoadStarMap(List<Empire> empires,Vector2D playerposition)
    {
        AnchorPane root=new AnchorPane();
        GridPane gp=Starmaphandler.fillstarmap(Mainhandler.starmaplocations,64,64);
        gp.setMinSize(64,64);

        Starmaphandler.Starmapanimation(root,gp,Mainhandler.starmaplocations,"highlightedstar.gif",playerposition,"blue");

        gp.setAlignment(Pos.CENTER);
        root.getChildren().add(gp);
        GridPane.setHalignment(gp.getParent(), HPos.CENTER);
        AnchorPane.setLeftAnchor(gp, Screen.getPrimary().getBounds().getWidth()/4);
        gp.setAlignment(Pos.CENTER);
        return  root;
    }


    static void Getempireterrorities(List<Empire> empires, List<Vector2D> bools,Image img) {
        PixelReader pixelReader= img.getPixelReader();
        for (int x = 0; x < bools.size(); x++) {
                //check if belong to any empire
            Vector2D v2d=bools.get(x);
            Color color = pixelReader.getColor(v2d.x, v2d.y);
            for (int i = 0; i < empires.size(); i++) {
                Empire e = (Empire) empires.get(i);
                if (Starmaphandler.samergbcolor(e.colorofempire.getFXColor(), color))//if true it adds a Vector2D to the Empire that identified by that value
                {
                    if(empires.get(i).terrorityofempire==null){empires.get(i).terrorityofempire=new ArrayList<Vector2D>(); }
                    empires.get(i).terrorityofempire.add(v2d);
                }
            }
        }
    }




}
