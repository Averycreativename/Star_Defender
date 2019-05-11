package stardef.Baseclasses;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name="Race")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Vector2D.class})
/**
 * This class represents a crew member.
 */
public class Race
{
    public String racename;//the racename is also the url
    public String name;
    public int currenthealth=100;
    public int maxhealth=100;
    public int attack=10;
    public int repair=5;
    public Vector2D position=new Vector2D();
    public boolean Onmyship=true;
    public int speed=5;
    public boolean Moved=false;
    public boolean overwatch=false;

    /**
     * The constructor i meant to use when starting a new game.
     * @param racename filename of character sprite
     * @param name name of this character
     * @param position position of this character
     */
    public Race(String racename,String name,Vector2D position)
    {
        this.racename=racename;
        this.name=name;
        this.position=position;
    }

    /**
     * Contains every attribute of the character.
     * @param racename filename of character sprite
     * @param name name of this character
     * @param maxhealth maximum health of character
     * @param attack attack of character
     * @param repair repair speed of character
     */
    public Race(String racename,String name,int maxhealth,int attack,int repair)
    {
        this.racename=racename;
        this.name=name;
        this.maxhealth=maxhealth;
        this.attack=attack;
        this.repair=repair;
    }

    /**
     * empty constructor.
     */
    public Race(){}

}