package stardef.Baseclasses;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name="Shipdata")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Race.class})
/**
 * Represents a state of the ship, that are changed during gameplay
 */
public class Shipdata {

    public int fuel;
    public int rocket;
    public int drone;
    public int currenthealth;
    public int maxhealth;
    public int systemenergy;
    public Race[] staff;

    /**
     * Constructor for ingame state of ship
     * @param fuel amount of fuel left
     * @param rocket number of rockets left
     * @param drone number of drones left
     * @param maxhealth maximum health
     * @param currenthealth current health
     * @param staff array of race
     */
    public  Shipdata(int fuel,int rocket,int drone,int maxhealth,int currenthealth,Race[] staff)
    {
        this.fuel=fuel; this.rocket=rocket; this.drone=drone;
        this.maxhealth=maxhealth; this.currenthealth=currenthealth;
        this.staff=staff;
    }

    /**
     * Constructor for start of gameplay
     * @param fuel amount of fuel
     * @param rocket number of rockets
     * @param drone number of drones left
     * @param maxhealth maximum health and also the current health
     * @param staff array of race
     * @param systemenergy energy of ship
     */
    public  Shipdata(int fuel,int rocket,int drone,int maxhealth,Race[] staff,int systemenergy)
    {
        this.fuel=fuel; this.rocket=rocket; this.drone=drone; this.systemenergy=systemenergy;
        this.maxhealth=maxhealth; this.currenthealth=maxhealth; this.staff=staff;
    }

    /**
     * Empty constructor of Shipdata class
     */
    public  Shipdata()
    {
        this.fuel=0; this.rocket=0; this.drone=0;
        this.maxhealth=1; this.currenthealth=this.maxhealth; this.staff=null;
    }
}
