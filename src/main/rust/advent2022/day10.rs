use std::fs::read_to_string;

const MAX_CYCLES: i32 = 221;
const INTERESTING_CYCLES: [i32; 6] = [20, 60, 100, 140, 180, 220];

#[derive(Copy, Clone, Debug)]
enum Instruction {
    ADD(i32, i32), // amount, cycles to complete
    NOOP(i32) // cycles to complete
}


fn to_instruction(line: &str) -> Instruction {
    match line.split(' ').collect::<Vec<_>>()[..] {
        ["addx", amount] => {return Instruction::ADD(amount.parse::<i32>().expect("do you really expect me to add {amount}"), 2)},
        ["noop"] => {return Instruction::NOOP(1)},
        [..] => panic!("what sorcery is this")
    }
}

struct CPU {
    instructions: Vec<Instruction>,
    processing: Option<Instruction>,
    register: i32
}

fn main() {
    let contents = read_to_string("inputs/advent2022/day10")
        .expect("Should have been able to read the file");

    let mut cpu = CPU {
        instructions: contents.lines().map(to_instruction).rev().collect::<Vec<_>>(),
        processing: None,
        register: 1
    };

    let mut signal = 0;
    for cycle in 1..MAX_CYCLES {
        start_of_cycle(&mut cpu);
        during_cycle(&mut cpu, cycle, &mut signal);
        end_of_cycle(&mut cpu);
    }

    println!("Part 1 {signal}");
}

fn start_of_cycle(cpu: &mut CPU) {
    match cpu.processing {
        Some(_instruction) => {} // already processing, dont take another job
        None => {cpu.processing = cpu.instructions.pop()}
    }
}

fn during_cycle(cpu: &mut CPU, cycle: i32, signal: &mut i32) {

    if INTERESTING_CYCLES.contains(&cycle){
        *signal += cycle * cpu.register;
    }
}

fn end_of_cycle(cpu: &mut CPU) {
    match cpu.processing {
        Some(Instruction::ADD(amount, cycles)) => {
            if cycles == 1 {
                // completing action
                cpu.register += amount;
                cpu.processing = None;
            } else {
                cpu.processing = Some(Instruction::ADD(amount, cycles-1));
            }
        }
        Some(Instruction::NOOP(_cycles)) => {
            cpu.processing = None;
        }
        None => {}
    }
}