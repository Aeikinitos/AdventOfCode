package advent2020.day24;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class LobbyLayout {

    Map<HexCoordinate, HexCoordinate> grid = new HashMap<>();

    public List<HexCoordinate> layTiles(List<String> directions){
        return directions.stream().map(this::createOrGetHexCoord).map(HexCoordinate::switchColor).distinct().sorted()
                         .filter(HexCoordinate::isBlack).collect(Collectors.toList());
    }

    public HexCoordinate createOrGetHexCoord(String directions){
        Queue<Character> directionsQueue = directions.chars().mapToObj(c -> (char)c).collect(Collectors.toCollection(LinkedList::new));
        HexCoordinate coord = new HexCoordinate();
        while (directionsQueue.size() > 0){
            Character i = directionsQueue.poll();
            switch (i) {
                case 's':
                    if( directionsQueue.poll() == 'e'){
                        coord.x++; coord.z--; break;
                    } else {
                        coord.y--; coord.z--; break;
                    }
                case 'n':
                    if( directionsQueue.poll() == 'e'){
                        coord.y++; coord.z++; break;
                    } else {
                        coord.x--; coord.z++; break;
                    }
                case 'e':
                    coord.x++; coord.y++; break;
                case 'w':
                    coord.x--; coord.y--; break;
            }
        }

        return grid.computeIfAbsent(coord, hexCoordinate -> coord);
    }

    public Map<HexCoordinate, HexCoordinate> flipGridTiles() {
        //Any black tile with zero or more than 2 black tiles immediately adjacent to it is flipped to white. (or with 1 or 2 stays black)
        //Any white tile with exactly 2 black tiles immediately adjacent to it is flipped to black.

        Map<HexCoordinate, HexCoordinate> newGrid = new HashMap<>();

        // for each black coord
        grid.keySet().stream()
            .filter(HexCoordinate::isBlack)
            .forEach(blackCoord -> {
            // if zero or more than 2 neighbhors.isBlack, add blackCoord to new grid
            if(between(blackCoord.getNeighbhors().stream()
                         .map(this::fromGridOrNew)
                         .filter(HexCoordinate::isBlack)
                         .count(), 1,2)) {
                newGrid.put(blackCoord, blackCoord);
            }
            // for all neighbhor.isWhite that have exactly 2 neighbhors.isBlack -> add neighbhor.switch() to new grid
            blackCoord.getNeighbhors()
                      .stream()
                      .map(this::fromGridOrNew)
                      .filter(HexCoordinate::isWhite)
                      .filter(whiteTile -> whiteTile
                                      .getNeighbhors()
                                      .stream()
                                      .map(this::fromGridOrNew)
                                      .filter(HexCoordinate::isBlack)
                                      .count() == 2)
                      .forEach(whiteTile -> newGrid.put(whiteTile.clone().switchColor(), whiteTile.clone().switchColor())); // need to switch color
        });

        grid = newGrid;
        return grid;
    }

    public HexCoordinate fromGridOrNew(HexCoordinate coord) {
        return grid.getOrDefault(coord, coord);
    }

    public boolean between(long count, long minInclusive, long maxInclusive){
        return minInclusive <= count && count <= maxInclusive;
    }
}


@Data
@NoArgsConstructor
@AllArgsConstructor
class HexCoordinate implements Comparable, Cloneable {
    int x = 0;
    int y = 0;
    int z = 0;

    boolean isWhite = true;

    public HexCoordinate(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean isBlack(){
        return !isWhite;
    }

    public HexCoordinate switchColor(){
        isWhite = !isWhite;
        return this;
    }

    public List<HexCoordinate> getNeighbhors(){
        return Arrays.asList(
                        new HexCoordinate(x+1,y+1,z),
                        new HexCoordinate(x+1,y,z-1),
                        new HexCoordinate(x-1,y-1,z),
                        new HexCoordinate(x-1,y,z+1),
                        new HexCoordinate(x,y+1,z+1),
                        new HexCoordinate(x,y-1,z-1)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        HexCoordinate that = (HexCoordinate) o;
        return x == that.x && y == that.y && z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public int compareTo(Object o) {
        return 0; // not really comparing
    }

    @Override
    protected HexCoordinate clone() {
        HexCoordinate clone = new HexCoordinate(x, y, z);
        clone.isWhite = clone.isWhite();
        return clone;
    }
}


