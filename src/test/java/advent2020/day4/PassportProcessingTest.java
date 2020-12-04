package advent2020.day4;

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
class PassportProcessingTest {

    @Test
    void testPassport() {
        boolean actual = new PassportProcessing().testPassport("ecl:gry pid:860033327 eyr:2020 hcl:#fffffd byr:1937 iyr:2017 cid:147 hgt:183cm");
        Assertions.assertEquals(true, actual);
    }

    @Test
    void testPassportsSampleList() {
        List<String> imported = readLines("src/test/resources/advent2020/day4/sampleInput");
        long actual = new PassportProcessing().testPassportsList(imported);
        Assertions.assertEquals(2, actual);
    }

    @Test
    void exercisePart1() {
        List<String> imported = readLines("src/test/resources/advent2020/day4/input");
        long actual = new PassportProcessing().testPassportsList(imported);
        System.out.println(actual);
    }

    @Test
    void testPassportPart2_ManualSample() {
        boolean actual = new PassportProcessing().testPassportAndFieldValidity("eyr:2024 pid:0123456789 hcl:#cfa07d byr:1947 iyr:2015 ecl:amb hgt:150cm");
        Assertions.assertEquals(false, actual);
    }

    @Test
    void testPassportsListPart2_SampleInvalid() {
        List<String> imported = readLinesAsEntries("src/test/resources/advent2020/day4/sampleInputInvalid");
        long actual = new PassportProcessing().testPassportsListPart2(imported);
        Assertions.assertEquals(0, actual);
    }

    @Test
    void testPassportsListPart2_SampleValid() {
        List<String> imported = readLinesAsEntries("src/test/resources/advent2020/day4/sampleInputValid");
        long actual = new PassportProcessing().testPassportsListPart2(imported);
        Assertions.assertEquals(4, actual);
    }

    @Test
    void exercisePart2() {
        List<String> imported = readLinesAsEntries("src/test/resources/advent2020/day4/input");
        long actual = new PassportProcessing().testPassportsListPart2(imported);
        System.out.println(actual);
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

    protected List<String> readLinesAsEntries(String filename){
        List<String> lines = Collections.emptyList();
        try
        {
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            sc.useDelimiter("\\Z");

            lines = Arrays.stream(sc.next().split("\\n\\n")).collect(Collectors.toList());

        }
        catch (IOException e)
        {
            // do something
            e.printStackTrace();
        }
        return lines;
    }
}