package advent2020.day13;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
class ShuttleSearchTest {

    @Test
    void findBusID() {
        List<Integer> busIds = Arrays.asList(7,13,59,31,19);
        ShuttleSearch.findBusID(busIds, 939);
    }

    @Test
    void part1(){
        List<String> lines = readLines("src/test/resources/advent2020/day13/input").stream().collect(Collectors.toList());
        Integer earliestArrival = Integer.valueOf(lines.get(0));
        List<Integer> busIds = Stream.of(lines.get(1).split(",")).filter(s -> !"x".equals(s)).map(Integer::valueOf)
                                      .collect(Collectors.toList());

        ShuttleSearch.findBusID(busIds, earliestArrival);
    }

    @Test
    void sample1() {
        List<String> busIds = Arrays.asList("7","13","x","x","59","x","31","19");
        long earliestTimeStamp = ShuttleSearch.findEarliestTimeStamp(busIds);

        Assertions.assertEquals(1068781L,earliestTimeStamp);
    }

    @Test
    void sample2() {
        List<String> busIds = Arrays.asList("17","x","13","19");
        long earliestTimeStamp = ShuttleSearch.findEarliestTimeStamp(busIds);

        Assertions.assertEquals(3417L,earliestTimeStamp);
    }

    @Test
    void sample3() {
        List<String> busIds = Arrays.asList("67","7","59","61");
        long earliestTimeStamp = ShuttleSearch.findEarliestTimeStamp(busIds);

        Assertions.assertEquals(754018L,earliestTimeStamp);
    }

    @Test
    void sample4() {
        List<String> busIds = Arrays.asList("67","x","7","59","61");
        long earliestTimeStamp = ShuttleSearch.findEarliestTimeStamp(busIds);

        Assertions.assertEquals(779210L,earliestTimeStamp);
    }

    @Test
    void sample5() {
        List<String> busIds = Arrays.asList("67","7","x","59","61");
        long earliestTimeStamp = ShuttleSearch.findEarliestTimeStamp(busIds);

        Assertions.assertEquals(1261476L,earliestTimeStamp);
    }

    @Test
    void sample6() {
        List<String> busIds = Arrays.asList("1789","37","47","1889");
        long earliestTimeStamp = ShuttleSearch.findEarliestTimeStamp(busIds);

        Assertions.assertEquals(1202161486L,earliestTimeStamp);
    }

    @Test
    void sample1Combo() {
        List<String> busIds = Arrays.asList("7","13","x","x","59","x","31","19");
        List<Combo> busIdsCombo = new ArrayList<>();
        for (int i = 0; i < busIds.size(); i++) {
            if("x".equals(busIds.get(i))) continue;
            busIdsCombo.add(new Combo(Integer.valueOf(busIds.get(i)), i));
        }
        long earliestTimeStamp = ShuttleSearch.findEarliestTimeForSchedule(busIdsCombo);

        Assertions.assertEquals(1068781L,earliestTimeStamp);
    }

    @Test
    void sample2Combo() {
        List<String> busIds = Arrays.asList("17","x","13","19");
        List<Combo> busIdsCombo = new ArrayList<>();
        for (int i = 0; i < busIds.size(); i++) {
            if("x".equals(busIds.get(i))) continue;
            busIdsCombo.add(new Combo(Integer.valueOf(busIds.get(i)), i));
        }
        long earliestTimeStamp = ShuttleSearch.findEarliestTimeForSchedule(busIdsCombo);

        Assertions.assertEquals(3417L,earliestTimeStamp);
    }

    @Test
    void sample3Combo() {
        List<String> busIds = Arrays.asList("67","7","59","61");
        List<Combo> busIdsCombo = new ArrayList<>();
        for (int i = 0; i < busIds.size(); i++) {
            if("x".equals(busIds.get(i))) continue;
            busIdsCombo.add(new Combo(Integer.valueOf(busIds.get(i)), i));
        }
        long earliestTimeStamp = ShuttleSearch.findEarliestTimeForSchedule(busIdsCombo);

        Assertions.assertEquals(754018L,earliestTimeStamp);
    }

    @Test
    void sample4Combo() {
        List<String> busIds = Arrays.asList("67","x","7","59","61");
        List<Combo> busIdsCombo = new ArrayList<>();
        for (int i = 0; i < busIds.size(); i++) {
            if("x".equals(busIds.get(i))) continue;
            busIdsCombo.add(new Combo(Integer.valueOf(busIds.get(i)), i));
        }
        long earliestTimeStamp = ShuttleSearch.findEarliestTimeForSchedule(busIdsCombo);

        Assertions.assertEquals(779210L,earliestTimeStamp);
    }

    @Test
    void sample5Combo() {
        List<String> busIds = Arrays.asList("67","7","x","59","61");
        List<Combo> busIdsCombo = new ArrayList<>();
        for (int i = 0; i < busIds.size(); i++) {
            if("x".equals(busIds.get(i))) continue;
            busIdsCombo.add(new Combo(Integer.valueOf(busIds.get(i)), i));
        }
        long earliestTimeStamp = ShuttleSearch.findEarliestTimeForSchedule(busIdsCombo);

        Assertions.assertEquals(1261476L,earliestTimeStamp);
    }

    @Test
    void sample6Combo() {
        List<String> busIds = Arrays.asList("1789","37","47","1889");
        List<Combo> busIdsCombo = new ArrayList<>();
        for (int i = 0; i < busIds.size(); i++) {
            if("x".equals(busIds.get(i))) continue;
            busIdsCombo.add(new Combo(Integer.valueOf(busIds.get(i)), i));
        }
        long earliestTimeStamp = ShuttleSearch.findEarliestTimeForSchedule(busIdsCombo);

        Assertions.assertEquals(1202161486L,earliestTimeStamp);
    }

    @Test
    void part2(){
        List<String> lines = readLines("src/test/resources/advent2020/day13/input").stream().collect(Collectors.toList());
        List<String> busIds = Stream.of(lines.get(1).split(",")).collect(Collectors.toList());

        List<Combo> busIdsCombo = new ArrayList<>();
        for (int i = 0; i < busIds.size(); i++) {
            if("x".equals(busIds.get(i))) continue;
            busIdsCombo.add(new Combo(Integer.valueOf(busIds.get(i)), i));
        }
        long earliestTimeStamp = ShuttleSearch.findEarliestTimeForSchedule(busIdsCombo);
        System.out.println(earliestTimeStamp);
    }



    /* *************************************** */

    protected List<String> readLines(String filename){
        List<String> lines = Collections.emptyList();
        try
        {
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            sc.useDelimiter("\\Z");

            lines = Arrays.stream(sc.next().split("\\n")).collect(Collectors.toList());

        }
        catch (IOException e)
        {
            // do something
            e.printStackTrace();
        }
        return lines;
    }
}