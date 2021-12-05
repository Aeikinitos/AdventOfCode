package advent2021.day5;

import common.InputParsers;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
class HydrothermalVentureTest {

    @Test
    void processSamplePart2() {
        long actual = new HydrothermalVenture().process(getSample());
        Assertions.assertEquals(12, actual);
    }

    @Test
    void processPart2() {
        List<String> input = InputParsers.readStringLines("src/test/resources/advent2021/day5/input");
        long actual = new HydrothermalVenture().process(input);
        Assertions.assertEquals(6189, actual);
    }

    private List<String> getSample(){
        return Arrays.asList(
                        "0,9 -> 5,9",
                        "8,0 -> 0,8",
                        "9,4 -> 3,4",
                        "2,2 -> 2,1",
                        "7,0 -> 7,4",
                        "6,4 -> 2,0",
                        "0,9 -> 2,9",
                        "3,4 -> 1,4",
                        "0,0 -> 8,8",
                        "5,5 -> 8,2"
        );
    }


}