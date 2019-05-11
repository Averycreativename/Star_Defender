package stardef.Baseclasses;

import java.util.List;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name="Empire")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Vector2D.class})
/**
 * This class used for storing a javafx color, a name for the empire, and a list of Vector2D.
 */
public class Empire
{
    public CustomColor colorofempire;//the color which identifies the empire
    public String nameofempire;
    public List terrorityofempire;

    /**
     * This constructor only contains a color and a name, the terrority list is automaticly created.
     * @param col color of empire
     * @param name name of empire
     */
   public Empire(CustomColor col, String name)
   {
    colorofempire=col;
    nameofempire=name;
    terrorityofempire=new ArrayList<Vector2D>();
   }

    /**
     * This constructor have no arguments.
     */
   public Empire()
   {}
}