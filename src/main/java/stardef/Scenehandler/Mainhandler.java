package stardef.Scenehandler;

import javafx.scene.Scene;
import stardef.Baseclasses.Empires;
import stardef.Baseclasses.Spaceship;
import stardef.Baseclasses.Vector2D;

import java.util.ArrayList;
import java.util.List;

import static stardef.Scenehandler.Scenefiller.LoadEmpires;

public class Mainhandler {
    public static Empires empires;
    public static Spaceship myship;
    public static boolean changetootherscene=false;
    public static Vector2D starmapposition=new Vector2D();
    public static List<Vector2D> starmaplocations=new ArrayList<>();
    public static Shipscenehandler ssh;

    /**
     * Create an instance of mainhandler,
     * and load data from xml files.
     *
     */
    public static void Generategame(List<Vector2D> starv2ds, Scene scene)
    {
        starmaplocations=starv2ds;
        starmapposition=starv2ds.get(0);
        empires=new Empires();
        empires=LoadEmpires("Simple.xml");
        myship=Scenefiller.LoadSpaceship("strdfxcf_A.xml");
        ssh=new Shipscenehandler(scene);

    }

}

