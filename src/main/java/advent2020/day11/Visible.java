package advent2020.day11;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class Visible {

    public static boolean sitDown(char[][] seating, int seatRow, int seatColumn) {
        return countVisible(seating, seatRow, seatColumn) == 0;
    }

    public static boolean standUp(char[][] seating, int seatRow, int seatColumn) {
        return countVisible(seating, seatRow, seatColumn) >= 5;
    }

    private static int countVisible(char[][] seating, int seatRow, int seatColumn){

        int rowLength = seating.length;
        int columnLength = seating[0].length;
        int count = 0;


        // check left for empty or occupied
        for (int i = seatRow-1; i >= 0; i--) {
            if('#' == seating[i][seatColumn]){
                count++;
                break;
            }
            if('L' == seating[i][seatColumn]){
                break;
            }
        }
        // check right for empty or occupied
        for (int i = seatRow+1; i < rowLength; i++) {
            if('#' == seating[i][seatColumn]){
                count++;
                break;
            }
            if('L' == seating[i][seatColumn]){
                break;
            }
        }
        // check up for empty or occupied
        for (int i = seatColumn-1; i >= 0; i--) {
            if('#' == seating[seatRow][i]){
                count++;
                break;
            }
            if('L' == seating[seatRow][i]){
                break;
            }
        }
        // check down for empty or occupied
        for (int i = seatColumn+1; i < columnLength; i++) {
            if('#' == seating[seatRow][i]){
                count++;
                break;
            }
            if('L' == seating[seatRow][i]){
                break;
            }
        }

        // check diagonal leftup for empty or occupied
        for (int i = 1; seatRow-i>=0 && seatColumn-i>=0; i++) {
            if('#' == seating[seatRow-i][seatColumn-i]){
                count++;
                break;
            }
            if('L' == seating[seatRow-i][seatColumn-i]){
                break;
            }
        }
        // check diagonal leftdown for empty or occupied
        for (int i = 1; seatRow+i<rowLength && seatColumn-i>=0; i++) {
            if('#' == seating[seatRow+i][seatColumn-i]){
                count++;
                break;
            }
            if('L' == seating[seatRow+i][seatColumn-i]){
                break;
            }
        }
        // check diagonal rightup for empty or occupied
        for (int i = 1; seatRow-i>=0 && seatColumn+i<columnLength; i++) {
            if('#' == seating[seatRow-i][seatColumn+i]){
                count++;
                break;
            }
            if('L' == seating[seatRow-i][seatColumn+i]){
                break;
            }
        }
        // check diagonal rightdown for empty or occupied
        for (int i = 1; seatRow+i<rowLength && seatColumn+i<columnLength; i++) {
            if('#' == seating[seatRow+i][seatColumn+i]){
                count++;
                break;
            }
            if('L' == seating[seatRow+i][seatColumn+i]){
                break;
            }
        }


        return count;
    }

}
