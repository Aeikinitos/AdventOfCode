package advent2020.day15;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
class RambunctiousRecitationTest {

    @Test
    void nextNumberSample1Individual() {
        List<Long> initialNumbers = Arrays.asList(0L, 3L, 6L);
        RambunctiousRecitation rambunctiousRecitation = new RambunctiousRecitation();
        rambunctiousRecitation.loadInitialData(initialNumbers);

        // initial 0, 3, 6,
        // output  0, 3, 3, 1, 0, 4, 0
        Assertions.assertEquals(0L, rambunctiousRecitation.nextNumber());
        Assertions.assertEquals(3L, rambunctiousRecitation.nextNumber());
        Assertions.assertEquals(3L, rambunctiousRecitation.nextNumber());
        Assertions.assertEquals(1L, rambunctiousRecitation.nextNumber());
        Assertions.assertEquals(0L, rambunctiousRecitation.nextNumber());
        Assertions.assertEquals(4L, rambunctiousRecitation.nextNumber());
        Assertions.assertEquals(0L, rambunctiousRecitation.nextNumber());
    }

    @Test
    void nextNumberSample1() {
        List<Long> initialNumbers = Arrays.asList(0L, 3L, 6L);
        RambunctiousRecitation rambunctiousRecitation = new RambunctiousRecitation();
        rambunctiousRecitation.loadInitialData(initialNumbers);

        long actual = -1L;
        for (int i = 0; i < 2020-initialNumbers.size(); i++) {
            actual = rambunctiousRecitation.nextNumber();
        }

        Assertions.assertEquals(436L, actual);
    }


    @Test
    void nextNumberSample2() {
        List<Long> initialNumbers = Arrays.asList(1L, 3L, 2L);
        RambunctiousRecitation rambunctiousRecitation = new RambunctiousRecitation();
        rambunctiousRecitation.loadInitialData(initialNumbers);

        long actual = -1L;
        for (int i = 0; i < 2020-initialNumbers.size(); i++) {
            actual = rambunctiousRecitation.nextNumber();
        }

        Assertions.assertEquals(1L, actual);
    }

    @Test
    void nextNumberSample3() {
        List<Long> initialNumbers = Arrays.asList(2L, 1L, 3L);
        RambunctiousRecitation rambunctiousRecitation = new RambunctiousRecitation();
        rambunctiousRecitation.loadInitialData(initialNumbers);

        long actual = -1L;
        for (int i = 0; i < 2020-initialNumbers.size(); i++) {
            actual = rambunctiousRecitation.nextNumber();
        }

        Assertions.assertEquals(10L, actual);
    }

    @Test
    void nextNumberSample4() {
        List<Long> initialNumbers = Arrays.asList(1L, 2L, 3L);
        RambunctiousRecitation rambunctiousRecitation = new RambunctiousRecitation();
        rambunctiousRecitation.loadInitialData(initialNumbers);

        long actual = -1L;
        for (int i = 0; i < 2020-initialNumbers.size(); i++) {
            actual = rambunctiousRecitation.nextNumber();
        }

        Assertions.assertEquals(27L, actual);
    }

    @Test
    void nextNumberSample5() {
        List<Long> initialNumbers = Arrays.asList(2L, 3L, 1L);
        RambunctiousRecitation rambunctiousRecitation = new RambunctiousRecitation();
        rambunctiousRecitation.loadInitialData(initialNumbers);

        long actual = -1L;
        for (int i = 0; i < 2020-initialNumbers.size(); i++) {
            actual = rambunctiousRecitation.nextNumber();
        }

        Assertions.assertEquals(78L, actual);
    }

    @Test
    void nextNumberSample6() {
        List<Long> initialNumbers = Arrays.asList(3L, 2L, 1L);
        RambunctiousRecitation rambunctiousRecitation = new RambunctiousRecitation();
        rambunctiousRecitation.loadInitialData(initialNumbers);

        long actual = -1L;
        for (int i = 0; i < 2020-initialNumbers.size(); i++) {
            actual = rambunctiousRecitation.nextNumber();
        }

        Assertions.assertEquals(438L, actual);
    }

    @Test
    void nextNumberSample7() {
        List<Long> initialNumbers = Arrays.asList(3L, 1L, 2L);
        RambunctiousRecitation rambunctiousRecitation = new RambunctiousRecitation();
        rambunctiousRecitation.loadInitialData(initialNumbers);

        long actual = -1L;
        for (int i = 0; i < 2020-initialNumbers.size(); i++) {
            actual = rambunctiousRecitation.nextNumber();
        }

        Assertions.assertEquals(1836L, actual);
    }

