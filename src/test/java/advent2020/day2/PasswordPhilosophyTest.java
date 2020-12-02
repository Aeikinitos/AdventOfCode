package advent2020.day2;

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
class PasswordPhilosophyTest {

    @Test
    void testPolicySample1() {
        Boolean actual = new PasswordPhilosophy().testPolicy("abcde", new Policy(1,3, 'a'));
        Assertions.assertEquals(true, actual);
    }

    @Test
    void testPolicySample2() {
        Boolean actual = new PasswordPhilosophy().testPolicy("cdefg", new Policy(1,3, 'b'));
        Assertions.assertEquals(false, actual);
    }

    @Test
    void testPolicySample3() {
        Boolean actual = new PasswordPhilosophy().testPolicy("ccccccccc", new Policy(1,9, 'c'));
        Assertions.assertEquals(true, actual);
    }

    @Test
    void countValid() {
        long actual = new PasswordPhilosophy().countValid(Arrays.asList(
                        new PolicyPassword("abcde", new Policy(1,3, 'a')),
                        new PolicyPassword("cdefg", new Policy(1,3, 'b')),
                        PolicyPassword.fromString("1-9 c: ccccccccc", Policy::new),
                        new PolicyPassword("ccccccccc",  new Policy(1,9, 'c'))
        ));
        Assertions.assertEquals(3, actual);

    }


    @Test
    void exericse_day1(){
        List<PolicyPassword> policyPasswords = readLines("src/test/resources/advent2020/day2/input", Policy::new);
        System.out.println(new PasswordPhilosophy().countValid(policyPasswords));
    }

    @Test
    void exericse_day2(){
        List<PolicyPassword> policyPasswords = readLines("src/test/resources/advent2020/day2/input", NewPolicy::new);
        System.out.println(new PasswordPhilosophy().countValid(policyPasswords));
    }


    /* *************************************** */




    protected List<PolicyPassword> readLines(String filename, PolicyFactory factory){
        List<PolicyPassword> lines = Collections.emptyList();
        try
        {
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            sc.useDelimiter("\\Z");

            lines = Arrays.stream(sc.next().split("\\n")).map((String string) -> PolicyPassword.fromString(string, factory)).collect(Collectors.toList());

        }
        catch (IOException e)
        {
            // do something
            e.printStackTrace();
        }
        return lines;
    }
}