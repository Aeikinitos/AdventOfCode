package advent2020.day12;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class ShipState {

    int N = 0;
    int E = 0;

    // 0 is looking E, 90 is looking N
    int rotation = 0;

    void applyInstruction(NavigationInstruction instruction){
        switch (instruction.direction){
            case 'N':
                N += instruction.amount;
                break;
            case 'S':
                N -= instruction.amount;
                break;
            case 'E':
                E += instruction.amount;
                break;
            case 'W':
                E -= instruction.amount;
                break;
            case 'L':
                rotation += instruction.amount;
                break;
            case 'R':
                rotation -= instruction.amount;
                break;
            case 'F':
                double vertical = error(Math.sin(toRadians(rotation)));
                double horizontal = error(Math.cos(toRadians(rotation)));
                N += instruction.amount * vertical;
                E += instruction.amount * horizontal;
                break;
        }
    }

    double toRadians (int angle) {
        return angle * (Math.PI / 180);
    }

    double error(double input){
        return Math.abs(input) < 0.1 ? 0 : input;
    }
}
