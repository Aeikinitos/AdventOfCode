package advent2020.day8;

import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
class HandheldHaltingTest {

    @Test
    void executeInstruction() {
        // setup
        HandheldHalting handheldHalting = new HandheldHalting();

        // test & validations
        int actualInstructionPointer = handheldHalting.executeAndMoveInstruction(Instruction.fromString("acc +1"));
        Assertions.assertEquals(1, actualInstructionPointer);
        Assertions.assertEquals(1, handheldHalting.getAccumulator());

        actualInstructionPointer = handheldHalting.executeAndMoveInstruction(Instruction.fromString("jmp +2"));
        Assertions.assertEquals(3, actualInstructionPointer);
        Assertions.assertEquals(1, handheldHalting.getAccumulator());

        actualInstructionPointer = handheldHalting.executeAndMoveInstruction(Instruction.fromString("nop +2"));
        Assertions.assertEquals(4, actualInstructionPointer);
        Assertions.assertEquals(1, handheldHalting.getAccumulator());

        actualInstructionPointer = handheldHalting.executeAndMoveInstruction(Instruction.fromString("jmp -1"));
        Assertions.assertEquals(3, actualInstructionPointer);
        Assertions.assertEquals(1, handheldHalting.getAccumulator());

    }

    @Test
    void executeInstructions() {
        // setup
        HandheldHalting handheldHalting = new HandheldHalting();

        Instruction[] instructions = new Instruction[] {
                        Instruction.fromString("nop +0"),
                        Instruction.fromString("acc +1"),
                        Instruction.fromString("jmp +4"),
                        Instruction.fromString("acc +3"),
                        Instruction.fromString("jmp -3"),
                        Instruction.fromString("acc -99"),
                        Instruction.fromString("acc +1"),
                        Instruction.fromString("jmp -4"),
                        Instruction.fromString("acc +6"),
        };
        handheldHalting.setInstructions(instructions);

        handheldHalting.executeInstructions();
        long accumulatorValue = handheldHalting.getAccumulator();
        Assertions.assertEquals(5, accumulatorValue);
    }

    @Test
    public void part1(){
        List<String> instructionLines = readLines("src/test/resources/advent2020/day8/input");
        Instruction[] instructions = instructionLines.stream().map(Instruction::fromString).toArray(Instruction[]::new);

        HandheldHalting handheldHalting = new HandheldHalting();
        handheldHalting.setInstructions(instructions);

        handheldHalting.executeInstructions();
        long accumulatorValue = handheldHalting.getAccumulator();
        System.out.println(accumulatorValue);
    }

    @Test
    void executeInstructionsWithInstructionFunction() {
        // setup
        HandheldHaltingWithUndo handheldHalting = new HandheldHaltingWithUndo();

        InstructionFunction[] instructions = new InstructionFunction[] {
                        InstructionFactory.fromString("nop +0"),
                        InstructionFactory.fromString("acc +1"),
                        InstructionFactory.fromString("jmp +4"),
                        InstructionFactory.fromString("acc +3"),
                        InstructionFactory.fromString("jmp -3"),
                        InstructionFactory.fromString("acc -99"),
                        InstructionFactory.fromString("acc +1"),
                        InstructionFactory.fromString("jmp -4"),
                        InstructionFactory.fromString("acc +6"),
        };
        //handheldHalting.setInstructions(instructions);
        // state {accumulator, nextInstructionPointer}
        State state = new State(0,0);

        // stack of <StateBeforeInstruction,  Instruction>
        Stack<Pair<State, InstructionFunction>> instructionsHistory = new Stack<>();

        state = handheldHalting.executeInstructions(instructions, instructionsHistory, state );
        long accumulatorValue = state.getAccumulator();
        Assertions.assertEquals(5, accumulatorValue);
    }

    @Test
    public void part1WithInstructionFunction(){
        List<String> instructionLines = readLines("src/test/resources/advent2020/day8/input");
        InstructionFunction[] instructions = instructionLines.stream().map(InstructionFactory::fromString).toArray(InstructionFunction[]::new);
        // state {accumulator, nextInstructionPointer}
        State state = new State(0,0);
        // stack of <StateBeforeInstruction,  Instruction>
        Stack<Pair<State, InstructionFunction>> instructionsHistory = new Stack<>();

        HandheldHaltingWithUndo handheldHaltingWithUndo = new HandheldHaltingWithUndo();
//        handheldHalting2.setInstructions(instructions);
        state = handheldHaltingWithUndo.executeInstructions(instructions, instructionsHistory, state);

        long accumulatorValue = state.getAccumulator();
        System.out.println(accumulatorValue);
    }

    @Test
    public void executeAndFixInstructionsWithInstructionFunction(){
        // setup
        HandheldHaltingWithUndo handheldHalting = new HandheldHaltingWithUndo();

        InstructionFunction[] instructions = new InstructionFunction[] {
                        InstructionFactory.fromString("nop +0"),
                        InstructionFactory.fromString("acc +1"),
                        InstructionFactory.fromString("jmp +4"),
                        InstructionFactory.fromString("acc +3"),
                        InstructionFactory.fromString("jmp -3"),
                        InstructionFactory.fromString("acc -99"),
                        InstructionFactory.fromString("acc +1"),
                        InstructionFactory.fromString("jmp -4"),
                        InstructionFactory.fromString("acc +6"),
        };
//        handheldHalting.setInstructions(instructions);
        // state {accumulator, nextInstructionPointer}
        State state = new State(0,0);
        // stack of <StateBeforeInstruction,  Instruction>
        Stack<Pair<State, InstructionFunction>> instructionsHistory = new Stack<>();

        state = handheldHalting.executeAndFixInstructions(instructions, instructionsHistory, state);
        long accumulatorValue = state.getAccumulator();
        Assertions.assertEquals(8, accumulatorValue);
    }

    @Test
    public void part2WithInstructionFunction(){
        List<String> instructionLines = readLines("src/test/resources/advent2020/day8/input");
        InstructionFunction[] instructions = instructionLines.stream().map(InstructionFactory::fromString).toArray(InstructionFunction[]::new);
        // state {accumulator, nextInstructionPointer}
        State state = new State(0,0);
        // stack of <StateBeforeInstruction,  Instruction>
        Stack<Pair<State, InstructionFunction>> instructionsHistory = new Stack<>();

        HandheldHaltingWithUndo handheldHaltingWithUndo = new HandheldHaltingWithUndo();
//        handheldHalting2.setInstructions(instructions);
        state = handheldHaltingWithUndo.executeAndFixInstructions(instructions, instructionsHistory, state);

        long accumulatorValue = state.getAccumulator();
        System.out.println(accumulatorValue);
    }

    /* *************************************** */

    protected List<String> readLines(String filename){
        List<String> lines = Collections.emptyList();
        try
        {
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            sc.useDelimiter("\\Z");

            lines = Arrays.stream(sc.next().split("\\n")).collect(Collectors.toList());

        }
        catch (IOException e)
        {
            // do something
            e.printStackTrace();
        }
        return lines;
    }
}