package advent2020.day3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
class TobogganTrajectoryTest {


    String[] sampleMap = {
                    "..##.......",
                    "#...#...#..",
                    ".#....#..#.",
                    "..#.#...#.#",
                    ".#...##..#.",
                    "..#.##.....",
                    ".#.#.#....#",
                    ".#........#",
                    "#.##...#...",
                    "#...##....#",
                    ".#..#...#.#"};

    @Test
    void countTreesPart1() {
        long actual = new TobogganTrajectory().countTrees(sampleMap,3,1);
        Assertions.assertEquals(7,actual);
    }

    @Test
    void exercisePart1() {
        String[] map = readLines("src/test/resources/advent2020/day3/input");
        long actual = new TobogganTrajectory().countTrees(map,3,1);
        System.out.println(actual);
    }

    @Test
    void countTreesPart2() {

        TobogganTrajectory tobogganTrajectory = new TobogganTrajectory();
        long prod = 1;
        prod  = tobogganTrajectory.countTrees(sampleMap,1,1);
        prod *= tobogganTrajectory.countTrees(sampleMap,3,1);
        prod *= tobogganTrajectory.countTrees(sampleMap,5,1);
        prod *= tobogganTrajectory.countTrees(sampleMap,7,1);
        prod *= tobogganTrajectory.countTrees(sampleMap,1,2);
        Assertions.assertEquals(336, prod);
        System.out.println(prod);
    }

    @Test
    void exercisePart2() {
        String[] map = readLines("src/test/resources/advent2020/day3/input");
        TobogganTrajectory tobogganTrajectory = new TobogganTrajectory();
        long prod = 1;
        prod  = tobogganTrajectory.countTrees(map,1,1);
        prod *= tobogganTrajectory.countTrees(map,3,1);
        prod *= tobogganTrajectory.countTrees(map,5,1);
        prod *= tobogganTrajectory.countTrees(map,7,1);
        prod *= tobogganTrajectory.countTrees(map,1,2);
        System.out.println(prod);
    }


    /* *************************************** */

    protected static String[] readLines(String filename){
        try
        {
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            sc.useDelimiter("\\Z");
            return sc.next().split("\\n");
        }
        catch (IOException e)
        {
            // do something
            e.printStackTrace();
        }
        return new String[]{};
    }
}