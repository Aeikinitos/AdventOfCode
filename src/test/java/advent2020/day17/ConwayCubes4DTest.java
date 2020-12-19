package advent2020.day17;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
class ConwayCubes4DTest {

    @Test
    void nextCycle() {
        List<String> input = Arrays.asList(
                        ".#.",
                        "..#",
                        "###"
        );

        ConwayCubes4D conwayCubes = new ConwayCubes4D(input);

        for (int i = 0; i < 6; i++) {
            conwayCubes.nextCycle();
        }

        int actual = conwayCubes.getActiveCoordinates().size();
        Assertions.assertEquals(848, actual);
    }

    @Test
    void part2(){
        List<String> input = Arrays.asList(
                        ".#.#.#..",
                        "..#....#",
                        "#####..#",
                        "#####..#",
                        "#####..#",
                        "###..#.#",
                        "#..##.##",
                        "#.#.####"
        );

        ConwayCubes4D conwayCubes = new ConwayCubes4D(input);

        for (int i = 0; i < 6; i++) {
            conwayCubes.nextCycle();
        }

        int actual = conwayCubes.getActiveCoordinates().size();

        System.out.println("Part1 :"+actual);
    }
}