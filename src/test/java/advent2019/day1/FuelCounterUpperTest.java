package advent2019.day1;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class FuelCounterUpperTest {


    private static Stream<Arguments> provideForCalculateFuelForModule() {
        return Stream.of(
                        Arguments.of(13,2),
                        Arguments.of(14,2),
                        Arguments.of(1969,654),
                        Arguments.of(100756,33583),
                        Arguments.of(3345909,1115301)
        );
    }

    @ParameterizedTest
    @MethodSource("provideForCalculateFuelForModule")
    public void calculateFuelForModule(int mass, long expected) {

        long actual = new FuelCounterUpper().calculateFuelForModule(mass);
        Assertions.assertEquals(expected, actual);
    }


    @ParameterizedTest
    @ValueSource(strings = {"src/test/resources/day1/testInput"})
    public void calculateForInputTest(String filename) {
        long expected = 34241;

        List<Integer> lines = readLines(filename);
        long actual = new FuelCounterUpper().calculateForInput(lines);
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"src/test/resources/day1/input"})
    public void calculateForInputExercise(String filename) {
        List<Integer> lines = readLines(filename);
        System.out.println(new FuelCounterUpper().calculateForInput(lines));
    }

    private static Stream<Arguments> provideForCalculateForInputInclFuel() {
        return Stream.of(
                        Arguments.of(13,2),
                        Arguments.of(14,2),
                        Arguments.of(1969,966),
                        Arguments.of(100756,50346)
        );
    }

    @ParameterizedTest
    @MethodSource("provideForCalculateForInputInclFuel")
    public void calculateForInputInclFuel(Integer input, long expected) {

        long actual = new FuelCounterUpper().calculateFuelForInputInclFuel(Collections.singletonList(input));
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"src/test/resources/day1/input"})
    public void calculateForInputInclFuelExercise(String filename) {
        List<Integer> lines = readLines(filename);
        System.out.println(new FuelCounterUpper().calculateFuelForInputInclFuel(lines));

    }

    /* *************************************** */




    protected List<Integer> readLines(String filename){
        List<Integer> lines = Collections.emptyList();
        try
        {
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            sc.useDelimiter("\\Z");

            lines = Arrays.stream(sc.next().split("\\n")).map(Integer::valueOf).collect(Collectors.toList());

        }
        catch (IOException e)
        {
            // do something
            e.printStackTrace();
        }
        return lines;
    }
}