package advent2020.day11;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
class SeatingSystemTest {

    char[][] seating = {
                    {'L','.','L','L','.','L','L','.','L','L'},
                    {'L','L','L','L','L','L','L','.','L','L'},
                    {'L','.','L','.','L','.','.','L','.','.'},
                    {'L','L','L','L','.','L','L','.','L','L'},
                    {'L','.','L','L','.','L','L','.','L','L'},
                    {'L','.','L','L','L','L','L','.','L','L'},
                    {'.','.','L','.','L','.','.','.','.','.'},
                    {'L','L','L','L','L','L','L','L','L','L'},
                    {'L','.','L','L','L','L','L','L','.','L'},
                    {'L','.','L','L','L','L','L','.','L','L'}
    };

    @Test
    void step() {

        char[][] expected = {
                        {'#','.','#','#','.','#','#','.','#','#'},
                        {'#','#','#','#','#','#','#','.','#','#'},
                        {'#','.','#','.','#','.','.','#','.','.'},
                        {'#','#','#','#','.','#','#','.','#','#'},
                        {'#','.','#','#','.','#','#','.','#','#'},
                        {'#','.','#','#','#','#','#','.','#','#'},
                        {'.','.','#','.','#','.','.','.','.','.'},
                        {'#','#','#','#','#','#','#','#','#','#'},
                        {'#','.','#','#','#','#','#','#','.','#'},
                        {'#','.','#','#','#','#','#','.','#','#'}
        };

        SeatingSystem seatingSystem = new SeatingSystem();

        // test
        char[][] actual = seatingSystem.step(seating, Adjucent::sitDown, Adjucent::standUp);


        // validate
        for (int i = 0; i < seating.length; i++) {
            for (int j = 0; j < seating[0].length; j++) {
                Assertions.assertEquals(expected[i][j], actual[i][j]);
            }
        }


    }


    @Test
    void iterateUntilStable() {
        char[][] expected = {
                        {'#','.','#','L','.','L','#','.','#','#'},
                        {'#','L','L','L','#','L','L','.','L','#'},
                        {'L','.','#','.','L','.','.','#','.','.'},
                        {'#','L','#','#','.','#','#','.','L','#'},
                        {'#','.','#','L','.','L','L','.','L','L'},
                        {'#','.','#','L','#','L','#','.','#','#'},
                        {'.','.','L','.','L','.','.','.','.','.'},
                        {'#','L','#','L','#','#','L','#','L','#'},
                        {'#','.','L','L','L','L','L','L','.','L'},
                        {'#','.','#','L','#','L','#','.','#','#'}
        };


        SeatingSystem seatingSystem = new SeatingSystem();

        // test
        char[][] actual = seatingSystem.iterateUntilStable(seating, Adjucent::sitDown, Adjucent::standUp);
        int countSeats = SeatingSystem.countSeats(actual);


        // validate
        for (int i = 0; i < seating.length; i++) {
            for (int j = 0; j < seating[0].length; j++) {
                Assertions.assertEquals(expected[i][j], actual[i][j]);
            }
        }
        Assertions.assertEquals(37, countSeats);
    }


    @Test
    void part1() {
        char[][] seating = readLines("src/test/resources/advent2020/day11/input");

        SeatingSystem seatingSystem = new SeatingSystem();

        // test
        char[][] actual = seatingSystem.iterateUntilStable(seating, Adjucent::sitDown, Adjucent::standUp);
        int countSeats = SeatingSystem.countSeats(actual);

        System.out.println(countSeats);
    }

    @Test
    void iterateUntilStable_Visible() {
        char[][] expected = {
                        {'#','.','L','#','.','L','#','.','L','#'},
                        {'#','L','L','L','L','L','L','.','L','L'},
                        {'L','.','L','.','L','.','.','#','.','.'},
                        {'#','#','L','#','.','#','L','.','L','#'},
                        {'L','.','L','#','.','L','L','.','L','#'},
                        {'#','.','L','L','L','L','#','.','L','L'},
                        {'.','.','#','.','L','.','.','.','.','.'},
                        {'L','L','L','#','#','#','L','L','L','#'},
                        {'#','.','L','L','L','L','L','#','.','L'},
                        {'#','.','L','#','L','L','#','.','L','#'}
        };


        SeatingSystem seatingSystem = new SeatingSystem();

        // test
        char[][] actual = seatingSystem.iterateUntilStable(seating, Visible::sitDown, Visible::standUp);
        int countSeats = SeatingSystem.countSeats(actual);


        // validate
        for (int i = 0; i < seating.length; i++) {
            for (int j = 0; j < seating[0].length; j++) {
                Assertions.assertEquals(expected[i][j], actual[i][j]);
            }
        }
        Assertions.assertEquals(26, countSeats);
    }

    @Test
    void part2() {
        char[][] seating = readLines("src/test/resources/advent2020/day11/input");

        SeatingSystem seatingSystem = new SeatingSystem();

        // test
        char[][] actual = seatingSystem.iterateUntilStable(seating, Visible::sitDown, Visible::standUp);
        int countSeats = SeatingSystem.countSeats(actual);

        System.out.println(countSeats);
    }

    /* *************************************** */

    protected char[][] readLines(String filename){
        char[][] seating = new char[98][];
        try
        {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            int i = 0;
            while(scanner.hasNext()) {
                seating[i++] = scanner.nextLine().toCharArray();
            }

        }
        catch (IOException e)
        {
            // do something
            e.printStackTrace();
        }
        return seating;
    }


}