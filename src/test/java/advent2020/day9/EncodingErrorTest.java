package advent2020.day9;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
class EncodingErrorTest {

    @Test
    void validate() {

        Queue queue = new PriorityQueue(Arrays.asList(35L, 20L, 15L, 25L, 47L));

        Assertions.assertTrue(EncodingError.validate(queue, 40L));
    }

    @Test
    void iterateUntilNotValidated() {
        List list = Arrays.asList(35L, 20L, 15L, 25L, 47L, 40L, 62L, 55L, 65L, 95L, 102L, 117L, 150L, 182L, 127L, 219L, 299L, 277L, 309L, 576L);
        Assertions.assertEquals(127L, EncodingError.iterateUntilNotValidated(list, 5));
    }

    @Test
    void part1(){
        List<Long> numbers = readLines("src/test/resources/advent2020/day9/input").stream().map(Long::valueOf).collect(Collectors.toList());
        Long answer = EncodingError.iterateUntilNotValidated(numbers, 25);
        System.out.println("Part 1: "+answer);
    }


    @Test
    void sumOfSmallAndLargeInContiguousSet() {
        List list = Arrays.asList(35L, 20L, 15L, 25L, 47L, 40L, 62L, 55L, 65L, 95L, 102L, 117L, 150L, 182L, 127L, 219L, 299L, 277L, 309L, 576L);
        Assertions.assertEquals(62, EncodingError.sumOfSmallAndLargeInContiguousSet(list, 5));
    }

    @Test
    void part2(){
        List<Long> numbers = readLines("src/test/resources/advent2020/day9/input").stream().map(Long::valueOf).collect(Collectors.toList());
        Long answer = EncodingError.sumOfSmallAndLargeInContiguousSet(numbers, 25);
        System.out.println("Part 2: "+answer);
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