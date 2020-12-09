package advent2020.day8;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Optional;
import java.util.Stack;



/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
@RequiredArgsConstructor
public class HandheldHalting {

    @Getter
    private long accumulator = 0;
    private int instructionPointer = 0;
    @Setter
    private Instruction[] instructions;
    private Stack<Integer> instructionsHistory = new Stack<>();

    /*
    Executes an instruction and returns the next instruction pointer
     */
    public int executeAndMoveInstruction(Instruction instruction){
        switch (instruction.getCommand()) {
            case acc:
                adjustAccumulator(instruction.getArgument());
                adjustInstructionPointer(+1);
                break;

            case jmp:
                adjustInstructionPointer(instruction.getArgument());
                break;

            case nop:
                adjustInstructionPointer(+1);
                break;

        }
        return  instructionPointer;
    }

    public int executeInstructions() {
        boolean loopDetected = false;
        while(!loopDetected){
            int nextInstructionPointer = instructionPointer;
            if(alreadyExecuted(nextInstructionPointer)){
                // exit
                loopDetected = true;
            } else {
                addInstructionToHistory(nextInstructionPointer);

                Optional<Instruction> optionalInstruction = getInstruction(nextInstructionPointer);
                if(optionalInstruction.isPresent()){
                    executeAndMoveInstruction(optionalInstruction.get());
                } else {
                    // exit status normal
                    return 0;
                }
            }
        }

        // exit status
        return -1;
    }


    public void undo(){

    }

    public int fixExecution(){

        // do
        // find last instruction that is jmp or nop
        // undo it
        // switch it
        // run from history with new set of rules.

        // while exit==-1 find next jmp/nop command from the history and repeat


        // for all executed instructions
        for (int historicalInstructionPointer : instructionsHistory) {
            Instruction instruction = getInstruction(historicalInstructionPointer).get();
            // if they are jmp/nop
            if(Command.jmp.equals(instruction.getCommand()) || Command.nop.equals(instruction.getCommand())){

            }
        }

        // switch them
        // run from that point
        // if exitStatus==0 exit

        instructionsHistory.forEach(historicalPointer ->
        {
            Instruction instruction = getInstruction(historicalPointer).get();

        });

        return 0;
    }

    private int findLastInstructionWithJMPNOP(){
        for (int historicalInstructionPointer : instructionsHistory) {
            Instruction instruction = getInstruction(historicalInstructionPointer).get();
            if(Command.jmp.equals(instruction.getCommand()) || Command.nop.equals(instruction.getCommand())){

            }
        }

        return -1;

    }

    private void adjustAccumulator(int argument){
        this.accumulator += argument;
    }

    private void adjustInstructionPointer(int argument){
        this.instructionPointer += argument;
    }

    private Optional<Instruction> getInstruction(int instructionPointer){
        if(instructionPointer < 0 || instructionPointer >= instructions.length){
            return Optional.empty();
        }

        return Optional.of(instructions[instructionPointer]);
    }

    private void addInstructionToHistory(int instructionPointer){
        instructionsHistory.add(instructionPointer);
    }

    private boolean alreadyExecuted(int instructionPointer){
        return instructionsHistory.contains(instructionPointer);
    }
}

