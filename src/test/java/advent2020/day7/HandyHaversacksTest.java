package advent2020.day7;

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
class HandyHaversacksTest {

    @Test
    void testOriginal_smallset() {
        List<String> lines = readLines("src/test/resources/advent2020/day7/sampleOriginal");
        Map<String, List<InnerBagRule>> rules = HandyHaversacks.parseRules(lines);

        int actual = new HandyHaversacks(rules).countInnerBags("vibrant plum");
        // 5 faded blue bags, 6 dotted black bags.
        Assertions.assertEquals(11, actual);
    }

    @Test
    void testOriginal_2levels() {
        List<String> lines = readLines("src/test/resources/advent2020/day7/sampleOriginal");
        Map<String, List<InnerBagRule>> rules = HandyHaversacks.parseRules(lines);

        int actual = new HandyHaversacks(rules).countInnerBags("sotirakis test");
        // 2 vibrant plum => (5 faded blue bags, 6 dotted black bags).
        Assertions.assertEquals(24, actual);
    }

    @Test
    void testOriginal() {
        List<String> lines = readLines("src/test/resources/advent2020/day7/sampleOriginal");
        Map<String, List<InnerBagRule>> rules = HandyHaversacks.parseRules(lines);

        int actual = new HandyHaversacks(rules).countInnerBags("shiny gold");
        Assertions.assertEquals(32, actual);
    }

    @Test
    void testHierarchy() {
        List<String> lines = readLines("src/test/resources/advent2020/day7/sampleHierarchy");
        Map<String, List<InnerBagRule>> rules = HandyHaversacks.parseRules(lines);

        int actual = new HandyHaversacks(rules).countInnerBags("shiny gold");
        Assertions.assertEquals(126, actual);
    }

    @Test
    void exercise2() {
        List<String> lines = readLines("src/test/resources/advent2020/day7/input");
        Map<String, List<InnerBagRule>> rules = HandyHaversacks.parseRules(lines);

        int actual = new HandyHaversacks(rules).countInnerBags("shiny gold");
        System.out.println(actual);
    }

    /* ******************************** */
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