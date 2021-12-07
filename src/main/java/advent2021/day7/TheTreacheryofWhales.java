package advent2021.day7;

import com.google.common.math.Quantiles;

import java.util.List;
import java.util.stream.IntStream;

public class TheTreacheryofWhales {

    protected long calculateFuel(List<Integer> positions){
        Integer target = (int) Quantiles.median().compute(positions);
        return positions.stream().mapToLong(position -> Math.abs(position-target)).sum();
    }

    protected int calculateIncrementalFuelBruteForce(List<Integer> positions) {
        return IntStream.range(0,1000).map(targetCandidate ->
                        positions.stream()
                                 .map(crabPosition -> crabPosition-targetCandidate)
                                 .map(Math::abs)
                                 .map(this::fuelCost)
                                 .reduce(0, Integer::sum)).min().getAsInt();
    }

    protected int calculateIncrementalFuel(List<Integer> positions){
        double target = positions.stream().mapToInt(value -> value).average().orElse(0L);

        return Math.min(
                        calculateIncrementalFuelWithTarget(positions, (int)Math.floor(target)),
                        calculateIncrementalFuelWithTarget(positions, (int)Math.ceil(target)));
    }

    private int calculateIncrementalFuelWithTarget(List<Integer> positions, int target){
        return positions.stream()
                        .mapToInt(Integer::intValue)
                        .map(position -> position-target)
                        .map(Math::abs)
                        .map(this::fuelCost)
                        .reduce(0, Integer::sum);
    }

    private int fuelCost(int n){
        return ((1+n) * n) / 2;
    }


}
