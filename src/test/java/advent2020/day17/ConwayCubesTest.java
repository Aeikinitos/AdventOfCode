package advent2020.day17;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
class ConwayCubesTest {

    @Test
    void nextCycle() {
        List<String> input = Arrays.asList(
                        ".#.",
                        "..#",
                        "###"
        );

        ConwayCubes conwayCubes = new ConwayCubes(input);

        for (int i = 0; i < 6; i++) {
            conwayCubes.nextCycle();
        }

        int actual = conwayCubes.getActiveCoordinates().size();
        Assertions.assertEquals(112, actual);
    }

    @Test
    void part1(){
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

        ConwayCubes conwayCubes = new ConwayCubes(input);

        for (int i = 0; i < 6; i++) {
            conwayCubes.nextCycle();
        }

        int actual = conwayCubes.getActiveCoordinates().size();

        System.out.println("Part1 :"+actual);
    }
}