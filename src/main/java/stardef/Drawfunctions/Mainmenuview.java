package stardef.Drawfunctions;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import org.pmw.tinylog.Logger;
import stardef.Baseclasses.*;
import stardef.Scenehandler.*;
import stardef.Star_Defender;

import java.util.List;

import static stardef.Scenehandler.Mainmenuhandler.*;

public class Mainmenuview {
    static ScrollPane CreateNewEmpires(Scene currentScene)
    {
        VBox box=new VBox(20);
        ScrollPane pane=new ScrollPane(box);
        HBox hfile=new HBox(30);
        Label namelabel2=new Label("Filename:");
        TextField tf=new TextField();
        hfile.getChildren().addAll(namelabel2,tf);

        Label namelabel=new Label("Empires:");
        HBox hbe= Addnewempire();

        Font font=new Font(36);
        Button btnaddnewempires=new Button("+");
        Button btncreatenewempires=new Button("Create new Empires");
        btncreatenewempires.setFont(font);
        VBox box2=new VBox(20);
        box2.getChildren().add(hbe);


        Label empty=new Label();
        empty.setFont(new Font(80));

        btnaddnewempires.setOnMouseClicked(e-> {
            box2.getChildren().add(Addnewempire());
        });
        btncreatenewempires.setOnMouseClicked(e->{

            Empires emp=new Empires();
            for(int i=0;i<box2.getChildren().size();i++)
            {
                HBox h=((HBox)box2.getChildren().get(i));
                emp.add(new Empire(new CustomColor(Integer.parseInt(((NumberTextField) h.getChildren().get(1)).getText()),
                        Integer.parseInt(((NumberTextField) h.getChildren().get(1)).getText()),
                        Integer.parseInt(((NumberTextField) h.getChildren().get(1)).getText())),
                        ((TextField)h.getChildren().get(0)).getText()));
            }
            Scenefiller.SaveEmpires(emp,tf.getText());
        });

        box.setTranslateX(Screen.getPrimary().getVisualBounds().getWidth()/16);
        box.setTranslateY(Screen.getPrimary().getVisualBounds().getHeight()/16);
        box.getChildren().addAll(namelabel,hfile,box2,btnaddnewempires,btncreatenewempires,empty);

        return pane;
    }
    static Pane CreateExtension(Scene currentscene)
    {
        Pane pane=new Pane();
        Font font=new Font(36);
        Button btncreateship=new Button("Create new ship");
        btncreateship.setFont(font);
        buttonhovereffect(btncreateship);
        buttonsimplified(btncreateship);
        buttonclickchangescene(btncreateship,currentscene,CreateNewShip(currentscene));

        Button btncreateempires=new Button("Create new empires");
        btncreateempires.setFont(font);
        buttonhovereffect(btncreateempires);
        buttonsimplified(btncreateempires);
        buttonclickchangescene(btncreateempires,currentscene,CreateNewEmpires(currentscene));

        VBox box=new VBox(20);
        box.setTranslateX(Screen.getPrimary().getVisualBounds().getWidth()/4);
        box.setTranslateY(Screen.getPrimary().getVisualBounds().getHeight()/4);
        box.getChildren().addAll(btncreateship,btncreateempires);
        pane.getChildren().add(box);

        return pane;
    }
    static ScrollPane CreateNewShip(Scene currentscene)
    {
        VBox box=new VBox(20);
        ScrollPane pane=new ScrollPane(box);
        Font font=new Font(36);
        Button btncreateship=new Button("Create new ship");
        btncreateship.setFont(font);

        HBox hbname=new HBox(20);
        Label namelabel=new Label("Shipname:");
        TextField namef = new TextField();
        namef.setPromptText("Ship image");
        TextField namef2 = new TextField();
        namef2.setPromptText("name");
        TextField namef3 = new TextField();
        namef3.setPromptText("Layout");
        hbname.getChildren().addAll(namelabel,namef,namef2,namef3);

        HBox hbfuel=new HBox(20);
        Label fuellabel=new Label("Fuel:  ");
        NumberTextField fuelf = new NumberTextField();
        hbfuel.getChildren().addAll(fuellabel,fuelf);

        HBox hbrocket=new HBox(20);
        Label rocketlabel=new Label("Rocket:");
        NumberTextField rocketf = new NumberTextField();
        hbrocket.getChildren().addAll(rocketlabel,rocketf);

        HBox hbdrone=new HBox(20);
        Label dronelabel=new Label("Drone:");
        NumberTextField dronef = new NumberTextField();
        hbdrone.getChildren().addAll(dronelabel,dronef);

        HBox hbhealth=new HBox(20);
        Label healthlabel=new Label("Health:");
        NumberTextField healthf = new NumberTextField();
        hbhealth.getChildren().addAll(healthlabel,healthf);

        HBox hbe=new HBox(20);
        Label elabel=new Label("System energy:");
        NumberTextField ef = new NumberTextField();
        hbe.getChildren().addAll(elabel,ef);

        Label stafflabel=new Label("Staff:");
        VBox staffbox=new VBox(15);
        HBox staffhbox=Addnewstaff();
        staffbox.getChildren().add(staffhbox);
        Button btnaddstaff=new Button("+");

        Label roomslabel=new Label("Rooms:");
        VBox roomsbox=new VBox(15);
        HBox pilotingroomshbox=Addnewroom();
        ((ComboBox)(pilotingroomshbox.getChildren().get(4))).getSelectionModel().select(Room.Roomtype.Piloting.toString());
        HBox engineroomshbox=Addnewroom();
        ((ComboBox)(engineroomshbox.getChildren().get(4))).getSelectionModel().select(Room.Roomtype.Engine.toString());
        roomsbox.getChildren().addAll(pilotingroomshbox,engineroomshbox);
        Button btnaddroom=new Button("+");


        btncreateship.setFont(font);


        box.setTranslateX(Screen.getPrimary().getVisualBounds().getWidth()/16);
        box.setTranslateY(Screen.getPrimary().getVisualBounds().getHeight()/16);
        Label empty=new Label();
        empty.setFont(new Font(100));
        box.getChildren().addAll(hbname,hbfuel,hbrocket,hbdrone,hbhealth,hbe,stafflabel,staffbox,btnaddstaff,roomslabel,roomsbox,btnaddroom,btncreateship,empty);



        btnaddstaff.setOnMouseClicked(e->{
            final int max=4;
            if(staffbox.getChildren().size()<max)
            {
                staffbox.getChildren().add(Addnewstaff());
                if(staffbox.getChildren().size()==max)
                {
                    box.getChildren().remove(box.getChildren().indexOf(staffbox)+1);
                }

            }
        });
        btnaddroom.setOnMouseClicked(e->{
            final int max=12;
            if(roomsbox.getChildren().size()<max)
            {
                roomsbox.getChildren().add(Addnewroom());
                if(roomsbox.getChildren().size()==max)
                {
                    box.getChildren().remove(box.getChildren().indexOf(roomsbox)+1);
                }
            }
        });

        btncreateship.setOnMouseClicked(e->{
            try {
                Room[] rooms=new Room[roomsbox.getChildren().size()];
                for(int i=0;i<roomsbox.getChildren().size();i++)
                {
                    HBox chb=((HBox)roomsbox.getChildren().get(i));
                    if(((ComboBox)chb.getChildren().get(4)).getSelectionModel().getSelectedItem()== Room.Roomtype.None.toString())
                    {
                        rooms[i] = new Room(new Vector2D(Integer.parseInt(((TextField)chb.getChildren().get(0)).getText()),
                                Integer.parseInt(((TextField)chb.getChildren().get(1)).getText())),

                                new Vector2D(Integer.parseInt(((TextField)chb.getChildren().get(2)).getText()),
                                        Integer.parseInt(((TextField)chb.getChildren().get(3)).getText())));
                    }
                    else
                    {
                        rooms[i] = new Room(new Vector2D(Integer.parseInt(((TextField)chb.getChildren().get(0)).getText()),
                                Integer.parseInt(((TextField)chb.getChildren().get(1)).getText())),

                                new Vector2D(Integer.parseInt(((TextField)chb.getChildren().get(2)).getText()),
                                        Integer.parseInt(((TextField)chb.getChildren().get(3)).getText())),

                                Room.Roomtype.valueOf(((ComboBox)chb.getChildren().get(4)).getSelectionModel().getSelectedItem().toString()),

                                new Vector2D(Integer.parseInt(((TextField)chb.getChildren().get(5)).getText()),
                                        Integer.parseInt(((TextField)chb.getChildren().get(6)).getText())),
                                Integer.parseInt(((TextField)chb.getChildren().get(7)).getText()));

                    }
                }
                Logger.info("Room[] created!");
                List<Vector2D> v2dlist=Room.AllpossiblePositions(rooms);
                Race[] races=new Race[staffbox.getChildren().size()];
                for(int i=0;i<staffbox.getChildren().size();i++)
                {
                    HBox hbox=(HBox) staffbox.getChildren().get(i);
                    races[i]=new Race(((TextField)hbox.getChildren().get(0)).getText(),
                            ((TextField)hbox.getChildren().get(1)).getText(),
                            v2dlist.get(0));
                    v2dlist.remove(0);
                }
                Logger.info("Race[] created!");
                Scenefiller.SaveSpaceship(
                        new Spaceship(namef.getText(),namef2.getText(),namef3.getText(),
                                rooms
                                ,new Shipdata(Integer.parseInt(fuelf.getText()),Integer.parseInt(rocketf.getText()),
                                Integer.parseInt(dronef.getText()),Integer.parseInt(healthf.getText()),
                                races,Integer.parseInt(ef.getText())))
                        ,namef.getText()+"_"+namef3.getText());

                Logger.info("Spaceship saved!");
            }
            catch (Exception exp) { exp.printStackTrace(); Logger.error("Something went wrong!"); Logger.error(exp.getMessage()); }
        });
        return pane;
    }
    public static Pane MainMenu(Scene currentscene)
    {
        Pane pane=new Pane();
        Font font=new Font(36);
        Button btnnewgame=new Button("New Game");
        btnnewgame.setFont(font);
        buttonhovereffect(btnnewgame);
        buttonsimplified(btnnewgame);
        buttonclickchangescene(btnnewgame,currentscene,NewGameMenu(currentscene));

        Button btnloadgame=new Button("Load Game");
        btnloadgame.setFont(font);
        buttonhovereffect(btnloadgame);
        buttonsimplified(btnloadgame);

        Button btncreateextension=new Button("Create simple extension");
        btncreateextension.setFont(font);
        buttonhovereffect(btncreateextension);
        buttonsimplified(btncreateextension);
        buttonclickchangescene(btncreateextension,currentscene,CreateExtension(currentscene));

        Button btncheckextensions=new Button("Check for extension files");
        btncheckextensions.setFont(font);
        buttonhovereffect(btncheckextensions);
        buttonsimplified(btncheckextensions);

        Button btnoptions=new Button("Options");
        btnoptions.setFont(font);
        buttonhovereffect(btnoptions);
        buttonsimplified(btnoptions);

        Button btnexit=new Button("Exit Game");
        btnexit.setFont(font);
        buttonhovereffect(btnexit);
        buttonsimplified(btnexit);


        VBox box=new VBox(20);
        box.setTranslateX(Screen.getPrimary().getVisualBounds().getWidth()/4);
        box.setTranslateY(Screen.getPrimary().getVisualBounds().getHeight()/4);
        box.getChildren().addAll(btnnewgame,btnloadgame,btncreateextension,btncheckextensions,btnoptions,btnexit);

        pane.getChildren().add(box);
        return pane;
    }
    public static Pane NewGameMenu(Scene currentscene)
    {
        Pane pane=new Pane();
        Font font=new Font(36);

        Button btndifEASY=new Button("Easy");
        btndifEASY.setFont(font);
        buttonhovereffect(btndifEASY);
        buttonsimplified(btndifEASY);
        buttonclickchangescene(btndifEASY,currentscene,MapChooser(currentscene));

        Button btndifNORMAL=new Button("Normal");
        btndifNORMAL.setFont(font);
        buttonhovereffect(btndifNORMAL);
        buttonsimplified(btndifNORMAL);

        Button btndifHARD=new Button("Hard");
        btndifHARD.setFont(font);
        buttonhovereffect(btndifHARD);
        buttonsimplified(btndifHARD);

        VBox box=new VBox(20);
        box.setTranslateX(Screen.getPrimary().getVisualBounds().getWidth()/4);
        box.setTranslateY(Screen.getPrimary().getVisualBounds().getHeight()/4);
        box.getChildren().addAll(btndifEASY,btndifNORMAL,btndifHARD);
        pane.getChildren().add(box);
        return pane;
    }
    static void buttonhovereffect(Button button)
    {
        button.setOnMouseEntered(x ->
        {
            button.setText("-> "+button.getText());
            button.setFont(new Font(button.getFont().getSize()+10));
            button.setOnMouseExited(y -> {
                button.setText(button.getText().substring(3));
                button.setFont(new Font(button.getFont().getSize()-10));
            });
        });
    }
    static void buttonsimplified(Button button)
    {
        String cnstpath="src/main/resources/Stylesheets/Simplestylesheet.css";
        button.setStyle(
                "-fx-background-color: none;" +
                        "-fx-focus-color: none;" +
                        "");
    }
    public static Pane MapChooser(Scene currentscene)
    {
        Pane pane=new Pane();
        VBox box=new VBox(50);
        HBox hbox=OpenMapChooser("Maps/Small");

        Font font=new Font(36);
        Button btnstart=new Button("Start");
        btnstart.setFont(font);
        buttonhovereffect(btnstart);
        buttonsimplified(btnstart);
        ListView lv = (ListView) hbox.getChildren().get(0);
        lv.getSelectionModel().select(0);
        String cnstpath="/Maps/Small/";
        btnstart.setOnMouseClicked(m->{
            String path = cnstpath + lv.getSelectionModel().getSelectedItem().toString() + ".png";
            if(path.contains("/")){path=cnstpath+path.substring(path.lastIndexOf("/")+1);}
            Image img=new Image(Star_Defender.class.getResourceAsStream(path));
            Mainhandler.Generategame(
                    Starmaphandler.Getstarlocation(img), currentscene);
            currentscene.setRoot(Mainhandler.ssh.GameScreen);
            AnchorPane.setTopAnchor(currentscene.getRoot().getChildrenUnmodifiable().get(0), 0.0);
            Logger.info(currentscene.getRoot().getClass());
        });
        box.getChildren().addAll(hbox,btnstart);
        box.setAlignment(Pos.CENTER);
        pane.getChildren().add(box);
        return pane;
    }
}
