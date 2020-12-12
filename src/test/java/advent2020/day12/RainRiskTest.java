package advent2020.day12;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
class RainRiskTest {

    @Test
    void applyInstructions() {
        List<NavigationInstruction> instructions = new ArrayList<>();
        instructions.add(new NavigationInstruction('F',10));
        instructions.add(new NavigationInstruction('N',3));
        instructions.add(new NavigationInstruction('F',7));
        instructions.add(new NavigationInstruction('R',90));
        instructions.add(new NavigationInstruction('F',11));
        ShipState expected = new ShipState(-8, 17, -90);

        ShipState actual = RainRisk.applyInstructions(new ShipState(), instructions);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void manhattanDistance() {

        List<NavigationInstruction> instructions = new ArrayList<>();
        instructions.add(new NavigationInstruction('F',10));
        instructions.add(new NavigationInstruction('N',3));
        instructions.add(new NavigationInstruction('F',7));
        instructions.add(new NavigationInstruction('R',90));
        instructions.add(new NavigationInstruction('F',11));

        ShipState actual = RainRisk.applyInstructions(new ShipState(), instructions);
        int manhattanDistance = RainRisk.manhattanDistance(actual);

        Assertions.assertEquals(25, manhattanDistance);
    }

    @Test
    void part1(){
        List<NavigationInstruction> instructions = readLines("src/test/resources/advent2020/day12/input").stream()
                                                                                                    .map(NavigationInstruction::fromString)
                                                                                                    .collect(Collectors
                                                                                                                    .toList());
        ShipState actual = RainRisk.applyInstructions(new ShipState(), instructions);
        int manhattanDistance = RainRisk.manhattanDistance(actual);
        System.out.println("Part1: " + manhattanDistance);
    }

    @Test
    void applyInstructionsWaypoint() {
        List<NavigationInstruction> instructions = new ArrayList<>();
        instructions.add(new NavigationInstruction('F',10));
        instructions.add(new NavigationInstruction('N',3));
        instructions.add(new NavigationInstruction('F',7));
        instructions.add(new NavigationInstruction('R',90));
        instructions.add(new NavigationInstruction('F',11));
        ShipState expected = new ShipStateWaypoint(72, 214, 0, -10, 4 );

        ShipState actual = RainRisk.applyInstructions(new ShipStateWaypoint(1,10), instructions);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void part2(){
        List<NavigationInstruction> instructions = readLines("src/test/resources/advent2020/day12/input").stream()
                                                                                                         .map(NavigationInstruction::fromString)
                                                                                                         .collect(Collectors
                                                                                                                         .toList());
        ShipState actual = RainRisk.applyInstructions(new ShipStateWaypoint(1,10), instructions);
        int manhattanDistance = RainRisk.manhattanDistance(actual);
        System.out.println("Part2: " + manhattanDistance);
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