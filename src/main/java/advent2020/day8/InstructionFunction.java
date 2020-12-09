/*
 * (c) Copyright 2016 Brite:Bill Ltd.
 *
 * 7 Grand Canal Street Lower, Dublin 2, Ireland
 * info@britebill.com
 * +353 1 661 9426
 */
package advent2020.day8;

import lombok.*;

import java.util.function.Function;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
abstract class InstructionFunction implements Function<State, State> {

    public int argument;

    @Getter @Setter
    private int instructionIndex;

    public InstructionFunction(int argument) {
        this.argument = argument;
    }

    public boolean hasIndex(int index) {
        return instructionIndex == index;
    }

    public abstract InstructionFunction swap();
}

class ACCInstruction extends InstructionFunction {

    public ACCInstruction(int argument) {
        super(argument);
    }

    @Override
    public State apply(State state) {
        return new State(state.accumulator+argument, state.instructionPointer+1);
    }

    @Override
    public InstructionFunction swap() {
        return this;
    }
}

class JMPInstruction extends InstructionFunction {

    public JMPInstruction(int argument) {
        super(argument);
    }

    @Override
    public State apply(State state) {
        return new State(state.accumulator, state.instructionPointer+argument);
    }

    public NOPInstruction swap(){
        return new NOPInstruction(argument);
    }
}

class NOPInstruction extends InstructionFunction {

    public NOPInstruction(int argument) {
        super(argument);
    }

    @Override
    public State apply(State state) {
        return new State(state.accumulator, state.instructionPointer+1);
    }

    public JMPInstruction swap(){
        return new JMPInstruction(argument);
    }
}


class InstructionFactory {
    public static InstructionFunction fromString(String intruction){
        String[] split = intruction.split(" ");
        switch(Command.valueOf(split[0])){
            case nop: return new NOPInstruction(Integer.valueOf(split[1]));
            case jmp: return new JMPInstruction(Integer.valueOf(split[1]));
            case acc: return new ACCInstruction(Integer.valueOf(split[1]));
            // not reachable in this context
            default: return null;
        }
    }
}

@Data
@RequiredArgsConstructor
class State {

    @NonNull
    int accumulator;
    @NonNull
    int instructionPointer;

    int exitStatus = -1;
}