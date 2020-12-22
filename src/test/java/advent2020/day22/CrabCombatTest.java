package advent2020.day22;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
class CrabCombatTest {


    @Test
    void playGame() {

        List<String> players = readLines("src/test/resources/advent2020/day22/sample");
        LinkedList<Integer> player1 = Stream.of(players.get(0).split("\\n")).skip(1).map(Integer::valueOf).collect(Collectors.toCollection(LinkedList::new)); ;
        LinkedList<Integer> player2 = Stream.of(players.get(1).split("\\n")).skip(1).map(Integer::valueOf).collect(Collectors.toCollection(LinkedList::new)); ;

        CrabCombat.playGame(player1, player2);
        Long score = CrabCombat.calculateScore(player1.size() == 0 ? player2 : player1);
        Assertions.assertEquals(306L, score);
    }

    @Test
    void part1() {

        List<String> players = readLines("src/test/resources/advent2020/day22/input");
        LinkedList<Integer> player1 = Stream.of(players.get(0).split("\\n")).skip(1).map(Integer::valueOf).collect(Collectors.toCollection(LinkedList::new)); ;
        LinkedList<Integer> player2 = Stream.of(players.get(1).split("\\n")).skip(1).map(Integer::valueOf).collect(Collectors.toCollection(LinkedList::new)); ;

        CrabCombat.playGame(player1, player2);
        Long score = CrabCombat.calculateScore(player1.size() == 0 ? player2 : player1);
        System.out.println(score);
    }

    @Test
    void playRecursiveCombat(){
        List<String> players = readLines("src/test/resources/advent2020/day22/sample");
        LinkedList<Integer> player1 = Stream.of(players.get(0).split("\\n")).skip(1).map(Integer::valueOf).collect(Collectors.toCollection(LinkedList::new)); ;
        LinkedList<Integer> player2 = Stream.of(players.get(1).split("\\n")).skip(1).map(Integer::valueOf).collect(Collectors.toCollection(LinkedList::new)); ;

        boolean playerOneWin = CrabCombat.playRecursiveCombat(player1, player2);
        Long score = CrabCombat.calculateScore(playerOneWin ? player1 : player2);
        Assertions.assertEquals(291L, score);
    }

    @Test
    void part2() {

        List<String> players = readLines("src/test/resources/advent2020/day22/input");
        LinkedList<Integer> player1 = Stream.of(players.get(0).split("\\n")).skip(1).map(Integer::valueOf).collect(Collectors.toCollection(LinkedList::new));
        LinkedList<Integer> player2 = Stream.of(players.get(1).split("\\n")).skip(1).map(Integer::valueOf).collect(Collectors.toCollection(LinkedList::new));

        boolean playerOneWin = CrabCombat.playRecursiveCombat(player1, player2);
        Long score = CrabCombat.calculateScore(playerOneWin ? player1 : player2);
        System.out.println(score);
    }

    /* *************************************** */

    protected List<String> readLines(String filename){
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