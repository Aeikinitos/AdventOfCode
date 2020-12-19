package advent2020.day19;

import lombok.Data;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class MonsterMessages {

    private static final Pattern SUB_RULE_PATTERN = Pattern.compile("(?<ruleId>\\d*): (?<option1>[\\d ]+)(\\| (?<option2>[\\d ]+))?");
    public Map<Integer, MessageRule> ruleIds = new HashMap<>();

    public void parseRawInput(List<String> rawInput){
        for (String line : rawInput) {
            Matcher matcher = SUB_RULE_PATTERN.matcher(line);
            if(matcher.find()){
                SubMessageRule subMessageRule = new SubMessageRule(matcher);
                ruleIds.put(subMessageRule.getId(), subMessageRule);
            } else {
                ValueMessageRule valueMessageRule = new ValueMessageRule(line);
                ruleIds.put(valueMessageRule.getId(), valueMessageRule);
            }
        }
    }

    public boolean processInput(String input){
        return ruleIds.get(0).branch(input).stream().filter(String::isEmpty).count() > 0;
    }

    @Data
    abstract class MessageRule {
        Integer id;

        public abstract List<String> branch(String input);
    }

    @Data
    class ValueMessageRule extends MessageRule {
        String matcher;

        public ValueMessageRule(String line) {
            String[] split = line.split(":");
            setId(Integer.valueOf(split[0]));
            setMatcher(split[1].substring(split[1].length() - 2, split[1].length() - 1));
        }

        @Override
        public List<String> branch(String input) {
            if(input.startsWith(matcher)){
                return Arrays.asList(input.substring(matcher.length()));
            }
            return new ArrayList<>();
        }
    }

    @Data
    class SubMessageRule extends MessageRule {
        List<List<Integer>> orMessageRules = new ArrayList<>();

        SubMessageRule(Matcher matcher){
            setId(Integer.valueOf(matcher.group("ruleId")));
            addOption(matcher.group("option1")); // ie 4 1
            addOption(matcher.group("option2"));
        }

        public List<String> branch(String input) {
            // for this rule to completely consume the input, consider both option for which
            // rule 1 can consume multiple ways and leave multiple remainders
            // which the rule 2 consumes
            // if any of the variations is empty, it means the combo rule 1&2 can consume it fully

            List<String> remainingVariants = new ArrayList<>();
            for (List<Integer> option : orMessageRules) {
                List<String> branches = Arrays.asList(input);
                for (Integer ruleId : option) {
                    List<String> nextBranches = new ArrayList<>();
                    for (String branch : branches) {
                        MessageRule rule = ruleIds.get(ruleId);
                        nextBranches.addAll(rule.branch(branch));
                    }
                    branches = nextBranches;
                }
                remainingVariants.addAll(branches);
            }
            return remainingVariants;
        }

        public void addOption(String input){
            if(input==null) {
                return;
            }
            orMessageRules.add(Stream.of(input.split(" ")).map(Integer::valueOf).collect(Collectors.toList()));
        }

    }
}

