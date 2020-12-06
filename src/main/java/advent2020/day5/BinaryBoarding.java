package advent2020.day5;

import javafx.util.Pair;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class BinaryBoarding {

    public static Pair<Integer, Integer> getRowSeatPair(String partitioning) {

        int rowStart = 0;
        int rowEnd = 127;

        char[] rowPart = new char[7];
        partitioning.getChars(0, 7, rowPart, 0);
        for (Character c : rowPart) {
            if (c == 'F') {
                rowEnd = rowEnd - (rowEnd - rowStart + 1) / 2;
            } else {
                rowStart = rowStart + (rowEnd - rowStart + 1) / 2;
            }
        }

        int seatNumberStart = 0;
        int seatNumberEnd = 7;

        char[] seatPart = new char[3];
        partitioning.getChars(7, 10, seatPart, 0);
        for (Character c : seatPart) {
            if (c == 'L') {
                seatNumberEnd = seatNumberEnd - (seatNumberEnd - seatNumberStart + 1) / 2;
            } else {
                seatNumberStart = seatNumberStart + (seatNumberEnd - seatNumberStart + 1) / 2;
            }
        }


        return new Pair<>(rowStart, seatNumberStart);
    }

    public static int getSeatID(Pair<Integer, Integer> rowSeat) {
        return rowSeat.getKey() * 8 + rowSeat.getValue();
    }

    public static void findSeatID(List<String> lines){
        List<Pair<Integer,Integer>> allSeats = new ArrayList<>();
        for (int i = 0; i <128; i++) {
            for (int j = 0; j < 8; j++) {
                allSeats.add(new Pair<Integer,Integer>(i,j));
            }
        }

        allSeats.removeAll(lines.stream().map(BinaryBoarding::getRowSeatPair).collect(Collectors.toList()));

        // No need to remove first and last rows since the next step will remove them anyhow
        // allSeats = allSeats.stream().filter(rowSeatPair -> rowSeatPair.getKey()!=0).filter(rowSeatPair -> rowSeatPair.getKey()!=127).collect(Collectors.toList());

        List<Integer> keys = allSeats.stream().map(Pair::getKey).collect(Collectors.toList());
        allSeats.stream().filter(rowSeatPair -> Collections.frequency(keys, rowSeatPair.getKey()) ==1).forEach(System.out::println);
    }
}