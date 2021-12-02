package advent2021.day2;

import java.util.List;

public class Dive {

    protected long calculatePositionProduct(List<String> directions, Coordinate position){
        directions.forEach(position::moveDirection);
        return position.product();
    }
}

interface Coordinate {
    void moveDirection(String instruction);

    long product();
}

class XYCoordinate implements Coordinate {
    int horizontal;
    int depth;

    public void moveDirection(String instruction){
        String[] split = instruction.split(" ");
        String direction = split[0];
        int delta = Integer.valueOf(split[1]);
        switch (direction){
            case "forward":
                horizontal += delta;
                break;
            case "down":
                depth += delta;
                break;
            case "up":
                depth -= delta;
                break;
        }
    }

    public long product() {
        return horizontal * depth;
    }
}


/**
 * down X increases your aim by X units.
 * up X decreases your aim by X units.
 * forward X does two things:
 * * It increases your horizontal position by X units.
 * * It increases your depth by your aim multiplied by X.
 */
class AimingCoordinate implements Coordinate {
    int aim;
    int horizontal;
    int depth;

    public void moveDirection(String instruction){
        String[] split = instruction.split(" ");
        String direction = split[0];
        int delta = Integer.valueOf(split[1]);
        switch (direction){
            case "forward":
                horizontal += delta;
                depth += aim * delta;
                break;
            case "down":
                aim += delta;
                break;
            case "up":
                aim -= delta;
                break;
        }
    }

    public long product() {
        return horizontal * depth;
    }
}
