package advent2021.day5;

import java.util.Arrays;
import java.util.List;

public class Lanternfish {

    protected long calculateFishPopulation(List<Integer> lenterfish, int days){
        long[] lenterFish = new long[9];
        lenterfish.stream().forEach(integer -> lenterFish[integer] += 1);

        for (int day = 0; day < days; day++) {
            long pregnant = lenterFish[0];
            for (int i = 0; i < 8; i++) {
                lenterFish[i] = lenterFish[i+1];
            }
            lenterFish[6] += pregnant;
            lenterFish[8] = pregnant;
        }
        return Arrays.stream(lenterFish).sum();
    }
}
