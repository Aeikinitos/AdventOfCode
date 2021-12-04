package advent2021.day4;

import common.InputParsers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class GiantSquidTest {


    @Test
    void calculateScoreSamplePart1() {
        List<String> input = InputParsers.readStringLines("src/test/resources/advent2021/day4/sampleInput");
        long actual = new GiantSquid().process(input, true);
        Assertions.assertEquals(4512, actual);
    }

    @Test
    void calculateScorePart1() {
        List<String> input = InputParsers.readStringLines("src/test/resources/advent2021/day4/input");
        long actual = new GiantSquid().process(input, true);
        Assertions.assertEquals(46920, actual);
    }

    @Test
    void calculateScoreSamplePart2() {
        List<String> input = InputParsers.readStringLines("src/test/resources/advent2021/day4/sampleInput");
        long actual = new GiantSquid().process(input, false);
        Assertions.assertEquals(1924, actual);
    }

    @Test
    void calculateScorePart2() {
        List<String> input = InputParsers.readStringLines("src/test/resources/advent2021/day4/input");
        long actual = new GiantSquid().process(input, false);
        Assertions.assertEquals(12635, actual);
    }
}