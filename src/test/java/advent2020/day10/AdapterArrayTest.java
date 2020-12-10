package advent2020.day10;

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
class AdapterArrayTest {

    @Test
    void differences() {
        List<Integer> input = Arrays.asList(1, 4, 5, 6, 7, 10, 11, 12, 15, 16, 19);
        List<Integer> expected = Arrays.asList(1, 3, 1, 1, 1, 3, 1, 1, 3, 1, 3, 3);
        List<Integer> actual = AdapterArray.differences(input);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void product() {
        List<Integer> input = Arrays.asList(1, 4, 5, 6, 7, 10, 11, 12, 15, 16, 19);
        List<Integer> differences = AdapterArray.differences(input);
        long product = AdapterArray.product(differences);
        Assertions.assertEquals(35, product);
    }

    @Test
    void combination() {
        List<Integer> input = Arrays.asList(1, 4, 5, 6, 7, 10, 11, 12, 15, 16, 19);
        List<Integer> differences = AdapterArray.differences(input);
        long product = AdapterArray.combination(differences);
        Assertions.assertEquals(8, product);
    }

    @Test
    void part1() {
        List<Integer> input = readLines("src/test/resources/advent2020/day10/input").stream().map(Integer::valueOf).collect(Collectors.toList());
        List<Integer> differences = AdapterArray.differences(input);
        long product = AdapterArray.product(differences);
        System.out.println("Part1 = " + product);
    }

    @Test
    void part2() {
        List<Integer> input = readLines("src/test/resources/advent2020/day10/input").stream().map(Integer::valueOf).collect(Collectors.toList());
        List<Integer> differences = AdapterArray.differences(input);
        long product = AdapterArray.combination(differences);
        System.out.println("Part2 = " + product);
    }

    /* *************************************** */

    protected List<String> readLines(String filename){
        List<String> lines = Collections.emptyList();
        try
        {
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            sc.useDelimiter("\\Z");

            lines = Arrays.stream(sc.next().split("\\n")).collect(Collectors.toList());

        }
        catch (IOException e)
        {
            // do something
            e.printStackTrace();
        }
        return lines;
    }
}