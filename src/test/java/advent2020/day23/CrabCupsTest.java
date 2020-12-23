package advent2020.day23;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
class CrabCupsTest {

    @Test
    void playCupsSample() {
        CrabCups crabCups = new CrabCups(Arrays.asList(3, 8, 9, 1, 2, 5, 4, 6, 7));
        Long actual = crabCups.playCups();
        Assertions.assertEquals(149245887792L,actual);
    }

    @Test
    void playCupsPart2() {
        CrabCups crabCups = new CrabCups(Arrays.asList(3, 9,4,6,1,8,5,2,7));
        Long result = crabCups.playCups();
        System.out.println("Part2: "+result);
    }
}