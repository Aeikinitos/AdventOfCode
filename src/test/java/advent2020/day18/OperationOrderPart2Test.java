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

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
class OperationOrderPart2Test {


    @Test
    void processInput() {

        List<String> input = OperationOrderPart2.parseRawInput("1 + 2 * 3 + 4 * 5 + 6");
        Long actual = OperationOrderPart2.processInput(input);

        Assertions.assertEquals(231L, actual);
    }

    @Test
    void processInputParenthesis() {

        List<String> input = OperationOrderPart2.parseRawInput("1 + (2 * 3) + (4 * (5 + 6))");
        Long actual = OperationOrderPart2.processInput(input);

        Assertions.assertEquals(51L, actual);
    }

    @Test
    void processInputParenthesisSample1() {

        List<String> input = OperationOrderPart2.parseRawInput("2 * 3 + (4 * 5)");
        Long actual = OperationOrderPart2.processInput(input);

        Assertions.assertEquals(46L, actual);
    }

    @Test
    void processInputParenthesisSample2() {

        List<String> input = OperationOrderPart2.parseRawInput("5 + (8 * 3 + 9 + 3 * 4 * 3)");
        Long actual = OperationOrderPart2.processInput(input);

        Assertions.assertEquals(1445L, actual);
    }

    @Test
    void processInputParenthesisSample3() {

        List<String> input = OperationOrderPart2.parseRawInput("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))");
        Long actual = OperationOrderPart2.processInput(input);

        Assertions.assertEquals(669060L, actual);
    }

    @Test
    void processInputParenthesisSample4() {

        List<String> input = OperationOrderPart2.parseRawInput("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2");
        Long actual = OperationOrderPart2.processInput(input);

        Assertions.assertEquals(23340L, actual);
    }

    @Test
    void part1(){
        List<String> lines = readLines("src/test/resources/advent2020/day18/input");

        Long sum = 0L;
        for (String line : lines) {
            List<String> input = OperationOrderPart2.parseRawInput(line);
            Long actual = OperationOrderPart2.processInput(input);
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