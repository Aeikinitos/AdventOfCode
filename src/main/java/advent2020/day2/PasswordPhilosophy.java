package advent2020.day2;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class PasswordPhilosophy {

    public long countValid(List<PolicyPassword> policyPasswordList){
        return policyPasswordList.stream().filter(PolicyPassword::isValid).count();
    }

    public boolean testPolicy(String input, Policy policy){
        return policy.test(input);
    }
}

class PolicyPassword {
    String password;
    Policy policy;

    public PolicyPassword(String password, Policy policy) {
        this.password = password;
        this.policy = policy;
    }

    public boolean isValid() {
        return policy.test(password);

    }

    public static PolicyPassword fromString(String string, PolicyFactory policy){

        Pattern pattern = Pattern.compile("(?<min>\\d*)-(?<max>\\d*) (?<character>[a-z]): (?<password>.*)");
        Matcher matcher = pattern.matcher(string);
        matcher.find();//assume input always plays nice :D
        return new PolicyPassword(
                        matcher.group("password"),
                        policy.create(
                                        Integer.valueOf(matcher.group("min")),
                                        Integer.valueOf(matcher.group("max")),
                                        matcher.group("character").charAt(0)));
    }

}
interface PolicyFactory {
    Policy create(int minOccurence, int maxOccurence, Character character);
}

class Policy implements Predicate<String> {
    int minOccurence;
    int maxOccurence;
    Character character;

    public Policy(int minOccurence, int maxOccurence, Character character) {
        this.minOccurence = minOccurence;
        this.maxOccurence = maxOccurence;
        this.character = character;
    }

    @Override
    public boolean test(String s) {
        long count = s.chars().filter(value -> value == character).count();
        return minOccurence <= count && count <= maxOccurence;
    }
}

class NewPolicy extends Policy {

    public NewPolicy(int minOccurence, int maxOccurence, Character character) {
        super(minOccurence, maxOccurence, character);
    }

    @Override
    public boolean test(String s) {
        // -1 to adjust to zero based
        return s.charAt(minOccurence-1)==character ^ s.charAt(maxOccurence-1)==character ;
    }
}
