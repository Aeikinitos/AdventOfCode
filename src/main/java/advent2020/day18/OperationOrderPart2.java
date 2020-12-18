package advent2020.day18;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class OperationOrderPart2 {

    public static List<String> parseRawInput(String rawInput) {
        return rawInput.chars()
                       .mapToObj(c -> (char) c)
                       .filter(character -> !Character.isSpaceChar(character))
                       .map(String::valueOf)
                       .collect(Collectors.toList());

    }

    public static Long processInput(List<String> input){

        // maxed scenario
        // 1 + 1 + ( 1 + ( 1 + 1 ) + 1 ) + 1 + ( 1 + 1 ) + 1

        int leftPointer = input.indexOf("(");   // 4
        if(leftPointer < 0){
            return calculateInput(input);
        }
        int rightPointer = findClosingParenthesisIndex(input, leftPointer);

        Long middle = processInput(input.subList(leftPointer+1, rightPointer)); // skip parenthesis

        List<String> replacedInput = new ArrayList<>();
        replacedInput.addAll(input.subList(0, leftPointer));
        replacedInput.add(middle.toString());
        replacedInput.addAll(input.subList(rightPointer + 1, input.size()));

        return processInput(replacedInput);
    }

    // Assumes no ()
    private static Long calculateInput(List<String> input){
        Long product = 1L;
        List<String> onlyPlus = new ArrayList<>();
        for (String component : input) {
            if("*".equals(component)){
                product *= sum(onlyPlus);
                onlyPlus.clear();
            } else {
                onlyPlus.add(component);
            }
        }
        return product * sum(onlyPlus);
    }

    private static Long sum(List<String> input){
        return input.stream()
                    .filter(s -> s.matches("[0-9]+"))
                    .map(Long::valueOf)
                    .reduce(0L, Long::sum);
    }


    private static int findClosingParenthesisIndex(List<String> input, int openingParenthesisIndex){
        int countOfOpenParenthesis = 0;
        for (int i = openingParenthesisIndex+1; i < input.size() ; i++) {
            String character = input.get(i);
            if("(".equals(character)){
                countOfOpenParenthesis++;
            }
            if(")".equals(character)) {
                if(countOfOpenParenthesis == 0){
                    return i;
                } else {
                    countOfOpenParenthesis--;
                }
            }
        }

        // this would mean the input is unbalanced
        return 0;
    }
}
