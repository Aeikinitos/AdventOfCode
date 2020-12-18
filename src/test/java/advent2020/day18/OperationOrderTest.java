package advent2020.day18;

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
class OperationOrderTest {

    @Test
    void processInput() {

        List<Character> input = OperationOrder.parseRawInput("1 + 2 * 3 + 4 * 5 + 6");
        Long actual = OperationOrder.processInput(input);

        Assertions.assertEquals(71L, actual);
    }

    @Test
    void processInputParenthesis() {

        List<Character> input = OperationOrder.parseRawInput("1 + (2 * 3) + (4 * (5 + 6))");
        Long actual = OperationOrder.processInput(input);

        Assertions.assertEquals(51L, actual);
    }

    @Test
    void processInputParenthesisSample1() {

        List<Character> input = OperationOrder.parseRawInput("2 * 3 + (4 * 5)");
        Long actual = OperationOrder.processInput(input);

        Assertions.assertEquals(26L, actual);
    }

    @Test
    void processInputParenthesisSample2() {

        List<Character> input = OperationOrder.parseRawInput("5 + (8 * 3 + 9 + 3 * 4 * 3)");
        Long actual = OperationOrder.processInput(input);

        Assertions.assertEquals(437L, actual);
    }

    @Test
    void processInputParenthesisSample3() {

        List<Character> input = OperationOrder.parseRawInput("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))");
        Long actual = OperationOrder.processInput(input);

        Assertions.assertEquals(12240L, actual);
    }

    @Test
    void processInputParenthesisSample4() {

        List<Character> input = OperationOrder.parseRawInput("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2");
        Long actual = OperationOrder.processInput(input);

        Assertions.assertEquals(13632L, actual);
    }

    @Test
    void part1(){
        List<String> lines = readLines("src/test/resources/advent2020/day18/input");

        Long sum = 0L;
        for (String line : lines) {
            List<Character> input = OperationOrder.parseRawInput(line);
            Long actual = OperationOrder.processInput(input);
            sum += actual;
        }

        System.out.println("Part1 :"+sum);

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