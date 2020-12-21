package advent2020.day21;

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
class AllergenAssessmentTest {

    @Test
    void sample() {
        List<String> input = readLines("src/test/resources/advent2020/day21/sample");

        AllergenAssessment allergenAssessment = new AllergenAssessment();

        allergenAssessment.parseRawInput(input);
        Long numberIngredientsWithNoAllergens = allergenAssessment.calculateNumberIngredientsWithNoAllergens();
        Assertions.assertEquals(5, numberIngredientsWithNoAllergens);
    }

    @Test
    void part1() {
        List<String> input = readLines("src/test/resources/advent2020/day21/input");

        AllergenAssessment allergenAssessment = new AllergenAssessment();

        allergenAssessment.parseRawInput(input);
        Long numberIngredientsWithNoAllergens = allergenAssessment.calculateNumberIngredientsWithNoAllergens();
        System.out.println(numberIngredientsWithNoAllergens);
    }

    @Test
    void samplePart2() {
        List<String> input = readLines("src/test/resources/advent2020/day21/sample");

        AllergenAssessment allergenAssessment = new AllergenAssessment();

        allergenAssessment.parseRawInput(input);
        Map<String, String> allergenToIngredient = allergenAssessment.deduceIngredientAllergens();
        allergenToIngredient.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).map(Map.Entry::getValue).reduce((s, s2) -> s+","+s2).ifPresent(System.out::println);
    }

    @Test
    void part2() {
        List<String> input = readLines("src/test/resources/advent2020/day21/input");

        AllergenAssessment allergenAssessment = new AllergenAssessment();

        allergenAssessment.parseRawInput(input);
        Map<String, String> allergenToIngredient = allergenAssessment.deduceIngredientAllergens();
        allergenToIngredient.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).map(Map.Entry::getValue).reduce((s, s2) -> s+","+s2).ifPresent(System.out::println);
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