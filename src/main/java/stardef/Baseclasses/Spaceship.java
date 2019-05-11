package stardef.Baseclasses;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name="Spaceship")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Room.class,Shipdata.class})
/**
 * Represents the spaceship.
 */
public class Spaceship
{
    public String imagepath;
    public String name;
    public String layoutname;
    public Room[] rooms;
    public Shipdata shipdata;

    /**
     * Constructor used to load spaceship.
     * @param imagepath filename of spaceship sprite
     * @param name name of spaceship
     * @param layoutname layout name, filename of xml file
     * @param rooms rooms of spaceship
     * @param shipdata shipdata of
     */
    public Spaceship(String imagepath,String name,String layoutname,Room[] rooms,Shipdata shipdata)
    {
        this.imagepath="file:src/main/resources/Sprites/Ships/"+imagepath; this.name=name;
        this.rooms=rooms; this.shipdata=shipdata; this.layoutname="file:src/main/resources/Xmlresources/"+layoutname;
    }

    /**
     * Empty constructor.
     */
    public Spaceship() {

    }

}

