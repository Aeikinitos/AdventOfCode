package advent2020.day10;

import java.util.ArrayList;
import java.util.List;
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
                if(countOfStraightOnes == 4){
                    product *= 7;
                }
                if(countOfStraightOnes == 3){
                    product *= 4;
                }
                if(countOfStraightOnes == 2){
                    product *= 2;
                }
                countOfStraightOnes = 0;
            }
        }

        return product;
    }
}
