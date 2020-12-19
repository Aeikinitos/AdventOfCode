package advent2020.day17;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
@Data
public class ConwayCubes4D {

    Set<Coordinates4D> activeCoordinates = new HashSet<>();

    public ConwayCubes4D(List<String> initialState) {
        for (int x = 0; x < initialState.size(); x++) {
            char[] chars = initialState.get(x).toCharArray();
            for (int y = 0; y < chars.length; y++) {
                if (chars[y]=='#'){
                    activeCoordinates.add(new Coordinates4D(x, y,0, 0));
                }
            }
        }
    }

    public void nextCycle(){
        Set<Coordinates4D> nextState = new HashSet<>();

        for (Coordinates4D coord : activeCoordinates) {
            // check self
            if(coord.nextState(activeCoordinates)){
                nextState.add(coord);
            }
            // check neighbhrs
            Set<Coordinates4D> neighbhors = coord.getNeighbhors();
            for (Coordinates4D neighbhor : neighbhors) {
                if(neighbhor.nextState(activeCoordinates)){
                    nextState.add(neighbhor);
                }
            }
        }

        activeCoordinates = nextState;
    }
}


@Data
@AllArgsConstructor
class Coordinates4D {
    int x;
    int y;
    int z;
    int w;

    public Set<Coordinates4D> getNeighbhors(){
        Set<Coordinates4D> neighbhors = new HashSet<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 2; l++) {
                        if(i==0&&j==0&&k==0&&l==0) continue; // skip self
                        neighbhors.add(new Coordinates4D(x+i, y+j, z+k, w+l));
                    }
                }
            }
        }
        return neighbhors;
    }

    // returns true is activating
    //If a cube is active and exactly 2 or 3 of its neighbors are also active, the cube remains active. Otherwise, the cube becomes inactive.
    //If a cube is inactive but exactly 3 of its neighbors are active, the cube becomes active. Otherwise, the cube remains inactive.
    public boolean nextState(Set<Coordinates4D> activeCoordinates){
        long count = getNeighbhors().stream().filter(activeCoordinates::contains).count();
        if(activeCoordinates.contains(this)){
            // active cube
            if(count == 2 || count == 3){
                return true;
            } else {
                return false;
            }
        }  else if(count == 3) {
            // inactive cuve with 3 active neighbhors
            return true;

        }
        return false;
    }
}
