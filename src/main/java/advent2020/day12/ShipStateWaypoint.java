package advent2020.day12;

import lombok.Data;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
@Data
class ShipStateWaypoint extends ShipState {

    double waypointN = 0;
    double waypointE = 0;

    public ShipStateWaypoint(double waypointN, double waypointE) {
        this.waypointN = waypointN;
        this.waypointE = waypointE;
    }

    public ShipStateWaypoint(int N, int E, int rotation, double waypointN, double waypointE) {
        super(N, E, rotation);
        this.waypointN = waypointN;
        this.waypointE = waypointE;
    }

    @Override
    void applyInstruction(NavigationInstruction instruction){
        switch (instruction.direction){
            case 'N':
                waypointN += instruction.amount;
                break;
            case 'S':
                waypointN -= instruction.amount;
                break;
            case 'E':
                waypointE += instruction.amount;
                break;
            case 'W':
                waypointE -= instruction.amount;
                break;
            case 'L':

                double vertical = error(Math.sin(toRadians(instruction.amount)));
                double horizontal = error(Math.cos(toRadians(instruction.amount)));

                double newWaypointN = waypointN*horizontal + waypointE*vertical;
                waypointE = - waypointN*vertical + waypointE*horizontal;
                waypointN = newWaypointN;
                break;
            case 'R':
                vertical = error(Math.sin(toRadians(-instruction.amount)));
                horizontal = error(Math.cos(toRadians(-instruction.amount)));

                newWaypointN = waypointN*horizontal + waypointE*vertical;
                waypointE = - waypointN*vertical + waypointE*horizontal;
                waypointN = newWaypointN;
                break;
            case 'F':
                N += instruction.amount * waypointN;
                E += instruction.amount * waypointE;
                break;
        }
    }
}
