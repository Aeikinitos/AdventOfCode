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
        Optional<String> s = ruleIds.get(0).consume(input).filter(String::isEmpty);
//        s.ifPresent(s1 ->  System.out.println(input));
        return s.isPresent();
    }

    @Data
    abstract class MessageRule {
        Integer id;

        /*
        consumes any possible input and returns the remaining
        A rule that matches "a" with input will return
        input | return
        "a"     Optional("")
        "b"     Optional.empty()
        "ab"    Optional("b")
         */
        public abstract Optional<String> consume(String input);
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
        public Optional<String> consume(String input) {
            if(input.startsWith(matcher)){
                return Optional.of(input.substring(matcher.length()));
            }
            return Optional.empty();
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
        // 0: 4 1 5
        // 1: 2 3 | 3 2 -> aaab, aaba, bbab, bbba | abaa, abbb, baaa, babb
        // 2: 4 4 | 5 5 (aa, bb)
        // 3: 4 5 | 5 4 (ab, ba)
        // 4: a
        // 5: b
        // Traverse depth first
        @Override
        public Optional<String> consume(String input) {

            for (List<Integer> option : orMessageRules) {
                Optional<String> remaining = Optional.of(input);
                boolean invalidOption= false;

                for (Integer ruleId : option) {
                    MessageRule rule = ruleIds.get(ruleId);
                    String remainingStrin = remaining.get();
                    remaining = rule.consume(remainingStrin);
                    if(!remaining.isPresent()){
                        // option is invalid
                        invalidOption = true;
                        break;
                    }
                }
                if(!invalidOption){
                    return remaining;
                }
            }
            return Optional.empty();
        }

        public void addOption(String input){
            if(input==null) {
                return;
            }
            orMessageRules.add(Stream.of(input.split(" ")).map(Integer::valueOf).collect(Collectors.toList()));
        }

    }
}

