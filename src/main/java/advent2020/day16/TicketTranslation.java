package advent2020.day16;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class TicketTranslation {

    public static long calculateScanningError(List<Ticket> tickets, List<TicketRule> ticketRules){

        int invalidSum = tickets.stream()
                              .map(ticket -> ticket.values)
                              .flatMap(Collection::stream)
                                // keep invalid tokens
                              .filter(token -> ticketRules.stream().noneMatch(ticketRuleRange -> ticketRuleRange.test(token)))
                              .reduce(0, Integer::sum);

        return invalidSum;
    }

    public static Long calculateFields(List<Ticket> tickets, List<TicketRule> ticketRules){
        List<Ticket> validTickets = tickets.stream()
                       // keep tickets that all their tokens are valid
                       .filter(ticket -> {
                           for (Integer token : ticket.values) {
                               boolean valid = ticketRules.stream()
                                                          .anyMatch(ticketRuleRange -> ticketRuleRange
                                                                          .test(token));
                               if (!valid) {
                                   return false;
                               }
                           }
                           return true;
                       }).collect(Collectors.toList());

        List<TicketField> ticketFields = new ArrayList<>();
        int numberOfFields = validTickets.get(0).values.size();
        for (int i = 0; i < numberOfFields; i++) {
            int finalI = i;
            ticketFields.add(validTickets.stream()
                                         .map(ticket -> ticket.values.get(finalI))
                                         .collect(Collectors.collectingAndThen(Collectors.toList(), integers -> new TicketField(finalI, integers))));
        }


        Map<String, List<Integer>> ticketRuleToFieldIndexes = new HashMap<>();
        ticketRules.forEach(ticketRule -> {
            ticketFields.forEach(ticketField -> {
                if(ticketField.values.stream().filter(ticketRule.negate()).count() > 0){
                    // this column is not described by this field
                } else {
                    ticketRuleToFieldIndexes.computeIfAbsent(ticketRule.name, ticketRuleName -> new ArrayList<>()).add(ticketField.index);
                }

            });
        });

        // find the ticketRule that has one applicable field
        // assign the field to the rule
        // remove the field from other ticketRuleFieldIndexes
        // repeat

        Map<String, Integer> ticketRuleToField = new HashMap<>();
        while(ticketRuleToField.size() < ticketRules.size()){
            List<Map.Entry<String, List<Integer>>> collect = ticketRuleToFieldIndexes.entrySet().stream()
                                                                                     .sorted(Comparator
                                                                                                     .comparingInt(o -> o
                                                                                                                     .getValue()
                                                                                                                     .size()))
                                                                                     .collect(Collectors.toList());
            // assume size of 1
            assert collect.get(0).getValue().size() == 1;
            Integer fieldIndex = collect.get(0).getValue().get(0);
            ticketRuleToField.put(collect.get(0).getKey(), fieldIndex);
            ticketRuleToFieldIndexes.remove(collect.get(0).getKey());
            ticketRuleToFieldIndexes.values().stream().forEach(integers ->
                            integers.remove(fieldIndex));
        }

        List<Integer> listOfDepartureFieldIndices = ticketRules.stream().filter(TicketRule::isDeparture).map(ticketRule -> ticketRule.name)
                                           .map(ticketRuleToField::get).collect(Collectors.toList());

        return ticketFields.stream().filter(ticketField -> listOfDepartureFieldIndices.contains(ticketField.index))
                           .map(ticketField -> tickets.get(0).values.get(ticketField.index))
                           .map(Long::valueOf)
                           .reduce(1L,  (a, b) -> a * b);

    }
}

class TicketField {
    int index;
    List<Integer> values;

    public TicketField(int index, List<Integer> values) {
        this.index = index;
        this.values = values;
    }
}

class Ticket {
    List<Integer> values;

    public Ticket(List<Integer> values) {
        this.values = values;
    }

    public Ticket(Integer... values){
        this.values = Arrays.asList(values);
    }

    public static Ticket fromString(String input){
        return new Ticket(Stream.of(input.split(",")).map(Integer::valueOf).collect(Collectors.toList()));
    }
}

class TicketRule implements Predicate<Integer>{
    String name;
    TicketRuleRange condition1;
    TicketRuleRange condition2;

    public TicketRule(String name, TicketRuleRange condition1, TicketRuleRange condition2) {
        this.name = name;
        this.condition1 = condition1;
        this.condition2 = condition2;
    }

    public static TicketRule fromString(String input){
        Matcher matcher = Pattern.compile("(?<field>.*): (?<from1>\\d*)-(?<to1>\\d*) or (?<from2>\\d*)-(?<to2>\\d*)").matcher(input);
        matcher.find();
        String name = matcher.group("field");
        Integer from1 = Integer.valueOf(matcher.group("from1"));
        Integer to1 = Integer.valueOf(matcher.group("to1"));
        Integer from2 = Integer.valueOf(matcher.group("from2"));
        Integer to2 = Integer.valueOf(matcher.group("to2"));
        return new TicketRule(name, new TicketRuleRange(from1, to1), new TicketRuleRange(from2, to2));
    }

    @Override
    public boolean test(Integer integer) {
        return condition1.or(condition2).test(integer);
    }

    public boolean isDeparture(){
        return name.startsWith("departure");
    }
}

class TicketRuleRange implements Predicate<Integer> {
    int start;
    int end;

    public TicketRuleRange(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean test(Integer ticketToken) {
        return start <= ticketToken && ticketToken <= end;
    }
}