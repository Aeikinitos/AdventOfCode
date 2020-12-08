/*
 * (c) Copyright 2016 Brite:Bill Ltd.
 *
 * 7 Grand Canal Street Lower, Dublin 2, Ireland
 * info@britebill.com
 * +353 1 661 9426
 */
package advent2020.day8;

import lombok.Data;
import lombok.NonNull;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
@Data
class Instruction {

    @NonNull
    private Command command;

    @NonNull
    private int argument;

    public static Instruction fromString(String intruction){
        String[] split = intruction.split(" ");
        return new Instruction(Command.valueOf(split[0]), Integer.valueOf(split[1]));
    }
}
