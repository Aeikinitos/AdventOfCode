package advent2020.day7;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class HandyHaversacks {
    private Map<String,List<InnerBagRule>> rules;

    public HandyHaversacks(Map<String, List<InnerBagRule>> rules) {
        this.rules = rules;
    }

    public int countInnerBags(String bag){
        List<InnerBagRule> targetBagRule = rules.get(bag);
        Integer sum = targetBagRule.stream()
                                       .map(this::countInnerBags)
                                       .reduce(0, Integer::sum);

        return sum;
    }

    public int countInnerBags(InnerBagRule rule){
        List<InnerBagRule> innerBagRules = rules.get(rule.getBag());
        Integer sumOfInnerBags = innerBagRules.stream()
                                      .map(innerBagRule -> rule.getCount()
                                                      * countInnerBags(innerBagRule))
                                      .reduce(0, Integer::sum);
        return rule.getCount()+sumOfInnerBags;
    }

    public static Map<String, List<InnerBagRule>> parseRules(List<String> entries){
        Map<String, List<InnerBagRule>> rules = new HashMap<>();
        for (String entry : entries) {
            String[] split = entry.split(" ");
            String bagName = split[0] + " " + split[1];

            List<InnerBagRule> innerRules = new ArrayList<>();
            if(!entry.contains("no other bags")){
                // outerbag takes 2 token, 'bags contain' take two more. start from 4th, read 3 tokens and skip one('bags') each time
                // 0, 1,        2       3       4      5,6
                // <outerBag> bags contain <amount> <innerBag>[, <amount> <innerBag>].
                for (int i = 4; i < split.length; i += 4) {
                    int amount = Integer.valueOf(split[i]);
                    String innerBag = split[i+1] +" "+ split[i+2];
                    innerRules.add(new InnerBagRule(amount, innerBag));
                }
            }
            rules.put(bagName, innerRules);

        }

        return rules;
    }

}


