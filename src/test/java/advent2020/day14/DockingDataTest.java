package advent2020.day14;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
class DockingDataTest {

    @Test
    void applyAssignmentSample1() {
        // value:  000000000000000000000000000000001011  (decimal 11)
        // mask:   XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
        // result: 000000000000000000000000000001001001  (decimal 73)

        String actual = DockingData.applyAssignment("000000000000000000000000000000001011", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X");

        Assertions.assertEquals("000000000000000000000000000001001001", actual);
    }

    @Test
    void applyAssignmentSample2() {
        // value:  000000000000000000000000000001100101  (decimal 101)
        // mask:   XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
        // result: 000000000000000000000000000001100101  (decimal 101)

        String actual = DockingData.applyAssignment("000000000000000000000000000001100101", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X");

        Assertions.assertEquals("000000000000000000000000000001100101", actual);
    }

    @Test
    void applyAssignmentSample3() {

        // value:  000000000000000000000000000000000000    (decimal 0)
        // mask:   XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
        // result: 000000000000000000000000000001000000    (decimal 64)

        String actual = DockingData.applyAssignment("000000000000000000000000000000000000", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X");

        Assertions.assertEquals("000000000000000000000000000001000000", actual);
    }

    @Test
    void processInputSample() {
        List<String> lines = Arrays.asList(
                        "mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X",
                        "mem[8] = 11",
                        "mem[7] = 101",
                        "mem[8] = 0"
        );

        DockingData dockingData = new DockingData();

        long sum = dockingData.processInput(lines);

        Assertions.assertEquals(165L, sum);
    }

    @Test
    void part1(){

        List<String> lines = readLines("src/test/resources/advent2020/day14/input");
        DockingData dockingData = new DockingData();

        long sum = dockingData.processInput(lines);
        System.out.println("Part1 : " + sum);
        
    }

    @Test
    void processInputSampleV2() {
        List<String> lines = Arrays.asList(
                        "mask = 000000000000000000000000000000X1001X",
                        "mem[42] = 100",
                        "mask = 00000000000000000000000000000000X0XX",
                        "mem[26] = 1"
        );

        DockingData dockingData = new DockingData();

        long sum = dockingData.processInputV2(lines);

        Assertions.assertEquals(208L, sum);
    }

    @Test
    void part2(){

        List<String> lines = readLines("src/test/resources/advent2020/day14/input");
        DockingData dockingData = new DockingData();

        long sum = dockingData.processInputV2(lines);
        System.out.println("Part2 : " + sum);

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