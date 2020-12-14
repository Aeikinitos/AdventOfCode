package advent2020.day14;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class DockingData {

    private String mask;
    private Map<String, String> memory = new HashMap<>();

    public static String applyAssignment(String value, String mask){


        char[] maskChars = mask.toCharArray();
        char[] result = new char[mask.length()];
        char[] valueChars = value.toCharArray();

        // for each bit in value (in reverse order since its right most significant
        // if mask == X, bit = inputBit
        // else bit = mask
        for (int i = 0; i < maskChars.length; i++) {
            if('X' == maskChars[maskChars.length-1-i]) {
                result[maskChars.length-1-i] = valueChars.length-1-i < 0 ? '0' : valueChars[valueChars.length-1-i];
            } else {
                result[maskChars.length-1-i] = maskChars[maskChars.length-1-i];
            }
        }

        return String.valueOf(result);
    }

    public long processInput(List<String> lines){
        for (String line : lines) {
            if(line.startsWith("mask =")){
                // set mask
                this.mask = line.substring(7);
                continue;
            }
            //mem[8617] = 182201
            Pattern pattern = Pattern.compile("mem\\[(?<address>\\d*)\\] = (?<value>\\d*)");
            Matcher matcher = pattern.matcher(line);
            if(!matcher.find()){
                return -1L;
            }
            String address = matcher.group("address");
            String value = matcher.group("value");

            String result = DockingData.applyAssignment(Long.toBinaryString(Long.valueOf(value)), this.mask);
            this.memory.put(address, result);
        }

        long sum = this.memory.values().stream().map(String::trim).map(s -> Long.parseLong(s, 2)).reduce(0L, Long::sum);
        return sum;
    }

    /*
    return list of address that the value needs to be applied
     */
    public static List<String> applyAssignmentV2(String value, String mask){


        char[] maskChars = mask.toCharArray();
        char[] result = new char[mask.length()];
        char[] valueChars = value.toCharArray();

        // for each bit in value
        // if mask == X, bit = inputBit
        // else bit = mask
        for (int i = 0; i < maskChars.length; i++) {
            if('X' == maskChars[maskChars.length-1-i]) {
                result[maskChars.length-1-i] = 'X';
            } else {
                result[maskChars.length-1-i] = (char) (maskChars[maskChars.length - 1 - i] | (valueChars.length-1-i < 0 ? '0' : valueChars[valueChars.length-1-i]));
            }
        }


        List<StringBuilder> results = new ArrayList<>();
        results.add(new StringBuilder());

        for (char c : result) {
            if (c == '0' || c == '1') {
                results.forEach(s -> s.append(c));
            } else {
                results.forEach(s -> s.append('0'));
                List<StringBuilder> branch = results
                                .stream()
                                .map(StringBuilder::new)
                                .map(stringBuilder -> stringBuilder.replace(stringBuilder.length()-1,stringBuilder.length(), "1"))
                                .collect(Collectors.toList());
                results.addAll(branch);
            }
        }

        return results.stream().map(StringBuilder::toString).collect(Collectors.toList());
    }

    public long processInputV2(List<String> lines){
        for (String line : lines) {
            if(line.startsWith("mask =")){
                // set mask
                this.mask = line.substring(7);
                continue;
            }

            Pattern pattern = Pattern.compile("mem\\[(?<address>\\d*)\\] = (?<value>\\d*)");
            Matcher matcher = pattern.matcher(line);
            if(!matcher.find()){
                return -1L;
            }
            String address = matcher.group("address");
            String value = matcher.group("value");

            List<String> addresses = DockingData.applyAssignmentV2(Long.toBinaryString(Long.valueOf(address)), this.mask);

            addresses.forEach(s -> this.memory.put(s, value));
        }

        long sum = this.memory.values().stream().map(String::trim).map(s -> Long.parseLong(s)).reduce(0L, Long::sum);
        return sum;
    }
}
