package stardef.Scenehandler;


/*bugs:
enemy position in Moves() doesn't check if they are on my ship; - now it does
also the enemies are set to my ship --- need a Setupenemyship() method to rewrite the variables; - resolved
roomposxy variable used in methods where they should not be; - i don't even know what is this
Need to rewrite v2dpath() due to inefficent pathfinding;
Didn't finish writing the drawmoves() method (can't replace position) - done
in Drawmoves() way to react to enemy is not finished -not anymore
 */
import org.pmw.tinylog.Logger;
import com.sun.glass.ui.Screen;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.transform.Transform;
import javafx.util.Duration;
import stardef.Baseclasses.Race;
import stardef.Baseclasses.Room;
import stardef.Baseclasses.Vector2D;
import stardef.Baseclasses.Shipdata;
import stardef.Baseclasses.Spaceship;
import stardef.Star_Defender;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The class that handles the scene
 */
public class Shipscenehandler {

    Mainhandler ctrl;
    Spaceship Enemyship;
    public Canvas canvas;
    GraphicsContext gc;
    private Timeline timeLine;
    private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0 ;
    private boolean arrayFilled = false ;

    public StackPane GameScreen;

    boolean InputDisabled=false;
    public Image premadecanvas;


    double roomsize=40;
    double shift;
    double[] shipboundaries;
    double[] roomposxy;
    Color[] Stepcolor=new Color[]
            {
                    new Color(Color.GREEN.getRed(),Color.GREEN.getGreen(),Color.GREEN.getBlue(),0),
                    new Color(Color.GREEN.getRed(),Color.GREEN.getGreen(),Color.GREEN.getBlue(),0.3),
                    new Color(Color.GREEN.getRed(),Color.GREEN.getGreen(),Color.GREEN.getBlue(),0.5),
                    new Color(Color.BLUEVIOLET.getRed(),Color.BLUEVIOLET.getGreen(),Color.BLUEVIOLET.getBlue(),0.3),
                    new Color(Color.BLUEVIOLET.getRed(),Color.BLUEVIOLET.getGreen(),Color.BLUEVIOLET.getBlue(),0.5),
                    new Color(Color.YELLOW.getRed(),Color.YELLOW.getGreen(),Color.YELLOW.getBlue(),0.3),
                    new Color(Color.YELLOW.getRed(),Color.YELLOW.getGreen(),Color.YELLOW.getBlue(),0.5),
                    new Color(Color.RED.getRed(),Color.RED.getGreen(),Color.RED.getBlue(),0.3),
                    new Color(Color.RED.getRed(),Color.RED.getGreen(),Color.RED.getBlue(),0.5),
                    new Color(Color.SANDYBROWN.getRed(),Color.SANDYBROWN.getGreen(),Color.SANDYBROWN.getBlue(),0.3),
                    new Color(Color.SANDYBROWN.getRed(),Color.SANDYBROWN.getGreen(),Color.SANDYBROWN.getBlue(),0.5),
            };
    Scene scene;

    public Shipscenehandler(Scene scene)
    {

        this.scene=scene;
        GameScreen=new StackPane();
        canvas = new Canvas(Screen.getMainScreen().getWidth(),Screen.getMainScreen().getHeight());
        shift=canvas.getWidth()/10;
        shipboundaries=new double[]{canvas.getHeight()/16,canvas.getHeight()/8,canvas.getHeight()/2*1.5d};
        roomposxy=new double[]{(shipboundaries[0] + shipboundaries[2] + shift)/ (2),
                (shipboundaries[1] + shipboundaries[2])/ (3),
                canvas.getWidth()-(shipboundaries[0] + shipboundaries[2] + shift)/ (2)+shift/4,
                (shipboundaries[0] + shipboundaries[2] + shift)/ (2)+shift*2,};
        gc = canvas.getGraphicsContext2D();
        Enemyship=Scenefiller.LoadSpaceship("strdfxcf_B.xml");
        SetUpEnemyShip();
        premadecanvas=pixelScaleAwareCanvasSnapshot(CreateShipScenePremade(ctrl.myship,Enemyship),1);
        GameScreen.getChildren().add(canvas);

        Drawselectable(ctrl.myship.shipdata.staff);

        redraw(30);//first i need to set up a scene
        //FPSmeter();

    }
    private void redraw(int FPS) {
        timeLine = new Timeline();
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.getKeyFrames().add(
                new KeyFrame(Duration.millis(1000/FPS),
                        new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent t) {
                                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                                CreateShipSceneRedraw(premadecanvas);

                            }
                        },
                        new KeyValue[0])
        );
        timeLine.playFromStart();
    }

    void FPSmeter() {
        AnimationTimer frameRateMeter = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long oldFrameTime = frameTimes[frameTimeIndex] ;
                frameTimes[frameTimeIndex] = now ;
                frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length ;
                if (frameTimeIndex == 0) {
                    arrayFilled = true ;
                }
                if (arrayFilled) {
                    long elapsedNanos = now - oldFrameTime ;
                    long elapsedNanosPerFrame = elapsedNanos / frameTimes.length ;
                    double frameRate = 1_000_000_000.0 / elapsedNanosPerFrame ;
                    Logger.info(frameRate);
                }
            }
        };
        frameRateMeter.start();
    }

