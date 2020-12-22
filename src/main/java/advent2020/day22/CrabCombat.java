package advent2020.day22;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class CrabCombat {


    public static void playGame(LinkedList<Integer> player1, LinkedList<Integer> player2){

        while(player1.size() > 0 &&  player2.size() > 0){
            Integer card1 = player1.poll();
            Integer card2 = player2.poll();
            if(card1 > card2){
                player1.add(card1);
                player1.add(card2);
            } else {
                player2.add(card2);
                player2.add(card1);
            }
        }
    }

    public static Long calculateScore(LinkedList<Integer> player){
        AtomicInteger maxScore = new AtomicInteger(player.size());
        return player.stream().map(integer -> integer * maxScore.getAndDecrement()).map(Long::valueOf).reduce(Long::sum).get();
    }

    // true if playerOne wins
    public static boolean playRecursiveCombat(LinkedList<Integer> player1, LinkedList<Integer> player2){
        Set<String> pastDecks = new HashSet<>();
        while(player1.size() > 0 &&  player2.size() > 0 ){
            String decksId = getDecksId(player1, player2);
            if(pastDecks.contains(decksId)){
                return true; // playerOne wins
            }
            pastDecks.add(decksId);
            Integer card1 = player1.poll();
            Integer card2 = player2.poll();
            if(shouldSubCombat(card1, player1, card2, player2)){
                if(playRecursiveCombat(new LinkedList<>(player1.subList(0, card1)), new LinkedList<>(player2.subList(0, card2)))){
                    player1.add(card1);
                    player1.add(card2);
                } else {
                    player2.add(card2);
                    player2.add(card1);
                }
                continue;
            }
            if(card1 > card2){
                player1.add(card1);
                player1.add(card2);
            } else {
                player2.add(card2);
                player2.add(card1);
            }
        }
        // true if playerOne wins
        return player2.size() == 0;

    }

    private static boolean shouldSubCombat(Integer card1, LinkedList<Integer> player1, Integer card2, LinkedList<Integer> player2) {
        return player1.size() >= card1 && player2.size() >= card2;
    }

    private static String getDecksId(LinkedList<Integer> player1, LinkedList<Integer> player2){
        StringBuilder sb = new StringBuilder();
        player1.stream().forEach(card -> sb.append(card).append("_"));
        sb.append("|");
        player2.stream().forEach(card -> sb.append(card).append("_"));
        return sb.toString();
    }

}
