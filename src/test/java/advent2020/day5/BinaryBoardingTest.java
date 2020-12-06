package advent2020.day5;

import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
class BinaryBoardingTest {

    @Test
    void getSeatID_FBFBBFFRLR() {
        int actual = BinaryBoarding.getSeatID(BinaryBoarding.getRowSeatPair("FBFBBFFRLR"));
        Assertions.assertEquals(357, actual);
    }

    @Test
    void getSeatID_BFFFBBFRRR() {
        int actual = BinaryBoarding.getSeatID(BinaryBoarding.getRowSeatPair("BFFFBBFRRR"));
        Assertions.assertEquals(567, actual);
    }

    @Test
    void getSeatID_FFFBBBFRRR() {
        int actual = BinaryBoarding.getSeatID(BinaryBoarding.getRowSeatPair("FFFBBBFRRR"));
        Assertions.assertEquals(119, actual);
    }

    @Test
    void getSeatID_BBFFBBFRLL() {
        int actual = BinaryBoarding.getSeatID(BinaryBoarding.getRowSeatPair("BBFFBBFRLL"));
        Assertions.assertEquals(820, actual);
    }

    @Test
    void exercicePart1(){
        List<String> lines = readLines("src/test/resources/advent2020/day5/input");
        System.out.println(lines.stream().map(BinaryBoarding::getRowSeatPair).map(BinaryBoarding::getSeatID).max(Integer::compareTo));
    }

    @Test
    void exercicePart2(){
        List<String> lines = readLines("src/test/resources/advent2020/day5/input");
        BinaryBoarding.findSeatID(lines);

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