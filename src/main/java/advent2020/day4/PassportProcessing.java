package advent2020.day4;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class PassportProcessing {

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
        StringBuilder sb = new StringBuilder();
        long count = 0;
        for (String imported : passportsImport){
            sb.append(imported);
            if(imported.equals("")){
                if( testPassport(sb.toString())){
                    count ++;
                } else {
                    System.out.println(sb.toString());
                }
                sb = new StringBuilder();
            }

        }
        // test the last entry since the last one wont have a trailing empty line
        if(testPassport(sb.toString())){
            count++;
        }
        return count;
    }
}


class Passport {

    /*
    ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
byr:1937 iyr:2017 cid:147 hgt:183cm
     */
    public boolean isValid(){
        return true;
    }

    public static Passport fromString(String input){
        return null;
    }
}
