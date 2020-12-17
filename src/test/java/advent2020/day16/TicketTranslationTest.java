package advent2020.day16;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
class TicketTranslationTest {

    @Test
    void findInvalid() {

        List<Ticket> tickets = Arrays.asList(
                        new Ticket(7,3,47),
                        new Ticket(40,4,50),
                        new Ticket(55,2,20),
                        new Ticket(38,6,12)
        );
        List<TicketRule> ticketRules = Arrays.asList(
                        new TicketRule("class", new TicketRuleRange(1,3), new TicketRuleRange(5,7)),
                        new TicketRule("row", new TicketRuleRange(6,11), new TicketRuleRange(33,44)),
                        new TicketRule("seat", new TicketRuleRange(13,40), new TicketRuleRange(45,50))
        );
        long invalid = TicketTranslation.findInvalid(tickets, ticketRules);

        Assertions.assertEquals(71, invalid);
    }

    @Test
    void part1() {

        List<String> lines = readLines("src/test/resources/advent2020/day16/input");
        String[] ticketRulesRaw = lines.get(0).split("\\n");
        List<TicketRule> ticketRules = Arrays.stream(ticketRulesRaw)
                                             .map(TicketRule::fromString)
                                             .collect(Collectors.toList());

        String[] ticketsRawInput = lines.get(2).split("\\n");
        List<Ticket> tickets = Arrays.stream(ticketsRawInput,1,ticketsRawInput.length)
                                             .map(Ticket::fromString)
                                             .collect(Collectors.toList());

        long invalid = TicketTranslation.findInvalid(tickets, ticketRules);

        System.out.println("Part1 :"+invalid);

    }

    @Test
    void skipInvalidSample() {

        List<Ticket> tickets = Arrays.asList(
                        new Ticket(11,12,13),
                        new Ticket(3,9,18),
                        new Ticket(15,1,5),
                        new Ticket(5,14,9)
        );
        List<TicketRule> ticketRules = Arrays.asList(
                        new TicketRule("departure class", new TicketRuleRange(0,1), new TicketRuleRange(4,19)),
                        new TicketRule("departure row", new TicketRuleRange(0,5), new TicketRuleRange(8,19)),
                        new TicketRule("seat", new TicketRuleRange(0,13), new TicketRuleRange(16,19))
        );
        Long product = TicketTranslation.skipInvalid(tickets, ticketRules);

        Assertions.assertEquals(11*12, product);
    }

    @Test
    void part2() {

        List<String> lines = readLines("src/test/resources/advent2020/day16/input");
        String[] ticketRulesRaw = lines.get(0).split("\\n");
        List<TicketRule> ticketRules = Arrays.stream(ticketRulesRaw)
                                             .map(TicketRule::fromString)
                                             .collect(Collectors.toList());

        String yourTicketRaw = lines.get(1).split("\\n")[1];
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(Ticket.fromString(yourTicketRaw));

        String[] ticketsRawInput = lines.get(2).split("\\n");
        Arrays.stream(ticketsRawInput,1,ticketsRawInput.length)
                        .map(Ticket::fromString)
                        .collect(Collectors.collectingAndThen(Collectors.toList(), tickets::addAll));

        Long product = TicketTranslation.skipInvalid(tickets, ticketRules);

        System.out.println("Part2 :"+product);

    }



    /* *************************************** */

    protected List<String> readLines(String filename){
        List<String> lines = Collections.emptyList();
        try
        {
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            sc.useDelimiter("\\Z");

            lines = Arrays.stream(sc.next().split("\\n\\n")).collect(Collectors.toList());

        }
        catch (IOException e)
        {
            // do something
            e.printStackTrace();
        }
        return lines;
    }
}