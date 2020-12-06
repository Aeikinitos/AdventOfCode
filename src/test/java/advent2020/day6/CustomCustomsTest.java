package advent2020.day6;

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
class CustomCustomsTest {

    @Test
    void countYes() {
        long actual = CustomCustoms.countYes("abcx\nabcy\nabcz");
        Assertions.assertEquals(6, actual);
    }

    @Test
    void exercicePart1(){
        List<String> groups = readGroups("src/test/resources/advent2020/day6/input");
        int actual = CustomCustoms.sumOfYes(groups);
        System.out.println(actual);
    }


    @Test
    void countYesEveryone() {
        Assertions.assertEquals(3, CustomCustoms.countYesEveryone("abc"));
        Assertions.assertEquals(0, CustomCustoms.countYesEveryone("a\nb\nc"));
        Assertions.assertEquals(1, CustomCustoms.countYesEveryone("ab\nac"));
        Assertions.assertEquals(1, CustomCustoms.countYesEveryone("a\na\na\na"));
        Assertions.assertEquals(1, CustomCustoms.countYesEveryone("b"));
    }

    @Test
    void sumtYesEveryone() {
        List<String> groupAnswers = Arrays.asList("abc", "a\nb\nc", "ab\nac", "a\na\na\na", "b");
        Assertions.assertEquals(6, CustomCustoms.sumOfYesEveryone(groupAnswers));
    }

    @Test
    void exercicePart2(){
        List<String> groups = readGroups("src/test/resources/advent2020/day6/input");
        int actual = CustomCustoms.sumOfYesEveryone(groups);
        System.out.println(actual);
    }

    /* ******************************** */
    protected List<String> readGroups(String filename){
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