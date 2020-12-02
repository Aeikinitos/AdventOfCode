package advent2020.day1;

import javafx.util.Pair;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class ReportRepair {


    public List<Long> findValuesThatSumTo(Long sum, List<Long> values){

        for (Long element1 : values) {
            if(element1 >= sum){
                continue;
            }

            for (Long element2 : values) {
                if(element1+element2 == sum)
                {
                    System.out.println(element1 + " " + element2);
                    return Arrays.asList(element1, element2);
                }
            }
        }

        return Collections.emptyList();
    }

    public List<Long> find3ValuesThatSumTo(Long sum, List<Long> values){

        for (Long element1 : values) {
            if(element1 >= sum){
                continue;
            }

            for (Long element2 : values) {
                if(element2 >= sum){
                    continue;
                }
                for (Long element3 : values) {

                    if (element1 + element2 +element3 == sum) {
                        System.out.println(element1 + " " + element2 + " " + element3);
                        return Arrays.asList(element1, element2, element3);
                    }
                }
            }
        }

        return Collections.emptyList();
    }

    public List<Long> findValuesThatSumTo_withStreams(Long sum, List<Long> values) {

        values.stream().distinct().filter(element1 -> {
            values.stream().distinct().filter(element2 -> element1 + element2 == sum).forEach(System.out::println);
            return true;
        }).count();

        return Collections.emptyList();
    }
}
