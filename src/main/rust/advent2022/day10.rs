use std::fs::read_to_string;

const MAX_CYCLES: i32 = 240;
const INTERESTING_CYCLES: [i32; 6] = [20, 60, 100, 140, 180, 220];
const CRT_LINE_SIZE: i32 = 40;

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

struct CRT {
    lines: Vec<Vec<char>>
}

impl CRT {
    pub fn new() -> Self {
        Self {
            lines: vec![vec!['.';40];6],
        }
    }

    fn draw(&mut self, drawing_position: i32, sprite_mid_point: i32) {
        let row = drawing_position / CRT_LINE_SIZE;
        let column = drawing_position % CRT_LINE_SIZE;

        if sprite_mid_point-1<= column && column <= sprite_mid_point+1 {
            self.lines[row as usize][column as usize] = '#';
        }
    }

    fn print(&self) {
        for row in 0..6 {
            for column in 0..40 {
                print!("{}", self.lines[row][column]);
            }
            println!();
        }
    }
}

fn main() {
    let contents = read_to_string("../../../../inputs/advent2022/day10")
        .expect("Should have been able to read the file");

    let mut cpu = CPU {
        instructions: contents.lines().map(to_instruction).rev().collect::<Vec<_>>(),
        processing: None,
        register: 1
    };

    let mut crt = CRT::new();

    let mut signal = 0;
    for cycle in 1..MAX_CYCLES {
        start_of_cycle(&mut cpu);
        during_cycle(&mut cpu, cycle, &mut signal, &mut crt);
        end_of_cycle(&mut cpu);
    }

    println!("Part 1 {signal}");
    println!("Part 2:");
    crt.print();
}

fn start_of_cycle(cpu: &mut CPU) {
    match cpu.processing {
        Some(_instruction) => {} // already processing, dont take another job
        None => {cpu.processing = cpu.instructions.pop()}
    }
}

fn during_cycle(cpu: &mut CPU, cycle: i32, signal: &mut i32, crt: &mut CRT) {

    if INTERESTING_CYCLES.contains(&cycle){
        *signal += cycle * cpu.register;
    }

    crt.draw(cycle-1, cpu.register);
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