//region UI
    public void UI_Systems(GraphicsContext gc,String[] systems,int[] systemenergy,double xstart,double ystart,double yspacing,double drawingsize)
    {
        String cnstpath="file:src/main/resources/Sprites/Icons/";
        for(int i=0;i<systemenergy.length;i++)
        {
            UI_SingleSystembar(gc,cnstpath+systems[i],7,5,xstart,ystart+i*(yspacing+drawingsize),drawingsize);
        }
    }
    public void UI_SingleSystembar(GraphicsContext gc,String imgurl, double xspacing,int numberofenergy,double xstart,double ystart, double drawingsize)
    {
        gc.drawImage(new Image(imgurl),xstart,ystart,drawingsize,drawingsize);
        Rectangle rectangle=new Rectangle(0,ystart,drawingsize/2,drawingsize);
        for(int i=0;i<numberofenergy;i++)
        {
            rectangle.setX(xstart+2*drawingsize/3+(drawingsize/2+xspacing)*(i+1));
            drawRectangle(gc, rectangle, Color.GREEN);
        }
    }
    public void UI_SingleSystembarIcon()
    {

    }
    public void UI_SystembarIcons()
    {

    }
    public void drawRectangle(GraphicsContext gc,Rectangle rect,Color fillcolor){
        gc.setFill(fillcolor);
        gc.fillRect(rect.getX(),
                rect.getY(),
                rect.getWidth(),
                rect.getHeight());
    }
//endregion
//region Drawing
    void DrawRooms(double xstartship, double ystartship, Room[] rooms,GraphicsContext gc)
    {
        Image roomimage = new Image("file:src/main/resources/Sprites/Tiles/shiptesttile.png");
        Image panelimage = new Image("file:src/main/resources/Sprites/Tiles/shiptilepanel.png");
        for(int i=0;i<rooms.length;i++) {
            Room croom=rooms[i];
            for (int y = 0; y < croom.roomsize.y; y++) {
                for (int x = 0; x < croom.roomsize.x; x++) {
                    gc.drawImage(roomimage,
                            xstartship+roomsize*(croom.position.x+x),
                            ystartship+roomsize*(croom.position.y+y),
                            roomsize,roomsize);
                    if(croom.Controlltileinroomsize!=null)
                    {
                        if(croom.Controlltileinroomsize.x==x&&croom.Controlltileinroomsize.y==y)
                        gc.drawImage(panelimage,
                                xstartship+roomsize*(croom.position.x+x),
                                ystartship+roomsize*(croom.position.y+y),
                                roomsize,roomsize/3);
                    }
                }
            }
        }
    }
    void Drawcrew(double xstartship, double ystartship, Race[] crew)
    {
        double crewsize=40;
        for(int i=0;i<crew.length;i++)
        {
            Image image=new Image("file:src/main/resources/Sprites/Characters/"+crew[i].racename+".png");
            Race crace=crew[i];

            if(Enemyship!=null)
            {
                gc.drawImage(image,//load image of the race
                        (crace.Onmyship ? roomposxy[0] : roomposxy[2]) + roomsize * crace.position.x,
                        roomposxy[1] + roomsize * crace.position.y,
                        crewsize,
                        crewsize
                );
            }
            else
                {
                    gc.drawImage(image,//load image of the race
                            roomposxy[3] + roomsize * crace.position.x,
                            roomposxy[1] + roomsize * crace.position.y,
                            crewsize,
                            crewsize
                    );
                }
        }
    }
