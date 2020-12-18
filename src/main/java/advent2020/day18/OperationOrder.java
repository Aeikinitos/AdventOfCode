package advent2020.day18;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class OperationOrder {

    public static List<Character> parseRawInput(String rawInput){
        return rawInput.chars()
                .mapToObj(c -> (char) c)
                .filter(character -> !Character.isSpaceChar(character))
                .collect(Collectors.toList());

    }

    public static Long processInput(List<Character> input){

        // if its an operation -> add to temp
        // if its a number -> if you have operation then apply the operation else add to answer
        // if its a parenthesis -> recurse the parenthesis and get the value

        Long answer = 0L;
        OperationCommand command = null;
        for (int i = 0; i < input.size(); i++) {
            Character character = input.get(i);
            Long number = null;
            switch (character){
                case '+':
                    command = new PlusOperationCommand();
                    break;
                case '*':
                    command = new MultiplyOperationCommand();
                    break;
                case ')':
                    break;
                case '(':
                    List<Character> sublist = sublistParenthesis(i, input);
                    number = processInput(sublist);
                    // skip the parenthesis
                    i += sublist.size()+2-1; // +1 for the openning '(', +1 for the closing ')', -1 for the i++
                default:
                    // number
                    if(number == null){
                        // in case we continue from parethesis
                        number = Long.valueOf(String.valueOf(character));
                    }
                    if(command == null){
                        answer += number;
                    } else {
                        answer = command.execute(number, answer);
                        command = null;
                    }
            }
        }
        return answer;
    }

    private static List<Character> sublistParenthesis(int fromIndexExclusive, List<Character> input){
        List<Character> sublist = new ArrayList<>();
        int countOfOpenParenthesis = 0;
        for (int i = fromIndexExclusive+1; i < input.size(); i++) {
            Character character = input.get(i);
            if('(' == character){
                countOfOpenParenthesis++;
            }
            if(')' == character) {
                if(countOfOpenParenthesis == 0){
                    return sublist;
                } else {
                    countOfOpenParenthesis--;
                }
            }
            sublist.add(character);
        }

        // shouldnt reach here since that would mean the input is unbalanced
        return sublist;
    }
}

interface OperationCommand {

    Long execute(Long left, Long right);
}

class PlusOperationCommand implements OperationCommand {
    @Override
    public Long execute(Long left, Long right) {
        return left + right;
    }
}

class MultiplyOperationCommand implements OperationCommand {
    @Override
    public Long execute(Long left, Long right) {
        return left * right;
    }
}

