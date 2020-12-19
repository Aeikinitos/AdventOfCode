package advent2020.day19;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
class MonsterMessagesTest {


    @Test
    void processInput() {
        MonsterMessages monsterMessages = new MonsterMessages();
        monsterMessages.parseRawInput(Arrays.asList(
                        "0: 4 1 5",
                        "1: 2 3 | 3 2",
                        "2: 4 4 | 5 5",
                        "3: 4 5 | 5 4",
                        "4: \"a\"",
                        "5: \"b\""
        ));

        Assertions.assertTrue(monsterMessages.processInput("ababbb"));
        Assertions.assertFalse(monsterMessages.processInput("bababa"));
        Assertions.assertTrue(monsterMessages.processInput("abbbab"));
        Assertions.assertFalse(monsterMessages.processInput("aaabbb"));
        Assertions.assertFalse(monsterMessages.processInput("aaaabbb"));
    }

    @Test
    void part1() {
        List<String> lines = readLines("src/test/resources/advent2020/day19/input");
        String[] rules = lines.get(0).split("\\n");
        String[] input = lines.get(1).split("\\n");
        MonsterMessages monsterMessages = new MonsterMessages();
        monsterMessages.parseRawInput(Arrays.asList(rules));

        long count = Stream.of(input).filter(monsterMessages::processInput).count();

        System.out.println("Part1 :"+count);
    }


    @Test
    void part2Sample() {
        List<String> lines = readLines("src/test/resources/advent2020/day19/samplePart2");
        String[] rules = lines.get(0).split("\\n");
        String[] input = lines.get(1).split("\\n");
        MonsterMessages monsterMessages = new MonsterMessages();
        monsterMessages.parseRawInput(Arrays.asList(rules));

        long count = Stream.of(input).filter(monsterMessages::processInput).count();

        Assertions.assertEquals(12, count);
    }

    @Test
    void part2Manual() {
        List<String> lines = readLines("src/test/resources/advent2020/day19/samplePart2");
        String[] rules = lines.get(0).split("\\n");
        MonsterMessages monsterMessages = new MonsterMessages();
        monsterMessages.parseRawInput(Arrays.asList(rules));

        boolean invalid = monsterMessages.processInput("aaaabbaaaabbaaa");
        Assertions.assertFalse(invalid);

        boolean valid = monsterMessages.processInput("aaaaabbaabaaaaababaa");
        Assertions.assertTrue(valid);

    }

    @Test
    void part2Loop() {

        MonsterMessages monsterMessages = new MonsterMessages();
        monsterMessages.parseRawInput(Arrays.asList(
                        "0: 3 | 0 1",
                        "3: \"b\"",
                        "1: \"a\""
        ));

        boolean valid = monsterMessages.processInput("ba");
        Assertions.assertTrue(valid);
    }


    @Test
    void part2() {
        List<String> lines = readLines("src/test/resources/advent2020/day19/inputPart2");
        String[] rules = lines.get(0).split("\\n");
        String[] input = lines.get(1).split("\\n");
        MonsterMessages monsterMessages = new MonsterMessages();
        monsterMessages.parseRawInput(Arrays.asList(rules));

        long count = Stream.of(input).filter(monsterMessages::processInput).count();

        System.out.println("Part2 :"+count);
    }

    /* *************************************** */

    protected List<String> readLines(String filename){
        List<String> lines = Collections.emptyList();
        try
        {
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            sc.useDelimiter("\\Z");

            lines = Arrays.stream(sc.next().split("\n\\n")).collect(Collectors.toList());

        }
        catch (IOException e)
        {
            // do something
            e.printStackTrace();
        }
        return lines;
    }
}