//endregion
//region Moving_related_methods
    void MoveAnimator(List<Vector2D> steps,Race race,boolean myturn)
    {
        InputDisabled=true;
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000/5),
                    e-> {
                Logger.info(race.position.x+" "+race.position.y);
                if(steps.size()==0)
                {
                    InputDisabled=false;
                    Drawselectable(ctrl.myship.shipdata.staff);
                    timeline.stop();
                }
                else if(steps.size()==1&&myturn&& Arrays.stream(ctrl.myship.shipdata.staff).anyMatch(f->Vector2D.Equal(f.position,steps.get(0))))
                {

                    Race switchedwith=Arrays.stream(ctrl.myship.shipdata.staff).filter(f->Vector2D.Equal(f.position,steps.get(0))).findFirst().get();
                    Vector2D holder=new Vector2D(race.position.x,race.position.y);
                    race.position=switchedwith.position;
                    switchedwith.position=holder;
                    steps.remove(0);
                }
                else {
                    race.position = steps.get(0);
                    steps.remove(0);
                }
                    }));
            timeline.playFromStart();

    }
    void TeleportAnimator(List<Vector2D> steps,Race race,boolean myturn)
    {
        InputDisabled=true;
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000/2),
                e-> {
                    if(steps.size()==0)
                    {
                        InputDisabled=false;
                        Drawselectable(ctrl.myship.shipdata.staff);
                        timeline.stop();
                    }
                    else if(steps.size()==1)
                        {
                            race.position = steps.get(0);
                            race.Onmyship=!race.Onmyship;
                            steps.remove(0);
                        }
                    else{steps.remove(0);}
                }));
        timeline.playFromStart();
    }
    void ProjectileAnimator(Race start,Race attacked,Pane drawon)
    {
        InputDisabled=true;
        boolean samex=false;
        boolean samey=false;

        Vector2D relativepos=new Vector2D(attacked.position.x-start.position.x,attacked.position.y-start.position.y);

        Vector2D startpos=start.position;

        double degree= Math.toDegrees(Math.atan2((double)relativepos.y,(double)relativepos.x))+90;//degree from top
        Logger.info("Angle: "+degree);

            if(degree>337.5||degree<=22.5){Logger.info("up"); samex=true;}
            else if(degree>22.5&&degree<=67.5){Logger.info("up-right"); startpos=new Vector2D(start.position.x+1,start.position.y);}
            else if(degree>67.5&&degree<=112.5){Logger.info("right");startpos=new Vector2D(start.position.x+1,start.position.y);samey=true;}
            else if(degree>112.5&&degree<=157.5){Logger.info("down-right");startpos=new Vector2D(start.position.x+1,start.position.y+1);}
            else if(degree>157.5&&degree<=202.5){Logger.info("down");startpos=new Vector2D(start.position.x,start.position.y+1);samex=true;}
            else if(degree>202.5&&degree<=247.5){Logger.info("down-left");startpos=new Vector2D(start.position.x,start.position.y+1);}
            else if(degree>247.5&&degree<=292.5){Logger.info("left");samey=true;}
            else if(degree>292.5&&degree<=337.5){Logger.info("up-left");}

        Rectangle projectile = new Rectangle (roomposxy[0]+startpos.x*roomsize+ (samex ? roomsize/2 : 0), roomposxy[1]+(startpos.y-1)*roomsize+(samey ? roomsize/2 : 0),
                20, 10);
        projectile.setArcHeight(5);
        projectile.setArcWidth(5);
        projectile.setFill(Color.RED);
        drawon.getChildren().add(projectile);

        double[] endpos=pathchecker(start,new double[]{(startpos.x-start.position.x)*roomsize+(samex?roomsize/2:0),(startpos.y-start.position.y)*roomsize+(samey?roomsize/2:0)},
                attacked.position,10);

        Path path = new Path();
        path.getElements().add(new MoveTo(roomposxy[0]+startpos.x*roomsize+  (samex?roomsize/2:0) , roomposxy[1]+(startpos.y-1)*roomsize+  (samey?roomsize/2:0) ));
        path.getElements().add(new LineTo(roomposxy[0]+endpos[0],roomposxy[1]+endpos[1]-roomsize));
        InputDisabled=true;
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(100));
        pathTransition.setPath(path);
        pathTransition.setNode(projectile);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(1);
        pathTransition.setAutoReverse(false);
        pathTransition.play();


        if(endpos[0]/roomsize-attacked.position.x>=-0.5&&endpos[0]/roomsize-attacked.position.x<=0.5&&
                endpos[1]/roomsize-attacked.position.y>=-0.5&&endpos[1]/roomsize-attacked.position.y<=0.5)
        {
            Logger.info("The attack was successful!");
            attacked.currenthealth-=start.attack;
        }

        pathTransition.setOnFinished(e->{
            drawon.getChildren().remove(projectile);
            if(attacked.currenthealth<=0)
            {
                Logger.info(attacked.name+" died!");
                if(Arrays.stream(Enemyship.shipdata.staff).anyMatch(f->f==attacked)) {
                    Race[] newarray = new Race[Enemyship.shipdata.staff.length - 1];
                    int index = 0;
                    for (int j = 0; j <newarray.length+1; j++) {
                        if (Enemyship.shipdata.staff[j] != attacked) {
                            newarray[index] = Enemyship.shipdata.staff[j];
                            index++;
                        }
                    }
                    Enemyship.shipdata.staff = newarray;
                }
                else{
                    Race[] newarray = new Race[ctrl.myship.shipdata.staff.length - 1];
                    int index = 0;
                    for (int j = 0; j < newarray.length+1; j++) {
                        if (ctrl.myship.shipdata.staff[j] != attacked) {
                            newarray[index] = ctrl.myship.shipdata.staff[j];
                            index++;
                        }
                    }
                    ctrl.myship.shipdata.staff = newarray;
                }
            }
            else{ Logger.info(attacked.name+" now have "+attacked.currenthealth+" health left.");}
            InputDisabled=false;
            Drawselectable(ctrl.myship.shipdata.staff);
        });
    }


    /**
     * sdfg.
     * @param character Choosen crewmember
     * @param pathshift Csusztatas
     * @param endpos Desired position
     * @param numberofchecks Number of checks after every 1.0d distance between the character and endpos
     * @return The value where this raycasting ends
     */
    double[] pathchecker(Race character,double[] pathshift,Vector2D endpos,int numberofchecks)
    {
        Vector2D current=character.position;
        double[] currentposition=new double[]{character.position.x*roomsize+pathshift[0],character.position.y*roomsize+pathshift[1]};
        double[] endposdouble=new double[]{endpos.x*roomsize+roomsize/2,endpos.y*roomsize+roomsize/2};
        double[] differencedouble=new double[]{endposdouble[0]-currentposition[0],endposdouble[1]-currentposition[1]};
        double distance=DistanceDouble(currentposition,endposdouble);

        if(character.Onmyship)
        {
            List<Vector2D> allrooms=Room.AllpossiblePositions(ctrl.myship.rooms);
            current=new Vector2D((int)(currentposition[0]/roomsize),(int)(currentposition[1]/roomsize));
            Logger.info(current.x+" "+current.y);

            Logger.info(Math.round(numberofchecks*(distance/roomsize)));
            int length=Math.round((float)(numberofchecks*(distance/roomsize)));
            for(int i=0;i<length;i++)
            {
                current=new Vector2D(
                        (int)(currentposition[0]/roomsize
                        +(i*((differencedouble[0]/length)/roomsize))),
                        (int)(currentposition[1]/roomsize
                        +i*((differencedouble[1]/length)/roomsize)));
                Logger.info(current.x+" "+current.y);
                if(!Vector2D.Contains(allrooms,current))
                {
                    return new double[]{currentposition[0] +(i*((differencedouble[0]/length))),
                        currentposition[1] +i*((differencedouble[1]/length))};
                }

            }
            return endposdouble;

        }
        else
            {
                return null;
            }
    }

    /**
     *
     * @param start a position on canvas
     * @param end a position on canvas
     * @return return the distance between 2 points
     */
    double DistanceDouble(double[] start,double[] end)
    {return Math.sqrt((end[1] - start[1]) * (end[1] - start[1]) + (end[0] - start[0]) * (end[0] - start[0]));}
    void Drawselectable(Race[] crew)
    {
        Pane sp=new Pane();
        GameScreen.setPickOnBounds(false);
        canvas.setPickOnBounds(false);
        GameScreen.getChildren().add(sp);

        if(Enemyship==null)
        {
            Button map=new Button("Map");
            HBox buttonbox=new HBox(10);

            buttonbox.setPickOnBounds(false);

            buttonbox.getChildren().add(map);
            GameScreen.getChildren().add(buttonbox);
            map.setOnMouseClicked(e->{
                Logger.info(ctrl.empires.getempires().size()+" ");
                scene.setRoot(Scenefiller.LoadStarMap(ctrl.empires.getempires(),ctrl.starmapposition));
            });
        }



        if(Arrays.stream(crew).anyMatch(e->e.Moved==false))
        {
            for (int i = 0; i < crew.length; i++) {
                Rectangle pane = new Rectangle((Enemyship!=null?roomposxy[0]:roomposxy[3]) + roomsize * crew[i].position.x, roomposxy[1] + roomsize * (crew[i].position.y - 1), roomsize, roomsize);
                pane.setFill(Stepcolor[0]);
                sp.getChildren().add(pane);
                if (!crew[i].Moved) {
                    pane.setOnMouseEntered(e -> {
                        if (!InputDisabled) {
                            pane.setFill(Stepcolor[1]);
                            pane.setOnMouseClicked(r -> {
                                int current = (sp.getChildren().indexOf(pane));
                                Logger.info("Character selected: " + crew[current].name + ", index:" + current + " position:" + crew[current].position.x + " " + crew[current].position.y);
                                Drawmoves(sp, Moves(crew[current], crew[current].speed, Room.AllpossiblePositions(ctrl.myship.rooms),true), crew[current]);
                            });
                        }
                    });

                    pane.setOnMouseExited(e -> {
                        pane.setFill(Stepcolor[0]);
                    });
                }
            }
        }
        //end of my turn
        else
            {

                if(Enemyship!=null)
                {
                    if(Enemyship.shipdata.staff.length!=0&&Arrays.stream(Enemyship.shipdata.staff).anyMatch(e->e.Moved==false))
                    {Enemyturn(Enemyship,ctrl.myship,Getnextenemy());}
                    else if(Enemyship.shipdata.staff.length==0||Enemyship.shipdata.currenthealth==0)
                    {
                        Enemyship=null;
                        Logger.info("The combat is over!");
                        premadecanvas=pixelScaleAwareCanvasSnapshot(CreateShipScenePremade(ctrl.myship,Enemyship),1);
                        Drawselectable(crew);}
                    else
                        {
                            Logger.info("It's the player's turn again!");
                            for(int i=0;i<crew.length;i++)
                            {
                                crew[i].Moved=false;
                            }
                            for (int i = 0; i < Enemyship.shipdata.staff.length; i++) {
                                Enemyship.shipdata.staff[i].Moved = false;

                            }
                            Drawselectable(crew);
                        }
                }
                else
                {
                    for(int i=0;i<crew.length;i++)
                    {
                        crew[i].Moved=false;
                    }
                    Drawselectable(crew);
                }
            }

    }
    void Drawmoves(Pane container,List<Vector2D> moves,Race character)
    {
        container.getChildren().removeAll(container.getChildren());
        for(int i=0;i<moves.size();i++)
        {
            Rectangle rect=new Rectangle((Enemyship!=null?roomposxy[0]:roomposxy[3])+roomsize*moves.get(i).x,roomposxy[1]+roomsize*(moves.get(i).y-1),roomsize, roomsize);
            container.getChildren().add(rect);
            Vector2D current=moves.get(i);

            //if on self
            if(Vector2D.Equal(current,character.position))
            {
                rect.setFill(Stepcolor[3]);
                rect.setOnMouseEntered(e-> {
                    rect.setFill(Stepcolor[4]);
                    rect.setOnMouseClicked(r->{
                        Logger.info(character.name+" hold position, and watch out for intruders");
                        character.Moved=true;
                        character.overwatch=true;
                        container.getChildren().removeAll(container.getChildren());
                        Drawselectable(ctrl.myship.shipdata.staff);
                    });
                });
                rect.setOnMouseExited(e->rect.setFill(Stepcolor[3]));
            }
            //if on ally
            else if(Vector2D.Contains(
                    Arrays.stream(ctrl.myship.shipdata.staff).map(e->e.position).collect(Collectors.toCollection(ArrayList::new)),
                    current))
            {
                rect.setFill(Stepcolor[5]);
                rect.setOnMouseEntered(e-> {
                    rect.setFill(Stepcolor[6]);
                    rect.setOnMouseClicked(r->{
                        Race switchedwith=Arrays.stream(ctrl.myship.shipdata.staff).filter(c->Vector2D.Equal(c.position,current)).findFirst().get();

                        Logger.info(character.name+" and "+ switchedwith.name+ " switched positions");
                        character.Moved=true;
                        container.getChildren().removeAll(container.getChildren());
                        MoveAnimator(v2dpath(character.position,current,moves,true),character,true);
                    });
                });
                rect.setOnMouseExited(e->rect.setFill(Stepcolor[5]));
            }
            //if on enemy
            else if(Enemyship!=null&&
                    Vector2D.Contains(
                    Arrays.stream(Enemyship.shipdata.staff).map(e->e.position).collect(Collectors.toCollection(ArrayList::new)),
                    current)&&
                    Arrays.stream(Enemyship.shipdata.staff).filter(e-> Vector2D.Equal(e.position,current)).map(e->e.Onmyship==character.Onmyship).findFirst().get())
            {
                rect.setFill(Stepcolor[7]);
                rect.setOnMouseEntered(e-> {
                    rect.setFill(Stepcolor[8]);
                    rect.setOnMouseClicked(r->{
                        Race attacked=Arrays.stream(Enemyship.shipdata.staff).filter(c->Vector2D.Equal(c.position,current)&&c.Onmyship==character.Onmyship).findFirst().get();
                        Logger.info(character.name+" attacked "+attacked.name);
                        character.Moved=true;
                        container.getChildren().removeAll(container.getChildren());
                        ProjectileAnimator(character,attacked,container);
                    });
                });
                rect.setOnMouseExited(e->rect.setFill(Stepcolor[7]));
            }
            //if on controllpanel
            else if(Vector2D.Contains(
                    Arrays.stream(ctrl.myship.rooms)
                            .filter(e->e.Controlltileinroomsize!=null)
                            .map(e->new Vector2D(e.position.x+e.Controlltileinroomsize.x,e.position.y+e.Controlltileinroomsize.y))
                            .collect(Collectors.toCollection(ArrayList::new)),
                    current))
            {
                rect.setFill(Stepcolor[9]);
                rect.setOnMouseEntered(e-> {
                    rect.setFill(Stepcolor[10]);
                    rect.setOnMouseClicked(r->{
                        Logger.info(character.name+" moved to: x:"+current.x+" y:"+current.y);
                        character.Moved=true;
                        container.getChildren().removeAll(container.getChildren());
                        MoveAnimator(v2dpath(character.position,current,moves,true),character,true);
                    });
                });
                rect.setOnMouseExited(e->rect.setFill(Stepcolor[9]));
            }
            //if on moved somewhere
            else {
                rect.setFill(Stepcolor[1]);
                rect.setOnMouseEntered(e-> {
                    rect.setFill(Stepcolor[2]);
                    rect.setOnMouseClicked(r->{
                        Logger.info(character.name+" moved to: x:"+current.x+" y:"+current.y);
                        character.Moved=true;
                        container.getChildren().removeAll(container.getChildren());
                        MoveAnimator(v2dpath(character.position,current,moves,true),character,true);
                    });
                });
                rect.setOnMouseExited(e -> rect.setFill(Stepcolor[1]));
            }

        }
    }

    /**
     *
     * @param start position of the character
     * @param end where the character want to get
     * @param moves a list of moves where the character can step based on the speed variable of the character
     * @param IncludeStart if true than the first move of the list will be the start variable
     * @return return the path between the start and end as a list of Vector2D
     */
    List<Vector2D> v2dpath(Vector2D start,Vector2D end,List<Vector2D> moves,boolean IncludeStart)
    {
        List<Vector2D> v2d=new ArrayList<>();
        if(IncludeStart){v2d.add(start);}
        while(!Vector2D.Equal(v2d.get(v2d.size()-1),end))
        {
            v2d.add(moves.stream().filter(e->Distance(e,v2d.get(v2d.size()-1))==1).min(Comparator.comparing(e->Distance(e,end))).get());
        }
        Logger.info("Optimal path: "+v2d.size());
        return v2d;
    }

    /**
     *
     * @param character choosen crew
     * @param distance number of steps
     * @param possiblepos positions on the ship where the character is
     * @param myturn this boolean is necessary because this method is used by the player and also by the ai
     * @return return the possible moves where the character can get
     */
    List<Vector2D> Moves(Race character,int distance,List<Vector2D> possiblepos,boolean myturn)
    {
        ArrayList<Vector2D> v2dlist=new ArrayList<Vector2D>();
        v2dlist.add(character.position);
        int countsteps=0;
        for(int i=0;i<distance;i++)
        {
            List<Vector2D> v2dlistcurrent=new ArrayList<Vector2D>();
            for(int j=0;j<v2dlist.size();j++)
            {
                countsteps++;
                if(Vector2D.Contains(possiblepos,new Vector2D(v2dlist.get(j).x+1,v2dlist.get(j).y)))
                {v2dlistcurrent.add(new Vector2D(v2dlist.get(j).x+1,v2dlist.get(j).y));}
                if(Vector2D.Contains(possiblepos,new Vector2D(v2dlist.get(j).x-1,v2dlist.get(j).y)))
                {v2dlistcurrent.add(new Vector2D(v2dlist.get(j).x-1,v2dlist.get(j).y));}
                if(Vector2D.Contains(possiblepos,new Vector2D(v2dlist.get(j).x,v2dlist.get(j).y+1)))
                {v2dlistcurrent.add(new Vector2D(v2dlist.get(j).x,v2dlist.get(j).y+1));}
                if(Vector2D.Contains(possiblepos,new Vector2D(v2dlist.get(j).x,v2dlist.get(j).y-1)))
                {v2dlistcurrent.add(new Vector2D(v2dlist.get(j).x,v2dlist.get(j).y-1));}
            }
            v2dlist.addAll(v2dlistcurrent);
            Vector2D.Distinct(v2dlist);
            if(Enemyship!=null&&i!=distance-1) {
                if(myturn) {
                    for (int j = 0; j < Enemyship.shipdata.staff.length; j++) {
                        Race v2d = Enemyship.shipdata.staff[j];
                        for (int k = 0; k < v2dlist.size(); k++) {
                            if (Vector2D.Equal(v2d.position, v2dlist.get(k)) && v2d.Onmyship == character.Onmyship) {
                                v2dlist.remove(k);
                            }
                        }
                    }
                }
                else
                    {
                        for (int j = 0; j < ctrl.myship.shipdata.staff.length; j++) {
                            Race v2d = ctrl.myship.shipdata.staff[j];
                            for (int k = 0; k < v2dlist.size(); k++) {
                                if (Vector2D.Equal(v2d.position, v2dlist.get(k)) && v2d.Onmyship == character.Onmyship) {
                                    v2dlist.remove(k);
                                }
                            }
                        }
                    }
            }
        }
        Logger.info("Number of possible moves: "+v2dlist.size());
        Logger.info("Number of itterations done: "+countsteps);

        return v2dlist;
    }
