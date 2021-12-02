package advent2021.day2;

import common.InputParsers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class DiveTest {

    @Test
    void calculatePositionProductSamplePart1() {

        long actual = new Dive().calculatePositionProduct(getSample(), new XYCoordinate());
        Assertions.assertEquals(150, actual);

    }

    @Test
    void calculatePositionProductPart1() {
        List<String> input = InputParsers.readStringLines("src/test/resources/advent2021/day2/input");
        long actual = new Dive().calculatePositionProduct(input, new XYCoordinate());
        Assertions.assertEquals(2036120, actual);

    }

    @Test
    void calculatePositionProductSamplePart2() {

        long actual = new Dive().calculatePositionProduct(getSample(), new AimingCoordinate());
        Assertions.assertEquals(900, actual);

    }

    @Test
    void calculatePositionProductPart2() {
        List<String> input = InputParsers.readStringLines("src/test/resources/advent2021/day2/input");
        long actual = new Dive().calculatePositionProduct(input, new AimingCoordinate());
        Assertions.assertEquals(2015547716, actual);

    }

    private List<String> getSample(){
        return Arrays.asList(
                        "forward 5",
                        "down 5",
                        "forward 8",
                        "up 3",
                        "down 8",
                        "forward 2");
    }
}