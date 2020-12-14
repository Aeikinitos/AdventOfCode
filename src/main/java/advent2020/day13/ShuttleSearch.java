/*
 * (c) Copyright 2016 Brite:Bill Ltd.
 *
 * 7 Grand Canal Street Lower, Dublin 2, Ireland
 * info@britebill.com
 * +353 1 661 9426
 */
package advent2020.day13;

import lombok.AllArgsConstructor;

import java.util.List;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class ShuttleSearch {

    public static void findBusID(List<Integer> busIds, Integer earliestArrival){
        Integer time = earliestArrival;
        Integer earliestDeparture = null;
        Integer earliestBus = null;
        do {
            for (Integer busId : busIds) {
                if( time%busId == 0){
                    earliestDeparture = time;
                    earliestBus = busId;
                    break;
                }
            }
            time++;
        } while(earliestDeparture == null);

        System.out.println((earliestDeparture-earliestArrival)*earliestBus);
    }

    public static long findEarliestTimeStamp(List<String> busIds){

        // get first bus, and for each multiple of it
        // check the next bus by
        // getting the time, add it by the offset index from the first element and
        // check if its % busid is 0
        // if its true then go on to the next else, go to next multiple of the first bus

        Integer busOne = Integer.valueOf(busIds.get(0));
        long iteration = 1;
        boolean sequenceIsValid = true;
        long targetTime;
        do {
            targetTime = busOne * iteration;
            sequenceIsValid = true;
            for (int i = 1; i < busIds.size(); i++) {
                if(validBusSchedule(targetTime+i, busIds.get(i))){
                    continue;
                } else {
                    sequenceIsValid = false;
                    break;
                }
            }

            iteration++;
        } while (!sequenceIsValid);

        return targetTime;

    }

    private static boolean validBusSchedule(long targetTime, String busId) {
        if("x".equals(busId)){
            return true;
        }

        return targetTime % Integer.valueOf(busId) == 0;
    }


    public static long findEarliestTimeForSchedule(List<Combo> busIds){
        if(busIds.size()==1){
            return busIds.get(0).offset*-1;
        }
        Combo bus1 = busIds.remove(0);
        Combo bus2 = busIds.remove(0);
        long numberToMultipleX = solveByY(bus1, bus2);

        // combine the busses to a new buss that meets on the same point in time and the offset is the first meeting point but from the start forward and not vice versa
        busIds.add(new Combo(bus1.busId*bus2.busId, (-1*(numberToMultipleX*bus1.busId-bus1.offset))));

        return findEarliestTimeForSchedule(busIds);

    }

    /*
    Find the earliest time the 2 busses meet their required schedule
     */
    private static long solveByY(Combo x, Combo y){
        long iteration = 1L;
        do {
            // yid*y + yoffset = xid*x + xoffset
            // solve by y
            // y = (xid*x - xoffset + yoffset) / yid
            if((x.busId * iteration - x.offset + y.offset) % y.busId == 0){
                return iteration;
            }
            iteration++;
        } while (true);

    }

}

@AllArgsConstructor
class Combo {
    long busId;
    long offset;
}
