package stardef.Baseclasses;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void possiblePositions() {
        Room r=new Room(new Vector2D(),new Vector2D(1,1));
        List<Vector2D> l= Room.PossiblePositions(r);
        assertEquals(1,l.size());
    }

    @Test
    void allpossiblePositions() {
        Room[] rooms =new Room[]
                {
                new Room(new Vector2D(1,1),new Vector2D(1,1)),
                new Room(new Vector2D(),new Vector2D(1,1))
                };
        List<Vector2D> l= Room.AllpossiblePositions(rooms);
        assertEquals(2,l.size());
    }
}