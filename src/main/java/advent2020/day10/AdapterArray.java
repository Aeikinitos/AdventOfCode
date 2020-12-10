package advent2020.day10;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class AdapterArray {

    public static List<Integer> differences(List<Integer> input){
        input.sort(Integer::compareTo);
        List<Integer> diff = new ArrayList<>();
        diff.add(input.get(0)-0);
        for (int i = 1; i < input.size(); i++) {
            diff.add(input.get(i)-input.get(i-1));
        }
        // add adapter diff
        diff.add(3);
        return diff;
    }

    public static long product(List<Integer> differences){
        AtomicInteger count1 = new AtomicInteger(0);
        AtomicInteger count3 = new AtomicInteger(0);
        differences.forEach(integer -> {if(integer.equals(1)) count1.incrementAndGet();else count3.getAndIncrement();});

        return count1.get()*count3.get();
    }

    public static long combination(List<Integer> differences){
        int countOfStraightOnes = 0;
        long product = 1;

        for (int i = 0; i < differences.size(); i++) {
            if(differences.get(i).equals(1)){
                countOfStraightOnes++;
            } else {
                // only calculate straight ones since once you find a diff of 3 any combination breaks
                product *= calculateProductCoefficient(countOfStraightOnes);
                countOfStraightOnes = 0;
            }
        }

        return product;
    }


    private static Map<Integer, Integer> alreadyCalculated = new HashMap<>();
    /*
        the first 3 are calculated on paper
        the following combinations will follow the pattern:
        remove the previous number and repeat the pattern as if countOfStraightOnes-1
        remove the last two numbers and repeat the pattern as if countOfStraightOnes-2
        plus the combinations of all numbers till this point
     */
    private static int calculateProductCoefficient(int countOfStraightOnes){
        if(countOfStraightOnes<=1){
            return 1;
        }
        Integer calculatedResult = alreadyCalculated.get(countOfStraightOnes);
        if(calculatedResult != null){
            return calculatedResult;
        }
        if(countOfStraightOnes == 2){
            return 2;
        }
        if(countOfStraightOnes == 3){
            return 4;
        }
        if(countOfStraightOnes == 4){
            return 7;
        }
        calculatedResult = calculateProductCoefficient(countOfStraightOnes - 1)
                        + calculateProductCoefficient(countOfStraightOnes - 2)
                        + calculateProductCoefficient(countOfStraightOnes - 3);
        alreadyCalculated.put(countOfStraightOnes, calculatedResult);
        return calculatedResult;
    }
}
