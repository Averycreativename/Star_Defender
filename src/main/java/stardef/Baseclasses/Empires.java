package stardef.Baseclasses;

import java.util.List;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


@XmlRootElement(name="Empires")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Empire.class})
/**
 * The Empire class only contains a list of empire objects
 */
public class Empires
{
    @XmlElement
    private List empires;

    /**
     * This constructor has no parameters, it creates a new Empire list.
     */
    public Empires() {empires=new ArrayList<Empire>();}

    /**
     * Constructor with 1 parameter.
     * @param empires only contains a list of empire objects
     */
    public Empires(List<Empire> empires){this.empires=empires;}

    /**
     * Getter for list of empires.
     * @return get the list from empires
     */
    public List<Empire> getempires(){return empires;}

    /**
     * Add a new object to the list of empires.
     * @param e empire to add to empires
     */
    public void add(Empire e){empires.add(e);}

}