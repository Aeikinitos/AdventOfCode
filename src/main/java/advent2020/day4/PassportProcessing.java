package advent2020.day4;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Splitter;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class PassportProcessing {

    /* Part 1 */
    Predicate<String> containsECL = input -> input.contains("ecl:");
    Predicate<String> containsPID = input -> input.contains("pid:");
    Predicate<String> containsEYR = input -> input.contains("eyr:");
    Predicate<String> containsHCL = input -> input.contains("hcl:");
    Predicate<String> containsBYR = input -> input.contains("byr:");
    Predicate<String> containsIYR = input -> input.contains("iyr:");
    Predicate<String> containsHGT = input -> input.contains("hgt:");

    Predicate<String> isValidPassport = input -> containsECL
                                                    .and(containsPID)
                                                    .and(containsEYR)
                                                    .and(containsHCL)
                                                    .and(containsBYR)
                                                    .and(containsIYR)
                                                    .and(containsHGT)
                                                    .test(input);

    public boolean testPassport(String passport){
        return isValidPassport.test(passport);
    }

    public long testPassportsList(List<String> passportsImport){
        long count = 0;
        for (String imported : passportsImport){
            if( testPassport(imported)){
                count ++;
            } else {
                System.out.println(imported);
            }
        }
        return count;
    }

    /* Part 2 */

    Predicate<Map.Entry<String, String>> validPassportEntry = fieldKeyFieldValue -> Fields.fromString(fieldKeyFieldValue.getKey()).test(fieldKeyFieldValue.getValue());


    public boolean testPassportAndFieldValidity(String passport) {
        return Splitter.onPattern("\\s+").withKeyValueSeparator(":")
                       .split(passport.replaceAll("\\\\n", " ")).entrySet().stream()
                       .filter(validPassportEntry.negate()).count() == 0;
    }

    public long testPassportsListPart2(List<String> passportsImport){
        long count = 0;
        for (String imported : passportsImport){
            if(testPassport(imported) && testPassportAndFieldValidity(imported)){
                System.out.println("'"+imported+"'");
                count ++;
            }
        }
        return count;
    }
}

enum Fields {

    BYR("byr", input -> 1920 <= Long.valueOf(input) && Long.valueOf(input) <= 2002),
    IYR("iyr", input -> 2010 <= Long.valueOf(input) && Long.valueOf(input) <= 2020),
    EYR("eyr", input -> 2020 <= Long.valueOf(input) && Long.valueOf(input) <= 2030),
    HCL("hcl", input -> Pattern.compile("#[a-f0-9]{6}").matcher(input).find()), //a # followed by exactly six characters 0-9 or a-f.
    ECL("ecl", input -> Pattern.compile("amb|blu|brn|gry|grn|hzl|oth").matcher(input).find()),  //exactly one of: amb blu brn gry grn hzl oth.
    PID("pid", input -> Pattern.compile("^\\d{9}$").matcher(input).find()), //a nine-digit number, including leading zeroes.
    CID("cid", input -> true),
    HGT("hgt", input -> {
        Matcher matcher = Pattern.compile("(?<height>\\d*)(?<metric>in|cm)").matcher(input);
        if( !matcher.find())
        {
            return false;
        }
        long height = Long.valueOf(matcher.group("height"));
        boolean metric = matcher.group("metric").equals("cm");
        return metric ? 150 <= height && height <= 193 : 59 <= height && height <= 76;
    });

    private static final Map<String, Fields> BY_NAME = new HashMap<>();

    static {
        for (Fields e : values()) {
            BY_NAME.put(e.key, e);
        }
    }

    private final String key;
    private final Predicate<String> isValid;

    public boolean test(String input){
        return isValid.test(input);
    }

    Fields(String key, Predicate<String> isValid) {
        this.key = key;
        this.isValid = isValid;
    }

    public static Fields fromString(String name) {
        return BY_NAME.get(name);
    }
}
