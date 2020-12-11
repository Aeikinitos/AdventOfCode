package advent2020.day11;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class Adjucent {

    public static boolean sitDown(char[][] seating, int seatRow, int seatColumn) {
        return countAdjucent(seating, seatRow, seatColumn) == 0;
    }

    public static boolean standUp(char[][] seating, int seatRow, int seatColumn) {
        return countAdjucent(seating, seatRow, seatColumn) >= 4;
    }

    private static int countAdjucent(char[][] seating, int seatRow, int seatColumn){

        int rowLength = seating.length;
        int columnLength = seating[0].length;
        int count = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(i==1&&j==1) continue; // current seat
                if(seatRow-1+i < 0 || seatColumn-1+j < 0 || seatRow-1+i >= rowLength || seatColumn-1+j >= columnLength) continue; // out of bounds
                if('#' == seating[seatRow-1+i][seatColumn-1+j]){
                    count++;
                }
            }
        }

        return count;
    }

}
