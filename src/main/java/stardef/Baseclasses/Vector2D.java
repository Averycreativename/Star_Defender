package stardef.Baseclasses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.List;

@XmlRootElement(name="Vector2D")
/**
 * Represents positions in 2D space.
 */
public class Vector2D
{
    public int x;
    public int y;
    public Vector2D(int x,int y)
    {
        this.x=x;
        this.y=y;
    }

    /**
     * Empty constructor.
     */
    public Vector2D()
    {
        x=0;
        y=0;
    }

    /**
     * Compare 2 Vector2D and return true if their values are equal.
     * @param a Vector2D object
     * @param b Vector2D object
     * @return return true if the x, and y positions are equal
     */
    public static boolean Equal(Vector2D a,Vector2D b)
    {
        if(a.x==b.x&&a.y==b.y){return true;}
        return false;
    }

    /**
     * remove all repeating elements
     * @param v2d a list of Vector2D's
     */
    public static void Distinct(List<Vector2D> v2d)
    {
        for(int i=0;i<v2d.size();i++)
        {
            if(Contains(v2d,v2d.get(i),i)){v2d.remove(i);i--;}
        }
    }

    /**
     *
     * @param list a list of Vector2D
     * @param v2d a single Vector2D
     * @return return true if list contains v2d
     */
    public static boolean Contains(List<Vector2D> list,Vector2D v2d)
    {
        for(int i=0;i<list.size();i++)
        {
            if(Vector2D.Equal(v2d,list.get(i))){return true;}
        }
        return false;
    };

    /**
     *
     * @param list a list of Vector2D
     * @param v2d a single Vector2D
     * @param but_not_nth_value an int
     * @return return true if list contains v2d and the index of v2d in the list is not but_not_nth_value
     */
    public static boolean Contains(List<Vector2D> list,Vector2D v2d,int but_not_nth_value)
    {
        for(int i=0;i<list.size();i++)
        {
            if(Vector2D.Equal(v2d,list.get(i))&&i!=but_not_nth_value){return true;}
        }
        return false;
    };
}
