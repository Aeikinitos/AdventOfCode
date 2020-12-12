package advent2020.day12;

import java.util.List;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class RainRisk {

    public static ShipState applyInstructions(ShipState state, List<NavigationInstruction> instructions){
        instructions.stream().forEach(state::applyInstruction);
        return state;
    }

    public static int manhattanDistance(ShipState state){
        return Math.abs(state.N)+Math.abs(state.E);
    }
}