//endregion
//region Create_ship_scenes
    private void CreateShipScene()
    {
        Image image = new Image("file:src/main/resources/Sprites/Ships/strdfxcf.png");


        gc.drawImage(image,shipboundaries[0]+shift,shipboundaries[1],shipboundaries[2],shipboundaries[2]);
        gc.drawImage(image,canvas.getWidth()-shipboundaries[2]+shift/4,shipboundaries[1],shipboundaries[2],shipboundaries[2]);
        gc.strokeLine(canvas.getWidth()/2+shift,0,canvas.getWidth()/2+shift,canvas.getHeight());

        String[] str=new String[]{"Weapons.png","Shield.png","Engine.png"};
        int[] sysenergy=new int[]{6,3,2};
        UI_Systems(gc,str,sysenergy,10,20,30,50);
        DrawRooms(shipboundaries[0]+shift,shipboundaries[1], ctrl.myship.rooms,gc);
        Drawcrew(shipboundaries[0]+shift,shipboundaries[1],ctrl.myship.shipdata.staff);

    }

    /**
     *
     * @param canvas a whole canvas that will be re-rendered every time
     * @param pixelScale relative size of image compared to the original canvas (it is unnecessary because the scale of image does not matter)
     * @return return an image that will be used to redraw the scene
     */
    static WritableImage pixelScaleAwareCanvasSnapshot(Canvas canvas, double pixelScale) {
        WritableImage writableImage = new WritableImage((int)Math.rint(pixelScale*canvas.getWidth()), (int)Math.rint(pixelScale*canvas.getHeight()));
        SnapshotParameters spa = new SnapshotParameters();
        spa.setTransform(Transform.scale(pixelScale, pixelScale));
        return canvas.snapshot(spa, writableImage);
    }
    void CreateShipSceneRedraw(Image premade)
    {
        gc.drawImage(premade,0,0);


        if(Enemyship!=null)
        {
            Drawcrew(shipboundaries[0]+shift,shipboundaries[1],ctrl.myship.shipdata.staff);
            Drawcrew(roomposxy[2], (shipboundaries[1] + shipboundaries[2]) / (3),Enemyship.shipdata.staff);
        }
        else
            {
                Drawcrew(roomposxy[3]+roomposxy[0],shipboundaries[1],ctrl.myship.shipdata.staff);
            }

    }

    /**
     *
     * @param my my spaceship
     * @param other enemy spaceship
     * @return return s canvas tthat will be rarely changed and always re-rendered
     */
    Canvas CreateShipScenePremade(Spaceship my,Spaceship other)
    {
        Canvas canvas=new Canvas(Screen.getMainScreen().getWidth(),Screen.getMainScreen().getHeight());
        GraphicsContext gc=canvas.getGraphicsContext2D();


        String shippath = "file:src/main/resources/Sprites/Ships/";
        Logger.info("Currently used ship's imagepath"+my.imagepath);

        if(Enemyship!=null)
        {
        gc.strokeLine(canvas.getWidth()/2+shift,0,canvas.getWidth()/2+shift,canvas.getHeight());
        //my ship
        gc.drawImage(new Image(my.imagepath+".png"),shipboundaries[0]+shift,shipboundaries[1],shipboundaries[2],shipboundaries[2]);
        DrawRooms(roomposxy[0],roomposxy[1], my.rooms,gc);
        //enemy ship
        gc.drawImage(new Image(other.imagepath+".png"), canvas.getWidth() - shipboundaries[2] + shift / 4, shipboundaries[1], shipboundaries[2], shipboundaries[2]);
        DrawRooms(roomposxy[2], (shipboundaries[1] + shipboundaries[2]) / (3), other.rooms, gc);
        }
        else
            {
                //my ship
                gc.drawImage(new Image(my.imagepath+".png"),shipboundaries[0]+shift*3,shipboundaries[1],shipboundaries[2],shipboundaries[2]);
                DrawRooms(roomposxy[3],roomposxy[1], my.rooms,gc);
            }

        return canvas;
    }
