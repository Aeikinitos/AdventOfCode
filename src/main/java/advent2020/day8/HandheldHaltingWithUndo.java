package advent2020.day8;

import javafx.util.Pair;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.Stack;



/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
@RequiredArgsConstructor
public class HandheldHaltingWithUndo {


    public State executeInstructions(InstructionFunction[] instructions, Stack<Pair<State, InstructionFunction>> instructionsHistory, State state) {

        while(true){

            if(alreadyExecuted(state.instructionPointer, instructionsHistory)){
                // exit
                state.setExitStatus(-1);
                return state;
            }

            Optional<InstructionFunction> optionalInstruction = tryGetInstruction(state.instructionPointer, instructions);
            if(!optionalInstruction.isPresent()) {
                // exit status normal
                state.setExitStatus(0);
                return state;
            }

            state = executeInstruction(optionalInstruction.get(), instructionsHistory, state);
        }
    }

    public State executeAndFixInstructions(InstructionFunction[] instructions, Stack<Pair<State, InstructionFunction>> instructionsHistory, State state) {

        state = executeInstructions(instructions, instructionsHistory, state);

        // while not succesfully executed, undo history
        while(state.getExitStatus() == -1){
            Pair<State, InstructionFunction> pop = undoUntilJMPNOP(instructionsHistory);

            // revert state
            state = pop.getKey();
            // swap operation
            int index = pop.getValue().getInstructionIndex();
            instructions[index] = instructions[index].swap();
            // reexecute with new instruction set starting from the same point in history
            state = executeInstructions(instructions, (Stack<Pair<State, InstructionFunction>>) instructionsHistory.clone(), state); // this call shouldn't modify the previous history, we dont want to investigate new paths that might get created from the changed instruction. its as if the execute needs to be stateless
            if(state.getExitStatus() == -1){
                // revert change
                instructions[index] = instructions[index].swap();
            }
            // continue reverting history
        }

        return state;
    }

    private Pair<State, InstructionFunction> undoUntilJMPNOP(Stack<Pair<State, InstructionFunction>> instructionsHistory){
        Pair<State, InstructionFunction> pop;
        do {
            pop = instructionsHistory.pop();
        } while (pop.getValue() instanceof ACCInstruction);

        return pop;
    }

    private Optional<InstructionFunction> tryGetInstruction(int instructionPointer, InstructionFunction[] instructions){
        if(instructionPointer < 0 || instructionPointer >= instructions.length){
            return Optional.empty();
        }

        return Optional.of(instructions[instructionPointer]);
    }

    private State executeInstruction(InstructionFunction instruction, Stack<Pair<State, InstructionFunction>> instructionsHistory, State state){
        instruction.setInstructionIndex(state.instructionPointer);
        addInstructionToHistory(state, instruction, instructionsHistory);
        state = instruction.apply(state);
        return state;
    }


    private void addInstructionToHistory(State newState, InstructionFunction instructionPointer, Stack<Pair<State, InstructionFunction>> instructionsHistory){
        instructionsHistory.add(new Pair(newState, instructionPointer));
    }

    private boolean alreadyExecuted(int instructionPointer, Stack<Pair<State, InstructionFunction>> instructionsHistory){
        return instructionsHistory.stream().map(Pair::getValue).filter(instructionFunction -> instructionFunction.hasIndex(instructionPointer)).count() > 0;
    }
}

