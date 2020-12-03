package advent2020.day3;

import javafx.util.Pair;

import java.util.function.Function;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class TobogganTrajectory {


    public long countTrees(String[] map, int rightMovement, int downMovement){

        int counter = 0;
        int posX = 0;
        int posY = 0;
        int rowLength = map[0].length();
        while(posY < map.length){

            if(map[posY].charAt(posX) == '#'){
                counter++;
            }

            // next move
            posX = (posX+rightMovement)%rowLength;
            posY += downMovement;
        }
        return counter;
    }
}
