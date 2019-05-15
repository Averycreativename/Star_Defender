package stardef.Scenehandler;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import org.pmw.tinylog.Logger;
import stardef.Baseclasses.*;
import stardef.Baseclasses.Xmlloader.Xmlloader;
import stardef.Star_Defender;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.rmi.UnexpectedException;
import java.security.CodeSource;
import java.util.*;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Mainmenuhandler {




    public static void buttonclickchangescene(Button button, Scene scene, Parent changeto)
    {
        button.setOnMouseClicked(m->{
            scene.setRoot(changeto);
        });
    }
    static List<String> getFilenamesinDirectory(String path)
    {
        if(!Xmlloader.isInJAR()) {
            String cnstpath = "src/main/resources/";
            File directory = new File(cnstpath + path);
            File[] listOfFiles = directory.listFiles();
            List<String> str = new ArrayList<String>();
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    str.add(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().indexOf('.')));
                }

            }
            return str;
        }
        else{
            try {
                List<String> str = new ArrayList<String>();
                URI uri = ClassLoader.getSystemResources(path).nextElement().toURI();
                Path myPath;
                if (uri.getScheme().equals("jar")) {
                    FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
                    myPath = fileSystem.getPath(path);
                } else {
                    myPath = Paths.get(uri);
                }
                Stream<Path> walk = Files.walk(myPath, 1);
                for (Iterator<Path> it = walk.iterator(); it.hasNext();) {
                    String cstr=it.next().toString();
                    if(cstr.contains("."))
                    {
                        str.add(cstr.substring(cstr.lastIndexOf('\\')+1, cstr.indexOf('.')));
                    }

                }
                return str;
            }catch (URISyntaxException exp){Logger.error(exp.getMessage());}
             catch (IOException exp){Logger.error(exp.getMessage());}
        }
        return new ArrayList<String>();

    }
    public static HBox OpenMapChooser(String path)
    {
        List<String> strings=getFilenamesinDirectory(path);
        ListView lv = new ListView(FXCollections.observableList(strings));
        HBox hbox = new HBox(lv);
        lv.setOnMouseClicked(e ->{
        });
        hbox.setAlignment(Pos.CENTER);
        hbox.setFillHeight(false);
        return hbox;
    }
    public static HBox Addnewstaff()
    {
        HBox hbstaff=new HBox(15);
        TextField staffrace = new TextField();
        staffrace.setPromptText("Racename");
        TextField staffname = new TextField();
        staffname.setPromptText("Personal name");
        hbstaff.getChildren().addAll(staffrace,staffname);
        return hbstaff;
    }
    public static HBox Addnewroom()
    {
        HBox hbroom=new HBox(15);
        NumberTextField roomxstart = new NumberTextField();
        roomxstart.setPromptText("X coordinate");
        NumberTextField roomystart = new NumberTextField();
        roomystart.setPromptText("Y coordinate");
        NumberTextField roomxsize = new NumberTextField();
        roomxsize.setPromptText("X size");
        NumberTextField roomysize = new NumberTextField();
        roomysize.setPromptText("Y size");
        ObservableList<String> options =
                FXCollections.observableArrayList(Arrays.stream(Room.Roomtype.values()).map(Enum::name).toArray(String[]::new));

        ComboBox roomtype = new ComboBox(options);
        roomtype.getSelectionModel().selectFirst();

        roomtype.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String old, String current) {
                if(current!= Room.Roomtype.None.toString()&&hbroom.getChildren().size()==5)
                {
                    NumberTextField roomxcp = new NumberTextField();
                    roomxcp.setPromptText("X controller's position");
                    NumberTextField roomycp = new NumberTextField();
                    roomycp.setPromptText("Y controller's position");
                    NumberTextField roomecp = new NumberTextField();
                    roomecp.setPromptText("Maximum system energy");
                    hbroom.getChildren().addAll(roomxcp,roomycp,roomecp);
                }
                else if(hbroom.getChildren().size()==7&&current==Room.Roomtype.None.toString())
                {
                    hbroom.getChildren().remove(5,7);
                }
            }
        });


        hbroom.getChildren().addAll(roomxstart,roomystart,roomxsize,roomysize,roomtype);
        return hbroom;
    }
    public static HBox Addnewempire()
    {
        HBox hbname=new HBox(20);
        TextField namef = new TextField();
        namef.setPromptText("Name of empire");
        NumberTextField red = new NumberTextField();
        red.setPromptText("RED");
        NumberTextField green = new NumberTextField();
        green.setPromptText("GREEN");
        NumberTextField blue = new NumberTextField();
        blue.setPromptText("BLUE");
        hbname.getChildren().addAll(namef,red,green,blue);
        return hbname;
    }



}
