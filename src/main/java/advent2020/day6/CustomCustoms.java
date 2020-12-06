package advent2020.day6;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class CustomCustoms {

    public static int countYes(String groupAnswer){
        return groupAnswer.chars().distinct().filter(value -> value != '\n').map(e->1).sum();
    }

    public static int sumOfYes(List<String> groupAnswers){
        return groupAnswers.stream().map(CustomCustoms::countYes).mapToInt(Integer::intValue).sum();
    }

    public static int countYesEveryone(String groupAnswers){
        String[] split = groupAnswers.split("\\n");
        List<Character> common = split[0].chars().mapToObj(e -> (char)e).collect(Collectors.toList());
        for (String groupAnswer : split) {
            common.retainAll(groupAnswer.chars().mapToObj(e -> (char)e).collect(Collectors.toList()));
        }

        return common.size();
    }

    public static int sumOfYesEveryone(List<String> groupAnswers){
        return groupAnswers.stream().map(CustomCustoms::countYesEveryone).mapToInt(Integer::intValue).sum();
    }
}
