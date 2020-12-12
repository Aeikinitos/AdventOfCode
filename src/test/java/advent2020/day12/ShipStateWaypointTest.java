package advent2020.day12;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
class ShipStateWaypointTest {

    @Test
    void applyInstruction() {
        NavigationInstruction navigationInstruction = new NavigationInstruction('R', 90);

        ShipStateWaypoint shipStateWaypoint = new ShipStateWaypoint(4, 10);
        shipStateWaypoint.applyInstruction(navigationInstruction);

        Assertions.assertEquals(4, shipStateWaypoint.waypointE);
        Assertions.assertEquals(-10, shipStateWaypoint.waypointN);
    }

    @Test
    void applyInstruction77() {
        NavigationInstruction navigationInstruction = new NavigationInstruction('R', 90);

        ShipStateWaypoint shipStateWaypoint = new ShipStateWaypoint(7, 7);
        shipStateWaypoint.applyInstruction(navigationInstruction);

        Assertions.assertEquals(7, shipStateWaypoint.waypointE);
        Assertions.assertEquals(-7, shipStateWaypoint.waypointN);
    }
}