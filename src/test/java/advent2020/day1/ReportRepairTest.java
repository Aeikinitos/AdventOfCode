package advent2020.day1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import org.junit.jupiter.params.provider.MethodSource;

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
public class ReportRepairTest {

    @Test
    public void findValuesThatSumTo() {
        Long sum = 2020L;
        List<Long> values = Arrays.asList(1721L,979L,366L,299L,675L,1456L);
        Long expected = 514579L;

        List<Long> valuesThatSumTo = new ReportRepair().findValuesThatSumTo(sum, values);
        Assertions.assertTrue(valuesThatSumTo.size() == 2);

        Long actual = valuesThatSumTo.get(0) * valuesThatSumTo.get(1);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void find3ValuesThatSumTo() {
        Long sum = 2020L;
        List<Long> values = Arrays.asList(1721L,979L,366L,299L,675L,1456L);
        Long expected = 241861950L;

        List<Long> valuesThatSumTo = new ReportRepair().find3ValuesThatSumTo(sum, values);
        Assertions.assertTrue(valuesThatSumTo.size() == 3);

        Long actual = valuesThatSumTo.get(0) * valuesThatSumTo.get(1) * valuesThatSumTo.get(2);
        Assertions.assertEquals(expected, actual);
    }

    /* Exercise */

    @Test
    public void findValuesThatSumTo_Exercise() {
        Long sum = 2020L;
        List<Long> values = readLines("src/test/resources/advent2020/day1/input");

        List<Long> valuesThatSumTo = new ReportRepair().findValuesThatSumTo(sum, values);
        Assertions.assertTrue(valuesThatSumTo.size() == 2);

        Long actual = valuesThatSumTo.get(0) * valuesThatSumTo.get(1);

        System.out.println(actual);
    }

    @Test
    public void find3ValuesThatSumTo_Exercise() {
        Long sum = 2020L;
        List<Long> values = readLines("src/test/resources/advent2020/day1/input");

        List<Long> valuesThatSumTo = new ReportRepair().find3ValuesThatSumTo(sum, values);
        Assertions.assertTrue(valuesThatSumTo.size() == 3);

        Long actual = valuesThatSumTo.get(0) * valuesThatSumTo.get(1) * valuesThatSumTo.get(2);

        System.out.println(actual);
    }

    /* **************************************************** */

    protected static List<Long> readLines(String filename){
        List<Long> lines = Collections.emptyList();
        try
        {
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            sc.useDelimiter("\\Z");

            lines = Arrays.stream(sc.next().split("\\n")).map(Long::valueOf).collect(Collectors.toList());

        }
        catch (IOException e)
        {
            // do something
            e.printStackTrace();
        }
        return lines;
    }
}
