package advent2021.day1;

import java.util.List;
import java.util.stream.IntStream;

public class SonarSweep {

    protected long calculateIncrementsSlidingWindow(List<Long> input, int slidingWindow){
        return  IntStream.range(slidingWindow, input.size())
                         .filter(index -> input.get(index) - input.get(index - slidingWindow) > 0)
                         .count();
    }
}
