package advent2021.day1;

import common.InputParsers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
class SonarSweepTest {

    @Test
    void calculateIncrements() {

        List<Long> input = getSampleInput();
        long actual = new SonarSweep().calculateIncrementsSlidingWindow(input, 1);
        Assertions.assertEquals(7, actual);
    }

    @Test
    void calculateIncrementsWithInput() {

        List<Long> input = InputParsers.readLines("src/test/resources/advent2021/day1/input");
        long actual = new SonarSweep().calculateIncrementsSlidingWindow(input, 1);
        Assertions.assertEquals(1393, actual);
    }

    @Test
    void calculateIncrementsSlidingWindow() {

        List<Long> input = getSampleInput();
        long actual = new SonarSweep().calculateIncrementsSlidingWindow(input, 3);
        Assertions.assertEquals(5, actual);
    }

    @Test
    void calculateIncrementsSlidingWindowWithInput() {

        List<Long> input = InputParsers.readLines("src/test/resources/advent2021/day1/input");
        long actual = new SonarSweep().calculateIncrementsSlidingWindow(input, 3);
        Assertions.assertEquals(1359, actual);
    }

    private List<Long> getSampleInput(){
        return  Arrays.asList(199L,
                        200L,
                        208L,
                        210L,
                        200L,
                        207L,
                        240L,
                        269L,
                        260L,
                        263L);
    }
}