/*
 * (c) Copyright 2016 Brite:Bill Ltd.
 *
 * 7 Grand Canal Street Lower, Dublin 2, Ireland
 * info@britebill.com
 * +353 1 661 9426
 */
package advent2019.day1;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class FuelCounterUpper {

    public long calculateForInput(List<Integer> lines){
        long sum = 0;
        for (int line : lines) {
            sum += calculateFuelForModule(line);
        }

        return sum;
    }

    public long calculateFuelForInputInclFuel(List<Integer> lines){
        long sum = 0;
        for (int line : lines) {
            long moduleFuel = calculateFuelForModule(line);
            sum += moduleFuel + calculateForFuel(moduleFuel);
        }

        return sum;
    }

    public long calculateForFuel(long fuel){
        System.out.println("calculate fuel for: "+fuel);
        if (fuel <= 0){
            return 0;
        }

        long fuelForFuel = Math.max(0, calculateFuelForModule(fuel));
        long sum = fuelForFuel + calculateForFuel(fuelForFuel);

        return sum;
    }

    public long calculateFuelForModule(long mass){
        return (long)Math.floor(mass/3)-2;
    }


}

