/*
 * (c) Copyright 2016 Brite:Bill Ltd.
 *
 * 7 Grand Canal Street Lower, Dublin 2, Ireland
 * info@britebill.com
 * +353 1 661 9426
 */
package advent2021.day1;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class SonarSweep {

    protected long calculateIncrementsSlidingWindow(List<Long> input, int slidingWindow){
        return  IntStream.range(slidingWindow, input.size())
                         .filter(index -> input.get(index) - input.get(index - slidingWindow) > 0)
                         .count();
    }
}
