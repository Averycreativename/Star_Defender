package stardef.Baseclasses;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement(name="Room")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Vector2D.class})
/**
 * This class represents a room.
 */
public class Room
{
    public Vector2D roomsize;
    public Vector2D position;
    public Vector2D Controlltileinroomsize;
    public int maxsystemenergy=1;
    public int currentsystemenergy=0;
    public enum Roomtype
    {
        None,
        Storage,
        Medibay,
        Engine,
        Piloting,
        Doorsystem,
        Shield,
        Weaponsystem,
        Teleporter,

    }
    public Roomtype roomtype;

    /**
     * Constructor for rooms where the roomtype is None.
     * @param position position of the room
     * @param roomsize size of the room
     */
    public Room(Vector2D position,Vector2D roomsize)
    {this.roomsize=roomsize; this.position=position; this.roomtype=Roomtype.None; this.Controlltileinroomsize=null; this.maxsystemenergy=0; }

    /**
     * Contructor for rooms where the roomtype is not None, but we did not wanted to specify where the controltile of the room is.
     * @param position position of the room
     * @param roomsize size of the room
     * @param roomtype type of room
     */
    public Room(Vector2D position,Vector2D roomsize,Roomtype roomtype)
    {this.roomsize=roomsize; this.position=position; this.roomtype=roomtype;
    if(roomtype!=Roomtype.None) {this.Controlltileinroomsize=new Vector2D(0,0); }
    else{this.Controlltileinroomsize=null;}
    }

    /**
     * Constructor where we wanted to specify the controltile, and max energy.
     * @param position position of the room
     * @param roomsize size of the room
     * @param roomtype type of room
     * @param Controlltileinroomsize position of the controltile relative to position
     * @param maxsysremenergy maximum energy of this room
     */
    public Room(Vector2D position,Vector2D roomsize,Roomtype roomtype, Vector2D Controlltileinroomsize,int maxsysremenergy)
    {this.roomsize=roomsize; this.position=position; this.roomtype=roomtype; this.Controlltileinroomsize=Controlltileinroomsize; this.maxsystemenergy=maxsysremenergy; }

    /**
     * Empty constructor.
     */
    public Room(){}

    /**
     * A method specify what positions are valid.
     * @param room room of choice
     * @return returns all the positions that are valid
     */
    public static List<Vector2D> PossiblePositions(Room room)
    {
        List<Vector2D> v2dlist=new ArrayList<Vector2D>();
        for (int y=room.position.y;y<room.position.y+room.roomsize.y;y++)
        {
            for (int x=room.position.x;x<room.position.x+room.roomsize.x;x++)
            {
                v2dlist.add(new Vector2D(x,y));
            }
        }
        return  v2dlist;
    }

    /**
     *  A method specify what positions are valid.
     * @param rooms array of rooms, the rooms variable of spaceship
     * @return return all the positions that are valid in all the rooms
     */
    public static List<Vector2D> AllpossiblePositions(Room[] rooms) {
        List<Vector2D> v2dlist = new ArrayList<Vector2D>();
        for (int i = 0; i < rooms.length; i++)
        {
            v2dlist.addAll(Room.PossiblePositions(rooms[i]));
        }
        return v2dlist.stream().distinct().collect(Collectors.toList());
    }
}
