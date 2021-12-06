package advent2021.day6;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
class LanternfishTest {

    @Test
    void calculatePopulationSample18DaysPart1() {
        long actual = new Lanternfish().calculateFishPopulation(getSample(), 18);
        Assertions.assertEquals(26, actual);
    }

    @Test
    void calculatePopulationSample80DaysPart1() {
        long actual = new Lanternfish().calculateFishPopulation(getSample(), 80);
        Assertions.assertEquals(5934, actual);
    }

    @Test
    void calculatePopulationPart1() {
        long actual = new Lanternfish().calculateFishPopulation(getInput(), 80);
        Assertions.assertEquals(359999, actual);
    }

    @Test
    void calculatePopulationSample1256aysPart2() {
        long actual = new Lanternfish().calculateFishPopulation(getSample(), 256);
        Assertions.assertEquals(26984457539L, actual);
    }

    @Test
    void calculatePopulationPart2() {
        long actual = new Lanternfish().calculateFishPopulation(getInput(), 256);
        Assertions.assertEquals(1631647919273L, actual);
    }


    private List<Integer> getSample(){
        return Arrays.asList(3,4,3,1,2);
    }

    private List<Integer> getInput(){
        return Arrays.asList(5,3,2,2,1,1,4,1,5,5,1,3,1,5,1,2,1,4,1,2,1,2,1,4,2,4,1,5,1,3,5,4,3,3,1,4,1,3,4,4,1,5,4,3,3,2,5,1,1,3,1,4,3,2,2,3,1,3,1,3,1,5,3,5,1,3,1,4,2,1,4,1,5,5,5,2,4,2,1,4,1,3,5,5,1,4,1,1,4,2,2,1,3,1,1,1,1,3,4,1,4,1,1,1,4,4,4,1,3,1,3,4,1,4,1,2,2,2,5,4,1,3,1,2,1,4,1,4,5,2,4,5,4,1,2,1,4,2,2,2,1,3,5,2,5,1,1,4,5,4,3,2,4,1,5,2,2,5,1,4,1,5,1,3,5,1,2,1,1,1,5,4,4,5,1,1,1,4,1,3,3,5,5,1,5,2,1,1,3,1,1,3,2,3,4,4,1,5,5,3,2,1,1,1,4,3,1,3,3,1,1,2,2,1,2,2,2,1,1,5,1,2,2,5,2,4,1,1,2,4,1,2,3,4,1,2,1,2,4,2,1,1,5,3,1,4,4,4,1,5,2,3,4,4,1,5,1,2,2,4,1,1,2,1,1,1,1,5,1,3,3,1,1,1,1,4,1,2,2,5,1,2,1,3,4,1,3,4,3,3,1,1,5,5,5,2,4,3,1,4);
    }
}