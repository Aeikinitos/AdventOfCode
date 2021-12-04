package advent2021.day3;

import common.InputParsers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
class BinaryDiagnosticTest {

    @Test
    void calculatePowerConsumptionSamplePart1() {

        long actual = new BinaryDiagnostic().calculatePowerConsumption(getSample(), 5);
        Assertions.assertEquals(198, actual);
    }

    @Test
    void calculatePowerConsumptionPart1() {
        List<String> input = getInput();
        long actual = new BinaryDiagnostic().calculatePowerConsumption(input, input.get(0).length());
        Assertions.assertEquals(4001724, actual);
    }

    @Test
    void getLifeSupportRatingSamplePart2() {
        long actual = new BinaryDiagnostic().getLifeSupportRating(getSample());
        Assertions.assertEquals(230, actual);
    }

    @Test
    void getLifeSupportRatingPart2() {
        long actual = new BinaryDiagnostic().getLifeSupportRating(getInput());
        Assertions.assertEquals(587895, actual);
    }

    private List<String> getInput(){
        return InputParsers.readStringLines("src/test/resources/advent2021/day3/input");
    }

    private List<String> getSample(){
        return Arrays.asList(
                        "00100",
                        "11110",
                        "10110",
                        "10111",
                        "10101",
                        "01111",
                        "00111",
                        "11100",
                        "10000",
                        "11001",
                        "00010",
                        "01010");
    }


}