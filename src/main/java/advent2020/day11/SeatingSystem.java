/*
 * (c) Copyright 2016 Brite:Bill Ltd.
 *
 * 7 Grand Canal Street Lower, Dublin 2, Ireland
 * info@britebill.com
 * +353 1 661 9426
 */
package advent2020.day11;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class SeatingSystem {

    boolean isSeatingUpdated = false;

    public char[][] iterateUntilStable(char[][] seating, Decider sitDown, Decider standUp){

        char[][] newSeatingArrangement = step(seating, sitDown, standUp);
        while(isSeatingUpdated){
//            printSeating(newSeatingArrangement);

            isSeatingUpdated = false;
            newSeatingArrangement = step(newSeatingArrangement, sitDown, standUp);

        }

        return newSeatingArrangement;
    }



    public char[][] step(char[][] seating, Decider sitDown, Decider standUp){

        char[][] newSeatingArrangment = copySeating(seating);

        for (int i = 0; i < seating.length; i++) {
            for (int j = 0; j < seating[0].length; j++) {
                if('L' == seating[i][j]  && sitDown.able(seating, i, j)){
                    newSeatingArrangment[i][j] = '#';
                    isSeatingUpdated = true;
                }
                if('#' == seating[i][j] && standUp.able(seating, i, j)){
                    newSeatingArrangment[i][j] = 'L';
                    isSeatingUpdated = true;
                }
            }
        }

        return newSeatingArrangment;
    }


    public static int countSeats(char[][] seating){
        int count = 0;
        for (int i = 0; i < seating.length; i++) {
            for (int j = 0; j < seating[0].length; j++) {
                if ('#' == seating[i][j]) {
                    count++;
                }
            }
        }
        return count;
    }

    private char[][]  copySeating(char[][] seating){
        char[][] copy = new char[seating.length][];

        for(int i = 0; i < seating.length; i++){
            copy[i] = seating[i].clone();
        }

        return copy;
    }

    private void printSeating(char[][] seating) {
        for (int i = 0; i < seating.length; i++) {
            for (int j = 0; j < seating[0].length; j++) {
                System.out.print(seating[i][j]);
            }
            System.out.println();
        }
        System.out.println("-----------");
    }
}
