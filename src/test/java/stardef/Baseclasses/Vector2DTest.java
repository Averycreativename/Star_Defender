package stardef.Baseclasses;

import clojure.core.Vec;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Vector2DTest {

    @Test
    void equal() {
        Vector2D a=new Vector2D(3,3);
        Vector2D b=new Vector2D(3,3);
        boolean c=Vector2D.Equal(a,b);
        assertEquals(true,c);
    }

    @Test
    void distinct() {
        List<Vector2D> a=new ArrayList<Vector2D>();
        a.add(new Vector2D());
        a.add(new Vector2D(1,1));
        a.add(new Vector2D(2,2));
        a.add(new Vector2D(2,2));
        a.add(new Vector2D(2,2));
        a.add(new Vector2D(2,2));
        Vector2D.Distinct(a);
        assertEquals(3,a.size());
    }

    @Test
    void contains() {
        List<Vector2D> list=new ArrayList<Vector2D>();
        list.add(new Vector2D());
        list.add(new Vector2D(1,0));
        list.add(new Vector2D(0,1));
        list.add(new Vector2D(1,1));
        Vector2D a=new Vector2D(0,1);
        boolean result=Vector2D.Contains(list,a);
        assertEquals(true,result);
    }

    @Test
    void contains1() {
        List<Vector2D> list=new ArrayList<Vector2D>();
        list.add(new Vector2D(1,0));
        list.add(new Vector2D(0,1));
        list.add(new Vector2D(1,1));
        Vector2D a=new Vector2D(0,1);
        int n=1;
        boolean result=Vector2D.Contains(list,a,n);
        assertEquals(false,result);
    }
}