    @Test
    void part1() {
        List<Long> initialNumbers = Arrays.asList(2L, 0L, 6L, 12L, 1L, 3L);
        RambunctiousRecitation rambunctiousRecitation = new RambunctiousRecitation();
        rambunctiousRecitation.loadInitialData(initialNumbers);

        long actual = -1L;
        for (int i = 0; i < 2020-initialNumbers.size(); i++) {
            actual = rambunctiousRecitation.nextNumber();
        }

        System.out.println(actual);
    }


    @Test
    void nextNumberSample1_part2() {
        List<Long> initialNumbers = Arrays.asList(0L, 3L, 6L);
        RambunctiousRecitation rambunctiousRecitation = new RambunctiousRecitation();
        rambunctiousRecitation.loadInitialData(initialNumbers);

        long actual = -1L;
        for (int i = 0; i < 30000000-initialNumbers.size(); i++) {
            actual = rambunctiousRecitation.nextNumber();
        }

        Assertions.assertEquals(175594L, actual);
    }


    @Test
    void nextNumberSample2_part2() {
        List<Long> initialNumbers = Arrays.asList(1L, 3L, 2L);
        RambunctiousRecitation rambunctiousRecitation = new RambunctiousRecitation();
        rambunctiousRecitation.loadInitialData(initialNumbers);

        long actual = -1L;
        for (int i = 0; i < 30000000-initialNumbers.size(); i++) {
            actual = rambunctiousRecitation.nextNumber();
        }

        Assertions.assertEquals(2578L, actual);
    }

    @Test
    void nextNumberSample3_part2() {
        List<Long> initialNumbers = Arrays.asList(2L, 1L, 3L);
        RambunctiousRecitation rambunctiousRecitation = new RambunctiousRecitation();
        rambunctiousRecitation.loadInitialData(initialNumbers);

        long actual = -1L;
        for (int i = 0; i < 30000000-initialNumbers.size(); i++) {
            actual = rambunctiousRecitation.nextNumber();
        }

        Assertions.assertEquals(3544142L, actual);
    }

    @Test
    void nextNumberSample4_part2() {
        List<Long> initialNumbers = Arrays.asList(1L, 2L, 3L);
        RambunctiousRecitation rambunctiousRecitation = new RambunctiousRecitation();
        rambunctiousRecitation.loadInitialData(initialNumbers);

        long actual = -1L;
        for (int i = 0; i < 30000000-initialNumbers.size(); i++) {
            actual = rambunctiousRecitation.nextNumber();
        }

        Assertions.assertEquals(261214L, actual);
    }

    @Test
    void nextNumberSample5_part2() {
        List<Long> initialNumbers = Arrays.asList(2L, 3L, 1L);
        RambunctiousRecitation rambunctiousRecitation = new RambunctiousRecitation();
        rambunctiousRecitation.loadInitialData(initialNumbers);

        long actual = -1L;
        for (int i = 0; i < 30000000-initialNumbers.size(); i++) {
            actual = rambunctiousRecitation.nextNumber();
        }

        Assertions.assertEquals(6895259L, actual);
    }

    @Test
    void nextNumberSample6_part2() {
        List<Long> initialNumbers = Arrays.asList(3L, 2L, 1L);
        RambunctiousRecitation rambunctiousRecitation = new RambunctiousRecitation();
        rambunctiousRecitation.loadInitialData(initialNumbers);

        long actual = -1L;
        for (int i = 0; i < 30000000-initialNumbers.size(); i++) {
            actual = rambunctiousRecitation.nextNumber();
        }

        Assertions.assertEquals(18L, actual);
    }

    @Test
    void nextNumberSample7_part2() {
        List<Long> initialNumbers = Arrays.asList(3L, 1L, 2L);
        RambunctiousRecitation rambunctiousRecitation = new RambunctiousRecitation();
        rambunctiousRecitation.loadInitialData(initialNumbers);

        long actual = -1L;
        for (int i = 0; i < 30000000-initialNumbers.size(); i++) {
            actual = rambunctiousRecitation.nextNumber();
        }

        Assertions.assertEquals(362L, actual);
    }

    @Test
    void part2() {
        List<Long> initialNumbers = Arrays.asList(2L, 0L, 6L, 12L, 1L, 3L);
        RambunctiousRecitation rambunctiousRecitation = new RambunctiousRecitation();
        rambunctiousRecitation.loadInitialData(initialNumbers);

        long actual = -1L;
        for (int i = 0; i < 30000000-initialNumbers.size(); i++) {
            actual = rambunctiousRecitation.nextNumber();
        }

        System.out.println(actual);
    }

}