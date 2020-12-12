package advent2020.day12;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
@Data
@AllArgsConstructor
class NavigationInstruction {

    char direction;
    int amount;

    static NavigationInstruction fromString(String input){
        return new NavigationInstruction(input.charAt(0), Integer.parseInt(input.substring(1)));
    }
}
