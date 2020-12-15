package advent2020.day15;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class RambunctiousRecitation {

    // lastNumbers turn
    long turn;
    long lastNumber;
    // number, last turn it was spoken
    Map<Long, Long> memory = new HashMap();

    public long nextNumber(){
        if(memory.containsKey(lastNumber)){
            Long lastTurn = memory.get(lastNumber);
            memory.put(lastNumber, turn);
            lastNumber = turn-lastTurn;
        } else {
            memory.put(lastNumber, turn);

            lastNumber = 0L;
        }
        turn ++;
        return lastNumber;
    }

    public void loadInitialData(List<Long> initial){
        for (int i = 0; i < initial.size()-1; i++) {
            memory.put(initial.get(i), i+1L);
        }
        lastNumber = initial.get(initial.size()-1);
        turn = initial.size();

    }
}