//endregion
//region helper_methods
    private void SetUpEnemyShip()
    {
        Race[] es=Enemyship.shipdata.staff;
        for(int i=0;i<es.length;i++)
        {
            es[i].Onmyship=false;
        }
    }

    /**
     *
     * @param a a choosen Vector2D
     * @param b another choosen Vector2D
     * @return distance between a and b
     */
    double Distance(Vector2D a,Vector2D b) { return (a.x-b.x)*(a.x-b.x)+(a.y-b.y)*(a.y-b.y); }

    /**
     *
     * @param min lowest value we want the double to be
     * @param max highest value we want the double to be
     * @return return a random double value between min and max
     */
    double Randomrange(double min,double max)
    {
        if(min >=max){ Logger.warn("'max' must be higher than 'min'");return 0;}
        Random r=new Random();
        return min+(max-min)*r.nextDouble();
    }

    /**
     *
     * @param v2d list of possible positions
     * @return a random Vector2D from v2d
     */
    Vector2D Randomroom(List<Vector2D> v2d)
    {return v2d.get((int)Randomrange(0,v2d.size()-1));}
//endregion
//region AI
    void Enemyturn(Spaceship enemy,Spaceship my,int currentenemy)
    {
        Race[] enemycrew=enemy.shipdata.staff;
        Race[] mycrew=my.shipdata.staff;
        double enemy_relativehealth=((double)enemy.shipdata.currenthealth)/((double) enemy.shipdata.maxhealth);
        double my_relativehealth=((double)my.shipdata.currenthealth)/((double) my.shipdata.maxhealth);
            if (!enemycrew[currentenemy].Moved&&!InputDisabled)
            {
                enemycrew[currentenemy].Moved=true;

                if (enemycrew.length < mycrew.length / 2 && enemy_relativehealth < my_relativehealth) {
                    if (!Arrays.stream(enemycrew).anyMatch(e -> e.Onmyship==false)) {
                        AI_teleportback(enemycrew[currentenemy]);
                    } else {
                        if(!Is_anybodyonVector2D(X_room_controlroomtile(enemy, Room.Roomtype.Piloting),false))
                        { AI_gotopiloting(Enemyship,enemycrew[currentenemy]);}
                        else if(Vector2D.Equal(X_room_controlroomtile(enemy, Room.Roomtype.Piloting),enemycrew[currentenemy].position))
                        {enemycrew[currentenemy].overwatch=true;}
                    }
                } else {
                    if (enemy_relativehealth >= 0.4d) {
                        if (enemycrew[currentenemy].Onmyship) {
                            AI_gotoattack(my,enemycrew[currentenemy]);
                        }
                        else {
                            if(X_room_exists(Enemyship.rooms, Room.Roomtype.Teleporter)&&X_room_havefreespace(Enemyship, Room.Roomtype.Teleporter)) {
                                if(!Vector2D.Contains(Room.PossiblePositions(Arrays.stream(Enemyship.rooms).filter(e->e.roomtype== Room.Roomtype.Teleporter).findFirst().get())
                                        ,enemycrew[currentenemy].position))
                                {AI_gototeleporter(Enemyship,enemycrew[currentenemy]);}
                                else
                                    {
                                        List<Vector2D> v2d=Room.AllpossiblePositions(ctrl.myship.rooms);
                                        v2d=v2d.stream().filter(e->
                                                        !Vector2D.Contains(Arrays.stream(ctrl.myship.shipdata.staff).filter(f->f.Onmyship).map(f->f.position).collect(Collectors.toList()),e))
                                                                .filter(e->
                                                                !Vector2D.Contains(Arrays.stream(Enemyship.shipdata.staff).filter(f->f.Onmyship).map(f->f.position).collect(Collectors.toList()),e)).collect(Collectors.toList());

                                        AI_teleportin(Enemyship,enemycrew[currentenemy],Randomroom(v2d));
                                    }
                            }
                            else{
                                if(Nextroomtype(enemy)!= Room.Roomtype.None)
                                {
                                    AI_goto(enemy,enemycrew[currentenemy],X_room_controlroomtile(enemy,Nextroomtype(enemy)));
                                }
                                else{ enemycrew[currentenemy].overwatch=true;}
                            }
                        }
                    }
                    else if (enemy_relativehealth < 0.4d) {
                        if (enemycrew[currentenemy].Onmyship) {
                            AI_teleportback(enemycrew[currentenemy]);
                        }
                        else {
                            AI_simplyrunaway(enemycrew[currentenemy]);
                        }
                    }
                }
        }


    }

    /**
     *
     * @param ship choosed spaceship
     * @return checks all the possible room types and return the most important one
     */
    Room.Roomtype Nextroomtype(Spaceship ship)
    {
        if(X_room_exists(ship.rooms, Room.Roomtype.Piloting)&&X_room_havefreespace(ship, Room.Roomtype.Piloting)){return Room.Roomtype.Piloting;}
        else if(X_room_exists(ship.rooms, Room.Roomtype.Engine)&&X_room_havefreespace(ship, Room.Roomtype.Engine)){return Room.Roomtype.Engine;}
        else if(X_room_exists(ship.rooms, Room.Roomtype.Weaponsystem)&&X_room_havefreespace(ship, Room.Roomtype.Weaponsystem)){return Room.Roomtype.Weaponsystem;}
        else if(X_room_exists(ship.rooms, Room.Roomtype.Shield)&&X_room_havefreespace(ship, Room.Roomtype.Shield)){return Room.Roomtype.Shield;}
        else if(X_room_exists(ship.rooms, Room.Roomtype.Teleporter)&&X_room_havefreespace(ship, Room.Roomtype.Teleporter)){return Room.Roomtype.Teleporter;}
        else if(X_room_exists(ship.rooms, Room.Roomtype.Medibay)&&X_room_havefreespace(ship, Room.Roomtype.Medibay)){return Room.Roomtype.Medibay;}
        else if(X_room_exists(ship.rooms, Room.Roomtype.Storage)&&X_room_havefreespace(ship, Room.Roomtype.Storage)){return Room.Roomtype.Storage;}
        else if(X_room_exists(ship.rooms, Room.Roomtype.Doorsystem)&&X_room_havefreespace(ship, Room.Roomtype.Doorsystem)){return Room.Roomtype.Doorsystem;}
        else {return Room.Roomtype.None;}
    }

    /**
     *
     * @param rooms an array of rooms
     * @param rt room type we want to check
     * @return return true if rt type of room exists in rooms
     */
    boolean X_room_exists(Room[] rooms, Room.Roomtype rt)
    { return Arrays.stream(rooms).anyMatch(e->e.roomtype==rt); }

    /**
     *
     * @param ship choosed spaceship
     * @param rt room type we want to check
     * @return return true if rt room type have 1 or more free spaces
     */
    boolean X_room_havefreespace(Spaceship ship,Room.Roomtype rt)
    { if(X_room_freespaces(ship, rt).isEmpty()){return false;} return true;}
    List<Vector2D> X_room_freespaces(Spaceship ship,Room.Roomtype rt)
    {
        List<Vector2D> v2d=Room.PossiblePositions(Arrays.stream(ship.rooms).filter(e->e.roomtype==rt).findFirst().get());
        for(int i=0;i<v2d.size()-1;i++)
        {
            if(Is_anybodyonVector2D(v2d.get(i),false)){v2d.remove(i);i--;}
        }
        return v2d;
    }

    /**
     *
     * @param v2d Vector2D we want to chack
     * @param onmyship boolean that ditermanes the ship to check out
     * @return return true if there is no character standing on v2d
     */
    boolean Is_anybodyonVector2D(Vector2D v2d,boolean onmyship)
    {
        if(Arrays.stream(ctrl.myship.shipdata.staff).anyMatch(e->e.Onmyship==onmyship&&e.position==v2d)&&
                Arrays.stream(Enemyship.shipdata.staff).anyMatch(e->e.Onmyship==onmyship&&e.position==v2d))
        {return true;}
        return false;
    }
    /*
    boolean Enemy_on_same_ship(Race me,Race[] enemy)
    {
        return Arrays.stream(enemy).anyMatch(e->e.Onmyship==me.Onmyship);
    }*/

    /**
     *
     * @param ship spaceship we want to check
     * @param rt room type we want to check
     * @return returns the Vector2D position of the control room of rt room type on ship
     */
    Vector2D X_room_controlroomtile(Spaceship ship,Room.Roomtype rt)
    {
        Vector2D v2d=Arrays.stream(ship.rooms).filter(f->f.roomtype==rt).findFirst().map(f->f.Controlltileinroomsize).get();
        List<Vector2D> v2d2= X_room_freespaces(ship,rt);
        return v2d2.stream().filter(e->Vector2D.Equal(e,new Vector2D(v2d2.get(0).x+v2d.x,v2d2.get(0).y+v2d.y))).findFirst().get();
    }

    void AI_gototeleporter(Spaceship ship,Race r)
    {
        AI_goto(ship,r,X_room_freespaces(ship, Room.Roomtype.Teleporter).get(0));
    }
    void AI_gotopiloting(Spaceship ship,Race r)
    {
        AI_goto(ship,r,X_room_controlroomtile(ship, Room.Roomtype.Piloting));
    }

    /**
     *
     * @return return the index of next enemy which have not moved yet
     */
    int Getnextenemy()
    {
        for(int i=0;i<Enemyship.shipdata.staff.length;i++)
        {
            if(!Enemyship.shipdata.staff[i].Moved){return i;}
        }
        return 0;
    }
    void AI_teleportback(Race r)
    {

    }
    void AI_gotohealing(Race r)
    {

    }
    void AI_gotoattack(Spaceship ship,Race r)
    {
        List<Vector2D> v2d=Moves(r,r.speed,Room.AllpossiblePositions(ship.rooms),false);
        List<Vector2D> closebyenemies=Arrays.stream(ctrl.myship.shipdata.staff).filter(e->e.Onmyship==r.Onmyship)
                .filter(e->Vector2D.Contains(v2d,e.position)).map(e->e.position).collect(Collectors.toList());
        //get to the closest enemy
        if(closebyenemies.size()==0)
        {
            AI_goto(ship,r,Arrays.stream(ctrl.myship.shipdata.staff)
                    .filter(e->e.Onmyship==r.Onmyship)
                    .min(Comparator.comparing(e->Distance(e.position,r.position))).get().position);
        }
        //attack enemy
        else
            {
                ProjectileAnimator(r, Arrays.stream(ctrl.myship.shipdata.staff)
                        .filter(e->e.Onmyship==r.Onmyship&&Vector2D.Equal(closebyenemies.get(0),e.position)).findFirst().get(),
                        (Pane)GameScreen.getChildren().get(1));
            }
    }
    void AI_simplyrunaway(Race r)
    {

    }
    void AI_goto(Spaceship ship,Race r,Vector2D goal)
    {


        List<Vector2D> v2d=Moves(r,r.speed,Room.AllpossiblePositions(ship.rooms),false);
        Vector2D closest=v2d.stream().filter(e->!Vector2D.Contains(Arrays.stream(ship.shipdata.staff).map(f->f.position).collect(Collectors.toList()),e)).min(Comparator.comparing(e->Distance(e,goal))).get();
        Logger.info("goal:"+goal.x+" "+goal.y+" "+closest.x+" "+closest.y);
        MoveAnimator(v2dpath(r.position,closest,v2d,true),r,false);
    }
    void AI_teleportin(Spaceship ship,Race r,Vector2D goal)
    {
        ArrayList<Vector2D> v2d=new ArrayList<>();
        Logger.info("Enemy "+r.name+" teleported into our ship!");
        v2d.add(r.position);
        v2d.add(goal);
        TeleportAnimator(v2d,r,false);
    }
//endregion

}
