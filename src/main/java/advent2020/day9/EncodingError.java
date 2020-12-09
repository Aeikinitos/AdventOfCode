package advent2020.day9;

import java.util.*;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class EncodingError {

    public static boolean validate(Queue<Long> queue, Long sum){
        Set visited = new HashSet();

        for (Long queuedNumber : queue) {
            Long additionalNumberNeeded = sum-queuedNumber;
            if(visited.contains(additionalNumberNeeded)){
                return true;
            } else {
                visited.add(queuedNumber);
            }
        }
        return false;
    }

    public static Long iterateUntilNotValidated(List<Long> list, int queueLength) {
        return list.get(iterateUntilNotValidatedIndex(list, queueLength));
    }

    public static int iterateUntilNotValidatedIndex(List<Long> list, int queueLength) {
        Queue queue = new LinkedList(list.subList(0,queueLength));

        int i = queueLength-1;
        while(checkAndUpdate(queue, list.get(++i)));

        return i;
    }

    public static boolean checkAndUpdate(Queue<Long> queue, Long sum){
        if(validate(queue, sum)){
            queue.poll();
            queue.add(sum);
            return true;
        } else {
            return false;
        }
    }

    // Part 2

    public static Long sumOfSmallAndLargeInContiguousSet(List<Long> list, int queueLength){
        int indexOfSum = iterateUntilNotValidatedIndex(list, queueLength);

        Long targetSum = list.get(indexOfSum);

        for (int startIndex = 0; startIndex < indexOfSum; startIndex++) {

            Long sum = list.get(startIndex);

            for (int endIndex = startIndex+1; endIndex < indexOfSum; endIndex++) {
                sum += list.get(endIndex);

                if(sum.equals(targetSum)){
                    Long min = list.subList(startIndex, endIndex+1).stream().min(Long::compareTo).get();
                    Long max = list.subList(startIndex, endIndex+1).stream().max(Long::compareTo).get();
                    return min+max;
                }

                if(sum > targetSum){
                    break;
                }
            }
        }

        return -1L;
    }
}
